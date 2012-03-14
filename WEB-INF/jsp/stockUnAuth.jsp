<%@ taglib uri='/WEB-INF/tlds/template.tld' prefix='template'%>
<template:insert template='stockUnAuthTemplate.jsp'>
  <template:put name='header' content='header.jsp' />
  <template:put name='topbar' content='topbar.jsp' />
  <template:put name='stockdetails' content='stockDetails.jsp' />
  <template:put name='topranks' content='topranks.jsp' />
  <template:put name='transactions' content='transactions.jsp' />
  <template:put name='recentTweets' content='recentTweets.jsp' />
  <template:put name='footer' content='footer.jsp' />
</template:insert>