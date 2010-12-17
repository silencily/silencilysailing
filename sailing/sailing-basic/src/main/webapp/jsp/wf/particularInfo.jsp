<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ include file="/decorators/default.jspf" %>

<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<script type="text/javascript"><!--
var TableSort = {};
var msg= new msgInfo();
var CurrentPage = {};
function checknumber(obj){
	var str = obj.value;

	var patn = /[0-9]+$/;
	if(patn.test(str)){
		return true;
	}
	return false;
}
function rowfield(){
	var rows = document.forms[0].row;
	var bool;
	if(rows!=null){
		var rowobj = rows.length;
		if(document.forms[0].r[2].checked){
			for(i=0;i<rowobj;i+=4){
				if(document.forms[0].row[i+1].value==""){
					bool = false
				}else if(document.forms[0].row[i+2].value==""){
					bool = false;
				}
			}
		}
	}
	return bool;
}

function selectAll(){
		s = document.forms[0].commitStep;
		if(s == null) return;
		if(s.length<1) return;
		for(i=0;i<s.length;i++){
			s.options[i].selected=true;
		}
		return true;
}
//单选按钮	
function detect()
{
	if (document.forms[0].r[0].checked)
	 document.getElementById("divId_requQues").style.display = "none";	
	else if (document.forms[0].r[1].checked)
	 document.getElementById("divId_requQues").style.display = "none";
	else
	document.getElementById("divId_requQues").style.display = "";
	
}


function createRole(tblUserId) {
	definedWin.openListingUrl('permissionAdd','<c:url value = "/sm/RoleAction.do?step=selectMultipleRoleEntry&strFlg=flow" />');
}

function definedWin.beginListObject(ids){
	var roleId =new Array();
	var roleName =new Array();
	var strId ="";
	var strName="";
	if ( ids != null) {
		strId = ids[0];
		if(strId.id){
			return;
		}
		strName =ids[1];
	}
	roleId = strId.split(";");
	roleName = strName.split(";");
	var i = roleId.length;
	var opt;
	for(i;i>0;i--){
		opt = new Option();
		opt.value = roleId[i-1];
		opt.text = roleName[i-1];
		//opt.selected = true;
		if(roleId[i-1]!=""){
			document.forms[0].tblUserId.options.add(opt);
		}
	}

}

function deletes(tblUserId){
    Control1=tblUserId;
    var opt;
    var j=Control1.length;
    
    if(j==0) return;
    for(j;j>0;j--)
    {
        if(Control1.options[j-1].selected==true)
        {    
            opt = new Option();
            opt.text =Control1.options[j-1].text;
            opt.value=Control1.options[j-1].value;
            Control1.options[j-1] = null;
        }
    }
}
//2008-5-27 yangxl 控制结束时stepid start

function controlss(){
 	if(document.forms[0].checkStepId.checked==true){
		document.forms[0].stepName.value='结束';
		document.forms[0].stepIds.value='END';
		document.forms[0].stepId.value = '99';
		document.forms[0].stepIds.readOnly = true;
		document.getElementById("quanxianId").style.display="none";
	}else{
		document.forms[0].stepName.value='';
		document.forms[0].stepIds.value='';
		document.forms[0].stepIds.readOnly = false; 
		document.getElementById("quanxianId").style.display="block";
	}
}
//end
</script>

<html>
<body class="main_body">
<form name="f" method="post" >
  <input type="hidden" name="oid" value="<c:out value = '${theForm.oid}' />" />
  <input type="hidden" name="step" value="<c:out value = '${theForm.step}' />" />
<div class="list_explorer">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td><span class="switch_open"
			onClick="StyleControl.switchDiv(this, $('supplyerInfoTable1'))"
			title="点击收缩表格"> 步骤基本信息 </span></td>
	</tr>
