<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf" />
<script type="text/javascript">	
		if (CurrentPage == null) {
			var CurrentPage = {};
		}
		if(requQues == null){
			var requQues = new EditPage("requQues");		
		}
		var msgInfo_ = new msgInfo();
</script>
<table style="display:none">
	<tr id="requQues_trId_[template]">
		<td width="5%">
				<input 	
					type="hidden" 
					value=""
					id="requQues_[template].id"
					name="tableViews[template].id"/>
				<input 
					type="hidden" 
					value="0" 
					name="tableViews[template].delFlg"
					id="requQues_[template].delFlg"/>
				<input 
					type="hidden" 
					id="requQues_[template].version" 
					name="tableViews[template].version" 
					value=""/>
				<vision:btn rwCtrlType="2"
				wfPermissionCode="requQues_[template].add"
				permissionCode="requQues_[template].add" style="display:"
				title="新增" clazz="list_create" id="requQues_[template].add"
				onclick="CurrentPage.addRow(divId_requQues);return false;"
				comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
				
			<vision:btn rwCtrlType="2"
				wfPermissionCode="delBtn" permissionCode="delBtn" name="delBtn"
				style="display:" title="删除" clazz="list_delete"
				id="requQues_[template].del" row="template"
				onclick="requQues.delRowaa(divId_requQues,this);return false;"
				comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
				
			<vision:btn rwCtrlType="2"
				wfPermissionCode="oldBtn" permissionCode="oldBtn" name="oldBtn"
				style="display:none" holdObj="true" title="恢复" clazz="list_renew"
				id="requQues_[template].old" row="template"
				onclick="requQues.renRowCur(divId_requQues,this);return false;"
				comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
				
		</td>
		<td>
			<input type="checkbox"
				id= "tableViews[template].checkbox" 
				value = ""/>
		</td>
		<td>
			<vision:text
				rwCtrlType="2" 
				permissionCode=""
				maxlength="100" 
				bisname="数据项标识" 
				name="tableViews[template].rowName" 
				id="tableViews[template].rowName" 
				value=""
				comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
		</td>
		<td>
			<vision:text
				rwCtrlType="2" 
				permissionCode=""
				maxlength="100" 
				bisname="数据项名称" 
				name="tableViews[template].rowDisplayname" 
				id="tableViews[template].rowDisplayname" 
				value=""
				comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
		</td>
		<td>
			<vision:text
				rwCtrlType="2" 
				permissionCode=""
				bisname="数据列排序"
				dataType="0" 
				name="tableViews[template].orderby" 
				id="tableViews[template].orderby" 
				value=""
				comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
		</td>
		<td>
			<vision:text
				rwCtrlType="2" 
				permissionCode=""
				maxlength="3" 
				dataType="0" 
				bisname="显示排序"
				name="tableViews[template].sortNum" 
				id="tableViews[template].sortNum" 
				value=""
				comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
		</td>	
		<td>
			<vision:text
				rwCtrlType="2" 
				permissionCode=""
				bisname="超链接"
				maxlength="200"
				name="tableViews[template].popLink" 
				id="tableViews[template].popLink" 
				value=""
				comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
		</td>
		<td>
			<vision:text
				rwCtrlType="2" 
				permissionCode=""
				maxlength="100" 
				bisname="显示格式"
				name="tableViews[template].style" 
				id="tableViews[template].style" 
				value=""
				comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
		</td>																
		<td>
			<ec:composite 
				value=""
				valueName = "tableViews[template].isDbField" 
			    textName = "temp.tableViews[template].isDbField" 
			    source = "${theForm.isDbFieldComboList}"/>	
		</td>
   </tr>	
</table>
<body class="main_body">
<form method="post" action="" name="infoForm1">
<input name="oid" type="hidden" value="<c:out value = '${theForm.oid}' />">
<input name="step" type="hidden" value="<c:out value = '${theForm.step}' />">
<div class="update_subhead">
<table cellspacing="0" cellpadding="0" border="0" width="100%">
	<tr>
		<td><span title="点击收缩节点" class="switch_open"
			onClick="StyleControl.switchDiv(this,$('info_table'))">画面详细</span></td>
	</tr>
