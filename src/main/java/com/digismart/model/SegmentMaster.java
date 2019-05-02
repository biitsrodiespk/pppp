package com.digismart.model;

import java.util.Date;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "segment_master")
@Cacheable
public class SegmentMaster {
	@Id
	@Field("segment_id")
	private String segment_id;
	@Field("autoincrement_id")
	private long autoincrementId;
	@Field("segment_name")
	private String segmentName;
	@Field("count")
	private Long count;
	@Field("description")
	private String description;
	@Field("last_used")
	private Date last_used;
	@Field("type")
	private String type;
	@Field("last_generated")
	private Date last_generated;
	@Field("created_date")
	private Date createdDate;

	@Field("segment_collection_name")
	private String segmentCollectionName;
	@Field("merge_id")
	private String mergeId;

	public String getSegment_id() {
		return segment_id;
	}

	public void setSegment_id(String segment_id) {
		this.segment_id = segment_id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getLast_used() {
		return last_used;
	}

	public void setLast_used(Date last_used) {
		this.last_used = last_used;
	}

	public Date getLast_generated() {
		return last_generated;
	}

	public void setLast_generated(Date last_generated) {
		this.last_generated = last_generated;
	}

	public String getSegmentName() {
		return segmentName;
	}

	public void setSegmentName(String segmentName) {
		this.segmentName = segmentName;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public long getAutoincrementId() {
		return autoincrementId;
	}

	public void setAutoincrementId(long autoincrementId) {
		this.autoincrementId = autoincrementId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSegmentCollectionName() {
		return segmentCollectionName;
	}

	public void setSegmentCollectionName(String segmentCollectionName) {
		this.segmentCollectionName = segmentCollectionName;
	}

	public String getMergeId() {
		return mergeId;
	}

	public void setMergeId(String mergeId) {
		this.mergeId = mergeId;
	}

}
