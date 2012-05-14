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

package com.twitstreet.mail;

import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.twitstreet.config.ConfigMgr;
import com.twitstreet.db.base.DBMgr;

public class MailMgrImpl implements MailMgr {
	public static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

	private  String SMTP_HOST_NAME = "smtp.gmail.com";
	private  String SMTP_PORT = "465";
	private  String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	private static Logger logger = Logger.getLogger(MailMgrImpl.class);
	@Inject
	DBMgr dbMgr;
	@Inject
	ConfigMgr configMgr;
	
	@Override
	public void sendMail(String subject, String message) throws MessagingException {
		boolean debug = false;
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

		Properties props = new Properties();
		props.put("mail.smtp.host", SMTP_HOST_NAME);
		props.put("mail.smtp.auth", "true");
		props.put("mail.debug", "true");
		props.put("mail.smtp.port", SMTP_PORT);
		props.put("mail.smtp.socketFactory.port", SMTP_PORT);
		props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
		props.put("mail.smtp.socketFactory.fallback", "false");

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {

			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(configMgr.getMailDealer(),configMgr.getMailDealerPassword());
			}
		});

		session.setDebug(debug);

		Message msg = new MimeMessage(session);
		msg.setFrom();

		String[] recipients = configMgr.getMailRecipients();
		InternetAddress[] addressTo = new InternetAddress[recipients.length];
		for (int i = 0; i < recipients.length; i++) {
			addressTo[i] = new InternetAddress(recipients[i]);
		}
		msg.setRecipients(Message.RecipientType.TO, addressTo);

		// Setting the Subject and Content Type
		msg.setSubject(subject);
		msg.setContent(message, "text/plain;charset=UTF-8");

		Transport.send(msg);
	}


}
