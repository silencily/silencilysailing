<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf" />
<html>
	<body>
		<script language="javascript">
			if (CurrentPage == null) {
    	var CurrentPage = {};
		} 
	CurrentPage.create = function() {
  		var pat = new RegExp('([\?\&])parentCode=[^\&]*');
	  	var url = window.parent.panel.dataArr[1][2];
	  	var parentCode = window.parent.document.getElementById("parentCode").value;
	  	var strTemp = parentCode +";org;c"
		if (pat.test(url)) {
		   	window.parent.panel.dataArr[1][2]=url.replace(pat, RegExp.$1 + "parentCode=" + strTemp);
		  	window.parent.panel.click(1);
		  	//window.parent.panel.dataArr[1][2]=url.replace(pat, RegExp.$1 + "parentCode=");
			}
	}		  
	  CurrentPage.del = function delNode(sel) {
		var oid1 = sel.id;
		var arrOid = oid1.split(";");
	  	var strFlg ;
	  	var selfId;
	  	selfId = arrOid[0];
	  	document.getElementById("parentCode").value = window.parent.codeTree.TreeList[window.parent.codeTree.SelectId].selfId;
	  	
		if ((arrOid.length >1) && arrOid[1] == "org"){
			if (! confirm(" 是否要删除该角色目录信息? ")) {
				return;
			}
			strFlg="c";
		} else {
			if (! confirm(" 是否要删除该角色信息? ")) {
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
		FormUtils.post(document.forms[0], '<c:url value='/sm/RoleAction.do?step=list&flg=org'/>'); 	 
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
	 <span class="switch_open" onClick="StyleControl.switchDiv(this, $('fatherInfo'))" title="点击这里进行查询">当前父节点信息</span>
</div>
<div id="fatherInfo" style="display:">
		<table width="100%"  border="0" cellpadding="0" cellspacing="0" class="Detail" id="divId_flaw_search_common" style="display:">
		    <tr>
				<td class="attribute">显示名称</td>
      			 <td>
	           <input name="fatherName" value="<c:out value='${theForm.fatherName}'/>"
				class="readonly" readonly="readonly" />
		        </td>
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
		        	<search:text name="display_Name" oper="like" type="java.lang.String" valueDefault='${theForm}'/>
		        </td>
		        <td class="attribute">说明</td>
		        <td>
		       		<search:text name="note" oper="like" type="java.lang.String" valueDefault='${theForm}'/>
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
					 <span class="switch_open" onClick="StyleControl.switchDiv(this,$('divId_requQues'))" title="点击收缩表格">检索结果</span>
				</td>
			</tr>
		</table>
	</div>
	<div id="divId_requQues" class="list_scroll">
		<table border="0" cellpadding="0" cellspacing="0" class="Listing" id="listtable" onClick="TableSort.sortColumn(event)">
			<thead>
				<tr>
					<td nowrap="nowrap">&nbsp;</td>
					<td nowrap="nowrap">显示名称</td>
					<td nowrap="nowrap">目录路径</td>
					<td nowrap="nowrap">说明</td>
					<td nowrap="nowrap">操作</td>	
				</tr>
			</thead>
			<tbody>
				<c:forEach items = "${theForm.orgList}" var = "item" varStatus = "status" >
					<tr>
						<td align="center">
						<input type="radio" onclick ="FormUtils.check($('detailIdsForPrintAll'),this)" name="oid" value="<c:out value="${item.id}"/>;org" />
						</td>
						<td>
							<c:out  value="${item.displayName}"/>&nbsp;
						</td>	
						<td >
							<c:out  value="${item.displayName}"/>&nbsp;
						</td>
						<td>
							<c:out  value="${item.note}"/>&nbsp;
						</td>
						<td align="center">
							<input type="button" class="list_delete" onClick="CurrentPage.del(this)" id="<c:out value="${item.id}"/>;org" style="display:" name="delBtn" title="删除"/>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<div class="list_bottom">

	<c:set var="paginater.forwardUrl" value="/sm/RoleAction.do?step=list&flg=org&parentCode=" scope="page" /> 

	<%@ include file="/decorators/paginater.jspf"%>
</div>
	</div>
</form>
</body>
</html>
