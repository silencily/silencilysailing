<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf"/>
<html>
<body class="main_body">
<form name="f" action="" method="post">
	<input type="hidden" name="oid" value="<c:out value='${theForm.bean.id}'/>" />
	<input type="hidden" name="step" value="<c:out value='${theForm.step}'/>" />

<div class="update_subhead">
	<span class="switch_open" onClick="StyleControl.switchDiv(this,submenu1)" title="����������">��Ŀ��ϸ</span>
</div>
	<table border="0" cellpadding="0" cellspacing="0" class="Detail" id="submenu1">
		<tr>
			<td class="attribute">��Ŀ����</td>
			<td>
				<vision:choose
					value="${theForm.bean.columnFlg.code}" 
					textName="bean.columnFlg.name"
					valueName="bean.columnFlg.code"
					source='${theForm.sysCodes["UF"]["LMQF"]}'
					comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagDefautPolicy"
					readonly='true'
				/>
			</td>
			<td class="attribute">��Ŀ����</td>	
			<td>
				<vision:text
					name="bean.columnNm"
					value="${theForm.bean.columnNm}"
					comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagDefautPolicy"
					maxlength="20"
					id="bean.columnNm"
					required='true'
				/>
			</td>
		</tr>
		<tr>		
			<td class="attribute">��Ҫ����</td>
			<td>
				<input type="checkbox" name="chk.feedbackFlg" value="1" <c:if test="${theForm.bean.feedbackFlg=='1'}">checked</c:if>
				       onclick="doCheck(this,document.getElementById('bean.feedbackFlg.code'))">
				<input type="hidden" name="bean.feedbackFlg.code" id="bean.feedbackFlg.code" value="<c:out value='${theForm.bean.feedbackFlg}'/>" >
			</td>
			<td >&nbsp;</td>
			<td>
				&nbsp;
			</td>
		</tr>
	</table>

<!-- ����ִ���� Start -->
<div class="update_subhead">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td>
			<span class="switch_open" onClick="StyleControl.switchDiv(this, listtable2)">����ִ����</span>
		</td>
		<td align="right">
	    </td>
	</tr>
