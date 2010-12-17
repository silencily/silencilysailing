<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf"/>
<html>
<head>
<title>用户选择列表</title>
<script type="text/javascript">
loadok = function()
{
	document.getElementById("loading").style.display="none";
}
</script>
</head>
<body class="list_body">
<span id="loading" STYLE="position:absolute; right:1px; background-color:yellow;font-size:12pt; font-weight:800">页面加载中...</span>
<iframe id="windowFrame" onload="loadok()" src="${initParam['publicResourceServer']}/sm/userManageAction.do?step=selectInfo&paginater.page=14" frameborder="0" width="100%" height="100%"/>
</body>

</html>
