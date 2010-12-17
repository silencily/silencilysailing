<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf"/>
<body class="list_body">
<html>
<form id="f">
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
			<td class="attribute">操作表名</td>
	        <td>
	        <search:text name="userName" oper="like" type="java.lang.String" valueDefault='${theForm}'/>
	        </td>
      </tr>
	  <tr>
	       	<td class="attribute">完成操作</td>
	        <td colspan="3">
	           <search:text name="funName" oper="like" type="java.lang.String" valueDefault='${theForm}'/>
	        </td>
      </tr>
  <tr>
     		 <td class="attribute">操作时间</td>
	        <td colspan="3">
	           <search:time name="time" pattern="yyyy-MM-dd"  valueDefault="${theForm}"/>
	        </td>
      </tr> 
   </table>
     <div class="query_button">
        <input type="button" value="" name="" class="opera_query" title="点击查询" onClick="CurrentPage.query();">
    </div>
 </div>
<input type="hidden" name="oid" value="<c:out value = '${theForm.oid}' />" />
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
	<thead>
				<tr>
					<td nowrap="nowrap">员工编号</td>	
					<td nowrap="nowrap">操作表名</td>
					<td nowrap="nowrap">完成操作</td>
					<td nowrap="nowrap">操作时间</td>
				</tr>
			</thead>
			<tbody id='tablist'>
				<c:forEach var="item" items="${theForm.cmnSysLog}" varStatus="status">
					<tr>
						<td nowrap="nowrap" align='left'>
							<c:out  value="${item.empCd}"/>&nbsp;
						</td>
						<td nowrap="nowrap" align='left'>
							<c:out  value="${item.userName}"/>&nbsp;
						</td>
						<td nowrap="nowrap" align='left'>
							<c:out  value="${item.funName}"/>&nbsp;
						</td>	
						<td nowrap="nowrap" align='left'>
							<c:out  value="${item.stringTime}"/>&nbsp;
						</td> 
					</tr>
				</c:forEach>					
			</tbody>
		</table>
<div class="list_bottom"><c:set var="paginater.forwardUrl" value="/sm/logManageAction.do?step=list" scope="page" /> 
	<%@ include file="/decorators/paginater.jspf"%>
			<input type="button" class="opera_display" title="导出Excel文件" onClick="Print.exportExcel($('divId_scrollLing'))" value="Excel导出"/>
</div>
	</div>
</form>
</html>
<script type="text/javascript">
var msgInfo_ = new msgInfo();
if (CurrentPage == null) {
   		var CurrentPage = {};
	}
   TableSort.dblClick = function()
   {
   	   return false;
   }
 CurrentPage.query= function () {
	 var date1=document.getElementsByName('conditions(time>=).value');
	 var date2=document.getElementsByName('conditions(time<=).value');
		if (DateUtils.CompareDate(date1[0].value,date2[0].value)<0){
					Validator.clearValidateInfo();
					Validator.warnMessage(msgInfo_.getMsgById('SM_I016_C_0'));
					return false;	
				}
		if (document.getElementsByName("paginater.page") != null) {
	  	  document.getElementsByName("paginater.page").value = 0;
		}		 
		$('step').value = 'list';
		FormUtils.post(document.forms[0], '<c:url value='/sm/logManageAction.do'/>');
	}
</script>


