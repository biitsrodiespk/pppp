package com.digismart.util;

import java.util.List;

import com.digismart.model.MailData;

public class BulkMailData {
	private List<MailData> mailList;
	private String type;

	public List<MailData> getMailList() {
		return mailList;
	}

	public void setMailList(List<MailData> mailList) {
		this.mailList = mailList;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}