<%--
    @version:$Id: dataInfo.jsp,v 1.1 2010/12/10 10:56:42 silencily Exp $
    @since $Date: 2010/12/10 10:56:42 $
    @name 档案基本信息详细页面
--%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf" />
<html>
<body class="main_body">
<script language="javascript">
		if (CurrentPage == null) {
				var CurrentPage = {};
		}
		var requQues = new EditPage("requQues");
		
</script>
<table style="display:none">

                   <tr id="requQues_trId_[template]">

                            <td width="5%">

                               <div>
<vision:btn										
								rwCtrlType="2"										
								permissionCode="addRow"										
								wfPermissionCode="addRow"										
								onclick="CurrentPage.addRow(divId_requQues);return false"					
								clazz="list_create"	
								id="requQues_[template].add"
								style="display:" 
								name="addBtn" 
								title="新增" 							
								comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurity4WorkFlowListTotalPolicy" />	
					      <vision:btn										
								rwCtrlType="2"										
								permissionCode="delRowaa"										
								wfPermissionCode="delRowaa"										
								onclick="requQues.delRowaa(divId_requQues,this);return false"					
								clazz="list_delete"	
								id="requQues_[template].del"
								row="template" 
							    name="delBtn"
								style="display:" 
								title="删除" 							
								comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurity4WorkFlowListTotalPolicy" />	
						  <vision:btn 
								rwCtrlType="2"
				                wfPermissionCode="oldBtn" 
				                permissionCode="oldBtn" 
				                name="oldBtn"
				                style="display:none" 
				                holdObj="true" 
				                title="恢复" 
				                clazz="list_renew"
				                id="requQues_[template].old" 
				                row="template"
				                onclick="requQues.renRowCur(divId_requQues,this);return false;"
				                comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurity4WorkFlowListTotalPolicy" />
					</div>
                             

                            </td>          

                            <td align="left">

                    <input type="hidden" name="member[template].id" value="" id="requQues_[template].id"/>

                    <input type="hidden" name="member[template].delFlg" value="0" id="requQues_[template].delFlg"/>

                    <input type="hidden" name="member[template].version" value=""/>      

                    <vision:text 
					required="true" 
					maxlength="50" 
					rwCtrlType="2" 
					permissionCode=""
			    	id="temp.member[template].memberName" 
			    	name="member[template].memberName"
			    	value= ""
			    	comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />           
                    </td>	
                    <td>
                    <vision:text 
					maxlength="50" 
					rwCtrlType="2" 
					permissionCode=""
			    	id="temp.member[template].type" 
			    	name="member[template].type"
			    	value= ""
			    	comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" /> 
			    	</td>
			    	<td>
			    	<vision:text 
					maxlength="50" 
					rwCtrlType="2" 
					permissionCode=""
			    	id="temp.member[template].name" 
			    	name="member[template].name"
			    	value= ""
			    	comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />  
			    	</td>
			    	<td>
			    	<vision:text 
					maxlength="50" 
					rwCtrlType="2" 
					permissionCode=""
			    	id="temp.member[template].childrensName" 
			    	name="member[template].childrensName"
			    	value= ""
			    	comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
			    	</td>
</tr>

             </table>
