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
				alert("��ѡ��ɾ����");
			}else{
				if (!confirm('�Ƿ�Ҫɾ������ѡ����û���Ϣ? ')) {
				    return false;
				}
				$('step').value = 'deleteUsersAll';
				$('oids').value = str;
		   		FormUtils.post(document.forms[0], '<c:url value="/sm/RoleAction.do"/>');
	   		}
	   }
		  
	  CurrentPage.removeUser = function(sel) {
	  	var userName = sel.row;
		if (! confirm(" �Ƿ�Ҫɾ���û�["+userName+"]��Ϣ? ")) {
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
					<span class="switch_open"  onClick="StyleControl.switchDiv(this, $('supplie'))" title="����������">��ǰ��ɫ��Ϣ</span>
	</div>		
	<div id="supplie" style="display:">
		<table border="0" cellpadding="0" cellspacing="0" class="Detail" id="parenttable" style="display: ">
			<tr>
				<td  class="attribute" >��ɫ��ʶ</td>
				<td ><input type="text" name="roleCd" value="<c:out value = '${theForm.roleBean.roleCd}'/>" disabled="disabled"/></td>
				<td  class="attribute" >��ɫ����</td>
				<td ><input type="text" name="name" value="<c:out value='${theForm.roleBean.name}'/>"  disabled="disabled"/></td>
			</tr>
		</table>
	</div>
	
	<div class="update_subhead" >
	    <span class="switch_close" onClick="StyleControl.switchDiv(this, $('supplierQuery'))" title="���������в�ѯ">��ѯ����</span>
	</div>
	<div id="supplierQuery" style="display:none">
		<table width="100%"  border="0" cellpadding="0" cellspacing="0" class="Detail" id="divId_flaw_search_common" style="display:">
		    <tr>
				<td class="attribute">Ա�����</td>
		        <td>
		           <search:text name="tblCmnUser.empCd" oper="like" type="java.lang.String" valueDefault='${theForm}'/>
		        </td>
				<td class="attribute">Ա������</td>
		        <td>
		        <search:text name="tblCmnUser.empName" oper="like" type="java.lang.String" valueDefault='${theForm}'/>
		        </td>
		    </tr>
		    <tr>
		        <td class="attribute">����״̬</td>
		        <td>
		        	<input name="conditions(tblCmnUser.status).name" type="hidden" value="tblCmnUser.status"/>
		            <input name="conditions(tblCmnUser.status).operator" type="hidden" value="="/>
		            <input name="conditions(tblCmnUser.status).createAlias" type="hidden" value="true"/>
		            <input name="conditions(tblCmnUser.status).type" type="hidden" value="java.lang.String"/>
	                <ec:composite value='${theForm.conditions["tblCmnUser.status"].value}'  valueName = "conditions(tblCmnUser.status).value" textName = "temp.conditions(status).value" source = "${theForm.statusComboList}" />
		        </td>
		        <td class="attribute">��������</td>
		        <td>
		            <search:text name="tblCmnUser.tblCmnDept.deptName" oper="like" type="java.lang.String" valueDefault='${theForm}'/>
		        </td>
		    </tr>
		    	</table>
	    <div class="query_button">
	        <input type="button" value="" name="" class="opera_query" title="�����ѯ" onClick="CurrentPage.myquery();">
	    </div>
	</div>
	<div class="update_subhead">
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td>
					 <span class="switch_open" onClick="StyleControl.switchDiv(this,$('divId_requQues'))" title="����������">��ɫ�û���Ϣ�б�</span>
				</td>
				
			</tr>
		</table>
	</div>
	<div id="divId_requQues" class="list_scroll">
		<table border="0" cellpadding="0" cellspacing="0" class="Listing" id="listtable"   onClick="TableSort.sortColumn(event)">
		<input type="hidden" name="checkall" onClick="CurrentPage.selectedAll()" title="�Ƿ�ȫѡ"/>
			<thead>
				<tr>
					<td nowrap="nowrap"><input id='detailIdsForPrintAll' type='checkbox' onclick="FormUtils.checkAll(this,document.getElementsByName('detailIds'))" title="�Ƿ�ȫѡ"/></td>
					<td>Ա�����</td>
					<td>Ա������</td>
					<td>����״̬</td>
					<td>����·��</td>
					<td>����</td>	
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
							<input type="button" class="list_delete" onClick="CurrentPage.removeUser(this)" id="<c:out value="${item.id}"/>" row="<c:out value="${item.tblCmnUser.empName}"/>" style="display:" name="delBtn" title="ɾ��"/>
						</td>
					</tr>
				</c:forEach>					
			</tbody>
		</table>
<div class="list_bottom">
<c:set var="paginater.forwardUrl" value="/sm/RoleAction.do?step=userShow" scope="page" /> 
	<%@ include file="/decorators/paginater.jspf"%><input name="" type="button" class="opera_batchdelete" value="����ɾ��" onClick="CurrentPage.deleteUserAll(); return false"/>
</div>
	</div>
</form>
</body>
</html>
