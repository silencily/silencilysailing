<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf"/>

<html>
<body class="list_body">
<form id="list_form1">
<jsp:directive.include file="popedomEditSearch.jspf"/>
<div class="update_subhead">
	<span title="点击伸缩节点" onClick="StyleControl.switchDiv(this, $('listInfo'))" class="switch_open">检索结果</span>
</div>
<div id="listInfo">
	<div class="list_scroll" id="divId_scrolling">
		<table onClick="TableSort.sortColumn(event)" id="listtable" class="Listing" cellspacing="0" cellpadding="0" border="0">
			<table:table name="viewBean" formName="${theForm}" box="radio"></table:table>
		</table>
	</div>
	<div class="list_bottom">
		<c:set var="paginater.forwardUrl" scope="page" value="/wf/PopedomEditAction.do?step=list&mainTableClassName=TblWfEditInfo"/>
		<%@ include file = "/decorators/paginater.jspf"%>
		<input value="" onClick="Print.exportExcel($('divId_scrolling'))" title="导出Excel文件" class="opera_export" type="button">
		<input onClick="CurrentPage.settable('wf_popedomEdit')" value="定制显示" class="opera_display" name="" type="button">
	</div>	  
</div>
</form>
</body>
<script type="text/javascript">
    var msgInfo_ = new msgInfo();
	if (CurrentPage == null) {
	   		var CurrentPage = {};
	}
	Global.setHeight();

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



CurrentPage.remove = function(oid) {
	if (! confirm("确认删除该记录? ")) {
		return;
	}

	FormUtils.post(document.forms[0], '<c:url value = "/wf/PopedomEditAction.do?step=delete&mainTableClassName=TblWfEditInfo&oid=" />' + oid );
}
 
</script>
<script type="text/javascript"></script>
</html>