package com.digismart.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.digismart.config.ApplicationProperties;
import com.digismart.model.MailData;
import com.digismart.model.ResponseDTO;

@Service
public class RestConnector {
	private Logger log = LoggerFactory.getLogger(RestConnector.class);
	/*
	 * @Value("${mail.data.url}") String mailDataUrl;
	 */

	@Autowired
	ApplicationProperties applicationProperties;

	public String executePost(MailData mailData) {
		HttpEntity<MailData> httpEntity = new HttpEntity<>(mailData);
		RestTemplate restTemplate = new RestTemplate();
		ResponseDTO response = restTemplate.postForObject(applicationProperties.getMail_data_url(), httpEntity,
				ResponseDTO.class);
		return response.getResponseMessage();
	}

	public String executePostBulk(BulkMailData mailData) {
		log.debug("api url {}", applicationProperties.getBulk_mail_data_url());
		HttpEntity<BulkMailData> httpEntity = new HttpEntity<>(mailData);
		RestTemplate restTemplate = new RestTemplate();
		ResponseDTO response = restTemplate.postForObject(applicationProperties.getBulk_mail_data_url(), httpEntity,
				ResponseDTO.class);
		return response.getResponseMessage();
	}
}
