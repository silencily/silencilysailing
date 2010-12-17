<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ include file="/decorators/default.jspf"%>

<body class="list_body">
<form>
<input type="hidden" name="topOrganizationId" value="<c:out value = "${theForm.topOrganizationId}" />" />	
<input type="hidden" name="treeType" value="<c:out value = "${theForm.treeType}" />" />
<c:forEach var="item" items="${theForm.roles}" varStatus="status">
	<input type="hidden" name="roles" value="<c:out value = "${item}" />" />
</c:forEach>

<%@ include file = "/jsp/common/user/userListSearch.jspf" %>

<div class="list_explorer">
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td>
				<span class="switch_open" onClick="StyleControl.switchDiv(this, $('userListTable'))">用户列表</span>
			</td>
		</tr>
	</table>
</div>
<div id="divId_scrollLing" class="list_scroll">
	<table border="0" cellpadding="0" cellspacing="0" class="Listing"  id="userListTable" onClick="TableSort.sortColumn(event)">
		<col><col><col><col><col style="display:none">
		<thead>
			<tr>
				<td field="oid">
					<c:choose>
					<c:when test = "${theForm.treeType == '2'}">
						<input type="checkbox" name="checkAll" onclick="FormUtils.checkAll($('checkAll'), document.getElementsByName('oid'))"/>
					</c:when>
					<c:otherwise>
						&nbsp;
					</c:otherwise>
					</c:choose>
				</td>	
				<td field="name">中文名</td>
				<td field="code">用户帐户</td>
				<td field="departName">所在部门</td>
				<td field="departId"></td>
			</tr>
		</thead>
		<tbody>

		<c:set var="box" value="radio"/>
		<c:if test="${theForm.treeType == '2'}">
			<c:set var="box" value="checkbox"/>
		</c:if>
		<c:forEach var="item" items="${theForm.results}" varStatus="status">
			<tr>
				<td align="center">
					<input type="<c:out value='${box}'/>" name="oid" value="<c:out value = "${item.id}" />" />
				</td>
				<td><c:out value = "${item.chineseName}" />&nbsp;</td>				
				<td><c:out value = "${item.username}" />&nbsp;</td>			
				<td><c:out value = "${item.organization.name}" />&nbsp;</td>	
				<td><c:out value = "${item.organization.id}" />&nbsp;</td>
			</tr>										
		</c:forEach>						
		</tbody>
	</table>
</div>

<div class="list_bottom">
	<c:set var = "paginater.forwardUrl" value = "/common/ui/user.do?step=list" scope = "page" />
	<%@ include file = "/decorators/paginater.jspf" %>
	<input type="button" class="export_excel" onclick="Print.exportExcel($('userListTable'))" />
</div>

</form>
</body>
<script language="javascript">

if (CurrentPage == null) {
    var CurrentPage = {};
}    

var listObject = new Object();
//definedWin激活该方法
CurrentPage.onLoadSelect = function(){
	//将列表对象化
	listObject = new ListUtil.Listing('listObject', 'userListTable');
	listObject.init();
			
	//重载definedWin中实现
	//选择
	top.definedWin.selectListingExtend[top.definedWin.modalNum] = function(){
		var arr = listObject.selectOne();
		<c:if test="${theForm.treeType == '2'}">
			arr = listObject.selectMult();
		</c:if>
		if(arr.length == 0){
			alert('请选择一行数据！');
			return;
		}
		top.definedWin.returnOpenTreeValue(arr);
	}
}
CurrentPage.onLoadSelect();
    

</script>
</html>

