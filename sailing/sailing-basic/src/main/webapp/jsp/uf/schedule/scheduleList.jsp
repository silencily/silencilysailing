<%--
    @version:$Id: scheduleList.jsp,v 1.1 2010/12/10 10:56:18 silencily Exp $
    @since $Date: 2010/12/10 10:56:18 $
--%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf" />
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK"/>
</head>
<script language="javascript">
	var CurrentPage = {};
	CurrentPage.myquery = function() {
		if (document.getElementsByName("paginater.page") != null) {
		    document.getElementsByName("paginater.page").value = 0;
		}
		$('currentDay').value='';
		FormUtils.post(document.forms[0], '<c:url value="/uf/schedule/ScheduleAction.do?step=list"/>');
	}
	CurrentPage.remove = function(oid) {
		if (!confirm('是否要删除选择的记录?')) {
			return false;
		} 
		FormUtils.post(document.forms[0], '<c:url value = "/uf/schedule/ScheduleAction.do?step=delete"/>&oid='+ oid );
	}
	
	CurrentPage.create = function () 
	{
		len = divId_requQues.rows.length;
		if(len<2)
		{
			TableSort.setNoSelect();
			TableSort.dblClick();
		}else
		{
			$('oid').value = '';
			TableSort.setNoSelect();
			TableSort.dblClick();
		}
	}
</script>
<body>
<form name="f" action="" method="post">
<input type="hidden" name="step" value="<c:out value = "${theForm.step}"/>"/>
<input type="hidden" name="currentDay" value="<c:out value = "${theForm.currentDay}"/>"/>
<jsp:directive.include file="search.jspf"/>
	<div class="update_subhead">
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td>
					 <span class="switch_open" onClick="StyleControl.switchDiv(this,$('divId_scrollLing'))" title="点击收缩表格">日程安排列表</span>
				</td>
			</tr>
		</table>
	</div>
	<div id="divId_scrollLing" class="list_scroll">
		<table border="0" cellpadding="0" cellspacing="0" class="Listing" id="divId_requQues" onClick="TableSort.sortColumn(event)">
			<thead>
				<tr>
					<td width="2%" align="left"><input name="chkall" type="checkbox" value="" 
					id="detailIdsForPrintAll" onClick="FormUtils.checkAll(this, document.getElementsByName('detailIds'))" title="是否全选" /></td>					
					<td>主题</td>
					<td>开始时间</td>
					<td>结束时间</td>
					<td>完成状态</td>
					<td width="5%">操作</td>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="item" items="${theForm.list}" varStatus="status">
					<tr id="requQues_trId_[<c:out value='${status.index}'/>]" >
						<td align="center"  width="2%">
							<input type="hidden" name="oid" value="<c:out value='${item.id}'/>" />
							<input name="detailIds" id="rid" type="checkbox" value="<c:out value='${item.id}'/>" onClick="FormUtils.check($('detailIdsForPrintAll'), document.getElementsByName('detailIds'))"/>
						</td>
						
						<td align="left"><c:out value='${item.title}'/></td>
						<td align="center"><c:out value='${item.begTime}'/></td>
						<td align="center"><c:out value='${item.endTime}'/></td>
						<td align="left"><c:out value='${item.completeFlg.name}'/></td>
						<td align="center"><input type="button" class="list_delete" onclick="CurrentPage.remove('<c:out value="${item.id}"/>')" /></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<div class="list_bottom">&nbsp;
			<c:set var ="paginater.forwardUrl" value = "/uf/schedule/ScheduleAction.do" scope = "page" />
			<%@ include file = "/decorators/paginater.jspf" %>
			<input type="button" class="opera_export" title="导出Excel" onClick="Print.exportExcel($('divId_scrollLing'))" value=""/>
<!-- 			<input type="button" name="" class="opera_batchdelete" value="批量删除" onClick='CurrentPage.deleteall()'/> -->
		</div>
		</div>
	</form>
</body>