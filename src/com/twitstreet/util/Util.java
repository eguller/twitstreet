/**
	TwitStreet - Twitter Stock Market Game
    Copyright (C) 2012  Engin Guller (bisanth@gmail.com), Cagdas Ozek (cagdasozek@gmail.com)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
**/

package com.twitstreet.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import com.google.inject.Inject;
import com.twitstreet.localization.LocalizationUtil;
import com.twitstreet.session.UserMgr;

public class Util {
	public static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	static SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
	
	private static final long SEC_MS = 1000; 
	private static final long MIN_MS = 60 * SEC_MS;
	private static final long HOUR_MS= 60 * MIN_MS;
	private static final long DAY_MS = 24 * HOUR_MS;
	private static final long WEEK_MS = 7 * DAY_MS;
	private static final long MONTH_MS = 4 * WEEK_MS; // assume 1 month is 4 week;
	private static final long YEAR_MS = 12 * MONTH_MS;
	
	
	public static String NO_RECORDS_FOUND_HTML = "<p>No records found.</p>";

	public static java.sql.Date toSqlDate(java.util.Date date) {
		return new java.sql.Date(date.getTime());
	}

	public static String getConfirmationJS(String localizedMessage, String okAction, String cancelAction){
		
		
		return "<script> " +
				" if(confirm(\""+localizedMessage+"\")){ " +
						okAction+"; " +
				" } else{"+
						cancelAction +"; " +
				" } " +
			   "</script>";
		
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

		// if(Math.abs(amount)<0.1 && amount!=0 && (int)amount != amount){
		//
		// if(amount<0){
		// amount = -0.1;
		// }
		// else{
		// amount = 0.1;
		// }
		//
		//
		//
		// }
		DecimalFormat decimalFormatter = new DecimalFormat("#,###,###.00");
		String formatted = decimalFormatter.format(amount);
		if (formatted.startsWith(".")) {
			formatted = "0" + formatted;
		} else if (formatted.startsWith("-.")) {
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

	public static double roundDouble(double pDouble, int decimalLength) {

		BigDecimal bd = new BigDecimal(pDouble);
		bd = bd.setScale(decimalLength, BigDecimal.ROUND_UP);

		return (bd.doubleValue());
	}

	public static String getChangePerHourString(double cph) {

		String cphStr = "$";
		cphStr = cphStr + Util.commaSep(Util.roundDouble(Math.abs(cph), 2));
		if (Math.abs(cph) < 0.01 && cph != 0) {

			cphStr = "$0.01";

		}

		if (cph > 0) {

			cphStr = cphStr + "/h &#9650;";

		} else if (cph < 0) {
			cphStr = cphStr + "/h &#9660;";
		}

		return cphStr;

	}

	public static String getPercentageChangePerHourString(double cph) {
		cph = cph * 100;
		String cphStr = String.valueOf(Util.roundDouble(Math.abs(cph), 2));
		// if( Math.abs(cph) <0.01){
		//
		// cphStr = "<" + cphStr;
		//
		// }
		cphStr += "%";

		// cphStr = cphStr + Util.commaSep(Util.roundDouble(Math.abs(cph),2));

		if (cph > 0) {

			cphStr = cphStr + "/h &#9650;";

		} else if (cph < 0) {
			cphStr = cphStr + "/h &#9660;";
		}

		return cphStr;

	}

	public static String getPercentageChangeString(double cph) {
		cph = cph * 100;
		String cphStr = String.valueOf(Util.roundDouble(Math.abs(cph), 2));
		cphStr += "% ";

		return cphStr;

	}

	public static String getRoundedChangePerHourString(double cph) {

		String cphStr = "";

		// if (Math.abs(cph) < 1) {
		//
		// cphStr = "<$";
		//
		// }
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
			cphStr = cphStr + Util.commaSep(roundedVal) + "/h &#9660;";
		}

		return cphStr;

	}

	public static String getRoundedMoneyString(double cph) {

		String cphStr = "$";

		// if (Math.abs(cph) < 1) {
		//
		// cphStr = "<$";
		//
		// }
		if (cph > 0) {
			if (cph != (int) cph) {
				cph += 1;
			}

			int roundedVal = (int) Math.abs(cph);
			cphStr = cphStr + Util.commaSep(roundedVal);

		} else if (cph < 0) {
			if (cph != (int) cph) {
				cph = cph - 1;
			}
			int roundedVal = (int) Math.abs(cph);
			cphStr = cphStr + Util.commaSep(roundedVal);
		}

		return cphStr;

	}

	public static String getRoundedProfitPerHourString(double cph) {

		String cphStr = "$" + getRoundedChangePerHourString(cph);

		return cphStr;

	}

	public static String getShareString(double cph) {
		cph = cph * 100;
		String cphStr = String.valueOf(Util.roundDouble(Math.abs(cph), 2));
		// if( Math.abs(cph) <0.01){
		//
		// cphStr = "<" + cphStr;
		//
		// }
		cphStr += "%";

		return cphStr;

	}

	public static String getIntervalStringForPage(int page, int recordPerPage,
			int userCount) {

		int i = page - 1;

		int start = i * recordPerPage + 1;
		int stop = (i + 1) * recordPerPage;

		if (stop > userCount) {

			stop = userCount;
		}
		String intervalString = "";

		if (start == stop) {

			intervalString = String.valueOf((start));
		} else {

			intervalString = (start) + "-" + (stop);
		}

		return intervalString;

	}

	public static boolean isValidTwitterUserName(String userName) {
		return Pattern.matches("[A-Za-z0-9_]+", userName);
	}

	public static String getTextInSpan(String text, String styleClass) {

		String spanStr = "<span";

		if (styleClass != null && styleClass.length() > 0) {
			spanStr = spanStr + " class='" + styleClass + "'";

		}
		spanStr = spanStr + ">";
		spanStr = spanStr + text + "</span>";

		return spanStr;

	}

	public static String getNumberFormatted(double amount, boolean dollar,
			boolean rounded, boolean perHour, boolean arrow,
			boolean appendPlusIfPositive, boolean getInSpan) {

		String moneyStr = "";

		if (rounded) {
			if (amount > 0) {
				if (amount != (int) amount) {
					amount += 1;
				}
			} else if (amount < 0) {
				if (amount != (int) amount) {
					amount = amount - 1;
				}
			}
			int roundedVal = (int) Math.abs(amount);
			moneyStr = moneyStr + Util.commaSep(roundedVal);
		} else {
			moneyStr = moneyStr + Util.commaSep(amount);
		}

		String arrowStr = "";
		String plusMinus = "";
		String spanClass = "";
		if (amount > 0) {
			arrowStr = "&#9650;";
			plusMinus = "+";
			spanClass = "green-profit";

		} else if (amount < 0) {
			arrowStr = "&#9660;";
			plusMinus = "-";
			spanClass = "red-profit";
		}

		moneyStr = (dollar) ? "$" + moneyStr : moneyStr;
		moneyStr = (appendPlusIfPositive) ? plusMinus + moneyStr : moneyStr;

		moneyStr = (perHour) ? moneyStr + "/h" : moneyStr;
		moneyStr = (arrow) ? moneyStr + arrowStr : moneyStr;

		moneyStr = (getInSpan) ? getTextInSpan(moneyStr, spanClass) : moneyStr;

		return moneyStr;
	}

	public static String getPercentageFormatted(double percentage,
			boolean rounded, boolean percentSign, boolean perHour,
			boolean arrow, boolean appendPlusIfPositive, boolean getInSpan) {
		double percentageOrig = percentage;
		percentage = percentage * 100;

		percentage = Util.roundDouble(Math.abs(percentage), 2);

		String percentageStr = "";

		if (rounded) {
			if (percentage > 0) {
				if (percentage != (int) percentage) {
					percentage += 1;
				}
			} else if (percentage < 0) {
				if (percentage != (int) percentage) {
					percentage = percentage - 1;
				}
			}
			int roundedVal = (int) Math.abs(percentage);
			percentageStr = percentageStr + Util.commaSep(roundedVal);
		} else {
			percentageStr = percentageStr + Util.commaSep(percentage);
		}

		String arrowStr = "";
		String plusMinus = "";
		String spanClass = "";
		if (percentageOrig > 0) {
			arrowStr = "&#9650;";
			plusMinus = "+";
			spanClass = "green-profit";

		} else if (percentageOrig < 0) {
			arrowStr = "&#9660;";
			plusMinus = "-";
			spanClass = "red-profit";
		}

		percentageStr = (percentSign) ? percentageStr + "%" : percentageStr;
		percentageStr = (appendPlusIfPositive) ? plusMinus + percentageStr
				: percentageStr;

		percentageStr = (perHour) ? percentageStr + "/h" : percentageStr;
		percentageStr = (arrow) ? percentageStr + arrowStr : percentageStr;

		percentageStr = (getInSpan) ? getTextInSpan(percentageStr, spanClass)
				: percentageStr;

		return percentageStr;
	}

	public static String getWatchListIcon(boolean add, int height, String title) {
		String imgElement = "";

		String styleString = "";

		if (height > 0) {

			styleString = "style = 'height:" + height + "px;' ";

		}
		if (add) {

			imgElement = "<img " + styleString + " alt='" + title + "' title='"
					+ title + "' src='images/eyeGreen.png'>";
		} else {

			imgElement = "<img " + styleString + " alt='" + title + "' title='"
					+ title + "' src='images/eyeRed.png'>";

		}

		return imgElement;
	}

	public static String dateDiff2String(Date date, String lang) {
		LocalizationUtil lutil = LocalizationUtil.getInstance();
		Date now = Calendar.getInstance().getTime();
		long diff = now.getTime() - date.getTime();
		if( diff / MIN_MS == 0){
			return diff / SEC_MS > 1 ? lutil.get("transactions.secs_ago", lang, diff / SEC_MS): lutil.get("transactions.just_now", lang);
		}
		else if(diff / HOUR_MS == 0){
			return diff / MIN_MS > 1 ?  lutil.get("transactions.mins_ago", lang, diff / MIN_MS) : lutil.get("transactions.min_ago", lang, 1);
		}
		else if(diff / DAY_MS == 0){
			return diff / HOUR_MS > 1 ? lutil.get("transactions.hours_ago", lang, diff / HOUR_MS) : lutil.get("transactions.hour_ago", lang, 1);
		}
		else if(diff / WEEK_MS == 0){
			return diff / DAY_MS > 1 ? lutil.get("transactions.days_ago", lang, diff / DAY_MS) : lutil.get("transactions.yesterday", lang);
		}
		else if(diff / MONTH_MS == 0){
			return diff / WEEK_MS > 1 ? lutil.get("transactions.weeks_ago", lang, diff / WEEK_MS) : lutil.get("transactions.week_ago", lang);
		}
		else if(diff / YEAR_MS == 0){
			return diff / MONTH_MS > 1 ?  lutil.get("transactions.months_ago", lang, diff / MONTH_MS) : lutil.get("transactions.month_ago", lang);
		}
		else{
			return diff / YEAR_MS > 1 ? lutil.get("transactions.years_ago", lang, diff / YEAR_MS) : lutil.get("transactions.years_ago", lang);
		}
	}
	public static Date stringToDate(String dateStr){
		
		try {
			return sdf.parse(dateStr);
		} catch (ParseException e) {
			
		 return null;
		}
		
	}
	public static String dateToString(Date date){
			return sdf.format(date);
	}
	
	public static String at(String name){
		if(name == null || name.length() == 0){
			return "@";
		}
		else if(name.charAt(0) == '@'){
			return name;
		}
		else{
			return "@" + name;
		}
	}
	
	public static String mentionMessage(String mention, String message){
		return at(mention) + " " + message;
	}
	
	public static String mentionMessage(String[] mentions, String message){
		String mentionMessage = "";
		for(String mention : mentions){
			mentionMessage += at(mention) + " ";
		}
		return mentionMessage + message;
	}

	public static int getPageCount(int itemCount, int itemPerPage){
		int pageCount = 1;
		if (itemCount > itemPerPage) {
			// we should add 1 because of integer conversion
			pageCount = (itemCount / itemPerPage);
			if(itemCount%itemPerPage!=0){
				pageCount++;
			}
		}
		return pageCount;
	}
	public static int getPageOfRank(int rank, int itemPerPage){
		int pageCount = 1;
		if (rank > itemPerPage) {
			// we should add 1 because of integer conversion
			pageCount = (rank / itemPerPage);
			if(rank%itemPerPage!=0){
				pageCount++;
			}
		}
		return pageCount;
	}
}
