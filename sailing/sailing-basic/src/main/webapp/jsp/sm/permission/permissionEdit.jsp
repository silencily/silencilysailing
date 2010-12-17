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
	    <span class=" switch_open" onClick="StyleControl.switchDiv(this, $('submenu'))" title="点击收缩节点">目录详细信息</span>
		</div>
		<table border="0" cellpadding="0" cellspacing="0" class="Detail" id="submenu" style="display: ">
			<tr>
				<td  class="attribute" >显示名称</td>
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
						bisname="显示名称"
						required="true"
						comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
				</td>
				<td  class="attribute" >顺序号</td>
				<td >
				<vision:text
						rwCtrlType="2"
						permissionCode=""
						dataType="0"
						id="bean.displayOrder"
						name="bean.displayOrder"
						value="${theForm.bean.displayOrder}"
						maxlength="4"
						bisname="顺序号"
						comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
				</td>
			</tr>
			<tr>
				<td  class="attribute" >父节点名称</td>
				<td colspan="3">
				<input name="fatherDisplayName"  id="fatherDisplayName" value="<c:out value='${theForm.bean.father.displayname}'/>" class="readonly" readonly="readonly"/>
				<span class="font_request">*</span> <input type="button" id="select_fromtree" onClick="CurrentPage.selectMenu()" />
				</td>
			</tr>
			<tr>
				<td  class="attribute" >说明</td>
				<td colspan="3">
					<vision:text 
					rwCtrlType="2" 
					permissionCode="" 
					dataType="6"
					name="bean.note" 
					value="${theForm.bean.note}"
					maxlength="200"
					bisname="说明"
					longTextType="true"
					comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagDefautPolicy" />
		  		</td>
			</tr>
		</table>
	</c:when>
	<c:when test="${theForm.menuDataFlg =='1'}">
	<div class="update_subhead">
    <span class=" switch_open" onClick="StyleControl.switchDiv(this, $('submenu'))" title="点击收缩节点">功能权限详细信息</span>
	</div>
	<table border="0" cellpadding="0" cellspacing="0" class="Detail" id="submenu" style="display: ">
		<tr>
			<td  class="attribute" >显示名称</td>
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
					bisname="显示名称"
					required="true"
					comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
			</td>
			<td  class="attribute" >顺序号</td>
			<td>
				<vision:text
				rwCtrlType="2"
				permissionCode=""
				dataType="0"
				id="bean.displayOrder"
				name="bean.displayOrder"
				value="${theForm.bean.displayOrder}"
				maxlength="4"
				bisname="顺序号" 
				comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy"/>
			</td>
		</tr>
		<tr>
			<td  class="attribute" >父节点名称</td>
			<td>
				<input name="fatherDisplayName"  id="fatherDisplayName" value="<c:out value='${theForm.bean.father.displayname}'/>" class="readonly" readonly="readonly"/>
				<span class="font_request">*</span> <input type="button" id="select_fromtree" onClick="CurrentPage.selectMenu()" />
			</td>
			<td  class="attribute" >功能权限类型</td>
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
			<td  class="attribute" >超链接</td>
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
			<td  class="attribute" >说明</td>
			<td colspan="3">
				<vision:text 
				rwCtrlType="2" 
				permissionCode="" 
				dataType="6"
				name="bean.note" 
				value="${theForm.bean.note}"
				maxlength="200"
				bisname="说明"
				longTextType="true"
				comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagDefautPolicy" />
	  		</td>
		</tr>
	</table>
</c:when>
<c:otherwise>
	<div class="update_subhead">
    <span class=" switch_open" onClick="StyleControl.switchDiv(this, $('submenu'))" title="点击收缩节点">数据项权限详细信息</span>
	</div>
	<table border="0" cellpadding="0" cellspacing="0" class="Detail" id="submenu" style="display: ">
		<tr>
			<td  class="attribute" >显示名称</td>
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
					bisname="显示名称"
					comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
			</td>
			<td  class="attribute" >父节点名称</td>
			<td>
				<input name="fatherDisplayName"  id="fatherDisplayName" value="<c:out value='${theForm.bean.father.displayname}'/>" class="readonly" readonly="readonly"/>
			</td>
		</tr>
		<tr>
			<td  class="attribute" >顺序号</td>
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
			<td  class="attribute" >数据项权限标识</td>
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
			<td  class="attribute" >说明</td>
			<td colspan="3">
				<vision:text 
				rwCtrlType="2" 
				permissionCode="" 
				dataType="6"
				name="bean.note" 
				value="${theForm.bean.note}"
				maxlength="200"
				bisname="说明"
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
		document.getElementById('bean.displayOrder').msg = "顺序号只能是整数";		
		document.getElementById('bean.displayname').dataType = 'Require';
		document.getElementById('bean.displayname').msg = "显示名称不能为空";
		document.getElementById('fatherDisplayName').dataType = 'Require';
		document.getElementById('fatherDisplayName').msg = "父节点不能为空";
		}
	</c:when>
	<c:when test="${theForm.menuDataFlg =='1'}">
			CurrentPage.initValideInput = function () {
			document.getElementById('bean.displayOrder').dataType = 'Number';
			document.getElementById('bean.displayOrder').msg = "顺序号只能是整数";	
			document.getElementById('fatherDisplayName').dataType = 'Require';
			document.getElementById('fatherDisplayName').msg = "父节点不能为空";
			document.getElementById('bean.displayName').dataType = 'Require';
			document.getElementById('bean.displayName').msg = "显示名称不能为空";
			document.getElementById('bean.urltype').dataType ='Require';
			document.getElementById('bean.urltype').msg = "功能权限类型不能为空";
			document.getElementById('bean.url').dataType ='Require';
			document.getElementById('bean.url').msg = "超链接不能为空";
		}
	</c:when>
	<c:otherwise>
		CurrentPage.initValideInput = function () {
			document.getElementById('bean.displayOrder').dataType = 'Number';
			document.getElementById('bean.displayOrder').msg = "顺序号只能是整数";	
			document.getElementById('fatherDisplayName').dataType = 'Require';
			document.getElementById('fatherDisplayName').msg = "父节点不能为空";
			document.getElementById('bean.displayName').dataType = 'Require';
			document.getElementById('bean.displayName').msg = "显示名称不能为空";
			document.getElementById('bean.permissionCd').dataType ='Require';
			document.getElementById('bean.permissionCd').msg = "数据项权限标识不能为空";
		}
	</c:otherwise>
</c:choose>
	CurrentPage.initValideInput();
	
	//选择目录组织sub窗口
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
