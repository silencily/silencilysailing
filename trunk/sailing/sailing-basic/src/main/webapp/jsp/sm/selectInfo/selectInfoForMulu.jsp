<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf" />
<html>
	<body>
		<script language="javascript">
			if (CurrentPage == null) {
    	var CurrentPage = {};
		} 
CurrentPage.query= function () {
	 if (document.getElementsByName("paginater.page") != null) {
	   	 document.getElementsByName("paginater.page").value = 0;
	 }
	FormUtils.post(document.forms[0], '<c:url value='/sm/RoleAction.do?step=roleSearch&selectInfo=selectInfo'/>'); 
}
</script>
<form name="f">
<input type="hidden" name="parentCode" id="parentCode" value="<c:out value = "${theForm.parentCode}"/>"/>
<input type="hidden" name="step" value="<c:out value = '${theForm.step}' />" />
	<div class="update_subhead" >
	    <span class="switch_close" onClick="StyleControl.switchDiv(this, $('supplierQuery'))" title="点击这里进行查询">查询条件</span>
	</div>
	<div id="supplierQuery" style="display:none">
		<table width="100%"  border="0" cellpadding="0" cellspacing="0" class="Detail" id="divId_flaw_search_common" style="display:">
		    <tr>
		        <td class="attribute">显示名称</td>
		        <td>
		        	<search:text name="display_Name" oper="like" type="java.lang.String" valueDefault='${theForm}'/>
		        </td>
		        <td class="attribute">角色标识</td>
		        <td>
		       		<search:text name="role_Cd" oper="like" type="java.lang.String" valueDefault='${theForm}'/>
		        </td>
		    </tr>
		    <tr>
		    	<td class="attribute" >角色所在目录</td>
			    <td>
		        	<input name="searchFatherName" value="<c:out value='${theForm.searchFatherName}'/>"/>
			    </td>
				<td  class="attribute" >系统角色</td>
				<td>
		        	<input name="conditions(systemRole).name" type="hidden" value="system_Role"/>
		            <input name="conditions(systemRole).operator" type="hidden" value="="/>
		            <input name="conditions(systemRole).createAlias" type="hidden" value="false"/>
		            <input name="conditions(systemRole).type" type="hidden" value="java.lang.String"/>
	                <ec:composite value='${theForm.conditions["systemRole"].value}'  valueName = "conditions(systemRole).value" textName = "temp.conditions(systemPermission).value" source = "${theForm.systemRoleComboList}"/>
				</td>
		    </tr>
		  </table>
	    <div class="query_button">
	        <input type="button" value="" name="" class="opera_query" title="点击查询" onClick="CurrentPage.query();">
	    </div>
	</div>
	<div class="update_subhead">
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td>
					 <span class="switch_open" onClick="StyleControl.switchDiv(this,$('divId_requQues'))" title="点击收缩表格">检索结果</span>
				</td>
			</tr>
		</table>
	</div>
	<div id="divId_requQues" class="list_scroll">
		<table border="0" cellpadding="0" cellspacing="0" class="Listing" id="listtable" onClick="TableSort.sortColumn(event)">
			<thead>
				<tr>
					<td nowrap="nowrap">角色标识</td>
					<td nowrap="nowrap">显示名称</td>
					<td nowrap="nowrap">角色路径</td>
					<td nowrap="nowrap">系统角色</td>
					<td nowrap="nowrap">说明</td>
					<td nowrap="nowrap">操作</td>	
				</tr>
			</thead>
			<tbody>
				<c:forEach items = "${theForm.rolList}" var = "item" varStatus = "status" >
					<tr>
						<td>
							<c:out  value="${item.roleCd}"/>&nbsp;
						</td>
						<td>
							<span onClick="javascript:definedWin.openListingUrl('rolePerUser', '<c:url value = '/sm/userManageAction.do?step=rolePerUserEntry&selectPage=selectPage&rowId=${item.id}' />',null,true)" class="font_link"><c:out  value="${item.name}"/>&nbsp;
						</td>
						<td>
							<c:out  value="${item.roleRoute}"/>&nbsp;
						</td>	
						<td>
							<c:out  value="${item.systemRoleName}"/>&nbsp;
						</td>
						<td>
							<c:out  value="${item.note}"/>&nbsp;
						</td>
						<td align="center">
							<input type="button" class="list_delete" onClick="CurrentPage.del(this)" id="<c:out value="${item.id}"/>;role" style="display:" name="delBtn" title="删除"/>
						</td>
					</tr>
				</c:forEach>					
			</tbody>
		</table>
<div class="list_bottom">
<c:choose>
<c:when test="${param.step=='list'}">
	<c:set var="paginater.forwardUrl" value="/sm/RoleAction.do?step=list" scope="page" /> 
</c:when>
<c:otherwise>
	<c:set var="paginater.forwardUrl" value="/sm/RoleAction.do?step=roleSearch&selectInfo=selectInfo" scope="page" /> 
</c:otherwise>
</c:choose>
	<%@ include file="/decorators/paginater.jspf"%>
</div>
	</div>
</form>
</body>
</html>
