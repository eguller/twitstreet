package com.twitstreet.util;

import java.util.Date;

import com.twitstreet.db.data.Stock;
import com.twitstreet.localization.LocalizationUtil;

public class GUIUtil {

	private static GUIUtil instance = new GUIUtil();
	private LocalizationUtil lutil = LocalizationUtil.getInstance();
	
	private GUIUtil() {

	}

	public static GUIUtil getInstance() {

		return instance;

	}

	public String getSpeedCalculation(Stock stock, String lang){
		
		Date date = new Date();
		
		int minuteLeft = 20 - (int)(date.getTime() - stock.getLastUpdate().getTime()) / (60 * 1000);
		
		minuteLeft=(minuteLeft<1)?1:minuteLeft;
		String activityMessage = LocalizationUtil.getInstance().get("stock.speedcalculation.tip", lang, String.valueOf(minuteLeft));
	
	
	
		return "<img alt='"+activityMessage+"' title='"+activityMessage+"' src='/images/activity_indicator_16.gif'/>";
	 
	
		
	}
	
	public String getVerifiedIcon(String lang){
		
		
		return "<img src='images/verified.png' title='"+lutil.get("stock.verified", lang)+"'/>";
	}
}
