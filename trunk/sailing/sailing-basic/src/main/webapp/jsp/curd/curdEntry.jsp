<%-- jsp1.2 --%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf"/>
<html>
<body class="list_body" >	
<div class="main_title"> <div> SERʾ������</div></div>
<div class="main_body">
	<div id="divId_panel">
		<!-- ��������ʾѡ�-->
	</div>	
</div>

<script type="text/javascript">
//����ѡ�
var arr = new Array();
arr[0] = [" �б� ", "", "<c:url value = '/curd/curdAction.do?step=list&paginater.page=0' />"];
arr[1] = [" ��ϸ ", "", "<c:url value = '/curd/curdAction.do?step=info' />"];
var panel = new Panel.panelObject("panel", arr, 0, "<c:out value = "${initParam['publicResourceServer']}/image/main/" />");
document.getElementById("divId_panel").innerHTML = panel.display();
</script>
</body>
</html>