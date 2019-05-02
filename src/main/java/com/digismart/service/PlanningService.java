package com.digismart.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import com.digismart.model.CampaignInfo;
import com.digismart.model.DomainIP;
import com.digismart.model.MailData;
import com.digismart.model.NewsLetter;
import com.digismart.model.PlannedCampaign;
import com.digismart.model.PlanningMaster;
import com.digismart.model.PlanningVolume;
import com.digismart.model.ScheduleCampaign;
import com.digismart.model.User;
import com.digismart.util.BulkMailData;
import com.digismart.util.IPFinderUtils;
import com.digismart.util.MongoConnection;
import com.digismart.util.RestConnector;
import com.digismart.util.Util;

@Service
public class PlanningService {

	private static final Logger logger = LoggerFactory.getLogger(PlanningService.class);

	@Autowired
	CheckPlanService checkPlanService;
	@Autowired
	MongoDBService mongoDBConnection;
	@Autowired
	DomainIPBuilder domainIPBuilder;
	@Autowired
	PlanningVolumeService planningVolumeService;
	@Autowired
	RestConnector restConnector;

	@Async("applyFiltersPool")
	public Future<List<User>> applyFilters(List<Map> rows, HashMap<String, DomainIP> domianIps, NewsLetter newsLetter,
			PlanningVolume planningVolume, PlanningMaster planningMaster, MongoConnection mongoConnection,
			AtomicLong afterEmailRCount, AtomicLong afterCRestriction, AtomicLong afterIPVolumeR) {
		ArrayList<User> list = new ArrayList<>();
		for (Map next : rows) {
			if (next.get("email_id") != null && !next.get("email_id").toString().isEmpty()) {
				if (next.get("default_domain") != null && !next.get("default_domain").toString().isEmpty()) {
					User user = new User();

					String defaultDomain = null;
					if (planningMaster.getDomain() != null && !"".equalsIgnoreCase(planningMaster.getDomain())) {
						defaultDomain = planningMaster.getDomain();
					} else {
						defaultDomain = next.get("default_domain").toString();
					}

					DomainIP domainIP = domianIps.get(defaultDomain);
					if (domainIP == null) {
						continue;
					}
					IPFinderUtils ipUtil = domainIP.getIpFinderUtils();
					if (checkPlanService.isEmailFrequencyExceed(next, planningMaster.getScheduleDate(), planningVolume,
							planningMaster.getCampaignType())) {
						continue;
					}
					afterEmailRCount.incrementAndGet();
					user.setDomain(domainIP.getDomain().getDomainName());
					String name = "";
					String mobileNo = "";
					if (next.get("first_name") != null) {
						name = name + next.get("first_name").toString();
					}
					if (next.get("last_name") != null) {
						name = " " + name + next.get("last_name").toString();
					}
					if (next.get("mobile_no") != null) {
						mobileNo = next.get("mobile_no").toString();
					}
					user.setName(name);
					user.setMobileNo(mobileNo);
					user.setEmailId(next.get("email_id").toString());
					user.setIpUtil(ipUtil);
					list.add(user);
				}

			}
		}
		List<User> cRestrictionOK = mongoDBConnection.applyCampaignRestriction(newsLetter, list,
				planningMaster.getScheduleDate(), mongoConnection, planningMaster.getCampaignType());
		int csize = cRestrictionOK.size();
		afterCRestriction.addAndGet(csize);
		logger.debug("cRestrictionOK Filter:: " + csize);
		List<User> filterIPVolume = checkPlanService.filterIPVolume(cRestrictionOK);
		int isize = filterIPVolume.size();
		afterIPVolumeR.addAndGet(isize);
		logger.debug("filterIPVolumeOK :  " + isize);
		return new AsyncResult<>(filterIPVolume);
	}

