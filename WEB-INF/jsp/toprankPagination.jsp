<%@page import="com.twitstreet.servlet.TopRankServlet"%>
<%@ page import="com.google.inject.Injector"%>
<%@ page import="com.google.inject.Guice"%>
<%@ page import="com.twitstreet.session.UserMgr"%>
<%@ page import="com.twitstreet.db.data.User"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.twitstreet.util.Util"%>

<%

int pageSelectorPerRow = 4;

Injector inj = (Injector) pageContext.getServletContext().getAttribute(Injector.class.getName());

UserMgr userMgr = inj.getInstance(UserMgr.class);

int pageCount= Integer.valueOf(request.getParameter("pageCount"));

int userCount = Integer.valueOf(request.getParameter("userCount"));
int currPage = Integer.valueOf(request.getParameter("currPage"));



if(pageCount<pageSelectorPerRow){
	
	pageSelectorPerRow = pageCount;
}


%>


<div class="tnt_pagination">
	<input class="toprank-current-page" type="hidden" value="<%=currPage%>" />


	<ul class="pureCssMenu pureCssMenum">

		<li class="pureCssMenui0"><a class="pureCssMenui0"
			href="javascript:void(0)"><span class="rankingDropDownItem"><%=Util.getIntervalStringForPage(currPage, userMgr.getRecordPerPage(), userCount) %></span>
			<![if gt IE 6]></a>
		<![endif]>
			<!--[if lte IE 6]><table><tr><td><![endif]-->
			<ul class="pureCssMenum">

				<!-- <span class="disabled_tnt_pagination">Prev</span> -->
				<%
					
					
					for(int i = 0; i < pageCount;i++) {	
					%>




				<%	
						
				       String intervalString = Util.getIntervalStringForPage(i+1, userMgr.getRecordPerPage(), userCount);
						
						
								
								
								%>
				<li class="pureCssMenui"><a class="pureCssMenui" href="#"
					onclick="retrievePage($(this),<%=i+1%>)"><span class="rankingDropDownItem"><%=intervalString%></span></a></li>

				<%	
					}
					
					%>
			</ul> <!--[if lte IE 6]></td></tr></table></a><![endif]--></li>
	</ul>

</div>

