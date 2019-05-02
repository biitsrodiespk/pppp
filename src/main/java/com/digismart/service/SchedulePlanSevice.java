package com.digismart.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.CloseableIterator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.digismart.aop.ExecutionTimeCalculation;
import com.digismart.config.ApplicationProperties;
import com.digismart.model.Limit;
import com.digismart.model.MailData;
import com.digismart.model.NewsLetter;
import com.digismart.model.PlannedCampaign;
import com.digismart.model.PlanningMaster;
import com.digismart.model.ScheduleCampaign;
import com.digismart.model.UserDetailDTO;
import com.digismart.util.MailComposer;
import com.digismart.util.Util;

@Service
public class SchedulePlanSevice {

	private static final Logger logger = LoggerFactory.getLogger(SchedulePlanSevice.class);

	@Autowired
	MongoDBService mongoDBConnection;

	@Autowired
	MailComposer mailComposer;

	@Autowired
	PlanningService planningService;

	@Autowired
	ApplicationProperties applicationProperties;
	@Autowired
	private CampaignService campaignService;

	@Async("apiAsyncPool")
	@ExecutionTimeCalculation
	public void schedulePlan(String campaignId, String planId) throws ExecutionException, InterruptedException {
		// logger.info("campaign Id {}, palnid ", campaignId, planId);

		ScheduleCampaign scheduleCampaign = mongoDBConnection.getScheduleCampaign(campaignId);
		if (scheduleCampaign != null) {
			logger.debug("scheduleCampaign Id {}", scheduleCampaign);
			PlanningMaster planningMaster = mongoDBConnection.getPlanningMaster(planId);
			if (planningMaster != null) {
				NewsLetter newsLetter = mongoDBConnection.getNewsLetter(planningMaster.getNewsletterId().toString());
				// logger.info("newsLetter {}", planningMaster);
				if (newsLetter != null) {
					campaignService.updateCampStatus(campaignId, "scheduling");
					saveToDB(planningMaster, scheduleCampaign, newsLetter);
				} else {
					logger.debug("newsletter not found for campId {} and paln id {}", campaignId, planId);
				}
			} else {
				logger.debug("planningMaster not found for campId {} and paln id {}", campaignId, planId);
			}
		} else {
			logger.debug("scheduleCampaign not found for campId {} and paln id {}", campaignId, planId);
		}
		// logger.info("planningMaster Id {}", planningMaster);

		// logger.info("saveToDB Done ");
	}

	private void saveToDB(PlanningMaster planningMaster, ScheduleCampaign scheduleCampaign, NewsLetter newsLetter)
			throws ExecutionException, InterruptedException {
		logger.debug("planningMaster " + planningMaster.getPlanId());
		CloseableIterator<PlannedCampaign> plannedCampaign = mongoDBConnection
				.getPlannedCampaign(planningMaster.getPlanId());
		logger.debug("plannedCampaign " + plannedCampaign);

		int batchSizeForCam_re = applicationProperties.getCursor_batch_size();
		int count = 0;
		Limit limits = mongoDBConnection.getLimits(planningMaster.getCampaignType());

		Date scd = Util.getDate("yyyy-MM-dd", planningMaster.getScheduleDate());
		Date expiryDate = Util.buildCampaignInfoExpiry(Integer.parseInt("" + limits.getCampaignBlockDays()), scd);

		List<Future<MailData>> mailDataList = new ArrayList<>();
		List<Future<String>> asyncResult = new ArrayList<>();
		while (plannedCampaign.hasNext()) {
			PlannedCampaign user = plannedCampaign.next();
			UserDetailDTO userDetailDTO = new UserDetailDTO();
			userDetailDTO.setEmail(user.getEmailId());
			userDetailDTO.setName(user.getName());
			userDetailDTO.setMobilenumber(user.getMobileNo());
			// logger.debug("scheduleCampaign " + scheduleCampaign);
			Future<MailData> mailData = mailComposer.getMailerHtmlContent(scheduleCampaign, newsLetter, userDetailDTO,
					user.getDomain(), user.getIpAddress(), user.getPort(), user.getSmtpHost(), user.getEmailPrefix(), user.getInfra());

			mailDataList.add(mailData);

			if (++count % batchSizeForCam_re == 0) {
				Future<String> saveSchedulePlan = planningService.saveSchedulePlan(mailDataList, planningMaster,
						newsLetter, expiryDate, scheduleCampaign);
				asyncResult.add(saveSchedulePlan);
				mailDataList = new ArrayList<>();
			}
		}
		if (mailDataList.size() > 0) {
			Future<String> saveSchedulePlan = planningService.saveSchedulePlan(mailDataList, planningMaster, newsLetter,
					expiryDate, scheduleCampaign);
			asyncResult.add(saveSchedulePlan);
			mailDataList = new ArrayList<>();
		}

		for (Future<String> res : asyncResult) {
			String str = res.get();
			logger.debug("DONE TO DB::::  " + str);
		}
		mongoDBConnection.updateCampMaster(scheduleCampaign.getId(), "campaign_status", "scheduled");
		mongoDBConnection.updateSegMasterLastUsed(scheduleCampaign.getSegmentId());
		mongoDBConnection.updateSchedulePlannedVolume(planningMaster.getScheduleDate(), scheduleCampaign.getId(),
				planningMaster.getCampaignType(), planningMaster);
		logger.debug("::::: Schedule Plan done::::  ");
	}

}
