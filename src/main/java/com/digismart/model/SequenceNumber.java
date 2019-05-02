package com.digismart.model;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "sequence_master")
@Cacheable
public class SequenceNumber {

	@Id
	private String id;
	private String name;
	private long lastcount;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getLastcount() {
		return lastcount;
	}
	public void setLastcount(long lastcount) {
		this.lastcount = lastcount;
	}

	
}
