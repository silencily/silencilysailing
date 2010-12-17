<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ include file="/decorators/default.jspf"%>

<script type="text/javascript">
var CurrentPage = {};

CurrentPage.remove= function(oid) {
	if (!confirm(' 删除节点同时将会删除所有子节点, 您确定要继续吗 ? ')) {
		return false;
	} 
	FormUtils.post(document.forms[0], '<c:url value = "/common/systemcode.do?step=delete&systemModuleName=${theForm.systemModuleName}&oid=" />' + oid);
}

</script>

<body class="list_body">
<form>
<input type="hidden" name="parentCode" value="<c:out value = "${theForm.parentCode}" />" />		
<input type="hidden" name="systemModuleName" value="<c:out value = "${theForm.systemModuleName}" />" />		

<%@ include file = "/jsp/common/systemcode/systemcodeParentSummary.jspf" %>

<div class="list_explorer">
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td>
				<span class="switch_open" onClick="StyleControl.switchDiv(this, $('systemcodeListTable'))">代码列表</span>
			</td>
		</tr>
	</table>
</div>
<div id="divId_scrollLing" class="list_scroll">
	<table border="0" cellpadding="0" cellspacing="0" class="Listing"  id="systemcodeListTable" onClick="TableSort.sortColumn(event)">
		<thead>
			<tr>
				<td>&nbsp;</td>
				<td>代码</td>		
				<td>名称</td>
				<td>排序号</td>						
				<td>描述</td>
				<td>&nbsp;</td>
			</tr>
		</thead>
		<tbody>
		<c:forEach var="item" items="${theForm.results}" varStatus="status">
			<tr>
				<td>
					<input type="hidden" name="oid" value="<c:out value = "${item.code}" />" />
					<c:out value = "${status.index + 1}" />
				</td>									
				<td><c:out value = "${item.code}" />&nbsp;</td>			
				<td><c:out value = "${item.name}" />&nbsp;</td>		
				<td><c:out value = "${item.sequenceNo}" />&nbsp;</td>	
				<td><c:out value = "${item.description}" />&nbsp;</td>				
				<td>
					<input type="button" class="list_delete" onclick="CurrentPage.remove('<c:out value = "${item.code}" />')"/>
				</td>	
			</tr>										
		</c:forEach>						
		</tbody>
	</table>&nbsp;
	<div class="list_bottom"></div>
</div>

</form>
</body>
<script language="javascript">
// 提示消息前刷新树
Validator.beforeSuccessMessage = function() {
	parent.parent.codeTree.Update(parent.parent.codeTree.SelectId);
}
</script>
</html>

