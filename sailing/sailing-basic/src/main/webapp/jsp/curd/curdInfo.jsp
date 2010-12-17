<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ include file = "/decorators/default.jspf" %>
<html>
<body>
<form>	
	<input type="hidden" name="oid" value="<c:out value = "${theForm.oid}" />" />
	<input type="hidden" name="step" value="<c:out value = "${theForm.step}" />" />

	<div class="main_body">
		<div class="list_group">
				<div class="list_title">SER <span class="list_notes">详细信息</span> </div>
				<div class="list_button">	
					<input type="button" id="opera_create"  title="新增" onClick="CurrentPage.redirect2info()" name="新增" value="新增"/>
					<input type="button" id="opera_save"  title="保存" onClick="CurrentPage.submit()" value="保存"/>	
					<input type="reset" id="opera_reset" value="重置"  title="重置" />							
				</div>
		</div>
	
		<div class="update_subhead">	
			<span class=" switch_open" onClick="StyleControl.switchDiv(this, $('submenu1'))" title="点击收缩表格">基本信息 </span>
		</div>
		<table border="0" cellpadding="0" cellspacing="0" class="Detail" id="submenu1">
			<tr>
				<td class="attribute">field1</td>
				<td>
					<input type="text" name="hi.field1" value="<c:out value = "${theForm.hi.field1}" />" /><span class="font_request">*</span>
				</td>
				<td class="attribute">field2</td>
				<td>
					<input type="text" name="hi.field2" value="<c:out value = "${theForm.hi.field2}" />" /><span class="font_request">*</span>
				</td>			
			</tr>
			<tr>
				<td class="attribute">field7</td>
				<td>
					<input type="text" name="hi.field7" value="<c:out value = "${theForm.hi.field7}" />" /><span class="font_request">*</span>
				</td>
				<td class="attribute">field8</td>
				<td>
					
					<input type="text" name="hi.field8" value="<c:out value = "${theForm.hi.field8}" />"    />
					
				</td>			
			</tr>
			<tr>
				<td class="attribute">name</td>
				<td colspan="3">
					<textarea name="hi.name" rows="4" cols="80"><c:out value = "${theForm.hi.name}" /></textarea>
				</td>		
			</tr>
		</table>					
	</div>
	
</form>
</body>
<jsp:directive.include file="/jsp/curd/edit.jsp" />


<script type="text/javascript">

var CurrentPage = {};

CurrentPage.submit = function() {
	if (CurrentPage.validation() == false) {
		return;
	}

	var id = $('oid').value;
	if (id == null || id.length == 0) {
		$('step').value = 'save';
		
	} else {
		$('step').value = 'update';
		
	}

	FormUtils.post(document.forms[0], '<c:url value = "/curd/curdAction.do" />');
}

CurrentPage.redirect2info = function() {
	location.href = '<c:url value = "/curd/curdAction.do?step=info" />';
}

CurrentPage.validation = function () {
	return Validator.Validate(document.forms[0], 4);
}

CurrentPage.initValidateInfo = function () {
	if (FormUtils.isWriteable($('hi.field1'))) {
		$('hi.field1').dataType = 'Require';
		$('hi.field1').msg = 'hi.field1不能为空';
	}
	if (FormUtils.isWriteable($('hi.field2'))) {
		$('hi.field2').dataType = 'Require';
		$('hi.field2').msg = 'hi.field2不能为空';	
	}
	if (FormUtils.isWriteable($('hi.field7'))) {
		$('hi.field7').dataType = 'Require';
		$('hi.field7').msg = 'hi.field7不能为空';	
	}
	if (FormUtils.isWriteable($('hi.field8'))) {
		$('hi.field8').dataType = 'Require';
		$('hi.field8').msg = 'hi.field8不能为空';	
	}
}

CurrentPage.initValidateInfo();

</script>