<%-- jsp1.2 --%>
<!-- liujinliang  2007-09-21 14:00-->
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ include file="/decorators/default.jspf" %>
<script type="text/javascript" src = "<c:out value='${initParam["publicResourceServer"]}'/>/public/scripts/xmlhttp.js"></script> 
<html>
<body class="list_body" >
<div class="main_title"> <div>ί�й���</div></div>
<div class="main_body">
	<div id="divId_panel">
		<!-- ��������ʾѡ�-->
	</div>	
</div>

<script type="text/javascript">
//����ѡ�
var arr = new Array();

arr[0] = [" ί���û� ", "", "<c:url value = '/sm/ConsignManagerAction.do?step=list&paginater.page=14'/>"];
arr[1] = [" ί�н�ɫ ", "", "<c:url value = '/sm/ConsignManagerAction.do?step=consignRoleList'/>"];
//arr[2] = [" ί��Ȩ�� ", "", "<c:url value = '/sm/ConsignManagerAction.do?step=consignPermissionList&paginater.page=14'/>"];
var panel = new Panel.panelObject("panel", arr, 0, "<c:out value = "${initParam['publicResourceServer']}/image/main/" />");
document.getElementById("divId_panel").innerHTML = panel.display();
Global.displayOperaButton = function(){ };
</script>
</body>
</html>