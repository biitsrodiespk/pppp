package com.digismart.util;

import java.util.concurrent.Future;

import org.bson.types.ObjectId;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import com.digismart.config.ApplicationProperties;
import com.digismart.model.LinkMaster;
import com.digismart.model.MailAudience;
import com.digismart.model.MailData;
import com.digismart.model.NewsLetter;
import com.digismart.model.SSlinks;
import com.digismart.model.ScheduleCampaign;
import com.digismart.model.SequenceNumber;
import com.digismart.model.UserDetailDTO;
import com.digismart.service.MongoDBService;
import com.mongodb.DuplicateKeyException;
import com.mongodb.MongoWriteException;

@Service
public class MailComposer {

	private static final Logger logger = LoggerFactory.getLogger(MailComposer.class);

	@Autowired
	MongoDBService mongoDBConnection;

	@Autowired
	private MongoOperations mongoOperation;
	@Autowired
	MongoTemplate mongoTemplate;
	@Autowired
	private Environment environment;

	// @Value("${aes.key}")
	// private String encryptionKey;
	/*
	 * @Value("${TRACKAPPNAME}") private String TRACKAPPNAME;
	 */

	@Autowired
	ApplicationProperties applicationProperties;

	public static final String trackerurl = "";

	@SuppressWarnings("boxing")
	@Async("getMailerHtmlContent")
	public Future<MailData> getMailerHtmlContent(ScheduleCampaign campaignMaster, NewsLetter newsletter,
			UserDetailDTO userDetailDTO, String sendingDomain, String sendingIp, int port, String smtpHost,
			String emailPrefix, String infra) {
		MailData mailDataDTO = new MailData();
		try {
			String subject = "";
			String htmlbody = "";
			String path = environment.getProperty("app.variables.track_app_name." + sendingDomain);
			if (path == null) {
				path = applicationProperties.getTrack_app_name();
			}
			//logger.info("Application Name {}", path);
			String trackUrlpath = "http://" + sendingDomain + "/" + path + "/v2";
			subject = campaignMaster.getSubjectLine();
			// subject = newsletter.getSubject();
			htmlbody = newsletter.getHtml_content();
			htmlbody = htmlbody.replaceAll("target=\"_blank\"", "");
			String msgId = userDetailDTO.getEmail() + "_" + newsletter.getId().toString() + "_" + campaignMaster.getId()
					+ "_" + sendingIp;

			/*
			 * String uId = Util.generateUid(userDetailDTO.getEmail(),
			 * newsletter.getId().toString(), campaignMaster.getId());
			 */

			String urlFields = "campId=" + campaignMaster.getId() + "&catId=" + newsletter.getCategory_id().toString()
					+ "&email=" + userDetailDTO.getEmail() + "&msgId=" + msgId + "&nId=" + newsletter.getId().toString()
					+ "&sgId=" + campaignMaster.getSegmentId().toString() + "&sd=" + sendingDomain + "&sIp=" + sendingIp
					+ "&subCatId=" + newsletter.getSubcategory_id().toString();// +
																				// "&uId="
																				// +
																				// uId;

			Document doc = Jsoup.parse(htmlbody);
			Elements links = doc.select("a[href]");
			Elements arealinks = doc.select("area[href]");
			links.addAll(arealinks);

			for (Element link : links) {

				try {
					if (link.attr("href").equalsIgnoreCase(ServerConstants.HTMLTAGFORUNSUBLINK)) {

						// String encryptedString = AES.encrypt(urlFields,
						// applicationProperties.getAes_key());

						link.attr("href", trackUrlpath + "/unsubUserClick?" + urlFields + "&v2=v2");
					} else if (link.attr("href").equalsIgnoreCase(ServerConstants.HTMLTAGFORVIEWINBROWSER)) {

						// String encryptedString = AES.encrypt(urlFields,
						// applicationProperties.getAes_key());
						link.attr("href", trackUrlpath + "/view?" + urlFields + "&v2=v2");
					} else if (link.attr("href").equalsIgnoreCase(ServerConstants.HTMLTAGFORRESUBSCRIBE)) {

						// String encryptedString = AES.encrypt(urlFields,
						// applicationProperties.getAes_key());

						link.attr("href", trackUrlpath + "/resubscribeUser?" + urlFields + "&v2=v2");
					} else if (link.attr("title").equalsIgnoreCase(ServerConstants.HTMLTAGFORNONMASKINGURL)) {

					} else {

						String fwdURL = link.attr("href").trim();
						// Save fwdURL in DB and generate linkId and append in link
						String linktag = link.attr("linktag").trim();
						// logger.info("linktag::: " + linktag);

						LinkMaster linkMaster = mongoDBConnection.findLinkMaster(campaignMaster.getId(), fwdURL,
								linktag);
						if (linkMaster != null) {
							// logger.info("linkMaster found: ");
							/*
							 * if (!linktag.equals(linkMaster.getType())) { linkMaster.setType(linktag);
							 * mongoTemplate.save(linkMaster); }
							 */

							/*
							 * String encryptedString = AES.encrypt(urlFields + "&linkId=" +
							 * linkMaster.getLinkId(), applicationProperties.getAes_key());
							 */
							link.attr("href", trackUrlpath + "/click?" + urlFields + "&linkId=" + linkMaster.getLinkId()
									+ "&v2=v2");

						} else {
							try {
								linkMaster = new LinkMaster();
								linkMaster.setCampaign_id(new ObjectId(campaignMaster.getId()));
								linkMaster.setUrl(fwdURL);
								linkMaster.setTotal_clicks(0);
								linkMaster.setUnique_clicks(0);
								linkMaster.setType(linktag);
								linkMaster.setAutoincrementId((int) getNextSequenceId(ServerConstants.LINKMASTER));
								mongoTemplate.save(linkMaster);
								// logger.info("LinkId: " + linkMaster.getLinkId());
								/*
								 * String encryptedString = AES.encrypt(urlFields + "&linkId=" +
								 * linkMaster.getLinkId(), applicationProperties.getAes_key());
								 */
								link.attr("href", trackUrlpath + "/click?" + urlFields + "&linkId="
										+ linkMaster.getLinkId() + "&v2=v2");

							} catch (DuplicateKeyException | org.springframework.dao.DuplicateKeyException
									| MongoWriteException e) {
								LinkMaster linkMasterN = mongoDBConnection.findLinkMaster(campaignMaster.getId(),
										fwdURL, linktag);
								if (linkMasterN != null) {
									// logger.info("linkMaster found: ");
									if (!linktag.equals(linkMasterN.getType())) {
										linkMasterN.setType(linktag);
										mongoTemplate.save(linkMasterN);
									}

									/*
									 * String encryptedString = AES.encrypt( urlFields + "&linkId=" +
									 * linkMasterN.getLinkId(), applicationProperties.getAes_key());
									 */

									link.attr("href", trackUrlpath + "/click?" + urlFields + "&linkId="
											+ linkMasterN.getLinkId() + "&v2=v2");

								}
							}
						}

					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			// Change domain in Image Url
			Elements imageLinks = doc.select("img[src]");
			for (Element element : imageLinks) {
				if (element.attr("title").equalsIgnoreCase(ServerConstants.IMAGEURLTITLE)) {
					String urlArray[] = element.attr("src").split("\\/");
					StringBuilder newURL = new StringBuilder();
					for (int l = 0; l < urlArray.length; l++) {
						if (l == 2) {
							newURL.append(sendingDomain).append("/");
						} else {
							if (urlArray.length - 1 == l) {
								newURL.append(urlArray[l]);
							} else {
								newURL.append(urlArray[l]).append("/");
							}
						}
					}
					element.attr("src", newURL.toString().trim());
				}
			}
			Element body = doc.body();

			// Adding Open Pixel URL
			// String encryptedStringOpen = AES.encrypt(urlFields,
			// applicationProperties.getAes_key());
			Document imgdoc = Jsoup.parse("<img alt=\"\" src=\"" + trackUrlpath + "/open?" + urlFields + "&v2=v2"
					+ "\" height='0px' width='0px'>");

			body.append(imgdoc.toString());

			Element tagBody = doc.body();
			htmlbody = tagBody.toString();
			// logger.info(htmlbody);
			htmlbody = htmlbody.replaceAll("<body>", "");
			htmlbody = htmlbody.replaceAll("</body>", "");
			htmlbody = htmlbody.replaceAll("<tbody>", "");
			htmlbody = htmlbody.replaceAll("</tbody>", "");
			// Adding HTML Meta Tag
			htmlbody = (ServerConstants.HTMLMETASTRING + htmlbody);

			// Replacing User based dynamic field in Mailer Content
			if (userDetailDTO.getName() != null) {
				htmlbody = htmlbody.replaceAll(ServerConstants.HTMLTAGFORNAME, userDetailDTO.getName());
				subject = subject.replaceAll(ServerConstants.HTMLTAGFORNAME, userDetailDTO.getName());
			} else {
				htmlbody = htmlbody.replaceAll(ServerConstants.HTMLTAGFORNAME, "");
				subject = subject.replaceAll(ServerConstants.HTMLTAGFORNAME, "");
			}
			if (userDetailDTO.getMobilenumber() != null) {
				htmlbody = htmlbody.replaceAll(ServerConstants.HTMLTAGFORMOBILENUMBER, userDetailDTO.getMobilenumber());
				subject = subject.replaceAll(ServerConstants.HTMLTAGFORMOBILENUMBER, userDetailDTO.getMobilenumber());
			} else {
				htmlbody = htmlbody.replaceAll(ServerConstants.HTMLTAGFORMOBILENUMBER, "");
				subject = subject.replaceAll(ServerConstants.HTMLTAGFORMOBILENUMBER, "");
			}
			if (userDetailDTO.getEmail() != null) {
				htmlbody = htmlbody.replaceAll(ServerConstants.HTMLTAGFOREMAIL, userDetailDTO.getEmail());
				subject = subject.replaceAll(ServerConstants.HTMLTAGFOREMAIL, userDetailDTO.getEmail());
			} else {
				htmlbody = htmlbody.replaceAll(ServerConstants.HTMLTAGFOREMAIL, "");
				subject = subject.replaceAll(ServerConstants.HTMLTAGFOREMAIL, "");
			}
			if (userDetailDTO.getVariable1() != null) {
				htmlbody = htmlbody.replaceAll(ServerConstants.HTMLTAGFORV1, userDetailDTO.getVariable1());
				subject = subject.replaceAll(ServerConstants.HTMLTAGFORV1, userDetailDTO.getVariable1());
			} else {
				htmlbody = htmlbody.replaceAll(ServerConstants.HTMLTAGFORV1, "");
				subject = subject.replaceAll(ServerConstants.HTMLTAGFORV1, "");
			}
			if (userDetailDTO.getVariable2() != null) {
				htmlbody = htmlbody.replaceAll(ServerConstants.HTMLTAGFORV2, userDetailDTO.getVariable2());
				subject = subject.replaceAll(ServerConstants.HTMLTAGFORV2, userDetailDTO.getVariable2());
			} else {
				htmlbody = htmlbody.replaceAll(ServerConstants.HTMLTAGFORV2, "");
				subject = subject.replaceAll(ServerConstants.HTMLTAGFORV2, "");
			}
			if (userDetailDTO.getVariable3() != null) {
				htmlbody = htmlbody.replaceAll(ServerConstants.HTMLTAGFORV3, userDetailDTO.getVariable3());
				subject = subject.replaceAll(ServerConstants.HTMLTAGFORV3, userDetailDTO.getVariable3());
			} else {
				htmlbody = htmlbody.replaceAll(ServerConstants.HTMLTAGFORV3, "");
				subject = subject.replaceAll(ServerConstants.HTMLTAGFORV3, "");
			}
			if (userDetailDTO.getVariable4() != null) {
				htmlbody = htmlbody.replaceAll(ServerConstants.HTMLTAGFORV4, userDetailDTO.getVariable4());
				subject = subject.replaceAll(ServerConstants.HTMLTAGFORV4, userDetailDTO.getVariable4());
			} else {
				htmlbody = htmlbody.replaceAll(ServerConstants.HTMLTAGFORV4, "");
				subject = subject.replaceAll(ServerConstants.HTMLTAGFORV4, "");
			}
			if (userDetailDTO.getVariable5() != null) {
				htmlbody = htmlbody.replaceAll(ServerConstants.HTMLTAGFORV5, userDetailDTO.getVariable5());
				subject = subject.replaceAll(ServerConstants.HTMLTAGFORV5, userDetailDTO.getVariable5());
			} else {
				htmlbody = htmlbody.replaceAll(ServerConstants.HTMLTAGFORV5, "");
				subject = subject.replaceAll(ServerConstants.HTMLTAGFORV5, "");
			}
			if (userDetailDTO.getVariable6() != null) {
				htmlbody = htmlbody.replaceAll(ServerConstants.HTMLTAGFORV6, userDetailDTO.getVariable6());
				subject = subject.replaceAll(ServerConstants.HTMLTAGFORV6, userDetailDTO.getVariable6());
			} else {
				htmlbody = htmlbody.replaceAll(ServerConstants.HTMLTAGFORV6, "");
				subject = subject.replaceAll(ServerConstants.HTMLTAGFORV6, "");
			}
			if (userDetailDTO.getVariable7() != null) {
				htmlbody = htmlbody.replaceAll(ServerConstants.HTMLTAGFORV7, userDetailDTO.getVariable7());
				subject = subject.replaceAll(ServerConstants.HTMLTAGFORV7, userDetailDTO.getVariable7());
			} else {
				htmlbody = htmlbody.replaceAll(ServerConstants.HTMLTAGFORV7, "");
				subject = subject.replaceAll(ServerConstants.HTMLTAGFORV7, "");
			}
			if (userDetailDTO.getVariable8() != null) {
				htmlbody = htmlbody.replaceAll(ServerConstants.HTMLTAGFORV8, userDetailDTO.getVariable8());
				subject = subject.replaceAll(ServerConstants.HTMLTAGFORV8, userDetailDTO.getVariable8());
			} else {
				htmlbody = htmlbody.replaceAll(ServerConstants.HTMLTAGFORV8, "");
				subject = subject.replaceAll(ServerConstants.HTMLTAGFORV8, "");
			}
			if (userDetailDTO.getVariable9() != null) {
				htmlbody = htmlbody.replaceAll(ServerConstants.HTMLTAGFORV9, userDetailDTO.getVariable9());
				subject = subject.replaceAll(ServerConstants.HTMLTAGFORV9, userDetailDTO.getVariable9());
			} else {
				htmlbody = htmlbody.replaceAll(ServerConstants.HTMLTAGFORV9, "");
				subject = subject.replaceAll(ServerConstants.HTMLTAGFORV9, "");
			}
			if (userDetailDTO.getVariable10() != null) {
				htmlbody = htmlbody.replaceAll(ServerConstants.HTMLTAGFORV10, userDetailDTO.getVariable10());
				subject = subject.replaceAll(ServerConstants.HTMLTAGFORV10, userDetailDTO.getVariable10());
			} else {
				htmlbody = htmlbody.replaceAll(ServerConstants.HTMLTAGFORV10, "");
				subject = subject.replaceAll(ServerConstants.HTMLTAGFORV10, "");
			}

			// Generating Gmail Unsub URL for mail header
			// String encryptedStringGmailUnsub = AES.encrypt(urlFields,
			// applicationProperties.getAes_key());

			String gmailUnsubLink = trackUrlpath + "/unsubGmail?" + urlFields + "&v2=v2";
			String encryptedMessageId = AES.encrypt(msgId, applicationProperties.getAes_key());
			mailDataDTO.setUnsub_url(gmailUnsubLink);
			mailDataDTO.setMessage_id(encryptedMessageId);
			mailDataDTO.setMobile_no(userDetailDTO.getMobilenumber());
			mailDataDTO.setRto(campaignMaster.getRtoType().booleanValue() == true ? 1 : 0);
			mailDataDTO.setEmail_id(userDetailDTO.getEmail());
			mailDataDTO.setSubscriber_name(userDetailDTO.getName());
			mailDataDTO.setCampaign_id(new ObjectId(campaignMaster.getId()));
			mailDataDTO.setCampaign_type(campaignMaster.getCampaignType());
			mailDataDTO.setNews_letter_id(newsletter.getId().toString());
			mailDataDTO.setSchedule_date(campaignMaster.getSchedule_date());
			mailDataDTO.setSchedule_time(campaignMaster.getSchedule_time());
			mailDataDTO.setMail_content(htmlbody);
			mailDataDTO.setDomain(sendingDomain);
			mailDataDTO.setInfra(infra);
			mailDataDTO.setIp(sendingIp);
			// mailDataDTO.setUid(uId);
			mailDataDTO.setUnsub_url(gmailUnsubLink);
			mailDataDTO.setReply_email("reply@" + sendingDomain);
			if (!"default".equalsIgnoreCase(campaignMaster.getFromEmail())) {
				mailDataDTO.setFrom_email(campaignMaster.getFromEmail() + "@" + sendingDomain);
			} else {
				mailDataDTO.setFrom_email(emailPrefix);
			}
			mailDataDTO.setPort(port);
			mailDataDTO.setSubject(subject);
			mailDataDTO.setSender_name(campaignMaster.getSenderName());
			mailDataDTO.setSending_status(ServerConstants.SENDINGCONSTANTFORSENDING);
			mailDataDTO.setSchedule_type(campaignMaster.getSchedule_type());
			mailDataDTO.setHost_name(smtpHost);
			mailDataDTO.setSeq_campaign_category(campaignMaster.getSeq_campaign_category());
			mailDataDTO.setSeq_campaign_subcategory(campaignMaster.getSeq_campaign_subcategory());
			mailDataDTO.setSeq_campaign_tag(campaignMaster.getSeq_campaign_tag());
			mailDataDTO.setSeq_client_id(campaignMaster.getSeq_client_id());
			mailDataDTO.setSeq_customer_id(campaignMaster.getSeq_customer_id());
			mailDataDTO.setSeq_newsletter_id(campaignMaster.getSeq_newsletter_id());
			mailDataDTO.setSeq_segment_id(campaignMaster.getSeq_segment_id());
			mailDataDTO.setSchedule_type(Constants.SCHEDULE_TYPE_NORMAL);

		} catch (

		Exception e) {
			e.printStackTrace();
			logger.debug("Errror in Mail content" + e.getMessage());
		}

		//logger.debug("mailDataDTO" + mailDataDTO);
		return new AsyncResult<>(mailDataDTO);
	}

	public void composeMailData(NewsLetter newsLetter, MailAudience mailAudience, MailData mailData,
			ScheduleCampaign scheduleCampaign) {
		String urlpath = "%%urlpath%%";
		String domainur = "%%domainur%%";
		String queue = "%%queue%%";
		String subject = newsLetter.getSubject();
		String htmlContent = newsLetter.getHtml_content();
		htmlContent = htmlContent.replaceAll("target=\"_blank\"", "");
		String campId = scheduleCampaign.getId();
		String newsletterid = newsLetter.getId().toString();
		String catid = newsLetter.getCategory_id().toString();
		String clickscounturl = urlpath + "/Status?id=" + campId + "&nid=" + newsletterid + "&cid=" + catid
				+ "&subid=%%subid%%&domainur=" + domainur + "&queue=" + queue
				+ "&domain=%%domain%%&action=clicks&forwardurl=";

		Document doc = Jsoup.parse(htmlContent);
		Elements links = doc.select("a[href]");
		Elements arealinks = doc.select("area[href]");
		links.addAll(arealinks);
		String fwdURL = "";
		for (Element link : links) {
			urlpath = urlpath.replaceAll("/w2email", "");
			if (link.attr("href").equalsIgnoreCase("%%unsubcriber%%")) {
				link.attr("href", urlpath + "/Status?id=" + campId + "&nid=" + newsletterid + "&domainur=" + domainur
						+ "&queue=" + queue + "&domain=%%domain%%&action=stop&cid=" + catid + "&type=c");

			} else if (link.attr("href").equalsIgnoreCase("%%browser%%")) {
				link.attr("href", urlpath + "/Status?id=" + campId + "&nid=" + newsletterid + "&domainur=" + domainur
						+ "&queue=" + queue + "&domain=%%domain%%&action=viewbrowser&cid=" + catid);

			} else if (link.attr("href").equalsIgnoreCase("%%mkmesp%%")) {
				link.attr("href", urlpath + "/Status?id=" + campId + "&nid=" + newsletterid + "&domainur=" + domainur
						+ "&queue=" + queue + "&domain=%%domain%%&action=mkmesp&cid=" + catid);
			} else {
				fwdURL = link.attr("href").trim();
				String linkId = mongoDBConnection.getLinkId(fwdURL);
				if (linkId != null && !linkId.isEmpty()) {
					link.attr("href", clickscounturl + linkId);
				} else {
					SSlinks sslinks = new SSlinks();
					sslinks.setUrl(fwdURL);
					String newlinkId = mongoDBConnection.saveLinkURL(sslinks);
					if (newlinkId != null && !newlinkId.isEmpty()) {
						link.attr("href", clickscounturl + newlinkId);
					}

				}
			}
		}
		htmlContent = htmlContent.replaceAll("%%Name%%", mailAudience.getName());
		htmlContent = htmlContent.replaceAll("%%mobileno%%", mailAudience.getMobile_no());
		htmlContent = htmlContent.replaceAll("%%domain%%", scheduleCampaign.getDomain());

		htmlContent = htmlContent.replaceAll("%%urlpath%%", trackerurl.replaceAll("/w2email", ""));
		String newdomainur = trackerurl.toString().replaceAll("/w2email", "");
		newdomainur = newdomainur.replaceAll("http://", "");
		newdomainur = newdomainur.trim();
		htmlContent = htmlContent.replaceAll("%%domainur%%", newdomainur);

		Element tagBody = doc.body();
		htmlContent = tagBody.toString();
		htmlContent = htmlContent.replaceAll("<body>", "");
		htmlContent = htmlContent.replaceAll("</body>", "");
		htmlContent = htmlContent.replaceAll("<tbody>", "");
		htmlContent = htmlContent.replaceAll("</tbody>", "");
		htmlContent = ("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>" + htmlContent);
		Document htmlDoc = null;
		htmlDoc = Jsoup.parse(htmlContent);
		htmlContent = htmlDoc.toString();
		mailData.setMail_content(htmlContent);
		mailData.setSubject(subject);
	}

	@SuppressWarnings("boxing")
	public long getNextSequenceId(String key) {

		Query query = new Query();
		query.addCriteria(Criteria.where("name").is(key));
		Update update = new Update();
		update.inc("lastcount", 1);

		FindAndModifyOptions options = new FindAndModifyOptions();
		options.returnNew(true);

		SequenceNumber seqId = mongoOperation.findAndModify(query, update, options, SequenceNumber.class);

		if (seqId == null) {
			// log.error("Unable to get sequence id for key : " + key);
		}

		return seqId.getLastcount();

	}

	public static void main(String[] args) {
		String str = "asnda=sads&sasadd=asdssa&asdfs=98";

		System.out.println(str);

	}

}
