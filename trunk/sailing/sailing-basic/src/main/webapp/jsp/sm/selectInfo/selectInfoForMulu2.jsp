<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf"/>
<script type="text/javascript">
if (CurrentPage == null) {
  var CurrentPage = {};
} 
CurrentPage.query = function() {
	if (document.getElementsByName("paginater.page") != null) {
	    document.getElementsByName("paginater.page")[0].value = 0;
	}
	FormUtils.post(document.forms[0], '<c:url value='/sm/RoleAction.do?step=roleSearch&selectInfo=selectInfo'/>'); 
}
</script>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK"/>
</head> 
 
<body>
 
<form name="form">
  <div class="list_group" id="list_group" >
    <div class="list_button">
      <span class="list_notes"></span>
      <input type="button" class="opera_normal" value="���" title="��ӵ�����ѡ��Χ" onclick="CurrentPage.AddChecked('listTable','choiceTable','tools_','��ɫ')" />
      <input type="button" class="opera_normal" value="�鿴" title="�鿴��ѡ��Ľ�ɫ" onclick="viewSelectedReqPlans(this);" id="viewBtn" />      
    </div>
  </div>
  <div id = "validateInfo"></div>
<div id="tableObjction" style="display:none" class="update_subhead">
  <table width="100%" id="tableObjc"  border="0" cellpadding="0" cellspacing="0">
    <tr>
      <td>
        <span id="divSpan" class="switch_open" onClick="StyleControl.switchDiv(this, $('choiceTable'));doChangeButton(choiceTable);" title="����������">
           ��ѡ��Ľ�ɫ
        </span>
      </td>
    </tr>
  </table>
</div>
<div id="divId_scrollLing" class="list_scroll">
  <table border="0" cellpadding="0" cellspacing="0" class="Listing" id="choiceTable" onClick="TableSort.sortColumnOnly(event)" >
    <thead>
      <tr>
	        <td nowrap="nowrap">����</td>
       		<td nowrap="nowrap">��ɫ��ʶ</td>
			<td nowrap="nowrap">��ʾ����</td>
			<td nowrap="nowrap">��ɫ·��</td>
			<td nowrap="nowrap">ϵͳ��ɫ</td>
			<td nowrap="nowrap">˵��</td>
      </tr>
    </thead>
    <tbody>
    </tbody>
  </table>
</div>
<input type="hidden" name="parentCode" id="parentCode" value="<c:out value = "${theForm.parentCode}"/>"/>
<input type="hidden" name="step" value="<c:out value = '${theForm.step}' />" />
<iframe id ="list" name="main" src='<c:url value="/sm/RoleAction.do?step=selectForRole&paginater.page=0&parentCode=${theForm.parentCode}"/>'  frameborder="0" width="100%" scrolling=no>
	</iframe>
</form>
<script type="text/javascript">  
  StyleControl.switchDiv(divSpan, choiceTable);
 if (CurrentPage == null) {
	    var CurrentPage = {};
	}    
	var msgInfo_ = new msgInfo();
	var listObject = new Object();
	
//definedWin����÷���
CurrentPage.onLoadSelect = function(){
	top.definedWin.selectListing = function(inum) {
    var oids = document.getElementsByName("newoid");
 	var strIds = new Array();
 	if(oids.length == 0){
 	  	alert("��ѡ������һ��Ȩ��");
  		return;
 	}
   	for(var i = 0; i < oids.length; i++) {
		strIds[i] = oids[i].value;
 	}
	top.definedWin.listObject(strIds);
	}
}
//�ر�
top.definedWin.closeListing = function(inum) {
	listObject.selectWindow();
}

CurrentPage.onLoadSelect();


  CurrentPage.validation = function () {
    return Validator.Validate(document.forms[0], 4);
  }
  
  function viewSelectedReqPlans(buttonObject) {
    if (buttonObject.value == '�鿴') {
      buttonObject.value = '����';
      buttonObject.title = '������ѡ��Ĺ�����';
      StyleControl.switchDiv(divSpan, choiceTable);
      document.getElementById("tableObjction").style.display='';
    } else {
      buttonObject.value = '�鿴';
      buttonObject.title = '�鿴��ѡ��Ĺ�����';
      StyleControl.switchDiv(divSpan,choiceTable);     
      document.getElementById("tableObjction").style.display='none';
    }
    Global.setHeight();
  }
//�������
CurrentPage.selectAlready = function(tblSelect,oid){
	var newoids = document.getElementsByName("newoid");
	for(var i=0; i < newoids.length;i++){
		if(oid == newoids[i].value){
	  		return true;
	  	}
	}
	return false;
}
CurrentPage.AddChecked = function(item,choice,strId,itemName){
        var tblCheck = document.frames('list').document.getElementById(item);
        var tblSelect = document.getElementById(choice);
        var index = tblSelect.getElementsByTagName("tr").length-1;
        var number =0;
        for (var i=1 ;i<tblCheck.getElementsByTagName("tr").length;i++){ 
            if (document.frames('list').document.getElementsByName("oldOid")[i - 1].checked ){
            	var oid = document.frames('list').document.getElementsByName("oldOid")[i - 1].value;
            	if(!CurrentPage.selectAlready(tblSelect,oid)){
            	number++;
            	//û���ظ��ģ�
            	var insTr = tblSelect.insertRow();
                var cheTr = tblCheck.rows(i); 
                   for (var j = 0;j < cheTr.getElementsByTagName("td").length;j ++){ 
                	
                	var insTd = insTr.insertCell();
                	insTd.setAttribute("name","")
                	if (j == 0){ 
                		 var id = document.frames('list').document.getElementById(strId+(i-1)).value;
                		  var rowValue = document.frames('list').document.getElementById("ojbName"+(i-1)).value; 
                	     var idhtml = ' <input type="checkbox" style=display:none checked="true" name="newoid" value="'+id+'"/> <input type="hidden" id="detailId_'+(i-1)+'"  name="detailId_'+(i-1)+'" value="'+id+'"/>';
                	    var delhtml = '<input type="button" row="'+rowValue+'" class="list_delete" title="ɾ��"     onclick="delSelectRow(this,'+choice+');" />';
                	    insTd.setAttribute("align", "center");
                	    insTd.innerHTML = idhtml+delhtml;
                	}else{
                	   if(cheTr.cells(j).getAttribute("align")){ 
                	   insTd.setAttribute("align", "right");
                	   } 
                	   if(cheTr.cells(j).style.display=='none'){ 
                	   insTd.style.display='none'
                	   } 
                	    insTd.innerHTML = cheTr.cells(j).innerHTML;
                	}
                  }
            	}      
            }
        } 
         var msgInfo_ = new msgInfo();
         Validator.clearValidateInfo();
         var msg1 = msgInfo_.getMsgById('AM_I159_R_0',[itemName]);
         var msg2 = msgInfo_.getMsgById('AM_I157_R_0',[number,itemName]);
         var message = number == 0 ? msg1 : msg2;
         Validator.successMessage(message);
}  
</script>
</body>
</html>


