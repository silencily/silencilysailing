<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf" />
<html>
	<body>
		<script language="javascript">
			if (CurrentPage == null) {
    	var CurrentPage = {};
		} 
		  
	  CurrentPage.del = function delNode(sel) {
		var oid1 = sel.id;
		var arrOid = oid1.split(";");
	  	var strFlg ;
	  	var selfId;
	  	selfId = arrOid[0];
	  	document.getElementById("parentCode").value = window.parent.codeTree.TreeList[window.parent.codeTree.SelectId].selfId;
	  	
		if ((arrOid.length >1) && arrOid[1] == "org"){
			if (! confirm(" �Ƿ�Ҫɾ���ý�ɫĿ¼��Ϣ? ")) {
				return;
			}
			strFlg="c";
		} else {
			if (! confirm(" �Ƿ�Ҫɾ���ý�ɫ��Ϣ? ")) {
				return;
			}
			strFlg="2";
		}
		FormUtils.post(document.forms[0], '<c:url value='/sm/RoleAction.do?step=delMethod&strFlg='/>' + strFlg +"&oid=" + selfId);
	}
CurrentPage.query= function () {
	 if (document.getElementsByName("paginater.page") != null) {
	   	 document.getElementsByName("paginater.page").value = 0;
	 }
	FormUtils.post(document.forms[0], '<c:url value='/sm/RoleAction.do?step=roleSearch'/>'); 
}
	
	CurrentPage.CheckNum = function (name) {
		if (name!=""){
			if(parseInt(name)>=0 && parseInt(name)<=999 ){
				return true;
			}
			else {
				return false;
			}
		}
		return true;
	}
	
	CurrentPage.create = function() {
		if($('oid')){
			$('oid').value = '';
		}
  		var pat = new RegExp('([\?\&])parentCode=[^\&]*');
	  	var parentCode = window.parent.document.getElementById("parentCode").value;
	  	var strTemp = parentCode +";role;c"
	  	var url = window.parent.panel.dataArr[1][2];
		if (pat.test(url)) {
		   	window.parent.panel.dataArr[1][2]=url.replace(pat, RegExp.$1 + "parentCode=" + strTemp);
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
<input type="hidden" name="parentCode" id="parentCode" value="<c:out value = "${theForm.parentCode}"/>"/>
<input type="hidden" name="empinfo.oid"  id= "empinfo.oid" value=""/>
<input type="hidden" name="step" value="<c:out value = '${theForm.step}' />" />
<div class="update_subhead" >
	 <span class="switch_open" onClick="StyleControl.switchDiv(this, $('fatherInfo'))" title="���������в�ѯ">��ǰ���ڵ���Ϣ</span>
</div>
<div id="fatherInfo" style="display:">
		<table width="100%"  border="0" cellpadding="0" cellspacing="0" class="Detail" id="divId_flaw_search_common" style="display:">
		    <tr>
				<td class="attribute">��ʾ����</td>
      			 <td>
	           <input name="fatherName" value="<c:out value='${theForm.fatherName}'/>"
				class="readonly" readonly="readonly" />
		        </td>
		    </tr>
		</table>
</div>
	<div class="update_subhead" >
	    <span class="switch_close" onClick="StyleControl.switchDiv(this, $('supplierQuery'))" title="���������в�ѯ">��ѯ����</span>
	</div>
	<div id="supplierQuery" style="display:none">
		<table width="100%"  border="0" cellpadding="0" cellspacing="0" class="Detail" id="divId_flaw_search_common" style="display:">
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
					 <span class="switch_open" onClick="StyleControl.switchDiv(this,$('divId_requQues'))" title="����������">�������</span>
				</td>
			</tr>
		</table>
	</div>
	<div id="divId_requQues" class="list_scroll">
		<table border="0" cellpadding="0" cellspacing="0" class="Listing" id="listtable" onClick="TableSort.sortColumn(event)">
			<thead>
				<tr>
					<td nowrap="nowrap">&nbsp;</td>
					<td nowrap="nowrap">��ɫ��ʶ</td>
					<td nowrap="nowrap">��ʾ����</td>
					<td nowrap="nowrap">��ɫ·��</td>
					<td nowrap="nowrap">ϵͳ��ɫ</td>
					<td nowrap="nowrap">˵��</td>
					<td nowrap="nowrap">����</td>	
				</tr>
			</thead>
			<tbody>
				<c:forEach items = "${theForm.rolList}" var = "item" varStatus = "status" >
					<tr>
						<td align="center">
								<input type="radio" onclick ="FormUtils.check($('detailIdsForPrintAll'),this)" name="oid" value="<c:out value="${item.id}"/>;role" />	
						</td>
						<td>
							<c:out  value="${item.roleCd}"/>&nbsp;
						</td>
						<td>
							<c:out  value="${item.name}"/>&nbsp;
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
						<td align="center">
							<input type="button" class="list_delete" onClick="CurrentPage.del(this)" id="<c:out value="${item.id}"/>;role" style="display:" name="delBtn" title="ɾ��"/>
						</td>
					</tr>
				</c:forEach>					
			</tbody>
		</table>
<div class="list_bottom">
<c:choose>
<c:when test="${param.step=='list'}">
	<c:set var="paginater.forwardUrl" value="/sm/RoleAction.do?step=list&flg=role" scope="page" /> 
</c:when>
<c:otherwise>
	<c:set var="paginater.forwardUrl" value="/sm/RoleAction.do?step=roleSearch&flg=role" scope="page" /> 
</c:otherwise>
</c:choose>
	<%@ include file="/decorators/paginater.jspf"%>
</div>
	</div>
</form>
</body>
</html>
