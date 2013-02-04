<%@ page import="com.twitstreet.servlet.UserProfileServlet"%>
<%@ page import="com.twitstreet.servlet.GetUserServlet"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.twitstreet.util.Util"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.twitstreet.db.data.User"%>
<%@ page import="com.twitstreet.localization.LocalizationUtil" %>
<%@page import="org.apache.log4j.Logger" %>
<%
	long start = 0;
	long end = 0;
	start = System.currentTimeMillis();
	Logger logger = Logger.getLogger(this.getClass());
	
	LocalizationUtil lutil = LocalizationUtil.getInstance();
	String lang = (String)request.getSession().getAttribute(LocalizationUtil.LANGUAGE);
		String getUserText = request.getParameter(GetUserServlet.GET_USER_PARAM) == null ? "" : (String) request.getParameter(GetUserServlet.GET_USER_PARAM);
		String getUserTextDisplay = request.getAttribute(GetUserServlet.GET_USER_DISPLAY) == null ? "" : (String) request.getParameter(GetUserServlet.GET_USER_DISPLAY);
		
		if(getUserText.length()>0){
%>
<div id="other-search-result">
			
			<%
				ArrayList<User> searchResults = (ArrayList<User>) request.getAttribute(GetUserServlet.GET_USER_OTHER_SEARCH_RESULTS);
		
				if (getUserText.length()>0 && searchResults != null && searchResults.size() > 0) {
			%>
			
			<h3><%=lutil.get("otherresults.header", lang,getUserText) %></h3>
			<table class="datatbl" style="margin-top: 10px;">
				
				<%
					for (int i = 0; i < searchResults.size();) {
				%>
				<tr>
					<%
						for (int j = 0; j < 3; j++) {
							if (i < searchResults.size()) {
								%>
			
								<td>
									<table>
										<tr>
											<td><img class="twuser" width="48" height="48" 
												src="<%=searchResults.get(i).getPictureUrl()%>" />
											</td>
											<td><a href="#!user=<%=searchResults.get(i).getId()%>" onclick="reloadIfHashIsMyHref(this); loadTitle('<%=lutil.get("user.bar.profile", lang, searchResults.get(i).getUserName())%>');"  title="<%=lutil.get("user.details.tip", lang, searchResults.get(i).getUserName()) %>">
													<%
														out.write(searchResults.get(i).getUserName());
													%> </a> 
												
													<br>
													<%
														//out.write(Util.commaSep(searchResults.get(i).getFollowerCount()));
													%>
											</td>
			
										</tr>
									</table>
								</td>
						<%
							} else {
						%>
								<td>
								
								
								</td>
						<%
							}
							i++;
						}
					%>
				</tr>
				<%
					}
				%>
			</table>
			<%
				
				
				}
			%>
		</div>
		
			<%
				}
			%>
			
	<%--		<%
			User user = (User) request.getAttribute(UserProfileServlet.USER_PROFILE_USER);

		if (user==null && getUserText.length() > 0) {
	%>	
			<div id="searchusernoresult"><p><%=lutil.get("shared.noresults", lang) %></p></div>
	<%
		}
	%> --%>
<%
end = System.currentTimeMillis();
logger.debug("getUser.jsp execution time: " + (end - start));
%>
	