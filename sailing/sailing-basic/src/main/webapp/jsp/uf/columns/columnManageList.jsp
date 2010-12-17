<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf" />
<html>
		<script language="javascript">
		var msgInfo_ = new msgInfo();
		if (CurrentPage == null) {
    		var CurrentPage = {};
		}     

		CurrentPage.remove = function(oid) {
			if (!confirm(msgInfo_.getMsgById('UF_I012_A_0'))) {
				return false;
			} 
			FormUtils.post(document.forms[0], '<c:url value = "/uf/desk/ColumnManageAction.do?step=delete&oid="/>'+ oid );
		}
	
		CurrentPage.create = function () {
			var len = listtable.rows.length;
			$('step').value = 'info';
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
	<body>
		<form name="f" method="post">
		<input type="hidden" name="step" value='' />
		<div class="update_subhead">
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td>
					<span class="switch_open" onClick="StyleControl.switchDiv(this, divId_scrollLing)"  title="点击收缩表格">栏目列表</span>
				</td>
			</tr>
		</table>
		</div>

        <div id="divId_scrollLing" class="list_scroll">
			<table width="100%" border="0" cellpadding="0" cellspacing="0" class="Listing"
				id="listtable" onClick="TableSort.sortColumn(event)">
				<thead>
					<tr>
						<td nowrap="nowrap" width="100" align="center">栏目区分</td>
						<td nowrap="nowrap" align="center">栏目名称</td>
						<td nowrap="nowrap" width="70" align="center">需要反馈</td>
						<td nowrap="nowrap" width="60" align="center">操作</td>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="item" items="${theForm.listDesktop}" varStatus="status">
						<tr>
							<td nowrap="nowrap" align="center">
								<input type="hidden" name="oid" value="<c:out value="${item.id}"/>" />
								<c:out value="${item.columnFlg.name}"/>
							</td>
							<td nowrap="nowrap">
								<input type="hidden" name="hasSon[<c:out value="${status.index}"/>]" 
								<c:forEach var="itemOrder" items="${item.tblUfColumnOrders}" varStatus="statusOrder">
								    <c:if test="${statusOrder.last}">
										value="<c:out value="${statusOrder.count}">0</c:out>"
									</c:if>
								</c:forEach>
								/>
								<c:out value="${item.columnNm}"/>		
							</td>
							<td nowrap="nowrap" align="center"><c:out value="${item.feedbackFlg.name}"/></td>
							<td nowrap="nowrap" align="center">
                                <input type="button" class="list_delete" onclick="CurrentPage.remove('<c:out value="${item.id}"/>')" title="点击删除" <c:if test="${item.columnFlg!='UF_LMQF_01'}">style="display:none"</c:if> />
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<div class="list_bottom">
			<c:set var = "paginater.forwardUrl" value = "/uf/desk/ColumnManageAction.do?step=list" scope = "page" />
			<%@ include file = "/decorators/paginater.jspf" %>
			</div>
			</div>
		</form>
	</body>
</html>
