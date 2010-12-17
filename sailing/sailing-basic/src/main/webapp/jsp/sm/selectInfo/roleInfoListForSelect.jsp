<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf" />
<html>
<base target  ="_self"/>
<body>

<form name="f">
<input type="hidden" name="parentCode" id="parentCode" value="<c:out value = "${theForm.parentCode}"/>"/>
<input type="hidden" name="oid"  id= "oid" value="<c:out value='${theForm.oid}'/>"/>
<input type="hidden" name="step" value="<c:out value = '${theForm.step}' />" />
	<div id="divId_requQues" class="list_scroll">
		<table border="0" cellpadding="0" cellspacing="0" class="Listing" id="listtable" onClick="">
			<thead>
				<tr>
					<td nowrap="nowrap">显示名称</td>
					<td nowrap="nowrap">权限类型</td>
					<td nowrap="nowrap">功能节点类型</td>
					<td nowrap="nowrap">数据编辑控制</td>
					<td nowrap="nowrap">列表数据显示范围</td>
					<td nowrap="nowrap">数据编辑范围</td>	
					<td nowrap="nowrap">权限路径</td>				
					<td nowrap="nowrap">说明</td>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="item" items="${theForm.list}" varStatus="status">
					<tr>
						<td>
							<c:out  value="${item.tblCmnPermission.displayname}"/>&nbsp;
						</td>
						<td >
							<c:out  value="${item.tblCmnPermission.nodetype}"/>&nbsp;
						</td>  	
						<td>
							<c:out  value="${item.tblCmnPermission.urltype}"/>&nbsp;
						</td>
						<td nowrap="nowrap" align='left' id= "" >
							<c:out  value="${item.rwCtrlName}"/>&nbsp;
						</td>	
						<td nowrap="nowrap" align='left' id= "">
							<c:out  value="${item.readAccessLevelName}"/>&nbsp;
						</td>
						<td nowrap="nowrap" align='left' id= "" >
							<c:out  value="${item.writeAccessLevelName}"/>&nbsp;
						</td>						
						<td>
							<c:out  value="${item.tblCmnPermission.permissionRoute}"/>&nbsp;
						</td>
						<td>
							<c:out  value="${item.tblCmnPermission.note}"/>&nbsp;
						</td>
					</tr>
				</c:forEach>					
			</tbody>
		</table>
	<div class="list_bottom">
		<c:set var = "paginater.forwardUrl" value = "/sm/RoleAction.do" scope = "page" />
		<jsp:directive.include file = "/decorators/paginater.jspf"/>
		<input type="button" class="opera_export" title="导出Excel" onclick="Print.exportExcel($('divId_scrollLing'))" value=""/>
	</div>
	</div>
</form>
</body>
</html>
