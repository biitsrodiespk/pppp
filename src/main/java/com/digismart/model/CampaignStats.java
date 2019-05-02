package com.digismart.model;

import org.bson.types.ObjectId;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "campaign_stats")
@Cacheable
public class CampaignStats {

	@Id
	@Field("camp_stat_id")
	private String campStatId;
	@Field("campaign_id")
	@JsonSerialize(using = ToStringSerializer.class)
	private ObjectId campaignId;
	@Field("html_recipients")
	private Integer htmlRecipients;
	@Field("total_pushed")
	private Integer totalPushed;
	@Field("total_email_opens")
	private Integer totalEmailOpens;
	@Field("email_open_unique")
	private Integer emailOpensUnique;
	@Field("total_link_clicks")
	private Integer totalLinkClicks;
	@Field("link_clicks_unique")
	private Integer linkClicksUnique;
	@Field("mobile_opens")
	private Integer mobileOpens;
	@Field("mobile_clicks")
	private Integer mobileClicks;
	@Field("soft_bounce_count")
	private Integer softBounceCount;
	@Field("hard_bounce_count")
	private Integer hardBounceCount;
	@Field("bounce_count_unknown")
	private Integer bounceCountUnknown;
	@Field("bounce_remove_count")
	private Integer bounceRemoveCount;
	@Field("unsubscribe_count")
	private Integer unsubscribeCount;
	@Field("unsubscribe_count_gmail")
	private Integer unsubscribeCountGmail;
	@Field("unsubscribe_remove_count")
	private Integer unsubscribeRemoveCount;
	@Field("campaign_type")
	private String campaignType;
	@Field("rto_type")
	private boolean rtoType;
	@Field("tags")
	private String tags;
	@Field("rto_phase")
	private String rtoPhase;
	public String getCampStatId() {
		return campStatId;
	}
	public void setCampStatId(String campStatId) {
		this.campStatId = campStatId;
	}
	public ObjectId getCampaignId() {
		return campaignId;
	}
	public void setCampaignId(ObjectId campaignId) {
		this.campaignId = campaignId;
	}
	public Integer getHtmlRecipients() {
		return htmlRecipients;
	}
	public void setHtmlRecipients(Integer htmlRecipients) {
		this.htmlRecipients = htmlRecipients;
	}
	public Integer getTotalPushed() {
		return totalPushed;
	}
	public void setTotalPushed(Integer totalPushed) {
		this.totalPushed = totalPushed;
	}
	public Integer getTotalEmailOpens() {
		return totalEmailOpens;
	}
	public void setTotalEmailOpens(Integer totalEmailOpens) {
		this.totalEmailOpens = totalEmailOpens;
	}
	public Integer getEmailOpensUnique() {
		return emailOpensUnique;
	}
	public void setEmailOpensUnique(Integer emailOpensUnique) {
		this.emailOpensUnique = emailOpensUnique;
	}
	public Integer getTotalLinkClicks() {
		return totalLinkClicks;
	}
	public void setTotalLinkClicks(Integer totalLinkClicks) {
		this.totalLinkClicks = totalLinkClicks;
	}
	public Integer getLinkClicksUnique() {
		return linkClicksUnique;
	}
	public void setLinkClicksUnique(Integer linkClicksUnique) {
		this.linkClicksUnique = linkClicksUnique;
	}
	public Integer getMobileOpens() {
		return mobileOpens;
	}
	public void setMobileOpens(Integer mobileOpens) {
		this.mobileOpens = mobileOpens;
	}
	public Integer getMobileClicks() {
		return mobileClicks;
	}
	public void setMobileClicks(Integer mobileClicks) {
		this.mobileClicks = mobileClicks;
	}
	public Integer getSoftBounceCount() {
		return softBounceCount;
	}
	public void setSoftBounceCount(Integer softBounceCount) {
		this.softBounceCount = softBounceCount;
	}
	public Integer getHardBounceCount() {
		return hardBounceCount;
	}
	public void setHardBounceCount(Integer hardBounceCount) {
		this.hardBounceCount = hardBounceCount;
	}
	public Integer getBounceCountUnknown() {
		return bounceCountUnknown;
	}
	public void setBounceCountUnknown(Integer bounceCountUnknown) {
		this.bounceCountUnknown = bounceCountUnknown;
	}
	public Integer getBounceRemoveCount() {
		return bounceRemoveCount;
	}
	public void setBounceRemoveCount(Integer bounceRemoveCount) {
		this.bounceRemoveCount = bounceRemoveCount;
	}
	public Integer getUnsubscribeCount() {
		return unsubscribeCount;
	}
	public void setUnsubscribeCount(Integer unsubscribeCount) {
		this.unsubscribeCount = unsubscribeCount;
	}
	public Integer getUnsubscribeCountGmail() {
		return unsubscribeCountGmail;
	}
	public void setUnsubscribeCountGmail(Integer unsubscribeCountGmail) {
		this.unsubscribeCountGmail = unsubscribeCountGmail;
	}
	public Integer getUnsubscribeRemoveCount() {
		return unsubscribeRemoveCount;
	}
	public void setUnsubscribeRemoveCount(Integer unsubscribeRemoveCount) {
		this.unsubscribeRemoveCount = unsubscribeRemoveCount;
	}
	public String getCampaignType() {
		return campaignType;
	}
	public void setCampaignType(String campaignType) {
		this.campaignType = campaignType;
	}
	public boolean isRtoType() {
		return rtoType;
	}
	public void setRtoType(boolean rtoType) {
		this.rtoType = rtoType;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public String getRtoPhase() {
		return rtoPhase;
	}
	public void setRtoPhase(String rtoPhase) {
		this.rtoPhase = rtoPhase;
	}
	
	

}
