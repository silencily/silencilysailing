<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf" />
<html>
<body>
<script language="javascript">
	if (CurrentPage == null) {
    	var CurrentPage = {};
	} 
	 CurrentPage.myquery= function () {
		if (document.getElementsByName("paginater.page") != null) {
	  	  document.getElementsByName("paginater.page").value = 0;
		}		 
		FormUtils.post(document.forms[0], '<c:url value='/sm/permissionAction.do?step=permissionRole'/>');
	}	   
   TableSort.dblClick = function()
   {
   	   return false;
   }
</script>
<form name="f">
<input type="hidden" name="oid" value="<c:out value='${theForm.oid}'/>"/>
<input type="hidden" name="parentCode" value="<c:out value = "${theForm.parentCode}"/>"/>
	<div class="update_subhead">
		<span class="switch_open" onClick="StyleControl.switchDiv(this, $('currentPermission'))" title="">当前权限信息</span>
	</div>		
	<div id="currentPermission" style="display">
		<table border="0" cellpadding="0" cellspacing="0" class="Detail" id="parenttable" style="display: ">
			<tr>
				<td  class="attribute" >权限名称</td>
				<td ><input type="text" name="tempDisplayname" value="<c:out value = '${theForm.bean.displayname}'/>" disabled="disabled"/></td>
				<td  class="attribute" >权限类型</td>
				<td ><input type="text" name="tempNodetype" value="<c:out value='${theForm.bean.nodeTypeName}'/>"  disabled="disabled"/></td>
			</tr>
		</table>
	</div>
	<div class="update_subhead" >
	    <span class="switch_close" onClick="StyleControl.switchDiv(this, $('supplierQuery'))" title="点击这里进行查询">查询条件</span>
	</div>
	<div id="supplierQuery" style="display:none">
		<table width="100%"  border="0" cellpadding="0" cellspacing="0" class="Detail" id="divId_flaw_search_common" style="display:">
		    <tr>
		        <td class="attribute">角色名称</td>
		        <td>
		        	<search:text name="tblCmnRole.name" oper="like" type="java.lang.String" valueDefault='${theForm}'/>
		        </td>
		        <td class="attribute">角色标识</td>
		        <td>
		       		<search:text name="tblCmnRole.roleCd" oper="like" type="java.lang.String" valueDefault='${theForm}'/>
		        </td>
		    </tr>
		  </table>
	    <div class="query_button">
	        <input type="button" class="opera_query" title="点击查询" onClick="CurrentPage.myquery();">
	    </div>
	</div>
	<div class="update_subhead">
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td>
					 <span class="switch_open" onClick="StyleControl.switchDiv(this,$('divId_scrollLing'))" title="点击收缩表格">权限相关角色列表</span>
				</td>
				
			</tr>
		</table>
	</div>
	<div id="divId_scrollLing" class="list_scroll">
		<table border="0" cellpadding="0" cellspacing="0" class="Listing" id="listtable" onClick="TableSort.sortColumn(event)">
	<thead>
				<tr>
					<td nowrap="nowrap">角色标识</td>
					<td nowrap="nowrap">显示名称</td>
					<td nowrap="nowrap">角色路径</td>
					<td nowrap="nowrap">系统角色</td>
					<td nowrap="nowrap">说明</td>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="item" items="${theForm.perRole}" varStatus="status">
					<tr>
						<td>
							<c:out  value="${item.tblCmnRole.roleCd}"/>&nbsp;
						</td>
						<td>
						<span onClick="javascript:definedWin.openListingUrl('rolePerUser', '<c:url value = '/sm/permissionAction.do?step=achieveRoleUser&paginater.page=0&roleId=${item.tblCmnRole.id}' />',null,true)" class="font_link"><c:out value="${item.tblCmnRole.name}" />&nbsp;
						</td>
						<td>
							<c:out  value="${item.tblCmnRole.roleRoute}"/>&nbsp;
						</td>	
						<td>
							<c:out  value="${item.tblCmnRole.systemRoleName}"/>&nbsp;
						</td>
						<td>
							<c:out  value="${item.tblCmnRole.note}"/>&nbsp;
						</td>
					</tr>
				</c:forEach>					
			</tbody>
		</table>
<div class="list_bottom"><c:set var="paginater.forwardUrl" value="/sm/permissionAction.do?step=permissionRole" scope="page" /> 
	<%@ include file="/decorators/paginater.jspf"%>
</div>
</div>
</form>
</body>
</html>
