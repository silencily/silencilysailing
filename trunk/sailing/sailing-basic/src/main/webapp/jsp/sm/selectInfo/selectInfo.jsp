<%-- jsp1.2 --%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ include file="/decorators/default.jspf" %>
<%@ include file="/decorators/tablibs.jsp" %>
<script type="text/javascript">
var CurrentPage = {};
var checkType = "<c:out value = "${theForm.checkType}" />";
CurrentPage.query = function() {
	if (document.getElementsByName("paginater.page") != null) {
	    document.getElementsByName("paginater.page").value = 0;
	}
	FormUtils.post(document.forms[0], '<c:url value="/sm/SmSearchAction.do?step=selectInfo&checkType=" />' + checkType);
	}
	
</script>
<html>
<body class="list_body">
<form name="form">
<input type="hidden" name="step" value="<c:out value = "${theForm.step}" />" />	
<%@ include file="search.jspf" %>
<div class="update_subhead">
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td><span class="switch_open" onClick="StyleControl.switchDiv(this,$('allHidden'))" title="伸缩节点">人员选择</span></td>
		</tr>
	</table>
</div>
<div id="allHidden">
<div id="divId_scrollLing" class="list_scroll">
	<table border="0" cellpadding="0" cellspacing="0" class="Listing" id="empInfoTable" onClick="TableSort.sortColumn(event)">
		<thead>
		<tr>
		<td width="25px" field=id nowrap="nowrap" align='center'>
			</td>
			<td field=empCd>员工号</td>
			<td field=empName>姓名</td>
			<td field=sex>性别</td>
			<td style="display: none" field=sexCode>性别编号</td>
			<td field=nation>民族</td>
			<td style="display: none" field=nationCode>民族编号</td>
			<td field=birthday>出生日期</td>
			<td field=nativePlace>籍贯</td>
			
			
		</tr>
		</thead>
		<tbody>
		<c:forEach var="item" items="${theForm.infoList}" varStatus="status">
			<tr>
			<td align="center">
					<c:if test="${theForm.checkType == 'checkbox'}">
						<input type="checkbox" name="oid" onclick="FormUtils.check($('detailIdsForPrintAll'),document.getElementsByName('oid'))" value="<c:out value = "${item.id}"/>"/>
					</c:if>
					<c:if test="${theForm.checkType == 'radio'}">
						<input type="<c:out value = "${theForm.checkType}" />" name="oid"  value="<c:out value = "${item.id}" />" />
					</c:if>
				</td>
				<td><c:out value = "${item.empCd}" escapeXml='true'/>&nbsp;</td>
				<td><c:out value = "${item.empName}" escapeXml='true'/>&nbsp;</td>
				<td align="center"><c:out value = "${item.sex.name}" escapeXml='true'/>&nbsp;</td>
				<td style="display: none"><c:out value = "${item.sex.code}" escapeXml='true'/>&nbsp;</td>
				<td align="center"><c:out value = "${item.nation.name}" escapeXml='true'/>&nbsp;</td>
				<td style="display: none"><c:out value = "${item.nation.code}" escapeXml='true'/>&nbsp;</td>
			    <td><fmt:formatDate value = '${item.birthday}' pattern = 'yyyy-MM-dd' />&nbsp;</td>
				<td><c:out value = "${item.nativePlace}" escapeXml='true'/>&nbsp;</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	</div>
<div class="list_bottom"><c:set var="paginater.forwardUrl" value="/sm/SmSearchAction.do?step=selectInfo&checkType=${theForm.checkType}" scope="page" /> 
	<%@ include file="/decorators/paginater.jspf"%>
</div>
</div>

<script type="text/javascript">	
	if (CurrentPage == null) {
	    var CurrentPage = {};
	}    
	
	var listObject = new Object();
	//definedWin激活该方法
	CurrentPage.onLoadSelect = function(){
		//将列表对象化
		listObject = new ListUtil.Listing('listObject', 'empInfoTable');
		listObject.init();
		//重载definedWin中实现
		//选择
		top.definedWin.selectListing = function(inum) {
			<c:if test="${theForm.checkType == 'radio'}">listObject.selectWindow(1);</c:if>
			<c:if test="${theForm.checkType == 'checkbox'}">listObject.selectWindow(2);</c:if>
		}
	
		//关闭
		top.definedWin.closeListing = function(inum) {
			listObject.selectWindow();
		}
	}
	CurrentPage.onLoadSelect();

	CurrentPage.validation = function () {
		return Validator.Validate(document.forms[0],5);
	}
</script>
</form>
</body>
</html>
