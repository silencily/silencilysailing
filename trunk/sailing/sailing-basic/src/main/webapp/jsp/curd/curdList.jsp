<%--
    ����ʹ�� JSP1.2 specification, ���� Sun J2EE 5 ˵��, ������֤�������� server �±�ʶ��
    ������������쳣, ���� "jsp1.2"����, ʹ�� jsp1.1 ����
--%>
<html>
<%-- jsp1.2 --%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf"/>

<html>
<script type="text/javascript">
var CurrentPage = {};

CurrentPage.remove = function(oid) {
	if (! confirm("  ��ȷ��Ҫɾ���� ? "+oid)) {
		return;
	}
	$('step').value = 'remove';
	
	FormUtils.post(document.forms[0], '<c:url value = "/curd/curdAction.do?oid=" />' + oid);
}
function selectAll(b) {
       		if(f.chkall.checked==true){
         		for (var i = 0; i < f.length; i++) {
            		if (f[i].id == "oid") {
              			f[i].checked = b;
            		}
          		}
       		}else{
          		for (var i = 0; i < f.length; i++) {
            		if (f[i].id == "oid") {
              			f[i].checked = false;
            		}
          		}
        	}
		}
</script>

<body class="list_body">


<form id="f">
<jsp:directive.include file="/jsp/curd/curdSearch.jspf" />
<input type="hidden" name="step" value="<c:out value = "${theForm.step}" />" />		

<div class="list_explorer">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td >
			<span class="switch_open" onClick="StyleControl.switchDiv(this, $('tabId_listing'))">ser</span>
		</td>
	</tr>
</table>
</div>

<div id="divId_scrollLing" class="list_scroll">
	<table border="0" cellpadding="0" cellspacing="0" class="Listing"  id="tabId_listing" onClick="TableSort.sortColumn(event)">
		<table:table name="viewBean"/>
	</table>&nbsp;
	<div class="list_bottom">
	<c:set var = "paginater.forwardUrl" value = "/curd/curdAction.do" scope = "page" />
	<%@ include file = "/decorators/paginater.jspf" %>
	<input type="button" class="export_excel" onclick="Print.exportExcel($('tabId_listing'))" value="����excel"/>
</div>
</div>

</form>
</body>
</html>

