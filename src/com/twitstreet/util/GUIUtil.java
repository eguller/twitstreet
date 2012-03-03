package com.twitstreet.util;

import java.util.Date;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import com.twitstreet.db.data.Stock;
import com.twitstreet.localization.LocalizationUtil;

public class GUIUtil {
	ResourceBundle propertiesFile;
	private static GUIUtil instance = new GUIUtil();
	private LocalizationUtil lutil = LocalizationUtil.getInstance();
	
	private GUIUtil() {
		String propFileStr = this.getClass().getName();

		propertiesFile = PropertyResourceBundle.getBundle(propFileStr);
		
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
		
		
		return "<img src='images/verified.png'  style=\"vertical-align: top\" title='"+lutil.get("stock.verified", lang)+"'/>";
	}
	
	public String getTwitterShareButton(String relativeUrl, String initialText, String lang){

		return getTwitterShareButton(relativeUrl, initialText, lang, null);	
	}
	public String getTwitterShareButton(String relativeUrl, String initialText, String lang, Object param){

		return getTwitterShareButton(relativeUrl, initialText, lang, new Object[]{param});	
	}
	
	public String getTwitterShareButton(String relativeUrl, String shareText, String lang, Object[] params){
		String buttonStr = propertiesFile.getString("twitter.share.button");
		buttonStr = buttonStr.replace("{0}", relativeUrl);
		
		
		String shareString = lutil.get(shareText, lang);
		if (params != null && params.length > 0) {
			int i = 0;
			for (Object obj : params) {

				String iStr = "{" + i + "}";

				shareString = shareString.replace(iStr, obj.toString());

				i++;

			}
		}

		buttonStr = buttonStr.replace("{1}", shareString);
		buttonStr = buttonStr.replace("{2}", lang);
		
		return buttonStr;	
	}
	
	public String getTwitterFollowButton(String userToFollow, String lang){
		String buttonStr = propertiesFile.getString("twitter.follow.button");
	
	
		buttonStr = buttonStr.replace("{0}", userToFollow);
		buttonStr = buttonStr.replace("{1}", lang);
		//buttonStr = buttonStr.replace("{2}", userToFollow);
		return buttonStr;	
	}
	
	public String getTwitterHashButton(String hashTag, String lang){
		String buttonStr = propertiesFile.getString("twitter.hash.button");
	
	
		buttonStr = buttonStr.replace("{0}", hashTag);
		buttonStr = buttonStr.replace("{1}", lang);
		//buttonStr = buttonStr.replace("{2}", userToFollow);
		return buttonStr;	
	}
	
	public String getTwitterMentionButton(String mention, String lang){
		String buttonStr = propertiesFile.getString("twitter.mention.button");
	
	
		buttonStr = buttonStr.replace("{0}", mention);
		buttonStr = buttonStr.replace("{1}", lang);
		//buttonStr = buttonStr.replace("{2}", userToFollow);
		return buttonStr;	
	}
}
