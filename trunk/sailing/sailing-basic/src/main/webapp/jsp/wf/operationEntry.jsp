<%-- jsp1.2 --%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ include file="/decorators/default.jspf" %>

<html>
<body class="list_body">
  <div class="main_title">
     <div>��������Ϣά��</div>
  </div>
  <div class="main_body">
    <div id="divId_panel"></div>
  </div>
</body>
 
<entryPanel:show checkSecurity="false" 
	panelList="�б�#/wf/operationlist.do?step=list&paginater.page=0,
	            ��ϸ#/wf/operationlist.do?step=info&paginater.page=0"
/>

</html>

