package com.digismart.service;

import static com.digismart.util.Util.dateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.BulkOperations.BulkMode;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOptions;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.util.CloseableIterator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import com.digismart.aop.ExecutionTimeCalculation;
import com.digismart.config.ApplicationProperties;
import com.digismart.model.CampaignInfo;
import com.digismart.model.CampaignMaster;
import com.digismart.model.CampaignStats;
import com.digismart.model.Domain;
import com.digismart.model.IPAddress;
import com.digismart.model.Limit;
import com.digismart.model.LinkMaster;
import com.digismart.model.NewsLetter;
import com.digismart.model.PlannedCampaign;
import com.digismart.model.PlannedVolume;
import com.digismart.model.PlanningMaster;
import com.digismart.model.PlanningVolume;
import com.digismart.model.SSlinks;
import com.digismart.model.ScheduleCampaign;
import com.digismart.model.SegmentMaster;
import com.digismart.model.User;
import com.digismart.util.Constants;
import com.digismart.util.MongoConnection;
import com.digismart.util.MongoIndexesUtil;
import com.digismart.util.Util;
import com.mongodb.DuplicateKeyException;
import com.mongodb.MongoCommandException;
import com.mongodb.client.result.UpdateResult;

@Repository
public class MongoDBService {

	private static final Logger logger = LoggerFactory.getLogger(MongoDBService.class);

	@Autowired
	@Qualifier("primaryMongoTemplate")
	MongoTemplate mongoTemplate;

	@Autowired
	@Qualifier("planningMongoTemplate")
	MongoTemplate planningMongoTemplate;

	@Autowired
	ApplicationProperties applicationProperties;
	@Autowired
	private MongoIndexesUtil mongoIndexesUtil;

