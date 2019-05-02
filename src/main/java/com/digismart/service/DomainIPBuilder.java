package com.digismart.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digismart.model.Domain;
import com.digismart.model.DomainIP;
import com.digismart.model.IPAddress;
import com.digismart.model.PlanningMaster;
import com.digismart.util.IPFinderUtils;
import com.digismart.util.MongoConnection;

@Service
public class DomainIPBuilder {
	private static Logger log = LoggerFactory.getLogger(DomainIPBuilder.class);

	@Autowired
	MongoDBService mongoDBService;

	public HashMap<String, DomainIP> build(String date, MongoConnection mongoConnection, String campaignType,
			PlanningMaster planningMaster) {
		List<Domain> domainList = mongoDBService.getAllDomains(planningMaster.getDomain());

		HashMap<String, DomainIP> domainIpMap = new HashMap<>();
		for (Domain domain : domainList) {
			List<IPAddress> ipAddressList = mongoDBService.getInfraIpLimit(domain);
			ipAddressList = buildIPVolume(ipAddressList, date, mongoConnection, campaignType);
			if(domainIpMap.containsKey(domain.getDomainName())){
				DomainIP dIp = domainIpMap.get(domain.getDomainName());
				List<IPAddress> ipAddList = dIp.getIpAddressList();
				ipAddList.addAll(ipAddressList);
				for(IPAddress ip : ipAddressList){
					ip.setInfra(domain.getInfra());
					ipAddList.add(ip);
				}
				IPFinderUtils ipUtilL = new IPFinderUtils(ipAddList);
				domainIpMap.put(domain.getDomainName(), new DomainIP(domain, new ArrayList<>(ipAddList), ipUtilL));
			}else{
				List<IPAddress> ipAddList = new ArrayList<IPAddress>();
				ipAddList.addAll(ipAddressList);
				for(IPAddress ip : ipAddressList){
					ip.setInfra(domain.getInfra());
					ipAddList.add(ip);
				}
				IPFinderUtils ipUtilL = new IPFinderUtils(ipAddList);
				domainIpMap.put(domain.getDomainName(), new DomainIP(domain, new ArrayList<>(ipAddList), ipUtilL));
			}
			
		}
		return domainIpMap;
	}

	private List<IPAddress> buildIPVolume(List<IPAddress> ips, String date, MongoConnection mongoConnection,
			String campaignType) {
		List<IPAddress> list = new ArrayList<>();
		for (IPAddress ip : ips) {
			long currentUsed = mongoDBService.getCurrentIpLimit(ip.getIpaddress(), date, mongoConnection, campaignType);
			long remaining = 0l;
			if ("Promo".equalsIgnoreCase(campaignType.trim())) {
				remaining = ip.getMax_volume_promo() - currentUsed;
			} else if ("Engagement".equalsIgnoreCase(campaignType.trim())) {
				remaining = ip.getMax_volume_engagement() - currentUsed;
			}
			ip.setMaximumVolume(remaining);
			log.debug("remaining :::: " + remaining);
			if (remaining > 0) {
				list.add(ip);
			}

		}
		return list;
	}
}
