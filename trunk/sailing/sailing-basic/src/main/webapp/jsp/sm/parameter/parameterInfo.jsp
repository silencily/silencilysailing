<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ include file="/decorators/default.jspf" %>
<jsp:directive.include file="/decorators/edit.jspf"/>
<html>
<body class="main_body">
<form action="/sm/parameterManageAction.do" method="post">
	<input type="hidden" name="oid" value="<c:out value = '${theForm.oid}' />" />
	<input type="hidden" name="step" value="<c:out value = '${theForm.step}' />" />
<div class="update_subhead">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td>
			<span class="switch_open" onClick="StyleControl.switchDiv(this, $('supplierInfo'))">基本信息</span>
		</td>
	</tr>
</table>
</div>
<div id="supplierInfo" style="display:">
	<table width="100%"  border="0" cellpadding="0" cellspacing="0" class="Detail" style="display:">
	  		<tr>
			<td class="attribute">参数标识</td>
			<td>
			<vision:text
						rwCtrlType="2"
						permissionCode=""
						dataType="1"
						id="sign"
						name="sysParameter.parameterSign"
						value="${theForm.sysParameter.parameterSign}"
						maxlength="32"
						bisname="参数标识"
						required="true"
						comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
			</td>
			<td class="attribute">参数值</td>
			<td>
			<vision:text
						rwCtrlType="2"
						permissionCode=""
						dataType="1"
						id="value"
						name="sysParameter.parameterValue"
						value="${theForm.sysParameter.parameterValue}"
						maxlength="256"
						bisname="参数值"
						required="true"
						comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
	       </td>
		</tr>
		<tr>
			<td class="attribute" >参数说明</td>
			<td colspan="3">
			<vision:text
						rwCtrlType="2"
						permissionCode=""
						dataType="6"
						name="sysParameter.parameterNote"
						value="${theForm.sysParameter.parameterNote}"
						maxlength="256"
						bisname="参数说明"
						longTextType="true"
						comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
<%--	  		<input type="text" class="input_long" name="sysParameter.parameterNote" value="<c:out value='${theForm.sysParameter.parameterNote}'/>"/>--%>
<%--	  		<input type="button" class="statusWarning" id="edit_longText" onClick="definedWin.openLongTextWin(document.getElementsByName('sysParameter.parameterNote')[0],'','');"/>--%>
		 </td>
		</tr>
	     	</table>
</div>
</form>
</body>
</html>

<script type="text/javascript">
var msgInfo_ = new msgInfo();
var CurrentPage = {};
CurrentPage.submit = function() {
	if (!CurrentPage.validation()) {
				return false;
			}
	var id = $('oid').value;
	$('step').value = 'update';

	FormUtils.post(document.forms[0], '<c:url value = "/sm/parameterManageAction.do" />');
}
	CurrentPage.create = function() {
	$('oid').value = '';
	$('step').value = 'info';
	FormUtils.post(document.forms[0], '<c:url value='/sm/parameterManageAction.do'/>');
} 
	CurrentPage.validation = function () {
		if(!verifyAllform()){return false;}
		return Validator.Validate(document.forms[0], 4);
	}
	CurrentPage.initValideInput = function () {
			document.getElementById('sign').dataType = 'Require';
			document.getElementById('sign').msg = msgInfo_.getMsgById('SM_I021_C_0');
			document.getElementById('value').dataType = 'Require';
			document.getElementById('value').msg = msgInfo_.getMsgById('SM_I022_C_0');
	}
	CurrentPage.initValideInput();
</script>