<%--
    @version:$Id: dataMemberInfo.jsp,v 1.1 2010/12/10 10:56:42 silencily Exp $
    @since $Date: 2010/12/10 10:56:42 $
    @name 档案基本信息详细页面
--%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf" />
<jsp:directive.include file="/decorators/tablibs.jsp" />
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
								onClick="CurrentPage.addRow(divId_requQues);return false"					
								clazz="list_create"	
								id="requQues_[template].add"
								style="display:" 
								name="addBtn" 
								title="新增" 							
								comInvorkeeClassFullName="com.qware.common.crud.tag.TagSecurity4WorkFlowListTotalPolicy" />	
					      <vision:btn										
								rwCtrlType="2"										
								permissionCode="delRowaa"										
								wfPermissionCode="delRowaa"										
								onClick="requQues.delRowaa(divId_requQues,this);return false"					
								clazz="list_delete"	
								id="requQues_[template].del"
								row="template" 
							    name="delBtn"
								style="display:" 
								title="删除" 							
								comInvorkeeClassFullName="com.qware.common.crud.tag.TagSecurity4WorkFlowListTotalPolicy" />	
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
				                comInvorkeeClassFullName="com.qware.common.crud.tag.TagSecurity4WorkFlowListTotalPolicy" />
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
			    	id="temp.member[template].searchScope" 
			    	name="member[template].searchScope"
			    	value= ""
			    	comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicyyTotalPolicy" />           
                    </td>	
                    <td>
                    <vision:text 
					required="true" 
					maxlength="50" 
					rwCtrlType="2" 
					permissionCode=""
			    	id="temp.member[template].updateScope" 
			    	name="member[template].updateScope"
			    	value= ""
			    	comInvorkeeClanet.silencily.sailing.common.crud.tag.TagSecurityTotalPolicyg.TagSecurityTotalPolicy" /> 
			    	</td>
			    	<td>
			    	<vision:text 
					required="true" 
					maxlength="50" 
					rwCtrlType="2" 
					permissionCode=""
			    	id="temp.member[template].createScope" 
			    	name="member[template].createScope"
			    	value= ""
			    	conet.silencily.sailing.common.crud.tag.TagSecurityTotalPolicymmon.crud.tag.TagSecurityTotalPolicy" />  
			    	</td>
			    	<td>
			    	<vision:text 
					required="true" 
					maxlength="50" 
					rwCtrlType="2" 
					permissionCode=""
			    	id="temp.member[template].deleteScope" 
			    	name="member[template].deleteScope"
			    	value= ""net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicyy.sailing.common.crud.tag.TagSecurityTotalPolicy" />
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
		<td class="attribute">成员变量名</td>
			<td>
				<vision:text 
				bisname="成员变量名"
				required="true"
				id="theForm.bean.memberName"
				maxlength="50" 
				rwCtrlType="2" 
				permissionCode=""
			    name="bean.memberName"
			    value= '${theForm.beannet.silencily.sailing.common.crud.tag.TagSecurityTotalPolicynet.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
			</td>
			</tr>
		<tr>
			<td class="attribute">类型</td>
			<td>
                <vision:text 
                bisname="类型"
                required="true"
                id="theForm.bean.type" 
                maxlength="50" 
                rwCtrlType="2" 
                permissionCode=""
                name="bean.type"
				vnet.silencily.sailing.common.crud.tag.TagSecurityTotalPolicyssFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
			</td>
		</tr>

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
            name="bean.namenet.silencily.sailing.common.crud.tag.TagSecurityTotalPolicymInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
            </td>
		</tr>
		<tr>
			<td class="attribute">子节点集合变量名</td>
            <td>
		    <vision:text 
            id="theForm.bean.childrensName" 
            maxlength="50" 
            rwCtrlType="2" 
            permissionCode=""
            name="bean.childrensNnet.silencily.sailing.common.crud.tag.TagSecurityTotalPolicyame}'
			comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
            </td>
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
								onClick="CurrentPage.addRow(divId_requQues)"					
								clazz="list_create"	
								style="display:" name="addBtn" title="新增" 							
								comInvorkeeClassFullName="com.qware.common.crud.tag.TagSecurity4WorkFlowListTotalPolicy" />	
							</td>
							<td><div align="center">查询范围</td>
							<td><div align="center">修改范围</td>
							<td><div align="center">新增范围</td>
							<td><div align="center">删除范围</td>
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
								onClick="CurrentPage.addRow(divId_requQues);return false"					
								clazz="list_create"	
								id="requQues_[${status.index}].add"
								style="display:" name="addBtn" title="新增" 							
								comInvorkeeClassFullName="com.qware.common.crud.tag.TagSecurity4WorkFlowListTotalPolicy" />	
							    <vision:btn										
								rwCtrlType="${2}"										
								permissionCode="delRowaa"										
								wfPermissionCode="delRowaa"										
								onClick="requQues.delRowaa(divId_requQues,this);return false"					
								clazz="list_delete"	
								id="requQues_[${status.index}].del"
								row="${status.index}" 
							    name="delBtn"
								style="display:" title="删除" 							
								comInvorkeeClassFullName="com.qware.common.crud.tag.TagSecurity4WorkFlowListTotalPolicy" />	
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
				                      comInvorkeeClassFullName="com.qware.common.crud.tag.TagSecurity4WorkFlowListTotalPolicy" />
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
								value="${item.searchScope}" 
								id="temp.member[${status.index}].searchScope"
								name="member[${status.index}].snet.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy                      comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />   	
					        </td>
                            <td>
                            <vision:text 
								required="true" 
								rwCtrlType="2"
								permissionCode=""
								value="${item.updateScope}" 
								id="temp.member[${status.index}].updateScope"
								name="member[${statnet.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy
                                comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />  
                            </td>
                            <td>
                            <vision:text 
								required="true" 
								rwCtrlType="2"
								permissionCode=""
								value="${item.createScope}" 
								id="temp.member[${status.index}].createScope"
								name="mnet.silencily.sailing.common.crud.tag.TagSecurityTotalPolicyuired="true"
                                comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />  
                            </td>
                            <td>
                            <vision:text 
								required="true" 
								rwCtrlType="2"
								permissionCode=""
								value="${item.deleteScope}" 
								id="temp.member[${status.index}].deleteScope"
			net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy
								required="true"
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
		return Validator.Validate(document.forms[0], 5);
	}
	CurrentPage.initValideInfo = function () {
		
		document.getElementById('bean.memberName').dataType = 'Require';
		document.getElementById('bean.memberName').msg = msgInfo_.getMsgById('HR_I068_C_1',['成员变量名']);
		document.getElementById('bean.type').dataType = 'Require';
		document.getElementById('bean.type').msg = msgInfo_.getMsgById('HR_I068_C_1',['类型']);
		document.getElementById('bean.name').dataType = 'Require';
		document.getElementById('bean.name').msg = msgInfo_.getMsgById('HR_I068_C_1',['名称']);

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
	FormUtils.post(document.forms[0], '<c:url value = "/sm/DataMemberAction.do" />');
}

CurrentPage.num();

	CurrentPage.selectEmp = function(empInfoPre){
		definedWin.openListingUrl(empInfoPre,'<c:url value='/sm/SelectInfoAction.do?step=selectEntry'/>');
	}
</script>
</form>
</body>
<script type="text/javascript" src = "<c:out value = "${initParam['publicResourceServer']}/js/checkonchangevalues.js" />"></script>
</html>