</table>
</div>
<div id="divId_target" class="list_scroll">
	<table border="0" cellpadding="0" cellspacing="0" class="Listing" id="listtable2">
		<thead id="tabtitle">
			<tr>
				<td nowrap="nowrap" width="60">
					<input type="button" class="list_create" onClick="experBeanController.addListingRow(listtable2);" id="addPubObj" style="display:" title="���"/>
				</td>
				<td nowrap="nowrap">Ա����</td>
				<td nowrap="nowrap">����</td>
				<td nowrap="nowrap">���ű��</td>
				<td nowrap="nowrap">����</td>
			</tr>
		</thead>
		<tbody id=tablist>
		<c:forEach var="item" items="${theForm.pubPermission}" varStatus="status">
			<tr id="experBeanController_trId_[<c:out value="${status.index}"/>]">
			<td align="center">
			<div>
				<input type="button" class="list_create" onClick="experBeanController.addListingRow(listtable2);" id="experBeanController_[<c:out value = "${status.index}" />].add" style="display:" title="���"/> 									
				<%-- <input type="button" class="list_delete" onClick="CurrentPage.remove('<c:out value="${item.id}"/>')"/>	--%>
				<input type="button" class="list_delete" onClick="experBeanController.delRowCur(listtable2,this);return false" id="experBeanController_[<c:out value='${status.index}'/>].del" style="display:" row="<c:out value='${status.index}'/>" name="delBtn" title="ɾ��"/>
				<input type="button" class="list_renew" onClick="experBeanController.renRowCur(listtable2,this);return false" id="experBeanController_[<c:out value='${status.index}'/>].old" style="display:none" row="<c:out value='${status.index}'/>" name="oldBtn" holdObj=true title="�ָ�"/>
			
			</div>
			</td>
				<td align="left">
					<input type="hidden" name="pubPermission[<c:out value="${status.index}"/>].id" value="<c:out value="${item.id}"/>" id="experBeanController_[<c:out value='${status.index}'/>].id"/>
					<input type="hidden" name="pubPermission[<c:out value='${status.index}'/>].delFlg" id="pubPermission[<c:out value='${status.index}'/>].delFlg" value="<c:out value='${item.delFlg}' />"/>
					<input type="hidden" name="pubPermission[<c:out value='${status.index}'/>].version" value="<c:out value='${item.version}' />"/>
				    <input name="experEmpInfo[<c:out value="${status.index}"/>].id" type="hidden" value="<c:out value="${item.tblHrEmpInfo.id}"/>" class="readonly" readonly="readonly" />
					<input name="experEmpInfo[<c:out value="${status.index}"/>].empCd" type="text" value="<c:out value="${item.tblHrEmpInfo.empCd}"/>" class="readonly" readonly="readonly" onchange="doTxtChange(<c:out value="${status.index}"/>)"/>&nbsp;
					<input type="button" id="edit_query" onClick="selectEmp('experEmpInfo['+this.row+']', definedWin, 'checkbox')" row="<c:out value='${status.index}'/>"/></td>
				<td align="left"><input name="experEmpInfo[<c:out value="${status.index}"/>].empName" type="text" value="<c:out value="${item.tblHrEmpInfo.empName}"/>" class="readonly" readonly="readonly" />&nbsp;</td>
				<td align="left"><input id="experEmpInfo[<c:out value="${status.index}"/>].deptCd" name="experBean[<c:out value="${status.index}"/>].deptCd" 
						type="text" value="<c:out value="${item.tblHrEmpInfo.tblCmnDept.deptCd}"/>" class="readonly" readonly="readonly" />&nbsp; </td>
				<td align="left"><input id="experEmpInfo[<c:out value="${status.index}"/>].deptName" name="experBean[<c:out value="${status.index}"/>].deptName" 
						type="text" value="<c:out value="${item.tblHrEmpInfo.tblCmnDept.deptName}"/>" class="readonly" readonly="readonly" />&nbsp;</td>
			</tr>
		</c:forEach>						
			</tbody>
    </table>
</div>
<!-- ����ִ���� End -->

