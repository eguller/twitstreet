<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="true" session="false"%>
<%@page import="com.twitstreet.data.HomeData"%>
<%@taglib tagdir="/WEB-INF/tags" prefix="tag"%>
<%
	HomeData data = (HomeData)request.getAttribute("data");
%><!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>twitstreet.com</title>
	<link href="css/twitstreet.css" rel="stylesheet" type="text/css"></link>
</head>
<body>
	<div id="head-container">
		<div id="header">
			<h1>
				<img src="images/twitstreet.png"></img>
			</h1>
		</div>
	</div>
	
	<div id="navigation-container">
		<div id="navigation">
			<ul>
				<li><a href="#">Home</a></li>
				<li><a href="#">About</a></li>
				<li><a href="#">Services</a></li>
				<li><a href="#">Contact us</a></li>
			</ul>
			<div style="text-align: right"><span id="tcp"></span></div>
		</div>
	</div>
	
	<div id="content-container">
		<div id="content-container2">
			<div id="content-container3">
				<div id="content">
					<tag:dashboard data="<%=data.dashboard %>"></tag:dashboard>
					
					<div id="portfolio">
						<table width="100%">
							<thead>
								<tr>
									<td align="center" colspan="4" class="sectionHeader">Your Portfolio</td>
								</tr>
								<tr style="text-align: center">
									<td>Initial</td>
									<td>Current</td>
									<td>User</td>
									<td>Change</td>
								</tr>
							</thead>
							<tr>
								<td>2500</td>
								<td>3000</td>
								<td><a href="ebertchicago" class="user">ebertchicago</a></td>
								<td><span class="up">%20</span><img src="images/up.gif"
									alt="bull" /></td>
							</tr>
							<tr>
								<td>700</td>
								<td>770</td>
								<td><a href="ebertchicago" class="user">ebertchicago</a></td>
								<td><span class="up">%10</span><img src="images/up.gif"
									alt="bull" /></td>
							</tr>
							<tr>
								<td>200000</td>
								<td>40000</td>
								<td><a href="twit_street" class="user">twit_street</a></td>
								<td><span class="down">%80</span><img src="images/down.gif"
									alt="bear" /></td>
							</tr>
							<tr>
								<td>13000</td>
								<td>19500</td>
								<td><a href="ebertchicago" class="user">umraniye</a></td>
								<td><span class="up">%50</span><img src="images/up.gif"
									alt="bull" /></td>
							</tr>
						</table>
					</div>
			
				</div>
				<div id="aside">

					<div id="topusers">
						<div class="sectionHeader">
							<span>Top 50</span>
						</div>
						<ol>
							<li><a href="twit_street" class="user">twit_street</a> - 22300$</li>
						</ol>
					</div>

					<div id="latest-transactions">
						<div class="sectionHeader"><span>Current Transactions</span></div>
							<ol>
								<li><a href="twit_street" class="user">twit_street</a> <span
									class="up">bought</span> 230 follower of <a href="raptor" style="">
										raptor</a></li>
								<li><a href="ooktay" class="user">ooktay</a> <span class="down">sold</span>
									470 follower of <a href="eguller" style=""> eguller</a></li>
								<li><a href="ecagiral" class="user">ecagiral</a> <span class="up">bought</span>
									160 follower of <a href="mertz" style="">mertz</a></li>
							</ol>
						</div>
				    </div>
				</div>
			</div>
		</div>
	</div>
	<div id="footer-container">
		<div id="footer">footer</div>
	</div>

	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.5.1/jquery.min.js"></script>
	<script type="text/javascript" src="/js/home.js"></script>
	<script type="text/javascript" src="http://platform.twitter.com/anywhere.js?id=<%=data.taApiKey%>&v=1"></script>
</body>