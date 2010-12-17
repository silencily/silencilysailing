<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf" />
<html>
	<body>
<script language="javascript">
	if (CurrentPage == null) {
    	var CurrentPage = {};
		} 
	CurrentPage.query= function () {
	 var strReVal = document.getElementsByName('menuRole');
		if (document.getElementsByName("paginater.page") != null) {
	  	  document.getElementsByName("paginater.page").value = 0;
		}
		FormUtils.post(document.forms[0], '<c:url value='/sm/RoleAction.do?step=selectRoleSearch'/>');
	}
</script>
<form name="f">
<input type="hidden" name="parentCode" id="parentCode" value="<c:out value = "${theForm.parentCode}"/>"/>
<input type="hidden" name="step" value="<c:out value = '${theForm.step}' />" />
	<div class="update_subhead" >
	    <span class="switch_close" onClick="StyleControl.switchDiv(this, $('supplierQuery'))" title="点击伸缩节点">查询条件</span>
	</div>
	<div id="supplierQuery" style="display:none">
		<table width="100%"  border="0" cellpadding="0" cellspacing="0" class="Detail" id="divId_flaw_search_common" style="display:">
		    <tr>
		        <td class="attribute">角色标识</td>
		        <td>
		        	<search:text name="role_Cd" oper="like" type="java.lang.String" valueDefault='${theForm}'/>
		        </td>
		        <td class="attribute">显示名称</td>
		        <td>
		            <search:text name="display_Name" oper="like" type="java.lang.String" valueDefault='${theForm}'/>
		        </td>
		    </tr>
		    <tr>
				<td class="attribute">角色所在目录</td>
		        <td>
		       		<input name="searchFatherName" value="<c:out value='${theForm.searchFatherName}'/>"/>
		        </td>
		    
				<td  class="attribute" >系统角色</td>
				<td>
		        	<input name="conditions(systemRole).name" type="hidden" value="system_Role"/>
		            <input name="conditions(systemRole).operator" type="hidden" value="="/>
		            <input name="conditions(systemRole).createAlias" type="hidden" value="false"/>
		            <input name="conditions(systemRole).type" type="hidden" value="java.lang.String"/>
	                <ec:composite value='${theForm.conditions["systemRole"].value}'  valueName = "conditions(systemRole).value" textName = "temp.conditions(systemPermission).value" source = "${theForm.systemRoleComboList}" />
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
					 <span class="switch_open" onClick="StyleControl.switchDiv(this,$('divId_scrollLing'))" title="点击伸缩节点">角色列表</span>
				</td>
			</tr>
		</table>
	</div>
<div id="divId_scrollLing" class="list_scroll">
	<table border="0" cellpadding="0" cellspacing="0" class="Listing" id="roleInfoTable" onClick="TableSort.sortColumn(event)">
		<thead>
		<tr>
			<td field=id>&nbsp;</td>
			<td field=roleCd>角色标识</td>
			<td field=roleName>角色名称</td>
			<td field=roleRoute>角色路径</td>
			<td field=systemRoleName>系统角色</td>
			<td field=note>说明</td>
		</tr>
		</thead>
		<tbody>
		<c:forEach var="item" items="${theForm.rolList}" varStatus="status">
			<tr>
				<td align="center"><input type="radio" name="oid"  value="<c:out value = "${item.id}" />" /></td>
				<td><c:out value = "${item.roleCd}" />&nbsp;</td>
				<td><c:out value = "${item.name}" />&nbsp;</td>
				<td><c:out value = "${item.roleRoute}" />&nbsp;</td>
				<td><c:out value = "${item.systemRoleName}" />&nbsp;</td>
				<td><c:out value = "${item.note}" />&nbsp;</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
<div class="list_bottom">
<c:choose>
<c:when test="${param.step=='selectRoleInfoList'}">
	<c:set var="paginater.forwardUrl" value="/sm/RoleAction.do?step=selectRoleInfoList" scope="page" /> 
</c:when>
<c:otherwise>
	<c:set var="paginater.forwardUrl" value="/sm/RoleAction.do?step=selectRoleSearch" scope="page" /> 
</c:otherwise>
</c:choose>
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
		listObject = new ListUtil.Listing('listObject', 'roleInfoTable');
		listObject.init();
		//重载definedWin中实现
		//选择
		top.definedWin.selectListing = function(inum) {
			listObject.selectWindow(1);
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
