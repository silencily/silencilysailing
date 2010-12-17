<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf" />
<html>
<body>
<form name="f">
<input type="hidden" name="oid"  id="oid" value="<c:out value='${theForm.oid}'/>"/>
<input type="hidden" name="parentCode"  id="parentCode" value="<c:out value='${theForm.parentCode}'/>"/>
<input type="hidden" name="oldParentCode"  id="oldParentCode"  value="<c:out value='${theForm.oldParentCode}'/>"/>
<input type="hidden" name="menuDataFlg"  id="menuDataFlg" value="<c:out value='${theForm.menuDataFlg}'/>"/>
<input type="hidden" name="strCreate"  id="hcreat" value="<c:out value='${theForm.strCreate}'/>"/>

<c:choose>
	<c:when test="${theForm.menuDataFlg =='0'}">
		<div class="update_subhead">
	    <span class=" switch_open" onClick="StyleControl.switchDiv(this, $('submenu'))" title="��������ڵ�">Ŀ¼��ϸ��Ϣ</span>
		</div>
		<table border="0" cellpadding="0" cellspacing="0" class="Detail" id="submenu" style="display: ">
			<tr>
				<td  class="attribute" >��ʾ����</td>
				<td >
				<input type="hidden" name="bean.nodetype"  id="oid" value="0"/>
				<vision:text
						rwCtrlType="2"
						permissionCode=""
						dataType="1"
						id="bean.displayname"
						name="bean.displayname"
						value="${theForm.bean.displayname}"
						maxlength="50"
						bisname="��ʾ����"
						required="true"
						comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
				</td>
				<td  class="attribute" >˳���</td>
				<td >
				<vision:text
						rwCtrlType="2"
						permissionCode=""
						dataType="0"
						id="bean.displayOrder"
						name="bean.displayOrder"
						value="${theForm.bean.displayOrder}"
						maxlength="4"
						bisname="˳���"
						comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
				</td>
			</tr>
			<tr>
				<td  class="attribute" >���ڵ�����</td>
				<td colspan="3">
				<input name="fatherDisplayName"  id="fatherDisplayName" value="<c:out value='${theForm.bean.father.displayname}'/>" class="readonly" readonly="readonly"/>
				<span class="font_request">*</span> <input type="button" id="select_fromtree" onClick="CurrentPage.selectMenu()" />
				</td>
			</tr>
			<tr>
				<td  class="attribute" >˵��</td>
				<td colspan="3">
					<vision:text 
					rwCtrlType="2" 
					permissionCode="" 
					dataType="6"
					name="bean.note" 
					value="${theForm.bean.note}"
					maxlength="200"
					bisname="˵��"
					longTextType="true"
					comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagDefautPolicy" />
		  		</td>
			</tr>
		</table>
	</c:when>
	<c:when test="${theForm.menuDataFlg =='1'}">
	<div class="update_subhead">
    <span class=" switch_open" onClick="StyleControl.switchDiv(this, $('submenu'))" title="��������ڵ�">����Ȩ����ϸ��Ϣ</span>
	</div>
	<table border="0" cellpadding="0" cellspacing="0" class="Detail" id="submenu" style="display: ">
		<tr>
			<td  class="attribute" >��ʾ����</td>
			<td >
			<input type="hidden" name="bean.nodetype"  id="oid" value="1"/>
				<vision:text
					rwCtrlType="2"
					permissionCode=""
					dataType="1"
					id="bean.displayname"
					name="bean.displayname"
					value="${theForm.bean.displayname}"
					maxlength="50"
					bisname="��ʾ����"
					required="true"
					comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
			</td>
			<td  class="attribute" >˳���</td>
			<td>
				<vision:text
				rwCtrlType="2"
				permissionCode=""
				dataType="0"
				id="bean.displayOrder"
				name="bean.displayOrder"
				value="${theForm.bean.displayOrder}"
				maxlength="4"
				bisname="˳���" 
				comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy"/>
			</td>
		</tr>
		<tr>
			<td  class="attribute" >���ڵ�����</td>
			<td>
				<input name="fatherDisplayName"  id="fatherDisplayName" value="<c:out value='${theForm.bean.father.displayname}'/>" class="readonly" readonly="readonly"/>
				<span class="font_request">*</span> <input type="button" id="select_fromtree" onClick="CurrentPage.selectMenu()" />
			</td>
			<td  class="attribute" >����Ȩ������</td>
			<td>
				<vision:choose
				rwCtrlType="2"
				permissionCode=""
				value="${theForm.bean.urltype}" 
				textName="temp.bean.urltype"
				valueName="bean.urltype" 
				source='${theForm.urltypeComboList}'
				required="true"
				comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagDefautPolicy"/>
			</td>
		</tr>
		<tr>
			<td  class="attribute" >������</td>
			<td colspan="3">
				<vision:text 
				rwCtrlType="2" 
				permissionCode="" 
				dataType="6"
				id="bean.url"
				name="bean.url" 
				value="${theForm.bean.url}"
				maxlength="200"
				bisname="url"
				longTextType="true"
				required="true"
				comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagDefautPolicy" />
	  		</td>
		</tr>
		<tr>
			<td  class="attribute" >˵��</td>
			<td colspan="3">
				<vision:text 
				rwCtrlType="2" 
				permissionCode="" 
				dataType="6"
				name="bean.note" 
				value="${theForm.bean.note}"
				maxlength="200"
				bisname="˵��"
				longTextType="true"
				comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagDefautPolicy" />
	  		</td>
		</tr>
	</table>
