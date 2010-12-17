<%--
    @version:$Id: deptList.jsp,v 1.1 2010/12/10 10:56:43 silencily Exp $
    @since $Date: 2010/12/10 10:56:43 $
    @��֯���б�ҳ��
--%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf"/>
<jsp:directive.include file="/decorators/edit.jspf"/>
<html>
<body class="list_body">
<script type="text/javascript">
	if (CurrentPage == null) {
		var CurrentPage = {};
	}
	//---list create----
	CurrentPage.create = function () {
	$('oid').value = '';
	//TableSort.setNoSelect();
	//TableSort.dblClick();
	FormUtils.post(document.forms[0], "<c:url value='/sm/TblSmDeptAction.do?step=edit'/>");
	}
	
	CurrentPage.selectRadio = function(selectRadio) {
		var radio=selectRadio;
		FormUtils.post(document.forms[0], "<c:url value='/sm/TblSmDeptAction.do?step=" + radio +"&mainTableClassName=TblCmnDept'/>");
	}
	
	function Validator.afterSuccessMessage(){
		if(parent.codeTree){
			parent.codeTree.Update(parent.codeTree.SelectId);
		}
	}

</script>

<form name="f" action="" method="post">
<input type="hidden" name="parentCode" value="<c:out value = "${theForm.parentCode}"/>"/>
<input type="hidden" name="oid" value="<c:out value='${theForm.bean.id}'/>"/>
<input type="hidden" name="step" value="<c:out value='${theForm.step}'/>"/>
<c:set var="parent" value="${theForm.bean.parent}"/>
	<div class="update_subhead">
		<span class="switch_open" onclick="StyleControl.switchDiv(this,$('stateTable'))" title="�����ڵ�">����״̬</span>
	</div>
	<table border="0" cellpadding="0" cellspacing="0" class="Detail" id="stateTable">
		<div>
			<input type="radio" name="radio" value="0" onClick="CurrentPage.selectRadio('selectAllRadio');" <c:if test="${theForm.step== 'selectAllRadio'}">checked</c:if> >���� 
			<input type="radio" name="radio" value="1" onClick="CurrentPage.selectRadio('list');" <c:if test="${theForm.step== 'list'}">checked</c:if>>����ʹ��
			<input type="radio" name="radio" value="2" onClick="CurrentPage.selectRadio('selectRadio');" <c:if test="${theForm.step=='selectRadio'}">checked</c:if>>ֹͣʹ��
		</div>
	</table>
<div class="update_subhead">
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td>
				<span class="switch_open" onClick="StyleControl.switchDiv(this, $('allHidden'))" title="�����ڵ�">�����б�</span>
			</td>
		</tr>
	</table>
</div>
<div id="allHidden">
<div id="divId_scrollLing" class="list_scroll">
	<table border="0" cellpadding="0" cellspacing="0" class="Listing" id="listtable"  onClick="TableSort.sortColumn(event)">
		<thead>
			<tr>
				<td type='Number'>���</td>
				<td>���ű���</td>
				<td>��������</td>
				<td>��������</td>
				<td>����״̬</td>				
				<td>���Ÿ�����</td>
				<td type='Number'>��ʾ˳��</td>
			</tr>
		</thead>
		<tbody>		
		<c:forEach var="item" items="${theForm.list}" varStatus="status">
			<tr>
				<td align="right">
				    <input type="hidden" name="oid" value="<c:out value="${item.id}"/>"/>
				    <c:out value="${status.count}"/>&nbsp;
				</td>
				<td><c:out value="${item.deptCd}" escapeXml='true'/>&nbsp;</td>
				<td><c:out value="${item.deptName}" escapeXml='true'/>&nbsp;</td>
				<td><c:out value="${item.deptProperty.name}" escapeXml='true'/>&nbsp;</td>
				<td align="left">
					<c:choose>
						<c:when test="${item.deptState == '0'}"><font color="Red" align="center"><c:out value="����ʹ��"/></font></c:when>
						<c:otherwise>
							<c:choose>
								<c:when test="${item.deptState == '1'}"><font align="center"><c:out value="ֹͣʹ��"/></font></c:when>
								<c:otherwise>
									<font color="Blue" align="center"><c:out value="����"/></font>
								</c:otherwise>
							</c:choose>
						</c:otherwise>
					</c:choose>
				</td>
				<td><c:out value="${item.deptCharge}" escapeXml='true'/>&nbsp;</td>
				<td align="right"><c:out value="${item.showSequence}"/>&nbsp;</td>
			</tr>										
		</c:forEach>						
		</tbody>
	</table>
</div>
	<div class="list_bottom">
		<c:set var = "paginater.forwardUrl" value = "/sm/TblSmDeptAction.do?step=list&mainTableClassName=TblCmnDept" scope = "page" />
		<jsp:directive.include file = "/decorators/paginater.jspf"/>
		<input type="button" class="opera_export" title="����Excel�ļ�" onclick="Print.exportExcel($('divId_scrollLing'))" value=""/>
	</div>
</div>
</form>
</body>
</html>
