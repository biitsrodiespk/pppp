package com.digismart.model;

import java.util.ArrayList;

import com.digismart.util.IPFinderUtils;

public class DomainIP {

	private Domain domain;
	private ArrayList<IPAddress> ipAddressList;
	private IPFinderUtils ipFinderUtils;

	public DomainIP(Domain domain, ArrayList<IPAddress> ipAddressList, IPFinderUtils ipFinderUtils) {
		super();
		this.domain = domain;
		this.ipAddressList = ipAddressList;
		this.ipFinderUtils = ipFinderUtils;
	}

	public Domain getDomain() {
		return domain;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	public ArrayList<IPAddress> getIpAddressList() {
		return ipAddressList;
	}

	public void setIpAddressList(ArrayList<IPAddress> ipAddressList) {
		this.ipAddressList = ipAddressList;
	}

	public IPFinderUtils getIpFinderUtils() {
		return ipFinderUtils;
	}

	public void setIpFinderUtils(IPFinderUtils ipFinderUtils) {
		this.ipFinderUtils = ipFinderUtils;
	}

	@Override
	public String toString() {
		return "DomainIP [domain=" + domain + ", ipAddressList=" + ipAddressList + ", ipFinderUtils=" + ipFinderUtils
				+ "]";
	}
}
