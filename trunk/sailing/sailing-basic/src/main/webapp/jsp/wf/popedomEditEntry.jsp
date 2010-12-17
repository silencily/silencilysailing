<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ include file="/decorators/default.jspf" %>

<html>
<body class="list_body">
<div class="main_title">
<div>流程中可编辑项设定</div>
</div>
<div class="main_body">
<div id="divId_panel"></div>
</div>
<c:if test="${not empty param.panelUrl}">
    <c:set var="panelUrl" value="${initParam['publicResourceServer']}${param.panelUrl}"/>
</c:if>
</body><entryPanel:show checkSecurity="false" panelList="列表#/wf/PopedomEditAction.do?step=list&paginater.pageSize=0&mainTableClassName=TblWfEditInfo
	,详细#/wf/PopedomEditAction.do?step=info&mainTableClassName=TblWfEditInfo"/>
</html>