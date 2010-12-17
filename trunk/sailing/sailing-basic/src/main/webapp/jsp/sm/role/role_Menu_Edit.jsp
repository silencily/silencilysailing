<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf" />
<html>
<body>

<form name="f">
<input type="hidden" name="oid"  id="oid" value="<c:out value='${theForm.oid}'/>;<c:out value='${theForm.orgRoleFlg}'/>"/>
<input type="hidden" name="oldParentCode"  id="oldParentCode"  value="<c:out value='${theForm.oldParentCode}'/>"/>
<input type="hidden" name="strFlg"  id="hflg" value="<c:out value='${theForm.strFlg}'/>"/>
<input type="hidden" name="orgRoleFlg"  id="orgRoleFlg" value="<c:out value='${theForm.orgRoleFlg}'/>"/>
<input type="hidden" name="strCreate"  id="hcreat" value="<c:out value='${theForm.strCreate}'/>"/>
<input type="hidden" name="parentCode"  id="parentCode" value="<c:out value='${param.parentCode}'/>"/>
<c:choose>
	<c:when test="${theForm.orgRoleFlg =='role'}">
	<div class="update_subhead" >
    <span class="switch_open" onClick="StyleControl.switchDiv(this, $('roleInfo'))" title="">��ɫ��ϸ��Ϣ</span>
