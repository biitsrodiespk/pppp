package com.digismart.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection="limit_master")
public class Limit {

	@Id
	private String id;
	
	@Field("mail_limit_user")
	private int mailLimitUser;
	
	@Field("campaign_limit")
	private long campaignLimit;
	
	@Field("campaign_block_days")
	private long campaignBlockDays;
	
	
	public int getMailLimitUser() {
		return mailLimitUser;
	}
	public void setMailLimitUser(int mailLimitUser) {
		this.mailLimitUser = mailLimitUser;
	}
	public long getCampaignLimit() {
		return campaignLimit;
	}
	public void setCampaignLimit(long campaignLimit) {
		this.campaignLimit = campaignLimit;
	}
	public long getCampaignBlockDays() {
		return campaignBlockDays;
	}
	public void setCampaignBlockDays(long campaignBlockDays) {
		this.campaignBlockDays = campaignBlockDays;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
