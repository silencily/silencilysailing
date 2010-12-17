<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ include file = "/decorators/default.jspf" %>

<c:set var="actionName" value="/common/messageAction.do"/>

<body>
<form enctype="multipart/form-data">	
	<input type="hidden" name="oid" value="<c:out value = "${theForm.oid}" />" />
	<input type="hidden" name="step" value="<c:out value = "${theForm.step}" />" />

	<div class="list_group">	
	<div class="list_title">
			�鿴 <c:out value = '${theForm.message.author.chineseName}'/> ���͵���Ϣ		
		<span class="list_notes"></span>
	</div>
	<div class="list_tools">
		<input type="button" class="opera_normal"  value="�ظ�" onClick="CurrentPage.createReplay()"/>
		<input type="button" class="opera_normal"  value="ת��" onClick="CurrentPage.createMove()"/>
	</div>
</div>
<div class="update_subhead" >
	 <span class="switch_open" onClick="StyleControl.switchDiv(this,$('divId_message'))" title="����������">��Ϣ���ݣ�</span>
</div>
  
  <table border="0" cellpadding="0" cellspacing="0" class="Detail" id="divId_message">
      <tr>
        <td class="attribute">������ </td>
        <td colspan=3>
			<ul id="personsUsername">
				&nbsp;
			</ul>
		</td>
      </tr>
	  <tr>
        <td class="attribute">��Ϣ����</td>
        <td colspan=3>
			<c:out value = "${theForm.message.title}" />&nbsp;
			<input type="hidden" name="message.title" value="<c:out value = "${theForm.message.title}" />" />
			<!-- ��Ϣ���� 
			<input type="hidden" name="message.type.code"-->
		</td>
      </tr>
      <tr>
        <td class="attribute">��Ϣ����</td>
        <td colspan=3 >
			<pre class="content_box" style="width:780px;overflow:auto"><c:out value = "${theForm.message.content}" escapeXml="false"/>&nbsp;</pre>
			<input type="hidden" name="message.content" value="<c:out value = "${theForm.message.content}" />" />
		</td>
      </tr>
      <tr>
        <td class="attribute">
			��ظ���
		</td>
        <td colspan=3>

			<table cellspacing="0" cellpadding="0" id="upload_table" class="" style="border:0px;">
				<c:forEach var="item" items="${theForm.message.uploadFiles.files}" varStatus="status">
				<tr>
					<td style="border:0px;">
						&nbsp;&nbsp;<c:out value='${status.index + 1}'/>&nbsp;.&nbsp;
						<a href="javascript:CurrentPage.showFile('<c:out value = "${item.fileName}"/>');"><c:out value='${item.fileName}'/></a>
						<input name="message.uploadFiles.files[<c:out value='${status.index}'/>].fileName" value="<c:out value='${item.fileName}'/>" type="hidden" id="uploadFiles"/>
					</td>
				</tr>
				</c:forEach>
			</table>

		</td>
      </tr>
      <tr>
        <td class="attribute">��Ϣ������</td>
        <td > 
			<c:out value = '${theForm.message.author.chineseName}'/>
			<input type="hidden" name="personName" 
			value="<c:out value = '${theForm.message.author.username}'/>"/>
			<input type="hidden" name="personChinese"
			value="<c:out value = '${theForm.message.author.chineseName}'/>"/>
		</td>
		<td class="attribute">��������</td>
        <td >
			<fmt:formatDate value = '${theForm.message.sendDate}' pattern = 'yyyy-MM-dd HH:mm:ss' />
		</td>
      </tr>
    </table>
    
<!--div class="update_subhead">
	 <span class="switch_open" onClick="StyleControl.switchDiv(this,divId_setting)" title="����������">
		��Ϣ����
	 </span>
