
<%@page import="java.util.ArrayList"%>
<%@ page import="com.twitstreet.localization.LocalizationUtil" %>

<%

LocalizationUtil lutil = LocalizationUtil.getInstance();
String lang = (String)request.getSession().getAttribute(LocalizationUtil.LANGUAGE);
%>

<div id="howtoplay" class="main-div">
	<div class="field-white" style="height:30px"> 
		<a style="line-height: 30px" href="javascript:void(0)" onclick="if($('#howtoplay-text').css('display') == 'none') $('#howtoplay-text').fadeTo('slow',1.0); else $('#howtoplay-text').hide(); "><strong><%=lutil.get("howtoplay.header", lang) %></strong></a>	
	</div>

	<div id="howtoplay-text" style="display: none">
		<table style="float:right;margin-bottom:20px; height:40px; width:50px">
			<tbody>
				<tr>
				
					<td style="vertical-align: middle">
					
					<a style="vertical-align: middle;" href="javascript:void(0)" onclick="$('#howtoplay-text').hide()"><%=lutil.get("shared.hide", lang) %>
			</a>
					</td>
				</tr>
				
			
			</tbody>
			
		</table>
<!-- 	<div style="float:center; margin-bottom:20px; height:70px"> -->
			
			
		
<!-- 		</div> -->

<ul style="line-height: 40px">

			<%
		ArrayList<String> paragraphList = lutil.getStartsWith("howtoplay.text", lang);
	
		for(String paragraph : paragraphList){
		%>		
			<li style="list-style: circle; list-style-position: inside; list-style-type: square;">
			
			<%=paragraph %>
			</li>
			
		<%		
		}
		%>
	
			</ul>
		<div style="text-align:center; margin-bottom:20px; height:70px" id="signup">
			
			<a href="/signin"><img  src="/images/twitter-big.png"></img>
			</a>
		
		</div>
	</div>
</div>
