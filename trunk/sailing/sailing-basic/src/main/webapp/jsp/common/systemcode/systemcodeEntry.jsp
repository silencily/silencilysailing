<%-- jsp1.2 --%>
<jsp:directive.page contentType="text/html; charset=GBK" />
<jsp:directive.include file="/decorators/default.jspf" />

<body class="list_body" >	

<div class="main_body">
	<div id="divId_panel">
		<!-- ��������ʾѡ�-->
	</div>	
</div>

<script type="text/javascript">
//����ѡ�
var array = new Array();
array[array.length] = [" �����б� ", "", "<c:url value = '/common/systemcode.do?step=list&systemModuleName=${theForm.systemModuleName}&parentCode=${theForm.parentCode}' />"];
array[array.length] = [" ������ϸ ", "", "<c:url value = '/common/systemcode.do?step=info&systemModuleName=${theForm.systemModuleName}&parentCode=${theForm.parentCode}' />"];

var panel = new Panel.panelObject("panel", array, 0, "<c:out value = "${initParam['publicResourceServer']}/image/main/"/>");
document.getElementById("divId_panel").innerHTML = panel.display();
</script>
</body>
</html>