<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf" />
<html>
	<body>
		<script language="javascript">
			if (CurrentPage == null) {
    	var CurrentPage = {};
		}   

	CurrentPage.deletePermissAll= function(sel) {
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
				alert("��ѡ��Ȩ����");
			}else{
				if (!confirm('�Ƿ�Ҫ�ջ�����ѡ���Ȩ��? ')) {
				    return false;
				}
				$('step').value = 'deletePermission';
		   		FormUtils.post(document.forms[0], '<c:url value="/sm/ConsignManagerAction.do?oid1="/>'+str );
	   		}		
		} else {
			if (! confirm(" �Ƿ�Ҫ�ջظ�Ȩ��? ")) {
				return;
			}
			$('step').value = 'deletePermission';
			FormUtils.post(document.forms[0], '<c:url value = "/sm/ConsignManagerAction.do?oid1=" />' + strTemp );
		}
	 }
		  
	 CurrentPage.updatePermission = function(sel) {
	   var oid2=sel.id;
	   window.open ('<c:url value = "/sm/ConsignManagerAction.do?step=permissionUpdate&oid2=" />' + oid2, "newwindow", "height=600, width=540, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no");
	   
	}
	
	CurrentPage.create = function() {
		var permissionIds="";
		permissionIds = window.showModalDialog('<c:url value = "/sm/permissionAction.do?step=selEntry" />',"","status:yes;center:yes;dialogHeight:600px;dialogWidth:900px");
		FormUtils.post(document.forms[0], '<c:url value = "/sm/ConsignManagerAction.do?step=addpermission&oid1=" />' + permissionIds );
	}	
	
</script>
<form name="f">
<input type="hidden" name="oid"  id= "oid" value="<c:out value='${theForm.oid}'/>"/>
<input type="hidden" name="step" value="<c:out value = '${theForm.step}' />" />
	<div class="update_subhead">
					<span class="switch_open"
						title="">��ǰί���û���Ϣ</span>
	</div>		
	<div id="supplie" style="display:">
		<table border="0" cellpadding="0" cellspacing="0" class="Detail" id="parenttable" style="display: ">
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
					 <span class="switch_open" onClick="StyleControl.switchDiv(this,$('divId_requQues'))" title="����������">��ɫ�û���Ϣ�б�</span>
				</td>
			</tr>
		</table>
	</div>
	<div id="divId_requQues" class="list_scroll">
		<table border="0" cellpadding="0" cellspacing="0" class="Listing" id="listtable" onClick="">
		<input type="hidden" name="checkall" onClick="CurrentPage.selectedAll()" title="�Ƿ�ȫѡ"/>
			<thead>
				<tr>
					<td nowrap="nowrap"><input id='detailIdsForPrintAll' type='checkbox' onClick="FormUtils.checkAll(this,document.getElementsByName('detailIds'))" /></td>
					<td>Ȩ������</td>
					<td>��ʼʱ��</td>
					<td>ʧЧʱ��</td>
					<td>����</td>	
				</tr>
			</thead>
			<tbody>
				<c:forEach items = "${theForm.list}" var = "item" varStatus = "status" >
					<tr>
						<td align="center">
							<input type="checkbox"  name="detailIds" value="<c:out value="${item.id}"/>" />
						</td>
						<td id="<c:out value="${item.id}"/>.displayname">
							<c:out value="${item.tblCmnPermission.displayname}"/>
						</td>
						<td id="<c:out value="${item.id}"/>.beginTime">
							<c:out  value="${item.beginTime}"/>
						</td>	
						<td id="<c:out value="${item.id}"/>.invalidTime">
							<c:out  value="${item.invalidTime}"/>
						</td>	
						<td width="5%">
							<input type="button" class="list_update" onClick="CurrentPage.updatePermission(this)" id="<c:out value="${item.id}"/>" style="display:" name="delBtn" title="�޸�"/>
							<input type="button" class="list_delete" onClick="CurrentPage.deletePermissAll(this)" id="<c:out value="${item.id}"/>" style="display:" name="delBtn" title="�ջ�"/>
						</td>
					</tr>
				</c:forEach>					
			</tbody>
		</table>
	</div>
</form>
</body>
</html>
