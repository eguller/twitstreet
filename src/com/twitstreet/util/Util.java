package com.twitstreet.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;

public class Util {
	public static String NO_RECORDS_FOUND_HTML = "<p>No records found.</p>";
	public static java.sql.Date toSqlDate(java.util.Date date) {
		return new java.sql.Date(date.getTime());
	}

	private static String convertToHex(byte[] data) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			int halfbyte = (data[i] >>> 4) & 0x0F;
			int two_halfs = 0;
			do {
				if ((0 <= halfbyte) && (halfbyte <= 9))
					buf.append((char) ('0' + halfbyte));
				else
					buf.append((char) ('a' + (halfbyte - 10)));
				halfbyte = data[i] & 0x0F;
			} while (two_halfs++ < 1);
		}
		return buf.toString();
	}

	public static String MD5(String text) throws NoSuchAlgorithmException,
			UnsupportedEncodingException {
		MessageDigest md;
		md = MessageDigest.getInstance("MD5");
		byte[] md5hash = new byte[32];
		md.update(text.getBytes("iso-8859-1"), 0, text.length());
		md5hash = md.digest();
		return convertToHex(md5hash);
	}

	/**
	 * Remove/collapse multiple spaces.
	 * 
	 * @param argStr
	 *            string to remove multiple spaces from.
	 * @return String
	 */
	public static String collapseSpaces(String argStr) {
		if (argStr != null) {
			char last = argStr.charAt(0);
			StringBuffer argBuf = new StringBuffer();

			for (int cIdx = 0; cIdx < argStr.length(); cIdx++) {
				char ch = argStr.charAt(cIdx);
				if (ch != ' ' || last != ' ') {
					argBuf.append(ch);
					last = ch;
				}
			}

			return argBuf.toString();
		} else {
			return "";
		}
	}

	public static String commaSep(double amount) {
		
//		if(Math.abs(amount)<0.1 && amount!=0 && (int)amount != amount){
//			
//			if(amount<0){
//				amount = -0.1;
//			}
//			else{
//				amount = 0.1;
//			}
//			
//			
//			
//		}
		DecimalFormat decimalFormatter = new DecimalFormat("#,###,###.00");
		String formatted = decimalFormatter.format(amount);
		if (formatted.startsWith(".")) {
			formatted = "0" + formatted;
		}
		else if (formatted.startsWith("-.")) {
			formatted = formatted.replace("-.", "-0.");
		}
		return formatted;
	}

	public static String commaSep(int amount) {
		DecimalFormat decimalFormatter = new DecimalFormat("#,###,###");
		return decimalFormatter.format(amount);
	}

	public static String convertStringToValidURL(String str)
			throws UnsupportedEncodingException {

		String newStr = "";

		newStr = URLEncoder.encode(str, "UTF-8");

		return newStr;
	}
	public static double roundDouble(double pDouble, int decimalLength){

		BigDecimal bd = new BigDecimal(pDouble);
		bd = bd.setScale(decimalLength, BigDecimal.ROUND_UP);

		return (bd.doubleValue());
	}

	public static String getChangePerHourString(double cph){
		
		String cphStr = "$";
		cphStr = cphStr + Util.commaSep(Util.roundDouble(Math.abs(cph),2));
		if( Math.abs(cph) <0.01 && cph!=0){
			
			cphStr = "$0.01";
			
		}	

		if (cph > 0) {

			cphStr = cphStr + "/h &#9650;";
			
			
		}
		else if (cph < 0){
			cphStr = cphStr + "/h &#9660;";
		}
		
		return cphStr;
		
	}
	public static String getFollowerChangePerHourString(int cph,int total){
		
		String cphStr = "";
		cphStr = cphStr + Util.commaSep(cph);
		if( Math.abs(cph) <0.01 && cph!=0){
			
			cphStr = "0.01";
			
		}	
		
		if (cph > 0) {

			cphStr = cphStr+ "/h" + " ("+getShareString((double) cph/total)+") &#9650; ";
			
			
		}
		else if (cph < 0){
			cphStr = cphStr+"/h"+ " ("+getShareString((double) cph/total)+") &#9660; ";
		}
		
		return cphStr;
		
	}
	public static String getProfitString(double profit){
		
		String cphStr = "$";
		cphStr = cphStr + Util.commaSep(Util.roundDouble(Math.abs(profit),2));
		if( Math.abs(profit) <0.01 && profit!=0){
			
			cphStr = "$0.01";
			
		}	

		if (profit > 0) {

			cphStr = cphStr.replace("$", "+$");
			
			
		}
		else if (profit < 0){
			cphStr = cphStr.replace("-", "").replace("$", "-$");
		}
		
		return cphStr;
		
	}

	public static String getRoundedChangePerHourString(double cph){
		
		String cphStr = "$";
		
//		if (Math.abs(cph) < 1) {
//
//			cphStr = "<$";
//
//		} 
		if (cph > 0) {
			if (cph != (int) cph) {
				cph += 1;
			}
			
			int roundedVal = (int) Math.abs(cph);
			cphStr = cphStr + Util.commaSep(roundedVal) + "/h &#9650;";

		} else if (cph < 0) {
			if (cph != (int) cph) {
				cph = cph - 1;
			}
			int roundedVal = (int) Math.abs(cph);
			cphStr = cphStr + Util.commaSep(roundedVal) +  "/h &#9660;";
		}
		
		return cphStr;
		
	}

	public static String getShareString(double cph){
		cph= cph*100;
		String cphStr = String.valueOf(Util.roundDouble(Math.abs(cph),2));
//		if( Math.abs(cph) <0.01){
//			
//			cphStr = "<" + cphStr;
//			
//		}	
		cphStr += "%";
		
		return cphStr;
		
	}
}
