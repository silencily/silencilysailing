<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf" />
<html>
<body>
<script language="javascript">
		if (CurrentPage == null) {
    	var CurrentPage = {};
		} 
		  
	  CurrentPage.removePermission = function delNode(sel) {
		if (! confirm(" �Ƿ�Ҫɾ����Ȩ����Ϣ? ")) {
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
	 <span class="switch_open" onClick="StyleControl.switchDiv(this, $('fatherInfo'))" title="���������в�ѯ">��ǰ���ڵ���Ϣ</span>
</div>
<div id="fatherInfo" style="display:">
		<table width="100%"  border="0" cellpadding="0" cellspacing="0" class="Detail" id="divId_flaw_search_common" style="display:">
		    <tr>
				<td class="attribute">��ʾ����</td>
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
		title="���������в�ѯ">��ѯ����</span>
</div>
	<div id="supplierQuery" style="display: none">
		<table width="100%"  border="0" cellpadding="0" cellspacing="0" class="Detail" id="divId_flaw_search_common" style="display:">
		    <tr>
				<td class="attribute">��ʾ����</td>
		        <td>
		           <search:text name="displayname" oper="like" type="java.lang.String" valueDefault='${theForm}'/>
		        </td>
				<td class="attribute">����Ȩ������</td>
		        <td>
		        	<input name="conditions(urltype).name" type="hidden" value="urltype"/>
		            <input name="conditions(urltype).operator" type="hidden" value="="/>
		            <input name="conditions(urltype).createAlias" type="hidden" value="false"/>
		            <input name="conditions(urltype).type" type="hidden" value="java.lang.String"/>
	                <ec:composite value='${theForm.conditions["urltype"].value}'  valueName = "conditions(urltype).value" textName = "temp.conditions(urltype).value" source = "${theForm.urltypeComboList}" />
		        </td>
		    </tr>
		    <tr>
				<td class="attribute">����</td>
		        <td>
		        	<input name="conditions(nodetype).name" type="hidden" value="nodetype"/>
		            <input name="conditions(nodetype).operator" type="hidden" value="="/>
		            <input name="conditions(nodetype).createAlias" type="hidden" value="false"/>
		            <input name="conditions(nodetype).type" type="hidden" value="java.lang.String"/>
	                <ec:composite value='${theForm.conditions["nodetype"].value}'  valueName = "conditions(nodetype).value" textName = "temp.conditions(nodetype).value" source = "${theForm.nodetypeComboList}" />
		        </td>
				<td class="attribute">������</td>
		        <td>
		           <search:text name="url" oper="like" type="java.lang.String" valueDefault='${theForm}'/>
		        </td>		        
		    </tr>
		    <tr>
				<td class="attribute">Ȩ������Ŀ¼</td>
		        <td>
		        	<input name="fatherName" value="<c:out value='${theForm.fatherName}'/>"/>
		        </td>
				<td class="attribute">Ȩ�ޱ�ʶ</td>
		        <td>
		           <search:text name="permission_Cd" oper="like" type="java.lang.String" valueDefault='${theForm}'/>
		        </td>
		    </tr>
		  </table>
	    <div class="query_button">
	        <input type="button" value="" name="" class="opera_query" title="�����ѯ" onClick="CurrentPage.query();">
	    </div>
	</div>
	<div class="update_subhead">
					 <span class="switch_open" onClick="StyleControl.switchDiv(this,$('divId_scrollLing'))" title="����������">Ȩ����Ϣ�б�</span>

	</div>
	<div id="divId_scrollLing" class="list_scroll">
		<table border="0" cellpadding="0" cellspacing="0" class="Listing" id="listtable" onClick="TableSort.sortColumn(event)">
			<thead>
				<tr>
					<td nowrap="nowrap">&nbsp;</td>
					<td nowrap="nowrap">����</td>
					<td nowrap="nowrap">��ʾ����</td>
					<td nowrap="nowrap">����Ȩ������</td>
					<td nowrap="nowrap">���ڵ�����</td>
					<td nowrap="nowrap">������</td>
					<td nowrap="nowrap">Ȩ��·��</td>	
					<td nowrap="nowrap">Ȩ�ޱ�ʶ</td>				
					<td nowrap="nowrap">˵��</td>
					<td nowrap="nowrap">����</td>	
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
							<input type="button" class="list_delete" onClick="CurrentPage.removePermission(this)" id="<c:out value="${item.id}"/>" style="display:" name="delBtn" title="ɾ��"/>
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
