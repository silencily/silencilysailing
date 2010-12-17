<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ include file="/decorators/default.jspf" %>

<%-- jsp1.2 --%>

<script type="text/javascript">
var msgInfo_ = new msgInfo();
if (CurrentPage == null) {
   		var CurrentPage = {};
	}
	

 //����һ����¼��
CurrentPage.create = function() {
	$('oid').value = '';
	$('step').value = 'info';
	FormUtils.post(document.forms[0], '<c:url value='/wf/operationlist.do?step=info'/>');
}

 // createXml()��������XML��
CurrentPage.createXml = function(){
	FormUtils.post(document.forms[0],'<c:url value = "/wf/operationlist.do?step=createXML" />');
}

CurrentPage.reset = function () {
			document.f.reset();
		}
CurrentPage.submit = function() {    
    CurrentPage.initValideInfo();
    	if (CurrentPage.validation()==false) {
		return false;
	}
	if(!verifyAllform()){return false;}
	if (CurrentPage.CheckNum(document.getElementById('wfoperinfo.stepNum')) == false) {
	    return false;
	}
	
	var id = $('oid').value;
	var a1="";
	var b1 = document.getElementById('flowManager');
	
	for(i=0;i<b1.length;i++){
	     a1 +=b1.options[i].value+",";
	}	
	if (id == null || id.length == 0) {
	       
		$('step').value = 'save';		 
		FormUtils.post(document.forms[0], '<c:url value ='/wf/operationlist.do?step=save&flowManager='/>'+a1);	
	} else {
		$('step').value = 'update';		
		FormUtils.post(document.forms[0], '<c:url value ='/wf/operationlist.do?step=update&flowManager='/>'+a1);
	}
}

CurrentPage.validation = function () {	
	return Validator.Validate(document.forms[0],5);
}

CurrentPage.initValideInfo = function () {
        
		document.getElementById('wfoperinfo.name').dataType = 'Require';
		document.getElementById('wfoperinfo.name').msg = msgInfo_.getMsgById('HR_I068_C_1',['��������']);
		document.getElementById('wfoperinfo.stepNum').dataType = 'Require';
		document.getElementById('wfoperinfo.stepNum').msg = msgInfo_.getMsgById('HR_I068_C_1',['��������']);
		document.getElementById('wfoperinfo.className').dataType = 'Require';
		document.getElementById('wfoperinfo.className').msg = msgInfo_.getMsgById('HR_I068_C_1',['�־û�����']);
}
CurrentPage.CheckNum = function(name){
    var str=name.value
    if (str!=""){
         var pattern =/^[0-9]+$/;
		     if(!pattern.test(str)){
		         Validator.warnMessage(msgInfo_.getMsgById('HR_I081_C_2',['����ֵ','0~999']));
		         name.focus();
		         return false;
		        
		     }else{
		         var number = parseInt(str);	
		  		 if (number >= 0 && number <= 999)
		  		 {
		  	   	    return true;
		  		 }else{
		  			Validator.warnMessage(msgInfo_.getMsgById('HR_I081_C_2',['����ֵ','0~999']));
		  			name.focus();
		            return false;
		       	}
		    }
	}else{
		return true;
	}
}

function createFlowManager(flowManager){
	definedWin.openListingUrl('permissionAdd','<c:url value = "/sm/userManageAction.do?step=selectInfo&strFlg=flow&paginater.page=0" />');
}
function goGraphics(){
	var id = $('oid').value;
	//FormUtils.post(document.forms[0], '<c:url value ='/wf/GraphicsAction.do?step=wfNoEntityInfo&idw='/>'+id);
	definedWin.openListingUrl('goGraphics','<c:url value = "/wf/GraphicsAction.do?step=wfNoEntityInfo&idw="/>'+id,0,true);
}
definedWin.returnListObject = function(ids){
	var userId =new Array();
	var userName =new Array();
	var strId ="";
	var strName="";
	if ( ids != null) {
		strId = ids[0];
		strName =ids[1];
	}
	userId = strId.split(";");
	userName = strName.split(";");
	var i = userId.length;
	var opt;
	for(i;i>0;i--){
		opt = new Option();
		opt.value = userId[i-1];
		opt.text = userName[i-1];
		//opt.selected = true;
		if(userId[i-1]!=""){
			document.forms[0].flowManager.options.add(opt);
		}
	}
}

