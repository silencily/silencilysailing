<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ include file = "/decorators/default.jspf" %>
<script type="text/javascript" src="<c:out value="${initParam['publicResourceServer']}"/>/public/scripts/codeTree.js"></script>

<body>
<table width="100%" border=0 cellpadding="0" cellspacing="0" height="100%">
	<tr valign="top">
		<td>
			<%-- 显示数据树 --%>
			<div id="span_menu" class="tree"></div>
		</td>
		<td class="tree_drag_width" onmousedown="setCapture()" onmouseup="releaseCapture();" onmousemove="StyleControl.dragWidth(this, 0)">&nbsp;</td>
		<td height="100%">
			<iframe name="listFrame" id="listFrame" frameborder="0" width="100%" height="100%" src="about:blank"></iframe>
		</td>
	</tr>
</table>

<script type="text/javascript" >
	var codeTree = new CodeTree.CodeTree('codeTree', '<c:out value="${initParam['publicResourceServer']}"/>/image/tree/column/');
	codeTree.dispPlace = 'span_menu';
	codeTree.hostUrl   = '<c:url value="/common/ui/organization.do?step=loadTree&oid="/>';
	codeTree.imagePath = '<c:out value="${initParam['publicResourceServer']}"/>/image/tree/'; 	
	codeTree.getDataID = '<c:out value = "${theForm.oid}" default = "0000" />'   ;//init first node data 
	codeTree.Load(0);
	
	//--初始化[0]节点
	/** overide codeTree.js */
	function CodeTree.initTreeList0(arr) {
		var obj = new this.Node(0, codeTree.getDataID, '<c:out value = "${theForm.organization.name}" />', 0, '', '', '', '', ''); 
		return obj ;
	}
	
	/* overide codeTree.js */
	function CodeTree.getPageUrl(cid) {
		var selectedId = this.TreeList[cid].selfId;
		var imageType = this.TreeList[cid].imgType;
		//多个角色过滤条件
		var roleCodes = '<c:out value='${param.roleCodes}'/>';
		var str = "";
		if(roleCodes){
			var roleArr = roleCodes.split(",");		
			for(var i=0;i<roleArr.length;i++){
				str += "&roles=" + roleArr[i] ;		
			}
		}
		var url = '<c:url value = "/common/ui/user.do?step=list&topOrganizationId=" />' + selectedId + '&paginater.page=0';
		url += '&treeType=<c:out value='${param.treeType}'/>';
		url += str;
		listFrame.location = url;
	}
	
	codeTree.getUrl(0);
</script>

</body>
</html>