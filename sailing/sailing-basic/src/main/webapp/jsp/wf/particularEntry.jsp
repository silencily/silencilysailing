<%-- jsp1.2 --%>
<%-- 
<html>

<jsp:directive.include file="/decorators/default.jspf" />
<body class="list_body">
<div class="main_title">
	<div>步骤信息维护</div>
</div>
<div class="main_body">
	<div id="divId_panel"></div>
</div>

<script type="text/javascript">
//定义选项卡
	var arr = new Array();
	arr[0] = ["步骤信息列表", "", "/wf/particularlist.do?step=list&ooid=<c:out value="${ooid}"/>"];
	arr[1] = ["详细信息", "", "/wf/particularlist.do?step=edit&idd=<c:out value="${row.id}"/>&ooid=<c:out value="${ooid}"/>"];

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
     <div>步骤信息维护</div>
  </div>
  <div class="main_body">
    <div id="divId_panel"></div>
  </div>
</body>
 
<entryPanel:show checkSecurity="false" 
	panelList="列表#/wf/particularlist.do?step=list&paginater.pageSize=0,
	            详细#/wf/particularlist.do?"
/>

</html>