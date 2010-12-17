<%--
    @version:$Id: deptEdit.jsp,v 1.1 2010/12/10 10:56:43 silencily Exp $
    @since $Date: 2010/12/10 10:56:43 $
    @name:部门合并
--%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ include file="/decorators/edit.jspf" %>
<%@ include file="/decorators/default.jspf" %>
<html>
<head>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
</head>
<body class="main_body">
<form name="f" action="" method="post">
<input type="hidden" name="parentCode" value="<c:out value = "${theForm.parentCode}"/>"/>
<input type="hidden" name="oid" value="<c:out value='${theForm.bean.id}'/>"/>
<input type="hidden" name="pid" value="<c:out value='${theForm.bean.parentId}'/>"/>
<input type="hidden" name="step" value="<c:out value='${theForm.step}'/>"/>
<input type="hidden" name="step" value="deptEdit"/>
<input type="hidden" name="beginPDN" value="<c:out value='${theForm.bean.deptName}' escapeXml='true'/>"/>
<input type="hidden" name="beginstate" value="正在使用" />
<input type="hidden" name="endstate" value="停止使用" />
<c:set var="parent" value="${theForm.bean.parent}"/>
<input type="hidden" name="dpn" id="dpn" value="<c:out value='${parent.deptName}' escapeXml='true'/>"/>
<!-- delete by xueyi start 08.01.15 -->
<!--<div class="list_group">-->
<!--</div>-->
<!-- delete by xueyi end 08.01.15 -->
<%@ include file="deptParent.jspf" %>
<div class="update_subhead">
	 <span class="switch_open" onClick="StyleControl.switchDiv(this,submenudept)" title="伸缩节点">部门合并</span>
</div>
<table border="0" cellpadding="0" cellspacing="0" class="Detail" id="submenudept">
	<tr>
		<td class="attribute">合并时间</td>
		<td><input type="text"	value="" id="input_text" name="statusBean.changeTime" class="readonly" readonly="readonly"/><input type="button" 
		value="" id="input_date" onClick="DateComponent.setDay(this,document.getElementsByName('statusBean.changeTime')[0])" />
			<span class="font_request">*</span>&nbsp;
		</td>
		<script language=JavaScript>
		var enabled = 0; today = new Date();
		var date;
		date =(today.getYear()) + "-" + (today.getMonth() + 1 ) + "-" + today.getDate();
		if(document.getElementById('statusBean.changeTime').value==""){
		document.getElementById('statusBean.changeTime').value = date;
		}
		</script>
		<%--deptName--%>	
		<td class="attribute">合并到部门</td>
		<%--deptId--%>
			<input type="hidden" id="parentId" name="bean.parentId" value="<c:out value='${parent.deptCd}'/>" class="readonly" readonly="readonly"/>
			<input type="hidden" id="parentCode" name="dummy.bean.parentCode" value="<c:out value='${parent.deptCd}'/>" class="readonly" readonly="readonly"/>
		<td><input name="departmentnameNew" value=""class="readonly" readonly="readonly">&nbsp;<span class="font_request">*</span>
		<input type="button" id="select_fromtree" name="deptchange"  title="点击选择部门"
		onClick="javascript:showCgmlTreeWindow(parentId, 'bean.parentId', 'departmentnameNew', '', '1');"/>
		</td>
	</tr>
</table>
<div class="update_subhead">
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td>
				<span class="switch_open" onClick="StyleControl.switchDiv(this, $('listtable1'))" title="伸缩节点">变更履历</span>
			</td>
		</tr>
	</table>