function deletes(flowManager){
    Control1 = null;
    Control1=flowManager;
    var opt;
    var j=Control1.length;
    
    if(j==0) return;
    for(j;j>0;j--)
    {
        if(Control1.options[j-1].selected==true)
        {    
            opt = new Option();
            opt.text =Control1.options[j-1].text;
            opt.value=Control1.options[j-1].value;
            Control1.options[j-1] = null;
        }
    }
}

<%------------------------------------------------------------------------------------------------%>
 <%--
  CurrentPage.deletePermissionsAll= function() {			
		var str="";
		var dd=document.getElementsByName('detailIds');
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
			$('step').value = 'deletePermissionsAll';
			$('oids').value = str;
	   		FormUtils.post(document.forms[0], '<c:url value="/sm/userManageAction.do"/>' );
   		}
   }
   
   	CurrentPage.myquery = function() {
	if (document.getElementsByName("paginater.page") != null) {
	    document.getElementsByName("paginater.page").value = 0;
	}
	FormUtils.post(document.forms[0], '<c:url value="/sm/userManageAction.do?step=queryPermissions"/>');
	}
--%>   
   CurrentPage.removeParticular = function(sel) {
	if (! confirm(msgInfo_.getMsgById('SM_I009_A_0'))) {
		return;
	}
	var oid1=sel.id;
	$('step').value = 'deleteparticular';
	FormUtils.post(document.forms[0], '<c:url value = "/wf/operationlist.do?step=deleteParticular&oid1=" />' + oid1 );
}
/////////////////////////////////////////////////////////
   var boxObj;
   CurrentPage.updateParticular = function(e) {
    
   	var tmp, trObj;
	if (TableSort.global_isMSIE5) {
		tmp = e.srcElement;
		
	} else if (TableSort.global_isByTagName) {
		tmp = e.target;
	}
    trObj = TableSort.getParentTagName(tmp, "TR");
    
	if(trObj.id=="tableHead")
	{
		return false;
	}
	if(boxObj!=undefined){
		boxObj.checked = false;
	}
	boxObj = trObj.cells[0].childNodes[0];
	var oid2=boxObj.value;
	boxObj.checked = true;
	boxObj.onfocus();
	
	var ooid= $('oid').value;
	// definedWin.openModalWindow('particularUpdate','<c:url value = "/wf/operationlist.do?step=particularlist&oid2="/>' +oid2+'&ooid='+ooid,800,700);
    definedWin.openListingUrl('particularUpdate','<c:url value = "/wf/operationlist.do?step=particularInfo&oid2="/>' +oid2+'&ooid='+ooid,0,true);
	// window.open('<c:url value = "/wf/operationlist.do?step=particularlist&oid2="/>' +oid2+'&ooid='+ooid, "newwindow", "height=500, width=800, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no");
}
   TableSort.dblClick = function()
   {
   	   return false;
   }
