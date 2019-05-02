package com.digismart.model;

import java.util.Date;

import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.digismart.util.ServerConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = ServerConstants.SCHEDULED_MAILS_COLLECTION)
public class MailData {
	@Id
	private String id;
	@NotEmpty(message = "email_id can not be empty")
	private String email_id;

	// @NotEmpty(message = "subscriber_name can not be empty")
	private String subscriber_name;

	private ObjectId campaign_id;
	// @NotEmpty(message = "campaign_type can not be empty")
	private String campaign_type;
	// @NotEmpty(message = "news_letter_id can not be empty")
	private String news_letter_id;

	// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd
	// HH:mm:ss")
	@NotEmpty(message = "schedule_time can not be empty")
	@JsonSerialize(using = ToStringSerializer.class)
	private Date schedule_time;
	// @NotEmpty(message = "schedule_date can not be empty")
	private String schedule_date;
	// @NotEmpty(message = "mail_content can not be empty")
	private String mail_content;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
	private Date created = new Date();
	// @NotEmpty(message = "domain can not be empty")
	private String domain;
	private String infra;
	// @NotEmpty(message = "ip can not be empty")
	private String ip;
	// @NotEmpty(message = "uid can not be empty")
	private String uid;

	// @NotEmpty(message = "unsub_url can not be empty")
	private String unsub_url;
	// @NotEmpty(message = "reply_email can not be empty")
	private String reply_email;
	// @NotEmpty(message = "from_email can not be empty")
	private String from_email;

	private int port;
	// @NotEmpty(message = "subject can not be empty")
	private String subject;
	// @NotEmpty(message = "sender_name can not be empty")
	private String sender_name;
	// @NotEmpty(message = "sending_status can not be empty")
	private String sending_status;

	// @NotEmpty(message = "schedule_type can not be empty")
	private String schedule_type;
	private String message_id;
	private String host_name;
	private String seq_campaign_tag;
	private String seq_campaign_category;
	private String seq_campaign_subcategory;
	private String seq_client_id;
	private String seq_customer_id;
	private String seq_segment_id;
	private String seq_newsletter_id;
	private int rto;

	public int getRto() {
		return rto;
	}

	public void setRto(int rto) {
		this.rto = rto;
	}

	public String getMobile_no() {
		return mobile_no;
	}

	public void setMobile_no(String mobile_no) {
		this.mobile_no = mobile_no;
	}

	private String mobile_no;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail_id() {
		return email_id;
	}

	public void setEmail_id(String email_id) {
		this.email_id = email_id;
	}

	public String getSubscriber_name() {
		return subscriber_name;
	}

	public void setSubscriber_name(String subscriber_name) {
		this.subscriber_name = subscriber_name;
	}

	public ObjectId getCampaign_id() {
		return campaign_id;
	}

	public void setCampaign_id(ObjectId campaign_id) {
		this.campaign_id = campaign_id;
	}

	public String getCampaign_type() {
		return campaign_type;
	}

	public void setCampaign_type(String campaign_type) {
		this.campaign_type = campaign_type;
	}

	public String getNews_letter_id() {
		return news_letter_id;
	}

	public void setNews_letter_id(String news_letter_id) {
		this.news_letter_id = news_letter_id;
	}

	public Date getSchedule_time() {
		return schedule_time;
	}

	public void setSchedule_time(Date schedule_time) {
		this.schedule_time = schedule_time;
	}

	public String getSchedule_date() {
		return schedule_date;
	}

	public void setSchedule_date(String schedule_date) {
		this.schedule_date = schedule_date;
	}

	public String getMail_content() {
		return mail_content;
	}

