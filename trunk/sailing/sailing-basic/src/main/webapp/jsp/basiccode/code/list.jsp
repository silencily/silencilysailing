<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf"/>
<html>
<body class="list_body">
<form name="f">
	<input type="hidden" name="parentCode" value="<c:out value = "${theForm.parentCode}"/>"/>
	<input type="hidden" name="oid" value="<c:out value='${theForm.bean.id}'/>"/>
	<c:set var="parent" value="${theForm.bean}"/>
	<jsp:directive.include file="parent.jspf"/>
	<div class="update_subhead">
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td>
					<span class="switch_open" onClick="StyleControl.switchDiv(this, $('listtable'))" title="�����ڵ�"  style="font-weight:bolder">�����б�</span>
				</td>
			</tr>
		</table>
	</div>
	<div id="divId_scrollLing" class="list_scroll">
		<table border="0" cellpadding="0" cellspacing="0" class="Listing" id="listtable" onClick="TableSort.sortColumn(event)">
			<thead>
				<tr>
					<td>����</td>
					<td type='Number' style="width:70px">��ʾ˳��</td>
					<td>����</td>
					<td style="width:5%">����</td>
				</tr>
			</thead>
			<tbody>
			<c:forEach var="item" items="${theForm.list}" varStatus="status">
				<tr>
					<td>
						<input type="hidden" name="currLayer[<c:out value='${status.count}'/>]" value="<c:out value='${item.layerNum}'/>" />
						<input type="hidden" name="delState[<c:out value='${status.count}'/>]" value="<c:out value='${item.deleteState}'/>" />
						<input type="hidden" name="oid" value="<c:out value='${item.id}' escapeXml='true'/>" />
						<c:out value="${item.name}" escapeXml='true'/>&nbsp;
					</td>
					<td style="text-align:right"><c:out value="${item.showSequence}"/>&nbsp;</td>
					<td><c:out value="${item.codeDesc}" escapeXml='true'/>&nbsp;</td>				
					<td align="center">
						<input type="button" class="list_delete" onclick="CurrentPage.remove('<c:out value="${item.id}"/>','<c:out value='${status.count}'/>')" title="���ɾ��"/>
					</td>
				</tr>										
			</c:forEach>
			</tbody>
		</table>
		<div class="list_bottom">
			<c:set var = "paginater.forwardUrl" value = "/common/basiccodemanager.do?step=list" scope = "page" />
			<%@ include file = "/decorators/paginater.jspf" %>
		</div>
	</div>
</form>
<script language="javascript">
	var msgInfo_ = new msgInfo();
	var CurrentPage = {};
	CurrentPage.remove = function(oid,idx) {
		//��ѡ�ڵ㲻�ǵ�3������ǳ�������,����ɾ��
		var layerNum = document.getElementById("currLayer[" + idx + "]").value;
		var deleteState = document.getElementById("delState[" + idx + "]").value;
		if(layerNum != 3 || deleteState !=1) {
			alert(msgInfo_.getMsgById('CM_I041_C_0'));
			return false;
		}
		if (!confirm(msgInfo_.getMsgById('CM_I026_A_0'))) {
			return false;
		} 
		FormUtils.post(document.forms[0], '<c:url value = "/common/basiccodemanager.do?step=delete"/>&oid=' + oid + "&parentCode=" + $('parentCode').value);
		if(parent.codeTree){
			parent.codeTree.Update(parent.codeTree.SelectId);
		}
	}
	
	CurrentPage.create = function () {
 		var pat = new RegExp('([\?\&])parentCode=[^\&]*');
	  	var parentCode = window.parent.codeTree.TreeList[window.parent.codeTree.SelectId].selfId;
		//��2��ڵ�����Ľڵ��²��������ӽڵ�		
		var nodeLayer = document.getElementById("parentLayer").value;
		if (nodeLayer != 2) {
			alert(msgInfo_.getMsgById('CM_I040_C_0'));
			return false;
		}
 	  	var url = window.parent.panel.dataArr[1][2];
		if (pat.test(url)) {
		   	window.parent.panel.dataArr[1][2]=url.replace(pat, RegExp.$1 + "parentCode=" + parentCode+"&type=new");
		   	$('oid').value="";
		  	window.parent.panel.click(1);
		  	window.parent.panel.dataArr[1][2]=url.replace(pat, RegExp.$1 + "&parentCode=");
		}
	}

</script>
</body>
</html>
