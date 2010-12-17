<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf"/>
<html>
<body>
<script type="text/javascript">
var msgInfo_ = new msgInfo();
var CurrentPage = {};
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
TableSort.dblClick = function()
   {
   	   return false;
   }
</script>
<div class="news_title_bg" >
	<div align="right">
		<input type="button" class="opera_confirm" name="" onclick="CurrentPage.selectAll()"/>
		<input type="button" class="opera_cancel" name="" onclick="window.close()"/>
	</div>
</div>
<div class="main_title">
<div>用户选择</div>
</div>
<form name="form">
<input type="hidden" name="step" value="<c:out value = "${theForm.step}" />" />	
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
		<c:forEach var="item" items="${theForm.user}" varStatus="status">
			<tr>
				<td align="center">
				<input type="checkbox"   name="detailIds" onclick="FormUtils.check($('detailIdsForPrintAll'),document.getElementsByName('detailIds'))" value="<c:out value="${item.id}"/>" />
				</td>
				<td nowrap="nowrap" align='left'>
					<c:out value = "${item.emp.empCd}"/>
				</td>
				<td nowrap="nowrap" align='left'>
					<c:out value = "${item.emp.empName}" />
				</td>
				<td nowrap="nowrap" align='left'>
					<c:out value = "${item.statusName}" />
				</td>
				<td nowrap="nowrap" align='left'>
					<c:out value = "${item.deptRoute}" />
				</td>
			</tr>
		</c:forEach>
		</tbody>
		</table>
<div class="list_bottom"><c:set var="paginater.forwardUrl" value="/sm/userManageAction.do?step=selectUser" scope="page" /> 
	<%@ include file="/decorators/paginater.jspf"%>
</div>
</div>
</form>
</body>
</html>

