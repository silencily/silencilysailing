<%-- jsp1.2 --%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf"/>

<%--
    ����ʹ�� JSP1.2 specification, ���� Sun J2EE 5 ˵��, ������֤�������� server �±�ʶ��
    ������������쳣, ���� "jsp1.2"����, ʹ�� jsp1.1 ����
--%>

<%-- jsp1.2 --%>

<html>
<body class="list_body">
<div class="update_subhead" >
    <span class="switch_open" onClick="StyleControl.switchDiv(this, $('userInfo'))" title="">��ǰ�û���Ϣ</span>
</div>
<div id="userInfo" style="display">
	<table width="100%"  border="0" cellpadding="0" cellspacing="0" class="Detail" style="display:">
	    <tr>
			<td class="attribute">Ա�����</td>
	        <td>
	           <input name="empCd" value="<c:out value='${theForm.bean.empCd}'/>"
				class="readonly" readonly="readonly" />
	        </td>
			<td class="attribute">Ա������</td>
	        <td>
	        <input name="empName" value="<c:out value='${theForm.bean.empName}'/>"
				class="readonly" readonly="readonly" />
	        </td>
	    </tr>
	     	</table>
</div>
<form action="" id="f">
<input type="hidden" name="oid" value="<c:out value = '${theForm.oid}' />" />
<input type="hidden" name="step" value="<c:out value = '${theForm.step}' />" />
<input type="hidden" name="strs"/>
<input type="hidden" name="oids"/>
<div class="update_subhead">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td>
			<span class="switch_open" onClick="StyleControl.switchDiv(this, $('divId_scrollLing'))" title="����������">�������</span>
		</td>
		
	</tr>
</table>
</div>
<div id="divId_scrollLing" class="list_scroll">
		<table border="0" cellpadding="0" cellspacing="0" class="Listing" id="listtable" ondblclick="CurrentPage.updatePermission(event)" onClick="TableSort.sortColumn(event)">
		<input type="hidden" name="checkall" onClick="CurrentPage.selectedAll()" title="�Ƿ�ȫѡ"/>
	<thead>
				<tr id="tableHead">
					<td nowrap="nowrap"><input id='detailIdsForPrintAll' type='checkbox' onClick="FormUtils.checkAll(this,document.getElementsByName('detailIds'))" /></td>
					<td nowrap="nowrap">��ѯ��Χ</td>	
					<td nowrap="nowrap">�޸ķ�Χ</td>
					<td nowrap="nowrap">������Χ</td>
					<td nowrap="nowrap">ɾ����Χ</td>
					<td nowrap="nowrap">ʵ���Ա</td>
					<td nowrap="nowrap">����</td>
					
				</tr>
			</thead>
			<tbody id='tablist'>
				<c:forEach var="item" items="${theForm.dataPermission}" varStatus="status">
					<tr>
						<td align="center">
							<input type="checkbox"   name="detailIds" onclick="FormUtils.check($('detailIdsForPrintAll'),document.getElementsByName('detailIds'))" value="<c:out value="${item.id}"/>" />
						</td>
						<td nowrap="nowrap" align='left'>
							<c:out  value="${item.searchScope}"/>&nbsp;
						</td>
						<td nowrap="nowrap" align='left'>
							<c:out  value="${item.updateScope}"/>&nbsp;
						</td>
						<td nowrap="nowrap" align='left'>
							<c:out  value="${item.createScope}"/>&nbsp;
						</td> 
						<td nowrap="nowrap" align='left'>
							<c:out  value="${item.deleteScope}"/>&nbsp;
						</td> 
						<td nowrap="nowrap" align='left'>
							<c:out  value="${item.tblCmnEntityMember.name}"/>&nbsp;
						</td> 
						<td nowrap="nowrap" align='center'>
							<input type="button" class="list_update" onClick="CurrentPage.updatePermission(event)" id="<c:out value="${item.id}"/>" style="display:" name="addBtn" title="�޸�"/>
							<input type="button" class="list_delete" onClick="CurrentPage.removePermission(this)" id="<c:out value="${item.id}"/>" style="display:" name="delBtn" title="ɾ��"/>
						</td>
					</tr>
				</c:forEach>					
			</tbody>
		</table>
