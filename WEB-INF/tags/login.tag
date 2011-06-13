<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ attribute name="data" required="true" type="com.twitstreet.data.LoginData" %>
<div class="login_Loggedin" style="<%=data.isLoggedIn?"":"display:none;"%>">
	<span class="login_userName"><%=data.userName%></span> logged in.
</div>
<div class="login_Notloggedin" style="<%=!data.isLoggedIn?"":"display:none;"%>">
    <input class="login_Login" type="button" value="Sign in with twitter"/>
</div>
