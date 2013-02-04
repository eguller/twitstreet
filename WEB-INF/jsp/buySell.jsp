
<%@page import="java.util.Locale"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page import="com.twitstreet.servlet.HomePageServlet"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="com.twitstreet.db.data.StockHistoryData"%>
<%@page import="com.twitstreet.db.data.UserStock"%>
<%@page import="java.sql.SQLException"%>
<%@page import="com.twitstreet.market.StockMgr"%>
<%@ page import="com.google.inject.Injector"%>
<%@ page import="com.twitstreet.db.data.User"%>
<%@ page import="com.google.inject.Guice"%>
<%@ page import="com.twitstreet.util.Util"%>
<%@ page import="com.twitstreet.db.data.Portfolio"%>
<%@page import="com.twitstreet.config.ConfigMgr"%>
<%@ page import="com.twitstreet.market.PortfolioMgr"%>
<%@page import="com.twitstreet.session.UserMgr"%>
<%@ page import="com.twitstreet.db.data.UserStockDetail"%>
<%@ page import="java.util.List"%>
<%@ page import="com.twitstreet.util.Util"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="com.twitstreet.market.StockMgr"%>
<%@ page import="com.twitstreet.db.data.Stock"%>
<%@ page import="com.twitstreet.servlet.TwitStreetServlet"%>
<%@ page import="com.twitstreet.localization.LocalizationUtil" %>
<%@page import="org.apache.log4j.Logger" %>
<%
	long start = 0;
	long end = 0;
	start = System.currentTimeMillis();
	Logger logger = Logger.getLogger(this.getClass());
	
	LocalizationUtil lutil = LocalizationUtil.getInstance();
	String lang = (String)request.getSession().getAttribute(LocalizationUtil.LANGUAGE);

	Injector inj = (Injector) pageContext.getServletContext().getAttribute(Injector.class.getName());
	UserMgr userMgr = inj.getInstance(UserMgr.class);
	User user = (User) request.getAttribute(User.USER);

	PortfolioMgr portfolioMgr = inj.getInstance(PortfolioMgr.class);
	StockMgr stockMgr = inj.getInstance(StockMgr.class);

	ConfigMgr configMgr = inj.getInstance(ConfigMgr.class);

	Stock stock = null;

	stock = (Stock) request.getAttribute(HomePageServlet.STOCK);

	UserStock userStock = null;
	if (user != null && stock != null) {
		userStock = portfolioMgr.getStockInPortfolio(user.getId(), stock.getId());

	}
%>