</c:when>
<c:otherwise>
	<div class="update_subhead">
    <span class=" switch_open" onClick="StyleControl.switchDiv(this, $('submenu'))" title="��������ڵ�">������Ȩ����ϸ��Ϣ</span>
	</div>
	<table border="0" cellpadding="0" cellspacing="0" class="Detail" id="submenu" style="display: ">
		<tr>
			<td  class="attribute" >��ʾ����</td>
			<td >
			<input type="hidden" name="bean.nodetype"  id="oid" value="2"/>
				<vision:text
					rwCtrlType="2"
					permissionCode=""
					dataType="1"
					id="bean.displayname"
					name="bean.displayname"
					value="${theForm.bean.displayname}"
					maxlength="50"
					bisname="��ʾ����"
					comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
			</td>
			<td  class="attribute" >���ڵ�����</td>
			<td>
				<input name="fatherDisplayName"  id="fatherDisplayName" value="<c:out value='${theForm.bean.father.displayname}'/>" class="readonly" readonly="readonly"/>
			</td>
		</tr>
		<tr>
			<td  class="attribute" >˳���</td>
			<td>
				<vision:text
				rwCtrlType="2"
				permissionCode=""
				dataType="0"
				id="bean.displayOrder"
				name="bean.displayOrder"
				value="${theForm.bean.displayOrder}"
				maxlength="100"
				comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
			</td>
			<td  class="attribute" >������Ȩ�ޱ�ʶ</td>
			<td>
				<vision:text
				rwCtrlType="2"
				permissionCode=""
				dataType="1"
				id="bean.permissionCd"
				name="bean.permissionCd"
				value="${theForm.bean.permissionCd}"
				maxlength="50"
		        comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
			</td>			
		</tr>
		<tr>
			<td  class="attribute" >˵��</td>
			<td colspan="3">
				<vision:text 
				rwCtrlType="2" 
				permissionCode="" 
				dataType="6"
				name="bean.note" 
				value="${theForm.bean.note}"
				maxlength="200"
				bisname="˵��"
				longTextType="true"
				comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagDefautPolicy" />
	  		</td>
		</tr>
	</table>
</c:otherwise>
</c:choose>
</form>
<script language="javascript">
var msgInfo_ = new msgInfo();
	if (CurrentPage == null) {
    	var CurrentPage = {};
		} 
	CurrentPage.submit = function() {
		if (!CurrentPage.validation()) {
				return false;
			}
		var strCreate = document.getElementById("strCreate").value;
		if (strCreate == "c"){
			$('oid').value ='';
		}
		FormUtils.post(document.forms[0], '<c:url value = "/sm/permissionAction.do?step=permissionSave"/>');
	}
	
	CurrentPage.validation = function () {
		if(!verifyAllform()){return false;}
		return Validator.Validate(document.forms[0], 5);
	}
	
