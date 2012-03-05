<%@page import="com.twitstreet.db.data.StockInPortfolio"%>
<%@page import="com.twitstreet.db.data.Stock"%>
<%@page import="com.twitstreet.db.data.Portfolio"%>
<%@page import="com.twitstreet.market.PortfolioMgr"%>
<%@ page import="com.google.inject.Injector"%>
<%@ page import="com.google.inject.Guice"%>
<%@ page import="com.twitstreet.session.UserMgr"%>
<%@ page import="com.twitstreet.db.data.User"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.twitstreet.util.Util"%>
<%@ page import="com.twitstreet.session.UserMgr"%>
<%@ page import="com.twitstreet.localization.LocalizationUtil"%>
<%@ page import="com.twitstreet.servlet.TwitStreetServlet"%>
<%@page import="com.twitstreet.util.GUIUtil"%>

<%
	Injector inj = (Injector) pageContext.getServletContext()
			.getAttribute(Injector.class.getName());
	User user = (User) request.getAttribute(User.USER);
	PortfolioMgr portfolioMgr = inj.getInstance(PortfolioMgr.class);

	LocalizationUtil lutil = LocalizationUtil.getInstance();
	String lang = (String) request.getSession().getAttribute(
			LocalizationUtil.LANGUAGE);
	Portfolio portfolio = null;
	if (user != null) {
		portfolio = portfolioMgr.getUserPortfolio(user);
	}
	if (portfolio == null) {

		return;

	}
%>

<div id="portfolio" class="main-div">
	<table class="datatbl">
		<tr>
			<td>
				<jsp:include page="portfolioBar.jsp"></jsp:include>
			</td>
		</tr>
		<tr>
			<td>
				<div id="portfolio-content">
					<jsp:include page="portfolioContent.jsp"></jsp:include>
				</div>
			</td>
		</tr>
	</table>
</div>
