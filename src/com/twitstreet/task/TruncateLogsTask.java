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

package com.twitstreet.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.twitstreet.config.ConfigMgr;
import com.twitstreet.mail.MailMgr;
import com.twitstreet.market.StockMgr;
import com.twitstreet.season.SeasonMgr;
import com.twitstreet.session.UserMgr;
import com.twitstreet.twitter.TwitterProxyFactory;

public class TruncateLogsTask implements Runnable {
	
	private static long  ONE_HOUR =  60* 60 *1000;
	@Inject
	TwitterProxyFactory twitterProxyFactory = null;
	@Inject
	ConfigMgr configMgr;
	@Inject
	SeasonMgr seasonMgr;
	@Inject
	UserMgr userMgr;
	@Inject
	StockMgr stockMgr;
	@Inject
	MailMgr mailMgr;
	private static Logger logger = Logger.getLogger(TruncateLogsTask.class);

	@Override
	public void run() {
	
 
		while (true) {
			try {
				logger.info("********************    PERFORMING TRUNCATE LOGS OPERATION    ********************");

				ArrayList<File> fileList = getOldLogs();
				ArrayList<File> filesToBeDeleted = new ArrayList<File>();
				
				
				for (File file : fileList) {
					String logStr = readFile(file);
					try {
						mailMgr.sendMail("Server "+configMgr.getServerId()+" : "+ file.getName(), logStr);
						logger.info("Sent log file: " + file.getName());
						filesToBeDeleted.add(file);
						
					} catch (Exception ex) {
						logger.info("Error in mailing log file: " + file.getName(), ex);
					}

				}

				
				for (File file : filesToBeDeleted) {
					if (file.delete()) {
						logger.info("Deleted log file: " + file.getName());
					} else {
						logger.info("Couldn't delete log file: " + file.getName());
					}
				}
				logger.info("********************      END OF TRUNCATE LOGS OPERATION      ********************");
				
			
			} catch (Throwable ex) {
				logger.error("Someone tried to kill our precious TruncateLogsTask. He says: ", ex);
			}
		
			
			try {
				Thread.sleep(ONE_HOUR);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}
	private String readFile(File file) throws IOException {
		
		  FileInputStream stream = new FileInputStream(file);
		  String fileContent = "";
		  try {
		    FileChannel fc = stream.getChannel();
		    MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
		 
		    /* Instead of using default, pass in a decoder. */
		    fileContent = Charset.defaultCharset().decode(bb).toString();
		  }
		  catch(Exception ex){
			  logger.info("Failed to read file: "+file.getName(), ex);
		  }
		  finally {
			 
		    stream.close();

		  }
		  return fileContent;
	}
	private ArrayList<File> getOldLogs(){

		String logPath ="";
		
		if(configMgr.isDev()){
			logPath = System.getProperty("catalina.home") + "/logs";
		}else{
			logPath = "/var/log/tomcat6";
		}
		File file = new File(logPath);

		File[] files = file.listFiles();
		ArrayList<File> fileList = new ArrayList<File>();
		for (int fileInList = 0; fileInList < files.length; fileInList++) {
			String fileName = files[fileInList].getName();
			if (fileName.startsWith("twitstreet.txt.")) {
				File fileCandidate = new File(logPath + "/" + fileName);
				if (fileCandidate.lastModified() < (new Date()).getTime() - 12 * 60 * 60 * 1000) {
					fileList.add(fileCandidate);
				}
			}
		}
		return fileList;
	}

}