<c:choose>
	<c:when test="${theForm.menuDataFlg =='0'}">
		CurrentPage.initValideInput = function () {
		document.getElementById('bean.displayOrder').dataType = 'Number';
		document.getElementById('bean.displayOrder').msg = "˳���ֻ��������";		
		document.getElementById('bean.displayname').dataType = 'Require';
		document.getElementById('bean.displayname').msg = "��ʾ���Ʋ���Ϊ��";
		document.getElementById('fatherDisplayName').dataType = 'Require';
		document.getElementById('fatherDisplayName').msg = "���ڵ㲻��Ϊ��";
		}
	</c:when>
	<c:when test="${theForm.menuDataFlg =='1'}">
			CurrentPage.initValideInput = function () {
			document.getElementById('bean.displayOrder').dataType = 'Number';
			document.getElementById('bean.displayOrder').msg = "˳���ֻ��������";	
			document.getElementById('fatherDisplayName').dataType = 'Require';
			document.getElementById('fatherDisplayName').msg = "���ڵ㲻��Ϊ��";
			document.getElementById('bean.displayName').dataType = 'Require';
			document.getElementById('bean.displayName').msg = "��ʾ���Ʋ���Ϊ��";
			document.getElementById('bean.urltype').dataType ='Require';
			document.getElementById('bean.urltype').msg = "����Ȩ�����Ͳ���Ϊ��";
			document.getElementById('bean.url').dataType ='Require';
			document.getElementById('bean.url').msg = "�����Ӳ���Ϊ��";
		}
	</c:when>
	<c:otherwise>
		CurrentPage.initValideInput = function () {
			document.getElementById('bean.displayOrder').dataType = 'Number';
			document.getElementById('bean.displayOrder').msg = "˳���ֻ��������";	
			document.getElementById('fatherDisplayName').dataType = 'Require';
			document.getElementById('fatherDisplayName').msg = "���ڵ㲻��Ϊ��";
			document.getElementById('bean.displayName').dataType = 'Require';
			document.getElementById('bean.displayName').msg = "��ʾ���Ʋ���Ϊ��";
			document.getElementById('bean.permissionCd').dataType ='Require';
			document.getElementById('bean.permissionCd').msg = "������Ȩ�ޱ�ʶ����Ϊ��";
		}
	</c:otherwise>
</c:choose>
	CurrentPage.initValideInput();
	
	//ѡ��Ŀ¼��֯sub����
	CurrentPage.selectMenu =function(){
	document.getElementById("oldParentCode").value = document.getElementById("parentCode").value;
	definedWin.openListingUrl('menuUpdate','<c:url value="/sm/permissionAction.do?step=selectMenu&oid2="/>');
 //	window.open ('<c:url value="/sm/permissionAction.do?step=selectMenu&oid2="/>', "newwindow", "height=600, width=540, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no");	
	}
	definedWin.returnListObject = function(arr){
		document.getElementById("parentCode").value= arr[0];
	  	document.getElementById("fatherDisplayName").value = arr[1];
	}	
	CurrentPage.create = function() {

		var tempFlg = document.getElementById("menuDataFlg").value;
		var tempParentCode = document.getElementById("parentCode").value;
		if (tempFlg ==0){
			tempFlg = 1;
		}
		var parentCode = tempParentCode+";"+tempFlg+";c"; 
	  	document.getElementById("oid").value = "";
	  	FormUtils.post(document.forms[0], '<c:url value = "/sm/permissionAction.do?step=edit&parentCode="/>' +  parentCode);

	}
	
	function Validator.afterSuccessMessage(){
	if(parent.codeTree){
		parent.codeTree.Update(parent.codeTree.SelectId);
	}
	}
	
</script>
</body>
</html>
