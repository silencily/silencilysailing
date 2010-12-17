<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf"/>
<jsp:directive.include file="/decorators/edit.jspf"/>
<!-- baihe 2007-09-21 14:00 -->
<html>
<title>角色权限详细</title>
<body>
<form name="f">
<div class="main_title">
<div>角色权限详细信息</div>
</div>
<input type="hidden" name="oid"  id= "oid" value="<c:out value='${theForm.oid}'/>"/>
<input type="hidden" name="step" value="<c:out value = '${theForm.step}' />" />
	<table border="0" cellpadding="0" cellspacing="0" class="Detail"
		id="submenu1">
		<tr>
			<td class="attribute">显示名称</td>
			<td>
				<vision:text
					rwCtrlType="2"
					permissionCode=""
					dataType="1"
					id="temp.cmnRolePermission.displayname"
					name="temp.cmnRolePermission.displayname"
					value="${theForm.cmnRolePermission.tblCmnPermission.displayname}"
					readonly="true"
					comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicyyTotalPolicy" />
			</td>
			<td class="attribute">列表数据显示范围</td>
			<td>
				<vision:choose
					rwCtrlType="2"
					permissionCode=""
					value="${theForm.cmnRolePermission.readAccessLevel}" 
					textName="temp.conditions(readAccessLevel).value"
					valueName="cmnRolePermission.readAccessLevel" 
					source='${theForm.readAccessLevelComboList}'
					comInvorkeeClassFullName="com.qware.common.crud.tag.TagDefautPolicy"/>
			</td>
		</tr>
		<tr>
			<td class="attribute">权限类型</td>
			<td>
			<vision:text
				rwCtrlType="2"
				permissionCode=""
				dataType="1"
				id="temp.cmnRolePermission.nodetype"
				name="temp.cmnRolePermission.nodetype"
				value="${theForm.cmnRolePermission.tblCmnPermission.nodeTypeName}"
				readonly="true"
				comInvorkeeClanet.silencily.sailing.common.crud.tag.TagSecurityTotalPolicyg.TagSecurityTotalPolicy" />
			</td>
			<td class="attribute">数据编辑控制</td>
			<td>
				<vision:choose
					rwCtrlType="2"
					permissionCode=""
					value="${theForm.cmnRolePermission.rwCtrl}" 
					textName="temp.conditions(rwCtrl).value"
					valueName="cmnRolePermission.rwCtrl" 
					source='${theForm.rwCtrlComboList}'
					comInvorkeeClassFullName="com.qware.common.crud.tag.TagDefautPolicy"/>
			</td>
		</tr>
		<tr>
			<td class="attribute">功能权限类型</td>
			<td>
			<vision:text
				rwCtrlType="2"
				permissionCode=""
				dataType="1"
				id="temp.cmnPermission.urltype"
				name="temp.cmnPermission.urltype"
				value="${theForm.cmnRolePermission.tblCmnPermission.urltypeCh}"
				conet.silencily.sailing.common.crud.tag.TagSecurityTotalPolicymmon.crud.tag.TagSecurityTotalPolicy" />
			</td>
			<td class="attribute">数据编辑范围</td>
			<td>
				<vision:choose
					rwCtrlType="2"
					permissionCode=""
					value="${theForm.cmnRolePermission.writeAccessLevel}" 
					textName="temp.conditions(writeAccessLevel).value"
					valueName="cmnRolePermission.writeAccessLevel" 
					source='${theForm.writeAccessLevelComboList}'
					comInvorkeeClassFullName="com.qware.common.crud.tag.TagDefautPolicy"/>
			</td>
		</tr>
		<tr>
			<td class="attribute">超链接</td>
			<td colspan="3">
			<vision:text 
				rwCtrlType="2" 
				permissionCode="" 
				dataType="8"
				name="temp.cmnRolePermission.url" 
				value="${theForm.cmnRolePermission.tblCmnPermission.url}"
				readonly="true"
				longTextType="true"
				comInvorkeeClassFullName="com.qware.common.crud.tag.TagDefautPolicy" />
			</td>
		</tr>
		<tr>
			<td class="attribute">权限路径</td>
			<td colspan="3">
			<vision:text 
				rwCtrlType="2" 
				permissionCode="" 
				dataType="8"
				name="temp.cmnRolePermission.permissionRoute" 
				value="${theForm.cmnRolePermission.tblCmnPermission.permissionRoute}"
				readonly="true"
				longTextType="true"
				comInvorkeeClassFullName="com.qware.common.crud.tag.TagDefautPolicy" />
			</td>
		</tr>
		<tr>
			<td class="attribute">说明</td>
			<td colspan="3">
				<vision:text 
				rwCtrlType="2" 
				permissionCode="" 
				dataType="8"
				name="temp.cmnRolePermission.note" 
				value="${theForm.cmnRolePermission.tblCmnPermission.note}"
				readonly="true"
				longTextType="true"
				comInvorkeeClassFullName="com.qware.common.crud.tag.TagDefautPolicy" />
			</td>
		</tr>
		</table>
</form>
</body>
<script type="text/javascript">
if (CurrentPage == null) {
    var CurrentPage = {};
} 
top.definedWin.selectListing = function(){
	var rwCtrl = document.getElementsByName('temp.conditions(rwCtrl).value')[0];
	if(rwCtrl.value==""){
		alert("数据编辑控制不能为空");
		return false;
	}
	$('step').value = 'permissionSave';	
	FormUtils.post(document.forms[0], '<c:url value='/sm/RoleAction.do'/>',true);
	var strOid = document.getElementById('oid').value;
	var strReVal = document.getElementsByName('temp.conditions(readAccessLevel).value');
	var strWrVal = document.getElementsByName('temp.conditions(writeAccessLevel).value');
	var strRWVal = document.getElementsByName('temp.conditions(rwCtrl).value');
	var arr = new Array();
	arr[0] = top.definedWin.txtName;
	arr[1] = strOid;
	arr[2] = strRWVal[0].value;
	arr[3] = strReVal[0].value;
	arr[4] = strWrVal[0].value;
	top.definedWin.listObject(arr);
}
</script>
</html>
