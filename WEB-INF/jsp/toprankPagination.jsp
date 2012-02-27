<%@ page import="com.twitstreet.servlet.TopRankServlet"%>
<%@ page import="com.google.inject.Injector"%>
<%@ page import="com.google.inject.Guice"%>
<%@ page import="com.twitstreet.session.UserMgr"%>
<%@ page import="com.twitstreet.db.data.User"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.twitstreet.util.Util"%>

<%
	int pageSelectorPerRow = 4;

	Injector inj = (Injector) pageContext.getServletContext()
			.getAttribute(Injector.class.getName());

	UserMgr userMgr = inj.getInstance(UserMgr.class);

	int pageCount = Integer.valueOf(request.getParameter("pageCount"));

	int userCount = Integer.valueOf(request.getParameter("userCount"));
	int currPage = Integer.valueOf(request.getParameter("currPage"));

	if (pageCount < pageSelectorPerRow) {

		pageSelectorPerRow = pageCount;
	}
%>

<div class="h3" style="width: 100%; padding-right:5px" align="right">

	<%if(currPage!=1){%><span class="paginationLink"><a href="javascript:void(0)" onclick="toprankPrevPage()">&lt;&lt;</a></span>
	<%}else{%><span class="paginationDisabledLink">&lt;&lt;</span><%}%>
	
	
	<select  class="topRankSelect" onchange="toprank($(this).val())">
	
		<% 					
					
		for(int i = 0; i < pageCount;i++) {	
			
	       String intervalString = Util.getIntervalStringForPage(i+1, userMgr.getRecordPerPage(), userCount);
					
		%>
			<option <%=(i+1==currPage)?"selected=\"selected\"":""%> value="<%=i+1%>"><%=intervalString%></option>
			
		<%	
		}		
		%>
	</select>
	<%if(currPage!=pageCount){%><span class="paginationLink"><a href="javascript:void(0)" onclick="toprankNextPage()">&gt;&gt;</a></span>
	<%}else{%><span class="paginationDisabledLink">&gt;&gt;</span><%}%>
	
</div>

