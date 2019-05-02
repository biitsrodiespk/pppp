package com.digismart.service;

import static com.digismart.util.Util.dateFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.CloseableIterator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.digismart.aop.ExecutionTimeCalculation;
import com.digismart.config.ApplicationProperties;
import com.digismart.model.DomainIP;
import com.digismart.model.NewsLetter;
import com.digismart.model.PlanningMaster;
import com.digismart.model.PlanningVolume;
import com.digismart.model.SegmentMaster;
import com.digismart.model.User;
import com.digismart.util.Constants;
import com.digismart.util.MongoConnection;

@Service
public class SavePlanService {

	private static final Logger logger = LoggerFactory.getLogger(SavePlanService.class);

	@Autowired
	CheckPlanService checkPlanService;
	@Autowired
	MongoDBService mongoDBConnection;
	@Autowired
	DomainIPBuilder domainIPBuilder;
	@Autowired
	PlanningVolumeService planningVolumeService;
	@Autowired
	PlanningService planningService;

	@Autowired
	ApplicationProperties applicationProperties;

	@Async("apiAsyncPool")
	@ExecutionTimeCalculation
	public String saveNewPlan(String planId, CountDownLatch latch) throws ExecutionException, InterruptedException {

		PlanningMaster planningMaster = mongoDBConnection.getPlanningMaster(planId);
		String status = "SUCCESS";
		if (planningMaster != null) {
			PlanningVolume planningVolume = new PlanningVolume();
			SegmentMaster segmentMaster = mongoDBConnection.getSegmentMaster(planningMaster.getSegmentId().toString());
			if (segmentMaster != null) {
				planningVolumeService.setSegmentCount(segmentMaster, planningVolume);
				planningVolumeService.setPlanLimits(planningMaster, planningVolume);
				boolean isCollectionExists = mongoDBConnection.isCollectionExists("plan_" + Constants.USER_EMAILS_COL
						+ "_" + dateFormat("yyyyMMdd", planningMaster.getScheduleDate()), MongoConnection.MAIN_DB);
				MongoConnection mongoConnection = isCollectionExists ? MongoConnection.PLANNING_DB
						: MongoConnection.MAIN_DB;
				HashMap<String, DomainIP> domianIps = domainIPBuilder.build(planningMaster.getScheduleDate(),
						mongoConnection, planningMaster.getCampaignType(), planningMaster);
				NewsLetter newsLetter = mongoDBConnection.getNewsLetter(planningMaster.getNewsletterId().toString());
				logger.debug(segmentMaster.getSegmentCollectionName());
				CloseableIterator<Map> joinedCollectionIt = checkPlanService.getJoinedCollectionIt(segmentMaster,
						isCollectionExists, planningMaster);
				saveToDB(domianIps, newsLetter, joinedCollectionIt, planningVolume, planningMaster, mongoConnection,
						latch);
			} else {
				latch.countDown();
			}
		} else {
			latch.countDown();
		}
		return status;
	}

	private void saveToDB(HashMap<String, DomainIP> domianIps, NewsLetter newsLetter,
			CloseableIterator<Map> joinedCollIt, PlanningVolume planningVolume, PlanningMaster planningMaster,
			MongoConnection mongoConnection, CountDownLatch latch) throws ExecutionException, InterruptedException {
		AtomicLong afterEmailRCount = new AtomicLong();
		AtomicLong afterCRestriction = new AtomicLong();
		AtomicLong afterIPVolumeR = new AtomicLong();
		AtomicLong totalSavedtoDB = new AtomicLong();
		Lock objectToLock = new ReentrantLock();

		int batchSizeForCam_re = applicationProperties.getCursor_batch_size();
		int count = 0;
		ArrayList<Map> list = new ArrayList<>();
		ArrayList<Future<List<User>>> asyncResult = new ArrayList<>();
		while (joinedCollIt.hasNext()) {
			if (totalSavedtoDB.get() >= planningVolume.getScheduleCount())
				break;
			list.add(joinedCollIt.next());
			if (++count % batchSizeForCam_re == 0) {
				Future<List<User>> filteredEmails = planningService.applyFilters(list, domianIps, newsLetter,
						planningVolume, planningMaster, mongoConnection, afterEmailRCount, afterCRestriction,
						afterIPVolumeR);
				Future<List<User>> saveToDb = planningService.saveToDb(filteredEmails, domianIps, newsLetter,
						planningVolume, planningMaster, afterEmailRCount, afterCRestriction, afterIPVolumeR,
						totalSavedtoDB, objectToLock);
				list = new ArrayList<>();
				asyncResult.add(saveToDb);
			}
		}
		if (list.size() > 0) {
			Future<List<User>> filteredEmails = planningService.applyFilters(list, domianIps, newsLetter,
					planningVolume, planningMaster, mongoConnection, afterEmailRCount, afterCRestriction,
					afterIPVolumeR);
			Future<List<User>> saveToDb = planningService.saveToDb(filteredEmails, domianIps, newsLetter,
					planningVolume, planningMaster, afterEmailRCount, afterCRestriction, afterIPVolumeR, totalSavedtoDB,
					objectToLock);
			list = new ArrayList<>();
			asyncResult.add(saveToDb);
		}
		for (Future<List<User>> users : asyncResult) {
			List<User> finalList = users.get();
			logger.debug("finalList DONE TO DB::::  " + finalList.size());
		}
		planningVolume.setTotalSavedInDB(totalSavedtoDB.get());
		mongoDBConnection.updatePlannedVolume(planningMaster.getScheduleDate(), planningVolume,
				planningMaster.getCampaignType(), planningMaster);

		mongoDBConnection.updatePlanningStatus(planningMaster.getPlanId(), null, "planned",
				planningMaster.getCampaignType());

		logger.debug(":::::: SAVE DONE ::::::" + totalSavedtoDB.get());
		latch.countDown();
	}

}
