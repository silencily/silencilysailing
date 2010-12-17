<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf" />
<html>
<body class="list_body">
<script language="javascript">
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
				alert("请选择删除项");
			}else{
				if (!confirm('是否要删除所有选择的权限信息? ')) {
				    return false;
				}
				$('oids').value = str;
		   		FormUtils.post(document.forms[0], '<c:url value="/sm/RoleAction.do?&step=deletePermissionsAll"/>');
	   		}
	   }
	   
	 CurrentPage.myquery= function () {
		if (document.getElementsByName("paginater.page") != null) {
	  	  document.getElementsByName("paginater.page").value = 0;
		}		 
		$('step').value = 'permissionShow';
		FormUtils.post(document.forms[0], '<c:url value='/sm/RoleAction.do'/>');
	}	   
		  
	  CurrentPage.removePermission = function(sel) {
	  	var permissionName = sel.row;
		if (! confirm(" 是否要删除["+permissionName+"]权限?")) {
			return;
		}
		var oid1=sel.id;
		$('step').value = 'removePermission';
		FormUtils.post(document.forms[0], '<c:url value = "/sm/RoleAction.do?oid1=" />' + oid1 );
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
		definedWin.openListingUrl('permissionUpdate','<c:url value = "/sm/RoleAction.do?step=userPermissionDetailed&oid2=" />' + oid2);
}
   TableSort.dblClick = function()
   {
   	   return false;
   }
/*   
	 CurrentPage.updatePermission = function(sel) {
	    var oid2=sel.id;
	 	definedWin.openListingUrl('permissionUpdate','<c:url value = "/sm/RoleAction.do?step=userPermissionDetailed&oid2=" />' + oid2);
	 //  window.open ('<c:url value = "/sm/RoleAction.do?step=userPermissionDetailed&oid2=" />' + oid2, "newwindow", "height=600, width=540, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no");
	   
	}
	*/