</div>
<div id="roleInfo" style="display:">
		<table border="0" cellpadding="0" cellspacing="0" class="Detail" id="parenttable" style="display: ">
			<c:choose>
			<c:when test="${theForm.consignFlg=='baseOrSystem'}">
			<tr>
				<td  class="attribute" >��ɫ��ʶ</td>
				<td >
					<vision:text
					rwCtrlType="1"
					permissionCode=""
					dataType="1"
					id="roleBean.roleCd"
					name="roleBean.roleCd"
					value="${theForm.roleBean.roleCd}"
					maxlength="32"
					readonly="true"
					bisname="��ɫ��ʶ"
					required="true"
					comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
					</td>
				<td  class="attribute" >��ɫ����</td>
				<td >
					<vision:text
					rwCtrlType="1"
					permissionCode=""
					dataType="1"
					id="roleBean.name"
					name="roleBean.name"
					readonly="true"
					value="${theForm.roleBean.name}"
					maxlength="100"
					bisname="��ɫ����"
					required="true"
					comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
				</td>
			</tr>
			<tr>
				<td  class="attribute" >���ڵ�����</td>
				<td>	
					<input name="fatherName"  id="fatherDisplayName" value="<c:out value='${theForm.fatherName}'/>" class="readonly" readonly="readonly"/>
				</td>
				<td  class="attribute" >˳���</td>
				<td >
				<vision:text
						rwCtrlType="1"
						permissionCode=""
						dataType="0"
						id="roleBean.displayOrder"
						name="roleBean.displayOrder"
						readonly="true"
						value="${theForm.roleBean.displayOrder}"
						maxlength="4"
						bisname="˳���"
						comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
				</td>
			</tr>
			<tr>
			<td  class="attribute" >ϵͳȨ��</td>
				<td colspan="3">
					<vision:choose
						rwCtrlType="1"
						permissionCode=""
						value="${theForm.roleBean.systemRole}" 
						readonly="true"
						textName="temp.conditions(systemRole).value"
						valueName="roleBean.systemRole" 
						source='${theForm.systemRoleComboList}' 
						comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagDefautPolicy"/>
				</td>
			</tr>
			<tr>
				<td  class="attribute" >˵��</td>
				<td colspan="3">
					<vision:text 
					rwCtrlType="2" 
					permissionCode="" 
					dataType="6"
					name="roleBean.note" 
					value="${theForm.roleBean.note}"
					maxlength="200"
					bisname="˵��"
					longTextType="true"
					comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagDefautPolicy" />
				</td>
			</tr>
			</c:when>
			<c:otherwise>
						<tr>
				<td  class="attribute" >��ɫ��ʶ</td>
				<td >
					<vision:text
					rwCtrlType="2"
					permissionCode=""
					dataType="1"
					id="roleBean.roleCd"
					name="roleBean.roleCd"
					value="${theForm.roleBean.roleCd}"
					maxlength="32"
					bisname="��ɫ��ʶ"
					required="true" 
					comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy"/>
					</td>
				<td  class="attribute" >��ɫ����</td>
				<td >
					<vision:text
					rwCtrlType="2"
					permissionCode=""
					dataType="1"
					id="roleBean.name"
					name="roleBean.name"
					value="${theForm.roleBean.name}"
					maxlength="100"
					bisname="��ɫ����"
					comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
				</td>
			</tr>
			<tr>
				<td  class="attribute" >���ڵ�����</td>
				<td>	
					<c:choose>
						<c:when test="${theForm.strCreate =='c'}">
							<input name="fatherName"  id="fatherDisplayName" value="<c:out value='${theForm.fatherName}'/>" class="readonly" readonly="readonly"/>
							<input  type="hidden" name="temp.roleBean.father.id" id='fatherid' value="<c:out value='${param.parentCode}'/>" class="readonly"/>
						</c:when>
						<c:otherwise>
							<input name="fatherName" id="fatherDisplayName" value="<c:out value='${theForm.roleBean.father.displayName}'/>" class="readonly" readonly="readonly"/>
							<input  type="hidden" name="temp.roleBean.father.id" id='fatherid' value="<c:out value='${theForm.roleBean.father.id}'/>" class="readonly"/>
							<span class="font_request">*</span> <input type="button"
							id="select_fromtree"
							onClick="CurrentPage.selectMenu()" />
						</c:otherwise>
					</c:choose>
				</td>
				<td  class="attribute" >˳���</td>
				<td >
				<vision:text
						rwCtrlType="2"
						permissionCode=""
						dataType="0"
						id="roleBean.displayOrder"
						name="roleBean.displayOrder"
						value="${theForm.roleBean.displayOrder}"
						maxlength="10"
						comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
				</td>
			</tr>
			<tr>
			<td  class="attribute" >ϵͳȨ��</td>
				<td colspan="3">
		            <ec:composite value='${theForm.roleBean.systemRole}'  valueName = "roleBean.systemRole" textName = "temp.conditions(systemRole).value" source = "${theForm.systemRoleComboList}" /><span class="font_request">*</span>
				</td>
			</tr>
			<tr>
				<td  class="attribute" >˵��</td>
				<td colspan="3">
					<vision:text 
					rwCtrlType="2" 
					permissionCode="" 
					dataType="6"
					name="roleBean.note" 
					value="${theForm.roleBean.note}"
					maxlength="200"
					bisname="˵��"
					longTextType="true"
					comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagDefautPolicy" />
				</td>
			</tr>
			</c:otherwise>
			</c:choose>
		</table>
	</div>
	</c:when>
	<c:otherwise>
		<div class="update_subhead" >
		    <span class="switch_open" onClick="StyleControl.switchDiv(this, $('orgInfo'))" title="">Ŀ¼��ϸ��Ϣ</span>
		</div>
		<div id="orgInfo" style="display:">
		<table border="0" cellpadding="0" cellspacing="0" class="Detail" id="parenttable" style="display: ">
			<tr>
				<td  class="attribute" >��ʾ����</td>
				<td>
					<c:choose>
						<c:when test="${theForm.orgBean.id == 'root'}">
							<vision:text
								rwCtrlType="2"
								permissionCode=""
								dataType="1"
								id="orgBean.displayName"
								name="orgBean.displayName"
								value="${theForm.orgBean.displayName}" 
								maxlength="32"
								comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy"/>
						</c:when>
						<c:otherwise>
							<vision:text
								rwCtrlType="2"
								permissionCode=""
								dataType="1"
								id="orgBean.displayName"
								name="orgBean.displayName"
								value="${theForm.orgBean.displayName}"
								maxlength="32"
								comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
						</c:otherwise>
					</c:choose>
				</td>
				<td  class="attribute" >˳���</td>
				<td>
					<c:choose>
						<c:when test="${theForm.orgBean.id == 'root'}">
							<vision:text
								rwCtrlType="2"
								permissionCode=""
								dataType="0"
								id="orgBean.displayOrder"
								name="orgBean.displayOrder"
								value="${theForm.orgBean.displayOrder}"
								comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
						</c:when>
						<c:otherwise>
							<vision:text
								rwCtrlType="2"
								permissionCode=""
								dataType="0"
								id="orgBean.displayOrder"
								name="orgBean.displayOrder"
								value="${theForm.orgBean.displayOrder}"
								bisname="˳���"
								comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
			<tr>
				<td  class="attribute" >���ڵ�����</td>
				<td colspan="3">
					<c:choose>
						<c:when test="${theForm.orgBean.id == 'root'}">
							<input name="fatherName" id="fatherDisplayName" value="<c:out value='${theForm.fatherName}'/>" class="readonly" readonly="readonly"/>
						</c:when>
						<c:when test="${theForm.strCreate =='c'}">
							<input name="fatherName" id="fatherDisplayName" value="<c:out value='${theForm.fatherName}'/>" class="readonly" readonly="readonly"/>
							<input  type="hidden" name="temp.orgBean.father.id" id='fatherid' value="<c:out value='${param.parentCode}'/>" class="readonly"/>
						</c:when>
						<c:otherwise>
							<input name="fatherName" id="fatherDisplayName" value="<c:out value='${theForm.orgBean.father.displayName}'/>" class="readonly" readonly="readonly"/>
							<input  type="hidden" name="temp.orgBean.father.id" id='fatherid' value="<c:out value='${theForm.orgBean.father.id}'/>" class="readonly"/>
							<span class="font_request">*</span> <input type="button"
							id="select_fromtree"
							onClick="CurrentPage.selectMenu();" />
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
			<tr>
				<td  class="attribute" >˵��</td>
				<td colspan="3">
					<c:choose>
						<c:when test="${theForm.orgBean.id == 'root'}">
						<vision:text 
							rwCtrlType="2" 
							permissionCode="" 
							dataType="6"
							name="orgBean.note" 
							value="${theForm.orgBean.note}"
							readonly="true"
							longTextType="true"
							comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagDefautPolicy" />
						</c:when>
						<c:otherwise>
						<vision:text 
							rwCtrlType="2" 
							permissionCode="" 
							dataType="6"
							name="orgBean.note" 
							value="${theForm.orgBean.note}"
							maxlength="200"
							bisname="˵��"
							longTextType="true"
							comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagDefautPolicy" />
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
		</table>
		</div>
	</c:otherwise>
