package com.digismart.model;

public class MailAudience {

	private String id, email_id, mobile_no = "", first_name = "", 
			last_name, default_domain, secondary_domain, product;
	private int promo_enable;

	public String getName() {
		return this.first_name + " " + this.last_name;
	}

	public String getEmail_id() {
		return email_id;
	}

	public void setEmail_id(String email_id) {
		this.email_id = email_id;
	}

	public String getMobile_no() {
		return mobile_no;
	}

	public void setMobile_no(String mobile_no) {
		this.mobile_no = mobile_no;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getDefault_domain() {
		return default_domain;
	}

	public void setDefault_domain(String default_domain) {
		this.default_domain = default_domain;
	}

	public String getSecondary_domain() {
		return secondary_domain;
	}

	public void setSecondary_domain(String secondary_domain) {
		this.secondary_domain = secondary_domain;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public int getPromo_enable() {
		return promo_enable;
	}

	public void setPromo_enable(int promo_enable) {
		this.promo_enable = promo_enable;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
