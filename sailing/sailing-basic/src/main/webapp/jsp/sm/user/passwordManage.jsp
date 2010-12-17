<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf"/>
<jsp:directive.include file="/decorators/edit.jspf"/>
<html>
<div class="main_title"> <div>密码管理</div></div>
<body class="main_body">
<div class="update_subhead" >
    <span class="switch_open" onClick="StyleControl.switchDiv(this, $('supplierQuery'))" title="">当前用户信息</span>
</div>

<div id="supplierQuery" style="display:">
	<table width="100%"  border="0" cellpadding="0" cellspacing="0" class="Detail" style="display:">
	    <tr>
			<td class="attribute">员工号</td>
	        <td>
	           <input name="temp.empCd" value="<c:out value='${theForm.empId}'/>"
				class="readonly" readonly="readonly" />
	        </td>
			<td class="attribute">用户姓名</td>
	        <td>
	        <input name="temp.userName" value="<c:out value='${theForm.userName}'/>"
				class="readonly" readonly="readonly" />
	        </td>
	    </tr>
	</table>
</div>
<form name="f">
<input type="hidden" name="oid" value="<c:out value = '${theForm.oid}' />" />
	<table width="100%"  border="0" cellpadding="0" cellspacing="0" class="Detail" style="display:">
	   	<tr>
			<td class="attribute">登录密码</td>
			<td>
			<input type="password" style="width:200px" name="oldPassword"/>
			</td>
		</tr>
		<tr>
			<td class="attribute" >新密码</td>
			<td>
			<input type="password" id="temp.new" style="width:200px" name="password" maxlength="16" bisname="新密码"/><span class="font_request">*</span>
			</td>
		</tr>
		<tr>
			<td class="attribute">重复新密码</td>
			<td>
			<input type="password" id="temp.repeat" style="width:200px" /><span class="font_request">*</span>
			</td>
		</tr>
	 </table>
</form>
</body>
</html>
<script type="text/javascript">
var msgInfo_ = new msgInfo();
var CurrentPage = {};
CurrentPage.submit = function() {
	var newly=document.getElementById('temp.new');
	var repeat=document.getElementById('temp.repeat');
	if (!CurrentPage.validation()) {
				return false;
			}
	if(newly.value!=repeat.value){

		Validator.clearValidateInfo();
		Validator.warnMessage(msgInfo_.getMsgById('SM_I007_C_0'));
		return false;
	}
	FormUtils.post(document.forms[0], '<c:url value = "/sm/userManageAction.do?&step=updatePassword" />');
}
CurrentPage.validation = function () {
		if(!verifyAllform()){return false;}
		return Validator.Validate(document.forms[0], 4);
	}
CurrentPage.initValideInput = function () {
			document.getElementById('temp.new').dataType = 'Require';
			document.getElementById('temp.new').msg = msgInfo_.getMsgById('SM_I006_C_0');
	}
CurrentPage.initValideInput();
</script>