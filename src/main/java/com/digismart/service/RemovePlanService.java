package com.digismart.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.CloseableIterator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.digismart.config.ApplicationProperties;
import com.digismart.model.NewsLetter;
import com.digismart.model.PlannedCampaign;
import com.digismart.model.PlanningMaster;

@Service
public class RemovePlanService {

	private static final Logger logger = LoggerFactory.getLogger(RemovePlanService.class);

	@Autowired
	MongoDBService mongoDBConnection;
	@Autowired
	PlanningService planningService;

	@Autowired
	ApplicationProperties applicationProperties;

	@Async("apiAsyncPool")
	public void removePlan(String planId) throws InterruptedException, ExecutionException {
		PlanningMaster planningMaster = mongoDBConnection.getPlanningMaster(planId);
		if (planningMaster != null) {
			NewsLetter newsLetter = mongoDBConnection.getNewsLetter(planningMaster.getNewsletterId().toString());
			if (newsLetter != null) {
				logger.debug("planningMaster " + planningMaster.getPlanId());
				CloseableIterator<PlannedCampaign> plannedCampaign = mongoDBConnection
						.getPlannedCampaign(planningMaster.getPlanId());
				int batchSizeForCam_re = applicationProperties.getCursor_batch_size();
				int count = 0;
				AtomicLong totalRemovedFromDB = new AtomicLong();
				List<PlannedCampaign> dbRows = new ArrayList<>();
				List<Future<String>> asyncResult = new ArrayList<>();
				while (plannedCampaign.hasNext()) {
					PlannedCampaign pc = plannedCampaign.next();
					dbRows.add(pc);
					if (++count % batchSizeForCam_re == 0) {
						Future<String> removeFromDB = planningService.removeFromDB(dbRows, planningMaster, newsLetter,
								totalRemovedFromDB);
						asyncResult.add(removeFromDB);
						dbRows = new ArrayList<>();
					}
				}
				if (dbRows.size() > 0) {
					Future<String> removeFromDB = planningService.removeFromDB(dbRows, planningMaster, newsLetter,
							totalRemovedFromDB);
					asyncResult.add(removeFromDB);
					dbRows = new ArrayList<>();
				}
				for (Future<String> res : asyncResult) {
					String str = res.get();
					logger.debug("REMOVE DONE FROM DB::::  " + str);
				}
				mongoDBConnection.removePlannedVolume(planningMaster.getScheduleDate(), totalRemovedFromDB.get(),
						planningMaster.getCampaignType(), planningMaster);
				mongoDBConnection.removePlanningMaster(planId);
			}else {
				logger.debug("NewsLetter object not found");
			}
		} else {
			logger.debug("planningMaster object  not found");
		}
	}

}
