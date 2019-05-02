package com.digismart.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "campaign_master")
public class ScheduleCampaign {

	@Id
	private String id;
	@Field(value = "campaign_name")
	private String campaignName;
	@Field(value = "newsletter_id")
	private String newsletterId;
	@Field(value = "subject_line")
	private String subjectLine;
	@Field(value = "sender_name")
	private String senderName;
	@Field(value = "from_email")
	private String fromEmail;
	@Field(value = "campaign_status")
	private String campaignStatus;
	@Field(value = "is_deleted")
	private Boolean isDeleted;
	@Field(value = "segment_id")
	private String segmentId;

	private Boolean rto1;
	private Boolean rto2;
	private String domain;

	@Field(value = "open_sms_active")
	private Boolean openSMSActive;
	@Field(value = "open_sms")
	private String openSMS;
	@Field(value = "click_sms_active")
	private Boolean clickSMSActive;
	@Field(value = "click_sms")
	private String clickSMS;
	@Field(value = "rto1_campaign_id")
	private String rto1CampaignId;
	@Field(value = "rto1_newsletter_id")
	private String rto1NewsletterId;
	@Field(value = "rto2_newsletter_id")
	private String rto2NewsletterId;
	@Field(value = "campaign_type")
	private String campaignType;
	@Field(value = "rto_type")
	private Boolean rtoType;
	@Field(value = "scheduled_count")
	private int scheduledCount;
	@Field(value = "start_time")
	private String startTime;
	@Field(value = "create_time")
	private String createTime;
	private String tags;
	private String schedule_type;
	private String domain_name;
	@Field("reply_to_email")
	private String replyToEmail;
	private String schedule_date;
	private String seq_campaign_tag;
	private String seq_campaign_category;
	private String seq_campaign_subcategory;
	private String seq_client_id;
	private String seq_customer_id;
	private String seq_segment_id;
	private String seq_newsletter_id;

	@Field("schedule_time")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
	private Date schedule_time;

	public Date getSchedule_time() {
		return schedule_time;
	}

	public void setSchedule_time(Date schedule_time) {
		this.schedule_time = schedule_time;
	}

	public String getReplyToEmail() {
		return replyToEmail;
	}

	public void setReplyToEmail(String replyToEmail) {
		this.replyToEmail = replyToEmail;
	}

	public String getSchedule_type() {
		return schedule_type;
	}

	public void setSchedule_type(String schedule_type) {
		this.schedule_type = schedule_type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}

	public String getNewsletterId() {
		return newsletterId;
	}