	@Async("saveToDbPool")
	public Future<List<User>> saveToDb(Future<List<User>> asyncResult, HashMap<String, DomainIP> domianIps,
			NewsLetter newsLetter, PlanningVolume planningVolume, PlanningMaster planningMaster,
			AtomicLong afterEmailRCount, AtomicLong afterCRestriction, AtomicLong afterIPVolumeR,
			AtomicLong totalSavedtoDB, Lock objectToLock) throws ExecutionException, InterruptedException {
		List<User> emailList = asyncResult.get();

		objectToLock.lock();
		long currentSaved = totalSavedtoDB.get();
		if (currentSaved == planningVolume.getScheduleCount()) {
			emailList = new ArrayList<User>();
		}
		long remainingCount = (planningVolume.getScheduleCount() - currentSaved);
		if (emailList.size() > remainingCount) {
			// long emailsToSave = (emailList.size() - remainingCount);
			int endIndex = Integer.parseInt(((remainingCount) + ""));
			emailList = emailList.subList(0, endIndex);

		}
		totalSavedtoDB.addAndGet(emailList.size());
		objectToLock.unlock();

		logger.debug("Email List For Save:: " + emailList.size());
		List<PlannedCampaign> plannedCampaignLis = new ArrayList<>();
		List<CampaignInfo> campaignInfoList = new ArrayList<>();
		HashMap<String, Integer> ipCountMap = new HashMap<>();
		Date scd = Util.getDate("yyyy-MM-dd", planningMaster.getScheduleDate());
		Date expiryDate = Util.buildCampaignInfoExpiry(Integer.parseInt("" + planningVolume.getCampaignBlockDays()),
				scd);
		for (User user : emailList) {
			PlannedCampaign pc = new PlannedCampaign();
			pc.setDate(planningMaster.getScheduleDate());
			pc.setDomain(user.getDomain());
			pc.setInfra(user.getInfra());
			pc.setEmailId(user.getEmailId());
			pc.setIpAddress(user.getIp());
			pc.setPlanId(new ObjectId(planningMaster.getPlanId()));
			pc.setPort(user.getIpAddress().getPort());
			pc.setSmtpHost(user.getIpAddress().getHost_name());
			pc.setName(user.getName());
			pc.setMobileNo(user.getMobileNo());
			pc.setEmailPrefix(user.getIpAddress().getFrom_email());
			plannedCampaignLis.add(pc);

			CampaignInfo campaignInfo = new CampaignInfo();
			campaignInfo.setAdded_date(planningMaster.getScheduleDate());
			campaignInfo.setCategory_id(newsLetter.getCategory_id());
			campaignInfo.setClient_id(newsLetter.getClient_id());
			campaignInfo.setCustomer_id(newsLetter.getCustomer_id());
			campaignInfo.setEmail_id(user.getEmailId());
			campaignInfo.setSubcategory_id(newsLetter.getSubcategory_id());
			campaignInfo.setExpire_at(expiryDate);
			campaignInfo.setCampaign_type(planningMaster.getCampaignType());
			campaignInfoList.add(campaignInfo);
			if (ipCountMap.containsKey(user.getIp())) {
				int ipCount = ipCountMap.get(user.getIp());
				ipCount = ipCount + 1;
				ipCountMap.put(user.getIp(), ipCount);
			} else {
				ipCountMap.put(user.getIp(), 1);
			}

			mongoDBConnection.updateUserEmails(user.getEmailId(), planningMaster.getScheduleDate(),
					MongoConnection.PLANNING_DB, 1, planningMaster.getCampaignType());
		}
		mongoDBConnection.updateDailyCampLimit(newsLetter, planningMaster.getScheduleDate(), emailList.size(),
				MongoConnection.PLANNING_DB, planningMaster.getCampaignType());
		saveIpLimitDaily(ipCountMap, planningMaster.getScheduleDate(), MongoConnection.PLANNING_DB,
				planningMaster.getCampaignType());
		mongoDBConnection.saveCampaignRepetition(campaignInfoList, MongoConnection.PLANNING_DB);
		mongoDBConnection.savePlannedCampaign(plannedCampaignLis);
		logger.debug("Email List For Save DONE:: " + emailList.size());
		return new AsyncResult<>(emailList);

	}

	private void saveIpLimitDaily(HashMap<String, Integer> ipCountMap, String date, MongoConnection mongoConnection,
			String campaignType) {
		ipCountMap.forEach((k, v) -> {
			mongoDBConnection.updateiplimtDaily(date, k, v, mongoConnection, campaignType);
		});
	}

