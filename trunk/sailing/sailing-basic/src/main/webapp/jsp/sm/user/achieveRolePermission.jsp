<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf" />
<html>
<body>
<script type="text/javascript">
if (CurrentPage == null) {
   		var CurrentPage = {};
	}
</script>
<form action="" id="f">
<input type="hidden" name="roleId" value="<c:out value = '${theForm.roleId}' />" /> 
<div width="100%" class="update_subhead">
	<span class="switch_open" onClick="StyleControl.switchDiv(this, $('div_rolePermission'))">角色关联权限列表</span>
</div>
<div id="div_rolePermission" class="list_scroll">
<table border="0" cellpadding="0" cellspacing="0" class="Listing">
	<thead>
		<tr>
			<td nowrap="nowrap">权限路径</td>
			<td nowrap="nowrap">显示名称</td>
			<td nowrap="nowrap">类型</td>
			<td nowrap="nowrap">功能权限类型</td>
			<td nowrap="nowrap">数据编辑控制</td>
			<td nowrap="nowrap">列表数据显示范围</td>
			<td nowrap="nowrap">数据编辑范围</td>
			<td nowrap="nowrap">说明</td>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="item" items="${theForm.rolePermission}"
			varStatus="status">
			<tr>
				<td nowrap="nowrap" align='left'><c:out
					value="${item.tblCmnPermission.permissionRoute}" />&nbsp;</td> 
				<td nowrap="nowrap" align='left'><c:out
					value="${item.tblCmnPermission.displayname}" />&nbsp;</td>
				<td nowrap="nowrap" align='left'><c:out
					value="${item.tblCmnPermission.nodeTypeName}" />&nbsp;</td>
				<td nowrap="nowrap" align='left'><c:out
					value="${item.tblCmnPermission.urltypeCh}" />&nbsp;</td>
				<td nowrap="nowrap" align='left'><c:out value="${item.rwCtrlName}" />&nbsp;</td>
				<td nowrap="nowrap" align='left'><c:out value="${item.readAccessLevelName}" />&nbsp;
				</td>
				<td nowrap="nowrap" align='left'><c:out value="${item.writeAccessLevelName}" />&nbsp;
				</td>
				<td nowrap="nowrap" align='left'><c:out value="${item.tblCmnPermission.note}" />&nbsp;
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<div class="list_bottom">
<c:set var="paginater.forwardUrl" value="/sm/userManageAction.do?step=achieveRolePermission" scope="page" /> 
	<%@ include file="/decorators/paginater.jspf"%>
</div>
</div>
</form>
</body>
</html>
