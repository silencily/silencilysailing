<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf" />
<html>
<body>
<script language="javascript">
	if (CurrentPage == null) {
    	var CurrentPage = {};
	} 
	 CurrentPage.myquery= function () {
		if (document.getElementsByName("paginater.page") != null) {
	  	  document.getElementsByName("paginater.page").value = 0;
		}		 
		FormUtils.post(document.forms[0], '<c:url value='/sm/permissionAction.do?step=permissionUser'/>');
	}	   
   TableSort.dblClick = function()
   {
   	   return false;
   }
</script>
<form name="f">
<input type="hidden" name="oid" value="<c:out value='${theForm.oid}'/>"/>
<input type="hidden" name="parentCode" value="<c:out value = "${theForm.parentCode}"/>"/>
	<div class="update_subhead">
		<span class="switch_open" onClick="StyleControl.switchDiv(this, $('currentPermission'))" title="">��ǰȨ����Ϣ</span>
	</div>		
	<div id="currentPermission" style="display">
		<table border="0" cellpadding="0" cellspacing="0" class="Detail" id="parenttable" style="display:">
			<tr>
				<td  class="attribute" >Ȩ������</td>
				<td ><input type="text" name="tempDisplayname" value="<c:out value = '${theForm.bean.displayname}'/>" disabled="disabled"/></td>
				<td  class="attribute" >Ȩ������</td>
				<td ><input type="text" name="tempNodetype" value="<c:out value='${theForm.bean.nodeTypeName}'/>"  disabled="disabled"/></td>
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
	           <search:text name="tblCmnUserByUserId.empCd" oper="like" type="java.lang.String" valueDefault='${theForm}'/>
	        </td>
			<td class="attribute">Ա������</td>
	        <td>
	        <search:text name="tblCmnUserByUserId.empName" oper="like" type="java.lang.String" valueDefault='${theForm}'/>
	        </td>
	    </tr>
	    <tr>
	        <td class="attribute">����״̬</td>
	        <td>
	        	<input name="conditions(tblCmnUserByUserId.status).name" type="hidden" value="tblCmnUserByUserId.status"/>
	            <input name="conditions(tblCmnUserByUserId.status).operator" type="hidden" value="="/>
	            <input name="conditions(tblCmnUserByUserId.status).createAlias" type="hidden" value="true"/>
	            <input name="conditions(tblCmnUserByUserId.status).type" type="hidden" value="java.lang.String"/>
                <ec:composite value='${theForm.conditions["tblCmnUserByUserId.status"].value}'  valueName = "conditions(tblCmnUserByUserId.status).value" textName = "temp.conditions(status).value" source = "${theForm.statusComboList}" />
	        </td>
	        <td class="attribute">��������</td>
	        <td>
	            <search:text name="tblCmnUserByUserId.tblCmnDept.deptName" oper="like" type="java.lang.String" valueDefault='${theForm}'/>
	        </td>
		    </tr>
		  </table>
	    <div class="query_button">
	        <input type="button" class="opera_query" title="�����ѯ" onClick="CurrentPage.myquery();">
	    </div>
	</div>
	<div class="update_subhead">
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td>
					 <span class="switch_open" onClick="StyleControl.switchDiv(this,$('divId_scrollLing'))" title="����������">Ȩ����ؽ�ɫ�б�</span>
				</td>
				
			</tr>
		</table>
	</div>
	<div id="divId_scrollLing" class="list_scroll">
		<table border="0" cellpadding="0" cellspacing="0" class="Listing" id="listtable" onClick="TableSort.sortColumn(event)">
	<thead>
				<tr>
					<td>Ա�����</td>
					<td>Ա������</td>
					<td>����״̬</td>
					<td>����·��</td>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="item" items="${theForm.perUser}" varStatus="status">
					<tr>
						<td>
							<c:out value="${item.tblCmnUserByUserId.empCd}"/>
						</td>
						<td>
							<c:out  value="${item.tblCmnUserByUserId.empName}"/>
						</td>	
						<td>
							<c:out  value="${item.tblCmnUserByUserId.statusName}"/>
						</td>	
						<td>
							<c:out  value="${item.tblCmnUserByUserId.deptRoute}"/>
						</td>
					</tr>
				</c:forEach>					
			</tbody>
		</table>
<div class="list_bottom"><c:set var="paginater.forwardUrl" value="/sm/permissionAction.do?step=permissionUser" scope="page" /> 
	<%@ include file="/decorators/paginater.jspf"%>
</div>
</div>
</form>
</body>
</html>
