package com.digismart.model;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * 
 * @author Shivani Aggarwal
 *
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "planning_master")
@Cacheable
public class PlanningMaster {

	@Id
	@Field("plan_id")
	private String planId;
	@Field("autoincrement_id")
	private long autoincrementId;
	@NotEmpty
	@Field("campaign_name")
	private String campaignName;
	@NotNull
	@Field("newsletter_id")
	@JsonSerialize(using = ToStringSerializer.class)
	private ObjectId newsletterId;
	@Field("newsletter_name")
	private String newsletterName;
	@Field("create_date")
	private Date createDate;
	@NotNull
	@Field("schedule_count")
	private Integer scheduleCount;
	@NotNull
	@Field("segment_id")
	@JsonSerialize(using = ToStringSerializer.class)
	private ObjectId segmentId;
	@Field("segment_name")
	private String segmentName;
	@NotEmpty
	@Field("schedule_date")
	private String scheduleDate;
	@Field("schedule_time")
	private Date scheduleTime;
	@Field("segment_count")
	private Integer segmentCount;
	@Field("available_volume")
	private Integer availableVolume;
	@Field("user_capping")
	private Integer userCapping;
	@Field("campaign_restriction")
	private Integer campaignRestriction;
	@Field("volume_restriction")
	private Integer volumeRestriction;
	@Field("plan_status")
	private String planStatus;
	@Field("planning")
	private boolean isPlanning;
	@Field("campaign_type")
	private String campaignType;
	@Field(value = "domain_name")
	private String domain;
	

	public String getNewsletterName() {
		return newsletterName;
	}

	public void setNewsletterName(String newsletterName) {
		this.newsletterName = newsletterName;
	}

	public Date getScheduleTime() {
		return scheduleTime;
	}

	public void setScheduleTime(Date scheduleTime) {
		this.scheduleTime = scheduleTime;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Integer getScheduleCount() {
		return scheduleCount;
	}

	public void setScheduleCount(Integer scheduleCount) {
		this.scheduleCount = scheduleCount;
	}

	public String getScheduleDate() {
		return scheduleDate;
	}

	public void setScheduleDate(String scheduleDate) {
		this.scheduleDate = scheduleDate;
	}

	public Integer getSegmentCount() {
		return segmentCount;
	}

	public void setSegmentCount(Integer segmentCount) {
		this.segmentCount = segmentCount;
	}

	public Integer getAvailableVolume() {
		return availableVolume;
	}

	public void setAvailableVolume(Integer availableVolume) {
		this.availableVolume = availableVolume;
	}

	public Integer getUserCapping() {
		return userCapping;
	}

	public void setUserCapping(Integer userCapping) {
		this.userCapping = userCapping;
	}

	public Integer getCampaignRestriction() {
		return campaignRestriction;
	}

	public void setCampaignRestriction(Integer campaignRestriction) {
		this.campaignRestriction = campaignRestriction;
	}

	public Integer getVolumeRestriction() {
		return volumeRestriction;
	}

	public void setVolumeRestriction(Integer volumeRestriction) {
		this.volumeRestriction = volumeRestriction;
	}

	public String getPlanStatus() {
		return planStatus;
	}

	public void setPlanStatus(String planStatus) {
		this.planStatus = planStatus;
	}

	public String getSegmentName() {
		return segmentName;
	}

	public void setSegmentName(String segmentName) {
		this.segmentName = segmentName;
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

	public ObjectId getSegmentId() {
		return segmentId;
	}

	public void setSegmentId(ObjectId segmentId) {
		this.segmentId = segmentId;
	}

	public long getAutoincrementId() {
		return autoincrementId;
	}

	public void setAutoincrementId(long autoincrementId) {
		this.autoincrementId = autoincrementId;
	}

	public Boolean getIsPlanning() {
		return isPlanning;
	}

	public void setIsPlanning(Boolean isPlanning) {
		this.isPlanning = isPlanning;
	}

	public String getCampaignType() {
		return campaignType;
	}

	public void setCampaignType(String campaignType) {
		this.campaignType = campaignType;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public void setPlanning(boolean isPlanning) {
		this.isPlanning = isPlanning;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PlanningMaster [planId=");
		builder.append(planId);
		builder.append(", autoincrementId=");
		builder.append(autoincrementId);
		builder.append(", campaignName=");
		builder.append(campaignName);
		builder.append(", newsletterId=");
		builder.append(newsletterId);
		builder.append(", newsletterName=");
		builder.append(newsletterName);
		builder.append(", createDate=");
		builder.append(createDate);
		builder.append(", scheduleCount=");
		builder.append(scheduleCount);
		builder.append(", segmentId=");
		builder.append(segmentId);
		builder.append(", segmentName=");
		builder.append(segmentName);
		builder.append(", scheduleDate=");
		builder.append(scheduleDate);
		builder.append(", scheduleTime=");
		builder.append(scheduleTime);
		builder.append(", segmentCount=");
		builder.append(segmentCount);
		builder.append(", availableVolume=");
		builder.append(availableVolume);
		builder.append(", userCapping=");
		builder.append(userCapping);
		builder.append(", campaignRestriction=");
		builder.append(campaignRestriction);
		builder.append(", volumeRestriction=");
		builder.append(volumeRestriction);
		builder.append(", planStatus=");
		builder.append(planStatus);
		builder.append(", isPlanning=");
		builder.append(isPlanning);
		builder.append(", campaignType=");
		builder.append(campaignType);
		builder.append("]");
		return builder.toString();
	}

}