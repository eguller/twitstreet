package com.twitstreet.servlet;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitstreet.base.Result;
import com.twitstreet.db.data.UserDO;
import com.twitstreet.session.UserMgr;
import com.twitstreet.util.Encryption;
import com.twitstreet.util.Util;

@SuppressWarnings("serial")
@Singleton
public class SignupServlet extends HttpServlet {
	@Inject UserMgr userMgr;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req,resp);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String userName = req.getParameter("userName");
		String password = req.getParameter("password");
		String email = req.getParameter("email");
		
		if(userName == null || userName.length() < 4 || userName.length() > 9){
			
		}
		
		if(password == null || password.length() == 0 || password.length() > 16){
			
		}
		
		if(email == null || !Util.isValidEmailAddress(email)){
			
		}
		
		String md5 = "";
		try {
			md5 = Encryption.md5(password);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		UserDO user = new UserDO();
		user.setCash(10000);
		user.setEmail(email);
		user.setPassword(md5);
		user.setUserName(userName);
		user.setFirstLogin(Calendar.getInstance().getTime());
		user.setLastLogin(Calendar.getInstance().getTime());
		user.setLastIp(req.getRemoteAddr());
		
		Result<UserDO> result = userMgr.signup(user);
		
	}
}