</table>
</div>
<div id="divId_scrollLing" class="list_scroll">
<table border="0" cellpadding="0" cellspacing="0" class="Listing"
	id="supplyerInfoTable1">
	<tr>
		<td>步骤ID：</td>
		<input type="hidden"  id="stepId" name="wfpinfo.stepId" value="<c:out value='${theForm.wfpinfo.stepId}'/>" />
		
		<td>&nbsp; 
		<input id="stepIds" value="" />
		&nbsp;<span class="font_request">*</span>&nbsp;
			&nbsp; <input type="checkbox" name="checkStepId" value="checkStepId"
					onClick="controlss();"/>&nbsp;选中结束流程&nbsp;	
		</td>
		<td>步骤名称：</td>
		<td>&nbsp; <input name="wfpinfo.stepName" id="stepName"
			value="<c:out value='${theForm.wfpinfo.stepName}'/>"/>&nbsp;<span class="font_request">*</span>&nbsp;</td>
	</tr>
	<tr>
		<td>处理事项：</td>
		<td>&nbsp; <input name="wfpinfo.dealInfo" id="dealInfo"
			value="<c:out value='${theForm.wfpinfo.dealInfo}'/>" /></td>
		<td>退回：</td>
		<c:set value="${theForm.wfpinfo.goback}" var="goback" />
		<c:choose>
			<c:when test="${theForm.wfpinfo.goback=='Y'}">
				<td>&nbsp; <input type="checkbox" name="tmp.wfpinfo.goback"  id="goback" 
					checked="checked"/></td>
			</c:when>
			<c:when test="${theForm.wfpinfo.goback=='N'}">
				<td>&nbsp; <input type="checkbox" name="tmp.wfpinfo.goback" id="goback"/></td>
			</c:when>
			<c:otherwise>
				<td>&nbsp; <input type="checkbox" name="tmp.wfpinfo.goback" id="goback" /></td>
			</c:otherwise>
		</c:choose>
	    <input type="hidden" id="hiddenGoback" name="wfpinfo.goback"/>
	
	</tr>
	<tr>
		<td>退回步骤：</td>
		<td>&nbsp; <c:set value="${theForm.wfpinfo.gobackStep}" var="gobackStep" />
		<select name="wfpinfo.gobackStep" style="width:80px; font-size:13px;">
			<option value=""><c:forEach var="goback"
				items="${theForm.gobacklist}">
				<option value="<c:out value='${goback.vvalue}'/>"
					<c:out value='${goback.vsel}'/>><c:out
					value='${goback.vnum}' />
			</c:forEach>
		</select></td>
		<td>取消：</td>
		<c:set value="${theForm.wfpinfo.cancel}" var="cancel" />
		<c:choose>
			<c:when test="${theForm.wfpinfo.cancel=='Y'}">
				<td>&nbsp; <input type="checkbox" name="tmp.wfpinfo.cancel" id="cancel"
					checked="checked" /></td>
			</c:when>
			<c:when test="${theForm.wfpinfo.cancel=='N'}">
				<td>&nbsp; <input type="checkbox" name="tmp.wfpinfo.cancel" id="cancel"/></td>
			</c:when>
			<c:otherwise>
				<td>&nbsp; <input type="checkbox" name="tmp.wfpinfo.cancel" id="cancel"/></td>
			</c:otherwise>
		</c:choose>
		<input type="hidden" id="hiddenCancel" name="wfpinfo.cancel"/>
	</tr>
	<tr>
		<td>取回：</td>
		<c:set value="${theForm.wfpinfo.fetch}" var="fetch" />
		<c:choose>
			<c:when test="${theForm.wfpinfo.fetch=='Y'}">
				<td>&nbsp; <input type="checkbox" name="tmp.wfpinfo.fetch"  id="fetch"
					checked="checked" /></td>
			</c:when>
			<c:when test="${theForm.wfpinfo.fetch=='N'}">
				<td>&nbsp; <input type="checkbox" name="tmp.wfpinfo.fetch" id="fetch"/></td>
			</c:when>
			<c:otherwise>
				<td>&nbsp; <input type="checkbox" name="tmp.wfpinfo.fetch" id="fetch"/></td>
			</c:otherwise>
		</c:choose>
		<input type="hidden" id="hiddenFetch" name="wfpinfo.fetch"/>
		<td>挂起：</td>
		<c:set value="${theForm.wfpinfo.suspend}" var="suspend" />
		<c:choose>
			<c:when test="${theForm.wfpinfo.suspend=='Y'}">
				<td>&nbsp; <input type="checkbox" name="tmp.wfpinfo.suspend" id="suspend"
					 checked="checked" /></td>
			</c:when>
			<c:when test="${theForm.wfpinfo.suspend=='N'}">
				<td>&nbsp; <input type="checkbox" name="tmp.wfpinfo.suspend" id="suspend"/></td>
			</c:when>
			<c:otherwise>
				<td>&nbsp; <input type="checkbox" name="tmp.wfpinfo.suspend" id="suspend"/></td>
			</c:otherwise>
		</c:choose>
		<input type="hidden" id="hiddenSuspend" name="wfpinfo.suspend"/>
	</tr>
	<tr>
		<td>传阅：</td>
		<c:set value="${theForm.wfpinfo.passround}" var="passround" />
		<c:choose>
			<c:when test="${wfpinfo.passround=='Y'}">
				<td>&nbsp; <input type="checkbox" name="tmp.wfpinfo.passround" id="passround"
					 checked="checked" /></td>
			</c:when>
			<c:when test="${wfpinfo.passround=='N'}">
				<td>&nbsp; <input type="checkbox" name="tmp.wfpinfo.passround" id="passround"/></td>
			</c:when>
			<c:otherwise>
				<td>&nbsp; <input type="checkbox" name="tmp.wfpinfo.passround" id="passround"/></td>
			</c:otherwise>
		</c:choose>
		<input type="hidden" id="hiddenPassround" name="wfpinfo.passround"/>
		<td>提交：</td>
		<c:set value="${theForm.wfpinfo.commit}" var="commit" />
		<c:choose>
			<c:when test="${theForm.wfpinfo.commit=='Y'}">
				<td>&nbsp; <input type="checkbox" name="tmp.wfpinfo.commit" id="commit"
					checked="checked" /></td>
			</c:when>
			<c:when test="${theForm.wfpinfo.commit=='N'}">
				<td>&nbsp; <input type="checkbox" name="tmp.wfpinfo.commit" id="commit"/></td>
			</c:when>
			<c:otherwise>
				<td>&nbsp; <input type="checkbox" name="tmp.wfpinfo.commit" id="commit"/></td>
			</c:otherwise>
		</c:choose>
		<input type="hidden" id="hiddenCommit" name="wfpinfo.commit"/>
	</tr>
	<tr>
		<td>审批方式：</td>
		<td>&nbsp; <c:set value="${theForm.wfpinfo.helpman}" var="helpman" /> <select
			name="wfpinfo.helpman" style="width:80px; font-size:13px;">
			<c:choose>
				<c:when test="${theForm.wfpinfo.helpman=='point.emp'}">
					<option value="point.emp">指定人</option>
					<option value="point.role">指定角色</option>
					<option value="point.business">业务指定</option>
				</c:when>
				<c:when test="${theForm.wfpinfo.helpman=='point.role'}">
					<option value="point.role">指定角色</option>
					<option value="point.business">业务指定</option>
					<option value="point.emp">指定人</option>
				</c:when>
				<c:when test="${theForm.wfpinfo.helpman=='point.business'}">
					<option value="point.business">业务指定</option>
					<option value="point.role">指定角色</option>
					<option value="point.emp">指定人</option>
				</c:when>
				<c:otherwise>
					<option value="point.role">指定角色</option>
					<option value="point.emp">指定人</option>
					<option value="point.business">业务指定</option>
				</c:otherwise>
			</c:choose>
		</select></td>
		<td>会签：</td>
		<c:set value="${theForm.wfpinfo.togethersign}" var="togethersign" />
		<c:choose>
			<c:when test="${theForm.wfpinfo.togethersign=='Y'}">
				<td>&nbsp; <input type="checkbox" name="tmp.wfpinfo.togethersign" id="togethersign"
					value="togethersign" checked="checked" /></td>
			</c:when>
			<c:when test="${theForm.wfpinfo.togethersign=='N'}">
				<td>&nbsp; <input type="checkbox" name="tmp.wfpinfo.togethersign" id="togethersign"/></td>
			</c:when>
			<c:otherwise>
				<td>&nbsp; <input type="checkbox" name="tmp.wfpinfo.togethersign" id="togethersign"/></td>
			</c:otherwise>
		</c:choose>
		<input type="hidden" id="hiddenTogethersign" name="wfpinfo.togethersign"/>
	</tr>
	<tr>
		<td>发消息：</td>
		<c:set value="${theForm.wfpinfo.message}" var="message" />
		<c:choose>
			<c:when test="${theForm.wfpinfo.message=='Y'}">
				<td>&nbsp; <input type="checkbox" name="tmp.wfpinfo.message"  id="message"
					checked="checked" /></td>
			</c:when>
			<c:when test="${theForm.wfpinfo.message=='N'}">
				<td>&nbsp; <input type="checkbox" name="tmp.wfpinfo.message" id="message"/></td>
			</c:when>
			<c:otherwise>
				<td>&nbsp; <input type="checkbox" name="tmp.wfpinfo.message" id="message"/></td>
			</c:otherwise>
		</c:choose>
		<input type="hidden" id="hiddenMessage" name="wfpinfo.message"/>
		<td>&nbsp;特殊对象：</td>
		<td>&nbsp;
			<input type="text"
					name="wfpinfo.pointStep"
					id="pointStep"
					value="<c:out value ='${theForm.wfpinfo.pointStep}'/>"
					style="width:300px;height:15px;overflow:hidden" />
					<input type="button" id="edit_longText"
					onclick="definedWin.openLongTextWin($('pointStep'));" />
		</td>
	</tr>
	<tr>
		<td>提交步骤：</td>
		<td>&nbsp; <c:set value="${theForm.wfpinfo.commitStep}" var="commitStep" />
		<select id="commitStep" name="wfpinfo.commitStep" multiple="multiple" size="10" type="select-multiple" style="width:150px; font-size:13px;">
			<option>
			   <c:forEach var="co" items="${theForm.commitlist}">
				    <option value="<c:out value='${co.vvalue}'/>"<c:out value='${co.vsel}'/>><c:out value='${co.vnum}'/>
			</c:forEach>
		</select></td>
		<td>权限要求：</td>
		<td align="left" valign="top">&nbsp; 
		<c:set value="${theForm.wfpinfo.tblUserId}" var="tblUserId" /> 
		<select id="tblUserId" name="wfpinfo.tblUserId" size="10" multiple="multiple" type="select-multiple"
			style="width:150px; font-size:13px;">
			<c:forEach var="roleli" items="${theForm.roleli}">
					<option value="<c:out value='${roleli.id}'/>">
					<c:out value='${roleli.name}' />
			</c:forEach>
		
		</select>&nbsp;
		<input type="hidden" name="roleinfo.roleName" value="">
		<input type="button" name="Submit10" value="添加" onClick="createRole()" />
		<input type="button" name="Submit20" value="删除" onClick="deletes(tblUserId)" /></td>
	</tr>
	<tr>
		<td>步骤检查：</td>
		<td colspan="3">
			<input type="text"
					name="wfpinfo.stepChecks"
					id="stepChecks"
					value="<c:out value ='${theForm.wfpinfo.stepChecks}'/>"
					style="width:300px;height:15px;overflow:hidden" />
					<input type="button" id="edit_longText"
					onclick="definedWin.openLongTextWin($('stepChecks'));" />
		&nbsp;</td>
	</tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" class="Listing"  id="quanxianId">
	<tr>
		<td>&nbsp;&nbsp;权限控制信息&nbsp;</td>
	</tr>

	<input type="hidden" name="ooid" value="<c:out value='${ooid}'/>" />
	
	<tr>
		<td colspan="4" align="left" valign="middle">
		<table border="0" cellpadding="0" cellspacing="0" class="Listing">
			<tr>
				<c:set value="${theForm.wfpinfo.fieldStatus}" var="r" />
				<c:choose>
					<c:when test="${theForm.wfpinfo.fieldStatus=='1'}">
					  <td colspan="4" align="left" valign="middle">
						<input type="radio" name="r" showName="编辑" required="true" checked onClick="detect();">全部可编辑 
						<input type="radio" name="r" onClick="detect();">全部不可编辑
					    <input type="radio" name="r" onClick="detect();">部分可编辑
					  </td>
					</c:when>
					<c:when test="${theForm.wfpinfo.fieldStatus=='2'}">
						<td colspan="4" align="left" valign="middle">
						  <input type="radio" name="r" showName="编辑" onClick="detect();">全部可编辑 
						  <input type="radio" name="r" required="true" checked onClick="detect();">全部不可编辑 
						  <input type="radio" name="r" onClick="detect();">部分可编辑
					    </td>
					</c:when>
					<c:when test="${theForm.wfpinfo.fieldStatus=='3'}">
						<td colspan="4" align="left" valign="middle">
						  <input type="radio" name="r" showName="编辑" required="true" checked onClick="detect();">全部可编辑 
						  <input type="radio" name="r" onClick="detect();">全部不可编辑 
						  <input type="radio" name="r"  required="true" checked onClick="detect();">部分可编辑
				        </td>
					</c:when>
					<c:otherwise>
						<td colspan="4" align="left" valign="middle">
						  <input type="radio" name="r" showName="编辑" required="true" checked onClick="detect();">全部可编辑 
						  <input type="radio" name="r" onClick="detect();">全部不可编辑 
						  <input type="radio" name="r" onClick="detect();">部分可编辑
					    </td>
					</c:otherwise>
				</c:choose>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<input type="hidden" name="nameoper" value="<c:out value='${nameoper}'/>" />
		<td colspan="4" align="left" valign="middle">
		
		<table border="0" cellpadding="0" cellspacing="0" class="Listing" id="divId_requQues" style="display:none">
			<tr>
				<td>操作</td>
				<td>表单项</td>
				<td>表单KEY</td>
				<td>是否必填</td>
			</tr>

			<c:forEach var="row" items="${list}" varStatus="status">
				<c:set var="hazardInfoCount" value="${status.count}" scope="page" />
				<tr id="requQues_trId_[<c:out value='${status.index}'/>]">
					<td width="25%" height="28" align="left" >
						&nbsp;&nbsp;<input type="button" class="list_create" onClick="CurrentPage.addRow(divId_requQues);" id="requQues_[<c:out value='${status.index}'/>].add" style="display:" name="addBtn" title="新增" /> 
						<input type="button" class="list_delete" onClick="requQues.delRowaa(divId_requQues,this);return false;" id="requQues_[<c:out value='${status.index}'/>].del" style="display:" row="<c:out value='${status.index}'/>" name="delBtn" title="删除" /> 
						<input type="button" class="list_renew" onClick="requQues.renRowCur(divId_requQues,this);return false;" id="requQues_[<c:out value='${status.index}'/>].old" style="display:none" row="<c:out value='${status.index}'/>" name="oldBtn" holdObj=true title="恢复" />&nbsp;</td>
					<td width="25%" height="28" align="left" >
						<input type="hidden" name="locspec[<c:out value='${status.index}'/>].id" value="<c:out value='${row.fid}' />" id="requQues_[<c:out value='${status.index}'/>].id" /> 
						<input type="hidden" name="locspec[<c:out value='${status.index}'/>].version" value="<c:out value='${item2.version}' />" /> 
						<input type="hidden" name="locspec[<c:out value='${status.index}'/>].delFlg" value="0" id="requQues_[<c:out value='${status.index}'/>].delFlg" /> 
						<input type="hidden" name="row" value="<c:out value='${row.fid}'/>" /> 
						<input id="Field[<c:out value='${status.index}'/>].fieldNameEdit" name="row" value="<c:out value="${row.fname}"/>">&nbsp;</td>
					<td width="25%" align="left">
						<input id="Field[<c:out value='${status.index}'/>].fieldCodeEdit" name="row" value="<c:out value="${row.fcode}"/>">&nbsp;
						<input type="button" id="edit_query" name="buttonSelect[<c:out value="${status.index}"/>]" onClick="CurrentPage.selectTskName('Field[<c:out value='${status.index}'/>]',definedWin, 'checkbox')" row="<c:out value='${status.index}'/>"  title="点击这里选择可编辑项"/>
					</td>
					<td width="25%" align="left">&nbsp;
					<select name="row" style="width:50px; font-size:13px;">
						<c:choose>
							<c:when test="${row.feditor=='Y'}">
								<option value="Y">是</option>
								<option value="N">否</option>
							</c:when>
							<c:when test="${row.feditor=='N'}">
								<option value="N">否</option>
								<option value="Y">是</option>
							</c:when>
							<c:otherwise>
								<option value="Y">是</option>
								<option value="N">否</option>
							</c:otherwise>
						</c:choose>
					</select>
					</td>
				</tr>
			</c:forEach>
		</table>
		
		</td>
	</tr>