<form name="f" action="" method="post">

	<input type="hidden" name="oid" value="<c:out value="${theForm.bean.id}"/>"/>
	<input type="hidden" name="oid" value="<c:out value="${theForm.oid}"/>"/>
	<input type="hidden" name="step" value="<c:out value='${theForm.step}'/>"/>
	<div class="update_subhead">
		<span class="switch_open" onClick="StyleControl.switchDiv(this,$('submenu1'))" title="伸缩节点">基本信息</span>
	</div>
	
	<table width="1000" border="0" cellpadding="0" cellspacing="0" class="Detail" id="submenu1">
		<tr>
		<td class="attribute">名称</td>
			<td>
				<vision:text 
				bisname="名称"
				required="true"
				id="theForm.bean.name"
				maxlength="50" 
				rwCtrlType="2" 
				permissionCode=""
			    name="bean.name"
			    value= "${theForm.bean.name}" 
			    comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy"/>
			</td>
			</tr>
		<tr>
			<td class="attribute">类名</td>
			<td>
                <vision:text 
                bisname="类名"
                required="true"
                id="theForm.bean.className" 
                maxlength="50" 
                rwCtrlType="2" 
                permissionCode=""
                name="bean.className"
				value="${theForm.bean.className}" 
				comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy"/>
			</td>
		</tr>

		<tr>
			<td class="attribute">所属模块</td>
            <td>
		    <vision:text 
		    bisname="所属模块"
		    required="true"
            id="theForm.bean.fatherModule" 
            maxlength="50" 
            rwCtrlType="2" 
            permissionCode=""
            name="bean.fatherModule"
            value="${theForm.bean.fatherModule}"
            comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy"/>
            </td>
		</tr>
		<tr id="tr_techTitle" style="display:none">
			<td colspan="2"><div id="div_techTitle" style="display:none"></div></td>
		</tr>	
	</table>
	<div class="update_subhead">				
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td>
						<span class="switch_open" onClick="StyleControl.switchDiv(this, $(divId_scrollLing))" title="点击伸缩节点">权限列表</span>
					</td>				
				</tr>
			</table>		
  </div>
  <div id="divId_scrollLing" class="list_scroll">
  <div class="list_scroll">
	  <table border="0" cellpadding="0" cellspacing="0" class="Listing" id="divId_requQues" 
				onClick="" >
					<thead>
						<tr>
							<td align="center" nowrap="nowrap" width="50">
							<vision:btn										
								rwCtrlType="2"										
								permissionCode="addRow"										
								wfPermissionCode="addRow"										
								onclick="CurrentPage.addRow(divId_requQues)"					
								clazz="list_create"	
								style="display:" name="addBtn" title="新增" 							
								comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurity4WorkFlowListTotalPolicy" />	
							</td>
							<td><div align="center">成员变量名</td>
							<td><div align="center">类型</td>
							<td><div align="center">名称</td>
							<td><div align="center">子节点集合变量名</td>
						</tr>
					</thead>
					<tbody>

					<c:forEach items = "${theForm.member}" var = "item" varStatus = "status" >
					    <c:set var="locRightBeanCount"  value = "${status.count}" scope = "page" />
								<tr id="requQues_trId_[<c:out value="${status.index}"/>]">
							<td width="5%" align="center">
							   <div>
							    <vision:btn										
								rwCtrlType="2"										
								permissionCode="addRow"										
								wfPermissionCode="addRow"										
								onclick="CurrentPage.addRow(divId_requQues);return false"					
								clazz="list_create"	
								id="requQues_[${status.index}].add"
								style="display:" name="addBtn" title="新增" 							
								comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurity4WorkFlowListTotalPolicy" />	
							    <vision:btn										
								rwCtrlType="${2}"										
								permissionCode="delRowaa"										
								wfPermissionCode="delRowaa"										
								onclick="requQues.delRowaa(divId_requQues,this);return false"					
								clazz="list_delete"	
								id="requQues_[${status.index}].del"
								row="${status.index}" 
							    name="delBtn"
								style="display:" title="删除" 							
								comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurity4WorkFlowListTotalPolicy" />	
								<vision:btn 
								      rwCtrlType="2"
				                      wfPermissionCode="oldBtn" 
				                      permissionCode="oldBtn" 
				                      name="oldBtn"
				                      style="display:none" 
				                      holdObj="true" 
				                      title="恢复" 
				                      clazz="list_renew"
				                      id="requQues_[${status.index}].old" 
				                      row="${status.index}"
				                      onclick="requQues.renRowCur(divId_requQues,this);return false;"
				                      comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurity4WorkFlowListTotalPolicy" />
							   </div>
							</td>	
							<td align="left">
					           <input type="hidden" name="member[<c:out value="${status.index}"/>].id" value="<c:out value="${item.id}"/>" id="requQues_[<c:out value='${status.index}'/>].id"/>
					           <input type="hidden" name="member[<c:out value='${status.index}'/>].delFlg" value="<c:out value='${item.delFlg}' />" id="requQues_[<c:out value='${status.index}'/>].delFlg"/>
					           <input type="hidden" name="member[<c:out value='${status.index}'/>].version" value="<c:out value='${item.version}' />"/> 	
                                <vision:text 
								required="true" 
								rwCtrlType="2"
								permissionCode=""
								value="${item.memberName}" 
								id="temp.member[${status.index}].memberName"
								name="member[${status.index}].memberName"
								comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />   	
					        </td>
                            <td>
                            <vision:text 
								rwCtrlType="2"
								permissionCode=""
								value="${item.type}" 
								id="temp.member[${status.index}].type"
								name="member[${status.index}].type"
							    comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
                            </td>
                            <td>
                            <vision:text 
								rwCtrlType="2"
								permissionCode=""
								value="${item.name}" 
								id="temp.member[${status.index}].name"
                                comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />  
                            </td>
                            <td>
                            <vision:text 
								rwCtrlType="2"
								permissionCode=""
								value="${item.childrensName}" 
								id="temp.member[${status.index}].childrenName"
                                comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />  
                            </td>
		                   </tr>
		                   
		            </c:forEach>						
		         </tbody>
	       </table>
