package com.digismart.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "campaign_day_limit")
public class CampaignDayLimit {
	@Id
	private String id;
	private ObjectId client_id;
	private ObjectId customer_id;
	private ObjectId category_id;
	private ObjectId subcategory_id;
	private String schedule_date;
	private int count;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public ObjectId getClient_id() {
		return client_id;
	}
	public void setClient_id(ObjectId client_id) {
		this.client_id = client_id;
	}
	public ObjectId getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(ObjectId customer_id) {
		this.customer_id = customer_id;
	}
	public ObjectId getCategory_id() {
		return category_id;
	}
	public void setCategory_id(ObjectId category_id) {
		this.category_id = category_id;
	}
	public ObjectId getSubcategory_id() {
		return subcategory_id;
	}
	public void setSubcategory_id(ObjectId subcategory_id) {
		this.subcategory_id = subcategory_id;
	}
	public String getSchedule_date() {
		return schedule_date;
	}
	public void setSchedule_date(String schedule_date) {
		this.schedule_date = schedule_date;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	

}