	@Async("saveSchedulePlan")
	public Future<String> saveSchedulePlan(List<Future<MailData>> mailDataList, PlanningMaster planningMaster,
			NewsLetter newsLetter, Date expiryDate, ScheduleCampaign scheduleCampaign)
			throws ExecutionException, InterruptedException {
		logger.debug("Entering into saveSchedulePlan");
		List<CampaignInfo> campaignInfoList = new ArrayList<>();
		HashMap<String, HashMap<String, Integer>> domainIPCountMap = new HashMap<>();
		List<MailData> mailList = new ArrayList<>();
		for (Future<MailData> ftu : mailDataList) {
			MailData mailData = ftu.get();
			// logger.debug("mailData:::: " + mailData);
			/* String response = restConnector.executePost(mailData); */
			// logger.debug("Mail API Response:::: " + response);
			mailList.add(mailData);
			CampaignInfo campaignInfo = new CampaignInfo();
			campaignInfo.setAdded_date(planningMaster.getScheduleDate());
			campaignInfo.setCategory_id(newsLetter.getCategory_id());
			campaignInfo.setClient_id(newsLetter.getClient_id());
			campaignInfo.setCustomer_id(newsLetter.getCustomer_id());
			campaignInfo.setEmail_id(mailData.getEmail_id());
			campaignInfo.setSubcategory_id(newsLetter.getSubcategory_id());
			campaignInfo.setExpire_at(expiryDate);
			campaignInfo.setCampaign_type(planningMaster.getCampaignType());
			campaignInfoList.add(campaignInfo);
			if (domainIPCountMap.containsKey(mailData.getDomain())) {
				HashMap<String, Integer> ipCount = domainIPCountMap.get(mailData.getDomain());
				if (ipCount.containsKey(mailData.getIp())) {
					int ipc = ipCount.get(mailData.getIp());
					ipc = ipc + 1;
					ipCount.put(mailData.getIp(), ipc);
				} else {
					ipCount.put(mailData.getIp(), 1);
				}
			} else {
				HashMap<String, Integer> ipCount = new HashMap<>();
				ipCount.put(mailData.getIp(), 1);
				domainIPCountMap.put(mailData.getDomain(), ipCount);
			}
			mongoDBConnection.updateUserEmails(mailData.getEmail_id(), planningMaster.getScheduleDate(),
					MongoConnection.MAIN_DB, 1, planningMaster.getCampaignType());
		}

		BulkMailData bulkMailData = new BulkMailData();
		bulkMailData.setMailList(mailList);
		bulkMailData.setType("normal");
		logger.debug("going to send data size  {}", bulkMailData.getMailList().size());
		String response = restConnector.executePostBulk(bulkMailData);
		logger.debug("response {}", response);
		int count = mailDataList.size();

		mongoDBConnection.updateTotalPushed(scheduleCampaign.getId(), count);
		mongoDBConnection.updateDailyCampLimit(newsLetter, planningMaster.getScheduleDate(), count,
				MongoConnection.MAIN_DB, planningMaster.getCampaignType());
		saveDomainIPWiseStats(domainIPCountMap, planningMaster.getScheduleDate(), scheduleCampaign.getId(),
				planningMaster.getCampaignType());
		mongoDBConnection.saveCampaignRepetition(campaignInfoList, MongoConnection.MAIN_DB);
		// logger.debug("saveCampaignRepetition:::: " + campaignInfoList);
		return new AsyncResult<>("SUCCESS");
	}

	private void saveDomainIPWiseStats(HashMap<String, HashMap<String, Integer>> domainIPCountMap, String date,
			String campaignId, String campaignType) {
		domainIPCountMap.forEach((k, v) -> {
			saveIpLimitDaily(v, date, MongoConnection.MAIN_DB, campaignType);
			v.forEach((ip, co) -> {
				mongoDBConnection.updateDomainIPWiseStats(campaignId, k, ip, date, co);
			});
		});
	}

	@Async("removePlan")
	public Future<String> removeFromDB(List<PlannedCampaign> dbRows, PlanningMaster planningMaster,
			NewsLetter newsLetter, AtomicLong totalRemovedFromDB) {
		HashMap<String, Integer> ipCountMap = new HashMap<>();
		for (PlannedCampaign pc : dbRows) {
			mongoDBConnection.updateUserEmails(pc.getEmailId(), planningMaster.getScheduleDate(),
					MongoConnection.PLANNING_DB, -1, planningMaster.getCampaignType());
			if (ipCountMap.containsKey(pc.getIpAddress())) {
				int ipCount = ipCountMap.get(pc.getIpAddress());
				ipCount = ipCount + 1;
				ipCountMap.put(pc.getIpAddress(), ipCount);
			} else {
				ipCountMap.put(pc.getIpAddress(), 1);
			}
		}
		removeIpLimitDaily(ipCountMap, planningMaster.getScheduleDate(), planningMaster.getCampaignType());
		mongoDBConnection.updateDailyCampLimit(newsLetter, planningMaster.getScheduleDate(), -dbRows.size(),
				MongoConnection.PLANNING_DB, planningMaster.getCampaignType());
		List<String> emails = dbRows.stream().map(us -> us.getEmailId()).collect(Collectors.toList());
		mongoDBConnection.removeCampaignRepetition(newsLetter, emails, MongoConnection.PLANNING_DB,
				planningMaster.getCampaignType());
		mongoDBConnection.removePlannedCampaign(planningMaster);
		totalRemovedFromDB.addAndGet(dbRows.size());
		return new AsyncResult<>("SUCCESS");
	}

	private void removeIpLimitDaily(HashMap<String, Integer> ipCountMap, String date, String campaignType) {
		ipCountMap.forEach((k, v) -> {
			int count = v;
			count = -count;
			mongoDBConnection.updateiplimtDaily(date, k, count, MongoConnection.PLANNING_DB, campaignType);
		});
	}
}
