<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf"/>
<jsp:directive.include file="/decorators/edit.jspf"/>
<!-- liujinliang 2007-09-21 14:00 -->
<html>
<title>数据权限详细</title>
<body>
<form name="f">
<input type="hidden" name="oid"  id= "oid" value="<c:out value='${theForm.oid}'/>"/>
<input type="hidden" name="step" value="<c:out value = '${theForm.step}' />" />
<input type="hidden" name="oid2" value="<c:out value = '${theForm.cmnUserMember.id}' />" />
<div class="main_title">
<div>数据权限详细信息</div>
</div>
	<table border="0" cellpadding="0" cellspacing="0" class="Detail"
		id="submenu1">
		<tr>
			<td class="attribute">查询范围</td>
			<td>
				<vision:text
					rwCtrlType="2"
					permissionCode=""
					name="cmnUserMember.searchScope"
					value="${theForm.cmnUserMember.searchScope}"
					comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicyyTotalPolicy" />
			</td>
		</tr>	
		<tr>
			<td class="attribute">修改范围</td>
			<td>
			<vision:text
				rwCtrlType="2"
				permissionCode=""
				name="cmnUserMember.updateScope"
				value="${theForm.cmnUserMember.updateScope}"
				comInvorkeeClanet.silencily.sailing.common.crud.tag.TagSecurityTotalPolicyg.TagSecurityTotalPolicy" />
			</td>
		</tr>
		<tr>
			<td class="attribute">新建范围</td>
			<td>
			<vision:text
				rwCtrlType="2"
				permissionCode=""
				name="cmnUserMember.createScope"
				value="${theForm.cmnUserMember.createScope}"
				conet.silencily.sailing.common.crud.tag.TagSecurityTotalPolicymmon.crud.tag.TagSecurityTotalPolicy" />
			</td>
		</tr>
		<tr>
			<td class="attribute">删除范围</td>
			<td colspan="3">
			<vision:text 
				rwCtrlType="2" 
				permissionCode="" 
				name="cmnUserMember.deleteScope" 
				value="${theForm.cmnUserMember.deleteSconet.silencily.sailing.common.crud.tag.TagSecurityTotalPolicyy.sailing.common.crud.tag.TagSecurityTotalPolicy" />
			</td>
		</tr>
		<tr>
			<td class="attribute">实体成员</td>
			<td colspan="3">
			<vision:text 
				rwCtrlType="2" 
				permissionCode="" 
				readonly="true"
				name="cmnUserMember.tblCmnEntityMember.name" 
				value="${theForm.cmnUserMember.tblCmnEntnet.silencily.sailing.common.crud.tag.TagSecurityTotalPolicynet.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
			</td>
		</tr>
		</table>
</form>
</body>
<script type="text/javascript">
	var msgInfo_ = new msgInfo();
	var TopOpera = {};
	TopOpera.save   = function(){}
	if (CurrentPage == null) {
		var CurrentPage = {};
	}	
	CurrentPage.validation = function () {
			return Validator.Validate(document.forms[0],5);
		}	
top.definedWin.selectListing = function(){
	var message = "" ;
		if (!CurrentPage.validation()) {
			return;
		}
	var oid2 = $('oid2').value;
	FormUtils.post(document.forms[0], '<c:url value='/sm/userManageAction.do?step=permissionSave&oid2='/>'+ oid2,true);
}
Validator.afterSuccessMessage = function(info)
{
	top.definedWin.closeModalWindow();
	top.openWinCallBack();
}
</script>
</html>

