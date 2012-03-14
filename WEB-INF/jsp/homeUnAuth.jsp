<%@ taglib uri='/WEB-INF/tlds/template.tld' prefix='template'%>
<template:insert template='homeUnAuthTemplate.jsp'>
  <template:put name='header' content='header.jsp' />
  <template:put name='topbar' content='topbar.jsp' />
  <template:put name='stocks' content='stocks.jsp' />
  <template:put name='users' content='users.jsp'/>
  <template:put name='mainmenu' content='mainMenu.jsp'/>
  <template:put name='howtoplay' content='howtoplay.jsp' />
  <template:put name='topranks' content='topranks.jsp' />
  <template:put name='transactions' content='transactions.jsp' />
  <template:put name='recentTweets' content='recentTweets.jsp' />
  <template:put name='footer' content='footer.jsp' />
  <template:put name="scripts" content="scripts.jsp" />
</template:insert>