</table>

<table border="0" cellpadding="0" cellspacing="0" class="Listing">
	<tr>
		<td height="35" align="center" valign="bottom">
		 <input type="button" name="Submit22" value="确定" onClick="CurrentPage.paradd()" />
		 <input type="reset" value="取消" />
		 <input type="button" name="backbutton" value="返回" onClick="CurrentPage.goback()"/>
		</td>
	</tr>
</table>

</form>
</body>

<script language="javascript">
var requQues = new EditPage("requQues");
requQues.holdRow = 0;
requQues.uuidObj   = "Field[0].fieldNameEdit";
requQues.rowNumber = <c:out value = "${pageScope['hazardInfoCount']}" default = "0" />;
//2007-5-27 yangxl 改 start
if(document.forms[0].stepId.value=='99'){
	document.forms[0].checkStepId.checked='true';
	document.forms[0].stepIds.value="END";
}else{
	document.forms[0].stepIds.value = document.forms[0].stepId.value;	
}
//end
CurrentPage.addRow = function(table) { 
			
			var len = table.rows.length; 
			if(len>1)
			{
				requQues.addListingRow(divId_requQues);
			}
			else
			{
				FormUtils.post(document.forms[0], '<c:url value = "/am/LocSpecAction.do?step=locSpec&type=new" />');
			}
			Global.setHeight();			
		}
		requQues.delRowaa = function (table,sel){		 
			var inum = sel.row;			
			var del = document.getElementById(requQues.name+"_["+inum+"].delFlg").value;
			var str = document.getElementById(requQues.name+"_["+inum+"].id").value;
			var obj = document.getElementById(requQues.name+"_trId_["+inum+"]");
			requQues.afterDeleteRow(table,sel);
			if(str !=""){
				requQues.disableButton(obj);
				requQues.switchButton(inum,"none","none","")
			}else{
				if(inum == 0){
					requQues.disableButton(obj);
					requQues.switchButton(inum,"none","none","")
				}else{
					table.deleteRow(obj.rowIndex);
					Global.setHeight();
				}
			}
		}
		requQues.renRowCur = function (table,sel){
			var inum = sel.row;
			var obj = document.getElementById(requQues.name+"_trId_["+inum+"]");
			this.unlockTr(obj);
			requQues.ableButton(obj);
			requQues.renRow(table,sel);
			requQues.switchButton(inum,"","","none")
		}
		CurrentPage.selectTskName = function(tskInfoPre, definedWin, iCheckMode){
			var nameoper = document.forms[0].nameoper.value;
			definedWin.openListingUrl(tskInfoPre, '<c:url value ="/wf/NapeEditAction.do?step=select&nameoper="/>'+nameoper);
		}
		/**
          * 在弹出人员选择窗体进行多选时，需要在此重载该函数，来对本页面做相应处理
          */
		definedWin.setListObjectInFor = function(arr,attribute,arrWithAtt) {
			var txtName, i1, i2, index, i;
			
			txtName = definedWin.txtName;
			i1 = txtName.indexOf("["); 
			i2 = txtName.indexOf("]");
			index = txtName.substring(i1+1, i2);
			i = parseInt(index);
			//---add by lzx
			for (i = parseInt(index); i < requQues.rowNumber; i++) {
				txtName = txtName.substring(0, i1) + "[" + i + "]";
				var td = document.getElementById(txtName + "." + "fieldCodeEdit");
				if (td) {
					break;
				}
			}
			if (i == requQues.rowNumber && parseInt(index) < requQues.rowNumber)
				txtName = txtName.substring(0, i1) + "[" + i + "]";	
			//---add by lzx	end		
			var td = document.getElementById(txtName + "." + "fieldCodeEdit");
			if (td == 'unfined' || td == null) requQues.addListingRow(divId_requQues);
			if(selectCheck(arr['fieldCodeEdit'],index))
			{
				for(var t in arr) {			
					tt = t.replace(/_/gi, "."); //在列表定义field时候使用"_"代替"."
					temp = document.getElementById(txtName + "." + tt);
					if (temp) {
						if(temp.tagName == "INPUT") {
							temp.value = arr[t];
							continue;
						} else {
							temp.innerText = arr[t];
							continue;
						}					
					}else{
						//如果声明field值，这里则可以填充多个
						var objs = document.getElementsByTagName("INPUT");
						for( n=0;n<objs.length;n++) {
							if(objs[n].field == (txtName + "." + tt)){
								objs[n].value = arr[t];
							}
						}
					}
				}
				
				// 设置下一个控件名
				txtName = txtName.substring(0, i1);
				definedWin.txtName = txtName + "[" + (i+1)+ "]";
			}
		}
		function selectCheck(sId,index)
		{
			Validator.clearValidateInfo();
			var table = document.all.divId_requQues;
			var subscriptCount = requQues.getMaxSubscript(table);
			for(var i = 0; i <=subscriptCount; i++)
			{
				var rowNum = requQues.getRowNumBySubscript(table,i);
		    	if(rowNum == -1)
		    	{
		      		continue;
		      	}
				var id = trim(document.getElementById("Field["+i+"].fieldCodeEdit").value);
				if(sId==id)
				{
					if(index!=i)
					{
						Validator.warnMessage('所选可编辑项已存在，请重新选择!');
					}
					return false;
				}
			}
			return true;
		}
					Global.setHeight();
	///////////////////////////////////////////////////////				
   // 确定按钮；	
 CurrentPage.paradd=function(){
	Validator.clearValidateInfo();
	// var boolCheck = document.forms[0].boolCheck;
	var obj = document.forms[0].stepIds; 
	
    var a="";
	var b = document.forms[0].commitStep;
	for(i=0;i<b.length;i++){
		if(b.options[i].selected){
			a +=b.options[i].value+",";
		}
	}
    
	var a1="";
	var b1 = document.forms[0].tblUserId;	
	for(i=0;i<b1.length;i++){
	        // alert(b1.options[i].value);
			a1 +=b1.options[i].value+",";
	}
	
	// 2008-5-27改 yangxl start
	if(document.forms[0].checkStepId.checked!=true){
		if(document.forms[0].stepIds.value==""){
		    Validator.warnMessage(msg.getMsgById('WF_I016_p_0'));
		    document.forms[0].stepIds.focus();
		    return;
	  	}else if(checknumber(obj)!=true) {
		    Validator.warnMessage(msg.getMsgById('WF_I017_p_0'));
		    document.forms[0].stepIds.focus();
		    return;
		}else{
			document.forms[0].stepId.value = document.forms[0].stepIds.value;
		}
	}
	// end
	<%-----------------------------------------------%>
	// 提交时确定checkbox的值；
	if(document.getElementById("goback").checked==true){
	    document.getElementById("hiddenGoback").value="Y";
	}else{
	    document.getElementById("hiddenGoback").value="N";
	}
	if(document.getElementById("passround").checked==true){
	    document.getElementById("hiddenPassround").value="Y";
	}else{
	    document.getElementById("hiddenPassround").value="N";
	}
	if(document.getElementById("togethersign").checked==true){
	    document.getElementById("hiddenTogethersign").value="Y";
	}else{
	    document.getElementById("hiddenTogethersign").value="N";
	}
	if(document.getElementById("fetch").checked==true){
	    document.getElementById("hiddenFetch").value="Y";
	}else{
	    document.getElementById("hiddenFetch").value="N";
	}
	if(document.getElementById("cancel").checked==true){
	    document.getElementById("hiddenCancel").value="Y";
	}else{
	    document.getElementById("hiddenCancel").value="N";
	}
	if(document.getElementById("suspend").checked==true){
	    document.getElementById("hiddenSuspend").value="Y";
	}else{
	    document.getElementById("hiddenSuspend").value="N";
	}
	if(document.getElementById("message").checked==true){
	    document.getElementById("hiddenMessage").value="Y";
	}else{
	    document.getElementById("hiddenMessage").value="N";
	}
	if(document.getElementById("commit").checked==true){
	    document.getElementById("hiddenCommit").value="Y";
	}else{
	    document.getElementById("hiddenCommit").value="N"; 
	}
	
	<%-----------------------------------------------%>
    if(document.forms[0].stepName.value==""){
		Validator.warnMessage(msg.getMsgById('WF_I018_p_0'));
	    document.forms[0].stepName.focus();
	    return;
    }else if(document.forms[0].passround.checked==true && document.forms[0].togethersign.checked==true){
    	Validator.warnMessage(msg.getMsgById('WF_I019_p_0'));
    	return;
    }else if(document.forms[0].passround.checked==true && document.forms[0].goback.checked==true){
    	Validator.warnMessage(msg.getMsgById('WF_I020_p_0'));
    	return;
    }else if(document.forms[0].passround.checked==true && document.forms[0].fetch.checked==true){
    	Validator.warnMessage(msg.getMsgById('WF_I021_p_0'));
    	return;
    }else if(document.forms[0].passround.checked==true && document.forms[0].suspend.checked==true){
    	Validator.warnMessage(msg.getMsgById('WF_I022_p_0'));
    	return;
    }else if(document.forms[0].togethersign.checked==true && document.forms[0].goback.checked==true){
    	Validator.warnMessage(msg.getMsgById('WF_I023_p_0'));
    	return;
    }else if(document.forms[0].togethersign.checked==true && document.forms[0].fetch.checked==true){
    	Validator.warnMessage(msg.getMsgById('WF_I024_p_0'));
    	return;
    }else if(rowfield()==false){
    	Validator.warnMessage(msg.getMsgById('WF_I025_p_0'));
    	return;
    }
   
    var f ="";
    if (document.forms[0].r[0].checked){
    	f = 1;
    }
    else if (document.forms[0].r[1].checked){
    	f = 2;
    }
    else{
    	f = 3;
    	if(document.forms[0].row.value==""){
    		Validator.warnMessage(msg.getMsgById('WF_I026_p_0'));
    	}
    }
   
	FormUtils.post(document.forms[0], '<c:url value = "/wf/operationlist.do?step=saveParticular&commitStep="/>'+a+'&tblUserId='+a1+'&fieldStatus='+f);
	/*
	var strOid = document.getElementById('oid').value;
	var strStepIdVal = document.getElementById('stepIds').value;
	var strStepNameVal = document.getElementById('stepName').value;
	var strDealInfoVal = document.getElementById('dealInfo').value;
	var strGobackVal = document.getElementById('hiddenGoback').value;
	var strGobackStepVal = document.getElementsByName('wfpinfo.gobackStep');
	var strCancelVal = document.getElementById('hiddenCancel').value;
	var strFetchVal = document.getElementById('hiddenFetch').value;
	var strSuspendVal = document.getElementById('hiddenSuspend').value;
	var strPassroundVal = document.getElementById('hiddenPassround').value;
	var strCommitVal = document.getElementById('hiddenCommit').value;
	// var strCommitStepVal = document.getElementById('commitStep').value;
	var strCommitStepVal = a;
	var strTogethersignVal = document.getElementById('hiddenTogethersign').value;
	var strHelpmanVal = document.getElementsByName('wfpinfo.helpman');
	var strMessageVal = document.getElementById('hiddenMessage').value;
	// var strTblUserIdVal = document.getElementById('tblUserId').value;
	// var strTblUserIdVal = a1;

	var arr = new Array();
	arr[0] = top.definedWin.txtName;
	arr[1] = strOid;
	arr[2] = strStepIdVal;
	arr[3] = strStepNameVal;
	arr[4] = strDealInfoVal;
	arr[5] = strGobackVal;
	arr[6] = strGobackStepVal[0].value;
	arr[7] = strCancelVal;
	arr[8] = strFetchVal;
	arr[9] = strSuspendVal;
	arr[10] = strPassroundVal;
	arr[11] = strCommitVal;
	arr[12] = strCommitStepVal;
	arr[13] = strTogethersignVal;
	arr[14] = strHelpmanVal[0].value;
	arr[15] = strMessageVal;
	// arr[16] = strTblUserIdVal;
	
	top.definedWin.myfun(arr);	
	*/
}

CurrentPage.goback = function(){
    definedWin.closeModalWindow();
    top.definedWin.backReflesh();
}
</script>
</html>