
<%@ page import="com.twitstreet.localization.LocalizationUtil" %>
	
			 <%


	LocalizationUtil lutil = LocalizationUtil.getInstance();
String lang = (String)request.getSession().getAttribute(LocalizationUtil.LANGUAGE);
	%>

	<table class="datatbl">
			<tr>
				<td>
					<div class="main-tabs">			 
						<a class="stocks-tab youarehere"
							onclick="showTabMain('.stocks-tab','#stocks-container');">
							<%=lutil.get("stocks", lang) %>
						</a>				
						<a class="users-tab" 
							onclick="showTabMain('.users-tab','#users-container');"> 
							<%=lutil.get("users", lang) %>
						</a>
					</div>
				</td>
				<td>
					<div class="h3-right-top">
						<div class="stocks-tab-right-content" style="display:none; text-align:right;float:right; text-size:20px; height:20px">		
							<a id="trendy-stocks-id" href="#suggestedstocks" onclick="reloadIfHashIsMyHref(this)" >
										<%=lutil.get("trendystocks.header", lang) %> 
							</a>
						</div>
						<div class="users-tab-right-content" style="display:none; text-align:right;float:right; text-size:20px; height:20px">		
							<a id="trendy-users-id" href="#topgrossingusers" onclick="reloadIfHashIsMyHref(this)" >
										<%=lutil.get("topgrossingusers.header", lang) %>
							</a>
						</div>
					</div>
				</td>
			</tr>
			
	</table>
		