CurrentPage.create = function() {
	//var oid2 = document.getElementById("oid").value;
	definedWin.openListingUrl('permissionAdd','<c:url value = "/sm/permissionAction.do?step=selEntry" />');
/*	permissionIds = window.showModalDialog('<c:url value = "/sm/permissionAction.do?step=selEntry" />',"","center:yes;dialogHeight:600px;dialogWidth:900px");
	if ( permissionIds != null) {
		for(var i =0; i < permissionIds.length; i++){
			if (permissionIds[i] != "" && permissionIds[i] != null){
				strPal = strPal + permissionIds[i] +";";	
				
			}
		}
		FormUtils.post(document.forms[0], '<c:url value = "/sm/RoleAction.do?step=addPermission&oid2=" />' + strPal );
	}
	*/
}
definedWin.returnListObject = function(permissionIds){
	var flg = permissionIds[0];
	if(flg == "permissionUpdate"){
		var strRw = permissionIds[1] + ".rwCtrlName";
		var strRe = permissionIds[1] + ".readAccessLevelName";		
		var strWr = permissionIds[1] + ".writeAccessLevelName";
		document.getElementById(strRw).innerHTML = permissionIds[2];
		document.getElementById(strRe).innerHTML = permissionIds[3];
		document.getElementById(strWr).innerHTML = permissionIds[4];	
	}else{
		var strPal = "";
		if ( permissionIds != null) {
			for(var i =0; i < permissionIds.length; i++){
				if (permissionIds[i] != "" && permissionIds[i] != null){
					strPal = strPal + permissionIds[i] +";";	
				}
			}
			$('permissionIds').value = strPal;
			//alert(strPal);
			FormUtils.post(document.forms[0], '<c:url value = "/sm/RoleAction.do?step=addPermission" />');
		}
	}
}		
</script>
<form name="f">
<input type="hidden" name="parentCode" value="<c:out value = "${theForm.parentCode}"/>"/>
<input type="hidden" name="oid"  id= "oid" value="<c:out value='${theForm.oid}'/>"/>
<input type="hidden" name="step" value="<c:out value = '${theForm.step}' />" />
<input type="hidden" name="oids"/>
<input type="hidden" name="permissionIds"/>
	<div class="update_subhead">
					<span class="switch_open" onClick="StyleControl.switchDiv(this, $('supplierQuery1'))" title="">当前角色信息</span>
	</div>		
	<div id="supplierQuery1" style="display">
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
				<td class="attribute">显示名称</td>
		        <td>
		           <search:text name="tblCmnPermission.displayname" oper="like" type="java.lang.String" valueDefault='${theForm}'/>
		        </td>
				<td class="attribute">权限类型</td>
		        <td>
		       		<input name="conditions(tblCmnPermission.nodetype).name" type="hidden" value="tblCmnPermission.nodetype"/>
		            <input name="conditions(tblCmnPermission.nodetype).operator" type="hidden" value="="/>
		            <input name="conditions(tblCmnPermission.nodetype).createAlias" type="hidden" value="true"/>
		            <input name="conditions(tblCmnPermission.nodetype).type" type="hidden" value="java.lang.String"/>
	                <ec:composite value='${theForm.conditions["tblCmnPermission.nodetype"].value}'  valueName = "conditions(tblCmnPermission.nodetype).value" textName = "temp.conditions(tblCmnPermission.nodetype).value" source = "${theForm.nodetypeComboList}" />
		       </td>
		    </tr>
		    <tr>
				<td class="attribute">功能权限类型</td>
		        <td>
		        	<input name="conditions(tblCmnPermission.urltype).name" type="hidden" value="tblCmnPermission.urltype"/>
		            <input name="conditions(tblCmnPermission.urltype).operator" type="hidden" value="="/>
		            <input name="conditions(tblCmnPermission.urltype).createAlias" type="hidden" value="true"/>
		            <input name="conditions(tblCmnPermission.urltype).type" type="hidden" value="java.lang.String"/>
	                <ec:composite value='${theForm.conditions["tblCmnPermission.urltype"].value}'  valueName = "conditions(tblCmnPermission.urltype).value" textName = "temp.conditions(tblCmnPermission.urltype).value" source = "${theForm.urltypeComboList}" />
		        </td>
				<td class="attribute">列表数据显示范围</td>
		        <td>
		        	<input name="conditions(readAccessLevel).name" type="hidden" value="readAccessLevel"/>
		            <input name="conditions(readAccessLevel).operator" type="hidden" value="="/>
		            <input name="conditions(readAccessLevel).createAlias" type="hidden" value="false"/>
		            <input name="conditions(readAccessLevel).type" type="hidden" value="java.lang.String"/>
	                <ec:composite value='${theForm.conditions["readAccessLevel"].value}'  valueName = "conditions(readAccessLevel).value" textName = "temp.conditions(readAccessLevel).value" source = "${theForm.readAccessLevelComboList}" />
		        </td>		        
		    </tr>
		    <tr>
				<td class="attribute">数据编辑控制</td>
		        <td>
		        	<input name="conditions(rwCtrl).name" type="hidden" value="rwCtrl"/>
		            <input name="conditions(rwCtrl).operator" type="hidden" value="="/>
		            <input name="conditions(rwCtrl).createAlias" type="hidden" value="false"/>
		            <input name="conditions(rwCtrl).type" type="hidden" value="java.lang.String"/>
	                <ec:composite value='${theForm.conditions["rwCtrl"].value}'  valueName = "conditions(rwCtrl).value" textName = "temp.conditions(rwCtrl).value" source = "${theForm.rwCtrlComboList}" />
		        </td>
		        <td class="attribute">数据编辑范围</td>
		        <td>
		        	<input name="conditions(writeAccessLevel).name" type="hidden" value="writeAccessLevel"/>
		            <input name="conditions(writeAccessLevel).operator" type="hidden" value="="/>
		            <input name="conditions(writeAccessLevel).createAlias" type="hidden" value="false"/>
		            <input name="conditions(writeAccessLevel).type" type="hidden" value="java.lang.String"/>
	                <ec:composite value='${theForm.conditions["writeAccessLevel"].value}'  valueName = "conditions(writeAccessLevel).value" textName = "temp.conditions(writeAccessLevel).value" source = "${theForm.writeAccessLevelComboList}" />
		        </td>
		    </tr>
		    <tr>
		    	<td class="attribute">超链接</td>
		        <td>
		           <search:text name="tblCmnPermission.url" oper="like" type="java.lang.String" valueDefault='${theForm}'/>
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
					 <span class="switch_open" onClick="StyleControl.switchDiv(this,$('divId_requQues'))" title="点击收缩表格">角色权限信息列表</span>
				</td>
				
			</tr>
		</table>
	</div>
	<div id="divId_requQues" class="list_scroll">
		<table border="0" cellpadding="0" cellspacing="0" class="Listing" id="listtable" ondblclick="CurrentPage.updatePermission(event)" onClick="TableSort.sortColumn(event)">
		<input type="hidden" name="checkall" onClick="CurrentPage.selectedAll()" title="是否全选"/>
	<thead name="tabtitle">
				<tr>
					<td nowrap="nowrap"><input id='detailIdsForPrintAll' type='checkbox' onClick="FormUtils.checkAll(this,document.getElementsByName('detailIds'))" /></td>
					<td nowrap="nowrap">显示名称</td>
					<td nowrap="nowrap">权限类型</td>
					<td nowrap="nowrap">功能权限类型</td>
					<td nowrap="nowrap">超链接</td>
					<td nowrap="nowrap">数据编辑控制</td>
					<td nowrap="nowrap">列表数据显示范围</td>
					<td nowrap="nowrap">数据编辑范围</td>	
					<td nowrap="nowrap">权限路径</td>				
					<td nowrap="nowrap">说明</td>
					<td nowrap="nowrap">操作</td>	
				</tr>
			</thead>
			<tbody id='tablist'>
				<c:forEach var="item" items="${theForm.list}" varStatus="status">
					<tr>
						<td align="center">
							<input type="checkbox"  name="detailIds" onclick="FormUtils.check($('detailIdsForPrintAll'),document.getElementsByName('detailIds'))" value="<c:out value="${item.id}"/>" />
						</td>
						<td>
							<c:out  value="${item.tblCmnPermission.displayname}"/>&nbsp;
						</td>
						<td >
							<c:out  value="${item.tblCmnPermission.nodeTypeName}"/>&nbsp;
						</td>  	
							<c:choose>
							<c:when test="${item.tblCmnPermission.nodetype=='1'}">
								<td nowrap="nowrap" align='left'>
									<c:out  value="${item.tblCmnPermission.urltypeCh}"/>&nbsp;
								</td> 
							</c:when>
							<c:otherwise>
								<td nowrap="nowrap" align='left'>
									&nbsp;
								</td> 
							</c:otherwise>
						</c:choose>
						<td nowrap="nowrap" align='left'>
							<c:out  value="${item.tblCmnPermission.url}"/>&nbsp;
						</td>	
						<td nowrap="nowrap" align='left' id= "<c:out value="${item.id}"/>.rwCtrlName" >
							<c:out  value="${item.rwCtrlName}"/>&nbsp;
						</td>	
						<td nowrap="nowrap" align='left' id= "<c:out value="${item.id}"/>.readAccessLevelName">
							<c:out  value="${item.readAccessLevelName}"/>&nbsp;
						</td>
						<td nowrap="nowrap" align='left' id= "<c:out value="${item.id}"/>.writeAccessLevelName" >
							<c:out  value="${item.writeAccessLevelName}"/>&nbsp;
						</td>						
						<td>
							<c:out  value="${item.tblCmnPermission.permissionRoute}"/>&nbsp;
						</td>
						<td>
							<c:out  value="${item.tblCmnPermission.note}"/>&nbsp;
						</td>
						<td width="5%">
							<input type="button" class="list_update" onClick="CurrentPage.updatePermission(event)" id="<c:out value="${item.id}"/>" style="display:" name="addBtn" title="修改"/>
							<input type="button" class="list_delete" onClick="CurrentPage.removePermission(this)" id="<c:out value="${item.id}"/>" row="<c:out value="${item.tblCmnPermission.displayname}"/>" style="display:" name="delBtn" title="删除"/>
						</td>
					</tr>
				</c:forEach>					
			</tbody>
			
		</table>
	</div>
		<div align="right">
				<input name="" type="button" class="opera_batchdelete" value="批量删除" onClick="CurrentPage.deletePermissionsAll(); return false"/>
	    <div>
<table name='tbl' id="tbl" border="1"></table>	    
<div class="list_bottom">&nbsp;
<c:set var="paginater.forwardUrl" value="/sm/RoleAction.do?step=permissionShow" scope="page" /> 
	<%@ include file="/decorators/paginater.jspf"%>
</div>
</form>
</body>
</html>
