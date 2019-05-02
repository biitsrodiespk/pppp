package com.digismart.controller;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.digismart.aop.ExecutionTimeCalculation;
import com.digismart.service.AsyncSaveScheduleService;
import com.digismart.service.CampaignService;
import com.digismart.service.CheckPlanService;
import com.digismart.service.MongoDBService;
import com.digismart.service.RemovePlanService;
import com.digismart.service.SavePlanService;
import com.digismart.service.SchedulePlanSevice;

@RestController
@RequestMapping("/api")
public class PlanningController {
	private static final Logger logger = LoggerFactory.getLogger(PlanningController.class);

	@Autowired
	CheckPlanService checkPlanService;

	@Autowired
	SavePlanService savePlanService;

	@Autowired
	SchedulePlanSevice schedulePlanSevice;

	@Autowired
	RemovePlanService removePlanService;
	
	@Autowired
	private CampaignService campaignService;
	
	@Autowired
	private AsyncSaveScheduleService asyncSaveScheduleService;
	
	@Autowired
	MongoDBService mongoDBConnection;

	@CrossOrigin
	@RequestMapping(value = "/checkPlan/{planId}", method = RequestMethod.GET)
	@ResponseBody
	public String checkPlan(@PathVariable String planId) {
		logger.debug("Check Plan Called :: " + planId);
		checkPlanService.checkPlan(planId);
		return "SUCCESS";
	}

	@CrossOrigin
	@RequestMapping(value = "/savePlan/{planId}", method = RequestMethod.GET)
	@ResponseBody
	public String savePlan(@PathVariable String planId) {
		logger.debug("Save Plan Called :: " + planId);
		String status = "SUCCESS";
		try {
			final CountDownLatch latch = new CountDownLatch(1);
			savePlanService.saveNewPlan(planId, latch);
			latch.await();
		} catch (Exception e) {
			status = "failed";
			logger.debug("Error in save plan {}", e.getMessage());
		}
		return status;
	}

	@CrossOrigin
	@RequestMapping(value = "/schedulePlan/{planId}/{campId}", method = RequestMethod.GET)
	@ResponseBody
	public String schedulePlan(@PathVariable String planId, @PathVariable String campId) {
		logger.debug("Schedule Plan Called :: " + planId + " campId " + campId);
		try {
			schedulePlanSevice.schedulePlan(campId, planId);

		} catch (ExecutionException | InterruptedException e) {
			e.printStackTrace();
		}
		return "SUCCESS";
	}

	@CrossOrigin
	@RequestMapping(value = "/removePlan/{planId}", method = RequestMethod.GET)
	@ResponseBody
	public String removePlan(@PathVariable String planId) {
		logger.debug("Remove Plan Called :: " + planId);
		try {
			removePlanService.removePlan(planId);
		} catch (ExecutionException | InterruptedException e) {
			e.printStackTrace();
		}
		return "SUCCESS";
	}

	@CrossOrigin
	@RequestMapping(value = "/saveandschedulecampaign/{planId}/{campId}", method = RequestMethod.GET)
	@ResponseBody
	@ExecutionTimeCalculation
	public String saveAndScheduleCampaign(@PathVariable String planId, @PathVariable String campId) {
		logger.debug("save Schedule Plan Called :: " + planId + " campId " + campId);
		String status = "SUCCESS";
		try {
			logger.debug("Going to saveScheduleAsync with plan Id : "+planId+" Camp Id : "+campId);
			asyncSaveScheduleService.saveScheduleAsync(planId,campId);
			campaignService.updateCampStatus(campId, "applying limits");
			mongoDBConnection.updatePlanningMasterWithStatus(planId, "applying limits");
			logger.debug("Going to saveScheduleAsync with plan Id : "+planId+" Camp Id : "+campId);
			/*final CountDownLatch latch = new CountDownLatch(1);
			logger.debug("Going to save plan with plan Id : "+planId);
			savePlanService.saveNewPlan(planId, latch);
			logger.debug("Save plan with plan Id : "+planId +" Done ");
			latch.await();
			logger.debug("Going to updateCampStatus with Camp Id : "+campId);
			campaignService.updateCampStatus(campId, "scheduling");
			logger.debug("Done updateCampStatus with Camp Id : "+campId);
			schedulePlanSevice.schedulePlan(campId, planId);
			logger.debug("schedulePlan called");*/
		} catch (Exception e) {
			status = "failed";
			/*campaignService.updateCampStatus(campId, "failed");
			logger.debug("Error in save plan {}", e.getMessage());*/
		}
		logger.debug("status {}", status);
		return status;
	}

}
