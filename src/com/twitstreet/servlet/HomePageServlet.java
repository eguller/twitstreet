package com.twitstreet.servlet;

import java.io.IOException;
import java.security.MessageDigest;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.twitstreet.data.DashboardData;
import com.twitstreet.data.HomeData;

@SuppressWarnings("serial")
@Singleton
public class HomePageServlet extends HttpServlet {

	@Inject
	@Named("com.twitstreet.meta.ConsumerSecret")
	private final String consumerSecret = null;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//TODO !!! don't validate cookie until absolutely necessary: e.g. BUY
		
		// * Determine userid
		String userid = null;
		// don't create session until absolutely necessary: e.g. BUY
		HttpSession session = req.getSession(false);
		if (session != null) {
			userid = (String) session.getAttribute("user");
		}
		if (userid == null) {
			if(session==null) {
				session = req.getSession(true);
			}
			// read cookie "twitter_anywhere_identity"
			Cookie taCookie = getCookie(req.getCookies(),
					"twitter_anywhere_identity");
			if (taCookie != null) {
				String taid = taCookie.getValue();
				// parse cookie "user_id:signature"
				int idx = taid.indexOf(':');
				if (idx > 0) {
					String cUserid = taid.substring(0, idx);
					String signature = taid.substring(idx + 1, taid.length());
					try {
						// SHA1.hexdigest(user_id + consumer_secret)
						MessageDigest md = MessageDigest.getInstance("SHA-1");
						md.update(cUserid.getBytes());
						md.update(consumerSecret.getBytes());
						byte[] digest = md.digest();

						StringBuilder hexdigest = new StringBuilder();
						for (int i = 0; i < digest.length; i++) {
							int digByte = digest[i] & 0xFF;
							if (digByte < 0x10) {
								hexdigest.append('0');
							}
							hexdigest.append(Integer.toHexString(digByte));
						}

						// create a session if authenticated
						if (signature.equals(hexdigest.toString())) {
							userid = cUserid;
							session.setAttribute("user", userid);
						}
						else {
							taCookie.setMaxAge(0);
							resp.addCookie(taCookie);
						}

					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		}

		// * Populate request beans
		DashboardData dashboardData = new DashboardData();
		dashboardData.isVisible = false;
		dashboardData.userid = userid;

		HomeData data = new HomeData();
		data.dashboard = dashboardData;
		req.setAttribute("data", data);

		// * Let the view render
		getServletContext().getRequestDispatcher("/WEB-INF/jsp/home.jsp")
				.forward(req, resp);
	}

	private Cookie getCookie(Cookie[] cookies, String name) {
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (name.equals(cookie.getName())) {
					return cookie;
				}
			}
		}
		return null;
	}
}
