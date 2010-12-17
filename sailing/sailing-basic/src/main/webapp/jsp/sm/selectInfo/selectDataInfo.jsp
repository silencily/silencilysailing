<%-- jsp1.2 --%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf"/>
<jsp:directive.include file="/decorators/tablibs.jsp" />
<script type="text/javascript">
var CurrentPage = {};
var checkType = "<c:out value = "${theForm.checkType}" />";
CurrentPage.query = function() {
	if (document.getElementsByName("paginater.page") != null) {
	    document.getElementsByName("paginater.page").value = 0;
	}
	FormUtils.post(document.forms[0], '<c:url value="/sm/SmSearchAction.do?step=selectDataInfo&checkType=" />' + checkType);
	}
	
</script>
<html>
<body class="list_body">
<form name="form">
<input type="hidden" name="step" value="<c:out value = "${theForm.step}" />" />	
<jsp:directive.include file="search.jspf"/>
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
			<td field=searchScope>查询范围</td>
			<td field=updateScope>修改范围</td>
			<td field=createScope>新增范围</td>
			<td field=deleteScope>删除范围</td>
            <td field=deleteScope>实体成员</td>
			
			
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
				<td><c:out value = "${item.searchScope}" escapeXml='true'/>&nbsp;</td>
				<td><c:out value = "${item.updateScope}" escapeXml='true'/>&nbsp;</td>
				<td><c:out value = "${item.createScope}" escapeXml='true'/>&nbsp;</td>
				<td><c:out value = "${item.deleteScope}" escapeXml='true'/>&nbsp;</td>
				<td><c:out value = "${item.tblCmnEntityMember.name}" escapeXml='true'/>&nbsp;</td>
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
top.definedWin.selectListing = function(){
	var arr = new Array();
	var dd=document.getElementsByName('oid');
	for(var i=0;i<dd.length;i++){
	   if(dd[i].checked==true){
	  			arr[i]=dd[i].value;
	  		}
	}
	if (arr.length==0){
		alert(msgInfo_.getMsgById('SM_I008_A_0'));
	}else{	
		top.definedWin.listObject(arr);
	}
}

	CurrentPage.validation = function () {
		return Validator.Validate(document.forms[0],5);
	}
</script>
</form>
</body>
</html>
