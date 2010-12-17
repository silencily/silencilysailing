<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf" />
<html>
<body>

<form name="f">
<input type="hidden" name="oid"  id="oid" value="<c:out value='${theForm.oid}'/>"/>
<input type="hidden" name="parentCode"  id="parentCode" value="<c:out value='${theForm.parentCode}'/>"/>
<input type="hidden" name="menuDataFlg"  id="menuDataFlg" value="<c:out value='${theForm.menuDataFlg}'/>"/>
<input type="hidden" name="strCreate"  id="hcreat" value="<c:out value='${theForm.strCreate}'/>"/>
<c:choose>
	<c:when test="${theForm.menuDataFlg =='0'}">
		<table border="0" cellpadding="0" cellspacing="0" class="Detail" id="parenttable" style="display: ">
			<tr>
				<div class="list_group">
					<div class="list_title" id="list_title">目录节点信息 <span class="list_notes"></span> </div>
					<input type="hidden" name="bean.nodetype"  id="oid" value="0"/>
				</div>	
			</tr>
			<tr>
				<td  class="attribute" >显示名称</td>
				<td ><c:out value='${theForm.bean.displayname}'/></td>
				<td  class="attribute" >顺序号</td>
				<td ><c:out value='${theForm.bean.displayOrder}'/></td>
			</tr>
			<tr>
				<td  class="attribute" >父节点名称</td>
				<td>	
					<c:out value='${theForm.bean.father.displayname}'/>				
				</td>
				<td  class="attribute" >说明</td>
				<td><c:out value='${theForm.bean.note}'/></td>
			</tr>
		</table>
	</c:when>
	<c:when test="${theForm.menuDataFlg =='1'}">
	<table border="0" cellpadding="0" cellspacing="0" class="Detail" id="parenttable" style="display: ">
		<tr>
			<div class="list_group">
				<div class="list_title" id="list_title">功能权限信息 <span class="list_notes"></span> </div>
				<input type="hidden" name="bean.nodetype"  id="oid" value="1"/>
			</div>	
		</tr>
			<tr>
				<td  class="attribute" >显示名称</td>
				<td ><c:out value='${theForm.bean.displayname}'/></td>
				<td  class="attribute" >顺序号</td>
				<td ><c:out value='${theForm.bean.displayOrder}'/></td>
			</tr>
			<tr>
				<td  class="attribute" >父节点名称</td>
				<td>	
					<c:out value='${theForm.bean.father.displayname}'/>				
				</td>
				<td  class="attribute" >说明</td>
				<td><c:out value='${theForm.bean.note}'/></td>
			</tr>
			<tr>
			<td  class="attribute" >功能权限类型</td>
	        <td><c:out value='${theForm.bean.urltypeCh}'/></td>
		
		</tr>
	</table>
</c:when>
<c:otherwise>
	<table border="0" cellpadding="0" cellspacing="0" class="Detail" id="parenttable" style="display: ">
		<tr>
			<div class="list_group">
				<div class="list_title" id="list_title">数据项权限信息 <span class="list_notes"></span> </div>
				<input type="hidden" name="bean.nodetype"  id="oid" value="2"/>
			</div>	
		</tr>
			<tr>
				<td  class="attribute" >显示名称</td>
				<td ><c:out value='${theForm.bean.displayname}'/></td>
				<td  class="attribute" >顺序号</td>
				<td ><c:out value='${theForm.bean.displayOrder}'/></td>
			</tr>
			<tr>
				<td  class="attribute" >父节点名称</td>
				<td>	
					<c:out value='${theForm.bean.father.displayname}'/>				
				</td>
				<td  class="attribute" >功能权限类型</td>
				<td><c:out value='${theForm.bean.urltypeCh}'/></td>
			</tr>
			<tr>
			<td  class="attribute" >说明</td>
	        <td><c:out value='${theForm.bean.note}'/></td>
		</tr>
	</table>
</c:otherwise>
</c:choose>
</form>
</body>
</html>