CurrentPage.addpart=function(){
      var oid2 ="" ;
      var ooid=$('oid').value;
      
      if( ooid=="" || ooid==null){
         alert("�޷��½����裬����ԭ�򣺹����������ڣ�");
         return;
      }
      
      definedWin.openListingUrl('addparticular','<c:url value = "/wf/operationlist.do?step=particularInfo&oid2="/>' +oid2+'&ooid='+ooid,0,true);

}
/*
  top.definedWin.myfun = function(arr){
		var flg = arr[0];
		if(flg == "particularUpdate"){
			var strStepId = arr[1] + ".stepId";
			var strStepName = arr[1] + ".stepName";		
			var strDealInfo = arr[1] + ".dealInfo";
			var strGoback = arr[1] + ".goback";
	        var strGobackStep = arr[1] + ".gobackStep";
	        var strCancel = arr[1] + ".cancel";
	        var strFetch = arr[1] + ".fetch";
	        var strSuspend = arr[1] + ".suspend";
	        var strPassround = arr[1] + ".passround";
	        var strCommit = arr[1] + ".commit";
	        var strCommitStep = arr[1] + ".commitStep";
	        var strTogethersign = arr[1] + ".togethersign";
	        var strHelpman = arr[1] + ".helpman";
	        var strMessage = arr[1] + ".message";
	        var strTblUserId = arr[1] + ".tblUserId";
	
			document.getElementById(strStepId).innerHTML = arr[2];
			document.getElementById(strStepName).innerHTML = arr[3];
			document.getElementById(strDealInfo).innerHTML = arr[4];	
			document.getElementById(strGoback).innerHTML = arr[5];
			document.getElementById(strGobackStep).innerHTML = arr[6];
			document.getElementById(strCancel).innerHTML = arr[7];
			document.getElementById(strFetch).innerHTML = arr[8];
			document.getElementById(strSuspend).innerHTML = arr[9];
			document.getElementById(strPassround).innerHTML = arr[10];
			document.getElementById(strCommit).innerHTML = arr[11];
			document.getElementById(strCommitStep).innerHTML = arr[12];
			document.getElementById(strTogethersign).innerHTML = arr[13];
			document.getElementById(strHelpman).innerHTML = arr[14];
			document.getElementById(strMessage).innerHTML = arr[15];
			// document.getElementById(strTblUserId).innerHTML = arr[16];
		}else{
		    	
		}
 }
*/
top.definedWin.backReflesh = function(){
    window.location.reload();
}

</script>
<%-- ------------------------------------------------------------------------------------------------ --%>
<html>
<body class="list_body">
<form id="f" method="post">
	<input type="hidden" name="oid" value="<c:out value = '${theForm.oid}' />" />
	<input type="hidden" name="step" value="<c:out value = '${theForm.step}' />" />

	<div class="main_body">
	    <div class="update_subhead">
	         <span class=" switch_open" onClick="StyleControl.switchDiv(this, $('submenu1'))" title="�����ڵ�">������Ϣ</span>
	    </div>
	    <table border="0" cellpadding="0" cellspacing="0" class="Detail" id="submenu1"> 
		    	              
		<tr>
			<td class="attribute">��������</td>
			<td>
			  <ec:composite value="${theForm.wfoperinfo.wftype}" textName="temp.wfoperinfo.wftype.code" valueName="wfoperinfo.wftype.code" source = '${theForm.sysCodes["WF"]["LCLX"]}'/>
	        </td>
			<td class="attribute">���̰汾</td>
			<td>
			  <input id="wfoperinfo.edition" name="wfoperinfo.edition" value="<c:out value='${theForm.wfoperinfo.edition}'/>" /></td>
	    </tr>
		<tr>
			<td class="attribute">��������</td>
			<td>
			  <input type="text" id="wfoperinfo.name" name="wfoperinfo.name" value="<c:out value='${theForm.wfoperinfo.name}'/>"/>
			   <span class="font_request">*</span>
			</td>
			  
			<td class="attribute">ʹ��״̬</td>
			<td>
			   <ec:composite value="${theForm.wfoperinfo.status}" textName="temp.wfoperinfo.status.code" valueName="wfoperinfo.status.code" source = '${theForm.sysCodes["WF"]["SYZT"]}'/>
	        </td>
	    </tr>
		<tr>
			<td class="attribute">ҵ������</td>
			<td>
			   <input  type="text" id="wfoperinfo.operName" name="wfoperinfo.operName" value="<c:out value='${theForm.wfoperinfo.operName}'/>" /></td>
			<td class="attribute">��������</td>
			<td>
			   <input type="text" id="wfoperinfo.stepNum" name="wfoperinfo.stepNum" value="<c:out value='${theForm.wfoperinfo.stepNum}'/>"/>
			   <span class="font_request">*</span>
	        </td>
	    </tr>
		<tr>
			<td class="attribute">�־û�����</td>
			<td>
			  <input type="text" id="wfoperinfo.className" name="wfoperinfo.className" value="<c:out value='${theForm.wfoperinfo.className}'/>"/>
			  <span class="font_request">*</span>
		    </td>
		    <td class="attribute">������ͼ��</td>
		    <td>
		      <input type="button" name="SubmitGraphics" value="BangBang" onClick="goGraphics()" />
		    </td>
		</tr>
		<tr>  
		    <td class="attribute">���̹���Ա</td>
		    <td>
		      <c:set value="${theForm.wfoperinfo.flowManager}" var="flowManager" /> 
		      <select id="flowManager" name ="wfoperinfo.flowManager" size="3" multiple="multiple" type="select-multiple" style="width:100px; font-size:13px;">
			     <c:forEach var="manager" items="${manager}">
					<option value="<c:out value='${manager.empCd}'/>">
					 <c:out value='${manager.empName}' />
			     </c:forEach>
		      </select>
		     <input type="button" name="Submit10" value="���" onClick="createFlowManager()" />
		     <input type="button" name="Submit20" value="ɾ��" onClick="deletes(flowManager)" />
		    
		    </td>		    
		</tr> 
	  </table>
