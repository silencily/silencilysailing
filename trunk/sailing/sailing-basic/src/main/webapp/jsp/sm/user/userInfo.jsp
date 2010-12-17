<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf" />
<jsp:directive.include file="/decorators/tablibs.jsp" />
<html>
<body class="main_body">
<form name="f" action="" method="post">

	<input type="hidden" name="oid" value="<c:out value="${theForm.bean.id}"/>"/>
	<input type="hidden" name="oid" value="<c:out value="${theForm.oid}"/>"/>
	<input type="hidden" name="step" value="<c:out value='${theForm.step}'/>"/>
	<div class="update_subhead">
		<span class="switch_open" onClick="StyleControl.switchDiv(this,$('submenu1'))" title="伸缩节点">基本信息</span>
	</div>
	
	<table width="1000" border="0" cellpadding="0" cellspacing="0" class="Detail" id="submenu1">
		<tr>
			<td class="attribute">员工编号</td>
			<td>
				<vision:text 
				bisname="员工编号" 
				required="true" 
				maxlength="50" 
				rwCtrlType="2" 
				permissionCode=""
			    name="bean.empCd" 
			    value= '${theForm.bean.empCd}'
			    comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
			</td>

			</tr>
		<tr>
			<td class="attribute">员工姓名</td>
			<td>
                <vision:text 
                bisname="员工姓名" 
                required="true"  
                maxlength="50" 
                rwCtrlType="2" 
                permissionCode=""
				name="bean.empName" 
				value= '${theForm.bean.empName}'
				comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
			</td>
		</tr>
		<tr>
			<td class="attribute">性别</td>
			<td>
				<vision:choose 
				bisname="性别" 
				required="true" 
				rwCtrlType="2" 
				permissionCode=""
				textName="bean.sex.name" 
				valueName="bean.sex.code" 
				value= '${theForm.bean.sex}' 
				buttonName="btn_sex" 
				source='${theForm.sysCodes["HR"]["XBIE"]}'
				comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
			</td>
		</tr>
		<tr>
			<td class="attribute">民族</td>
			<td>
				<vision:choose  
				rwCtrlType="2" 
				permissionCode=""
				textName="bean.nation.name" 
				valueName="bean.nation.code" 
				value= '${theForm.bean.nation}' 
				buttonName="btn_nation" 
				source='${theForm.sysCodes["HR"]["MINZ"]}'
				comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
			</td>
		</tr>
		<tr>
			<td class="attribute">出生日期</td>
            <td>
		    <vision:text 
		    bisname="出生日期" 
		    required="true" 
		    rwCtrlType="2" 
		    permissionCode=""
			name="bean.birthday" 
			value='${theForm.bean.birthday}' 
			dataType="2"
			canDelete="true"
			comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
            </td>
		</tr>
		<tr>
			<td class="attribute">籍贯</td>
			<td>
		    <vision:text  
		    maxlength="100" 
		    longTextType="true" 
		    maxviewlength="100" 
		    rwCtrlType="2" 
		    permissionCode=""
			name="bean.nativePlace" 
			value= '${theForm.bean.nativePlace}'
			comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
			</td>			
		</tr>
		<tr>
			<td class="attribute">证件号码</td>
			<td>
				<vision:text  
				rwCtrlType="2" 
				permissionCode=""
				name="bean.idCard" 
				value="${theForm.bean.idCard}" 
				comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
			</td>
		</tr>
		<tr>
			<td class="attribute" >激活状态</td>
			<td>
                <ec:composite value='${theForm.bean.status}'  valueName = "bean.status" textName = "bean.conditions(status).value" source = "${theForm.statusComboList}"/>
			</td>
		</tr>
		<tr>
			<td class="attribute">部门</td>
			<td valign="middle">
				<input type="hidden" name="deptId" id="deptId" value="<c:out value='${theForm.bean.tblCmnDept.id}'/>" />
				<input type="text" name="deptName" id="deptName" value="<c:out value='${theForm.bean.tblCmnDept.deptName}'/>" class="readonly" readonly="readonly" />
				<span class="font_request">*</span>
				<input type="button" title="点击选择部门" id="select_fromtree"
					onClick="showCgmlTreeWindow(deptId, 'deptId', 'deptName', '', '1');"/>	
			</td>
		</tr>
	</table>
  <div class="list_bottom"></div>  

<script type="text/javascript">
	var msgInfo_ = new msgInfo();
	if (CurrentPage == null) {
		var CurrentPage = {};
	}	
	CurrentPage.validation = function () {
			return Validator.Validate(document.forms[0],5);
		}
	CurrentPage.initValideInfo = function () {
		
		document.getElementById('bean.empCd').dataType = 'Number';
		document.getElementById('bean.empCd').msg = msgInfo_.getMsgById('HR_I068_C_1',['员工编号']);
		document.getElementById('bean.empName').dataType = 'Require';
		document.getElementById('bean.empName').msg = msgInfo_.getMsgById('HR_I068_C_1',['员工姓名']);
		document.getElementById('bean.sex.code').dataType = 'Require';
		document.getElementById('bean.sex.code').msg = msgInfo_.getMsgById('HR_I068_C_1',['性别']);
		document.getElementById('bean.birthday').dataType = 'Require';
		document.getElementById('bean.birthday').msg = msgInfo_.getMsgById('HR_I068_C_1',['出生日期']);
		document.getElementById('deptName').dataType = 'Require';
		document.getElementById('deptName').msg = msgInfo_.getMsgById('HR_I068_C_1',['部门']);
	}

	var today= DateUtils.GetDBDate();
	var month=today.getMonth()+1;
	month=month<10? "0"+month:month;
	var tday=today.getDate();
	tday=tday<10? "0"+tday:tday;
 	// 日期为 yyyy-mm-dd 格式
	var strToday= today.getFullYear() + "-" + month + "-" + tday;
    CurrentPage.CheckDay = function (day) {
		if (day != "") {
			if(DateUtils.CompareDate(strToday,day)>0)
				return false;
		} else {
			return true;
		}
	}
    CurrentPage.create = function() {
			$('oid').value = '';
		$('step').value = 'info';
		FormUtils.post(document.forms[0], '<c:url value = "/sm/userManageAction.do" />');
	}
	CurrentPage.submit = function() {
		if(!verifyAllform()) {return false;}
		CurrentPage.initValideInfo();
		var message = "" ;
		if (!CurrentPage.validation()) {
			return;
		}
	var id = $('oid').value;
	if (id == null || id.length == 0) {
		$('step').value = 'save';
		
	} else {
		$('step').value = 'update';
	}
	FormUtils.post(document.forms[0], '<c:url value = "/sm/userManageAction.do" />');
}
</script>
</form>
</body>
</html>