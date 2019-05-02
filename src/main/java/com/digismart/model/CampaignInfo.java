package com.digismart.model;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class that can be used to define campaign repetition by using MongoDB
 * collection ("campaign_info")method as a "setter" or "getter" for a logical
 * property (depending on its signature),
 * <p>
 * Default value for created_at (new Date()) indicates that this property set
 * the current time to database, but it can be specified to specify different
 * name by using "setter".
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "campaign_info")
public class CampaignInfo {

	@Id
	@JsonProperty
	private String id;
	@JsonProperty
	private Date expire_at;
	@JsonProperty
	private Date created_at = new Date();
	@JsonProperty
	private ObjectId client_id;
	@JsonProperty
	private ObjectId customer_id;
	@JsonProperty
	private ObjectId category_id;
	@JsonProperty
	private ObjectId subcategory_id;
	@JsonProperty
	private String email_id;
	@JsonProperty
	private String added_date;
	private String campaign_type;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getExpire_at() {
		return expire_at;
	}

	public void setExpire_at(Date expire_at) {
		this.expire_at = expire_at;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
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

	public String getEmail_id() {
		return email_id;
	}

	public void setEmail_id(String email_id) {
		this.email_id = email_id;
	}

	public String getAdded_date() {
		return added_date;
	}

	public void setAdded_date(String added_date) {
		this.added_date = added_date;
	}

	public String getCampaign_type() {
		return campaign_type;
	}

	public void setCampaign_type(String campaign_type) {
		this.campaign_type = campaign_type;
	}

}