<script type="text/javascript" language="javascript">
	// ע��, ��̬�����б������ CurrentPage.initValidateInfo �������Ա�֤��֤��Ϣ������
	var experBeanController = new EditPage("experBeanController");
	/**
	  * �ڵ�����Աѡ������ж�ѡʱ����Ҫ�ڴ����ظú��������Ա�ҳ������Ӧ����
	  */
	definedWin.setListObjectInFor = function(arr,attribute,arrWithAtt) {
		var txtName, i1, i2, index, i;
	
		txtName = definedWin.txtName;
		i1 = txtName.indexOf("["); 
		i2 = txtName.indexOf("]");
		index = txtName.substring(i1+1, i2);
		i = parseInt(index);

		for (i = parseInt(index); i < newcount; i++) {
			txtName = txtName.substring(0, i1) + "[" + i + "]";
			var td = document.getElementById(txtName + "." + "id");
			if (td) {
				break;
			}
		}
		if (i == newcount && parseInt(index) < newcount)
			txtName = txtName.substring(0, i1) + "[" + i + "]";
		var td = document.getElementById(txtName + "." + "id");
		if (td == 'undefined' || td == null) experBeanController.addListingRow(listtable2);
	
		for(var t in arr) {	
			tt = t.replace(/_/gi, "."); //���б���fieldʱ��ʹ��"_"����"."
			temp = document.getElementById(txtName + "." + tt);
			if (temp) {
				if(temp.tagName == "INPUT") {
					temp.value = arr[t];
					continue;
				} else {
					temp.innerText = arr[t];
					continue;
				}					
			}else{
				//�������fieldֵ����������������
				var objs = document.getElementsByTagName("INPUT");
				for( n=0;n<objs.length;n++) {
					if(objs[n].field == (txtName + "." + tt)){
						objs[n].value = arr[t];
					}
				}
			}
		}
		
		// ������һ���ؼ���
		txtName = txtName.substring(0, i1);
		definedWin.txtName = txtName + "[" + (i+1)+ "]";	
	}
	//CheckBox����ʱ���еĲ���
	doCheck = function(chk,hdn){
	    if (chk.checked ){
	        hdn.value = "1";
	    }else{
	        hdn.value = "0";
	    }
//        alert(hdn.value);
	}
    //--ȡ��select��ֵ--
	//----------------------------------------------------------------------------------------------------------
	var msgInfo_ = new msgInfo();

	if (CurrentPage == null) {
		var CurrentPage = {};
	}
	CurrentPage.reset = function () {
		document.forms[0].reset();
	}
	CurrentPage.submit = function () {
		if (!CurrentPage.validation()) {
			return;
		}
		FormUtils.post(document.forms[0], '<c:url value='/uf/desk/ColumnManageAction.do?step=save'/>');
	}
	CurrentPage.initValideInput = function () {
		document.getElementById('bean.columnNm').dataType = 'Require';
		document.getElementById('bean.columnNm').msg = msgInfo_.getMsgById('UF_I003_R_1',['��Ŀ����']);		
	}	
	CurrentPage.initValideInput();
	CurrentPage.create = function() {
		$('bean.columnFlg.code').value = '';
		$('bean.columnNm').value='';
		$('oid').value = '';
		$('step').value = 'info';
		FormUtils.post(document.forms[1], '<c:url value='/uf/desk/ColumnManageAction.do?step=info'/>');
	}

	/**
	 * ��֤Ա����Ϣ�Ƿ��ظ�
	 */
	CurrentPage.validateEmpItems = function() {
		var empMap = new Object();
		
	//	var empItemsCount = <c:out value = "${pageScope['experBeanCount']}" default = "0" />;
//		var tableRowNum=document.all.listtable2.rows.length;
		var tableRowNum=newcount;
		for (var i = 0; i < tableRowNum; i++) {
			if($('experEmpInfo[' + i + '].empCd')){
				var empCd = $('experEmpInfo[' + i + '].empCd').value;
				if(empCd==""){
					continue;
				}
				var empName = $('experEmpInfo[' + i + '].empName').value;
				var entryValue = empMap[empCd + ' - ' + empName];
				if (entryValue == null) {
					entryValue = 0;
				}
				entryValue++;
				empMap[empCd + ' - ' + empName] = entryValue;
			}
		}
		
		var repeatedKeys = new Array();
		for (entryKey in empMap) {
			var entryValue = empMap[entryKey];
			if (entryValue > 1) {
				repeatedKeys[repeatedKeys.length] = entryKey;
			}
		}
		
		if (repeatedKeys.length > 0) {
			Validator.clearValidateInfo();
			Validator.warnMessage(msgInfo_.getMsgById('UF_I017_C_1',['Ա����']));
			for (var i = 0; i < repeatedKeys.length; i++) {
				Validator.warnMessage('   ' + repeatedKeys[i]);
			}
			return false;
		}
		
		return true;
	}

	CurrentPage.validation = function () {
		if(!Validator.Validate(document.forms[0], 4)){
			return false;
		}
		return CurrentPage.validateEmpItems();
	}

	var id = $('oid').value;

	//�Է���ִ���˱���еĲ��������ӣ�ɾ���Լ��Զ�һ�еĻָ���
	var newcount = document.getElementById("listtable2").getElementsByTagName("tr").length -1;
	//�ڷ���ִ���˱�������һ��
	experBeanController.addListingRow = function(tbl){ 
	    var testTbl =  document.getElementById("listtable2");
	    var newTr = testTbl.insertRow();	
	    newTr.id="experBeanController_trId_[" + newcount + "]";
		var cellhtml;
		//���2�� 
		var newTd0 = newTr.insertCell(); 
		newTd0.align = 'center';
		var newTd1 = newTr.insertCell();
		var newTd2 = newTr.insertCell();
		var newTd3 = newTr.insertCell();
		var newTd4 = newTr.insertCell();
		//���������ݺ����� 
		cellhtml = 	'<div>'	+
		"<input type=button class='list_create' onclick='experBeanController.addListingRow(listtable2);' id=experBeanController_[" + newcount + "].add title='���' /> " +									
		'<input type="button" class="list_delete" onClick="experBeanController.delRowCur(listtable2,this);return false" id="experBeanController_[' + newcount + '].del"  row="' + newcount + '" name="delBtn" title="ɾ��"/>' +
		'<input type="button" class="list_renew" onClick="experBeanController.renRowCur(listtable2,this);return false" id="experBeanController_[' + newcount + '].old" style="display:none" row="'+ newcount + '" name="oldBtn" holdObj=true title="�ָ�"/></div>';
		newTd0.innerHTML = cellhtml;
		cellhtml = 
		'<input type="hidden" name="pubPermission[' + newcount + '].id" value="" id="experBeanController_[' + newcount + '].id"/>' +
		'<input type="hidden" name="pubPermission[' + newcount + '].delFlg" value="0" id="pubPermission[' + newcount + '].delFlg"/>' +
		'<input name="experEmpInfo[' + newcount + '].id" type="hidden" />' +
 		'<input name="experEmpInfo[' + newcount + '].empCd" type="text"  class="readonly" readonly="readonly" onchange="doTxtChange(' + newcount + ')"/>&nbsp; ' + 
        '<input type="button" id="edit_query" onClick="selectEmp(\'experEmpInfo[\'+this.row+\']\', definedWin, \'checkbox\')" row="' + newcount + '"/>';
		newTd1.innerHTML = cellhtml;
		cellhtml = '<input name="experEmpInfo[' + newcount + '].empName" type="text" class="readonly" readonly="readonly" />&nbsp;';
		newTd2.innerHTML = cellhtml;
		cellhtml = '<input id="experEmpInfo[' + newcount + '].deptCd" name="experBean[' + newcount + '].deptCd" ' +
				   'type="text" class="readonly" readonly="readonly" />&nbsp; ';
		newTd3.innerHTML = cellhtml;		   
		cellhtml = '<input id="experEmpInfo[' + newcount + '].deptName" name="experBean[' + newcount + '].deptName"' +
						'type="text" class="readonly" readonly="readonly" />&nbsp; ';
		newTd4.innerHTML = cellhtml;				
		newcount++; 
		Global.setHeight();
	}	
	//�ڷ���ִ���˱���ɾ��һ��
	experBeanController.delRowCur = function (table,sel){
		var inum = sel.row;
		
		var del = document.getElementById("pubPermission["+inum+"].delFlg").value;
		
		var str = document.getElementById(experBeanController.name+"_["+inum+"].id").value;
		
		var obj = document.getElementById(experBeanController.name+"_trId_["+inum+"]");
		experBeanController.afterDeleteRow(table,sel)
		if(str ==""){
//			if(inum == 0){
//				experBeanController.disableButton(obj);
//				experBeanController.switchButton(inum,"none","none","")
//			}else{
				table.deleteRow(obj.rowIndex);
				Global.setHeight();
//			}
		}else{
			//experBeanController.disableButton(obj);
			obj.className = "disabled";
			document.getElementById("pubPermission["+inum+"].delFlg").value="1";
			experBeanController.switchButton(inum,"none","none","")
		}
	}
	//�ڷ���ִ���˱��һ�а�ɾ����ť����еĻָ�����
	experBeanController.renRowCur = function (table,sel){
		var inum = sel.row;
		document.getElementById("pubPermission["+inum+"].delFlg").value="0";
		experBeanController.renRow(table,sel);
	}
	//������ִ���˱��б��ʱ�Զ���һ��
	//if (newcount == 0) {
	//    experBeanController.addListingRow(listtable2);
	//}
</script>
</form>
<form name="empty" action="" method="post">
</form>
</body>
</html>	