</c:choose>
</form>
		<script language="javascript">
		if (CurrentPage == null) {
    	var CurrentPage = {};
		}   
		  
		CurrentPage.remove = function(oid) {
			if (!confirm("�Ƿ�Ҫɾ��ѡ��ļ�¼?")) {
				return false;
			} 
			FormUtils.post(document.forms[0], '<c:url value = "/sm/RoleAction.do?step=delete&oid="/>'+ oid );
		}
		
		CurrentPage.selectAll=function() {
			if(f.chkall.checked==true){
				for (var i = 0; i < f.length; i++) {
					if (f[i].id == "rid") {
						f[i].checked = b;
						}
					}
			}else{
				for (var i = 0; i < f.length; i++) {
					if (f[i].id == "rid") {
						f[i].checked = false;
					}
				}
			}
		}
			function countSelected() {
			    var c = 0;
			    for (var i = 0; i < f.length; i++) {
			        if (f[i].id == "rid") {
			           if (f[i].checked) {
			           	c++;
			            }
			        }
				}
			    return c;
			}
			
	CurrentPage.validation = function () {
		if(!verifyAllform()){return false;}
		return Validator.Validate(document.forms[0], 5);
	}
	
<c:choose>
	<c:when test="${theForm.orgRoleFlg =='role'}">
		CurrentPage.initValideInput = function () {
			document.getElementById('roleBean.displayOrder').dataType = 'Number';
			document.getElementById('roleBean.displayOrder').msg = "˳���ֻ������Ȼ��";	
			document.getElementById('fatherDisplayName').dataType = 'Require';
			document.getElementById('fatherDisplayName').msg = "���ڵ㲻��Ϊ��";
			document.getElementById('roleBean.roleCd').dataType = 'Require';
			document.getElementById('roleBean.roleCd').msg = "��ɫ��ʶ����Ϊ��";
			document.getElementById('input_text').dataType = 'Require';
			document.getElementById('input_text').msg = "ϵͳȨ�޲���Ϊ��";
			document.getElementById('roleBean.name').dataType = 'Require';
			document.getElementById('roleBean.name').msg = "��ɫ���Ʋ���Ϊ��";
		}
	</c:when>
	<c:otherwise>
		CurrentPage.initValideInput = function () {
			document.getElementById('orgBean.displayOrder').dataType = 'Number';
			document.getElementById('orgBean.displayOrder').msg = "˳���ֻ������Ȼ��";	
			document.getElementById('fatherDisplayName').dataType = 'Require';
			document.getElementById('fatherDisplayName').msg = "���ڵ㲻��Ϊ��";
			document.getElementById('orgBean.displayName').dataType = 'Require';
			document.getElementById('orgBean.displayName').msg = "��ʾ���Ʋ���Ϊ��";
		}
	</c:otherwise>
