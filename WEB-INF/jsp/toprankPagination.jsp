<%@page import="com.twitstreet.servlet.TopRankServlet"%>
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


<div class="tnt_pagination">
	<input class="toprank-current-page" type="hidden" value="<%=currPage%>" />

	<table class="datatbl">

		<!-- <span class="disabled_tnt_pagination">Prev</span> -->
		<%
			for (int i = 0; i < pageCount;) {
		%>

		<tr>
			<%
				for (int j = 0; j < pageSelectorPerRow; j++, i++) {
						if (i >= pageCount) {
			%>

			<td style="width:<%=100 / pageSelectorPerRow%>%;" />

			<%
				continue;
						}

						String textAlign = "";
						if (j == 0) {
							textAlign = "text-align: left;";
						} else if (j == pageSelectorPerRow - 1) {
							textAlign = "text-align: right;";
						} else {
							textAlign = "text-align: center;";

						}
			%>

			<td style="width:<%=100 / pageSelectorPerRow%>%;<%=textAlign%>">

				<%
					int maxRank = userMgr.getRecordPerPage();
							int start = i * maxRank + 1;
							int stop = (i + 1) * maxRank;

							if (stop > userCount) {

								stop = userCount;
							}
							String intervalString = "";

							if (start == stop) {

								intervalString = String.valueOf((start));
							} else {

								intervalString = (start) + "-" + (stop);
							}
							if (i + 1 == currPage) {
				%> <a class="active_tnt_link"
				onclick="retrievePage($(this),<%=i + 1%>)"><%=intervalString%></a> <%
 	} else {
 %> <a href="javascript:void(0)"
				onclick="retrievePage($(this),<%=i + 1%>)"><%=intervalString%></a> <%
 	}
 %>
			</td>
			<%
				}
			%>

		</tr>

		<%
			}
		%>

	</table>
</div>

