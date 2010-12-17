<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ include file = "/decorators/default.jspf" %>

<c:set var="actionName" value="/common/messageAction.do"/>

<body>
<form enctype="multipart/form-data">	
	<input type="hidden" name="oid" value="" />
	<input type="hidden" name="step" value="<c:out value = "${theForm.step}" />" />

	<div class="list_group">	
	<div class="list_title">
				
		<c:set var ="operaName" value="ת��һ����Ϣ"/>
		<c:if test="${empty theForm.oid}">
				<c:set var ="operaName" value="�ظ�һ������Ϣ"/>
		</c:if>
		<c:out value="${operaName}"/>	
		<span class="list_notes"></span>
	</div>
</div>
<div class="update_subhead" >
	 <span class="switch_open" onClick="StyleControl.switchDiv(this,$('divId_message'))" title="����������">��Ϣ���ݣ�</span>
</div>
  
  <table border="0" cellpadding="0" cellspacing="0" class="Detail" id="divId_message">
      <tr>
        <td class="attribute">������ </td>
        <td colspan=3>
			<input type="hidden" name="personsUsername" value=""/>
			<input type="text" name="personsChinese" class="input_long" value="" readonly/>
			<input type="button" id="select_fromtree"  title="����ѡ���ѡ����Ϣ������" onClick="TreeUtils.selectAndSetUser($('personsUsername'), '', $('personsChinese'), TreeUtils.treeTypeMulti)"/>
			<span class="font_request">*</span>
		</td>
      </tr>
	  <tr>
        <td class="attribute">��Ϣ����</td>
        <td colspan=3>
			<input name="message.title" value="<c:out value = "${theForm.message.title}" />" class="input_long" maxlength="50">
			<span class="font_request">*</span>
			<!-- ��Ϣ���� -->
			<input type="hidden" name="message.type.code" value="message.sendType.01">
		</td>
      </tr>
      <tr>
        <td class="attribute">��Ϣ����</td>
        <td colspan=3>
			<input type="hidden" name="message.content" value="<c:out value = "${theForm.message.content}" />" id="content"/>
			<iframe src="<c:url value='/public/editor/editor.htm'/>" name="frame_editor" id="frame_editor"
			frameborder="0" width="100%" height="280" scrolling=no></iframe>
		</td>
      </tr>
      <tr>
        <td class="attribute">
			<a href="javascript:CurrentPage.addUpload(upload_table);">������Ӹ���</a>
		</td>
        <td colspan=3>

			<table cellspacing="0" cellpadding="0" id="upload_table" class="Blank" width=100%>
				<c:forEach var="item" items="${theForm.message.uploadFiles.files}" varStatus="status">
				<tr>
					<td>
						<input type="button" id="opera_delete_file" onclick="javascript:CurrentPage.deleteUpload(this);"/>
						&nbsp;&nbsp;<c:out value='${status.index + 1}'/>&nbsp;.&nbsp;
						<a href="javascript:CurrentPage.showFile('<c:out value = "${item.fileName}"/>');"><c:out value='${item.fileName}'/></a>
						<input name="message.uploadFiles.files[<c:out value='${status.index}'/>].fileName" value="<c:out value='${item.fileName}'/>" type="hidden"/>
					</td>
				</tr>
				</c:forEach>
			</table>

		</td>
      </tr>
	  <tr>
        <td class="attribute">��ǰ״̬</td>
        <td colspan=3> 
			<input type="radio" name="message.status.code" value="message.sendStatus.01" checked/>
			����
			<input type="radio" name="message.status.code" value="message.sendStatus.00"
			<c:if test = '${theForm.message.status.code == "message.sendStatus.00"}'>checked</c:if>
			/>
			�ݸ�			
		</td>
      </tr>

      <tr>
        <td class="attribute">��Ϣ������</td>
        <td > 
			<input type="text" name="sendPerson" value="<c:out value = '${theForm.message.author.chineseName}'/>" readonly/>
		</td>
		<td class="attribute">��������</td>
        <td >
			<input name="sendDate" type="text" value="<fmt:formatDate value = '${theForm.message.sendDate}' pattern = 'yyyy-MM-dd HH:mm:ss' />" readonly/>
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

