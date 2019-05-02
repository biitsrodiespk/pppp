package com.digismart.model;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * 
 * @author Sumit Sharma
 *
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "campaign_master")
public class CampaignMaster {

	@Id
	@Field("campaign_id")
	private String campaignId;
	@Field("campaign_name")
	private String campaignName;
	@Field("newsletter_id")
	@JsonSerialize(using = ToStringSerializer.class)
	private ObjectId newsletterId;
	@Field("newsletter_name")
	private String newsletterName;
	@Field("category_id")
	private ObjectId catId;
	@Field("subcategory_id")
	@JsonSerialize(using = ToStringSerializer.class)
	private ObjectId subCategoryId;
	@Field("no_of_images")
	private Integer noOfImages;
	@Field("no_of_links")
	private Integer noOfLinks;
	@Field("subject_line")
	private String subjectLine;
	@Field("sender_name")
	private String senderName;
	@Field("from_email")
	private String fromEmail;
	@Field("bounce_email")
	private String bounceEmail;
	@Field("reply_to_email")
	private String replyToEmail;
	@Field("create_time")
	private Date createTime;
	@Field("start_time")
	private String startTime;
	@Field("finish_time")
	private Date finishTime;
	@Field("scheduled_count")
	private Integer scheduledCount;
	@Field("total_pushed")
	private Integer totalPushed;
	@Field("html_recipients")
	private Integer htmlRecipients;
	@Field("total_link_clicks")
	private Integer totalLinkClicks;
	@Field("total_email_opens")
	private Integer totalEmailOpens;
	@Field("email_open_unique")
	private Integer emailOpensUnique;
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
	@Field("campaign_status")
	private String campaignStatus;
	@Field("is_deleted")
	private boolean isDeleted;
	@Field("segment_id")
	@JsonSerialize(using = ToStringSerializer.class)
	private ObjectId segmentId;
	@Field("rto1")
	private boolean rto1;
	@Field("rto2")
	private boolean rto2;
	@Field("rto3")
	private boolean rto3;
	@Field("rto4")
	private boolean rto4;
	@Field("domain")
	private String domain;
	@Field("open_sms_active")
	private boolean openSMSActive;
	@Field("open_sms")
	private String openSMS;
	@Field("click_sms_active")
	private boolean clickSMSActive;
	@Field("click_sms")
	private String clickSMS;
	@Field("rto1_campaign_id")
	@JsonSerialize(using = ToStringSerializer.class)
	private ObjectId rto1CampaignId;
	@Field("rto1_newsletter_id")
	@JsonSerialize(using = ToStringSerializer.class)
	private ObjectId rto1NewsletterId;
	@Field("rto2_campaign_id")
	@JsonSerialize(using = ToStringSerializer.class)
	private ObjectId rto2CampaignId;
	@Field("rto2_newsletter_id")
	@JsonSerialize(using = ToStringSerializer.class)
	private ObjectId rto2NewsletterId;
	@Field("rto3_campaign_id")
	@JsonSerialize(using = ToStringSerializer.class)
	private ObjectId rto3CampaignId;
	@Field("rto3_newsletter_id")
	@JsonSerialize(using = ToStringSerializer.class)
	private ObjectId rto3NewsletterId;
	@Field("rto4_campaign_id")
	@JsonSerialize(using = ToStringSerializer.class)
	private ObjectId rto4CampaignId;
	@Field("rto4_newsletter_id")
	@JsonSerialize(using = ToStringSerializer.class)
	private ObjectId rto4NewsletterId;
	@Field("campaign_type")
	private String campaignType;
	@Field("rto_type")
	private boolean rtoType;
	@Field("tags")
	private String tags;
	@Field("rto_phase")
	private String rtoPhase;
	public String getCampaignId() {
		return campaignId;
	}
	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}
	public String getCampaignName() {
		return campaignName;
	}
	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}
	public ObjectId getNewsletterId() {
		return newsletterId;
	}
	public void setNewsletterId(ObjectId newsletterId) {
		this.newsletterId = newsletterId;
	}
	public String getNewsletterName() {
		return newsletterName;
	}
	public void setNewsletterName(String newsletterName) {
		this.newsletterName = newsletterName;
	}
	public ObjectId getCatId() {
		return catId;
	}
	public void setCatId(ObjectId catId) {
		this.catId = catId;
	}
	public ObjectId getSubCategoryId() {
		return subCategoryId;
	}
	public void setSubCategoryId(ObjectId subCategoryId) {
		this.subCategoryId = subCategoryId;
	}
	public Integer getNoOfImages() {
		return noOfImages;
	}
	public void setNoOfImages(Integer noOfImages) {
		this.noOfImages = noOfImages;
	}
	public Integer getNoOfLinks() {
		return noOfLinks;
	}
	public void setNoOfLinks(Integer noOfLinks) {
		this.noOfLinks = noOfLinks;
	}
	public String getSubjectLine() {
		return subjectLine;
	}
	public void setSubjectLine(String subjectLine) {
		this.subjectLine = subjectLine;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public String getFromEmail() {
		return fromEmail;
	}
	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}
	public String getBounceEmail() {
		return bounceEmail;
	}
	public void setBounceEmail(String bounceEmail) {
		this.bounceEmail = bounceEmail;
	}
	public String getReplyToEmail() {
		return replyToEmail;
	}
	public void setReplyToEmail(String replyToEmail) {
		this.replyToEmail = replyToEmail;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public Date getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}
	public Integer getScheduledCount() {
		return scheduledCount;
	}
	public void setScheduledCount(Integer scheduledCount) {
		this.scheduledCount = scheduledCount;
	}
	public Integer getTotalPushed() {
		return totalPushed;
	}
	public void setTotalPushed(Integer totalPushed) {
		this.totalPushed = totalPushed;
	}
	public Integer getHtmlRecipients() {
		return htmlRecipients;
	}
	public void setHtmlRecipients(Integer htmlRecipients) {
		this.htmlRecipients = htmlRecipients;
	}
	public Integer getTotalLinkClicks() {
		return totalLinkClicks;
	}
	public void setTotalLinkClicks(Integer totalLinkClicks) {
		this.totalLinkClicks = totalLinkClicks;
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
	public String getCampaignStatus() {
		return campaignStatus;
	}
	public void setCampaignStatus(String campaignStatus) {
		this.campaignStatus = campaignStatus;
	}
	public boolean isDeleted() {
		return isDeleted;
	}
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	public ObjectId getSegmentId() {
		return segmentId;
	}
	public void setSegmentId(ObjectId segmentId) {
		this.segmentId = segmentId;
	}
	public boolean isRto1() {
		return rto1;
	}
	public void setRto1(boolean rto1) {
		this.rto1 = rto1;
	}
	public boolean isRto2() {
		return rto2;
	}
	public void setRto2(boolean rto2) {
		this.rto2 = rto2;
	}
	public boolean isRto3() {
		return rto3;
	}
	public void setRto3(boolean rto3) {
		this.rto3 = rto3;
	}
	public boolean isRto4() {
		return rto4;
	}
	public void setRto4(boolean rto4) {
		this.rto4 = rto4;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public boolean isOpenSMSActive() {
		return openSMSActive;
	}
	public void setOpenSMSActive(boolean openSMSActive) {
		this.openSMSActive = openSMSActive;
	}
	public String getOpenSMS() {
		return openSMS;
	}
	public void setOpenSMS(String openSMS) {
		this.openSMS = openSMS;
	}
	public boolean isClickSMSActive() {
		return clickSMSActive;
	}
	public void setClickSMSActive(boolean clickSMSActive) {
		this.clickSMSActive = clickSMSActive;
	}
	public String getClickSMS() {
		return clickSMS;
	}
	public void setClickSMS(String clickSMS) {
		this.clickSMS = clickSMS;
	}
	public ObjectId getRto1CampaignId() {
		return rto1CampaignId;
	}
	public void setRto1CampaignId(ObjectId rto1CampaignId) {
		this.rto1CampaignId = rto1CampaignId;
	}
	public ObjectId getRto1NewsletterId() {
		return rto1NewsletterId;
	}
	public void setRto1NewsletterId(ObjectId rto1NewsletterId) {
		this.rto1NewsletterId = rto1NewsletterId;
	}
	public ObjectId getRto2CampaignId() {
		return rto2CampaignId;
	}
	public void setRto2CampaignId(ObjectId rto2CampaignId) {
		this.rto2CampaignId = rto2CampaignId;
	}
	public ObjectId getRto2NewsletterId() {
		return rto2NewsletterId;
	}
	public void setRto2NewsletterId(ObjectId rto2NewsletterId) {
		this.rto2NewsletterId = rto2NewsletterId;
	}
	public ObjectId getRto3CampaignId() {
		return rto3CampaignId;
	}
	public void setRto3CampaignId(ObjectId rto3CampaignId) {
		this.rto3CampaignId = rto3CampaignId;
	}
	public ObjectId getRto3NewsletterId() {
		return rto3NewsletterId;
	}
	public void setRto3NewsletterId(ObjectId rto3NewsletterId) {
		this.rto3NewsletterId = rto3NewsletterId;
	}
	public ObjectId getRto4CampaignId() {
		return rto4CampaignId;
	}
	public void setRto4CampaignId(ObjectId rto4CampaignId) {
		this.rto4CampaignId = rto4CampaignId;
	}
	public ObjectId getRto4NewsletterId() {
		return rto4NewsletterId;
	}
	public void setRto4NewsletterId(ObjectId rto4NewsletterId) {
		this.rto4NewsletterId = rto4NewsletterId;
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