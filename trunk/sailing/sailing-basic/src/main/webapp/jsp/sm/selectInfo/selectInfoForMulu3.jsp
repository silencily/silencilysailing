<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf"/>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK"/>
</head> 
<script type="text/javascript">
	if (CurrentPage == null) {
	    var CurrentPage = {};
	}
	
CurrentPage.query = function() {
	if (document.getElementsByName("paginater.page") != null) {
	    document.getElementsByName("paginater.page")[0].value = 0;
	}
	FormUtils.post(document.forms[0], '<c:url value='/sm/RoleAction.do?step=roleSearch&selectInfo=selectInfo'/>'); 
}	
</script> 
<body>
<form name="form">
<input type="hidden" name="parentCode" id="parentCode" value="<c:out value = "${theForm.parentCode}"/>"/>
<div class="update_subhead" >
	    <span class="switch_close" onClick="StyleControl.switchDiv(this, $('supplierQuery'))" title="���������в�ѯ">��ѯ����</span>
	</div>
	<div id="supplierQuery" style="display:none">
		<table width="100%"  border="0" cellpadding="0" cellspacing="0" class="Detail" id="divId_flaw_search_common" onClick="TableSort.sortColumnOnly(event)">
		    <tr>
			    <td class="attribute">��ʾ����</td>
		        <td>
		        	<search:text name="display_Name" oper="like" type="java.lang.String" valueDefault='${theForm}'/>
		        </td>
		        <td class="attribute">��ɫ��ʶ</td>
		        <td>
		       		<search:text name="role_Cd" oper="like" type="java.lang.String" valueDefault='${theForm}'/>
		        </td>
		    </tr>
		    <tr>
		    	<td class="attribute" >��ɫ����Ŀ¼</td>
			    <td>
		        	<input name="searchFatherName" value="<c:out value='${theForm.searchFatherName}'/>"/>
			    </td>
				<td  class="attribute" >ϵͳ��ɫ</td>
				<td>
		        	<input name="conditions(systemRole).name" type="hidden" value="system_Role"/>
		            <input name="conditions(systemRole).operator" type="hidden" value="="/>
		            <input name="conditions(systemRole).createAlias" type="hidden" value="false"/>
		            <input name="conditions(systemRole).type" type="hidden" value="java.lang.String"/>
	                <ec:composite value='${theForm.conditions["systemRole"].value}'  valueName = "conditions(systemRole).value" textName = "temp.conditions(systemPermission).value" source = "${theForm.systemRoleComboList}"/>
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
        <span class="switch_open" onClick="StyleControl.switchDiv(this, $('listTable'))" title="����������">
           ��ɫ�б�
        </span>
      </td>
    </tr>
  </table>
</div>
<div id="list">
<div id="divId_scrollLing" class="list_scroll">
  <table border="0" cellpadding="0" cellspacing="0" class="Listing" id="listTable" onClick="TableSort.sortColumn(event)">
    <input type="hidden" name="checkall" onClick="CurrentPage.selectedAll()" title="�Ƿ�ȫѡ"/>
    <thead>
      <tr>
        <td><input id='detailIdsForPrintAll' type='checkbox' onclick="FormUtils.checkAll(this,document.getElementsByName('oldOid'))" title="�Ƿ�ȫѡ"/></td>
			<td nowrap="nowrap">��ɫ��ʶ</td>
			<td nowrap="nowrap">��ʾ����</td>
			<td nowrap="nowrap">��ɫ·��</td>
			<td nowrap="nowrap">ϵͳ��ɫ</td>
			<td nowrap="nowrap">˵��</td>
      </tr>
    </thead>
    <tbody>
               <c:forEach items = "${theForm.rolList}" var = "item" varStatus = "status" >
				<tr id="requQues_trId_[<c:out value='${status.index}'/>]" >
				
					<td align="center">
					<input type="hidden" id="tools_<c:out value='${status.index}'/>" value="<c:out value='${item.id}' />" />
		            <input type="checkbox" name="oldOid"  value="<c:out value = "${item.id}" />" /> 
					</td>				
				 	 <td>
						<c:out  value="${item.roleCd}"/>&nbsp;
					</td>
					<td>
						<input type="hidden" id="ojbName<c:out value='${status.index}'/>" value="<c:out value = "${item.name}" />" />
						<span onClick="javascript:definedWin.openListingUrl('rolePerUser', '<c:url value = '/sm/userManageAction.do?step=rolePerUserEntry&selectPage=selectPage&rowId=${item.id}' />',null,true)" class="font_link"><c:out  value="${item.name}"/>&nbsp;
					</td>
					<td>
						<c:out  value="${item.roleRoute}"/>&nbsp;
					</td>	
					<td>
						<c:out  value="${item.systemRoleName}"/>&nbsp;
					</td>
					<td>
						<c:out  value="${item.note}"/>&nbsp;
					</td>
				</tr>
	        </c:forEach>
    </tbody>
  </table>
</div>
<div class="list_bottom">
<c:choose>
<c:when test="${param.step=='selectInfo2'|| param.step=='selectForRole'}">
	<c:set var="paginater.forwardUrl" value="/sm/RoleAction.do?step=selectForRole&mulu=mulu" scope="page" /> 
</c:when>
<c:otherwise>
	<c:set var="paginater.forwardUrl" value="/sm/RoleAction.do?step=roleSearch&selectInfo=selectInfo" scope="page" /> 
</c:otherwise>
</c:choose>
	<%@ include file="/decorators/paginater.jspf"%>
</div>
</div>
</form>
<script type="text/javascript">  
</script>
</body>
</html>

