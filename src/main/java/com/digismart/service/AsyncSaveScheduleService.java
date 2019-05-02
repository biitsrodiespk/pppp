package com.digismart.service;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncSaveScheduleService {

	private static final Logger logger = LoggerFactory.getLogger(AsyncSaveScheduleService.class);

	@Autowired
	SavePlanService savePlanService;

	@Autowired
	SchedulePlanSevice schedulePlanSevice;

	@Autowired
	private CampaignService campaignService;
	
	@Async("saveScheduleNew")
	public void saveScheduleAsync(String planId,String campId){
		try {
			final CountDownLatch latch = new CountDownLatch(1);
			logger.debug("Going to save plan with plan Id : "+planId);
			savePlanService.saveNewPlan(planId, latch);
			logger.debug("Save plan with plan Id : "+planId +" Done ");
			latch.await();
			logger.debug("Going to updateCampStatus with Camp Id : "+campId);
			campaignService.updateCampStatus(campId, "scheduling");
			logger.debug("Done updateCampStatus with Camp Id : "+campId);
			schedulePlanSevice.schedulePlan(campId, planId);
			logger.debug("SchedulePlan called");
		} catch (Exception e) {
			campaignService.updateCampStatus(campId, "failed");
			logger.debug("Error in save plan {}", e.getMessage());
		}
	}
	
}
