<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf"/>
<jsp:directive.include file="/decorators/edit.jspf"/>
<!-- liujinliang 2007-09-21 14:00 -->
<html>
<title>角色详细</title>
<body class="list_body"  onLoad="CurrentPage.returnShow">
<script language="javascript">
if (CurrentPage == null) {
    var CurrentPage = {};
}
top.definedWin.selectListing = function(){
		var strOid = document.getElementById('oid').value;
		var startTime = document.getElementById('startTime').value;
		var endTime = document.getElementById('endTime').value;
		if(startTime==""){
		alert("起始时间不能为空");
		return false;
		}
		if(endTime==""){
		alert("结束时间不能为空");
		return false;
		}
		if (DateUtils.CompareDate(startTime,endTime)<0){
					Validator.clearValidateInfo();
					alert("起始时间不能大于失效时间，请重新输入！");
					return false;	
				}
		if (CurrentPage.CheckDay($('startTime').value) == false ) {
					Validator.clearValidateInfo();
					alert("起始时间不能是过去时间，请重新输入！");
					return false;
				}
		if (CurrentPage.CheckDay($('endTime').value) == false ) {
					Validator.clearValidateInfo();
					alert("失效时间不能是过去时间，请重新输入！");
					return false;
				}
		$('step').value = 'roleSave';	
		FormUtils.post(document.forms[0], '<c:url value='/sm/ConsignManagerAction.do'/>',true);
		var begin = strOid + ".beginTime";
		var end = strOid + ".invalidTime";
		var arr = new Array();
		arr[0] = top.definedWin.txtName;
		arr[1] = begin;
		arr[2] = end;
		arr[3] = startTime;
		arr[4] = endTime;
		top.definedWin.listObject(arr);
}
/*
	CurrentPage.returnPermission = function() {
		var strOid = document.getElementById('oid').value;
		var startTime = document.getElementById('startTime').value;
		var endTime = document.getElementById('endTime').value;
		if(startTime==""){
		alert("起始时间不能为空");
		return false;
		}
		if(endTime==""){
		alert("结束时间不能为空");
		return false;
		}
		if (DateUtils.CompareDate(startTime,endTime)<0){
					Validator.clearValidateInfo();
					alert("起始时间不能大于失效时间，请重新输入！");
					return false;	
				}
		if (CurrentPage.CheckDay($('startTime').value) == false ) {
					Validator.clearValidateInfo();
					alert("起始时间不能是过去时间，请重新输入！");
					return false;
				}
		if (CurrentPage.CheckDay($('endTime').value) == false ) {
					Validator.clearValidateInfo();
					alert("失效时间不能是过去时间，请重新输入！");
					return false;
				}
		var begin = strOid + ".beginTime";
		var end = strOid + ".invalidTime";
		window.opener.document.getElementById(begin).innerHTML = startTime;
		window.opener.document.getElementById(end).innerHTML = endTime;
		window.close();
		$('step').value = 'roleSave';	
		FormUtils.post(document.forms[0], '<c:url value='/sm/ConsignManagerAction.do'/>');
	}
	*/
			var d, s = "";
   			d = DateUtils.GetDBDate();
   			s +=d.getYear() + "-";
   			var month = d.getMonth()+1;
   			if(month<10){
   			s += "0" + month + "-";
   			}else{
   			s += month + "-";
   			}
   			var day = d.getDate();
   			if(day <10){
   			s += "0" + day;
   			}else{
   				s += d.getDate();  
   			}
			CurrentPage.CheckDay = function (day) {
				if (day != ""){
					if(DateUtils.CompareDate(s,day)<0)
						return false;
					}
				else{
					return true;
				}
			}
	
</script>
<form name="f">
<input type="hidden" name="oid"  id= "oid" value="<c:out value='${theForm.oid}'/>"/>
<input type="hidden" name="step" value="<c:out value = '${theForm.step}' />" />
<!--<div class="update_subhead" id="list_button" align="right">-->
<!--	<input type="button" class="opera_save" onClick="CurrentPage.returnPermission()"/>-->
<!--	<input name="button" type="button"  class="opera_cancel" id="cancelname" onClick="window.close();"/>-->
<!--</div>-->
<div class="main_title">
<div>委托角色详细信息</div>
</div>
	<table border="0" cellpadding="0" cellspacing="0" class="Detail"
		id="submenu1">
		<tr>
			<td class="attribute">角色名称</td>
			<td>
				<vision:text
					rwCtrlType="2"
					permissionCode=""
					dataType="1"
					id="temp.cmnRole.displayname"
					name="temp.cmnRole.displayname"
					value="${theForm.userRoleBean.tblCmnRole.name}"
					readonly="true"
					comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicyyTotalPolicy" />
			</td>
			<td class="attribute">角色标识</td>
			<td>
				<vision:text
					rwCtrlType="2"
					permissionCode=""
					dataType="1"
					id="temp.cmnRole.roleCd"
					name="temp.cmnRole.roleCd"
					value="${theForm.userRoleBean.tblCmnRole.roleCd}"
					readonly="true"
					comInvorkeeClanet.silencily.sailing.common.crud.tag.TagSecurityTotalPolicyg.TagSecurityTotalPolicy" />
			</td>
		</tr>	
		<tr>
			<td class="attribute">起始时间</td>
			<td>
			<input type="text" value="<c:out value='${theForm.userRoleBean.beginTime}'/>" id ="startTime" name="userRoleBean.beginTime" readonly
				"<c:out value='${pageScope.textdisable}'/>" /><input type="button" value="" id="input_date" onClick="DateComponent.setDay(this,document.getElementsByName('userRoleBean.beginTime')[0])"
				<c:out value='${pageScope.buttondisable}'/> />
			</td>
			<td class="attribute">失效时间</td>
			<td>
			<input type="text" value="<c:out value='${theForm.userRoleBean.invalidTime}'/>" id ="endTime" name="userRoleBean.invalidTime" readonly
				"<c:out value='${pageScope.textdisable}'/>" /><input type="button" value="" id="input_date" onClick="DateComponent.setDay(this,document.getElementsByName('userRoleBean.invalidTime')[0])"
				<c:out value='${pageScope.buttondisable}'/> />
			</td>
		</tr>
		</table>
</form>
</body>
</html>
