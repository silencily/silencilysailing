<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%--
    @version:$Id: scheduleEntry.jsp,v 1.1 2010/12/10 10:56:18 silencily Exp $
    @since $Date: 2010/12/10 10:56:18 $
--%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK"/>
</head>
<jsp:directive.include file="/decorators/default.jspf" />
<body class="list_body">
<div class="main_title">
	<div>日程安排</div>
</div>
<div class="main_body">
	<div id="divId_panel"></div>
</div>
<script type="text/javascript">
	var arr = new Array();
	arr[0] = [" 日历 ", "", "<c:url value='/uf/schedule/ScheduleAction.do?step=calendar'/>"];
	arr[1] = [" 列表 ", "", "<c:url value='/uf/schedule/ScheduleAction.do?step=list&paginater.page=0'/>"];
	arr[2] = [" 详细 ", "", "<c:url value='/uf/schedule/ScheduleAction.do?step=info'/>"];
	var panel = new Panel.panelObject("panel", arr, 0, "<c:out value = "${initParam['publicResourceServer']}/image/main/"/>"
    <c:if test="${not empty param.panelUrl}">,"<c:url value="${param.panelUrl}" />"</c:if>);
	panel.listFrame = "frame_panel_1";
	document.getElementById("divId_panel").innerHTML = panel.display();
	Global.displayOperaButton = function(){ };
	panel.setNewUrl = function(url)
	{
		arr[1][2] = "<c:url value='/uf/schedule/ScheduleAction.do?step=list&paginater.page=0'/>&currentDay="+url;
	}
</script> 
</body>
</html>