<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf"/>
<script type="text/javascript" src = "<c:out value="${initParam['publicResourceServer']}/public/scripts/xmlhttp.js"/>"></script> 
<html>
<body class="list_body" >	
<div class="main_title"> <div>当前角色：<c:out value="${theForm.searchTag}"/></div></div>
<div class="main_body">
	<div id="divId_panel">
	</div>	
</div>

<script type="text/javascript">
//定义选项卡
var arr = new Array();
arr[0] = [" 关联权限 ", "", "<c:url value = '/sm/userManageAction.do?step=achieveRolePermission&roleId=${theForm.roleId}&paginater.page=0'/>"];
arr[1] = [" 关联用户 ", "", "<c:url value = '/sm/userManageAction.do?step=achieveRoleUser&roleId=${theForm.roleId}&paginater.page=0'/>"];
var panel = new Panel.panelObject("panel", arr, 0, "<c:out value = "${initParam['publicResourceServer']}/image/main/" />");
document.getElementById("divId_panel").innerHTML = panel.display();
Global.displayOperaButton = function(){ };
</script>
</body>
</html>
