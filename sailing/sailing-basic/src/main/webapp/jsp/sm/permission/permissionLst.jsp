<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf" />
<html>
<body>
<script language="javascript">
		if (CurrentPage == null) {
    	var CurrentPage = {};
		} 
		  
	  CurrentPage.removePermission = function delNode(sel) {
		if (! confirm(" 是否要删除该权限信息? ")) {
			return;
		}
		var oid1=sel.id;
		$('step').value = 'removePermission';
		FormUtils.post(document.forms[0], '<c:url value = "/sm/permissionAction.do?oid1=" />' + oid1 );
	  
		}
	 CurrentPage.query= function () {
		if (document.getElementsByName("paginater.page") != null) {
	  	  document.getElementsByName("paginater.page").value = 0;
		}		 
		FormUtils.post(document.forms[0], '<c:url value='/sm/permissionAction.do?step=list&search=s'/>');
	}
	
	CurrentPage.create = function() {
 		var tempParentCode = window.parent.codeTree.TreeList[window.parent.codeTree.SelectId].selfId;
  		var strFlg = window.parent.codeTree.TreeList[window.parent.codeTree.SelectId].imgType;
 		var pat = new RegExp('([\?\&])parentCode=[^\&]*');
 		var strFlg = window.parent.codeTree.TreeList[window.parent.codeTree.SelectId].imgType;
	  	var parentCode;
	  	if (strFlg == '2') {
  			parentCode = tempParentCode+";"+"2"+";c";  
	       } else {
	       	parentCode = tempParentCode+";"+"1"+";c";
			
			} 
 	  	var url = window.parent.panel.dataArr[1][2];
		if (pat.test(url)) {
			if($('oid')){		
				$('oid').value = '';
			}
		   	window.parent.panel.dataArr[1][2]=url.replace(pat, RegExp.$1 + "parentCode=" + parentCode);
		  	window.parent.panel.click(1);	
		  	window.parent.panel.dataArr[1][2]=url.replace(pat, RegExp.$1 + "&parentCode=");
		}
	}
	
	function Validator.afterSuccessMessage(){
		if(parent.codeTree){
			parent.codeTree.Update(parent.codeTree.SelectId);
		}
		}
		
		
</script>
<form name="f">
<input type="hidden" name="parentCode" value="<c:out value = "${theForm.parentCode}"/>"/>
<input type="hidden" name="step" value="<c:out value = '${theForm.step}' />" />
<div class="update_subhead" >
	 <span class="switch_open" onClick="StyleControl.switchDiv(this, $('fatherInfo'))" title="点击这里进行查询">当前父节点信息</span>
</div>
<div id="fatherInfo" style="display:">
		<table width="100%"  border="0" cellpadding="0" cellspacing="0" class="Detail" id="divId_flaw_search_common" style="display:">
		    <tr>
				<td class="attribute">显示名称</td>
      			 <td>
	           <input name="temp.fatherName" value="<c:out value='${theForm.currentFatherName}'/>"
				class="readonly" readonly="readonly" />
		        </td>
		    </tr>
		</table>
</div>
<div class="update_subhead">
	<span class="switch_close"
		onClick="StyleControl.switchDiv(this,$(supplierQuery))"
		title="点击这里进行查询">查询条件</span>
