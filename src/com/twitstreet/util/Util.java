package com.twitstreet.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Util {

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
	}

	public static String commaSep(String amount) {
		String commaAmount = "";
		int remaining = amount.length() % 3 == 0 ? 3 : amount.length() % 3;
		commaAmount = amount.substring(0, remaining);
		for (int i = remaining; i < amount.length(); i = i + 3) {
			commaAmount = commaAmount + "," + amount.substring(i, i + 3);

		}
		return commaAmount;
	}
	
	public static String commaSep(int amount) {
		return commaSep(String.valueOf(amount));
	}
}
