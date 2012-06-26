<%@page import="com.twitstreet.db.data.Stock"%>
<%@page import="com.twitstreet.servlet.HomePageServlet"%>
<%@ page import="com.twitstreet.localization.LocalizationUtil"%>
<%@ page import="com.twitstreet.db.data.User"%>
<%
	LocalizationUtil lutil = LocalizationUtil.getInstance();
	String lang = (String) request.getSession().getAttribute(
			LocalizationUtil.LANGUAGE);
	
	String selectedTab = (String) request.getAttribute(HomePageServlet.SELECTED_TAB_STOCK_BAR);

	Stock stock = (Stock) request.getAttribute(HomePageServlet.STOCK);
%>
<div class="main-tabs" style="width: 100%;">
	<div class="title-bar">
		<a class="suggested-stocks-tab <%=(!selectedTab.equalsIgnoreCase("suggested-stocks-tab"))?"": "youarehere"%> "

			onclick="reloadIfHashIs('#!suggestedstocks'); loadTitle('<%=lutil.get("suggestedstocks.title", lang)%>'); setPageUrl('.stocks-tab','/#!suggestedstocks');">

			<%=lutil.get("suggestedstocks.header", lang)%> </a>
		<a class="top-grossing-stocks-tab <%=(!selectedTab.equalsIgnoreCase("top-grossing-stocks-tab"))?"": "youarehere"%>"

			onclick="reloadIfHashIs('#!topgrossingstocks'); loadTitle('<%=lutil.get("topgrossingstocks.title", lang)%>'); setPageUrl('.stocks-tab','/#!topgrossingstocks');">

			<%=lutil.get("topgrossingstocks.header", lang)%> </a>
	<%if(stock!=null){ %>
		<a class="stock-details-tab <%=(!selectedTab.equalsIgnoreCase("stock-details-tab"))?"": "youarehere"%>"

			onclick="reloadIfHashIs('#!stock=<%=stock.getId()%>'); loadTitle('<%=lutil.get("stock.bar.profile", lang, stock.getName())%>'); setPageUrl('.stocks-tab','/#!stock=<%=stock.getId()%>');">
			 <%=stock.getName()%> 
			</a>
		<%} %>
	</div>
</div>
