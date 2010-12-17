<%--
    @version:$Id: edit.jsp,v 1.1 2010/12/10 10:56:44 silencily Exp $
    @since $Date: 2010/12/10 10:56:44 $
--%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<html>
<jsp:directive.include file="/decorators/edit.jspf"/>
<jsp:directive.include file="/decorators/default.jspf"/>
<body class="main_body">
<form name="f" action="" method="post">
	
	<input type="hidden" name="oid" value="<c:out value='${theForm.bean.id}'/>"/>
	<input type="hidden" name="parentCode" value="<c:out value='${theForm.parentCode}'/>"/>
	<input type="hidden" name="step" value="save"/>
	<input type="hidden" name="bean.version" value="<c:out value='${theForm.bean.version}'/>"/>
	<input type="hidden" name="bean.subid" value="<c:out value='${theForm.bean.subid}'/>"/>
	<input type="hidden" name="bean.typeCode" value="<c:out value='${theForm.bean.typeCode}'/>"/>
	<input type="hidden" name="bean.displayName" value="<c:out value='${theForm.bean.displayName}'/>"/>
	<input type="hidden" name="oldCode" value="<c:out value='${theForm.bean.code}'/>"/>
	<c:set var="parent" value="${theForm.bean.parent}"/>
	<jsp:directive.include file="parent.jspf"/>
	
	<div class="update_subhead">
		 <span class="switch_open" onclick="StyleControl.switchDiv(this,submenu1)" title="…ÏÀıΩ⁄µ„">¥˙¬Î–≈œ¢</span>
	</div>
	<table border="0" cellpadding="0" cellspacing="0" class="Detail" id="submenu1">
		<c:if test="${theForm.bean.deleteState=='0'}">
			<c:set var="textdisabled" value="class=readonly readonly=readonly" scope="page"></c:set>
		</c:if>
		<tr>
			<input type="hidden" name="bean.id" value="<c:out value='${theForm.bean.id}' />" />
			<input type="hidden" name="bean.deleteState" value="<c:out value='${theForm.bean.deleteState}'/>" />
			<input type="hidden" name="bean.layerNum" value="<c:out value='${theForm.bean.layerNum}'/>" />
			<input type="hidden" name="bean.code" value="<c:out value='${theForm.bean.code}'/>"/>
			<td class="attribute">√˚≥∆</td>
			<td>
				<input bisname="√˚≥∆" maxlength="50" name="bean.name" value="<c:out value='${theForm.bean.name}'/>"/>
				<span class="font_request">*</span>&nbsp;
			</td>
			<td colspan="2"></td>
		</tr>
		<tr>
			<td class="attribute">œ‘ æÀ≥–Ú</td>
			<td>
				<input bisname="œ‘ æÀ≥–Ú" maxlength="22" name="bean.showSequence" style="text-align:right" value="<c:out value='${theForm.bean.showSequence}'/>"/>
				<span class="font_request">*</span>&nbsp;
			</td>
			<td colspan="2"></td>
		</tr>
		<tr>
			<td class="attribute">√Ë ˆ</td>
			<td colspan="3">
				<input bisname="√Ë ˆ" maxlength="100" name="bean.codeDesc" style="width:440px" class="input_long" value="<c:out value = '${theForm.bean.codeDesc}'/>"/>
				<span title="<c:out value='${theForm.bean.codeDesc}' escapeXml='true'/>">
					<input type="button" id="edit_longText" onClick="definedWin.openLongTextWin(document.getElementsByName('bean.codeDesc')[0],'','');"/>
				</span>
			</td>
		</tr>
	</table>
	
	<script type="text/javascript">
		var msgInfo_ = new msgInfo();
		if (CurrentPage == null) {
			var CurrentPage = {};
		}
		parent.setParentCd(document.getElementById('parentCode').value);
		CurrentPage.reset = function () {
			document.f.reset();
		}
		
		CurrentPage.submit = function () {
			if (!CurrentPage.validation()||!verifyAllform()) {
				return;
			}
			FormUtils.post(document.forms[0], "<c:url value='/common/basiccodemanager.do?step=save'/>");
		}
		CurrentPage.validation = function () {
			return Validator.Validate(document.forms[0], 5);
		}
		CurrentPage.initValidateInfo = function () {
			var delState = document.getElementById('bean.deleteState').value;
			document.getElementById('bean.name').dataType = 'Require';
			document.getElementById('bean.name').msg = msgInfo_.getMsgById('CM_I032_C_1',['√˚≥∆']);
			document.getElementById('bean.showSequence').dataType = 'Require|Number';
			document.getElementById('bean.showSequence').msg = msgInfo_.getMsgById('CM_I032_C_1',['œ‘ æÀ≥–Ú']) + "|" + msgInfo_.getMsgById('CM_I039_C_1',['œ‘ æÀ≥–Ú']);
		}
		CurrentPage.initValidateInfo();
		CurrentPage.create = function() {
			var pLayer = document.getElementById("parentLayer").value;
			var parentCd = document.getElementById("parentCode").value;
			if (pLayer != 2) {
				alert(msgInfo_.getMsgById('CM_I040_C_0'));
				return false;
			}
			//$('oid').value = '';
			FormUtils.post(document.forms[0], "<c:url value='/common/basiccodemanager.do?step=edit&type=new&parentCode=" + parentCd + "'/>");
		}
		
		function Validator.afterSuccessMessage(){
			if(parent.codeTree){
				parent.codeTree.Update(parent.codeTree.SelectId);
			}
		}
		Global.setHeight();
	</script>
	<script type="text/javascript" src = "<c:out value = "${initParam['publicResourceServer']}/js/bisValidate.js" />"></script>
</form>
</body>
<script type="text/javascript" src = "<c:out value = "${initParam['publicResourceServer']}/js/checkonchangevalues.js" />" ></script>
</html>
