<%@ page import="java.util.ArrayList"%>
<%@ page import="com.twitstreet.localization.LocalizationUtil" %>

<%
LocalizationUtil lutil = LocalizationUtil.getInstance();
String lang = (String)request.getSession().getAttribute(LocalizationUtil.LANGUAGE);
%>
<div id="howtoplay" class="main-div">
	<div class="field-white" style="height:50px; cursor: pointer;"  onclick="if($('#howtoplay-text').css('display') == 'none') $('#howtoplay-text').fadeTo('slow',1.0); else $('#howtoplay-text').hide(); "> 
		<a style="line-height: 50px; font-size: 20px" href="javascript:void(0)"><%=lutil.get("howtoplay.header", lang) %></a>	
	</div>
	<div id="howtoplay-text" style="display: none; margin-left:20px">
		<div style="clear: both; text-align: right; margin:5px;padding:5px;">
						<a style="vertical-align: middle;" href="javascript:void(0)" onclick="$('#howtoplay-text').hide()"><%=lutil.get("shared.hide", lang) %>
						</a>
		</div>
		<ul style="line-height: 40px; list-style: circle; list-style-type: square;">

		<%
	ArrayList<String> paragraphList = lutil.getStartsWith("howtoplay.text", lang);

	for(String paragraph : paragraphList){
	%>		
		<li>
		
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
