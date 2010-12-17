<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ include file="/decorators/default.jspf"%>

<body class="main_body">
<form>	
	<input type="hidden" name="oid" value="<c:out value = "${theForm.oid}" />" />
	<input type="hidden" name="systemModuleName" value="<c:out value = "${theForm.systemModuleName}" />" />
	<input type="hidden" name="parentCode" value="<c:out value = "${theForm.parentCode}" />" />
	<input type="hidden" name="systemCode.parentCode" value="<c:out value = "${theForm.parentCode}" />" />

	<div class="list_group">
		<div class="list_title">
			<c:choose>
				<c:when test = "${theForm.create}">
					����
				</c:when>
				<c:otherwise>
					�޸�
				</c:otherwise>
			</c:choose>����
		</div>	
	</div>
	
	<%@ include file = "/jsp/common/systemcode/systemcodeParentSummary.jspf" %>
					
	<div class="update_subhead"> 
		<span class="switch_open" onClick="StyleControl.switchDiv(this, submenu1)" title="����������">������Ϣ</span>  
	</div>
	<table border="0" cellpadding="0" cellspacing="0" class="Detail" id="submenu1">
		<tr>
			<td class="attribute">����</td>
			<td colspan="3" nowrap="nowrap">
				<input type="text" name="systemCode.code" value="<c:out value = '${theForm.systemCode.code}' default = "${theForm.parentCode}." />" class="input_long" />				
				<input type="button" id="edit_longText" onclick="definedWin.openLongTextWin($('systemCode.code'));" />
				<span class="font_request">*</span>
			</td>
		</tr>
		<tr>	
			<td class="attribute">����</td>
			<td colspan="3" nowrap="nowrap">
				<input type="text" name="systemCode.name" value="<c:out value = '${theForm.systemCode.name}' />" class="input_long" />				
				<input type="button" id="edit_longText" onclick="definedWin.openLongTextWin($('systemCode.name'));" />
				<span class="font_request">*</span>
			</td>
		</tr>								
		<tr>
			<td class="attribute">���</td>
			<td>
				<input type="text" name="systemCode.sequenceNo" value="<c:out value = '${theForm.systemCode.sequenceNo}' />" />
				<span class="font_request">*</span>
			</td>
			<td class="blank">&nbsp;</td>
			<td>&nbsp;</td>			
		</tr>
		<tr>		
			<td class="attribute">����</td>
			<td colspan="3" nowrap="nowrap">
				<input type="text" name="systemCode.description" value="<c:out value = "${theForm.systemCode.description}" />"  class="input_long" />
				<input type="button" id="edit_longText" onclick="definedWin.openLongTextWin($('systemCode.description'));" />	
			</td>
		</tr>
	</table>
	</div>

</form>
</body>
<script language="javascript">
var CurrentPage = {};

CurrentPage.create = function() {
	FormUtils.redirect('<c:url value = "/common/systemcode.do?step=info&systemModuleName=${theForm.systemModuleName}&parentCode=${theForm.parentCode}" />');
}

CurrentPage.submit = function() {
	if (CurrentPage.validation() == false) {
		return;
	}

	FormUtils.post(document.forms[0], '<c:url value = "/common/systemcode.do?step=save" />');
}

CurrentPage.validation = function () {
	return Validator.Validate(document.forms[0], 4);
}

CurrentPage.initValidateInfo = function () {			
	if (FormUtils.isWriteable($('systemCode.code'))) {
		$('systemcode.code').dataType = 'Require';
		$('systemcode.code').msg = '���벻��Ϊ��';	
	}
	if (FormUtils.isWriteable($('systemCode.name'))) {
		$('systemcode.name').dataType = 'Require';
		$('systemcode.name').msg = '���Ʋ���Ϊ��';	
	}
	if (FormUtils.isWriteable($('systemCode.sequenceNo'))) {
		$('systemcode.sequenceNo').dataType = 'Require|Integer';
		$('systemcode.sequenceNo').msg = '��Ų���Ϊ��|��ű���������';	
	}
}

CurrentPage.initValidateInfo();

// ��ʾ��Ϣǰˢ����
Validator.beforeSuccessMessage = function() {
	parent.parent.codeTree.Update(parent.parent.codeTree.SelectId);
}
</script>
</html>

