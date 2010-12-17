<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf" />

<html>
<body class="list_body">
<script type="text/javascript">
var msgInfo_ = new msgInfo();
if (CurrentPage == null) {
   		var CurrentPage = {};
	}
<c:if test="${theForm.oid !=''}">
CurrentPage.create = function () {
	definedWin.openListingUrl('roleAdd','<c:url value = "/sm/RoleAction.do?step=selectMultipleRoleEntry" />');
}
</c:if>
definedWin.returnListObject = function(ids){
	var strs="";
	if ( ids != null) {
		for(var i=0;i<ids.length;i++){	
			if(ids[i]!=null||ids[i]!=""){
				strs+=ids[i] + "$";
			}
		}
		$('step').value = 'selectRole';
		$('strs').value = strs;
		FormUtils.post(document.forms[0], '<c:url value="/sm/userManageAction.do"/>');
	}
}
CurrentPage.myquery = function() {
	FormUtils.post(document.forms[0], '<c:url value="/sm/userManageAction.do?step=queryRole"/>');
}

CurrentPage.deletePermissionsAll= function() {			
		var str="";
		var dd=document.getElementsByName('detailIds');
		for(var i=0;i<dd.length;i++){
			if(dd[i].checked==true){
				var oid1=dd[i].value;
				str+=oid1+"$";
			}
		}
		if (str==""||str==null){
			alert(msgInfo_.getMsgById('SM_I014_A_0'));
		}else{
			if (!confirm(msgInfo_.getMsgById('SM_I015_A_0'))) {
			    return false;
			}
			$('step').value = 'deleteRoleAll';
			$('oids').value = str;
	   		FormUtils.post(document.forms[0], '<c:url value="/sm/userManageAction.do"/>');
   		}
   }
     TableSort.dblClick = function()
   {
   	   return false;
   }
   
   CurrentPage.removeRole = function(sel) {
	if (! confirm(msgInfo_.getMsgById('SM_I009_A_0'))) {
		return;
	}
	var oid1=sel.id;
	$('step').value = 'removeRole';
	FormUtils.post(document.forms[0], '<c:url value = "/sm/userManageAction.do?oid1=" />' + oid1 );
}
</script>
<form id="f">
<div class="update_subhead"><span class="switch_open"
	onClick="StyleControl.switchDiv(this, $('userInfo'))" title="">当前用户信息</span>
</div>
<div id="userInfo" style="">
<table width="100%" border="0" cellpadding="0" cellspacing="0"
	class="Detail" style="display: ">
	<tr>
		<td class="attribute">员工编号</td>
		<td><input name="empCd"
			value="<c:out value='${theForm.bean.empCd}'/>" class="readonly"
			readonly="readonly" /></td>
		<td class="attribute">员工姓名</td>
		<td><input name="empName"
			value="<c:out value='${theForm.bean.empName}'/>" class="readonly"
			readonly="readonly" /></td>
	</tr>
</table>
</div>
<form action="" id="f">
<div class="update_subhead"><span class="switch_close"
	onClick="StyleControl.switchDiv(this,$(supplierQuery))"
	title="点击这里进行查询">查询条件</span></div>
<div id="supplierQuery" style="display: none">
<table width="100%" border="0" cellpadding="0" cellspacing="0"
	class="Detail" id="divId_flaw_search_common" style="display: ">
	<tr>
		<td class="attribute">角色标识</td>
		<td><search:text name="tblCmnRole.roleCd" oper="like"
			type="java.lang.String" valueDefault='${theForm}' /></td>
		<td class="attribute">角色名称</td>
		<td><search:text name="tblCmnRole.name" oper="like"
			type="java.lang.String" valueDefault='${theForm}' /></td>
	</tr>
</table>
<div class="query_button"><input type="button" value="" name=""
	class="opera_query" title="点击查询" onClick="CurrentPage.myquery();">
</div>
</div>
<input type="hidden" name="oid"
	value="<c:out value = '${theForm.oid}' />" /> <input type="hidden"
	name="step" value="<c:out value = '${theForm.step}' />" /> <input
	type="hidden" name="oids" /> <input type="hidden" name="strs" />
<div class="update_subhead">
<table width="100%" border="0" cellpadding="0" cellspacing="0"
	class="update_subhead">
	<tr>
		<td><span class="switch_open"
			onClick="StyleControl.switchDiv(this, $('divId_scrollLing'))">角色列表</span>
		</td>

	</tr>
</table>
</div>
<div id="divId_scrollLing" class="list_scroll">
<table border="0" cellpadding="0" cellspacing="0" class="Listing"
	id="listtable" onClick="TableSort.sortColumn(event)">
	<input type="hidden" name="checkall"
		onClick="CurrentPage.selectedAll()" title="是否全选" />
	<thead>
		<tr id="tableHead">
			<td nowrap="nowrap"><input id='detailIdsForPrintAll'
				type='checkbox'
				onClick="FormUtils.checkAll(this,document.getElementsByName('detailIds'))" /></td>
			<td nowrap="nowrap">角色标识</td>
			<td nowrap="nowrap">角色名称</td>
			<td nowrap="nowrap">角色路径</td>
			<td nowrap="nowrap">说明</td>
			<td nowrap="nowrap">操作</td>
		</tr>
	</thead>
	<tbody id='tablist'>
		<c:forEach var="item" items="${theForm.role}" varStatus="status">
			<tr>
				<td align="center"><input type="checkbox" name="detailIds"
					onclick="FormUtils.check($('detailIdsForPrintAll'),document.getElementsByName('detailIds'))"
					value="<c:out value="${item.id}"/>" />&nbsp;</td>
				<td nowrap="nowrap" align='left'><c:out
					value="${item.tblCmnRole.roleCd}" />&nbsp;</td>
				<td nowrap="nowrap" align='left'><span
					onClick="javascript:definedWin.openListingUrl('rolePerUser', '<c:url value = '/sm/userManageAction.do?step=rolePerUserEntry&rowId=${item.id}' />',null,true)"
					class="font_link"><c:out value="${item.tblCmnRole.name}" />&nbsp;
				</td>
				<td nowrap="nowrap" align='left'><c:out
					value="${item.tblCmnRole.roleRoute}" />&nbsp;</td>
				<td nowrap="nowrap" align='left'><c:out
					value="${item.tblCmnRole.note}" />&nbsp;</td>
				<td nowrap="nowrap" align='center'><input type="button"
					class="list_delete" onClick="CurrentPage.removeRole(this)"
					id="<c:out value="${item.id}"/>" style="display: " name="delBtn"
					title="删除" /></td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<div class="list_bottom"><c:set var="paginater.forwardUrl"
	value="/sm/userManageAction.do?step=queryRole" scope="page" /> <%@ include
	file="/decorators/paginater.jspf"%> <input
	name="" type="button" align="right" class="opera_batchdelete"
	value="批量删除" onClick="CurrentPage.deletePermissionsAll(); return false" />
</div>
</div>
</form>
</body>
</html>