	public SegmentMaster getSegmentMaster(String segmentId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(new ObjectId(segmentId)));
		return mongoTemplate.findOne(query, SegmentMaster.class);
	}

	public PlanningMaster getPlanningMaster(String planId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(new ObjectId(planId)));
		logger.debug("plannignMaster  Query {}", query);
		return mongoTemplate.findOne(query, PlanningMaster.class);
	}

	public void removePlanningMaster(String planId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(new ObjectId(planId)));
		mongoTemplate.remove(query, PlanningMaster.class);
	}

	public void updatePlanningStatus(String planId, PlanningVolume planningVolume, String status, String campaignType) {
		
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("_id").is(new ObjectId(planId)));
			query.addCriteria(Criteria.where("campaign_type").is(campaignType));
			if (status.equals("checked")) {
				Update update = new Update();
				update.set("plan_status", status);
				update.set("available_volume", planningVolume.getAvailableVolume());
				update.set("user_capping", planningVolume.getFilterUserCapping());
				update.set("campaign_restriction", planningVolume.getFilterCampaignRestriction());
				update.set("volume_restriction", planningVolume.getFilterVolumeRestriction());
				update.set("segment_count", planningVolume.getSegmentTotalCount());
				update.set("campaign_type", campaignType);
				mongoTemplate.updateFirst(query, update, PlanningMaster.class);
				logger.debug("In If Update done for Plan Id : "+planId +" with plan status : "+status);
			} else {
				Update update = new Update();
				update.set("plan_status", status);
				mongoTemplate.updateFirst(query, update, PlanningMaster.class);
				logger.debug("In Else Update done for Plan Id : "+planId +" with plan status : "+status);
			}
		} catch (Exception e) {
			logger.error("Exception in planning master update : "+e.getMessage());
			e.printStackTrace();
		}
	}

	public PlannedVolume getPlannedVolume(String date, String campaignType) {
		Query query = new Query();
		query.addCriteria(Criteria.where("schedule_date").is(date));
		// query.addCriteria(Criteria.where("campaign_type").is(campaignType));

		return mongoTemplate.findOne(query, PlannedVolume.class);
	}

	public void updateSchedulePlannedVolume(String date, String campaignId, String campaignType,
			PlanningMaster planningMaster) {
		logger.debug("updateSchedule :: " + date + " campaignId " + campaignId);
		Query queryCamp = new Query();
		queryCamp.addCriteria(Criteria.where("campaign_id").is(new ObjectId(campaignId)));
		CampaignStats campaignStats = mongoTemplate.findOne(queryCamp, CampaignStats.class);

		PlannedVolume plannedVolume = getPlannedVolume(date, campaignType);
		logger.debug("updateSchedulePlannedVolume :: " + plannedVolume);
		long planned_volume = 0;
		long scheduled_volume = 0;
		if (campaignStats.getTotalPushed() != null) {
			scheduled_volume = campaignStats.getTotalPushed();

		}
		if (plannedVolume != null) {
			planned_volume = plannedVolume.getPlannedVolume();
			scheduled_volume = scheduled_volume + plannedVolume.getScheduledVolume();
		}
		if (planningMaster.getIsPlanning()) {
			double scheduled = Util.getPercentage(scheduled_volume, planned_volume);
			Query query = new Query();
			query.addCriteria(Criteria.where("schedule_date").is(date));
			Update update = new Update();
			update.set("scheduled_volume", scheduled_volume);
			update.set("scheduled", scheduled);
			// update.set("campaign_type", campaignType);
			try {
				logger.debug("query :: " + query);
				logger.debug("update :: " + update);
				UpdateResult upsert = mongoTemplate.upsert(query, update, PlannedVolume.class);
				logger.debug("upsert :: " + upsert.getModifiedCount());
			} catch (MongoCommandException | DuplicateKeyException | org.springframework.dao.DuplicateKeyException e) {
				mongoTemplate.updateFirst(query, update, PlannedVolume.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void updatePlannedVolume(String date, PlanningVolume planningVolume, String campaignType,
			PlanningMaster planningMaster) {

		PlannedVolume plannedVolume = getPlannedVolume(date, campaignType);
		logger.debug("plannedVolume :: " + plannedVolume);
		long total_volume = 0;
		long available_volume = 0;
		long planned_volume = 0;
		long scheduled_volume = 0;
		if (plannedVolume != null) {
			total_volume = plannedVolume.getTotalVolume();
			planned_volume = plannedVolume.getPlannedVolume() + planningVolume.getTotalSavedInDB();
			scheduled_volume = plannedVolume.getScheduledVolume();
		} else {
			total_volume = getIpLimitPromo();
			planned_volume = planningVolume.getTotalSavedInDB();
		}
		double planned = Util.getPercentage(planned_volume, total_volume);
		double scheduled = Util.getPercentage(scheduled_volume, planned_volume);
		if (planningMaster != null && planningMaster.getIsPlanning()) {
			available_volume = total_volume - planned_volume;
			Query query = new Query();
			query.addCriteria(Criteria.where("schedule_date").is(date));
			Update update = new Update();
			update.set("total_volume", total_volume);
			update.set("available_volume", available_volume);
			update.set("planned", planned);
			update.set("create_date", new Date());
			update.set("planned_volume", planned_volume);
			update.set("last_used", new Date());
			update.set("scheduled", scheduled);
			update.set("campaign_type", campaignType);
			try {
				logger.debug("query :: " + query);
				logger.debug("update :: " + update);
				UpdateResult upsert = mongoTemplate.upsert(query, update, PlannedVolume.class);
				logger.debug("upsert :: " + upsert.getModifiedCount());
			} catch (MongoCommandException | DuplicateKeyException | org.springframework.dao.DuplicateKeyException e) {
				mongoTemplate.updateFirst(query, update, PlannedVolume.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void removePlannedVolume(String date, long removedFromDB, String campaignType,
			PlanningMaster planningMaster) {

		PlannedVolume plannedVolume = getPlannedVolume(date, campaignType);
		logger.debug("plannedVolume :: " + plannedVolume);
		long total_volume = 0;
		long available_volume = 0;
		long planned_volume = 0;
		if (plannedVolume != null) {
			total_volume = plannedVolume.getTotalVolume();
			planned_volume = plannedVolume.getPlannedVolume() - removedFromDB;
		}

		if (planned_volume == 0 && planningMaster.getIsPlanning()) {
			logger.debug("Planned volume is " + planned_volume);
			Query query = new Query();
			query.addCriteria(Criteria.where("schedule_date").is(date));
			mongoTemplate.remove(query, PlannedVolume.class);
			logger.debug("Planned volume entry deleted for  " + date);
		} else {
			if (planningMaster.getIsPlanning()) {
				double planned = Util.getPercentage(planned_volume, total_volume);
				available_volume = total_volume - planned_volume;
				Query query = new Query();
				query.addCriteria(Criteria.where("schedule_date").is(date));
				Update update = new Update();
				update.set("total_volume", total_volume);
				update.set("available_volume", available_volume);
				update.set("planned", planned);
				update.set("create_date", new Date());
				update.set("planned_volume", planned_volume);
				update.set("campaign_type", campaignType);
				try {
					logger.debug("query :: " + query);
					logger.debug("update :: " + update);
					UpdateResult upsert = mongoTemplate.upsert(query, update, PlannedVolume.class);
					logger.debug("upsert :: " + upsert.getModifiedCount());
				} catch (MongoCommandException | DuplicateKeyException
						| org.springframework.dao.DuplicateKeyException e) {
					mongoTemplate.updateFirst(query, update, PlannedVolume.class);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public Limit getLimits(String campaingType) {
		Query query = new Query();
		query.addCriteria(Criteria.where("campaign_type").is(campaingType));
		return mongoTemplate.findOne(query, Limit.class);
	}

	public List<Domain> getAllDomains(String domain) {
		Query query = new Query();
		if (domain != null && !domain.equalsIgnoreCase("")) {
			query.addCriteria(Criteria.where("domain_name").is(domain));
		}
		return mongoTemplate.find(query, Domain.class);
	}

	public long getIpLimitPromo() {
		Query query = new Query();
		List<IPAddress> ipAddress = mongoTemplate.find(query, IPAddress.class);
		long totalPrompCount = ipAddress.stream().mapToLong(ip -> ip.getMax_volume_promo()).sum();
		return totalPrompCount;
	}

	public List<IPAddress> getInfraIpLimit(Domain domain) {
		Query query = new Query();
		query.addCriteria(Criteria.where("domain_id").is(new ObjectId(domain.getId())));
		List<IPAddress> ipAddress = mongoTemplate.find(query, IPAddress.class);
		return ipAddress;
	}

	public NewsLetter getNewsLetter(String newsLetterId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(new ObjectId(newsLetterId)));
		logger.info("newsLetter query {}", query);
		return mongoTemplate.findOne(query, NewsLetter.class);
	}

	public long getCount(String collectionName, Query query, MongoConnection mongoConnection) {
		if (mongoConnection == MongoConnection.PLANNING_DB) {
			return planningMongoTemplate.count(query, collectionName);
		}
		return mongoTemplate.count(query, collectionName);
	}

	public boolean isCollectionExists(String collectionName, MongoConnection mongoConnection) {
		if (mongoConnection == MongoConnection.PLANNING_DB) {
			return planningMongoTemplate.collectionExists(collectionName);
		}
		return mongoTemplate.collectionExists(collectionName);
	}

	public int getCurrentIpLimit(String ipAddress, String date, MongoConnection mongoConnection, String campaignType) {
		Query query = new Query();
		query.addCriteria(Criteria.where("ip_address").is(ipAddress));
		query.addCriteria(Criteria.where("date").is(date));
		query.addCriteria(Criteria.where("campaign_type").is(campaignType));
		mongoConnection = MongoConnection.PLANNING_DB;
		MongoTemplate template = mongoTemplate;
		if (mongoConnection == MongoConnection.PLANNING_DB) {
			template = planningMongoTemplate;
		}
		Map row = template.findOne(query, Map.class, "max_daily_ip_limit");
		if (row != null && row.containsKey("count")) {
			return Integer.parseInt(row.get("count").toString());
		}
		return 0;
	}

	@ExecutionTimeCalculation
	public List<User> applyCampaignRestriction(NewsLetter newsLetter, List<User> users, String scheduleDate,
			MongoConnection mongoConnection, String campaignType) {

		Query query = new Query();
		query.addCriteria(Criteria.where("client_id").is(newsLetter.getClient_id()));
		query.addCriteria(Criteria.where("customer_id").is(newsLetter.getCustomer_id()));
		query.addCriteria(Criteria.where("category_id").is(newsLetter.getCategory_id()));
		query.addCriteria(Criteria.where("subcategory_id").is(newsLetter.getSubcategory_id()));
		query.addCriteria(Criteria.where("expire_at").gt(Util.getCurrentDateFromStr(scheduleDate)));
		query.addCriteria(Criteria.where("campaign_type").is(campaignType));
		List<String> emails = users.stream().map(u -> u.getEmailId()).collect(Collectors.toList());
		query.addCriteria(Criteria.where("email_id").in(emails));
		query.fields().include("email_id").exclude("_id");
		MongoTemplate template = mongoTemplate;
		mongoConnection = MongoConnection.PLANNING_DB;
		if (mongoConnection == MongoConnection.PLANNING_DB) {
			template = planningMongoTemplate;
		}
		List<Map> filteredEmailsObj = template.find(query, Map.class, "campaign_info");
		List<String> filteredEmails = filteredEmailsObj.stream().map(u -> u.get("email_id").toString())
				.collect(Collectors.toList());
		// logger.debug("filteredEmails :: " + filteredEmails.size());
		List<User> finalList = new ArrayList<>(users);
		finalList.removeAll(filteredEmails);
		return finalList;
	}

	public CloseableIterator<Map> joinUserCappingCollection(SegmentMaster segmentMaster,
			MongoConnection mongoConnection, PlanningMaster planningMaster) {
		AggregationOptions aggregationOptions = Aggregation.newAggregationOptions().allowDiskUse(true)
				.cursorBatchSize(applicationProperties.getCursor_batch_size()).build();
		mongoConnection = MongoConnection.PLANNING_DB;
		Aggregation aggregation = Aggregation
				.newAggregation(Aggregation.lookup("user_emails", "email_id", "email_id", "count_info"))
				.withOptions(aggregationOptions);

		/*
		 * Aggregation.unwind("count_info", true),
		 * Aggregation.match(Criteria.where("count_info.campaign_type").in(crMatch)))
		 */
		if (mongoConnection == MongoConnection.PLANNING_DB) {
			aggregation = Aggregation.newAggregation(Aggregation.lookup(
					"plan_" + Constants.USER_EMAILS_COL + "_"
							+ dateFormat("yyyyMMdd", planningMaster.getScheduleDate()),
					"email_id", "email_id", "count_info")).withOptions(aggregationOptions);
			/*
			 * Aggregation.unwind("count_info", true),
			 * Aggregation.match(Criteria.where("count_info.campaign_type").in(crMatch))
			 */
			logger.debug("aggregation :: " + aggregation.toString());
			return mongoTemplate.aggregateStream(aggregation, segmentMaster.getSegmentCollectionName(), Map.class);
		}

		logger.debug("aggregation :: on collection name {}", aggregation.toString(),
				segmentMaster.getSegmentCollectionName());
		logger.debug("collection Name " + segmentMaster.getSegmentCollectionName());
		return mongoTemplate.aggregateStream(aggregation, segmentMaster.getSegmentCollectionName(), Map.class);
	}

	public void updateDailyCampLimit(NewsLetter newsLetter, String date, int count, MongoConnection mongoConnection,
			String campaignType) {
		Query query = new Query();
		query.addCriteria(Criteria.where("client_id").is(newsLetter.getClient_id()).and("customer_id")
				.is(newsLetter.getCustomer_id()).and("category_id").is(newsLetter.getCategory_id()).and("schedule_date")
				.is(date).and("subcategory_id").is(newsLetter.getSubcategory_id()).and("campaign_type")
				.is(campaignType));
		logger.debug("updateDailyCampLimit " + query);
		Update update = new Update();
		update.inc("count", count);
		if (mongoConnection == MongoConnection.PLANNING_DB) {
			try {
				planningMongoTemplate.upsert(query, update, "campaign_day_limit");
			} catch (MongoCommandException | DuplicateKeyException | org.springframework.dao.DuplicateKeyException e) {
				planningMongoTemplate.updateFirst(query, update, "campaign_day_limit");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				mongoTemplate.upsert(query, update, "campaign_day_limit");
			} catch (MongoCommandException | DuplicateKeyException | org.springframework.dao.DuplicateKeyException e) {
				mongoTemplate.updateFirst(query, update, "campaign_day_limit");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void updateiplimtDaily(String date, String ip, int count, MongoConnection mongoConnection,
			String campaignType) {
		Query query = new Query();
		query.addCriteria(
				Criteria.where("date").is(date).and("ip_address").is(ip).and("campaign_type").is(campaignType));
		Update update = new Update();
		update.inc("count", count);
		if (mongoConnection == MongoConnection.PLANNING_DB) {
			try {
				planningMongoTemplate.upsert(query, update, "max_daily_ip_limit");
			} catch (MongoCommandException | DuplicateKeyException | org.springframework.dao.DuplicateKeyException e) {
				planningMongoTemplate.updateFirst(query, update, "max_daily_ip_limit");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				mongoTemplate.upsert(query, update, "max_daily_ip_limit");
			} catch (MongoCommandException | DuplicateKeyException | org.springframework.dao.DuplicateKeyException e) {
				mongoTemplate.updateFirst(query, update, "max_daily_ip_limit");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void saveCampaignRepetition(List<CampaignInfo> campaignInfoList, MongoConnection mongoConnection) {

		BulkOperations bulkOps = null;
		if (mongoConnection == MongoConnection.PLANNING_DB) {
			bulkOps = planningMongoTemplate.bulkOps(BulkMode.UNORDERED, CampaignInfo.class);
		} else {
			bulkOps = mongoTemplate.bulkOps(BulkMode.UNORDERED, CampaignInfo.class);
		}

		logger.debug("campaignInfoList.size()   " + campaignInfoList.size());
		try {
			for (CampaignInfo campInfo : campaignInfoList) {
				/*
				 * Query query = new Query();
				 * query.addCriteria(Criteria.where("client_id").is(campInfo.getClient_id()));
				 * query.addCriteria(Criteria.where("customer_id").is(campInfo.getCustomer_id())
				 * );
				 * query.addCriteria(Criteria.where("category_id").is(campInfo.getCategory_id())
				 * ); query.addCriteria(Criteria.where("subcategory_id").is(campInfo.
				 * getSubcategory_id()));
				 * query.addCriteria(Criteria.where("email_id").is(campInfo.getEmail_id()));
				 * query.addCriteria(Criteria.where("campaign_type").is(campInfo.
				 * getCampaign_type())); Update update = new Update(); update.set("expire_at",
				 * campInfo.getExpire_at());
				 */
				// tmpTemplate.upsert(query, update);
				// tmpTemplate.updateFirst(query, update, CampaignInfo.class);
				// bulkOps.upsert(query, update);
				bulkOps.insert(campInfo);
			}
			if (campaignInfoList.size() > 0)
				bulkOps.execute();
		} catch (Exception e) {
			/*
			 * for (CampaignInfo campInfo : campaignInfoList) { Query query = new Query();
			 * query.addCriteria(Criteria.where("client_id").is(campInfo.getClient_id()));
			 * query.addCriteria(Criteria.where("customer_id").is(campInfo.getCustomer_id())
			 * );
			 * query.addCriteria(Criteria.where("category_id").is(campInfo.getCategory_id())
			 * ); query.addCriteria(Criteria.where("subcategory_id").is(campInfo.
			 * getSubcategory_id()));
			 * query.addCriteria(Criteria.where("email_id").is(campInfo.getEmail_id()));
			 * query.addCriteria(Criteria.where("campaign_type").is(campInfo.
			 * getCampaign_type())); Update update = new Update(); update.set("expire_at",
			 * campInfo.getExpire_at()); bulkOps.upsert(query, update); //
			 * bulkOps.insert(campInfo); // tmpTemplate.updateFirst(query, update,
			 * CampaignInfo.class); } if (campaignInfoList.size() > 0) bulkOps.execute();
			 * 
			 * logger.debug("campaignInfoList campaign repetion saved   " +
			 * campaignInfoList.size());
			 */}
	}

	public void removeCampaignRepetition(NewsLetter newsLetter, List<String> emails, MongoConnection mongoConnection,
			String campaignType) {
		Query query = new Query();
		query.addCriteria(Criteria.where("client_id").is(newsLetter.getClient_id()));
		query.addCriteria(Criteria.where("customer_id").is(newsLetter.getCustomer_id()));
		query.addCriteria(Criteria.where("category_id").is(newsLetter.getCategory_id()));
		query.addCriteria(Criteria.where("subcategory_id").is(newsLetter.getSubcategory_id()));
		query.addCriteria(Criteria.where("email_id").in(emails));
		query.addCriteria(Criteria.where("campaign_type").is(campaignType));
		// logger.debug("remove Camp rep : " + query);
		if (mongoConnection == MongoConnection.PLANNING_DB) {
			planningMongoTemplate.remove(query, CampaignInfo.class);
		} else {
			mongoTemplate.remove(query, CampaignInfo.class);
		}

	}

	@Async("updateUserEmailsPool")
	public void updateUserEmails(String email_id, String schedule_date, MongoConnection mongoConnection, int count,
			String campaignType) {
		Query query = new Query();
		query.addCriteria(Criteria.where("schedule_date").is(schedule_date).and("email_id").is(email_id));
		Update update = new Update();
		update.inc(campaignType + "_count", count);
		String collectionName = "plan_" + Constants.USER_EMAILS_COL + "_" + dateFormat("yyyyMMdd", schedule_date);
		if (mongoConnection == MongoConnection.PLANNING_DB) {
			try {
				mongoIndexesUtil.createIndexes(mongoTemplate, collectionName, "schedule_date", "email_id");
				mongoTemplate.upsert(query, update, collectionName);
			} catch (MongoCommandException | DuplicateKeyException | org.springframework.dao.DuplicateKeyException e) {
				mongoTemplate.updateFirst(query, update, collectionName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			mongoIndexesUtil.createIndexes(mongoTemplate, Constants.USER_EMAILS_COL, "schedule_date", "email_id");
			try {
				mongoTemplate.upsert(query, update, Constants.USER_EMAILS_COL);
			} catch (MongoCommandException | DuplicateKeyException | org.springframework.dao.DuplicateKeyException e) {
				mongoTemplate.updateFirst(query, update, Constants.USER_EMAILS_COL);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void savePlannedCampaign(List<PlannedCampaign> plannedCampaignLis) {
		planningMongoTemplate.insert(plannedCampaignLis, PlannedCampaign.class);
	}

	public void removePlannedCampaign(PlanningMaster planningMaster) {
		Query query = new Query();
		query.addCriteria(Criteria.where("plan_id").is(new ObjectId(planningMaster.getPlanId())));
		logger.info("removePlannedCampaign from mongo " + query);
		planningMongoTemplate.remove(query, PlannedCampaign.class);
	}

	public CloseableIterator<PlannedCampaign> getPlannedCampaign(String planId) {
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("plan_id").is(new ObjectId(planId)));
			CloseableIterator<PlannedCampaign> stream = planningMongoTemplate.stream(query, PlannedCampaign.class);
			return stream;
		} catch (Exception e) {
			logger.info("error while selecting from mongo ");
			e.printStackTrace();
			return null;
		}
	}

	public CampaignMaster getCampaignMaster(String campaignId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(new ObjectId(campaignId)));
		return mongoTemplate.findOne(query, CampaignMaster.class);
	}

	public ScheduleCampaign getScheduleCampaign(String campaignId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(new ObjectId(campaignId)));
		logger.debug("query  " + query);
		return mongoTemplate.findOne(query, ScheduleCampaign.class);
	}

	public String getLinkId(String url) {

		Query query = new Query();
		query.addCriteria(Criteria.where("url").is(url));
		SSlinks findOne = mongoTemplate.findOne(query, SSlinks.class);
		if (findOne != null) {
			return findOne.getId();
		}
		return null;
	}

	public String saveLinkURL(SSlinks sslinks) {
		mongoTemplate.save(sslinks);
		return sslinks.getId();
	}

	public LinkMaster findLinkMaster(String campId, String url, String linktag) {
		Query query = new Query();
		query.addCriteria(Criteria.where("campaign_id").is(new ObjectId(campId)));
		query.addCriteria(Criteria.where("url").is(url));
		query.addCriteria(Criteria.where("type").is(linktag));
		return mongoTemplate.findOne(query, LinkMaster.class);
	}

	public void updateTotalPushed(String campaignId, int count) {
		Query query = new Query();
		query.addCriteria(Criteria.where("campaign_id").is(new ObjectId(campaignId)));
		Update update = new Update();
		update.inc("total_pushed", count);
		logger.debug("campaignId::::  " + campaignId);
		logger.debug("total_pushed::::  " + count);
		try {
			mongoTemplate.upsert(query, update, "campaign_stats");
		} catch (MongoCommandException | DuplicateKeyException | org.springframework.dao.DuplicateKeyException e) {
			mongoTemplate.updateFirst(query, update, "campaign_stats");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void updateDomainIPWiseStats(String campaignId, String domain_name, String ip_address, String date,
			int count) {
		Query query = new Query();
		query.addCriteria(Criteria.where("campaign_id").is(new ObjectId(campaignId)).and("domain_name").is(domain_name)
				.and("ip_address").is(ip_address).and("date").is(date));
		Update update = new Update();
		update.inc("total_push", count);
		try {
			mongoTemplate.upsert(query, update, "domain_ip_wise_stats");
		} catch (MongoCommandException | DuplicateKeyException | org.springframework.dao.DuplicateKeyException e) {
			mongoTemplate.updateFirst(query, update, "domain_ip_wise_stats");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void updateCampMaster(String id, String key, String val) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(new ObjectId(id)));
		Update update = new Update();
		update.set(key, val);
		try {
			mongoTemplate.upsert(query, update, "campaign_master");
		} catch (MongoCommandException | DuplicateKeyException | org.springframework.dao.DuplicateKeyException e) {
			mongoTemplate.updateFirst(query, update, "campaign_master");
		} catch (Exception e) {
			logger.error("Exception:: ", e);
		}

	}

	public void updateSegMasterLastUsed(String segmentId) {
		BulkOperations ops = mongoTemplate.bulkOps(BulkMode.UNORDERED, "segment_master");

		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(new ObjectId(segmentId)));
		Update update = new Update();
		update.set("last_used", new Date());
		try {
			ops.upsert(query, update);
		} catch (MongoCommandException | DuplicateKeyException | org.springframework.dao.DuplicateKeyException e) {
			ops.updateOne(query, update);
		}

		query = new Query();
		query.addCriteria(Criteria.where("_id").is(new ObjectId(segmentId)));
		query.fields().include("merge_id").exclude("_id");
		String list = mongoTemplate.findOne(query, String.class, "segment_master");
		if (list != null && !list.isEmpty()) {
			String[] arr = null;

			try {
				JSONObject jobj = new JSONObject(list);
				if (jobj.length() > 0) {
					arr = jobj.getString("merge_id").split(",");
					for (String entry : arr) {
						query = new Query();
						query.addCriteria(Criteria.where("_id").is(new ObjectId(entry)));
						update = new Update();
						update.set("last_used", new Date());
						try {
							ops.upsert(query, update);
						} catch (MongoCommandException | DuplicateKeyException
								| org.springframework.dao.DuplicateKeyException e) {
							ops.updateOne(query, update);
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
		ops.execute();
	}

	public void updatePlanningMasterWithStatus(String planId, String planStatus) {
			Query query = new Query();
			query.addCriteria(Criteria.where("_id").is(new ObjectId(planId)));
			Update update = new Update();
			update.set("plan_status", planStatus);
			try {
				mongoTemplate.upsert(query, update, PlanningMaster.class);
			} catch (MongoCommandException | DuplicateKeyException | org.springframework.dao.DuplicateKeyException e) {
				mongoTemplate.updateFirst(query, update, PlanningMaster.class);
			} catch (Exception e) {
				logger.error("Exception:: ", e);
			}
	}

	public String getInfraFromDomain(String domainName) {
		String infra = "";
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("domain_name").is(domainName));
			Domain domain = mongoTemplate.findOne(query, Domain.class);
			if(domain!=null){
				infra = domain.getInfra();
			}
		} catch (Exception e) {
			logger.error("Exception:: ", e);
		}
		return infra;
	}

}
