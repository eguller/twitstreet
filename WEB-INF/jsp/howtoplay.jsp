
<%@page import="java.util.ArrayList"%>
<%@ page import="com.twitstreet.localization.LocalizationUtil" %>

<%

LocalizationUtil lutil = LocalizationUtil.getInstance();
String lang = (String)request.getSession().getAttribute(LocalizationUtil.LANGUAGE);
%>

<div id="howtoplay" class="main-div">
	<div class="h3"> 
		<a style="float:left" href="javascript:void(0)" onclick="if($('#howtoplay-text').css('display') == 'none') $('#howtoplay-text').fadeTo('slow',1.0); else $('#howtoplay-text').hide(); "><strong><%=lutil.get("howtoplay.header", lang) %></strong></a>
	
	
		
	</div>
	<div id="howtoplay-text" style="display: none">
		<table style="float:right;margin-bottom:20px; height:28px">
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
	<%
	if(lang.equalsIgnoreCase("en")){
	%>	
		<p>Twitstreet is a stock exchange game. But in this game we are
			buying and selling followers of twitter users.</p>
		<p>In this game you will have $1,000 cash initially. Every
			follower worth $1.For example Barack Obama has nearly 1 million
			followers on twitter. You can buy 1.000 follower of Barack Obama and
			this equals to 0.1% of his followers. Let say Obama's follower count
			increased to 2 million during his 2012 election's campaign. So you
			will have 2.000 follower of Obama and your portfolio worth $2,000.</p>
		<p>Point is, try to find trendy people on twitter. Who will be
			popular on twitter in near future ? </p>
		<p>Good luck!</p>
	<%
	}else if(lang.equalsIgnoreCase("tr")){
		%>
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
	 <%
	 }
	%>
		
		<div style="float:right; margin-bottom:20px; height:70px" id="signup">
			
			<a href="/signin"><img  src="/images/twitter-big.png"></img>
			</a>
		
		</div>
	</div>
</div>
