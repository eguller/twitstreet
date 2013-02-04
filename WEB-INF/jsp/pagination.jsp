<%@page import="com.twitstreet.servlet.PaginationDO"%>
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
	
	LocalizationUtil lutil = LocalizationUtil.getInstance();
	String lang = (String) request.getSession().getAttribute(LocalizationUtil.LANGUAGE);
	Injector inj = (Injector) pageContext.getServletContext().getAttribute(Injector.class.getName());

	UserMgr userMgr = inj.getInstance(UserMgr.class);
	User sessionUser = (User) request.getAttribute(User.USER);

	PaginationDO pdo = (PaginationDO) request.getAttribute("pdo");
	request.setAttribute("pdo", null);
	int pageCount;
	int recordPerPage;
	int itemCount;
	int currPage;
	String name;
	String fn;
	if (pdo != null) {
		pageCount = pdo.getPageCount();
		recordPerPage = pdo.getRecordPerPage();
		itemCount = pdo.getItemCount();
		name = pdo.getName();
		fn = pdo.getOnChangeFunction();
		currPage = pdo.getCurrPage();
	} else {
		try {
			pageCount = Integer.valueOf(request.getParameter("pageCount"));
			recordPerPage = Integer.valueOf(request.getParameter("recordPerPage"));
			itemCount = Integer.valueOf(request.getParameter("itemCount"));
			name = String.valueOf(request.getParameter("paginationName"));
			fn = String.valueOf(request.getParameter("onChangeFunction"));
			currPage = Integer.valueOf(request.getParameter("currPage"));
		} catch (Exception ex) {
			return;
		}
	}
%>

<!-- <div class="h3" style="width: 100%; padding-right:5px;margin-bottom: 10px;" align="right"> -->
<div align="center">
	<%
		if (currPage != 1) {
	%><span class="paginationLink" style="font-size: 10px"><a href="javascript:void(0)" onclick="<%=fn%>(parseInt($('.<%=name%>Select:first').val())-1)">&lt;&lt;</a></span>
	<%
		} else {
	%><span class="paginationDisabledLink" style="font-size: 10px">&lt;&lt;</span><%
		}
	%>
	
	
	<select  class="<%=name%>Select" style="font-size: 10px" onchange="<%=fn%>($(this).val())">
		
		<%
					for (int i = 0; i < pageCount; i++) {

						String pageString ="";
						if(pdo==null || pdo.isShowInterval()){
							pageString = Util.getIntervalStringForPage(i + 1, recordPerPage, itemCount);
						}else{
							pageString = String.valueOf(i+1);
						}
				%>
		<option style="text-align:center;" <%=(i + 1 == currPage) ? "selected=\"selected\"" : ""%> value="<%=i + 1%>"><%=pageString%></option>
		<%
			}
		%>
	</select>
	<%
		if (currPage != pageCount) {
	%><span class="paginationLink" style="font-size: 10px"><a href="javascript:void(0)" onclick="<%=fn%>(parseInt($('.<%=name%>Select:first').val())+1)">&gt;&gt;</a></span>
	<%
		} else {
	%><span class="paginationDisabledLink" style="font-size: 10px">&gt;&gt;</span><%
		}
	%>
	
</div>
<%
end = System.currentTimeMillis();
logger.debug("pagination.jsp execution time: " + (end - start));
%>	