</div>
	<div id="supplierQuery" style="display: none">
		<table width="100%"  border="0" cellpadding="0" cellspacing="0" class="Detail" id="divId_flaw_search_common" style="display:">
		    <tr>
				<td class="attribute">显示名称</td>
		        <td>
		           <search:text name="displayname" oper="like" type="java.lang.String" valueDefault='${theForm}'/>
		        </td>
				<td class="attribute">功能权限类型</td>
		        <td>
		        	<input name="conditions(urltype).name" type="hidden" value="urltype"/>
		            <input name="conditions(urltype).operator" type="hidden" value="="/>
		            <input name="conditions(urltype).createAlias" type="hidden" value="false"/>
		            <input name="conditions(urltype).type" type="hidden" value="java.lang.String"/>
	                <ec:composite value='${theForm.conditions["urltype"].value}'  valueName = "conditions(urltype).value" textName = "temp.conditions(urltype).value" source = "${theForm.urltypeComboList}" />
		        </td>
		    </tr>
		    <tr>
				<td class="attribute">类型</td>
		        <td>
		        	<input name="conditions(nodetype).name" type="hidden" value="nodetype"/>
		            <input name="conditions(nodetype).operator" type="hidden" value="="/>
		            <input name="conditions(nodetype).createAlias" type="hidden" value="false"/>
		            <input name="conditions(nodetype).type" type="hidden" value="java.lang.String"/>
	                <ec:composite value='${theForm.conditions["nodetype"].value}'  valueName = "conditions(nodetype).value" textName = "temp.conditions(nodetype).value" source = "${theForm.nodetypeComboList}" />
		        </td>
				<td class="attribute">超链接</td>
		        <td>
		           <search:text name="url" oper="like" type="java.lang.String" valueDefault='${theForm}'/>
		        </td>		        
		    </tr>
		    <tr>
				<td class="attribute">权限所在目录</td>
		        <td>
		        	<input name="fatherName" value="<c:out value='${theForm.fatherName}'/>"/>
		        </td>
				<td class="attribute">权限标识</td>
		        <td>
		           <search:text name="permission_Cd" oper="like" type="java.lang.String" valueDefault='${theForm}'/>
		        </td>
		    </tr>
		  </table>
	    <div class="query_button">
	        <input type="button" value="" name="" class="opera_query" title="点击查询" onClick="CurrentPage.query();">
	    </div>
	</div>
	<div class="update_subhead">
					 <span class="switch_open" onClick="StyleControl.switchDiv(this,$('divId_scrollLing'))" title="点击收缩表格">权限信息列表</span>

	</div>
	<div id="divId_scrollLing" class="list_scroll">
		<table border="0" cellpadding="0" cellspacing="0" class="Listing" id="listtable" onClick="TableSort.sortColumn(event)">
			<thead>
				<tr>
					<td nowrap="nowrap">&nbsp;</td>
					<td nowrap="nowrap">类型</td>
					<td nowrap="nowrap">显示名称</td>
					<td nowrap="nowrap">功能权限类型</td>
					<td nowrap="nowrap">父节点名称</td>
					<td nowrap="nowrap">超链接</td>
					<td nowrap="nowrap">权限路径</td>	
					<td nowrap="nowrap">权限标识</td>				
					<td nowrap="nowrap">说明</td>
					<td nowrap="nowrap">操作</td>	
				</tr>
			</thead>
			<tbody>
				<c:forEach var="item" items="${theForm.list}" varStatus="status">
					<tr>
						<td align="center">
							<input type="radio" onclick ="FormUtils.check($('detailIdsForPrintAll'),this)" name="oid" value="<c:out value="${item.id}"/>;<c:out value="${item.nodetype}"/>" />
						</td>
						<td>
							<c:out  value="${item.nodeTypeName}"/>&nbsp;
						</td>
						<td>
							<c:out  value="${item.displayname}"/>&nbsp;
						</td>
						<td >
							<c:out  value="${item.urltypeCh}"/>&nbsp;
						</td>  	
						<td>
							<c:out  value="${item.father.displayname}"/>&nbsp;
						</td>
						<td>
							<c:out  value="${item.url}"/>&nbsp;
						</td>
						<td>
							<c:out  value="${item.permissionRoute}"/>&nbsp;
						</td>
						<c:choose>
							<c:when test="${item.nodetype == '2'}">
								<td>
									<c:out  value="${item.permissionCd}"/>&nbsp;
								</td>				
							</c:when>
							<c:otherwise>
							<td align ="center">
								<c:out  value="-"/>	
							</td>					
							</c:otherwise>
						</c:choose>
						<td>
							<c:out  value="${item.note}"/>&nbsp;
						</td>
						<td align="center">
							<input type="button" class="list_delete" onClick="CurrentPage.removePermission(this)" id="<c:out value="${item.id}"/>" style="display:" name="delBtn" title="删除"/>
						</td>
					</tr>
				</c:forEach>					
			</tbody>
		</table>
<c:choose>
<c:when test="${theForm.pageUpDlg=='searchTag'}">
<div class="list_bottom"><c:set var="paginater.forwardUrl" value="/sm/permissionAction.do?step=list" scope="page" /> 
	<%@ include file="/decorators/paginater.jspf"%>
</div>
</c:when>
<c:otherwise>
<div class="list_bottom"><c:set var="paginater.forwardUrl" value="/sm/permissionAction.do?step=list&search=s" scope="page" /> 
	<%@ include file="/decorators/paginater.jspf"%>
</div>
</c:otherwise>
</c:choose>
	</div>
</form>
</body>
</html>
