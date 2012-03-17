package com.twitstreet.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import com.google.inject.Inject;
import com.twitstreet.localization.LocalizationUtil;
import com.twitstreet.session.UserMgr;

public class Util {

	private static final int SEC = 0;
	private static final int MIN = 1;
	private static final int HOUR = 2;
	private static final int DAY = 3;
	private static final int WEEK = 4;
	private static final int MONTH = 5; // assume 1 month is 4 week;
	private static final int YEAR = 6;

	private static long[] TIME = new long[YEAR + 1];
	
	// TIME[SEC] = 1000;
	// TIME[MIN] = 60 * TIME[SEC];
	// TIME[HOUR] = 60 * TIME[MIN];
	// TIME[DAY] = 24 * TIME[HOUR];
	// TIME[WEEK] = 7 * TIME[DAY];
	// TIME[MONTH] = 4 * TIME[WEEK];
	// TIME[YEAR] = 12 * TIME[MONTH];

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
		if (percentage > 0) {
			arrowStr = "&#9650;";
			plusMinus = "+";
			spanClass = "green-profit";

		} else if (percentage < 0) {
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

	public static String dateDiff2String(Date date) {
		Date now = Calendar.getInstance().getTime();
		return null;
	}

}