</div> 
<table border="0" cellpadding="0" cellspacing="0" class="Detail" id="divId_setting">
     <tr>
        <td class="attribute">��Ϣ��Ҫ��</td>
        <td >
			
		</td>
		<td class="attribute">��Ϣ</td>
        <td >
		
		</td>
     </tr>
</table-->

<!-- ��Ŷ���û�-->
<div id="divId_persons" style="display:">
	<div ></div>
</div>

<input type="hidden" name="fileName" value=""/>
<input type="hidden" name="status" value=""/>

</form>

</body>
</html>


<script type="text/javascript">

var CurrentPage = {};

/*
CurrentPage.submit = function() {
	if (CurrentPage.validation() == false) {
		return;
	}
	setAcceptPersons();
	FormUtils.post(document.forms[0], '<c:url value = "${actionName}" />?step=save');
}
*/
CurrentPage.create = function() {
	FormUtils.disableBody();
	var url  = "<c:url value = '${actionName}?step=info' />";
	if(parent.panel.selectId == 3)
		location = url;
	else{
		parent.panel.switchTab(3);	
		FormUtils.post(document.forms[0], url, true, parent.panel.dataArr[3][3]);	
	}
}

//�ظ���Ϣ
CurrentPage.createReplay = function() {	
	$('oid').disabled = true;
	$("status").value = "message.acceptStatus.02";
	var url  = "<c:url value = '${actionName}?step=detailSend' />";
	for(var i=0;i<document.getElementsByName("uploadFiles").length;i++){
		document.getElementsByName("uploadFiles")[i].disabled = true;
	}
	$("message.title").value   = "�ظ���" + $("message.title").value;
	$("message.content").value = " \
	<br/>   \
	<div class='box_dashed'> > \
	<br/> > ��ʷ��Ϣ��\
	<br/> > -------------------------------------------\
	<br/> > "+ $("message.content").value +" \
	<br/> > -------------------------------------------\
	<br/> > </div> ";
	if(parent.panel.selectId == 3)
		FormUtils.post(document.forms[0],url);
	else{
		parent.panel.switchTab(3);	
		FormUtils.post(document.forms[0], url, true, parent.panel.dataArr[3][3]);
	}
}
//ת��
CurrentPage.createMove = function(){
	$("status").value = "message.acceptStatus.03";
	var url  = "<c:url value = '${actionName}?step=detailSend' />";
	if(parent.panel.selectId == 3)
		FormUtils.post(document.forms[0],url);
	else{
		parent.panel.switchTab(3);	
		FormUtils.post(document.forms[0], url, true, parent.panel.dataArr[3][3]);
	}
}


<%-- ��ʾ�Ѿ��ϴ����ļ���Դ --%>
CurrentPage.showFile = function (fileName) {
	var url = "<c:url value = "${actionName}?step=downloadAttachement&oid=${theForm.oid}" />";
	$('fileName').value = fileName;
	FormUtils.post(document.forms[0],url,true);
}


CurrentPage.initValidateInfo = function () {
	var arr1 = "";
	<c:forEach items = "${theForm.message.acceptPersons}" var = "item" varStatus = "status" >
		arr1 += "<li><c:out value = '${item.acceptPerson.chineseName}' />";
		arr1 += "��<c:out value = '${item.acceptPerson.username}' />����";	
		arr1 += " <c:out value = '${item.status.name}' default='δ�Ķ�'/> ";
		arr1 += " <span class='font_ignore'><fmt:formatDate value = '${item.lastModifiedTime}' pattern = 'yyyy-MM-dd HH:mm:ss'/></span> ";
		//arr1 += '<input type="text" name="acceptPersons[<c:out value = '${status.index}'/>].acceptPerson.username"  value="<c:out value = '${item.acceptPerson.username}' />"/>\';
		arr1 += "</li>";
	</c:forEach>
	$('personsUsername').innerHTML = arr1;
}
CurrentPage.initValidateInfo();

//refact tabSort.js
parent.panel.listFrame = parent.panel.dataArr[0][3];
</script>