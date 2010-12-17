<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf" />
<html>
	<body>
		<script language="javascript">
			if (CurrentPage == null) {
    	var CurrentPage = {};
		}   

   TableSort.dblClick = function()
   {
   	   return false;
   }

	CurrentPage.deleteUserAll= function(sel) {
		var strTemp = sel.id;
		if (strTemp == "") {
			var str="";
			var dd=document.getElementsByName('oid');
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
				alert("��ѡ��ɾ����");
			}else{
				if (!confirm('�Ƿ�ɾ����ί���û�? ')) {
				    return false;
				}
				$('step').value = 'deleteUsersAll';
		   		FormUtils.post(document.forms[0], '<c:url value="/sm/ConsignManagerAction.do?oid1="/>'+str );
	   		}		
		} else {
			if (! confirm(" �Ƿ�ɾ����ѡί���û�? ")) {
				return;
			}
			$('oid1').value = strTemp;
			FormUtils.post(document.forms[0], '<c:url value = "/sm/ConsignManagerAction.do?step=deleteUsersAll" />');
		}
	 }

	CurrentPage.SelectRole = function(sel){
		$('oid').value = sel.id;
		window.parent.panel.click(1);
	}
	
	CurrentPage.create = function() {
/*	var userIds = new Array();
	var strPal = "";
	var iHeight="600";
	var iWidth="850";
	var sFeatures="dialogHeight: " + iHeight + "px;" + "dialogWidth: " + iWidth + "px;" + "help:no;" + "scroll:0;";
	userIds=window.showModalDialog("/qware/jsp/sm/user/windowFrameForConsign.jsp","",sFeatures);	
	if (userIds != null) {
		for(var i =0; i < userIds.length; i++){
			if (userIds[i] != "" && userIds[i] != null){
				strPal = strPal + userIds[i] +";";	
				
			}
		}
		$('oid2').value = strPal;
		FormUtils.post(document.forms[0], '<c:url value = "/sm/ConsignManagerAction.do?step=list" />');
	}
*/
	definedWin.openListingUrl('permissionAdd','<c:url value = "/sm/userManageAction.do?step=selectInfoForConsign&paginater.page=0" />');
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
		FormUtils.post(document.forms[0], '<c:url value = "/sm/ConsignManagerAction.do?step=list" />');
	}
}	
</script>
<form name="f">
<input type="hidden" name="step" value="<c:out value = '${theForm.step}' />" />
<input type="hidden" name="oid1"/>
<input type="hidden" name="oid2"/>
	<div class="update_subhead">
    <span class="switch_open" onClick="StyleControl.switchDiv(this, $('supplierQuery'))" title="">��ǰί���û���Ϣ</span>
	</div>		
	<div id="supplierQuery" style="display:">
		<table width="100%"  border="0" cellpadding="0" cellspacing="0" class="Detail" style="display:">
			<tr>
				<td  class="attribute" >Ա�����</td>
				<td ><input type="text" name="empId" value="<c:out value = '${theForm.userBean.empCd}'/>" disabled="disabled"/></td>
				<td  class="attribute" >Ա������</td>
				<td ><input type="text" name="empName" value="<c:out value='${theForm.userBean.empName}'/>"  disabled="disabled"/></td>
			</tr>
		</table>
	</div>
	

	<div class="update_subhead">
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td>
					 <span class="switch_open" onClick="StyleControl.switchDiv(this,$('divId_requQues'))" title="����������">ί���û���Ϣ�б�</span>
				</td>
			</tr>
		</table>
	</div>
	<div id="divId_requQues" class="list_scroll">
		<table border="0" cellpadding="0" cellspacing="0" class="Listing" id="listtable" onClick="TableSort.sortColumn(event)">
		<input type="hidden" name="checkall" onClick="CurrentPage.selectedAll()" title="�Ƿ�ȫѡ"/>
			<thead>
				<tr>
					<td nowrap="nowrap"><input id='detailIdsForPrintAll' type='checkbox' onclick="FormUtils.checkAll(this,document.getElementsByName('oid'))" title="�Ƿ�ȫѡ"/></td>
					<td>Ա�����</td>
					<td>Ա������</td>
					<td>����״̬</td>
					<td>���֤����</td>
					<td>�Ա�</td>
					<td>����</td>
					<td>��������</td>
					<td>�μӹ���ʱ��</td>
					<td>����</td>	
				</tr>
			</thead>
			<tbody>
				<c:forEach items = "${theForm.list}" var = "item" varStatus = "status" >
					<tr>
						<td align="center">
							<input type="checkbox" name="oid" onclick="FormUtils.check($('detailIdsForPrintAll'),document.getElementsByName('oid'))" value="<c:out value="${item.id}"/>" />
						</td>
						<td>
							<c:out value="${item.empCd}"/>&nbsp;
						</td>
						<td>
							<c:out  value="${item.empName}"/>&nbsp;
						</td>	
						<td>
							<c:out  value="${item.statusName}"/>&nbsp;
						</td>	
						<td>
							<c:out  value="${item.idCard}"/>&nbsp;
						</td>
						<td>
							<c:out  value="${item.sex.name}"/>&nbsp;
						</td>
						<td>
							<c:out  value="${item.nation.name}"/>&nbsp;
						</td>
						<td>
							<c:out  value="${item.tblCmnDept.deptName}"/>&nbsp;
						</td>
						<td>
							item.powerWorkTime&nbsp;
						</td>
						<td width="10%" align="center">
							<input type="button" class="list_delete" onClick="CurrentPage.deleteUserAll(this)" id="<c:out value="${item.id}"/>" style="display:" name="delBtn" title="ɾ��ί���û�"/>
							<input type="button" class="list_detail" onClick="CurrentPage.SelectRole(this)" id="<c:out value="${item.id}"/>" style="display:" name="delBtn" title="ί�н�ɫ"/>
						</td>
					</tr>
				</c:forEach>					
			</tbody>
		</table>
<div class="list_bottom">
	 <input name="" type="button" class="opera_batchdelete"  value="�����ջ�" onClick="CurrentPage.deleteUserAll(this); return false"/>
</div>
</div>
</form>
</body>
</html>
