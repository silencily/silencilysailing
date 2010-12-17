<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf"/>
<html>
<body>
<script type="text/javascript">
if (CurrentPage == null) {
   		var CurrentPage = {};
	}
 CurrentPage.myquery= function () {
	if (document.getElementsByName("paginater.page") != null) {
 	  document.getElementsByName("paginater.page").value = 0;
	}		 
FormUtils.post(document.forms[0], '<c:url value='/sm/permissionAction.do?step=achieveRoleUser'/>');
	}	
</script>
<form action="" id="f">
<div class="main_title">
<div>当前角色：<c:out value="${theForm.roleName}"/></div>
</div>
<input type="hidden" name="roleId" value="<c:out value='${theForm.roleId}'/>"/>
	<div class="update_subhead" >
	    <span class="switch_close" onClick="StyleControl.switchDiv(this, $('supplierQuery'))" title="点击这里进行查询">查询条件</span>
	</div>
	<div id="supplierQuery" style="display:none">
		<table width="100%"  border="0" cellpadding="0" cellspacing="0" class="Detail" id="divId_flaw_search_common" style="display:">
		    <tr>
			<td class="attribute">员工编号</td>
	        <td>
	           <search:text name="tblCmnUser.empCd" oper="like" type="java.lang.String" valueDefault='${theForm}'/>
	        </td>
			<td class="attribute">员工姓名</td>
	        <td>
	        <search:text name="tblCmnUser.empName" oper="like" type="java.lang.String" valueDefault='${theForm}'/>
	        </td>
	    </tr>
	    <tr>
	        <td class="attribute">激活状态</td>
	        <td>
	        	<input name="conditions(tblCmnUser.status).name" type="hidden" value="tblCmnUser.status"/>
	            <input name="conditions(tblCmnUser.status).operator" type="hidden" value="="/>
	            <input name="conditions(tblCmnUser.status).createAlias" type="hidden" value="true"/>
	            <input name="conditions(tblCmnUser.status).type" type="hidden" value="java.lang.String"/>
                <ec:composite value='${theForm.conditions["tblCmnUser.status"].value}'  valueName = "conditions(tblCmnUser.status).value" textName = "temp.conditions(status).value" source = "${theForm.statusComboList}" />
	        </td>
	        <td class="attribute">部门名称</td>
	        <td>
	            <search:text name="tblCmnUser.tblCmnDept.deptName" oper="like" type="java.lang.String" valueDefault='${theForm}'/>
	        </td>
		    </tr>
		  </table>
	    <div class="query_button">
	        <input type="button" class="opera_query" title="点击查询" onClick="CurrentPage.myquery();">
	    </div>
	</div>
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
		<c:forEach var="item" items="${theForm.list}" varStatus="status">
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
<c:set var="paginater.forwardUrl" value="/sm/permissionAction.do?step=achieveRoleUser" scope="page" /> 
	<%@ include file="/decorators/paginater.jspf"%>
</div>
</div>
</form>
</body>
</html>