<div id="buy-sell-section">
	<div id="userfound">

		
		
		<%if (stock != null) {
		
			%>	
			<div class="field-white">
			<table class="datatbl">
				<tr>


					<td>
						<%
							int col = 3;
						%>
						<table class="datatbl">
							<tr>
								<td style="width: <%=100 / col%>%; text-align: center;"><span
									class="green-light"><b><%=lutil.get("stock.available", lang) %></b> </span></td>
								<td style="width: <%=100 / col%>%; text-align: center;"><span
									class="red-light"><b><%=lutil.get("stock.sold", lang) %></b> </span></td>
								<td style="width: <%=100 / col%>%; text-align: center;"><b><%=lutil.get("stock.total", lang) %></b>
								</td>
								
							</tr>
							<tr>
								<td id="available" style="width:  <%=100 / col%>%; text-align: center;">
									<%=Util.getNumberFormatted(stock.getAvailable(), false, true, false, false, false, false)%>											
								</td>
								<td id="sold" style="width:  <%=100 / col%>%; text-align: center;">
								
									<%=Util.getNumberFormatted(stock.getTotal() - stock.getAvailable(), false, true, false, false, false, false)%>
								
								</td>
								<td id="total" style="width:  <%=100 / col%>%; text-align: center;">
									<table class="datatbl2">
										<tr>
											<td width="33%">
											</td>
											<td width="33%" align="center">
												<%=Util.commaSep(stock.getTotal())%>
											</td>
											<td width="33%" align="right">
												<%
													if (stock.getChangePerHour() != 0) {

														out.write(Util.getNumberFormatted(stock.getChangePerHour(), false, true, true, true, false, true));
													}
												%>
											</td>
										</tr>											
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</div>
		
		
		<%	
		Date dateAvailable = new Date();
		if (!stock.isOldEnough()) {
				
				dateAvailable = new Date( stock.getCreatedAt().getTime()+ Stock.STOCK_OLDER_THAN_DAYS_AVAILABLE * 1000 * 60 * 60 * 24 );
				
				
				
				String localizedDateFormat = lutil.get("date.format.full", lang,String.valueOf(Stock.STOCK_OLDER_THAN_DAYS_AVAILABLE));

				SimpleDateFormat sdf = new SimpleDateFormat(localizedDateFormat,new Locale(lang));
				
				String localizedDate = sdf.format(dateAvailable);
			
			%>
		
			<div style="margin-top:10px" class="field-white">
			<br>
			<br>
				<b class="red"><%=lutil.get("stock.notOldEnough", lang) %></b>
				<br>
					<br>
				<%=lutil.get("stock.accountShouldBeOld", lang,Stock.STOCK_OLDER_THAN_DAYS_AVAILABLE) %>
				<br>
				<br>
				<b>"<%=stock.getName()%>" <%=lutil.get("stock.willBeAvailableAt", lang,localizedDate) %></b>	
				<br>
				<br>
				<div align="center" id="stockCountdown" style="height:80px; width:100%">
				
				</div>
			</div>
			
		
			<script>$('#stockCountdown').countdown({until: new Date(<%=dateAvailable.getTime()%>)});</script>
			<%}else {%>
		
			<table class="datatbl" style="margin-top: 10px;">
			
				
				<tr>
					<td colspan="3">
						
					</td>
				</tr>

	
				<tr>
					<td colspan="3" style="text-align: center;">
	
					<div id="dashboard-message-field" style="margin-top: 6px;"
											class="field-white">
					<p style="width: 100%; text-align: center; margin-top: 10px; margin-bottom: 10px; padding-top: 5px; padding-bottom: 5px;">
					<span id="user-stock"> 
								<%
								
								if(user!=null){
									%>
									
										
												
												<%	
												 	if (userStock == null) {
												 		out.write(lutil.get("stock.youdonthave", lang,stock.getName()) +"<br>");
												 	} else {
												 		int amount = (int) (userStock.getPercent() * stock.getTotal());
												 		String commaSep = Util.commaSep(amount);
												 		out.write(lutil.get("stock.youhave", lang,new Object[]{commaSep, stock.getName()})+"<br>");
												 	}
												
												 	if (user.getCash() < 1 && stock.getAvailable()>0) {
												 		out.write(lutil.get("stock.notenoughcash", lang,stock.getName()));
												 	}%> 
											
										
								
								<%	
								}
								else{
	 								
	 							
	 											out.write(lutil.get("stockdetails.notsignedin", lang));
	 							
	 							
	 							}%>	
	 							</span>
						</p>
						
				</div>
			</td>
	
	
	
		</tr>
				<tr id="buy-links-row">
					<td colspan="3" id="buy-links">
						<div id="buy-sell-div">
							<table class="buy-sell-table">
								<%
									if (user != null && stock != null) {
										ArrayList<Double> buyValues = new ArrayList<Double>();
										ArrayList<Double> sellValues = new ArrayList<Double>();
										double totalCash = user.getCash();
										int available = stock.getAvailable();
										int min = (int) Math.min(totalCash, available);
	
										int i = min == 0 ? 0 : String.valueOf((int) min).length();
										if ((int) Math.pow(10, i - 1) != min && min > 0) {
											buyValues.add(Math.floor(min));
										}
	
										for (; i > 0; i--) {
											double tenTimesX = Math.pow(10, i - 1);
											buyValues.add(tenTimesX);
											if(i!=1){
												buyValues.add(tenTimesX/2);
												
											}
										}
	
										if (userStock != null) {
											double userTotalStock = (userStock.getPercent() * stock.getTotal());
											i = userTotalStock < 1 ? 0 : String.valueOf((int) userTotalStock).length();
											
											double tenTimesX = Math.pow(10, i - 1);
											if (userTotalStock != tenTimesX) {
												sellValues.add(Math.floor(userTotalStock));
											}
											for (; i > 0; i--) {
												tenTimesX = Math.pow(10, i - 1);
												sellValues.add(tenTimesX);
												if(i!=1){
													sellValues.add(tenTimesX/2);
													
												}
											}
											
											
										}
										i = 0;
	
										if (sellValues.size() > 0 && ((user.getCash() + user.getPortfolio()) > configMgr.getComissionTreshold())) {
								%>
								<tr>
									<td colspan="3" style="text-align: center; padding-top: 10px;">
										<p style="margin-bottom: 10px;">
										
										
										
										<%=lutil.get("stockdetails.commission", lang) %>
										
										
										
										<p>
									</td>
								</tr>
								<%
									}
	
										while (true) {
											out.write("<tr>");
											out.write("<td>");
											if (i < buyValues.size() && stock.getTotal() > configMgr.getMinFollower()) {
												out.write("<button class=\"buy-button\" onclick=\"buy(" + stock.getId() + "," + buyValues.get(i) + ");\">");
												out.write(lutil.get("stockdetails.buy", lang)+"<br>");
												out.write(Util.commaSep(buyValues.get(i).intValue()));
												out.write("</button>");
											}
											out.write("</td>");
											out.write("<td>");
											if (i < sellValues.size()) {
												out.write("<button class=\"sell-button\" onclick=\"sell(" + stock.getId() + "," + sellValues.get(i) + ");\">");
												out.write(lutil.get("stockdetails.sell", lang)+"<br>");
												out.write(Util.commaSep(sellValues.get(i).intValue()));
												out.write("</button>");
											}
											out.write("</td>");
											out.write("</tr>");
											i++;
											if (i > buyValues.size() && i > sellValues.size()) {
												break;
											}
										}
									}
								%>
							</table>
						</div>
					</td>
				</tr>
	
			</table>
				
			<%
			} 	
			
		}
		%>
	</div>
</div>
<%
end = System.currentTimeMillis();
logger.debug("balance.jsp execution time: " + (end - start));
%>


