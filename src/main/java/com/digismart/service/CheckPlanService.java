package com.digismart.service;

import static com.digismart.util.Util.dateFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.CloseableIterator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.digismart.aop.ExecutionTimeCalculation;
import com.digismart.config.ApplicationProperties;
import com.digismart.model.DomainIP;
import com.digismart.model.IPAddress;
import com.digismart.model.NewsLetter;
import com.digismart.model.PlanningMaster;
import com.digismart.model.PlanningVolume;
import com.digismart.model.SegmentMaster;
import com.digismart.model.User;
import com.digismart.util.Constants;
import com.digismart.util.IPFinderUtils;
import com.digismart.util.MongoConnection;

@Service
public class CheckPlanService {

	private static final Logger logger = LoggerFactory.getLogger(CheckPlanService.class);

	@Autowired
	MongoDBService mongoDBConnection;

	@Autowired
	PlanningVolumeService planningVolumeService;

	@Autowired
	DomainIPBuilder domainIPBuilder;

	@Autowired
	ApplicationProperties applicationProperties;

	@Async("apiAsyncPool")
	@ExecutionTimeCalculation
	public PlanningVolume checkPlan(String planId) {
		PlanningMaster planningMaster = mongoDBConnection.getPlanningMaster(planId);
		PlanningVolume planningVolume = new PlanningVolume();
		try {
			if (planningMaster != null) {
				SegmentMaster segmentMaster = mongoDBConnection
						.getSegmentMaster(planningMaster.getSegmentId().toString());
				planningVolumeService.setSegmentCount(segmentMaster, planningVolume);
				planningVolumeService.setPlanLimits(planningMaster, planningVolume);

				boolean isCollectionExists = mongoDBConnection.isCollectionExists("plan_" + Constants.USER_EMAILS_COL
						+ "_" + dateFormat("yyyyMMdd", planningMaster.getScheduleDate()), MongoConnection.MAIN_DB);
				MongoConnection mongoConnection = isCollectionExists ? MongoConnection.PLANNING_DB
						: MongoConnection.MAIN_DB;

				HashMap<String, DomainIP> domianIps = domainIPBuilder.build(planningMaster.getScheduleDate(),
						mongoConnection, planningMaster.getCampaignType(), planningMaster);

				NewsLetter newsLetter = mongoDBConnection.getNewsLetter(planningMaster.getNewsletterId().toString());
				CloseableIterator<Map> joinedCollectionIt = getJoinedCollectionIt(segmentMaster, isCollectionExists,
						planningMaster);
				applyFilter(domianIps, newsLetter, joinedCollectionIt, planningVolume, planningMaster, mongoConnection);
				mongoDBConnection.updatePlanningStatus(planId, planningVolume, "checked",
						planningMaster.getCampaignType());
			}
		} catch (Exception e) {
			mongoDBConnection.updatePlanningStatus(planId, planningVolume, "failed", planningMaster.getCampaignType());
			logger.debug("error in checking plan {}", e.getMessage());
		}

		return planningVolume;

	}

