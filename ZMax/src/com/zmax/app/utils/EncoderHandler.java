package com.zmax.app.utils;

import java.security.MessageDigest;

/**
 * blog www.micmiu.com
 * 
 * @author Michael
 * 
 */
public class EncoderHandler {
	
	private static final String ALGORITHM = "MD5";
	
	private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	
	/**
	 * encode string
	 * 
	 * @param algorithm
	 * @param str
	 * @return String
	 */
	public static String encode(String algorithm, String str) {
		if (str == null) {
			return null;
		}
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
			messageDigest.update(str.getBytes());
			return getFormattedText(messageDigest.digest());
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
	
	/**
	 * encode By MD5
	 * 
	 * @param str
	 * @return String
	 */
	public static String encodeByMD5(String str) {
		if (str == null) {
			return null;
		}
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(ALGORITHM);
			messageDigest.update(str.getBytes());
			return getFormattedText(messageDigest.digest());
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
	
	/**
	 * Takes the raw bytes from the digest and formats them correct.
	 * 
	 * @param bytes
	 *            the raw bytes from the digest.
	 * @return the formatted bytes.
	 */
	private static String getFormattedText(byte[] bytes) {
		int len = bytes.length;
		StringBuilder buf = new StringBuilder(len * 2);
		// 把密文转换成十六进制的字符串形式
		for (int j = 0; j < len; j++) {
			buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
			buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
		}
		return buf.toString();
	}
	
	public static void main(String[] args) {
		System.out.println("111111 MD5  :" + EncoderHandler.encodeByMD5("111111"));
		System.out.println("111111 MD5  :" + EncoderHandler.encode("MD5", "111111"));
		System.out.println("111111 SHA1 :" + EncoderHandler.encode("SHA1", "111111"));
	}
	
	public static String getPublicKey(String pms_hotel_id, String req_time) {
		String req_time_s = req_time.substring(0, 8);
//		Log.i(req_time + "      " + req_time_s);
		String md5str = "zmax" + req_time_s;
//		Log.i("md5 encode str :   " + md5str);
		String md5_result = EncoderHandler.encode("MD5", md5str);
//		Log.i("md5 result  str :   " + md5_result);
		
		String shaStr = pms_hotel_id + req_time + md5_result;
//		Log.i("sha encode str :   " + shaStr);
		String sha_result = EncoderHandler.encode("SHA1", shaStr);
//		Log.i("sha encode result  :   " + sha_result);
		
		return sha_result;
	}
	
}
