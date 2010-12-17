<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf"/>
<script type="text/javascript">
var msgInfo_ = new msgInfo();
if (CurrentPage == null) {
	    var CurrentPage = {};
}

CurrentPage.query = function() { 
	if (document.getElementsByName("paginater.page") != null) {
	    document.getElementsByName("paginater.page").value = 0;
	}
	FormUtils.post(document.forms[0], '<c:url value='/sm/RoleAction.do?step=selectMultipleRoleSearch'/>'); 
}
TableSort.dblClick = function()
   {
   	   return false;
   }
/*
CurrentPage.selectAll= function() {			

		var ids=new Array();
		var dd=document.getElementsByName('detailIds');
		for(var i=0;i<dd.length;i++){
			if(dd[i].checked==true){
			ids[i]=dd[i].value;
			}
		}
		if (ids.length==0){
			alert(msgInfo_.getMsgById('SM_I008_A_0'));
		}else{
		
			window.returnValue=ids;	
			window.close();	
			}
	}
*/	
definedWin.closeListing  = function(){
	definedWin.closeModalWindow();
}

top.definedWin.selectListing = function(){
	var arr = new Array();
	var sId ="";
	var rolName="";
	var strFlg = document.getElementById("strFlg").value;
	var dd=document.getElementsByName('detailIds');
	for(var i=0;i<dd.length;i++){
		if(dd[i].checked==true){
		  	if (strFlg == "flow"){
	  			var ss = dd[i].row.split(";");
	  			// alert(ss[0]);
	  			sId += ss[0] + ";";
	  			rolName += ss[1] +";";
	  		}else{	  		
	  			arr[i]=dd[i].value;
	  		}
		}
	}
	if(strFlg == "flow"){
		arr[0] = sId;
		arr[1] = rolName;
	}
	if (arr.length==0){
		alert(msgInfo_.getMsgById('SM_I008_A_0'));
	}else{	
		top.definedWin.listObject(arr);
	}
}	
</script>
<html> 
<body>
<form name="form">
<input type="hidden" name="parentCode" id="parentCode" value="<c:out value = "${theForm.parentCode}"/>"/>
<input type="hidden" name="strFlg" value="<c:out value = "${theForm.strFlg}" />" />	
<input type="hidden" name="step" value="<c:out value = "${theForm.step}" />" />	
<div class="update_subhead" >
	    <span class="switch_close" onClick="StyleControl.switchDiv(this, $('supplierQuery'))" title="点击伸缩节点">查询条件</span>
	</div>
	<div id="supplierQuery" style="display:none">
		<table width="100%"  border="0" cellpadding="0" cellspacing="0" class="Detail" id="divId_flaw_search_common" style="display:">
		    <tr>
		        <td class="attribute">角色标识</td>
		        <td>
		        	<search:text name="role_Cd" oper="like" type="java.lang.String" valueDefault='${theForm}'/>
		        </td>
		        <td class="attribute">显示名称</td>
		        <td>
		            <search:text name="display_Name" oper="like" type="java.lang.String" valueDefault='${theForm}'/>
		        </td>
		    </tr>
		    <tr>
				<td class="attribute">角色所在目录</td>
		        <td>
		       		<input name="searchFatherName" value="<c:out value='${theForm.searchFatherName}'/>"/>
		        </td>
		    
				<td  class="attribute" >系统角色</td>
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
	        <input type="button" value="" name="" class="opera_query" title="点击查询" onClick="CurrentPage.query();">
	    </div>
	</div>

	<div class="update_subhead">
  <table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
      <td>
        <span class="switch_open" onClick="StyleControl.switchDiv(this, $('listTable'))" title="点击收缩表格">
           角色列表
        </span>
      </td>
    </tr>
  </table>
</div>
<div id="list">
<div id="divId_scrollLing" class="list_scroll">
  <table border="0" cellpadding="0" cellspacing="0" class="Listing" id="listTable" onClick="TableSort.sortColumn(event)">
    <input type="hidden" name="checkall" onClick="CurrentPage.selectedAll()" title="是否全选"/>
    <thead>
      <tr>
        <td><input id='detailIdsForPrintAll' type='checkbox' onclick="FormUtils.checkAll(this,document.getElementsByName('detailIds'))" title="是否全选"/></td>
			<td nowrap="nowrap">角色标识</td>
			<td nowrap="nowrap">显示名称</td>
			<td nowrap="nowrap">角色路径</td>
			<td nowrap="nowrap">系统角色</td>
			<td nowrap="nowrap">说明</td>
      </tr>
    </thead>
    <tbody>
               <c:forEach items = "${theForm.rolList}" var = "item" varStatus = "status" >
				<tr id="requQues_trId_[<c:out value='${status.index}'/>]" >
				
					<td align="center">
					<input type="hidden" id="tools_<c:out value='${status.index}'/>" value="<c:out value='${item.id}' />" />
		            <input type="checkbox" name="detailIds" onclick="FormUtils.check($('detailIdsForPrintAll'),document.getElementsByName('detailIds'))"  value="<c:out value = "${item.id}" />" row="<c:out value="${item.id}"/>;<c:out value="${item.name}"/>"/> 
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
   <c:when test="${param.step=='selectMultipleRoleList'}">
	  <c:set var="paginater.forwardUrl" value="/sm/RoleAction.do?step=selectMultipleRoleList" scope="page" /> 
   </c:when>
   <c:otherwise>
	  <c:set var="paginater.forwardUrl" value="/sm/RoleAction.do?step=selectMultipleRoleSearch" scope="page" /> 
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


