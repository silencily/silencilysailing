<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<!DOCTYPE   HTML   PUBLIC   "-//W3C//DTD   HTML   4.0   Transitional//EN " >
<%@ include file="/decorators/default.jspf" %>
<html>
<head>
<title>用户选择列表</title>
<script type="text/javascript">
loadok = function()
{
	document.getElementById("loading").style.display="none";
	document.getElementById("windowFrame").style.height=info_content.document.body.scrollHeight+1+"px";

}
</script>
</head>
<body class="list_body">
<span id="loading" STYLE="position:absolute; right:1px; background-color:yellow;font-size:12pt; font-weight:800">页面加载中...</span>
<iframe id="windowFrame" name="info_content" onload="loadok()" src="<c:url value="/wf/CommonAction.do?step=querySigners&paginater.page=0&paginater.pageSize=14&className=${param.className}&oid=${param.oid}&stepId=${param.stepId}&workflowName=${param.workflowName}"/>" frameborder="0" width="100%" height="100%"/>
</body>
</html>