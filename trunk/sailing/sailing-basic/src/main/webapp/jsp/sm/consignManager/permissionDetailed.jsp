<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf"/>
<jsp:directive.include file="/decorators/edit.jspf"/>
<!-- liujinliang 2007-09-21 14:00 -->
<html>
<title>Ȩ����ϸ</title>
<body class="list_body"  onLoad="CurrentPage.returnShow">
<script language="javascript">

	if (CurrentPage == null) {
    	var CurrentPage = {};
		} 
		
		  
	CurrentPage.returnPermission = function() {
		var strOid = document.getElementById('oid').value;
		var startTime = document.getElementById('startTime').value;
		var endTime = document.getElementById('endTime').value;
		var begin = strOid + ".beginTime";
		var end = strOid + ".invalidTime";
		window.opener.document.getElementById(begin).innerHTML = startTime;
		window.opener.document.getElementById(end).innerHTML = endTime;
		window.close();
		$('step').value = 'permissionSave';	
		FormUtils.post(document.forms[0], '<c:url value='/sm/ConsignManagerAction.do'/>');
	}
	
	CurrentPage.returnShow = function() {
	} 	
	
</script>
<form name="f" on >
<input type="hidden" name="oid"  id= "oid" value="<c:out value='${theForm.oid}'/>"/>
<input type="hidden" name="step" value="<c:out value = '${theForm.step}' />" />
	<div class="main_body">
	<div class="list_group">
	<div align="right" class="update_subhead"><input type="button" value="����" onClick="CurrentPage.returnPermission();"/><input type="reset" value="ȡ��" onClick="window.close()"/></div>
	</div>
	<div class="main_title"> <div class="main_title"> Ȩ����ϸ��Ϣ</div>
	</div>

	<table border="0" cellpadding="0" cellspacing="0" class="Detail"
		id="submenu1">
		<tr>
			<td class="attribute">Ȩ������</td>
			<td>
			<input type="text" name="temp.cmnPermission.displayname" value="<c:out value='${theForm.userPermissionBean.tblCmnPermission.displayname}'/>" class="readonly" readonly="readonly" />
			</td>
		</tr>	
		<tr>
			<td class="attribute">��ʼʱ��</td>
			<td>
			<input type="text" id ="startTime" name="userPermissionBean.beginTime" value="<c:out value='${theForm.userPermissionBean.beginTime}'/>" />
			</td>
		</tr>
		<tr>
			<td class="attribute">ʧЧʱ��</td>
			<td>
			<input type="text" id ="endTime" name="userPermissionBean.invalidTime" value="<c:out value='${theForm.userPermissionBean.invalidTime}'/>" />
			</td>
		</tr>
		</table>
		</div>
</form>
</body>
</html>
