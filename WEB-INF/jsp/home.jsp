<%@ taglib uri='/WEB-INF/tlds/template.tld' prefix='template'%>
<template:insert template='homeTemplate.jsp'>
  <template:put name='title' content='twitstreet.com' direct='true'/>
  <template:put name='header' content='header.html' />
  <template:put name='topbar' content='topbar.jsp' />
  <template:put name="navigation" content="navigation.jsp" />
  <template:put name='dashboard' content='dashboard.jsp' />
  <template:put name='howtoplay' content='howtoplay.jsp' />
  <template:put name='signup' content='signup.jsp' />
  <template:put name='topranks' content='topranks.jsp' />
  <template:put name='latesttransactions' content='latesttransactions.jsp' />
  <template:put name='footer' content='footer.jsp' />
</template:insert>