</table>
</div>
<table border="0" width="100%" class="Detail" id="info_table">
	<tr>
		<td class="attribute">画面标识</td>
		<td>
			<vision:text
				comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy"
				maxlength="100"
				bisname="画面标识" 
				dataType="0"
				name="bean.tableName"
				id="bean.tableName" 
				required="true"
				permissionCode="" 
				rwCtrlType="2"
				value="${theForm.bean.tableName}" />				
		</td>
		<td class="attribute">画面名称</td>
		<td>
			<vision:text
				comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy"
				maxlength="4000"
				bisname="画面名称" 
				dataType="0"
				name="bean.screenName"
				id="bean.screenName" 
				required="true"
				permissionCode="" 
				rwCtrlType="2"
				value="${theForm.bean.screenName}" />	
		</td>
	</tr>
	<tr>
		<td class="attribute">默认排序</td>
		<td colspan = "3">
			<ec:composite 
				value='${theForm.orderAsc}'
				valueName = "orderAsc" 
			    textName = "temp.conditions(orderAsc).value" 
			    source = "${theForm.orderAscComboList}"/>		
		</td>
	</tr>	
</table>
<div class="update_subhead">
<table cellspacing="0" cellpadding="0" border="0" width="100%">
	<tr>
		<td><span
			onClick="StyleControl.switchDiv(this,$('dingzhixianshi'))"
			title="点击收缩节点" class="switch_open">定制显示</span></td>
	</tr>
</table>
</div>
<div id="dingzhixianshi" style="overflow-x:scroll;width:100%">
<table id="divId_requQues" class="Listing" cellspacing="0"
	cellpadding="0" border="0">
	<thead>
		<tr>
			<td width="5%">
				<vision:btn
					rwCtrlType="2"
					wfPermissionCode="addBtn" 
					permissionCode="addBtn"
					name="addBtn" 
					style="display:" 
					title="新增" 
					clazz="list_create"
					onclick="CurrentPage.addRow(divId_requQues);return false"
					comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
			</td>
			<td>是否排序</td>
			<td>数据项标识</td>
			<td>数据项名称</td>
			<td>数据列排序</td>
			<td>显示排序</td>
			<td>超链接</td>
			<td>显示格式</td>
			<td>是否数据库字段</td>
		</tr>
	</thead>
	<tbody style="text-align:center">
		<c:forEach 
			varStatus="status" 
			var="item"
			items="${theForm.tableViews}">
			<tr onClick="TableSort.clickListing(event)"
				id="requQues_trId_[<c:out value='${status.index}'/>]">
				<td width="5%">
					<input type="hidden" name="tableViews[<c:out value='${status.index}'/>].id"
						value="<c:out value='${item.id}'/>"
						id="requQues_[<c:out value='${status.index}'/>].id">
					<input	
						type="hidden"
						name="tableViews[<c:out value='${status.index}'/>].delFlg"
						value="<c:out value='${item.delFlg}' />"
						id="requQues_[<c:out value='${status.index}'/>].delFlg">
					<input	
						type="hidden"
						name="tableViews[<c:out value='${status.index}'/>].version"
						value="<c:out value='${item.version}' />"> 
					<vision:btn	
						rwCtrlType="2"
						wfPermissionCode="requQues_[${status.index}].add" 
						permissionCode="requQues_[${status.index}].add"
						style="display:" 
						title="新增" 
						clazz="list_create"
						onclick="CurrentPage.addRow(divId_requQues);return false"
						id="requQues_[${status.index}].add"
						comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
					<vision:btn	
						rwCtrlType="2"
						wfPermissionCode="requQues_[${status.index}].del" 
						permissionCode="requQues_[${status.index}].del"
						name="delBtn" 
						style="display:" 
						title="删除" 
						clazz="list_delete"
						onclick="requQues.delRowaa(divId_requQues,this);return false"
						id="requQues_[${status.index}].del" 
						row="${status.index}"
						comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
					<vision:btn
						rwCtrlType="2"
						wfPermissionCode="requQues_[${status.index}].old" 
						permissionCode="requQues_[${status.index}].old"
						name="oldBtn" 
						style="display:none" 
						holdObj="true" 
						title="恢复"
						clazz="list_renew"
						onclick="requQues.renRowCur(divId_requQues,this);return false"
						id="requQues_[${status.index}].old" 
						row="${status.index}"
						comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
				</td>
				<td>
					<c:choose>
						<c:when test="${item.sortNum !='' && item.sortNum != null}">
							<input type="checkbox"
								id= "tableViews[<c:out value='${status.index}'/>].checkbox" 
								checked ="true"
								value = ""/>
						</c:when>
						<c:otherwise>
							<input type="checkbox"
								id= "tableViews[<c:out value='${status.index}'/>].checkbox" 
								value = ""/>
						</c:otherwise>
					</c:choose>
				</td>
				<td>
					<vision:text
						rwCtrlType="2" 
						permissionCode=""
						maxlength="100" 
						bisname="数据项标识" 
						name="tableViews[${status.index}].rowName" 
						id="tableViews[${status.index}].rowName" 
						value="${item.rowName}"
						comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
				</td>
				<td>
					<vision:text
						rwCtrlType="2" 
						permissionCode=""
						maxlength="100" 
						bisname="数据项名称" 
						name="tableViews[${status.index}].rowDisplayname" 
						id="tableViews[${status.index}].rowDisplayname" 
						value="${item.rowDisplayname}"
						comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
				</td>
				<td>
					<vision:text
						rwCtrlType="2" 
						permissionCode=""
						bisname="数据列排序"
						dataType="0" 
						name="tableViews[${status.index}].orderby" 
						id="tableViews[${status.index}].orderby" 
						value="${item.orderby}"
						comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
				</td>
				<td>
					<vision:text
						rwCtrlType="2" 
						permissionCode=""
						maxlength="3" 
						dataType="0" 
						bisname="显示排序"
						name="tableViews[${status.index}].sortNum" 
						id="tableViews[${status.index}].sortNum" 
						value="${item.sortNum}"
						comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
				</td>	
				<td>
					<vision:text
						rwCtrlType="2" 
						permissionCode=""
						bisname="超链接"
						maxlength="200"
						name="tableViews[${status.index}].popLink" 
						id="tableViews[${status.index}].popLink" 
						value="${item.popLink}"
						comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
				</td>
				<td>
					<vision:text
						rwCtrlType="2" 
						permissionCode=""
						maxlength="100" 
						bisname="显示格式"
						name="tableViews[${status.index}].style" 
						id="tableViews[${status.index}].style" 
						value="${item.style}"
						comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagSecurityTotalPolicy" />
				</td>																
				<td>
					<ec:composite 
						value='${item.isDbField}'
						valueName = "tableViews[${status.index}].isDbField" 
					    textName = "temp.tableViews[${status.index}].isDbField" 
					    source = "${theForm.isDbFieldComboList}"/>	
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
</div>
</form>
</body>
<script type="text/javascript">
requQues.rowNumber = $(divId_requQues).rows.length-1;
requQues.uuidObj   = "id";
CurrentPage.addRow = function(table){
	requQues.addListingRow(divId_requQues);
	Global.setHeight();	
}

