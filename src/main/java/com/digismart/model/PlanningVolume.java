package com.digismart.model;

public class PlanningVolume {

	private long mailLimitUser;
	private long campaignLimit;
	private long scheduleCount;
	private long segmentTotalCount;
	private long availableVolume;
	private long filterUserCapping;
	private long filterCampaignRestriction;
	private long filterVolumeRestriction;
	private long campaignBlockDays;
	private long totalSavedInDB;
	public long getMailLimitUser() {
		return mailLimitUser;
	}
	public void setMailLimitUser(long mailLimitUser) {
		this.mailLimitUser = mailLimitUser;
	}
	public long getCampaignLimit() {
		return campaignLimit;
	}
	public void setCampaignLimit(long campaignLimit) {
		this.campaignLimit = campaignLimit;
	}
	public long getScheduleCount() {
		return scheduleCount;
	}
	public void setScheduleCount(long scheduleCount) {
		this.scheduleCount = scheduleCount;
	}
	public long getSegmentTotalCount() {
		return segmentTotalCount;
	}
	public void setSegmentTotalCount(long segmentTotalCount) {
		this.segmentTotalCount = segmentTotalCount;
	}
	public long getAvailableVolume() {
		return availableVolume;
	}
	public void setAvailableVolume(long availableVolume) {
		this.availableVolume = availableVolume;
	}
	public long getFilterUserCapping() {
		return filterUserCapping;
	}
	public void setFilterUserCapping(long filterUserCapping) {
		this.filterUserCapping = filterUserCapping;
	}
	public long getFilterCampaignRestriction() {
		return filterCampaignRestriction;
	}
	public void setFilterCampaignRestriction(long filterCampaignRestriction) {
		this.filterCampaignRestriction = filterCampaignRestriction;
	}
	public long getFilterVolumeRestriction() {
		return filterVolumeRestriction;
	}
	public void setFilterVolumeRestriction(long filterVolumeRestriction) {
		this.filterVolumeRestriction = filterVolumeRestriction;
	}
	public long getCampaignBlockDays() {
		return campaignBlockDays;
	}
	public void setCampaignBlockDays(long campaignBlockDays) {
		this.campaignBlockDays = campaignBlockDays;
	}
	public long getTotalSavedInDB() {
		return totalSavedInDB;
	}
	public void setTotalSavedInDB(long totalSavedInDB) {
		this.totalSavedInDB = totalSavedInDB;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PlanningVolume [mailLimitUser=");
		builder.append(mailLimitUser);
		builder.append(", campaignLimit=");
		builder.append(campaignLimit);
		builder.append(", scheduleCount=");
		builder.append(scheduleCount);
		builder.append(", segmentTotalCount=");
		builder.append(segmentTotalCount);
		builder.append(", availableVolume=");
		builder.append(availableVolume);
		builder.append(", filterUserCapping=");
		builder.append(filterUserCapping);
		builder.append(", filterCampaignRestriction=");
		builder.append(filterCampaignRestriction);
		builder.append(", filterVolumeRestriction=");
		builder.append(filterVolumeRestriction);
		builder.append(", campaignBlockDays=");
		builder.append(campaignBlockDays);
		builder.append(", totalSavedInDB=");
		builder.append(totalSavedInDB);
		builder.append("]");
		return builder.toString();
	}

}
