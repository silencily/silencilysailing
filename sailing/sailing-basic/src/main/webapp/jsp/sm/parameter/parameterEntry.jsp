<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ include file="/decorators/default.jspf" %>
<script type="text/javascript" src = "<c:out value='${initParam["publicResourceServer"]}'/>/public/scripts/xmlhttp.js"></script> 
<html>
<body class="list_body" >	
<div class="main_title"> <div>系统参数管理</div></div>
<div class="main_body">
	<div id="divId_panel">
		<!-- 在这里显示选项卡-->
	</div>	
</div>

<script type="text/javascript">
//定义选项卡
var arr = new Array();
arr[0] = [" 列表 ", "", "<c:url value = '/sm/parameterManageAction.do?step=list&paginater.page=14'/>"];
arr[1] = [" 详细 ", "", "<c:url value = '/sm/parameterManageAction.do?step=info'/>"];
var panel = new Panel.panelObject("panel", arr, 0, "<c:out value = "${initParam['publicResourceServer']}/image/main/" />");
document.getElementById("divId_panel").innerHTML = panel.display();
Global.displayOperaButton = function(){ };
</script>
</body>
</html>
