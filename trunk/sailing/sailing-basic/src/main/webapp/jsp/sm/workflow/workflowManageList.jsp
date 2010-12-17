<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf" />
<%@ page import="net.silencily.sailing.basic.wf.dto.WfSearch" %>
<html>
<style type="text/css">
.showAllMessage{
	height:20px;
	cursor:pointer;
	padding-left:20px;
	background-repeat : no-repeat;
	white-space:nowrap;
	padding-top: 4px;
}
</style>
<head>
<script type="text/javascript">
	   var CurrentPage = {};
	   var Query= function(){
		   	var title = document.getElementById("wfsearchname").value;
			var state = document.getElementById("theForm.wfSearch.value").value;
			var committer= document.getElementById("wfsearchcommiter").value;
			var startTime= document.getElementsByName("datetime1")[0].value;
			var endTime= document.getElementsByName("datetime2")[0].value;
			if(startTime>endTime){
				alert("初始时间不能大于结束时间，请重新输入");
				return false;
			}
			var urlTemp=document.getElementById("qware").value + "/sm/wfManageAction.do?step=list&paginater.page=0"
			var url = urlTemp + "&title="+title+"&state="+state+"&committer="+committer+"&startTime="+startTime+"&endTime="+endTime+"&clearwf=true";
		    FormUtils.post(document.forms[0], url);
	   }
	   CurrentPage.workflowUrl = function(e) {
		   	var tmp, trObj;
			if (TableSort.global_isMSIE5) {
				tmp = e.srcElement;
			} else if (TableSort.global_isByTagName) {
				tmp = e.target;
			}
			trObj = TableSort.getParentTagName(tmp, "TR");
			var boxwfname = trObj.cells[0].childNodes[1];
			var boxurl = trObj.cells[1].childNodes[1];
			var boxStep = trObj.cells[2].childNodes[1];
			var stepId = trObj.cells[3].childNodes[1];
			var step = "system";
			var wfname = boxwfname.value;
			var wfstep = boxStep.value;
			var wfurl = boxurl.value + '&urlKey=' + step +'&stepId='+stepId.value + '&wfname='+wfname;
			var re;
			var r;
			re=/&/g;
			r=wfurl.replace(re,"|");
			var workflowurl=document.getElementById("myActionurl").value + '&wfname='+ wfname + '&wfstep=' + wfstep +'&wfurl='+r ;
			if(undefined != boxurl.value ){
				parent.parent.frames["mainFrame"].location =workflowurl;
			}
			if(undefined == boxurl.value){
				alert("当前双击位置不存在数据，跳转无效！");
			}
		}
			TableSort.dblClick = function(){};

</script>
</head>
<body class="list_body">
<form id="f">

<input type="hidden" name="step"
	value="<c:out value = '${theForm.step}' />" />
<input type="hidden" name="myActionurl" id="myActionurl"
	value="<c:out value='${initParam["publicResourceServer"]}'/>/wf/personWfSearchAction.do?step=startStep" />
<input type="hidden" name="qware"
	value="<c:out value='${initParam["publicResourceServer"]}'/>" />



<div class="update_subhead"><span class="switch_close"
	onClick="StyleControl.switchDiv(this,$(supplierQuery))"
	title="点击这里进行查询">查询条件</span></div>
<div id="supplierQuery" style="display: none">
<% 
	//取到SESSION中暂存的值
	//处理查询值回显问题
	String title = session.getAttribute("wfsearch3")== null? "" : (((WfSearch)session.getAttribute("wfsearch3")).getTitle());
	String state = session.getAttribute("wfsearch3")== null? "" : (((WfSearch)session.getAttribute("wfsearch3")).getState());
	String committer = session.getAttribute("wfsearch3")== null? "" : (((WfSearch)session.getAttribute("wfsearch3")).getCommitter());
	String startTime = session.getAttribute("wfsearch3")== null? "" : (((WfSearch)session.getAttribute("wfsearch3")).getStartTime());
	String endTime = session.getAttribute("wfsearch3")== null? "" : (((WfSearch)session.getAttribute("wfsearch3")).getEndTime());
