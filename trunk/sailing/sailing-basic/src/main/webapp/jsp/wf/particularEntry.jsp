<%-- jsp1.2 --%>
<%-- 
<html>

<jsp:directive.include file="/decorators/default.jspf" />
<body class="list_body">
<div class="main_title">
	<div>������Ϣά��</div>
</div>
<div class="main_body">
	<div id="divId_panel"></div>
</div>

<script type="text/javascript">
//����ѡ�
	var arr = new Array();
	arr[0] = ["������Ϣ�б�", "", "/wf/particularlist.do?step=list&ooid=<c:out value="${ooid}"/>"];
	arr[1] = ["��ϸ��Ϣ", "", "/wf/particularlist.do?step=edit&idd=<c:out value="${row.id}"/>&ooid=<c:out value="${ooid}"/>"];

	var panel = new Panel.panelObject("panel", arr, 0, "<c:out value = "${initParam['publicResourceServer']}/image/main/" />");
	document.getElementById("divId_panel").innerHTML = panel.display();
	Global.displayOperaButton = function(){ };
</script>
</body>
</html>
--%>
<%-- jsp1.2 --%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ include file="/decorators/default.jspf" %>


<html>
<body class="list_body">
  <div class="main_title">
     <div>������Ϣά��</div>
  </div>
  <div class="main_body">
    <div id="divId_panel"></div>
  </div>
</body>
 
<entryPanel:show checkSecurity="false" 
	panelList="�б�#/wf/particularlist.do?step=list&paginater.pageSize=0,
	            ��ϸ#/wf/particularlist.do?"
/>

</html>