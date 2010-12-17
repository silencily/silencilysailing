<%-- jsp1.2 --%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ include file="/decorators/default.jspf" %>

<html>
<body class="list_body">
  <div class="main_title">
     <div>工作流信息维护</div>
  </div>
  <div class="main_body">
    <div id="divId_panel"></div>
  </div>
</body>
 
<entryPanel:show checkSecurity="false" 
	panelList="列表#/wf/operationlist.do?step=list&paginater.page=0,
	            详细#/wf/operationlist.do?step=info&paginater.page=0"
/>

</html>

