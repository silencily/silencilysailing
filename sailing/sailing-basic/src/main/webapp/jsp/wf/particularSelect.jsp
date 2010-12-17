<%-- jsp1.2 --%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ include file="/decorators/default.jspf" %>

<script type="text/javascript">
var CurrentPage = {};
	CurrentPage.query = function() {
		if (document.getElementsByName("paginater.page") != null) {
		    document.getElementsByName("paginater.page").value = 0;
		}
		var nameoper = document.forms[0].nameoper.value;
		FormUtils.post(document.forms[0], '<c:url value="/wf/NapeEditAction.do?step=select&nameoper="/>' + nameoper);
	}
	
</script>
<%-- 可编辑项选择页面 --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK"/>
</head>
<body class="list_body">
<form name="form">
<input type="hidden" name="nameoper" value="<c:out value='${nameoper}'/>" />
<input type="hidden" name="step" value="<c:out value = "${theForm.step}" />" />	
<jsp:directive.include file="/jsp/wf/particularSearch.jspf" />
<div class="update_subhead">
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td><span class="switch_open" onClick="StyleControl.switchDiv(this,$(wzlb))" title="点击收缩表格">可编辑项列表</span></td>
		</tr>
	</table>
</div>
<div id="wzlb">
<div id="divId_scrollLing" class="list_scroll">
	<table border="0" cellpadding="0" cellspacing="0" class="Listing" id="tgtTable" onClick="TableSort.sortColumn(event)">
		<thead>
		<tr>
			<td field=id nowrap="nowrap"  align="center"><input name="chkall" type="checkbox" value="" id="detailIdsForPrintAll" onClick="FormUtils.checkAll(this, document.getElementsByName('detailIds'))" title="是否全选" /></td>
			<td field=tblWfOperName>业务名称</td>
			<td field=tblWfName>流程名称</td>
			<td field=fieldNameEdit>表单项</td>
			<td field=fieldCodeEdit>表单KEY</td>
		</tr>
		</thead>
		<tbody>
		<c:forEach var="item" items="${theForm.list}" varStatus="status">
			<tr>
				<td  align="center"><input type="checkbox" id="oid" name="detailIds"  value="<c:out value = "${item.id}" />"
				onclick="FormUtils.check($('detailIdsForPrintAll'),document.getElementsByName('detailIds'))"  />&nbsp;</td>
				<td><c:out value = "${item.tblWfOperName}" />&nbsp;</td>
				<td><c:out value = "${item.tblWfName}" />&nbsp;</td>
				<td><c:out value = "${item.fieldNameEdit}" />&nbsp;</td>
				<td><c:out value = "${item.fieldCodeEdit}" />&nbsp;</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</div>
<div class="list_bottom"><c:set var="paginater.forwardUrl"value="/wf/NapeEditAction.do?step=select" scope="page" /> 
	<%@ include file="/decorators/paginater.jspf"%>
</div>
</div>
<script type="text/javascript">	
	if (CurrentPage == null) {
	    var CurrentPage = {};
	}    
	
	var listObject = new Object();
	//definedWin激活该方法
	CurrentPage.onLoadSelect = function(){
		//将列表对象化
		listObject = new ListUtil.Listing('listObject', 'tgtTable');
		listObject.init();
		//重载definedWin中实现
		//选择
		top.definedWin.selectListing = function(inum) {
			listObject.selectWindow(2);
		}
	
		//关闭
		top.definedWin.closeListing = function(inum) {
			listObject.selectWindow();
		}
	}
	CurrentPage.onLoadSelect();

	CurrentPage.validation = function () {
		return Validator.Validate(document.forms[0], 4);
	}
</script>
</form>
</body>
</html>

