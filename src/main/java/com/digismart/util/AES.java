package com.digismart.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AES {

	private static final Logger log = LoggerFactory.getLogger(AES.class);
	private static SecretKeySpec secretKey;
	private static byte[] key;

	public static void setKey(String myKey) {
		MessageDigest sha = null;
		try {
			key = myKey.getBytes("UTF-8");
			sha = MessageDigest.getInstance("SHA-1");
			key = sha.digest(key);
			key = Arrays.copyOf(key, 16);
			secretKey = new SecretKeySpec(key, "AES");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public static String encrypt(String strToEncrypt, String secret) {
		try {
			if (secret != null && secret.length() == 16) {
				if ((strToEncrypt != null) && (!strToEncrypt.equalsIgnoreCase(""))) {
					setKey(secret);
					Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");

					cipher.init(1, secretKey);
					byte[] encVal = cipher.doFinal(strToEncrypt.getBytes());
					return new String(Base64.encodeBase64(encVal));

				}
			}

		} catch (Exception e) {
			log.info("Error while encrypting: " + e.toString());
		}
		log.info("Encrypted String :" + strToEncrypt);
		return strToEncrypt;
	}

	public static String decrypt(String strToDecrypt, String secret) {
		try {
			if (secret != null && secret.length() == 16) {
				if ((strToDecrypt != null) && (!strToDecrypt.equalsIgnoreCase(""))) {
					setKey(secret);
					Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
					cipher.init(2, secretKey);
					byte[] decordedValue = Base64.decodeBase64(strToDecrypt);
					byte[] decValue = cipher.doFinal(decordedValue);
					return new String(decValue);
				}
			}

		} catch (Exception e) {
			log.info("Error while decrypting: " + e.toString());
		}
		return strToDecrypt;
	}

}