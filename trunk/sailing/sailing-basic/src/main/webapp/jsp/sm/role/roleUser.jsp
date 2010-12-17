<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf" />
<html>
	<body>
		<script language="javascript">
			if (CurrentPage == null) {
    	var CurrentPage = {};
		}   

	CurrentPage.deleteUserAll= function() {			
			var str="";
			var dd=document.getElementsByName('detailIds');
			for(var i=0;i<dd.length;i++){
				if(dd[i].checked==true){
					var oid1=dd[i].value;
					str+=oid1+"$";
				}
			}
			if (str==""||str==null){
				alert("请选择删除项");
			}else{
				if (!confirm('是否要删除所有选择的用户信息? ')) {
				    return false;
				}
				$('step').value = 'deleteUsersAll';
				$('oids').value = str;
		   		FormUtils.post(document.forms[0], '<c:url value="/sm/RoleAction.do"/>');
	   		}
	   }
		  
	  CurrentPage.removeUser = function(sel) {
	  	var userName = sel.row;
		if (! confirm(" 是否要删除用户["+userName+"]信息? ")) {
			return;
		}
		var oid1=sel.id;
		$('step').value = 'removeUser';
		FormUtils.post(document.forms[0], '<c:url value = "/sm/RoleAction.do?oid1=" />' + oid1 );
	}
	   
	   CurrentPage.updatePermission = function(sel) {
	   var oid2=sel.id;
	   window.open ('<c:url value = "/sm/RoleAction.do?step=userPermissionDetailed&oid2=" />' + oid2, "newwindow", "height=600, width=540, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no");
	   
	}
	 CurrentPage.myquery= function () {
		if (document.getElementsByName("paginater.page") != null) {
	  	  document.getElementsByName("paginater.page").value = 0;
		}		 
		$('step').value = 'userShow';
		FormUtils.post(document.forms[0], '<c:url value='/sm/RoleAction.do'/>');
	}
	
	CurrentPage.CheckNum = function (name) {
		if (name!=""){
			if(parseInt(name)>=0 && parseInt(name)<=999 ){
				return true;
			}
			else {
				return false;
			}
		}
		return true;
	}
   
	CurrentPage.create = function() {
//	var userIds = new Array();
//	var strPal = "";
//	var iHeight="600";
//	var iWidth="850";
//	var sFeatures="dialogHeight: " + iHeight + "px;" + "dialogWidth: " + iWidth + "px;" + "help:no;" + "scroll:0;";
	definedWin.openListingUrl('permissionAdd','<c:url value = "/sm/userManageAction.do?step=selectInfo&paginater.page=0" />');
/*	userIds=window.showModalDialog("/qware/jsp/sm/user/windowFrame.jsp","",sFeatures);	
	if (userIds != null) {
		for(var i =0; i < userIds.length; i++){
			if (userIds[i] != "" && userIds[i] != null){
				strPal = strPal + userIds[i] +";";	
				
			}
		}
		$('oid2').value = strPal;
		FormUtils.post(document.forms[0], '<c:url value = "/sm/RoleAction.do?step=addUser" />');
	}
	*/
}
definedWin.returnListObject = function(userIds){
	var strPal = "";
	if (userIds != null) {
		for(var i =0; i < userIds.length; i++){
			if (userIds[i] != "" && userIds[i] != null){
				strPal = strPal + userIds[i] +";";	
			}
		}
		$('oid2').value = strPal;
		FormUtils.post(document.forms[0], '<c:url value = "/sm/RoleAction.do?step=addUser" />');
	}
}
   TableSort.dblClick = function()
   {
   	   return false;
   }
