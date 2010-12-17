<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf"/>
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
	<span class="switch_open" onClick="StyleControl.switchDiv(this, $('div_roleUser'))">角色关联用户列表</span>
</div>
<div id="div_roleUser" class="list_scroll">
	<table border="0" cellpadding="0" cellspacing="0" class="Listing">
	<thead>
		<tr>
			<td nowrap="nowrap">员工编号</td>
			<td nowrap="nowrap">员工姓名</td>
			<td nowrap="nowrap">激活状态</td>
			<td nowrap="nowrap">部门名称</td>
		</tr> 
	</thead>
	<tbody>
		<c:forEach var="item" items="${theForm.roleUser}" varStatus="status">
			<tr>
				<td nowrap="nowrap" align='left'>
					<c:out  value="${item.tblCmnUser.empCd}"/>&nbsp;
				</td>	
				<td nowrap="nowrap" align='left'>
					<c:out  value="${item.tblCmnUser.empName}"/>&nbsp;
				</td>
				<td nowrap="nowrap" align='left'>
					<c:out  value="${item.tblCmnUser.statusName}"/>&nbsp;
				</td>  	
				<td nowrap="nowrap" align='left'>
					<c:out  value="${item.tblCmnUser.tblCmnDept.deptName}"/>&nbsp;
				</td>
			</tr>
		</c:forEach>					
	</tbody>
</table>
<div class="list_bottom">
<c:set var="paginater.forwardUrl" value="/sm/userManageAction.do?step=achieveRoleUser" scope="page" /> 
	<%@ include file="/decorators/paginater.jspf"%>
</div>
</div>
</form>
</body>
</html>