</form>
</body>
</html>


<script type="text/javascript">

var CurrentPage = {};

CurrentPage.submit = function() {
	if (CurrentPage.validation() == false) {
		return;
	}
	var content = frames["frame_editor"].Editor.getContent();
	$('message.content').value = content;
	setAcceptPersons();
	FormUtils.post(document.forms[0], '<c:url value = "${actionName}" />?step=save');
}
function setAcceptPersons(){
	var arr1 = $('personsUsername').value.split(",");
	var arr2 = $('personsChinese').value.split(",");
	for(var i=0;i<arr1.length;i++){
		var newDiv = document.createElement("div");
		newDiv.innerHTML = ' \
			<input type="hidden" name="acceptPersons['+i+'].acceptPerson.username"    value="'+arr1[i]+'"/>\
			<input type="hidden" name="acceptPersons['+i+'].acceptPerson.chineseName" value="'+arr2[i]+'"/>\
			<input type="hidden" name="acceptPersons['+i+'].acceptDate" value="'+$('sendDate').value+'"/>\
		';
		$('divId_persons').appendChild(newDiv);		
	}
	Global.setHeight();
}

CurrentPage.create = function() {
	location = "<c:url value = '${actionName}?step=info' />";
}

CurrentPage.validation = function () {
	return Validator.Validate(document.forms[0], 4);
}

var count_of_row = document.getElementById("upload_table").rows.length;

<%-- ����Ҫ�ϴ��ĸ���, ���ҵ������۵�ʱչ���� --%>
CurrentPage.addUpload = function (obj) {
	tr = obj.insertRow();
	var str =  '<input type="button" id="opera_delete_file" onclick="javascript:CurrentPage.deleteUpload(this);"/>';
	var name = "message.uploadFiles.files[" + (count_of_row++) + "].formFile";
	var td1 = document.createElement("td");	
	td1.innerHTML = str + "&nbsp;&nbsp;<input name='" + name + "' type='file' size='50'/>";
	tr.appendChild(td1);
	Global.setHeight();
}

CurrentPage.deleteUpload = function (obj) {
	obj.parentNode.parentNode.removeNode(true);
}
<%-- ��ʾ�Ѿ��ϴ����ļ���Դ --%>
CurrentPage.showFile = function (fileName) {
	var url = "<c:url value = "${actionName}?step=downloadAttachement&oid=${theForm.oid}" />";
	$('fileName').value = fileName;
	FormUtils.post(document.forms[0],url,true);
}


CurrentPage.initValidateInfo = function () {
	var arr1 = "";
	var arr2 = "";
	<c:if test="${!empty theForm.oid}">
		<c:forEach items = "${theForm.message.acceptPersons}" var = "item" varStatus = "status" >
			arr1 += "<c:out value = '${item.acceptPerson.username}' />,"
			arr2 += "<c:out value = '${item.acceptPerson.chineseName}' />,";
		</c:forEach>
	</c:if>
    <c:if test="${!empty theForm.personName}">
		arr1 += "<c:out value = '${theForm.personName}' />,"
		arr2 += "<c:out value = '${theForm.personChinese}' />"+",";
	</c:if>
	$('personsUsername').value = arr1.substring(0,arr1.length-1);
	$('personsChinese').value  = arr2.substring(0,arr2.length-1);
	$('personsChinese').dataType = 'Require';
	$('personsChinese').msg = '�����˲���Ϊ��';
	$('message.title').dataType = 'Require';
	$('message.title').msg = '��Ϣ���ⲻ��Ϊ��';

	<c:if test="${!empty theForm.oid}">
	$("message.title").value   = "ת����" + $("message.title").value;
	$("message.content").value = " \
		<br/>   \
		<br/> <div class='box_dashed'>> \
	    <br/> > ת����Ϣ��\
		<br/> > -------------------------------------------\
		<br/> > "+ $("message.content").value +" \
		<br/> > -------------------------------------------\
		<br/> > </div>"
	$("sendPerson").value = "";
	$("sendDate").value = "";
	</c:if>
}
CurrentPage.initValidateInfo();

//refact tabSort.js
parent.panel.listFrame = parent.panel.dataArr[2][3];
</script>