<div class="list_bottom"><c:set var="paginater.forwardUrl" value="/sm/userManageAction.do?step=dataPermission" scope="page" /> 
	<%@ include file="/decorators/paginater.jspf"%>
			<input type="button" class="opera_export" title="����Excel�ļ�" onClick="Print.exportExcel($('divId_scrollLing'))" value=""/>
		   <input name="" type="button" class="opera_batchdelete"  value="����ɾ��" onClick="CurrentPage.deletePermissionsAll(); return false"/>
	    
</div>
	</div>
</form>
<script type="text/javascript">
var msgInfo_ = new msgInfo();
var CurrentPage = {};

CurrentPage.deletePermissionsAll= function() {			
		var str="";
		var dd=document.getElementsByName('detailIds');
		for(var i=0;i<dd.length;i++){
			if(dd[i].checked==true){
				var oid1=dd[i].value;
				str+=oid1+"$";
			}
		}
		if (str==""||str==null){
			alert(msgInfo_.getMsgById('SM_I014_A_0'));
		}else{
			if (!confirm(msgInfo_.getMsgById('SM_I015_A_0'))) {
			    return false;
			}
			$('step').value = 'deletePermissionsAll';
			$('oids').value = str;
	   		FormUtils.post(document.forms[0], '<c:url value="/sm/userManageAction.do"/>' );
   		}
   }
   
   	CurrentPage.myquery = function() {
	if (document.getElementsByName("paginater.page") != null) {
	    document.getElementsByName("paginater.page").value = 0;
	}
	FormUtils.post(document.forms[0], '<c:url value="/sm/userManageAction.do?step=dataPermission"/>');
	}
   
   CurrentPage.removePermission = function(sel) {
	if (! confirm(msgInfo_.getMsgById('SM_I009_A_0'))) {
		return;
	}
	var oid1=sel.id;
	$('step').value = 'removeDataPermission';
	FormUtils.post(document.forms[0], '<c:url value = "/sm/userManageAction.do?oid1=" />' + oid1 );
}
   var boxObj;
   CurrentPage.updatePermission = function(e) {
   	var tmp, trObj;
	if (TableSort.global_isMSIE5) {
		tmp = e.srcElement;
	} else if (TableSort.global_isByTagName) {
		tmp = e.target;
	}
	trObj = TableSort.getParentTagName(tmp, "TR");
	if(trObj.id=="tableHead")
	{
		return false;
	}
	if(boxObj!=undefined){
		boxObj.checked = false;
	}
	boxObj = trObj.cells[0].childNodes[0];
	var oid2=boxObj.value;
	boxObj.checked = true;
	boxObj.onfocus();
	definedWin.openListingUrl('permissionUpdate','<c:url value = "/sm/userManageAction.do?step=userPermissionDetailed&oid2=" />' + oid2);
	//window.open('<c:url value = "/sm/userManageAction.do?step=userPermissionDetailed&oid2=" />' + oid2, "newwindow", "height=500, width=800, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no");
}
   TableSort.dblClick = function()
   {
   	   return false;
   }
   
<c:if test="${theForm.oid !=''}">
CurrentPage.create = function (oid) {
   	var ids=new Array();
   	var userId = $('oid').value;
   	//definedWin.openModalWindow('permissionAdd','<c:url value = "/sm/DataMemberAction.do?step=entry&userId=" />' + userId);
   	definedWin.openListingUrl('permissionAdd','<c:url value = "/sm/DataMemberAction.do?step=entry&userId=" />' + userId);
/*	ids=window.showModalDialog('<c:url value = "/sm/permissionAction.do?step=selEntry" />',"","status:yes;center:yes;dialogHeight:600px;dialogWidth:900px;help:no");
	var strs="";
	if ( ids != null) {
		var len=ids.length;
		for(var i=0;i<len;i++)
		{	if(ids[i]!=null||ids[i]!=""){
			strs+=ids[i] + "$";
			}
		}
		$('step').value = 'selectPermission';
		$('strs').value = strs;
		FormUtils.post(document.forms[0], '<c:url value="/sm/userManageAction.do"/>');
	}
	*/
}
</c:if>
	top.openWinCallBack = function(arr){
		top.openWinCallBack = null;
		var userId = $('oid').value;
			FormUtils.post(document.forms[0], '<c:url value="/sm/userManageAction.do?step=dataPermission&oid="/>'+userId);
	}

</script>
</body>
</html>



