package com.digismart.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.digismart.model.Limit;
import com.digismart.model.PlanningMaster;
import com.digismart.model.PlanningVolume;
import com.digismart.model.SegmentMaster;
import com.digismart.util.MongoConnection;

@Service
public class PlanningVolumeService {
	private static Logger logger = LoggerFactory.getLogger(PlanningVolumeService.class);

	@Autowired
	MongoDBService mongoDBConnection;

	public PlanningVolume setSegmentCount(SegmentMaster segmentMaster, PlanningVolume planningVolume) {
		long segmentCollectionCount = mongoDBConnection.getCount(segmentMaster.getSegmentCollectionName(), new Query(),
				MongoConnection.MAIN_DB);
		planningVolume.setSegmentTotalCount(segmentCollectionCount);
		logger.debug("segment count  {}", segmentCollectionCount);
		return planningVolume;
	}

	public PlanningVolume setPlanLimits(PlanningMaster planningMaster, PlanningVolume planningVolume) {
		Limit limits = mongoDBConnection.getLimits(planningMaster.getCampaignType());
		if (limits != null) {
			planningVolume.setCampaignLimit(limits.getCampaignLimit());
			planningVolume.setMailLimitUser(limits.getMailLimitUser());
			planningVolume.setScheduleCount(planningMaster.getScheduleCount());
			planningVolume.setCampaignBlockDays(limits.getCampaignBlockDays());
		}
		return planningVolume;
	}
}
