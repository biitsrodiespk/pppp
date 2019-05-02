package com.digismart.model;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 * @author Shivani Aggarwal
 *
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "planned_volume")
public class PlannedVolume {
	
	@Id
	@Field("plan_vol_id")
	private String planVolId;
	@Field("autoincrement_id")
	private long autoincrementId;
	@Field("create_date")
	private Date createDate;
	@Field("schedule_date")
	private String scheduleDate;
	@Field("total_volume")
	private long totalVolume;
	@Field("planned_volume")
	private long plannedVolume;
	@Field("available_volume")
	private long availableVolume;
	@Field("planned")
	private double planned;
	@Field("scheduled")
	private double scheduled;
	@Field("scheduled_volume")
	private long scheduledVolume;
	@Field("last_used")
	private Date lastUsed;
	@Field("campaign_type")
	private String campaignType;
	

	public String getPlanVolId() {
		return planVolId;
	}

	public void setPlanVolId(String planVolId) {
		this.planVolId = planVolId;
	}

	public long getTotalVolume() {
		return totalVolume;
	}

	public void setTotalVolume(long totalVolume) {
		this.totalVolume = totalVolume;
	}

	public long getPlannedVolume() {
		return plannedVolume;
	}

	public void setPlannedVolume(long plannedVolume) {
		this.plannedVolume = plannedVolume;
	}

	public double getPlanned() {
		return planned;
	}

	public void setPlanned(double planned) {
		this.planned = planned;
	}

	public double getScheduled() {
		return scheduled;
	}

	public void setScheduled(double scheduled) {
		this.scheduled = scheduled;
	}

	public Date getLastUsed() {
		return lastUsed;
	}

	public void setLast_used(Date lastUsed) {
		this.lastUsed = lastUsed;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getScheduleDate() {
		return scheduleDate;
	}

	public void setScheduleDate(String scheduleDate) {
		this.scheduleDate = scheduleDate;
	}

	public long getAvailableVolume() {
		return availableVolume;
	}

	public void setAvailableVolume(long availableVolume) {
		this.availableVolume = availableVolume;
	}

	public long getAutoincrementId() {
		return autoincrementId;
	}

	public void setAutoincrementId(long autoincrementId) {
		this.autoincrementId = autoincrementId;
	}

	
	public long getScheduledVolume() {
		return scheduledVolume;
	}

	public void setScheduledVolume(long scheduledVolume) {
		this.scheduledVolume = scheduledVolume;
	}

	public void setLastUsed(Date lastUsed) {
		this.lastUsed = lastUsed;
	}

	public String getCampaignType() {
		return campaignType;
	}

	public void setCampaignType(String campaignType) {
		this.campaignType = campaignType;
	}
	

}