<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ include file="/decorators/default.jspf" %>
<body class="list_body">
<html>
<form id="f">
<input type="hidden" name="step" value="<c:out value = '${theForm.step}' />" />
<div class="update_subhead">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td>
			<span class="switch_open" onClick="StyleControl.switchDiv(this, $('divId_scrollLing'))"title="����������">ϵͳ�����б�</span>
		</td>
	</tr>
</table>
</div>
<div id="divId_scrollLing" class="list_scroll">
	<table border="0" cellpadding="0" cellspacing="0" class="Listing" id="listtable" onClick="TableSort.sortColumn(event)">
			<input type="hidden" name="checkall" onClick="CurrentPage.selectedAll()" title="�Ƿ�ȫѡ"/>
			<table:table name="viewBean" box="radio"/>
		</table>
<div class="list_bottom"><c:set var="paginater.forwardUrl" value="/sm/ParameterManageAction.do?step=list" scope="page" /> 
	<%@ include file="/decorators/paginater.jspf"%>
		
			<input type="button" class="opera_export" title="����Excel" onClick="Print.exportExcel($('divId_scrollLing'))" value=""/>
			<input name="" class="opera_display" type="button" title="������ʾ�б���Ŀ" value="������ʾ" onClick ="CurrentPage.settable('cmn_parameter')"/>
</div>
	</div>
</form>
</html>
<script type="text/javascript">
var msgInfo_ = new msgInfo();
if (CurrentPage == null) {
   		var CurrentPage = {};
	}

CurrentPage.remove = function(oid) {
	if (! confirm(msgInfo_.getMsgById('SM_I009_A_0'))) {
		return;
	}
	$('step').value = 'remove';
	FormUtils.post(document.forms[0], '<c:url value = "/sm/parameterManageAction.do?oid=" />' + oid );
}
   CurrentPage.create = function () {
	var len = listtable.rows.length;
	if(len<2){
		TableSort.setNoSelect();
		TableSort.dblClick();
	}else{
		$('oid').value = '';
		TableSort.setNoSelect();
		TableSort.dblClick();
	}
}
</script>


