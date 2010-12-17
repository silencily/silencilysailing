<%-- jsp1.2 --%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf"/>
<%--
    尝试使用 JSP1.2 specification, 按照 Sun J2EE 5 说明, 并不保证在其他的 server 下被识别
    所以如果发生异常, 搜索 "jsp1.2"字样, 使用 jsp1.1 代替
--%>

<%-- jsp1.2 --%>
<body class="list_body">
<html>
<form id="f">
<jsp:directive.include file="/jsp/sm/user/userSearch.jspf" />
<input type="hidden" name="step" value="<c:out value = '${theForm.step}' />" />
<div class="update_subhead">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td>
			<span class="switch_open" onClick="StyleControl.switchDiv(this, $('divId_scrollLing'))"title="点击收缩表格">检索结果</span>
		</td>
		
	</tr>
</table>
</div>

<div id="divId_scrollLing" class="list_scroll">
		<table border="0" cellpadding="0" cellspacing="0" class="Listing" id="listtable" onClick="TableSort.sortColumn(event)">
			<input type="hidden" name="checkall" onClick="CurrentPage.selectedAll()" title="是否全选"/>
			<table:table name="viewBean" box="check"/>
		</table>
<div class="list_bottom"><c:set var="paginater.forwardUrl" value="/sm/userManageAction.do?step=list" scope="page" /> 
	<%@ include file="/decorators/paginater.jspf"%>
		
		<input type="button" class="opera_export" title="导出Excel文件" onClick="Print.exportExcel($('divId_scrollLing'))" value=""/>
		<input name="" class="opera_display" type="button" value="定制显示" title="定制显示列表栏目" onClick ="CurrentPage.settable('cmn_user')"/>
		<input name="" type="button" class="opera_display" value="批量删除" onClick="CurrentPage.deleteAll(); return false"/>
		<input name="" type="button" class="opera_display" value="批量激活" onClick="CurrentPage.batchjihuo(); return false"/>
		<input name="" type="button" class="opera_display" value="批量禁用" onClick="CurrentPage.batchjinyong(); return false"/>
</div>
</div>
</form>
</html>
<script type="text/javascript">
var msgInfo_ = new msgInfo();
	if (CurrentPage == null) {
   		var CurrentPage = {};
	}    
CurrentPage.remove = function(oid) {
	if (! confirm(msgInfo_.getMsgById('SM_I009_A_0'))) {
		return;
	}
	$('step').value = 'remove';
	FormUtils.post(document.forms[0], '<c:url value = "/sm/userManageAction.do?oid=" />' + oid );
}
 
   CurrentPage.deleteAll = function() {
       var str="";
	   var dd=document.getElementsByName('oid');
	   for(var i=0;i<dd.length;i++){
	   	   if(dd[i].checked==true){
			   var oid1=dd[i].value;
			   str+=oid1+"$";
		   }
	   }	   
	   if (str==""||str==null){
			alert(msgInfo_.getMsgById('SM_I014_A_0'));
		}else{
			if (!confirm(msgInfo_.getMsgById('SM_I015_A_0'))) {
			    return false;
			}
       		$('step').value = 'deleteAll';
       		FormUtils.post(document.forms[0], '<c:url value="/sm/userManageAction.do?oids="/>'+str );
       	}
   }
   
  CurrentPage.batchjihuo = function() {
       var str="";
	   var dd=document.getElementsByName('oid');
	   for(var i=0;i<dd.length;i++){
	   	   if(dd[i].checked==true){
			   var oid1=dd[i].value;
			   str+=oid1+"$";
		   }
	   }	   
	   if (str==""||str==null){
			alert(msgInfo_.getMsgById('SM_I010_A_0'));
		}else{
			if (!confirm(msgInfo_.getMsgById('SM_I011_A_0'))) {
			    return false;
			}
       		$('step').value = 'batchjihuo';
       		FormUtils.post(document.forms[0], '<c:url value="/sm/userManageAction.do?oids="/>'+str );
       	}
   }
   
   CurrentPage.batchjinyong = function() {
       var str="";
	   var dd=document.getElementsByName('oid');
	   for(var i=0;i<dd.length;i++){
	   	   if(dd[i].checked==true){
			   var oid1=dd[i].value;
			   str+=oid1+"$";
		   }
	   }
	   if (str==""||str==null){
			alert(msgInfo_.getMsgById('SM_I012_A_0'));
		}else{
			if (!confirm(msgInfo_.getMsgById('SM_I013_A_0'))) {
			    return false;
			}
       		$('step').value = 'batchjinyong';
       		FormUtils.post(document.forms[0],  '<c:url value="/sm/userManageAction.do?oids="/>'+str  );
       	}
   }
		CurrentPage.create = function() {
			$('oid').value = '';	
			TableSort.setNoSelect();
			TableSort.dblClick();
	}
	CurrentPage.settable =function(pageid){
		var url = '<c:url value="/curd/curdAction.do?step=setTable&pageId="/>';
		url+=pageid;
		definedWin.openListingUrl("setTable",url);
	}
</script>


