package com.twitstreet.twitter;

import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

public class Util {
	private static final Gson gson = new Gson();
	private static final long SEC_MS = 1000;
	private static final long MIN_MS = 60 * SEC_MS;
	private static final long HOUR_MS = 60 * MIN_MS;
	private static final long DAY_MS = 24 * HOUR_MS;
	private static final long WEEK_MS = 7 * DAY_MS;
	private static final long MONTH_MS = 4 * WEEK_MS; // assume 1 month is 4
														// week;
	private static final long YEAR_MS = 12 * MONTH_MS;

	public static String DATE_FORMAT = "h:mm a, MMM d ''yy";
	
	private static Util instance = new Util();
	private static Logger logger = Logger.getLogger(Util.class);

	// SHORTENER--
	public static String[] key = new String[] {
			"AIzaSyDj8IptCTCTJhQTibwUe-c70o2qfTUjlOQ",
			"AIzaSyAXcU_dfWO5ZPewK9KQtGs5h_1d3ghQrAo" };

	private static int keyIndex = 0;
	private static int KEY_COUNT = 2;
	// --SHORTENER

	private String contextPath = "";

	private Util() {

	}

	public static String parseImg(String sourceString) {
		String img = null;
		try {
			img = sourceString.split("id=\"prodImageCell\"")[1].split("</div>")[0]
					.split("src=\"")[1].split("\" ")[0];
		} catch (Exception e) {
			// Try another regex
		}
		if (img != null) {
			return img;
		}
		try {
			img = sourceString.split("<img id=\"main-image\" src=\"")[1]
					.split("\"")[0];
		} catch (Exception e) {
			// Try another regex
		}
		return img;
	}

	public static String parse(String sourceString, String prefix,
			String suffix, String contentStartString, String contentEndString) {

		return parse(sourceString, prefix, suffix, contentStartString,
				contentEndString, null, null)[0];
	}

	public static String[] parse(String sourceString, String prefix,
			String suffix, String contentStartString, String contentEndString,
			String itemSeparator) {

		return parse(sourceString, prefix, suffix, contentStartString,
				contentEndString, itemSeparator, null);
	}

	public static String[] parse(String sourceString, String prefix,
			String suffix, String contentStartString, String contentEndString,
			String itemSeparator, String itemSplitter) {

		String[] returnArr = new String[1];
		String returnValue = "";
		if (contentStartString != null && contentStartString != "") {
			String[] result = sourceString.split(contentStartString);

			if (result.length > 1) {
				sourceString = result[1];
			}
		}
		if (contentEndString != null && contentEndString != "") {
			String[] result = sourceString.split(contentEndString);

			if (result.length > 1) {
				sourceString = result[0];
			}
		}

		returnValue = getBetween(sourceString, prefix, suffix);

		return new String[] { returnValue };
	}
	
	public static String getBetween(String stringToParse, String prefix,
			String suffix) {

		String temp1 = getBetweenBasic(stringToParse, prefix, suffix);
		String temp2 = removeTags(temp1);
		temp2 = temp2.trim();
		return temp2;
	}
	
	public static String getBetweenBasic(String stringToParse, String prefix,
			String suffix) {

		String temp1 = "";

		if (stringToParse.split(prefix).length > 1) {
			temp1 = stringToParse.split(prefix)[1].split(suffix)[0];
		}

		return temp1;
	}
	
	public static String removeTags(String text) {
		int indexOfAB1;
		int indexOfAB2;
		String editText = new String(text);
		boolean tagFound = true;

		while (tagFound) {
			indexOfAB1 = editText.indexOf("<");
			indexOfAB2 = editText.indexOf(">");

			if (indexOfAB1 < 0 && indexOfAB2 >= 0
					&& editText.length() - 1 > indexOfAB2
					|| indexOfAB1 > indexOfAB2 && indexOfAB2 >= 0) {

				editText = editText.substring(indexOfAB2 + 1);
				tagFound = true;

			} else if (indexOfAB1 >= 0 && indexOfAB1 < indexOfAB2) {
				tagFound = true;
				editText = editText.substring(0, indexOfAB1)
						+ editText.substring(indexOfAB2 + 1);
			} else {
				tagFound = false;
			}
		}
		return editText;
	}
	
	public static boolean isListValid(List<?> arr) {
		return arr != null && arr.size() > 0;
	}
}