	public void setNewsletterId(String newsletterId) {
		this.newsletterId = newsletterId;
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

	public String getCampaignStatus() {
		return campaignStatus;
	}

	public void setCampaignStatus(String campaignStatus) {
		this.campaignStatus = campaignStatus;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getSegmentId() {
		return segmentId;
	}

	public void setSegmentId(String segmentId) {
		this.segmentId = segmentId;
	}

	public Boolean getRto1() {
		return rto1;
	}

	public void setRto1(Boolean rto1) {
		this.rto1 = rto1;
	}

	public Boolean getRto2() {
		return rto2;
	}

	public void setRto2(Boolean rto2) {
		this.rto2 = rto2;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public Boolean getOpenSMSActive() {
		return openSMSActive;
	}

	public void setOpenSMSActive(Boolean openSMSActive) {
		this.openSMSActive = openSMSActive;
	}

	public String getOpenSMS() {
		return openSMS;
	}

	public void setOpenSMS(String openSMS) {
		this.openSMS = openSMS;
	}

	public Boolean getClickSMSActive() {
		return clickSMSActive;
	}

	public void setClickSMSActive(Boolean clickSMSActive) {
		this.clickSMSActive = clickSMSActive;
	}

	public String getClickSMS() {
		return clickSMS;
	}

	public void setClickSMS(String clickSMS) {
		this.clickSMS = clickSMS;
	}

	public String getRto1CampaignId() {
		return rto1CampaignId;
	}

	public void setRto1CampaignId(String rto1CampaignId) {
		this.rto1CampaignId = rto1CampaignId;
	}

	public String getRto1NewsletterId() {
		return rto1NewsletterId;
	}

	public void setRto1NewsletterId(String rto1NewsletterId) {
		this.rto1NewsletterId = rto1NewsletterId;
	}

	public String getRto2NewsletterId() {
		return rto2NewsletterId;
	}

	public void setRto2NewsletterId(String rto2NewsletterId) {
		this.rto2NewsletterId = rto2NewsletterId;
	}

	public String getCampaignType() {
		return campaignType;
	}

	public void setCampaignType(String campaignType) {
		this.campaignType = campaignType;
	}

	public Boolean getRtoType() {
		return rtoType;
	}

	public void setRtoType(Boolean rtoType) {
		this.rtoType = rtoType;
	}

	public int getScheduledCount() {
		return scheduledCount;
	}

	public void setScheduledCount(int scheduledCount) {
		this.scheduledCount = scheduledCount;
	}

	/*
	 * public String getStartTime() { return startTime; }
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getDomain_name() {
		return domain_name;
	}

	public void setDomain_name(String domain_name) {
		this.domain_name = domain_name;
	}

	public String getSchedule_date() {
		return schedule_date;
	}

	public void setSchedule_date(String schedule_date) {
		this.schedule_date = schedule_date;
	}

	public String getSeq_campaign_tag() {
		return seq_campaign_tag;
	}

	public void setSeq_campaign_tag(String seq_campaign_tag) {
		this.seq_campaign_tag = seq_campaign_tag;
	}

	public String getSeq_campaign_category() {
		return seq_campaign_category;
	}

	public void setSeq_campaign_category(String seq_campaign_category) {
		this.seq_campaign_category = seq_campaign_category;
	}

	public String getSeq_campaign_subcategory() {
		return seq_campaign_subcategory;
	}

	public void setSeq_campaign_subcategory(String seq_campaign_subcategory) {
		this.seq_campaign_subcategory = seq_campaign_subcategory;
	}

	public String getSeq_client_id() {
		return seq_client_id;
	}

	public void setSeq_client_id(String seq_client_id) {
		this.seq_client_id = seq_client_id;
	}

	public String getSeq_customer_id() {
		return seq_customer_id;
	}

	public void setSeq_customer_id(String seq_customer_id) {
		this.seq_customer_id = seq_customer_id;
	}

	public String getSeq_segment_id() {
		return seq_segment_id;
	}

	public void setSeq_segment_id(String seq_segment_id) {
		this.seq_segment_id = seq_segment_id;
	}

	public String getSeq_newsletter_id() {
		return seq_newsletter_id;
	}

	public void setSeq_newsletter_id(String seq_newsletter_id) {
		this.seq_newsletter_id = seq_newsletter_id;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ScheduleCampaign [id=");
		builder.append(id);
		builder.append(", campaignName=");
		builder.append(campaignName);
		builder.append(", newsletterId=");
		builder.append(newsletterId);
		builder.append(", subjectLine=");
		builder.append(subjectLine);
		builder.append(", senderName=");
		builder.append(senderName);
		builder.append(", fromEmail=");
		builder.append(fromEmail);
		builder.append(", campaignStatus=");
		builder.append(campaignStatus);
		builder.append(", isDeleted=");
		builder.append(isDeleted);
		builder.append(", segmentId=");
		builder.append(segmentId);
		builder.append(", rto1=");
		builder.append(rto1);
		builder.append(", rto2=");
		builder.append(rto2);
		builder.append(", domain=");
		builder.append(domain);
		builder.append(", openSMSActive=");
		builder.append(openSMSActive);
		builder.append(", openSMS=");
		builder.append(openSMS);
		builder.append(", clickSMSActive=");
		builder.append(clickSMSActive);
		builder.append(", clickSMS=");
		builder.append(clickSMS);
		builder.append(", rto1CampaignId=");
		builder.append(rto1CampaignId);
		builder.append(", rto1NewsletterId=");
		builder.append(rto1NewsletterId);
		builder.append(", rto2NewsletterId=");
		builder.append(rto2NewsletterId);
		builder.append(", campaignType=");
		builder.append(campaignType);
		builder.append(", rtoType=");
		builder.append(rtoType);
		builder.append(", scheduledCount=");
		builder.append(scheduledCount);
		builder.append(", startTime=");
		builder.append(startTime);
		builder.append(", createTime=");
		builder.append(createTime);
		builder.append(", tags=");
		builder.append(tags);
		builder.append(", schedule_type=");
		builder.append(schedule_type);
		builder.append(", domain_name=");
		builder.append(domain_name);
		builder.append(", replyToEmail=");
		builder.append(replyToEmail);
		builder.append(", schedule_date=");
		builder.append(schedule_date);
		builder.append(", seq_campaign_tag=");
		builder.append(seq_campaign_tag);
		builder.append(", seq_campaign_category=");
		builder.append(seq_campaign_category);
		builder.append(", seq_campaign_subcategory=");
		builder.append(seq_campaign_subcategory);
		builder.append(", seq_client_id=");
		builder.append(seq_client_id);
		builder.append(", seq_customer_id=");
		builder.append(seq_customer_id);
		builder.append(", seq_segment_id=");
		builder.append(seq_segment_id);
		builder.append(", seq_newsletter_id=");
		builder.append(seq_newsletter_id);
		builder.append(", schedule_time=");
		builder.append(schedule_time);
		builder.append("]");
		return builder.toString();
	}

}
