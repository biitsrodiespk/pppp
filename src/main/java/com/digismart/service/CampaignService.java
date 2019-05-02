package com.digismart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class CampaignService {
	@Autowired
	@Qualifier("primaryMongoTemplate")
	private MongoTemplate mongoTemplate;

	public void updateCampStatus(String cmapignId, String status) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(cmapignId));
		Update update = new Update();
		update.set("campaign_status", status);
		mongoTemplate.updateFirst(query, update, "campaign_master");

	}

}
