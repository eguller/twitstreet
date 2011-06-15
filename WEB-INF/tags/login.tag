<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ attribute name="data" required="true" type="com.twitstreet.data.LoginData" %>
<div class="Loggedin" style="<%=data.isLoggedIn?"":"display:none;"%>">
	<span class="UserName"><%=data.userName%></span> logged in.
</div>
<div class="Notloggedin" style="<%=!data.isLoggedIn?"":"display:none;"%>">
    <input class="BtnLogin" type="button" value="Sign in with twitter"/>
</div>
