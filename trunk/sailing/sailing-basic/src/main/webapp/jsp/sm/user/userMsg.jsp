<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf" />
<html>
<body class="main_body">
<form action="" method="post"><input type="hidden" name="oid"
	value="<c:out value = '${theForm.oid}' />" /> <input type="hidden"
	name="step" value="<c:out value = '${theForm.oid}' />" /> <input
	type="hidden" name="msgOid"
	value="<c:out value = '${theForm.cmnMsgConfig.id}' />" />
<div class="update_subhead"><span class="switch_open"
	onClick="StyleControl.switchDiv(this, $('userInfo'))" title="">��ǰ�û���Ϣ</span>
</div>
<div id="userInfo" style="">
<table width="100%" border="0" cellpadding="0" cellspacing="0"
	class="Detail" style="display: ">
	<tr>
		<td class="attribute">Ա�����</td>
		<td><input name="empCd"
			value="<c:out value='${theForm.bean.empCd}'/>" class="readonly"
			readonly="readonly" /></td>
		<td class="attribute">Ա������</td>
		<td><input name="empName"
			value="<c:out value='${theForm.bean.empName}'/>" class="readonly"
			readonly="readonly" /></td>
	</tr>
</table>
</div>
<div class="update_subhead"><span class="switch_open"
	onClick="StyleControl.switchDiv(this, $('supplierInfo'))" title="">��Ϣ��������</span>
</div>
<div id="supplierInfo" style="display: ">
<table border="0" cellpadding="0" cellspacing="0" class="Detail"
	style="display: ">
	<tr>
		<td class="attribute">IM����</td>
		<td><ec:composite value='${theForm.cmnMsgConfig.imService}'
			valueName="cmnMsgConfig.imService" textName="temp.imService.value"
			source="${theForm.messageComboList}" /></td>
		<td class="attribute">IM��ʶ</td>
		<td><vision:text rwCtrlType="2" permissionCode="" dataType="1"
			name="cmnMsgConfig.imSign" value="${theForm.cmnMsgConfig.imSign}"
			maxlength="200" bisname="IM��ʶ"
			comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
		</td>

	</tr>
	<tr>
		<td class="attribute">EMAIL����</td>
		<td><ec:composite value='${theForm.cmnMsgConfig.emailService}'
			valueName="cmnMsgConfig.emailService"
			textName="temp.emailService.value"
			source="${theForm.messageComboList}" /></td>
		<td class="attribute">EMAIL��ʶ</td>
		<td><vision:text rwCtrlType="2" permissionCode="" dataType="1"
			name="cmnMsgConfig.emailSign"
			value="${theForm.cmnMsgConfig.emailSign}" maxlength="200"
			bisname="EMAIL��ʶ"
			comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
		</td>

	</tr>
	<tr>
		<td class="attribute">SMS����</td>
		<td><ec:composite value='${theForm.cmnMsgConfig.smsService}'
			valueName="cmnMsgConfig.smsService" textName="temp.smsService.value"
			source="${theForm.messageComboList}" /></td>
		<td class="attribute">SMS��ʶ</td>
		<td><vision:text rwCtrlType="2" permissionCode="" dataType="1"
			name="cmnMsgConfig.smsSign" value="${theForm.cmnMsgConfig.smsSign}"
			maxlength="200" bisname="SMS��ʶ"
			comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
		</td>
	</tr>
</table>
</div>
</form>
</html>
<script type="text/javascript">
if (CurrentPage == null) {
   		var CurrentPage = {};
	}
<c:if test="${theForm.oid !=''}">
CurrentPage.submit = function() {
	if (!CurrentPage.validation()) {
				return false;
			}
	if(!verifyAllform()){return false;}
	var id = $('msgOid').value;
	
	if (id == null || id.length == 0) {
		$('step').value = 'msgSave';
		
	} else {

		$('step').value = 'msgUpdate';
	}
	FormUtils.post(document.forms[0], '<c:url value = "/sm/userManageAction.do" />');
}
	CurrentPage.validation = function () {
		return Validator.Validate(document.forms[0], 5);
	}
	CurrentPage.initValideInput = function () {
			document.getElementsByName('temp.imService.value')[0].dataType = 'Require';
			document.getElementsByName('temp.imService.value')[0].msg = "IM������Ϊ��";
			document.getElementsByName('temp.emailService.value')[0].dataType = 'Require';
			document.getElementsByName('temp.emailService.value')[0].msg = "EMAIL������Ϊ��";
			document.getElementsByName('temp.smsService.value')[0].dataType = 'Require';
			document.getElementsByName('temp.smsService.value')[0].msg = "SMS������Ϊ��";
	}
	CurrentPage.initValideInput();
</c:if>
</script>


