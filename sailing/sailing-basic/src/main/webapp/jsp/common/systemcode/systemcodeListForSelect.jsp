<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ include file="/decorators/default.jspf"%>

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
				<td field="oid">&nbsp;</td>
				<td field="code">代码</td>		
				<td field="name">名称</td>
				<td field="sequenceNo">排序号</td>						
				<td field="description">描述</td>
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
			</tr>										
		</c:forEach>						
		</tbody>
	</table>
</div>
<div class="list_bottom"></div>

</form>
</body>
<script language="javascript">

if (CurrentPage == null) {
    var CurrentPage = {};
}    

var listObject = new Object();
//definedWin激活该方法
CurrentPage.onLoadSelect = function(){
	//传入自定义参数为对应属性：fieldName fieldCode
	if(parent.Global.URLParams["fieldName"]){
		var thead = $("systemcodeListTable").rows[0];
		thead.cells[1].field = parent.Global.URLParams["fieldCode"];
		thead.cells[2].field = parent.Global.URLParams["fieldName"];
	}
	//将列表对象化
	listObject = new ListUtil.Listing('listObject', 'systemcodeListTable');
	listObject.init();
	//重载definedWin中实现
	//选择
	top.definedWin.selectListingExtend[top.definedWin.modalNum] = function(){
		listObject.selectWindow(1);
	}
}
CurrentPage.onLoadSelect();
    

</script>
</html>

