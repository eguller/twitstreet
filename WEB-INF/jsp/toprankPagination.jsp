<%@page import="com.twitstreet.localization.LocalizationUtil"%>
<%@ page import="com.twitstreet.servlet.TopRankServlet"%>
<%@ page import="com.google.inject.Injector"%>
<%@ page import="com.google.inject.Guice"%>
<%@ page import="com.twitstreet.session.UserMgr"%>
<%@ page import="com.twitstreet.db.data.User"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.twitstreet.util.Util"%>
<%@page import="org.apache.log4j.Logger" %>
<%
	long start = 0;
	long end = 0;
	start = System.currentTimeMillis();
	Logger logger = Logger.getLogger(this.getClass());
	
	int pageSelectorPerRow = 4;

	Injector inj = (Injector) pageContext.getServletContext()
			.getAttribute(Injector.class.getName());

	UserMgr userMgr = inj.getInstance(UserMgr.class);
	User sessionUser = (User)request.getAttribute(User.USER);
	int pageCount = Integer.valueOf(request.getParameter("pageCount"));

	int userCount = Integer.valueOf(request.getParameter("itemCount"));
	int currPage = Integer.valueOf(request.getParameter("currPage"));

	LocalizationUtil lutil = LocalizationUtil.getInstance();
	String lang = (String) request.getSession().getAttribute(
			LocalizationUtil.LANGUAGE);
	if (pageCount < pageSelectorPerRow) {

		pageSelectorPerRow = pageCount;
	}
%>

<!-- <div class="h3" style="width: 100%; padding-right:5px;margin-bottom: 10px;" align="right"> -->
<div>
	<%if(currPage!=1){%><span class="paginationLink" style="font-size: 10px"><a href="javascript:void(0)" onclick="toprankPrevPage()">&lt;&lt;</a></span>
	<%}else{%><span class="paginationDisabledLink" style="font-size: 10px">&lt;&lt;</span><%}%>
	
	
	<select  class="topRankSelect" style="font-size: 10px" onchange="toprank($(this).val())">
		<%if(sessionUser!=null){%><option value="0" style="text-align:center;"><%=lutil.get("ranking.showme",lang) %></option><%}%>
		<% 		
		for(int i = 0; i < pageCount;i++) {	
			
	       String intervalString = Util.getIntervalStringForPage(i+1, UserMgr.MAX_TOPRANK_USER, userCount);
		%>
		<option style="text-align:center;" <%=(i+1==currPage)?"selected=\"selected\"":""%> value="<%=i+1%>"><%=intervalString%></option>
		<%	
		}		
		%>
	</select>
	<%if(currPage!=pageCount){%><span class="paginationLink" style="font-size: 10px"><a href="javascript:void(0)" onclick="toprankNextPage()">&gt;&gt;</a></span>
	<%}else{%><span class="paginationDisabledLink" style="font-size: 10px">&gt;&gt;</span><%}%>
	
</div>
<%
end = System.currentTimeMillis();
logger.debug("toprankPagination.jsp execution time: " + (end - start));
%>
