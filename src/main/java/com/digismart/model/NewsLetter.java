package com.digismart.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "newsletter")
public class NewsLetter {
	@Id
	private ObjectId id;
	private String name;
	private String sender_name;
	private String type;
	private ObjectId client_id;
	private ObjectId customer_id;
	private ObjectId category_id;
	private ObjectId subcategory_id;
	private String subject;
	private String product_id;
	private String customer_name;
	private String html_content;
	private String no_of_images;
	private String no_of_links;
	private boolean is_deleted;
	private boolean status;
	private String create_date;
	private String last_used;

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSender_name() {
		return sender_name;
	}

	public void setSender_name(String sender_name) {
		this.sender_name = sender_name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getCustomer_name() {
		return customer_name;
	}

	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}

	public String getHtml_content() {
		return html_content;
	}

	public void setHtml_content(String html_content) {
		this.html_content = html_content;
	}

	public String getNo_of_images() {
		return no_of_images;
	}

	public void setNo_of_images(String no_of_images) {
		this.no_of_images = no_of_images;
	}

	public String getNo_of_links() {
		return no_of_links;
	}

	public void setNo_of_links(String no_of_links) {
		this.no_of_links = no_of_links;
	}

	public boolean isIs_deleted() {
		return is_deleted;
	}

	public void setIs_deleted(boolean is_deleted) {
		this.is_deleted = is_deleted;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}

	public String getLast_used() {
		return last_used;
	}

	public void setLast_used(String last_used) {
		this.last_used = last_used;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NewsLetter [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", sender_name=");
		builder.append(sender_name);
		builder.append(", type=");
		builder.append(type);
		builder.append(", client_id=");
		builder.append(client_id);
		builder.append(", customer_id=");
		builder.append(customer_id);
		builder.append(", category_id=");
		builder.append(category_id);
		builder.append(", subcategory_id=");
		builder.append(subcategory_id);
		builder.append(", subject=");
		builder.append(subject);
		builder.append(", product_id=");
		builder.append(product_id);
		builder.append(", customer_name=");
		builder.append(customer_name);
		builder.append(", html_content=");
		builder.append(html_content);
		builder.append(", no_of_images=");
		builder.append(no_of_images);
		builder.append(", no_of_links=");
		builder.append(no_of_links);
		builder.append(", is_deleted=");
		builder.append(is_deleted);
		builder.append(", status=");
		builder.append(status);
		builder.append(", create_date=");
		builder.append(create_date);
		builder.append(", last_used=");
		builder.append(last_used);
		builder.append("]");
		return builder.toString();
	}

	

}