	private void applyFilter(HashMap<String, DomainIP> domianIps, NewsLetter newsLetter,
			CloseableIterator<Map> joinedCollIt, PlanningVolume planningVolume, PlanningMaster planningMaster,
			MongoConnection mongoConnection) {
		int afterEmailRCount = 0;
		int afterCRestriction = 0;
		Integer afterIPVolumeR = new Integer(0);
		// Query Batch Size
		int batchSizeForCam_re = applicationProperties.getQuery_batch_size();
		ArrayList<User> list = new ArrayList<>();
		while (joinedCollIt.hasNext()) {
			try {
				Map next = joinedCollIt.next();
				if (next.get("email_id") != null && !next.get("email_id").toString().isEmpty()) {
					if (next.get("default_domain") != null && !next.get("default_domain").toString().isEmpty()) {
						User user = new User();
						String defaultDomain = null;
						if (planningMaster.getDomain() != null && !"".equalsIgnoreCase(planningMaster.getDomain())) {
							defaultDomain = planningMaster.getDomain();
						} else {
							defaultDomain = next.get("default_domain").toString();
						}

						// logger.info("default Domain " + defaultDomain);
						DomainIP domainIP = domianIps.get(defaultDomain);
						if (domainIP == null) {
							continue;
						}
						IPFinderUtils ipUtil = domainIP.getIpFinderUtils();
						if (isEmailFrequencyExceed(next, planningMaster.getScheduleDate(), planningVolume,
								planningMaster.getCampaignType())) {
							logger.debug("isEmailFrequencyExceed :  " + next.get("email_id").toString());
							continue;
						}
						afterEmailRCount++;
						user.setDomain(domainIP.getDomain().getDomainName());
						user.setEmailId(next.get("email_id").toString());
						user.setDomainIP(domainIP);
						user.setIpUtil(ipUtil);
						list.add(user);

						if (afterEmailRCount % batchSizeForCam_re == 0) {
							List<User> cRestrictionOK = mongoDBConnection.applyCampaignRestriction(newsLetter, list,
									planningMaster.getScheduleDate(), mongoConnection,
									planningMaster.getCampaignType());
							// .debug("cRestrictionOK : " + cRestrictionOK);
							afterCRestriction = (afterCRestriction + cRestrictionOK.size());
							list = new ArrayList<>();
							List<User> filterIPVolume = filterIPVolume(cRestrictionOK);
							afterIPVolumeR = (afterIPVolumeR + filterIPVolume.size());
							// logger.debug("filterIPVolume : " + filterIPVolume);
						}
						if (planningVolume.getScheduleCount() <= afterIPVolumeR) {
							break;
						}

					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		if (list.size() > 0) {
			List<User> cRestrictionOK = mongoDBConnection.applyCampaignRestriction(newsLetter, list,
					planningMaster.getScheduleDate(), mongoConnection, planningMaster.getCampaignType());
			// logger.debug("cRestrictionOK : " + cRestrictionOK);
			afterCRestriction = (afterCRestriction + cRestrictionOK.size());
			list = new ArrayList<>();
			List<User> filterIPVolume = filterIPVolume(cRestrictionOK);
			afterIPVolumeR = (afterIPVolumeR + filterIPVolume.size());
			// logger.debug("filterIPVolume : " + filterIPVolume);
		}
		logger.debug("afterEmailRCount ::: " + afterEmailRCount);
		logger.debug("afterCRestriction ::: " + afterCRestriction);
		logger.debug("afterIPVolumeR ::: " + afterIPVolumeR);

		PlanningVolume buildPlanningVolume = buildPlanningVolume(planningVolume, afterEmailRCount, afterCRestriction,
				afterIPVolumeR);

		logger.debug("buildPlanningVolume ::: " + buildPlanningVolume);
	}

	protected List<User> filterIPVolume(List<User> cRestrictionOK) {
		IPAddress ip = null;
		List<User> afterIpFilter = new ArrayList<>();
		for (User user : cRestrictionOK) {
			IPFinderUtils ipUtil = user.getIpUtil();
			DomainIP domainIP = user.getDomainIP();
			while (!ipUtil.isEmpty()) {
				ip = getNextIP(ipUtil);
				if (ip != null) {
					break;
				}
			}
			if (ip == null) {
				continue;
			}
			user.setIpAddress(ip);
			user.setDomainIP(domainIP);
			user.setIp(ip.getIpaddress());
			user.setInfra(ip.getInfra());
			afterIpFilter.add(user);
		}
		return afterIpFilter;
	}

	private PlanningVolume buildPlanningVolume(PlanningVolume planningVolume, int afterEmailRCount,
			int afterCRestriction, int afterIPVolumeR) {
		if (planningVolume.getScheduleCount() <= afterIPVolumeR) {
			planningVolume.setAvailableVolume(planningVolume.getScheduleCount());
			planningVolume.setFilterCampaignRestriction(0);
			planningVolume.setFilterUserCapping(0);
			planningVolume.setFilterVolumeRestriction(0);
		} else {
			planningVolume.setAvailableVolume(afterIPVolumeR);
			if (afterEmailRCount > planningVolume.getScheduleCount()) {
				afterEmailRCount = Integer.parseInt(planningVolume.getScheduleCount() + "");
			}

			long filterUserCapping = (planningVolume.getScheduleCount() - afterEmailRCount);
			planningVolume.setFilterUserCapping(filterUserCapping);
			if (planningVolume.getScheduleCount() > planningVolume.getSegmentTotalCount()) {
				filterUserCapping = (planningVolume.getSegmentTotalCount() - afterEmailRCount);
				planningVolume.setFilterUserCapping(filterUserCapping);
			}
			Integer filteCount = afterEmailRCount - afterCRestriction;
			logger.debug("filter Count    " + filteCount);
			planningVolume.setFilterCampaignRestriction(filteCount < 0 ? 0 : filteCount);
			planningVolume.setFilterVolumeRestriction((afterCRestriction - afterIPVolumeR));

		}
		return planningVolume;
	}

	protected boolean isEmailFrequencyExceed(Map row, String date, PlanningVolume pv, String campaignType) {
		try {
			if (row.containsKey("count_info") && row.get("count_info") != null
					&& ((row.get("count_info") instanceof Map))) {
				Map countInfo = (Map) row.get("count_info");
				String scheduleDate = countInfo.get("schedule_date").toString();
				Integer countValue = (Integer) countInfo.get(campaignType + "_count");
				long count = Long.parseLong(countValue != null ? countValue.toString() : "0");
				if (date.equals(scheduleDate) && count >= pv.getMailLimitUser()) {
					logger.debug("isEmailFrequencyExceed :  " + date + " count : " + count);
					return true;
				}
			}
			if (row.containsKey("count_info") && row.get("count_info") != null
					&& ((row.get("count_info") instanceof ArrayList))) {
				ArrayList<Map> countInfoList = (ArrayList<Map>) row.get("count_info");
				boolean isEmailFrequencyExceed = false;
				for (Map countInfo : countInfoList) {
					String scheduleDate = countInfo.get("schedule_date").toString();
					Integer countValue = (Integer) countInfo.get(campaignType + "_count");
					long count = Long.parseLong(countValue != null ? countValue.toString() : "0");
					if (date.equals(scheduleDate) && count >= pv.getMailLimitUser()) {
						logger.debug("isEmailFrequencyExceed :  " + date + " count : " + count);
						isEmailFrequencyExceed = true;
						break;
					}
				}
				return isEmailFrequencyExceed;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	protected CloseableIterator<Map> getJoinedCollectionIt(SegmentMaster segmentMaster, boolean isCollectionExists,
			PlanningMaster planningMaster) {
		if (!isCollectionExists) {
			CloseableIterator<Map> joinedCollection = mongoDBConnection.joinUserCappingCollection(segmentMaster,
					MongoConnection.MAIN_DB, planningMaster);
			return joinedCollection;
		} else {
			CloseableIterator<Map> joinedCollection = mongoDBConnection.joinUserCappingCollection(segmentMaster,
					MongoConnection.PLANNING_DB, planningMaster);

			return joinedCollection;
		}
	}

	protected IPAddress getNextIP(IPFinderUtils ipUtil) {
		synchronized (ipUtil) {
			IPAddress ip = ipUtil.next();
			// logger.debug("IP ...." + ip);
			if (ip == null) {
				logger.debug("No ip avilable ....");
				return null;
			}
			if (ip.getCount() < ip.getMaximumVolume() || ip.getMaximumVolume() == -1) {
				ip.setCount(ip.getCount() + 1);
			} else {
				ipUtil.remove(ip);
				return null;
			}
			return ip;
		}

	}
}
