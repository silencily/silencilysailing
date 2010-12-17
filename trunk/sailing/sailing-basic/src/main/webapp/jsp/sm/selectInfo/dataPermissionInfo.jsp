<%--
    @version:$Id: dataPermissionInfo.jsp,v 1.1 2010/12/10 10:56:17 silencily Exp $
    @since $Date: 2010/12/10 10:56:17 $
    @name 档案基本信息详细页面
--%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf" />
<jsp:directive.include file="/decorators/tablibs.jsp" />
<html>
<body class="main_body">
<form name="f" action="" method="post">
<input type="hidden" name="parentId" value="<c:out value = "${theForm.bean.id}"/>"/>
<input type="hidden" name="oid" value="<c:out value='${theForm.bean.id}'/>"/>
<input type="hidden" name="step" value="<c:out value='${theForm.step}'/>"/>
<input type="hidden" name="userId" value="<c:out value='${theForm.userId}'/>"/>
	<div class="update_subhead">
		<span class="switch_open" onClick="StyleControl.switchDiv(this,$('submenu1'))" title="伸缩节点">基本信息</span>
	</div>
	
		<table width="1000" border="0" cellpadding="0" cellspacing="0" class="Detail" id="submenu1">
		<tr>
		<td class="attribute">查询范围</td>
			<td>
				<vision:text 
				id="theForm.bean.tblCmnUserMember.searchScope"
				maxlength="50" 
				rwCtrlType="2" 
				permissionCode=""
			    name="searchScope"
			    value= '${theForm.searchScope}'
			    comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicyyTotalPolicy" />
			</td>
			</tr>
		<tr>
			<td class="attribute">修改范围</td>
			<td>
                <vision:text 
                id="theForm.bean.tblCmnUserMember.updateScope"
                maxlength="50" 
                rwCtrlType="2" 
                permissionCode=""
                name="updateScope"
				value= '${theForm.updateScope}'
				comInvorkeeClanet.silencily.sailing.common.crud.tag.TagSecurityTotalPolicyg.TagSecurityTotalPolicy" />
			</td>
		</tr>

		<tr>
			<td class="attribute">新建范围</td>
            <td>
		    <vision:text 
            id="theForm.bean.tblCmnUserMember.createScope" 
            maxlength="50" 
            rwCtrlType="2" 
            permissionCode=""
            name="createScope"
       		value= '${theForm.createScope}'
			conet.silencily.sailing.common.crud.tag.TagSecurityTotalPolicymmon.crud.tag.TagSecurityTotalPolicy" />
            </td>
		</tr>
		<tr>
			<td class="attribute">删除范围</td>
            <td>
		    <vision:text 
            id="theForm.bean.tblCmnUserMember.deleteScope" 
            maxlength="50" 
            rwCtrlType="2" 
            permissionCode=""
            name="deleteScope"
       		value= '${theForm.deleteScnet.silencily.sailing.common.crud.tag.TagSecurityTotalPolicyy.sailing.common.crud.tag.TagSecurityTotalPolicy" />
            </td>
		</tr>
	</table>
  <div class="list_bottom"></div>  

<script type="text/javascript">	
	var msgInfo_ = new msgInfo();
	var TopOpera = {};
	TopOpera.save   = function(){}
	if (CurrentPage == null) {
		var CurrentPage = {};
	}	
	CurrentPage.validation = function () {
			return Validator.Validate(document.forms[0],5);
		}
var userId = $('userId').value;
top.definedWin.selectListing = function(){
		var message = "" ;
		var scope = $('searchScope').value + $('updateScope').value + $('createScope').value  + $('deleteScope').value;
		if (scope == ""){
			alert("请添加至少一个权限");
			return;
		}
		if (!CurrentPage.validation()) {
			return;
		}
	FormUtils.post(document.forms[0], '<c:url value="/sm/DataMemberAction.do?step=save&userId=" />'+ userId);
	
	
	
}
Validator.afterSuccessMessage = function(info)
{
	top.definedWin.closeModalWindow();
	top.openWinCallBack();
}

</script>
</form>
</body>
<script type="text/javascript" src = "<c:out value = "${initParam['publicResourceServer']}/js/checkonchangevalues.js" />"></script>
</html>