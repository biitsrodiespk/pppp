package com.digismart.model;
import org.bson.types.ObjectId;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * 
 * @author Sumit Sharma
 *
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "link_master")
@Cacheable
public class LinkMaster {

	@Id
	private String linkId;
	@Field("url")
	private String url;
	@Field("campaign_id")
	@JsonSerialize(using = ToStringSerializer.class)
	private ObjectId campaign_id;
	@Field("type")
	private String type;
	@Field("total_clicks")
	private Integer total_clicks;
	@Field("unique_clicks")
	private Integer unique_clicks;
	@Field("autoincrement_id")
	private Integer autoincrementId;
	public String getLinkId() {
		return linkId;
	}
	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public ObjectId getCampaign_id() {
		return campaign_id;
	}
	public void setCampaign_id(ObjectId campaign_id) {
		this.campaign_id = campaign_id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getTotal_clicks() {
		return total_clicks;
	}
	public void setTotal_clicks(Integer total_clicks) {
		this.total_clicks = total_clicks;
	}
	public Integer getUnique_clicks() {
		return unique_clicks;
	}
	public void setUnique_clicks(Integer unique_clicks) {
		this.unique_clicks = unique_clicks;
	}
	public Integer getAutoincrementId() {
		return autoincrementId;
	}
	public void setAutoincrementId(Integer autoincrementId) {
		this.autoincrementId = autoincrementId;
	}


}