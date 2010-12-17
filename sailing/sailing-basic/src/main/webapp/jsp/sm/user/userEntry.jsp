<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf"/>

<html>
<script type="text/javascript" src = "<c:out value='${initParam["publicResourceServer"]}'/>/public/scripts/xmlhttp.js"></script> 
<body class="list_body" >	
<div class="main_title"> <div>用户管理</div></div>
<div class="main_body">
	<div id="divId_panel">
		<!-- 在这里显示选项卡-->
	</div>	
</div>

<script type="text/javascript">
//定义选项卡
var arr = new Array();
arr[0] = [" 列表 ", "", "<c:url value = '/sm/userManageAction.do?step=list&paginater.page=0'/>"];
arr[1] = [" 详细 ", "", "<c:url value = '/sm/userManageAction.do?step=info'/>"];
arr[2] = [" 数据权限 ", "", "<c:url value = '/sm/userManageAction.do?step=dataPermission&paginater.page=0'/>"];
arr[3] = [" 角色 ", "", "<c:url value = '/sm/userManageAction.do?step=queryRole&paginater.page=0'/>"];
arr[4] = [" 消息 ", "", "<c:url value = '/sm/userManageAction.do?step=msg'/>"];
var panel = new Panel.panelObject("panel", arr, 0, "<c:out value = "${initParam['publicResourceServer']}/image/main/" />");
document.getElementById("divId_panel").innerHTML = panel.display();
Global.displayOperaButton = function(){ };
</script>
</body>
</html>
