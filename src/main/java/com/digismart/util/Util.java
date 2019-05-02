package com.digismart.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Util {
	private static Logger log = LoggerFactory.getLogger(Util.class);

	public static String getCurrentDateStr(String format) {
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.setCalendar(Calendar.getInstance());
		sdf.applyPattern(format);
		String dateformat = sdf.format(new Date());
		return dateformat;
	}

	public static String dateFormat(String format, String dateTimeStr) {
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.setCalendar(Calendar.getInstance());
		sdf.applyPattern(format);
		String dateformat = sdf.format(getCurrentDateFromStr(dateTimeStr));
		return dateformat;
	}

	public static Date getDate(String format, String dateStr) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			Date parse = sdf.parse(dateStr);
			return parse;
		} catch (Exception e) {
			e.printStackTrace();
			return new Date();
		}

	}

	public static Date getCurrentDateFromStr(String dateTimeStr) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			cal.setTime(sdf.parse(dateTimeStr));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return cal.getTime();
	}

	public static Date buildCampaignInfoExpiry(int expiryAfterDays, Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.add(Calendar.DAY_OF_MONTH, expiryAfterDays);
		return cal.getTime();
	}

	public static double getPercentage(long n, long total) {
		double proportion = ((double) n) / ((double) total);
		return round(proportion * 100, 4);
	}

	private static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();
		BigDecimal bd = new BigDecimal(Double.toString(value));
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	public static String getEncodedString(String input) {
		String encoded = "";
		try {
			log.info("Before Encoding " + input);
			encoded = URLEncoder.encode(input, "UTF-8");
			log.info("After Encoding " + encoded);
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encoded;
	}

}