</div>
	             
</div>  
  <div class="list_bottom"></div>  

<script type="text/javascript">
	var msgInfo_ = new msgInfo();
	if (CurrentPage == null) {
		var CurrentPage = {};
	}
	      var num = 0;
     
      CurrentPage.num = function () {
			num=document.all.divId_requQues.rows.length;
		}
    	 var msgInfo_ = new msgInfo();
    	 requQues.holdRow = 0;
    	requQues.rowNumber = $(divId_requQues).rows.length-1;
		requQues.uuidObj   = "id";

	
	CurrentPage.validation = function () {
	         if(!CurrentPage.validateEmpItems()){
				return false;
			}
		return Validator.Validate(document.forms[0], 5);
	}
	CurrentPage.initValideInfo = function () {
		
		document.getElementById('bean.name').dataType = 'Require';
		document.getElementById('bean.name').msg = msgInfo_.getMsgById('HR_I068_C_1',['名称']);
		document.getElementById('bean.className').dataType = 'Require';
		document.getElementById('bean.className').msg = msgInfo_.getMsgById('HR_I068_C_1',['类名']);
		document.getElementById('bean.fatherModule').dataType = 'Require';
		document.getElementById('bean.fatherModule').msg = msgInfo_.getMsgById('HR_I068_C_1',['所属模块']);

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
	CurrentPage.addRow = function(table) { 	
	  	num++;
		
			requQues.addListingRow(divId_requQues);
	
		
}	

    CurrentPage.create = function() {
			$('oid').value = '';
		$('step').value = 'info';
		FormUtils.post(document.forms[0], '<c:url value = "/sm/DataPermissionAction.do" />');
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
	FormUtils.post(document.forms[0], '<c:url value = "/sm/DataPermissionAction.do" />');
}

		/**
 * 验证成员信息是否重复
 */
CurrentPage.validateEmpItems = function() {
	var empMap = new Object();
		var tableRowNum=document.all.divId_requQues.rows.length;
		for(var i=0;i<tableRowNum-1;i++){
		    var number=i+1;
	        if(!document.getElementById("member["+i+"].memberName"))
					continue;
				var empCd=document.getElementById("member["+i+"].memberName").value;
				var delflg=document.getElementById(requQues.name+"_["+i+"].delFlg").value;
			    if(delflg=="") delflg="0";
				if(empCd=="" && delflg=="0" ){
 //<!--				CurrentPage.check("AM_I008_C_1","成员");-->
				CurrentPage.check("AM_I008_C_1",['第'+number+'行的成员']);
				return false;	
				}		
			}
		for (var i = 0; i < tableRowNum-1; i++) {
			if($('member[' + i + '].memberName')){
				var memberName = $('member[' + i + '].memberName').value;
				if(memberName==""||document.getElementById("member["+i+"].delFlg").value == '1'){
					continue;
				}
				
				var entryValue = empMap[memberName];
				if (entryValue == null) {
					entryValue = 0;
				}
				entryValue++;
				empMap[memberName] = entryValue;
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
		Validator.warnMessage("成员列表重复，保存失败！");
		for (var i = 0; i < repeatedKeys.length; i++) {
			Validator.warnMessage('   ' + repeatedKeys[i]);
			return false;
		}
	}
	return true;
}

         CurrentPage.check = function (msg,index) {
		    Validator.clearValidateInfo();
			 Validator.warnMessage(msgInfo_.getMsgById(msg,[index]));
			return false;
		}
CurrentPage.num();
</script>
</form>
</body>
 <script type="text/javascript" src = "<c:out value = "${initParam['publicResourceServer']}/js/checkonchangevalues.js"/>"></script>

</html>