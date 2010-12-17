<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf" />
<html>
	<body>
		<script language="javascript">
			if (CurrentPage == null) {
    	var CurrentPage = {};
		}   

	CurrentPage.deleteRoleAll= function(sel) {
		var strTemp = sel.id;
		if (strTemp == "") {
			var str="";
			var dd=document.getElementsByName('detailIds');
			var count = 0;
			var strTemp;
			for(var i=0;i<dd.length;i++){
				if(dd[i].checked==true){
					count++;
					strTemp = dd[i].value;
				}
			}
			if (count==1){
				str = strTemp;
			} else {
				for(var i=0;i<dd.length;i++){
					if(dd[i].checked==true){
						var oid1=dd[i].value;
						str+=oid1+"$";
					}
				}
			}
		
			if (str==""||str==null){
				alert("请选择角色项");
			}else{
				if (!confirm('是否要收回所有选择的角色? ')) {
				    return false;
				}
				$('oid1').value = str;
		   		FormUtils.post(document.forms[0], '<c:url value="/sm/ConsignManagerAction.do?step=deleteRole"/>');
	   		}		
		} else {
			if (! confirm(" 是否要收回该角色? ")) {
				return;
			}
			$('step').value = 'deleteRole';
			FormUtils.post(document.forms[0], '<c:url value = "/sm/ConsignManagerAction.do?oid1=" />' + strTemp );
		}
	 }

	 CurrentPage.updateRole = function(sel) {
	   var oid2=sel.id;
	   definedWin.openListingUrl('roleUpdate','<c:url value = "/sm/ConsignManagerAction.do?step=roleUpdate&oid2=" />' + oid2);
	//   window.open ('<c:url value = "/sm/ConsignManagerAction.do?step=roleUpdate&oid2=" />' + oid2, "newwindow", "height=500, width=800, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no");
	   
	}
	
CurrentPage.create = function() {
	var oid2 = document.getElementById("oid").value;
	if(oid2 !=""){
		definedWin.openListingUrl('roleAdd','<c:url value = "/sm/RoleAction.do?step=selectEntry&flg=consign&oid=" />' +oid2);
	}
/*		roleOid = window.showModalDialog('<c:url value = "/sm/RoleAction.do?step=selectEntry&oid=" />' +oid2,"","status:no;center:yes;dialogHeight:600px;dialogWidth:900px;help:no");
		if ( roleOid != null) {
			
			for(var i =0; i < roleOid.length; i++){
				if (roleOid[i] != "" && roleOid[i] != null){
					strPal = strPal + roleOid[i] +";";	
					
				}
			}
		FormUtils.post(document.forms[0], '<c:url value = "/sm/ConsignManagerAction.do?step=addRole&oid1=" />' + strPal );
		}
	}else
	{
	 return false;
	}
*/
}
definedWin.returnListObject = function(roleOid){
	var flg = roleOid[0];
	if(flg == "roleUpdate"){
		var begin = roleOid[1];
		var end = roleOid[2];
		document.getElementById(begin).innerHTML = roleOid[3];
		document.getElementById(end).innerHTML = roleOid[4];
	}else{
		var strPal = "";
		if ( roleOid != null) {
			for(var i =0; i < roleOid.length; i++){
				if (roleOid[i] != "" && roleOid[i] != null){
					strPal = strPal + roleOid[i] +";";	
				}
			}
			$('oid1').value = strPal;
			FormUtils.post(document.forms[0], '<c:url value = "/sm/ConsignManagerAction.do?step=addRole" />');
		}else
		{
		 return false;
		}
	}
}
   TableSort.dblClick = function()
   {
   	   return false;
   }
	
</script>
<form name="f">
<input type="hidden" name="oid"  id= "oid" value="<c:out value='${theForm.oid}'/>"/>
<input type="hidden" name="step" value="<c:out value = '${theForm.step}' />" />
<input type="hidden" name="oid1"/>
	<div class="update_subhead">
    <span class="switch_open" onClick="StyleControl.switchDiv(this, $('supplierQuery'))" title="">当前委托用户信息</span>
	</div>		
	<div id="supplierQuery" style="display:">
		<table width="100%"  border="0" cellpadding="0" cellspacing="0" class="Detail" style="display:">
			<tr>
				<td  class="attribute" >员工编号</td>
				<td ><input type="text" name="empId" value="<c:out value = '${theForm.userBean.empCd}'/>" disabled="disabled"/></td>
				<td  class="attribute" >员工姓名</td>
				<td ><input type="text" name="empName" value="<c:out value='${theForm.userBean.empName}'/>"  disabled="disabled"/></td>
			</tr>
		</table>
	</div>
	<div class="update_subhead">
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td>
					 <span class="switch_open" onClick="StyleControl.switchDiv(this,$('divId_requQues'))" title="点击收缩表格">委托角色信息列表</span>
				</td>
			</tr>
		</table>
	</div>
	<div id="divId_requQues" class="list_scroll">
		<table border="0" cellpadding="0" cellspacing="0" class="Listing" id="listtable" onClick="TableSort.sortColumn(event)">
		<input type="hidden" name="checkall" onClick="CurrentPage.selectedAll()" title="是否全选"/>
			<thead>
				<tr>
					<td nowrap="nowrap"><input id='detailIdsForPrintAll' type='checkbox' onClick="FormUtils.checkAll(this,document.getElementsByName('detailIds'))" /></td>
					<td>角色名称</td>
					<td>角色标识</td>
					<td>起始时间</td>
					<td>失效时间</td>
					<td>操作</td>	
				</tr>
			</thead>
			<tbody>
				<c:forEach items = "${theForm.list}" var = "item" varStatus = "status" >
					<tr>
						<td align="center">
							<input type="checkbox"  name="detailIds" value="<c:out value="${item.id}"/>" onClick="FormUtils.check($('detailIdsForPrintAll'),document.getElementsByName('detailIds'))" value="<c:out value="${item.id}"/>" />
						</td>
						<td id="<c:out value="${item.id}"/>.displayname">
							<c:out value="${item.tblCmnRole.name}"/>&nbsp;
						</td>
						<td id="<c:out value="${item.id}"/>.roleCd">
							<c:out value="${item.tblCmnRole.roleCd}"/>&nbsp;
						</td>
						<td id="<c:out value="${item.id}"/>.beginTime" align="center">
							<fmt:formatDate value="${item.beginTime}"/>&nbsp;
						</td>	
						<td id="<c:out value="${item.id}"/>.invalidTime" align="center">
							<fmt:formatDate value="${item.invalidTime}"/>&nbsp;
						</td>	
						<td width="5%">
							<input type="button" class="list_update" onClick="CurrentPage.updateRole(this)" id="<c:out value="${item.id}"/>" style="display:" name="delBtn" title="修改"/>
							<input type="button" class="list_delete" onClick="CurrentPage.deleteRoleAll(this)" id="<c:out value="${item.id}"/>" style="display:" name="delBtn" title="收回"/>
						</td>
					</tr>
				</c:forEach>					
			</tbody>
		</table>
	<div class="list_bottom">
		   <input name="" type="button" class="opera_batchdelete"  value="批量收回" onClick="CurrentPage.deleteRoleAll(this); return false"/>
	</div>
</div>
</form>
</body>
</html>
