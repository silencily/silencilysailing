<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf"/>
<script type="text/javascript">
var msgInfo_ = new msgInfo();
var CurrentPage = {};

CurrentPage.query = function() {
	if (document.getElementsByName("paginater.page") != null) {
	    document.getElementsByName("paginater.page").value = 0;
	}
	FormUtils.post(document.forms[0], '<c:url value="/sm/userManageAction.do?step=selectInfo"/>');
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
<!--<div class="news_title_bg" >-->
<!--<div align="right"><input type="button" class="opera_confirm" name="" onclick="CurrentPage.selectAll()"/><input type="button" class="opera_cancel" name="" onclick="window.close()"/></div>-->
<!--</div>-->
<form name="form">
<div class="main_title">
<div>用户选择</div>
</div>
<input type="hidden" name="strFlg" value="<c:out value = "${theForm.strFlg}" />" />	
<input type="hidden" name="step" value="<c:out value = "${theForm.step}" />" />	
<div class="update_subhead" >
    <span class="switch_close" onClick="StyleControl.switchDiv(this, $('supplierQuery'))" title="点击这里进行查询">查询条件</span>
</div>

<div id="supplierQuery" style="display:none">
	<table width="100%"  border="0" cellpadding="0" cellspacing="0" class="Detail" id="divId_flaw_search_common" style="display:">
	    <tr>
			<td class="attribute">员工编号</td>
	        <td>
	           <search:text name="empCd" oper="like" type="java.lang.String" valueDefault='${theForm}'/>
	        </td>
			<td class="attribute">员工姓名</td>
	        <td>
	        <search:text name="empName" oper="like" type="java.lang.String" valueDefault='${theForm}'/>
	        </td>
	    </tr>
	    <tr>
	        <td class="attribute">激活状态</td>
	        <td>
	        	<input name="conditions(status).name" type="hidden" value="status"/>
	            <input name="conditions(status).operator" type="hidden" value="="/>
	            <input name="conditions(status).createAlias" type="hidden" value="false"/>
	            <input name="conditions(status).type" type="hidden" value="java.lang.String"/>
                <ec:composite value='${theForm.conditions["status"].value}'  valueName = "conditions(status).value" textName = "temp.conditions(status).value" source = "${theForm.statusComboList}" />
				<%-- 
				<search:select name="status"/>
                	<ec:composite value='${theForm.conditions["status"].value}' valueName = "conditions(status).value" textName = "temp.conditions(status).value" source = '${theForm.statusComboList}'/>	
           --%>
	        </td>
	        <td class="attribute">部门名称</td>
	        <td>
	            <search:text name="tblCmnDept.deptName" oper="like" type="java.lang.String" valueDefault='${theForm}'/>
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
			<td><span class="switch_open" onClick="StyleControl.switchDiv(this,$('divId_scrollLing'))" title="点击收缩表格">用户列表</span></td>
		</tr>
	</table>
</div>
<div id="divId_scrollLing" class="list_scroll">
		<table border="0" cellpadding="0" cellspacing="0" class="Listing" id="listtable" onClick="TableSort.sortColumn(event)">
		<input type="hidden" name="checkall" onClick="CurrentPage.selectedAll()" title="是否全选"/>
	<thead>
			<tr>
			<td nowrap="nowrap"><input id='detailIdsForPrintAll' type='checkbox' onClick="FormUtils.checkAll(this,document.getElementsByName('detailIds'))" /></td>
			<td nowrap="nowrap">员工编号</td>
			<td nowrap="nowrap">员工姓名</td>
			<td nowrap="nowrap">激活状态</td>
			<td nowrap="nowrap">部门路径</td>
		</tr>
		</thead>
		<tbody>
		<c:forEach var="item" items="${theForm.selectUser}" varStatus="status">
			<tr>
				<td align="center">
							<input type="checkbox"   name="detailIds" onclick="FormUtils.check($('detailIdsForPrintAll'),document.getElementsByName('detailIds'))" value="<c:out value="${item.id}"/>" row="<c:out value="${item.empCd}"/>;<c:out value="${item.empName}"/>"/>
				</td>
				<td nowrap="nowrap" align='left'>
					<c:out value = "${item.empCd}"/>
				</td>
				<td nowrap="nowrap" align='left'>
					<c:out value = "${item.empName}" />
				</td>
				<td nowrap="nowrap" align='left'>
					<c:out value = "${item.statusName}" />
				</td>
				<td nowrap="nowrap" align='left'>
		
				</td>
			</tr>
		</c:forEach>
		</tbody>
		</table>
<div class="list_bottom"><c:set var="paginater.forwardUrl" value="/sm/userManageAction.do?step=selectInfo" scope="page" /> 
	<%@ include file="/decorators/paginater.jspf"%>
</div>
</div>
</form>
</body>
</html>