</script>
<form name="f">
<input type="hidden" name="parentCode" value="<c:out value = "${theForm.parentCode}"/>"/>
<input type="hidden" name="oid"  id= "oid" value="<c:out value='${theForm.oid}'/>"/>
<input type="hidden" name="empinfo.oid"  id= "empinfo.oid" value=""/>
<input type="hidden" name="oid2"/>
<input type="hidden" name="oids"/>
<input type="hidden" name="step" value="<c:out value = '${theForm.step}' />" />
	<div class="update_subhead">
					<span class="switch_open"  onClick="StyleControl.switchDiv(this, $('supplie'))" title="点击收缩表格">当前角色信息</span>
	</div>		
	<div id="supplie" style="display:">
		<table border="0" cellpadding="0" cellspacing="0" class="Detail" id="parenttable" style="display: ">
			<tr>
				<td  class="attribute" >角色标识</td>
				<td ><input type="text" name="roleCd" value="<c:out value = '${theForm.roleBean.roleCd}'/>" disabled="disabled"/></td>
				<td  class="attribute" >角色名称</td>
				<td ><input type="text" name="name" value="<c:out value='${theForm.roleBean.name}'/>"  disabled="disabled"/></td>
			</tr>
		</table>
	</div>
	
	<div class="update_subhead" >
	    <span class="switch_close" onClick="StyleControl.switchDiv(this, $('supplierQuery'))" title="点击这里进行查询">查询条件</span>
	</div>
	<div id="supplierQuery" style="display:none">
		<table width="100%"  border="0" cellpadding="0" cellspacing="0" class="Detail" id="divId_flaw_search_common" style="display:">
		    <tr>
				<td class="attribute">员工编号</td>
		        <td>
		           <search:text name="tblCmnUser.empCd" oper="like" type="java.lang.String" valueDefault='${theForm}'/>
		        </td>
				<td class="attribute">员工姓名</td>
		        <td>
		        <search:text name="tblCmnUser.empName" oper="like" type="java.lang.String" valueDefault='${theForm}'/>
		        </td>
		    </tr>
		    <tr>
		        <td class="attribute">激活状态</td>
		        <td>
		        	<input name="conditions(tblCmnUser.status).name" type="hidden" value="tblCmnUser.status"/>
		            <input name="conditions(tblCmnUser.status).operator" type="hidden" value="="/>
		            <input name="conditions(tblCmnUser.status).createAlias" type="hidden" value="true"/>
		            <input name="conditions(tblCmnUser.status).type" type="hidden" value="java.lang.String"/>
	                <ec:composite value='${theForm.conditions["tblCmnUser.status"].value}'  valueName = "conditions(tblCmnUser.status).value" textName = "temp.conditions(status).value" source = "${theForm.statusComboList}" />
		        </td>
		        <td class="attribute">部门名称</td>
		        <td>
		            <search:text name="tblCmnUser.tblCmnDept.deptName" oper="like" type="java.lang.String" valueDefault='${theForm}'/>
		        </td>
		    </tr>
		    	</table>
	    <div class="query_button">
	        <input type="button" value="" name="" class="opera_query" title="点击查询" onClick="CurrentPage.myquery();">
	    </div>
	</div>
	<div class="update_subhead">
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td>
					 <span class="switch_open" onClick="StyleControl.switchDiv(this,$('divId_requQues'))" title="点击收缩表格">角色用户信息列表</span>
				</td>
				
			</tr>
		</table>
	</div>
	<div id="divId_requQues" class="list_scroll">
		<table border="0" cellpadding="0" cellspacing="0" class="Listing" id="listtable"   onClick="TableSort.sortColumn(event)">
		<input type="hidden" name="checkall" onClick="CurrentPage.selectedAll()" title="是否全选"/>
			<thead>
				<tr>
					<td nowrap="nowrap"><input id='detailIdsForPrintAll' type='checkbox' onclick="FormUtils.checkAll(this,document.getElementsByName('detailIds'))" title="是否全选"/></td>
					<td>员工编号</td>
					<td>员工姓名</td>
					<td>激活状态</td>
					<td>部门路径</td>
					<td>操作</td>	
				</tr>
			</thead>
			<tbody>
				<c:forEach items = "${theForm.list}" var = "item" varStatus = "status" >
					<tr>
						<td align="center">
						<input type="checkbox" name="detailIds" onclick="FormUtils.check($('detailIdsForPrintAll'),document.getElementsByName('detailIds'))" value="<c:out value="${item.id}"/>" />
						</td>
						<td>
							<c:out value="${item.tblCmnUser.empCd}"/>
						</td>
						<td>
							<c:out  value="${item.tblCmnUser.empName}"/>
						</td>	
						<td>
							<c:out  value="${item.tblCmnUser.statusName}"/>
						</td>	
						<td>
						
						</td>
						<td width="5%">
							<input type="button" class="list_delete" onClick="CurrentPage.removeUser(this)" id="<c:out value="${item.id}"/>" row="<c:out value="${item.tblCmnUser.empName}"/>" style="display:" name="delBtn" title="删除"/>
						</td>
					</tr>
				</c:forEach>					
			</tbody>
		</table>
<div class="list_bottom">
<c:set var="paginater.forwardUrl" value="/sm/RoleAction.do?step=userShow" scope="page" /> 
	<%@ include file="/decorators/paginater.jspf"%><input name="" type="button" class="opera_batchdelete" value="批量删除" onClick="CurrentPage.deleteUserAll(); return false"/>
</div>
	</div>
</form>
</body>
</html>
