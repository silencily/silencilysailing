<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf" />
<html>
	<body>
<script language="javascript">
	if (CurrentPage == null) {
    	var CurrentPage = {};
		} 
	CurrentPage.query= function () {
	 var strReVal = document.getElementsByName('menuRole');
		if (document.getElementsByName("paginater.page") != null) {
	  	  document.getElementsByName("paginater.page").value = 0;
		}
		FormUtils.post(document.forms[0], '<c:url value='/sm/RoleAction.do?step=selectRoleSearch'/>');
	}
</script>
<div class="main_title">
<div>�û�ѡ��</div>
</div>
<form name="form">
<input type="hidden" name="step" value="<c:out value = "${theForm.step}" />" />	
<div class="update_subhead">
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td><span class="switch_open" onClick="StyleControl.switchDiv(this,$('divId_scrollLing'))" title="����������">�û��б�</span></td>
		</tr>
	</table>
</div>
<div id="divId_scrollLing" class="list_scroll">
		<table border="0" cellpadding="0" cellspacing="0" class="Listing" id="listtable" onClick="TableSort.sortColumn(event)">
	<thead>
			<tr>
			<td field=id>&nbsp;</td>
			<td field=empCd>Ա�����</td>
			<td field="empName">Ա������</td>
			<td field="statusName">����״̬</td>
			<td field="deptRoute">����·��</td>
		</tr>
		</thead>
		<tbody>
		<c:forEach var="item" items="${theForm.user}" varStatus="status">
			<tr>
				<td align="center"><input type="radio" name="oid"  value="<c:out value = "${item.emp.id}" />" /></td>
				<td nowrap="nowrap" align='left'>
					<c:out value = "${item.emp.empCd}"/>&nbsp;
				</td>
				<td nowrap="nowrap" align='left'>
					<c:out value = "${item.emp.empName}" />&nbsp;
				</td>
				<td nowrap="nowrap" align='left'>
					<c:out value = "${item.statusName}" />&nbsp;
				</td>
				<td nowrap="nowrap" align='left'>
					<c:out value = "${item.deptRoute}" />&nbsp;
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
<div class="list_bottom"><c:set var="paginater.forwardUrl" value="/sm/userManageAction.do?step=selectCurDutyper" scope="page" /> 
	<%@ include file="/decorators/paginater.jspf"%>
</div>
</div>

<script type="text/javascript">	
	if (CurrentPage == null) {
	    var CurrentPage = {};
	}    
	var listObject = new Object();
	//definedWin����÷���
	CurrentPage.onLoadSelect = function(){
		//���б����
		listObject = new ListUtil.Listing('listObject', 'listtable');
		listObject.init();
		//����definedWin��ʵ��
		//ѡ��
		top.definedWin.selectListing = function(inum) {
			listObject.selectWindow(1);
		}
	
		//�ر�
		top.definedWin.closeListing = function(inum) {
			listObject.selectWindow();
		}
	}
	CurrentPage.onLoadSelect();

	CurrentPage.validation = function () {
		return Validator.Validate(document.forms[0], 4);
	}
</script>
</form>
</body>
</html>
