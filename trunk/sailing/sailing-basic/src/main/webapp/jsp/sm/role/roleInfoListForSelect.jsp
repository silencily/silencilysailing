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
<form name="f">
<input type="hidden" name="parentCode" id="parentCode" value="<c:out value = "${theForm.parentCode}"/>"/>
<input type="hidden" name="step" value="<c:out value = '${theForm.step}' />" />
	<div class="update_subhead" >
	    <span class="switch_close" onClick="StyleControl.switchDiv(this, $('supplierQuery'))" title="��������ڵ�">��ѯ����</span>
	</div>
	<div id="supplierQuery" style="display:none">
		<table width="100%"  border="0" cellpadding="0" cellspacing="0" class="Detail" id="divId_flaw_search_common" style="display:">
		    <tr>
		        <td class="attribute">��ɫ��ʶ</td>
		        <td>
		        	<search:text name="role_Cd" oper="like" type="java.lang.String" valueDefault='${theForm}'/>
		        </td>
		        <td class="attribute">��ʾ����</td>
		        <td>
		            <search:text name="display_Name" oper="like" type="java.lang.String" valueDefault='${theForm}'/>
		        </td>
		    </tr>
		    <tr>
				<td class="attribute">��ɫ����Ŀ¼</td>
		        <td>
		       		<input name="searchFatherName" value="<c:out value='${theForm.searchFatherName}'/>"/>
		        </td>
		    
				<td  class="attribute" >ϵͳ��ɫ</td>
				<td>
		        	<input name="conditions(systemRole).name" type="hidden" value="system_Role"/>
		            <input name="conditions(systemRole).operator" type="hidden" value="="/>
		            <input name="conditions(systemRole).createAlias" type="hidden" value="false"/>
		            <input name="conditions(systemRole).type" type="hidden" value="java.lang.String"/>
	                <ec:composite value='${theForm.conditions["systemRole"].value}'  valueName = "conditions(systemRole).value" textName = "temp.conditions(systemPermission).value" source = "${theForm.systemRoleComboList}" />
				</td>
		    </tr>
		    	</table>
	    <div class="query_button">
	        <input type="button" value="" name="" class="opera_query" title="�����ѯ" onClick="CurrentPage.query();">
	    </div>
	</div>
	<div class="update_subhead">
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td>
					 <span class="switch_open" onClick="StyleControl.switchDiv(this,$('divId_scrollLing'))" title="��������ڵ�">��ɫ�б�</span>
				</td>
			</tr>
		</table>
	</div>
<div id="divId_scrollLing" class="list_scroll">
	<table border="0" cellpadding="0" cellspacing="0" class="Listing" id="roleInfoTable" onClick="TableSort.sortColumn(event)">
		<thead>
		<tr>
			<td field=id>&nbsp;</td>
			<td field=roleCd>��ɫ��ʶ</td>
			<td field=roleName>��ɫ����</td>
			<td field=roleRoute>��ɫ·��</td>
			<td field=systemRoleName>ϵͳ��ɫ</td>
			<td field=note>˵��</td>
		</tr>
		</thead>
		<tbody>
		<c:forEach var="item" items="${theForm.rolList}" varStatus="status">
			<tr>
				<td align="center"><input type="radio" name="oid"  value="<c:out value = "${item.id}" />" /></td>
				<td><c:out value = "${item.roleCd}" />&nbsp;</td>
				<td><c:out value = "${item.name}" />&nbsp;</td>
				<td><c:out value = "${item.roleRoute}" />&nbsp;</td>
				<td><c:out value = "${item.systemRoleName}" />&nbsp;</td>
				<td><c:out value = "${item.note}" />&nbsp;</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
<div class="list_bottom">
<c:choose>
<c:when test="${param.step=='selectRoleInfoList'}">
	<c:set var="paginater.forwardUrl" value="/sm/RoleAction.do?step=selectRoleInfoList" scope="page" /> 
</c:when>
<c:otherwise>
	<c:set var="paginater.forwardUrl" value="/sm/RoleAction.do?step=selectRoleSearch" scope="page" /> 
</c:otherwise>
</c:choose>
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
		listObject = new ListUtil.Listing('listObject', 'roleInfoTable');
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
