package com.twitstreet.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import twitter4j.Twitter;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.db.data.User;
import com.twitstreet.market.TransactionMgr;
import com.twitstreet.session.UserMgr;
import com.twitstreet.session.UserMgrImpl;
import com.twitstreet.twitter.TwitterProxy;
import com.twitstreet.twitter.TwitterProxyFactory;

@SuppressWarnings("serial")
@Singleton
public class BuyServlet extends HttpServlet {
	private static Logger logger = Logger.getLogger(UserMgrImpl.class);
	@Inject TransactionMgr transactionMgr;
	@Inject UserMgr sessionMgr;
	@Inject UserMgr userMgr;
	@Inject TwitterProxyFactory twitterProxyFactory = null;
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		doPost(request, response);
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		User user = (User) request.getSession(false).getAttribute(User.USER);
		if(user == null){
			//uses someone else account to get quote for unauthenticated users.
			user = userMgr.random();
			if(user != null){
				TwitterProxy twitterProxy = twitterProxyFactory.create(user.getOauthToken(), user.getOauthTokenSecret());
			}
		}
		else{
			String stock = request.getParameter("tuser");
			String amount = request.getParameter("amount");
			TwitterProxy proxy = twitterProxyFactory.create(user.getOauthToken(), user.getOauthTokenSecret());
			
			try {
				transactionMgr.buy("buyer", stock, Integer.parseInt(amount));
			} catch (NumberFormatException e) {
				// TODO Wrong stock amount inform user
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Database operation failed inform user
				e.printStackTrace();
			}
		}

	}
}
