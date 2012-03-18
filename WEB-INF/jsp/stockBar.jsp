<%@page import="com.twitstreet.db.data.Stock"%>
<%@page import="com.twitstreet.servlet.HomePageServlet"%>
<%@ page import="com.twitstreet.localization.LocalizationUtil"%>
<%@ page import="com.twitstreet.db.data.User"%>
<%
	LocalizationUtil lutil = LocalizationUtil.getInstance();
	String lang = (String) request.getSession().getAttribute(
			LocalizationUtil.LANGUAGE);
	

	Stock stock = (Stock) request.getAttribute(HomePageServlet.STOCK);
%>
<div class="main-tabs" style="width: 100%;">
	<div class="title-bar">
		<a class="suggested-stocks-tab <%=(stock!=null)?"": "youarehere"%> "
			onclick="showSuggestedStocksContent(); loadSuggestedStocks();">
			<%=lutil.get("suggestedstocks.header", lang)%> </a>
	
		<a class="stock-details-tab <%=(stock==null)?"": "youarehere"%>"
			onclick="showStockDetailsContent(); reloadStock();">
			 <%=(stock==null)?"": "\""+stock.getName()+"\""%> 
			<%=lutil.get("stockdetails", lang)%> </a>
	
	</div>
</div>