%>
<table border="0" cellpadding="0" cellspacing="0" class="Detail"
	id="parenttable" style="display: ">
	<tr>
		<td class="attribute">任务名称</td>
		<td>
		<input type="text" id="wfsearchname" name="wfsearchname" value="<%=title%>">
		</td>
		<td class="attribute">任务状态</td>
		<td>
		<vision:choose rwCtrlType="" permissionCode=""
			value="<%=state%>" textName="theForm.wfSearch.state"
			valueName="theForm.wfSearch.value"
			source='${theForm.comboSupportList}' canDelete="true"
			comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
		</td>
	</tr>
	<tr>
		<td class="attribute">流程发起者</td>
		<td>
		<input type="text" id="wfsearchcommiter" name="wfsearchcommiter" value="<%=committer%>">
		</td>
		<td></td>
		<td></td>
	</tr>
	<tr>
		<td class="attribute">提交时间</td>
		<td colspan="2"><vision:text rwCtrlType="2" permissionCode=""
			name="datetime1" value="<%=startTime%>"  dataType="3"
			comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
		至 <vision:text rwCtrlType="2" permissionCode="" name="datetime2"
			value="<%=endTime%>" dataType="3"
			comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
		</td>
		<td></td>
	</tr>
</table>
<div class="query_button"><input type="button" value="" name=""
	id="opera_query" title="点击查询" onClick="Query();" /></div>
</div>

<div class="update_subhead">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td><span class="switch_open"
			onClick="StyleControl.switchDiv(this, $(wzlb))" title="点击收缩表格">检索结果</span>
		</td>
	</tr>
</table>
</div>

<div id="wzlb">
<div id="divId_scrollLing" class="list_scroll">
<table border="0" cellpadding="0" cellspacing="0" class="Listing"
	id="listtable" onClick="TableSort.sortColumn(event)"
	ondblclick="CurrentPage.workflowUrl(event)">
	<thead>
		<tr>
			<td nowrap="nowrap">任务名称</td>
			<td nowrap="nowrap">任务状态</td>
			<td nowrap="nowrap">步骤名称</td>
			<td nowrap="nowrap">提交时间</td>
			<td nowrap="nowrap">流程发起者</td>
			<td nowrap="nowrap">操作</td>
		</tr>
	</thead>
	<tbody id='tablist'>
		<c:forEach var="item" items="${theForm.allWfList}" varStatus="status">
			<tr>
				<td nowrap="nowrap" class="showAllMessage"  title='<c:out value="${item.title}" />'>
				         <c:out value="${item.titleCut}" /> 
				         <input type="hidden" value='<c:out  value="${item.temp}"/>' />
				         &nbsp;
				         </td>
				<td align="center"><c:out value="${item.wfState}" /> <input
					type="hidden" value='<c:out  value="${item.url}"/>' />&nbsp;</td>
				<td nowrap="nowrap"><c:out value="${item.currentStepName}" />
				<input type="hidden"
					value='<c:out  value="${item.currentStepName}"/>' />&nbsp;</td>
				<td align="center"><c:out value="${item.commitTime}" />
				    <input type="hidden" value='<c:out  value="${item.stepId}"/>' />&nbsp;</td>
				<td nowrap="nowrap"><c:out value="${item.commiter}" />&nbsp;</td>
				<td align="center"><input type="button" name="Submit1" value="详细" onClick="CurrentPage.workflowUrl(event)"></td>
			</tr>
		</c:forEach>
	</tbody>
</table>
</div>
<div class="list_bottom">
	<c:set var="paginater.forwardUrl" value="/sm/wfManageAction.do?step=list" scope="page" /> 
	<%@include file="/decorators/paginater.jspf"%> 
</div>
</div>
</form>
</body>
</html>