CurrentPage.create = function () {
	$('oid').value = '';
	FormUtils.post(document.forms[0], '<c:url value = "/sm/ctscreen.do?step=info" />');
}

CurrentPage.submit = function () {
	if(!CurrentPage.validation()){return false;}
	if(!verifyAllform()){return false;}
	FormUtils.post(document.forms[0], '<c:url value = "/sm/ctscreen.do?step=save" />');

}
CurrentPage.validation = function () {
	Validator.clearValidateInfo();
	if( !Validator.Validate(document.forms[0], 5)){
		return false;
	}
	var tableViews=requQues.rowNumber;
	for(var i = 0;i < tableViews;i++){
		var orderby = document.getElementById("tableViews[" + i + "].orderby");
		var sortNum = document.getElementById("tableViews[" + i + "].sortNum");
		var checkbox = document.getElementById("tableViews[" + i + "].checkbox");
		var num = i + 1;
		if(orderby && !Validator.isNumber(orderby.value)){
			Validator.warnMessage(msgInfo_.getMsgById('CM_I038_C_2',[num,'数据列排序']));
			orderby.focus();
			return false;
		}
		if(sortNum && !Validator.isNumber(sortNum.value)){
			Validator.warnMessage(msgInfo_.getMsgById('CM_I038_C_2',[num,'显示排序']));
			sortNum.focus();
			return false;
		}
		if(checkbox && checkbox.checked == false){
			document.getElementById("tableViews[" + i + "].sortNum").value = "";
		}
	}
	return true;
}

CurrentPage.initValideInput = function () {
	if($('bean.tableName')){
		$('bean.tableName').dataType = 'Require';
		$('bean.tableName').msg = msgInfo_.getMsgById('CM_I032_C_1',['画面标识']);	
	}
	if($('bean.screenName')){
		$('bean.screenName').dataType = 'Require';
		$('bean.screenName').msg = msgInfo_.getMsgById('CM_I032_C_1',['画面名称']);	
	}
}	
CurrentPage.initValideInput();
Global.setHeight();
</script>
<script type="text/javascript" src = "<c:out value = "${initParam['publicResourceServer']}/js/checkonchangevalues.js" />"></script>	