</div>
<div id="divId_scrollLing" class="list_scroll">
	<table border="0" cellpadding="0" cellspacing="0" class="Listing" id="listtable1"  onClick="TableSort.sortColumnOnly(event)">
		<thead>
			<tr>
				<td type='Number' style='width:30px;'>序号</td>
				<td>变更时间</td>
				<td>操作人</td>
				<td>操作时间</td>
				<td>原状态</td>
				<td>新状态</td>
				<td>备注</td>
			</tr>
		</thead>
		<tbody> 		
		<c:forEach var="item" items="${theForm.bean.tblCmnDeptStatusRecs}" varStatus="status">
			<tr>
				<input type="hidden" name="oid" value="<c:out value="${item.id}"/>"/>
				<td align="right"><c:out value="${status.count}"/>&nbsp;</td>
				<td align="center"><fmt:formatDate value="${item.changeTime}" pattern = "yyyy-MM-dd"/>&nbsp;</td>
				<td align="left"><c:out value="${item.operator}" escapeXml='true'/>&nbsp;</td>
				<td align="center"><fmt:formatDate value = "${item.operateTime}" pattern = "yyyy-MM-dd HH:mm:ss" />&nbsp;</td>
				<td align="center">
					<c:choose>
						<c:when test="${item.beginState== '0'}"><font color="Red"><c:out value="正在使用"/></font></c:when>
						<c:otherwise>
							<c:choose>
								<c:when test="${item.beginState == '1'}"><c:out value="停止使用"/></c:when>
							</c:choose>
						</c:otherwise>
					</c:choose>&nbsp;
				</td>
				<td align="center">
					<c:choose>
						<c:when test="${item.endState== '0'}"><font color="Red"><c:out value="正在使用"/></font></c:when>
						<c:otherwise>
							<c:choose>
								<c:when test="${item.endState == '1'}"><c:out value="停止使用"/></c:when>
							</c:choose>
						</c:otherwise>
					</c:choose>&nbsp;
				</td>
				<td style="width:130px;">
					<input type="text" id="textArea" name="changes[<c:out value='${status.index}'/>].remark" class="input_long" style="width:100px;" readonly value="<c:out value='${item.remark}'/>"/>
					<input type="button" id="edit_longText" title="<c:out value='${item.remark}' escapeXml='true'/>" onClick="definedWin.openLongTextWin(document.getElementsByName('changes[<c:out value='${status.index}'/>].remark')[0],'',true);"/>
				</td>
			</tr>										
		</c:forEach>					
		</tbody>
	</table>
</div>
<script type="text/javascript"><!--	
var msgInfo_ = new msgInfo();
var str="aa";
	if (CurrentPage == null) {
		var CurrentPage = {};
	}
	
	CurrentPage.reset = function () {
		document.f.reset();
	}
<c:if test="${theForm.bean.deptState != '1'}">	
	CurrentPage.submit = function () {
		if (!CurrentPage.validation()) {
			return;
		}
		<%--验证时间是否是未来日期--%>
		if (CurrentPage.CheckDay() == false) {
			return false;
		}		
	<%--	if(document.getElementById('dpn').value==document.getElementById('departmentnameNew').value){
			alert(msgInfo_.getMsgById('HR_I039_C_1',['合并到部门']));
			return;
		} --%>
		if (!confirm(msgInfo_.getMsgById('HR_I021_A_4',[document.getElementById('beginPDN').value,document.getElementById('departmentnameNew').value,document.getElementById('beginPDN').value,document.getElementById('departmentnameNew').value]))) {
			return;
		}
		FormUtils.post(document.forms[0], '<c:url value='/sm/TblSmDeptAction.do?step=deptEdit'/>');
	}
</c:if>
	CurrentPage.validation = function () {
		return Validator.Validate(document.forms[0],5);
	}
	<%--校验--%>
	CurrentPage.initValideInput = function () {
		document.getElementById('statusBean.changeTime').dataType = 'Require';
		document.getElementById('statusBean.changeTime').msg = msgInfo_.getMsgById('HR_I068_C_1',['合并时间']);
		document.getElementById('departmentnameNew').dataType = 'Require';
		document.getElementById('departmentnameNew').msg = msgInfo_.getMsgById('HR_I068_C_1',['合并到部门']);
	}
	<%--验证时间是否是未来日期--%>
	CurrentPage.CheckDay=function(){
		var today= new Date();
		var month=today.getMonth()+1;
		month=month<10? "0"+month:month;
		var day=today.getDate();
		day=day<10? "0"+day:day;
	   	<%--日期为 yyyy-mm-dd 格式--%>
	    var strToday= today.getFullYear()+"-"+month +"-"+ day;
	    //window.alert(strToday);
   		if($('statusBean.changeTime').value!=""){
	 		if (DateUtils.CompareDate (strToday,($('statusBean.changeTime').value))>0){
				Validator.warnMessage(msgInfo_.getMsgById('HR_I072_C_1',['合并时间']));
				document.getElementById('statusBean.changeTime').focus();
					return false;
			}
    	}
    }
	CurrentPage.initValideInput();
	
	function Validator.afterSuccessMessage(){
		if(parent.codeTree){
			parent.codeTree.Update(parent.codeTree.SelectId);
		}
	}
</script>
</form>
</body>
</html>
