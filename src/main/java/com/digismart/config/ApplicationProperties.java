package com.digismart.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.variables")
public class ApplicationProperties {

	private int query_batch_size;

	private int cursor_batch_size;

	private int thread_size;

	private String aes_key;

	private String track_app_name;

	private String mail_data_url;

	private String bulk_mail_data_url;

	public int getQuery_batch_size() {
		return query_batch_size;
	}

	public void setQuery_batch_size(int query_batch_size) {
		this.query_batch_size = query_batch_size;
	}

	public int getCursor_batch_size() {
		return cursor_batch_size;
	}

	public void setCursor_batch_size(int cursor_batch_size) {
		this.cursor_batch_size = cursor_batch_size;
	}

	public int getThread_size() {
		return thread_size;
	}

	public void setThread_size(int thread_size) {
		this.thread_size = thread_size;
	}

	public String getAes_key() {
		return aes_key;
	}

	public void setAes_key(String aes_key) {
		this.aes_key = aes_key;
	}

	public String getTrack_app_name() {
		return track_app_name;
	}

	public void setTrack_app_name(String track_app_name) {
		this.track_app_name = track_app_name;
	}

	public String getMail_data_url() {
		return mail_data_url;
	}

	public void setMail_data_url(String mail_data_url) {
		this.mail_data_url = mail_data_url;
	}

	public String getBulk_mail_data_url() {
		return bulk_mail_data_url;
	}

	public void setBulk_mail_data_url(String bulk_mail_data_url) {
		this.bulk_mail_data_url = bulk_mail_data_url;
	}

}
