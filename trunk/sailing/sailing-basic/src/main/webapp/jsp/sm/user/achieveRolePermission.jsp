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
	<span class="switch_open" onClick="StyleControl.switchDiv(this, $('div_rolePermission'))">��ɫ����Ȩ���б�</span>
</div>
<div id="div_rolePermission" class="list_scroll">
<table border="0" cellpadding="0" cellspacing="0" class="Listing">
	<thead>
		<tr>
			<td nowrap="nowrap">Ȩ��·��</td>
			<td nowrap="nowrap">��ʾ����</td>
			<td nowrap="nowrap">����</td>
			<td nowrap="nowrap">����Ȩ������</td>
			<td nowrap="nowrap">���ݱ༭����</td>
			<td nowrap="nowrap">�б�������ʾ��Χ</td>
			<td nowrap="nowrap">���ݱ༭��Χ</td>
			<td nowrap="nowrap">˵��</td>
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