	public void setMail_content(String mail_content) {
		this.mail_content = mail_content;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
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

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUnsub_url() {
		return unsub_url;
	}

	public void setUnsub_url(String unsub_url) {
		this.unsub_url = unsub_url;
	}

	public String getReply_email() {
		return reply_email;
	}

	public void setReply_email(String reply_email) {
		this.reply_email = reply_email;
	}

	public String getFrom_email() {
		return from_email;
	}

	public void setFrom_email(String from_email) {
		this.from_email = from_email;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSender_name() {
		return sender_name;
	}

	public void setSender_name(String sender_name) {
		this.sender_name = sender_name;
	}

	public String getSending_status() {
		return sending_status;
	}

	public void setSending_status(String sending_status) {
		this.sending_status = sending_status;
	}

	public String getSchedule_type() {
		return schedule_type;
	}

	public void setSchedule_type(String schedule_type) {
		this.schedule_type = schedule_type;
	}

	public String getMessage_id() {
		return message_id;
	}

	public void setMessage_id(String message_id) {
		this.message_id = message_id;
	}

	public String getHost_name() {
		return host_name;
	}

	public void setHost_name(String host_name) {
		this.host_name = host_name;
	}

	public String getSeq_campaign_tag() {
		return seq_campaign_tag;
	}

	public void setSeq_campaign_tag(String seq_campaign_tag) {
		this.seq_campaign_tag = seq_campaign_tag;
	}

	public String getSeq_campaign_category() {
		return seq_campaign_category;
	}

	public void setSeq_campaign_category(String seq_campaign_category) {
		this.seq_campaign_category = seq_campaign_category;
	}

	public String getSeq_campaign_subcategory() {
		return seq_campaign_subcategory;
	}

	public void setSeq_campaign_subcategory(String seq_campaign_subcategory) {
		this.seq_campaign_subcategory = seq_campaign_subcategory;
	}

	public String getSeq_client_id() {
		return seq_client_id;
	}

	public void setSeq_client_id(String seq_client_id) {
		this.seq_client_id = seq_client_id;
	}

	public String getSeq_customer_id() {
		return seq_customer_id;
	}

	public void setSeq_customer_id(String seq_customer_id) {
		this.seq_customer_id = seq_customer_id;
	}

	public String getSeq_segment_id() {
		return seq_segment_id;
	}

	public void setSeq_segment_id(String seq_segment_id) {
		this.seq_segment_id = seq_segment_id;
	}

	public String getSeq_newsletter_id() {
		return seq_newsletter_id;
	}

	public void setSeq_newsletter_id(String seq_newsletter_id) {
		this.seq_newsletter_id = seq_newsletter_id;
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
		builder.append("MailData [id=");
		builder.append(id);
		builder.append(", email_id=");
		builder.append(email_id);
		builder.append(", subscriber_name=");
		builder.append(subscriber_name);
		builder.append(", campaign_id=");
		builder.append(campaign_id);
		builder.append(", campaign_type=");
		builder.append(campaign_type);
		builder.append(", news_letter_id=");
		builder.append(news_letter_id);
		builder.append(", schedule_time=");
		builder.append(schedule_time);
		builder.append(", schedule_date=");
		builder.append(schedule_date);
		builder.append(", created=");
		builder.append(created);
		builder.append(", domain=");
		builder.append(domain);
		builder.append(", infra=");
		builder.append(infra);
		builder.append(", ip=");
		builder.append(ip);
		builder.append(", uid=");
		builder.append(uid);
		builder.append(", unsub_url=");
		builder.append(unsub_url);
		builder.append(", reply_email=");
		builder.append(reply_email);
		builder.append(", from_email=");
		builder.append(from_email);
		builder.append(", port=");
		builder.append(port);
		builder.append(", subject=");
		builder.append(subject);
		builder.append(", sender_name=");
		builder.append(sender_name);
		builder.append(", sending_status=");
		builder.append(sending_status);
		builder.append(", schedule_type=");
		builder.append(schedule_type);
		builder.append(", message_id=");
		builder.append(message_id);
		builder.append(", host_name=");
		builder.append(host_name);
		builder.append(", seq_campaign_tag=");
		builder.append(seq_campaign_tag);
		builder.append(", seq_campaign_category=");
		builder.append(seq_campaign_category);
		builder.append(", seq_campaign_subcategory=");
		builder.append(seq_campaign_subcategory);
		builder.append(", seq_client_id=");
		builder.append(seq_client_id);
		builder.append(", seq_customer_id=");
		builder.append(seq_customer_id);
		builder.append(", seq_segment_id=");
		builder.append(seq_segment_id);
		builder.append(", seq_newsletter_id=");
		builder.append(seq_newsletter_id);
		builder.append(", rto=");
		builder.append(rto);
		builder.append(", mobile_no=");
		builder.append(mobile_no);
		builder.append("]");
		return builder.toString();
	}

	

}