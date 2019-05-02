package com.digismart.model;

import com.digismart.util.IPFinderUtils;

public class User {

	private String emailId;
	private String domain;
	private String ip;
	private DomainIP domainIP ;
	private IPFinderUtils ipUtil ;
	private IPAddress ipAddress;
	private String name;
	private String mobileNo;
	private String infra;
	
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

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!emailId.equals(obj))
			return false;
		return true;
	}

	public DomainIP getDomainIP() {
		return domainIP;
	}

	public void setDomainIP(DomainIP domainIP) {
		this.domainIP = domainIP;
	}

	public IPFinderUtils getIpUtil() {
		return ipUtil;
	}

	public void setIpUtil(IPFinderUtils ipUtil) {
		this.ipUtil = ipUtil;
	}

	public IPAddress getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(IPAddress ipAddress) {
		this.ipAddress = ipAddress;
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

	public String getInfra() {
		return infra;
	}

	public void setInfra(String infra) {
		this.infra = infra;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("User [emailId=");
		builder.append(emailId);
		builder.append(", domain=");
		builder.append(domain);
		builder.append(", ip=");
		builder.append(ip);
		builder.append(", domainIP=");
		builder.append(domainIP);
		builder.append(", ipUtil=");
		builder.append(ipUtil);
		builder.append(", ipAddress=");
		builder.append(ipAddress);
		builder.append(", name=");
		builder.append(name);
		builder.append(", mobileNo=");
		builder.append(mobileNo);
		builder.append(", infra=");
		builder.append(infra);
		builder.append("]");
		return builder.toString();
	}

	
	
	
}