<%-- -------------------------------------------------------------------------------------------------  --%>

<div class="update_subhead">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td>
			<span class="switch_open" onClick="StyleControl.switchDiv(this, $('divId_scrollLing'))" title="����������">������Ϣ�б�</span>
		</td>
		
	</tr>
</table>
</div>
<div id="divId_scrollLing" class="list_scroll">
		<table border="0" cellpadding="0" cellspacing="0" class="Listing" id="listtable" ondblclick="CurrentPage.updateParticular(event)" onClick="TableSort.sortColumn(event)">
		<input type="hidden" name="checkall" onClick="CurrentPage.selectedAll()" title="�Ƿ�ȫѡ"/>
	<thead>
	
				<tr id="tableHead">
					<td nowrap="nowrap">
					   <input id='detailIdsForPrintAll' type='checkbox' onClick="FormUtils.checkAll(this,document.getElementsByName('detailIds'))" />
					</td>
					<td nowrap="nowrap">����</td>	
					<td nowrap="nowrap">��������</td>
					<td nowrap="nowrap">��������</td>
					<td nowrap="nowrap">�˻�</td>
					<td nowrap="nowrap">�˻ز���</td>
					<td nowrap="nowrap">ȡ��</td>
					<td nowrap="nowrap">ȡ��</td>
					<td nowrap="nowrap">����</td>
					<td nowrap="nowrap">����</td>
					<td nowrap="nowrap">�ύ</td>
					<td nowrap="nowrap">�ύ����</td>
					<td nowrap="nowrap">��ǩ</td>
					<td nowrap="nowrap">����</td>
					<td nowrap="nowrap">��Ϣ</td>
					<td nowrap="nowrap">Ȩ��Ҫ��</td>
					<td nowrap="nowrap">����
					 <input type="button" class="list_create" onClick="CurrentPage.addpart();" id="" style="display:" name="addBtn" title="����" />
					</td>
						
				</tr>
			</thead>
			<%-- 
			<input type="hidden" name="ooid" value="<c:out value='${ooid}'/>" />
			--%>
			<tbody id='tablist'>
				<c:forEach var="item" items="${theForm.particulars}" varStatus="status">
					<tr>
						<td align="center">
							<input type="checkbox"   name="detailIds" onclick="FormUtils.check($('detailIdsForPrintAll'),document.getElementsByName('detailIds'))" value="<c:out value="${item.id}"/>" />
						</td>
						<td nowrap="nowrap" align='left' id="<c:out value="${item.id}"/>.stepId" >
							<c:out  value="${item.stepId}"/>&nbsp;
						</td>
						<td nowrap="nowrap" align='left' id="<c:out value="${item.id}"/>.stepName">
							<c:out  value="${item.stepName}"/>&nbsp;
						</td>
						<td nowrap="nowrap" align='left' id="<c:out value="${item.id}"/>.dealInfo">
							<c:out  value="${item.dealInfo}"/>&nbsp;
						</td> 
						<td nowrap="nowrap" align='left' id="<c:out value="${item.id}"/>.goback">
							<c:out  value="${item.goback}"/>&nbsp;
						</td>
						<td nowrap="nowrap" align='left' id="<c:out value="${item.id}"/>.gobackStep">
							<c:out  value="${item.gobackStep}"/>&nbsp;
						</td>
						<td nowrap="nowrap" align='left' id="<c:out value="${item.id}"/>.cancel">
							<c:out  value="${item.cancel}"/>&nbsp;
						</td>
						<td nowrap="nowrap" align='left' id="<c:out value="${item.id}"/>.fetch">
							<c:out  value="${item.fetch}"/>&nbsp;
						</td>
						<td nowrap="nowrap" align='left' id="<c:out value="${item.id}"/>.suspend">
							<c:out  value="${item.suspend}"/>&nbsp;
						</td>
						<td nowrap="nowrap" align='left' id="<c:out value="${item.id}"/>.passround">
							<c:out  value="${item.passround}"/>&nbsp;
						</td>
						<td nowrap="nowrap" align='left' id="<c:out value="${item.id}"/>.commit">
							<c:out  value="${item.commit}"/>&nbsp;
						</td>
						<td nowrap="nowrap" align='left' id="<c:out value="${item.id}"/>.commitStep">
							<c:out  value="${item.commitStep}"/>&nbsp;
						</td>
						<td nowrap="nowrap" align='left' id="<c:out value="${item.id}"/>.togethersign">
							<c:out  value="${item.togethersign}"/>&nbsp;
						</td>
						<td nowrap="nowrap" align='left' id="<c:out value="${item.id}"/>.helpman">
							<c:out  value="${item.helpman}"/>&nbsp;
						</td>
						<td nowrap="nowrap" align='left' id="<c:out value="${item.id}"/>.message">
							<c:out  value="${item.message}"/>&nbsp;
						</td>
						<td nowrap="nowrap" align='left' id="<c:out value="${item.id}"/>.tblUserId">
							<c:out  value="${item.tblUserId}"/>&nbsp;
						</td>
						<td nowrap="nowrap" align='center'>
						   
							<input type="button" class="list_update" onClick="CurrentPage.updateParticular(event)" id="<c:out value="${item.id}"/>" style="display:" name="addBtn" title="�޸�"/>
							<input type="button" class="list_delete" onClick="CurrentPage.removeParticular(this)" id="<c:out value="${item.id}"/>" style="display:" name="delBtn" title="ɾ��"/>
						</td>
					</tr>
				</c:forEach>					
			</tbody>
		</table>
	 
		
<div class="list_bottom">
  <c:set var="paginater.forwardUrl" value="/wf/operationlist.do?step=info" scope="page" /> 
  
	 <%@ include file="/decorators/paginater.jspf"%>
		   <input type="button" class="opera_export" title="����Excel�ļ�" onClick="Print.exportExcel($('divId_scrollLing'))" value=""/>
		   <input name="" type="button" class="opera_batchdelete"  value="����ɾ��" onClick="CurrentPage.deletePermissionsAll(); return false"/>		   
	       <input name="" type="button" class="opera_display" value="����XML" onClick="CurrentPage.createXml(); return false"/>
</div>	  
	</div>
</div>
</form>

</body>
</html>

<script type="text/javascript" src = "<c:out value = "${initParam['publicResourceServer']}/js/checkonchangevalues.js" />"></script>
		