</c:choose>
		<c:if test="${theForm.consignFlg!='baseOrSystem'}">
			CurrentPage.initValideInput();
		</c:if>
		
		CurrentPage.deleteall = function() {			
		 var str="";
		 var dd=document.getElementsByName('detailIds');
		
			for(var i=0;i<dd.length;i++){
				if(dd[i].checked==true){
				
				var oid1=dd[i].value;
			
				str+=oid1+"$";
				}
			}
			if (str==""||str==null){
				alert("��ѡ��ɾ����");
			}else{
				if (!confirm("��ȷ��Ҫȫ��ɾ���� ")) {
			    	return false;
		    	}
			
				$('step').value = "deleteAll";
				FormUtils.post(document.forms[0], '<c:url value="/sm/RoleAction.do?step=deleteAll&oids="/>'+str);
			}
				
		}
	CurrentPage.settable =function(pageid){
		var url = '<c:url value="/curd/curdAction.do?step=setTable&pageId="/>';
		url+=pageid;
		definedWin.openListingUrl("setTable",url);
		}
		
		
				
	CurrentPage.submit = function() {
		if (!CurrentPage.validation()) {
			return false;
		}
		var strOrg = document.getElementById("orgRoleFlg").value;
		var strCreat = document.getElementById("hcreat").value;
		if(strCreat == "c"){
			$('oid').value = '';
		}
		var oldParentCode = document.getElementById("oldParentCode").value;
		if (strOrg == "org"){
			FormUtils.post(document.forms[0], '<c:url value = "/sm/RoleAction.do?step=roleMenuSave&oldParentCode="/>'+oldParentCode);
		} else  {
			FormUtils.post(document.forms[0], '<c:url value = "/sm/RoleAction.do?step=roleSave&oldParentCode="/>'+oldParentCode);
		}
	}
	//ѡ��Ŀ¼��֯sub����
	CurrentPage.selectMenu =function(){
		document.getElementById("oldParentCode").value = document.getElementById("parentCode").value;
		definedWin.openListingUrl('menuUpdate','<c:url value="/sm/RoleAction.do?step=selectMenu&oid2="/>');
  //	window.open ('<c:url value="/sm/RoleAction.do?step=selectMenu&oid2="/>', "newwindow", "height=600, width=540, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no");	
	}
	definedWin.returnListObject = function(arr){
		document.getElementById("fatherId").value = arr[0];
	  	document.getElementById("fatherDisplayName").value = arr[1];
	}
	<c:if test="${theForm.orgRoleFlg =='role'}">
	CurrentPage.create = function() {
  		var pat = new RegExp('([\?\&])parentCode=[^\&]*');
	  	var url = window.parent.panel.dataArr[1][2];
	  	var parentCode = window.parent.document.getElementById("parentCode").value;
	  	var strTemp = parentCode +";role;c"
		if (pat.test(url)) {
		   	window.parent.panel.dataArr[1][2]=url.replace(pat, RegExp.$1 + "parentCode=" + strTemp);
		  	window.parent.panel.click(1);
		  	window.parent.panel.dataArr[1][2]=url.replace(pat, RegExp.$1 + "parentCode=");
			}
	}
	</c:if>
	<c:if test="${theForm.orgRoleFlg =='org'}">
	CurrentPage.create = function() {
  		var pat = new RegExp('([\?\&])parentCode=[^\&]*');
	  	var url = window.parent.panel.dataArr[1][2];
	  	var parentCode = window.parent.document.getElementById("parentCode").value;
	  	var strTemp = parentCode +";org;c"
		if (pat.test(url)) {
		   	window.parent.panel.dataArr[1][2]=url.replace(pat, RegExp.$1 + "parentCode=" + strTemp);
		  	window.parent.panel.click(1);
		  	window.parent.panel.dataArr[1][2]=url.replace(pat, RegExp.$1 + "parentCode=");
			}
	}
	</c:if>
	function Validator.afterSuccessMessage(){
	if(parent.codeTree){
		parent.codeTree.Update(parent.codeTree.SelectId);
	}
	}	
</script>
</body>
<script type="text/javascript" src = "<c:out value = "${initParam['publicResourceServer']}/js/checkonchangevalues.js" />"></script>
</html>
