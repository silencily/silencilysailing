<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf" />
<html>
<head>
<script type="text/javascript">
	   var CurrentPage = {};
	   var Query= function(){
		   	var title = document.getElementById("wfsearchname").value;
			var committer= document.getElementById("wfsearchcommiter").value;
			var startTime= document.getElementsByName("datetime1")[0].value;
			var endTime= document.getElementsByName("datetime2")[0].value;
			if(startTime>endTime){
				alert("��ʼʱ�䲻�ܴ��ڽ���ʱ�䣬����������");
				return false;
			}
			var urlTemp=document.getElementById("qware").value + "/wf/personWfSearchAction.do?step=entrustList&paginater.page=14"
			var url = urlTemp + "&title="+title+"&state="+state+"&committer="+committer+"&startTime="+startTime+"&endTime="+endTime;
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
			var step = "entrustList";
			var wfname = boxwfname.value;
			var wfstep = boxStep.value;
			var wfurl = boxurl.value + '&urlKey=' + step;
			var re;
			var r;
			re=/&/g;
			r=wfurl.replace(re,"|");
			var workflowurl=document.getElementById("myActionurl").value + '&wfname='+ wfname + '&wfstep=' + wfstep +'&wfurl='+r ;
			if(undefined != boxurl.value ){
				parent.parent.frames["mainFrame"].location =workflowurl;
			}
			if(undefined == boxurl.value){
				alert("��ǰ˫��λ�ò��������ݣ���ת��Ч��");
			}
		}
			TableSort.dblClick = function(){};
	</script>
</head>
<body class="list_body">
<input type="hidden" name="step"
	value="<c:out value = '${theForm.step}' />" />
<input type="hidden" name="myActionurl" id="myActionurl"
	value="<c:out value='${initParam["publicResourceServer"]}'/>/wf/personWfSearchAction.do?step=startStep" />
<input type="hidden" name="qware"
	value="<c:out value='${initParam["publicResourceServer"]}'/>" />


<!--��ǰҳ���step����-->
<form id="f">

<div class="update_subhead"><span class="switch_close"
	onClick="StyleControl.switchDiv(this,$(supplierQuery))"
	title="���������в�ѯ">��ѯ����</span></div>
<div id="supplierQuery" style="display: none">
<table border="0" cellpadding="0" cellspacing="0" class="Detail"
	id="parenttable" style="display: ">
	<tr>
		<td class="attribute">��������</td>
		<td><vision:text rwCtrlType="2" permissionCode=""
			id="wfsearchname" name="wfsearchname" value="" dataType="0"
			clazz="normal"
			comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
		</td>
		<td class="attribute">�ύ�û�</td>
		<td><vision:text rwCtrlType="2" permissionCode=""
			id="wfsearchcommiter" name="wfsearchcommiter" value="" dataType="0"
			clazz="normal"
			comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
		</td>
	</tr>
	<tr>
		<td class="attribute">�ύʱ��</td>
		<td colspan="2"><vision:text rwCtrlType="2" permissionCode=""
			name="datetime1" value="" dataType="3"
			comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
		�� <vision:text rwCtrlType="2" permissionCode="" name="datetime2"
			value="" dataType="3"
			comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
		</td>
		<td></td>
	</tr>
</table>
<div class="query_button"><input type="button" value="" name=""
	id="opera_query" title="�����ѯ" onClick="Query();" /></div>
</div>

<div class="update_subhead">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td><span class="switch_open"
			onClick="StyleControl.switchDiv(this, $('listtable'))" title="����������">�������</span>
		</td>
	</tr>
</table>
</div>


<div id="divId_scrollLing" class="list_scroll">
<table border="0" cellpadding="0" cellspacing="0" class="Listing"
	id="listtable" onClick="TableSort.sortColumn(event)"
	ondblclick="CurrentPage.workflowUrl(event)">
	<thead>
		<tr>
			<td nowrap="nowrap">��������</td>
			<td nowrap="nowrap">����״̬</td>
			<td nowrap="nowrap">��������</td>
			<td nowrap="nowrap">�ύʱ��</td>
			<td nowrap="nowrap">����</td>
		</tr>
	</thead>
	<tbody id='tablist'>
		<c:forEach var="item" items="${theForm.entrustList}" varStatus="status">
			<tr>
				<td nowrap="nowrap"><c:out value="${item.title}" /> <input
					type="hidden" value='<c:out  value="${item.wfName}"/>' />&nbsp;</td>
				<td nowrap="nowrap"><c:out value="${item.wfState}" /> <input
					type="hidden" value='<c:out  value="${item.url}"/>' />&nbsp;</td>
				<td nowrap="nowrap"><c:out value="${item.currentStepName}" />
				<input type="hidden"
					value='<c:out  value="${item.currentStepName}"/>' />&nbsp;</td>
				<td nowrap="nowrap"><c:out value="${item.commitTime}" />&nbsp;</td>
				<td align="center"><input type="button" name="Submit1" value="��ϸ" onClick="CurrentPage.workflowUrl(event)">&nbsp;</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
</div>
<div class="list_bottom">
	<c:set var="paginater.forwardUrl" value="/wf/personWfSearchAction.do?step=entrustList" scope="page" /> 
	<%@include file="/decorators/paginater.jspf"%> 
</div>
</form>
</body>
</html>