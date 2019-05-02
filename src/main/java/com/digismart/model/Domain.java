package com.digismart.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Class that can be used to define domain's by using MongoDB collection
 * ("domain_master")method as a "setter" or "getter" for a logical property
 * (depending on its signature).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "domain_master")
public class Domain {

	@Id
	private String id;
	@Field(value = "domain_name")
	private String domainName;
	
	@Field(value = "infra")
	private String infra;
	
	private boolean isDeleted;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getInfra() {
		return infra;
	}

	public void setInfra(String infra) {
		this.infra = infra;
	}

	
}
