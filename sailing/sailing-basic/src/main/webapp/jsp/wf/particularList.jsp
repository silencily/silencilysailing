<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ include file="/decorators/default.jspf" %>



<%--
    尝试使用 JSP1.2 specification, 按照 Sun J2EE 5 说明, 并不保证在其他的 server 下被识别
    所以如果发生异常, 搜索 "jsp1.2"字样, 使用 jsp1.1 代替
--%>

<%-- jsp1.2 --%>

<html>
<body class="list_body">
<form id="list_form1">
<%-- 
<jsp:directive.include file="/jsp/wf/particularSearch.jspf" />
--%>
<input type="hidden" name="step" value="<c:out value = '${theForm.step}' />" />
<div class="update_subhead">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td>
		    <%--StyleControl.switchDiv(this, $('allHidden')) 展开或收起列表区域--%>
		    <span class="switch_open" onClick="StyleControl.switchDiv(this, $('allHidden'))" title="伸缩节点">步骤信息列表</span>
		</td>
	</tr>
</table>
</div>
<div id="allHidden">
<div id="divId_scrollLing" class="list_scroll">
		<table border="0" cellpadding="0" cellspacing="0" class="Listing" id="listtable" onClick="TableSort.sortColumn(event)">
			<input type="hidden" name="checkall" onClick="CurrentPage.selectedAll()"/>
			<%-- 自定义标签，用于显示查询结果,在Action中需要设置name为"viewBean"的atrribute，参考ClassroomMagAction的list方法--%>
			<table:table name="partviewBean" />
		</table>
	</div>
<div class="list_bottom">
    
	<c:set var = "paginater.forwardUrl" value = "/wf/particularlist.do?step=list&mainTableClassName=TblWfParticularInfo" scope = "page" />
	<%@ include file = "/decorators/paginater.jspf" %>
	
</div>
</div>
</form>
</body>
</html>
<%-- 
<script type="text/javascript">
var msgInfo_ = new msgInfo();
if (CurrentPage == null) {
   		var CurrentPage = {};
	}
// 	remove()方法进行一条记录的删除；
CurrentPage.remove = function(oid) {
	if (! confirm(msgInfo_.getMsgById('HR_I061_A_0'))) {
		return;
	}
	FormUtils.post(document.forms[0], '<c:url value = "/scm/ClassroomMagAction.do?step=remove&mainTableClassName=TblScmClassroomInfo&oid=" />' + oid );
}
 
   // create()方法新添加一条记录；
  CurrentPage.create = function () {
	var len = listtable.rows.length;
	if(len<2){
		TableSort.setNoSelect();
		TableSort.dblClick();
	}else{
		$('oid').value = '';
		TableSort.setNoSelect();
		TableSort.dblClick();
	}
}
</script>
--%>
<%-- 
<%@ page contentType="text/html; charset=GBK"%>
<jsp:directive.include file="/decorators/default.jspf" />
<jsp:directive.include file="/decorators/tablibs.jsp" />
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<script language=javascript>
var msg= new msgInfo();
//添加
function paradd(){
	document.forms[0].action='<%=request.getContextPath()%>/wf/particularlist.do?step=edit';
}
//返回
function retums(){
	document.forms[0].action='<%=request.getContextPath()%>/wf/operationlist.do?step=list';
	document.forms[0].submit();
}
function createXml(){
	document.forms[0].action='<%=request.getContextPath()%>/wf/particularlist.do?step=createXML';
	document.forms[0].submit();
}
//删除
function isSelectedSubject(){
	var len = document.forms[0].id.length;
	if(len!=null){
		for(var i=0;i<len;i++){
			if(document.forms[0].id[i].checked==true)
				return true;
		}
	}else{
			if(document.forms[0].id.checked==true)
				return true;
	}
	return false; 
}
function deletes(path){
	Validator.clearValidateInfo();
	if(isSelectedSubject()==false){
		Validator.warnMessage(msg.getMsgById('WF_I007_p_0'));
		return ;
	}
	if(!window.confirm("\u60a8\u786e\u8ba4\u5bf9\u5df2\u9009\u8bb0\u5f55\u6267\u884c\u8be5\u9879\u64cd\u4f5c\uff1f")){
		return ;
	}
	document.forms[0].action=path+"/wf/particularlist.do?step=delete";
	document.forms[0].submit();
}
function gl(obj,_type){

  if(_type==1)
   obj.style.backgroundColor="#F5FAFE";
  else if(_type==2)
   obj.style.backgroundColor="";

}
</script>

