package com.digismart.model;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "planned_campaign")
public class PlannedCampaign {

	@Field("plan_id")
	private ObjectId planId;

	@Field("email_id")
	private String emailId;

	@Field("domain")
	private String domain;
	
	@Field("infra")
	private String infra;


	@Field("ip_address")
	private String ipAddress;

	@Field("plan_date")
	private String date;

	@Field("name")
	private String name;

	@Field("mobile_no")
	private String mobileNo;

	@Field("port")
	int port;

	@Field("smtp_host")
	String smtpHost;

	@Field("email_prefix")
	private String emailPrefix;

	public ObjectId getPlanId() {
		return planId;
	}

	public void setPlanId(ObjectId planId) {
		this.planId = planId;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getSmtpHost() {
		return smtpHost;
	}

	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}

	public String getEmailPrefix() {
		return emailPrefix;
	}

	public void setEmailPrefix(String emailPrefix) {
		this.emailPrefix = emailPrefix;
	}

	public String getInfra() {
		return infra;
	}

	public void setInfra(String infra) {
		this.infra = infra;
	}

}
