<%--
    @version:$Id: newsEntry.jsp,v 1.1 2010/12/10 10:56:43 silencily Exp $
    @since:$Date: 2010/12/10 10:56:43 $
    @进入页面
--%>

<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ include file="/decorators/default.jspf" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK"/>
</head>
<body class="list_body">
<div class="main_title">
	<div>信息发布</div>
</div>
<div class="main_body">
    <div id="divId_panel"></div>
</div>
<script type="text/javascript">
    var arr = new Array();
    arr[0] = [" 列表 ", "", "<c:url value='/uf/news/NewsPublishAction.do?step=list&paginater.page=0'/>"];
    arr[1] = [" 详细 ", "", "<c:url value='/uf/news/NewsPublishAction.do?step=info'/>"];
    var panel = new Panel.panelObject("panel", arr, 0, "<c:out value = "${initParam['publicResourceServer']}/image/main/"/>");
    document.getElementById("divId_panel").innerHTML = panel.display();
    Global.displayOperaButton = function(){ };
    //panel.click(0);
</script> 
</body>
</html>