<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf"/>
<script type="text/javascript" src = "<c:out value = "${initParam['publicResourceServer']}/js/bisValidate.js" />"></script>

<script type="text/javascript">	
		if (CurrentPage == null) {
			var CurrentPage = {};
		}
		if(requQues == null){
			var requQues = new EditPage("requQues");		
		}
</script>
<body class="main_body">
<form method="post" action="" name="f" enctype="multipart/form-data">
	<input name="oid" type="hidden" value="<c:out value = '${theForm.oid}' />">
	<c:set var = "beanClassName" value="net.silencily.sailing.basic.wf.domain.TblWfEditInfo"/>
		<input type="hidden" name="taskId" value="<c:out value='${param.taskId}'/>" />
		<input type="hidden" name="urlKey" value="<c:out value='${param.urlKey}'/>" />
			<div class="update_subhead">
				<span title="��������ڵ�" class="switch_open" onClick="StyleControl.switchDiv(this,$('detailBase_table'))">������Ϣ</span>
			</div>
<table border="0" width="100%" class="Detail" id="detailBase_table">
		  <tr>
			<td class="attribute">��key</td>
			<td>
				<vision:text
				rwCtrlType="${theForm.bean.rwCtrlType}"
				permissionCode=""
				wfPermissionCode=""
				maxlength="100" 
				bisname="��key" 
				required="true" 
				name="bean.fieldCodeEdit"
				value="${theForm.bean.fieldCodeEdit}"
				comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurity4WorkFlowListTotalPolicy"/>					
			</td>
			<td class="attribute">����</td>
			<td>
				<vision:text
				rwCtrlType="${theForm.bean.rwCtrlType}"
				permissionCode=""
				wfPermissionCode=""
				maxlength="100" 
				bisname="����" 
				required="true" 
				name="bean.fieldNameEdit"
				value="${theForm.bean.fieldNameEdit}"
				comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurity4WorkFlowListTotalPolicy"/>	
			</td>
		</tr>	
		<tr>
			<td class="attribute">��������</td>
			<td colspan="3">
				<vision:text
				rwCtrlType="${theForm.bean.rwCtrlType}"
				permissionCode=""
				wfPermissionCode=""
				maxlength="1000" 
				bisname="��������" 
				required="true" 
				name="bean.tblWfName"
				longTextType="true"
				value="${theForm.bean.tblWfName}"
				comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurity4WorkFlowListTotalPolicy"/>	
			</td>
		</tr>	
		<tr>
			<td class="attribute">ҵ������</td>
			<td colspan="3">
				<vision:text 
				rwCtrlType="${theForm.bean.rwCtrlType}"
				permissionCode=""
				wfPermissionCode=""
				maxlength="1000" 
				bisname="ҵ������" 
				required="true" 
				name="bean.tblWfOperName"
				longTextType="true"
				value="${theForm.bean.tblWfOperName}"
				comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurity4WorkFlowListTotalPolicy"/>
			</td>
		</tr>
		<tr>
			<td class="attribute">��ע</td>
			<td colspan="3">
				<vision:text 
				rwCtrlType="${theForm.bean.rwCtrlType}"
				permissionCode=""
				wfPermissionCode=""
				maxlength="1000" 
				bisname="��ע"
				name="bean.remark"
				longTextType="true"
				value="${theForm.bean.remark}"
				comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurity4WorkFlowListTotalPolicy"/>
			</td>
		</tr>
</table>
</form>
</body>

<script type="text/javascript">

CurrentPage.create = function () {
	var oid = document.getElementById("oid");
	oid.value = "";
	FormUtils.post(document.forms[0], '<c:url value = "/wf/PopedomEditAction.do?step=info&mainTableClassName=TblWfEditInfo" />');

}
CurrentPage.submit = function () {

	if (!CurrentPage.initValideInfo()) {
			return;					
	}
	FormUtils.post(document.forms[0], '<c:url value = "/wf/PopedomEditAction.do?step=save&taskId=${param.taskId}&urlKey=${param.urlKey}" />');

}

CurrentPage.initValideInfo = function () {
		Validator.clearValidateInfo();	
		
		var fieldCodeEdit = trim(document.getElementById("bean.fieldCodeEdit").value);
		var fieldNameEdit = trim(document.getElementById("bean.fieldNameEdit").value);
		var tblWfName = trim(document.getElementById("bean.tblWfName").value);
		var tblWfOperName = trim(document.getElementById("bean.tblWfOperName").value);

		if(fieldCodeEdit == "")
		{
			Validator.warnMessage(msgInfo_.getMsgById('CM_I032_C_1',['��key']));
			document.getElementById("bean.fieldCodeEdit").focus();
				return false;
		}
		if(fieldNameEdit == "")
		{
			Validator.warnMessage(msgInfo_.getMsgById('CM_I032_C_1',['����']));
			document.getElementById("bean.fieldNameEdit").focus();
				return false;
		}
		if(tblWfName == "")
		{
			Validator.warnMessage(msgInfo_.getMsgById('CM_I032_C_1',['��������']));
			document.getElementById("bean.tblWfName").focus();
				return false;
		}
		if(tblWfOperName == "")
		{
			Validator.warnMessage(msgInfo_.getMsgById('CM_I032_C_1',['ҵ������']));
			document.getElementById("bean.tblWfOperName").focus();
				return false;
		}
		
		if(!verifyAllform()){return false;}
		
		return true;		
	}
	

Global.setHeight();

</script>
<script type="text/javascript" src = "<c:out value = "${initParam['publicResourceServer']}/js/checkonchangevalues.js" />"></script>