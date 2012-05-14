/**
	TwitStreet - Twitter Stock Market Game
    Copyright (C) 2012  Engin Guller (bisanthe@gmail.com), Cagdas Ozek (cagdasozek@gmail.com)

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

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import com.google.inject.Inject;
import com.twitstreet.db.data.Stock;
import com.twitstreet.db.data.StockHistoryData;
import com.twitstreet.localization.LocalizationUtil;
import com.twitstreet.market.StockMgr;
import com.twitstreet.market.StockMgrImpl;

public class GUIUtil {
	ResourceBundle propertiesFile;
	private static GUIUtil instance = new GUIUtil();
	private LocalizationUtil lutil = LocalizationUtil.getInstance();
	StockMgr stockMgr = StockMgrImpl.getInstance();;
	
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
	
	public String getTwitterHashButton(String hashTag, String lang,String text){
		String buttonStr = propertiesFile.getString("twitter.hash.button");
	
	
		buttonStr = buttonStr.replace("{0}", hashTag);
		buttonStr = buttonStr.replace("{1}", lang);
		buttonStr = buttonStr.replace("{2}", text);
		return buttonStr;	
	}	
	public String getTwitterHashButton(String hashTag, String lang){
	;
		return getTwitterHashButton(hashTag, lang, "");	
	}
	public String getTwitterMentionButton(String mention, String lang, String text){
		String buttonStr = propertiesFile.getString("twitter.mention.button");
	
	
		buttonStr = buttonStr.replace("{0}", mention);
		buttonStr = buttonStr.replace("{1}", lang);
		if(text==null){
			text = "";
		}
		buttonStr = buttonStr.replace("{2}", text);
		return buttonStr;	
	}
	public String getTwitterMentionButton(String mention, String lang){
		
		return getTwitterMentionButton(mention, lang,null);	
	}
	
	public String getTimeLine(Stock stock, StockHistoryData shd, String chartNamePrefix, String divIdPrefix,String format){
		StringBuilder scriptStrBuilder = new StringBuilder();
		scriptStrBuilder.append("<script type='text/javascript'>" +
		"var dateArrayStock"+stock.getId()+" = new Array();"+
		"var valueArrayStock"+stock.getId()+" = new Array();"+
		"var stockNameStock"+stock.getId()+" = '" + stock.getName() +"';"+
		"\n");
		
		
	
	
		
			LinkedHashMap<Date, Integer> dvm = shd.getDateValueMap();

			scriptStrBuilder.append("dateArrayStock"+stock.getId()+".push(new Date(" + stock.getLastUpdate().getTime() + "));\n");

			scriptStrBuilder.append("valueArrayStock"+stock.getId()+".push(" + stock.getTotal() + ");\n");

			for (Date date : dvm.keySet()) {
				scriptStrBuilder.append("dateArrayStock"+stock.getId()+".push(new Date(" + date.getTime() + "));\n");

				scriptStrBuilder.append("valueArrayStock"+stock.getId()+".push(" + dvm.get(date) + ");\n");
		}
			scriptStrBuilder.append("drawStockHistory('" + chartNamePrefix+ "-"+stock.getId()+"' , '"+divIdPrefix+"-"+stock.getId()+"', dateArrayStock"+stock.getId()+", valueArrayStock"+stock.getId()+",	stockNameStock"+stock.getId()+",'"+format+"'");
			scriptStrBuilder.append("</script>");
			
			String returnStr = scriptStrBuilder.toString();
			return returnStr;	
	}
}