<html>
<body class="list_body">
<form name="f" method="post">
<div class="list_explorer">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td><span class="switch_open"
			onClick="StyleControl.switchDiv(this, $('supplyerInfoTable'))"
			title="点击收缩表格"> 步骤信息列表 </span></td>
	</tr>
</table>
</div>
<div id="divId_scrollLing" class="list_scroll">
<table border="0" cellpadding="0" cellspacing="0" class="Listing"
	id="supplyerInfoTable" onClick="TableSort.sortColumn(event)">
	<thead>
		<tr>
			<td></td>
			<td>步骤</td>
			<td>步骤名称</td>
			<td>处理事项</td>
			<td>退回</td>
			<td>退回步骤</td>
			<td>取消</td>
			<td>取回</td>
			<td>挂起</td>
			<td>传阅</td>
			<td>提交</td>
			<td>提交步骤</td>
			<td>会签</td>
			<td>审批方式</td>
			<td>消息</td>
			<td>权限要求</td>
			<td>特殊对象</td>
			<td>操作</td>
		</tr>
	</thead>
	<input type="hidden" name="operNames"
					value="<c:out value='${operNameSelect}'/>" />
	<input type="hidden" name="names"
					value="<c:out value='${nameSelect}'/>" />
	<input type="hidden" name="opeditions"
					value="<c:out value='${opeditionSelect}'/>" />
	<input type="hidden" name="ooid"
					value="<c:out value='${ooid}'/>" />
	<tbody>
		<c:forEach var="row" items="${lists}">
			<tr onClick="gl(this,1);" ondblclick="gl(this,2);">
				<td><input type="checkbox"
					name="id" value='<c:out value="${row.id}"/>' />&nbsp;</td>
				<td><c:set value="${row.stepId}" var="stepId"></c:set>
					<c:choose>
						<c:when test="${row.stepId=='99'}">
							END&nbsp;
						</c:when>
						<c:otherwise>
							<c:out value="${row.stepId}"/>&nbsp;
						</c:otherwise>
					</c:choose>
				</td>
				<td><c:out value="${row.stepName}" />&nbsp;</td>
				<td><c:out value="${row.dealInfo}" />&nbsp;</td>
				<td align="center"><c:out value="${row.goback}" />&nbsp;</td>
				<td align="center"><c:out value="${row.gobackStep}" />&nbsp;</td>
				<td align="center"><c:out value="${row.cancel}" />&nbsp;</td>
				<td align="center"><c:out value="${row.fetch}" />&nbsp;</td>
				<td align="center"><c:out value="${row.suspend}" />&nbsp;</td>
				<td align="center"><c:out value="${row.passround}" />&nbsp;</td>
				<td align="center"><c:out value="${row.commit}" />&nbsp;</td>
				<td align="center"><c:out value="${row.commitStep}" />&nbsp;</td>
				<td align="center"><c:out value="${row.togethersign}" />&nbsp;</td>
				<c:set value="${row.helpman}" var="helpman" />
				<c:choose>
					<c:when test="${row.helpman=='point.emp'}">
						<td align="center">指定人&nbsp;</td>
					</c:when>
					<c:when test="${row.helpman=='point.role'}">
						<td align="center">指定角色&nbsp;</td>
					</c:when>
					<c:when test="${row.helpman=='point.business'}">
						<td align="center">业务指定&nbsp;</td>
					</c:when>
					<c:otherwise>
						<td align="center">&nbsp;</td>
					</c:otherwise>
				</c:choose>
				<td align="center"><c:out value="${row.message}" />&nbsp;</td>
				<td><c:out value="${row.tblUserId}" />&nbsp;</td>
				<td><c:out value="${row.pointStep}" />&nbsp;</td>
				<td><a
					href="<%=request.getContextPath()%>/wf/particularlist.do?step=edit&idd=<c:out value="${row.id}"/>&ooid=<c:out value="${ooid}"/>&operNames=<c:out value='${operNameSelect}'/>&names=<c:out value='${nameSelect}'/>&opeditions=<c:out value='${opeditionSelect}'/>">
				编辑详细</a>&nbsp;</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<table border="0" cellpadding="0" cellspacing="0" class="Listing">
	<tr>
		<td><input type="submit" name="Submit1" value="添加"
			onclick="paradd()">
		<input type="button" name="Submit2" value="删除"
			onclick="deletes('<%=request.getContextPath()%>')">
		<input type="button" name="Submit3" value="返回"
			onclick="retums()">
		<input type="button" name="Submit44" value="生成XML"
			onclick="createXml()"></td>
	</tr>
</table>
</form>
</body>
</html>
--%>
