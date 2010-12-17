<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ include file="/decorators/default.jspf" %>



<%--
    尝试使用 JSP1.2 specification, 按照 Sun J2EE 5 说明, 并不保证在其他的 server 下被识别
    所以如果发生异常, 搜索 "jsp1.2"字样, 使用 jsp1.1 代替
--%>

<%-- jsp1.2 --%>

<html>
<body class="list_body">
<form id="f">

<jsp:directive.include file="/jsp/wf/operationSearch.jspf" />

<input type="hidden" name="step" value="<c:out value = '${theForm.step}' />" />
<div class="update_subhead">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td>
		    <%--StyleControl.switchDiv(this, $('allHidden')) 展开或收起列表区域--%>
		    <span class="switch_open" onClick="StyleControl.switchDiv(this, $('allHidden'))" title="伸缩节点">检索结果</span>
		</td>
	</tr>
</table>
</div>
<div id="allHidden">
<div id="divId_scrollLing" class="list_scroll">
		<table border="0" cellpadding="0" cellspacing="0" class="Listing" id="listtable" onClick="TableSort.sortColumn(event)">
			<input type="hidden" name="checkall" onClick="CurrentPage.selectedAll()"/>

			<table:table name="operviewBean" />
		</table>
	</div>
<div class="list_bottom">
    
	<c:set var = "paginater.forwardUrl" value = "/wf/operationlist.do?step=list" scope = "page" />
	
	<%@ include file = "/decorators/paginater.jspf" %>

	 
	<input type="button" class="opera_export" title="导出Excel文件" onClick="Print.exportExcel($('tabId_listing'))" value=""/>
	<input name="" type="button" class="opera_display" value="批量生成XML" onClick="CurrentPage.createXml(); return false"/>
   
</div>
</div>
</form>
</body>
</html>

<script type="text/javascript">
var msgInfo_ = new msgInfo();
if (CurrentPage == null) {
   		var CurrentPage = {};
	}
// 	remove()方法进行一条记录的删除；
CurrentPage.remove = function(oid) {
	if (! confirm(msgInfo_.getMsgById('HR_I061_A_0'))) {
		return;
	}
	FormUtils.post(document.forms[0], '<c:url value = "/wf/operationlist.do?step=delete&oid=" />' + oid );
} 
   // create()方法新添加一条记录；
CurrentPage.create = function () {
	var len = listtable.rows.length;
	if(len<2){
		TableSort.setNoSelect();
		TableSort.dblClick();
	}else{
		$('oid').value = '';
		TableSort.setNoSelect();
		TableSort.dblClick();
	}
}
  // createXml()方法批量生成XML；
CurrentPage.createXml = function(){    
	FormUtils.post(document.forms[0],'<c:url value = "/wf/operationlist.do?step=createAllXML" />');
}

</script>