<%@page import="com.twitstreet.servlet.UserProfileServlet"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.twitstreet.twitter.SimpleTwitterUser"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.twitstreet.db.data.UserStock"%>
<%@page import="java.sql.SQLException"%>
<%@page import="com.twitstreet.market.StockMgr"%>
<%@ page import="com.google.inject.Injector"%>
<%@ page import="com.google.inject.Guice"%>
<%@ page import="com.twitstreet.db.data.User"%>
<%@ page import="com.twitstreet.db.data.Stock"%>
<%@ page import="com.twitstreet.util.Util"%>
<%@page import="com.twitstreet.db.data.Portfolio"%>
<%@page import="com.twitstreet.market.PortfolioMgr"%>
<%@page import="com.twitstreet.config.ConfigMgr"%>
<%@page import="com.twitstreet.session.UserMgr"%>
<%@ page import="com.twitstreet.servlet.HomePageServlet"%>
<%@ page import="com.twitstreet.localization.LocalizationUtil" %>
	

<%@page import="com.twitstreet.session.UserMgr"%>
<%
	Injector inj = (Injector) pageContext.getServletContext().getAttribute(Injector.class.getName());
	UserMgr userMgr = inj.getInstance(UserMgr.class);

	LocalizationUtil lutil = LocalizationUtil.getInstance();
	String lang = (String)request.getSession().getAttribute(LocalizationUtil.LANGUAGE);
	Stock stock = (Stock) request.getAttribute(HomePageServlet.STOCK);
	String quote = request.getAttribute(HomePageServlet.QUOTE) == null ? "" : (String) request.getAttribute(HomePageServlet.QUOTE);
	String quoteDisplay = request.getAttribute(HomePageServlet.QUOTE_DISPLAY) == null ? "" : (String) request.getAttribute(HomePageServlet.QUOTE_DISPLAY);

	User user = null;

	try {
		String userId = (String) request.getParameter(User.USER);
		user = userMgr.getUserById(Long.valueOf(userId));
	} catch (Exception ex) {

	}

	if (quote == null || quote.length() < 1) {

		quote = quoteDisplay;
	}
	if (quote == null || quote.length() < 1) {
		if (user != null) {
			quote = user.getUserName();
		}

	}
	
	if (quote == null || quote.length() < 1) {
		quote = "Barack Obama";

	}
%>
<div id="get-quote-div">

	
	<div id="quoteholder" class="main-div">

		<div>
			<input type="text" class="textbox" id="getQuoteTextboxId" value="<%=quote%>"  onFocus="if(this.value == 'Enter Your Default Text Here') {this.value = '';}" onBlur="if (this.value == '') {this.value = 'Enter Your Default Text Here';}"
				name="quote" /> <input type="button" id="getQuoteButton"
				onclick="reloadIfHashIs('#!searchstock='+$('#getQuoteTextboxId').val());window.location = '#!searchstock='+$('#getQuoteTextboxId').val(); loadTitle('<%= lutil.get("stocks.search.title", lang, quote) %>)"  value="<%=lutil.get("shared.search", lang) %>">
			<script>
				jQuery('#getQuoteTextboxId').click(function() {
					selectAllText(jQuery(this))
				});
			</script>
			
		</div>
	
		<input type="hidden" id="quote-hidden" value="<%=quote%>" /> <input
			type="hidden" id="quote-id"
			value="<%=stock == null ? "" : stock.getId()%>" />
	</div>

	<script type="text/javascript">
		$("#getQuoteTextboxId").keyup(function(event) {
			if (event.keyCode == 13) {
				$("#getQuoteButton").click();
			}
		});
	</script>


</div>


