<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ include file="/decorators/default.jspf" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK"/>
</head>
<body class="list_body">
<div class="main_title">
	<div>��Ŀά��</div>
</div>
<div class="main_body">
	<div id="divId_panel"><!-- ��Ŀά��--></div>
</div>

<script type="text/javascript">
//����ѡ�
	var arr = new Array();
	arr[0] = ["�б�", "", "<c:url value = '/uf/desk/ColumnManageAction.do?step=list&paginater.page=0'/>"];
	arr[1] = ["��ϸ", "", "<c:url value = '/uf/desk/ColumnManageAction.do?step=info'/>"];

	var panel = new Panel.panelObject("panel", arr, 0, "<c:out value = "${initParam['publicResourceServer']}/image/main/" />");
	document.getElementById("divId_panel").innerHTML = panel.display();
	Global.displayOperaButton = function(){ };
</script>
</body>
</html>