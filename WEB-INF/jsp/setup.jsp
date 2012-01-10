<%@ taglib uri='/WEB-INF/tlds/template.tld' prefix='template'%>
<template:insert template='setupTemplate.jsp'>
  <template:put name='title' content='twitstreet.com' direct='true'/>
  <template:put name='header' content='header.jsp' />
  <template:put name='topbar' content='topbar.jsp' />
  <template:put name='setupConfig' content='setupConfig.jsp' />
  <template:put name='footer' content='footer.jsp' />
</template:insert>