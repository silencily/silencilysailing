<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ include file = "/decorators/default.jspf" %>
<script type="text/javascript" src="<c:out value="${initParam['publicResourceServer']}"/>/public/scripts/codeTree.js"></script>

<body>
<div class="main_title"> <div>代码管理</div></div>

<table width="100%" border=0 cellpadding="0" cellspacing="0" height="100%">
	<tr valign="top">
		<td width="20%">
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
	codeTree.hostUrl   = '<c:url value="/common/systemcode.do?step=loadTree&systemModuleName=${theForm.systemModuleName}&oid="/>';
	codeTree.imagePath = '<c:out value="${initParam['publicResourceServer']}"/>/image/tree/'; 	
	codeTree.getDataID = '<c:out value = "${theForm.oid}" default = "0" />'   ;//init first node data 
	codeTree.Load(0);
	
	//--初始化[0]节点
	/** overide codeTree.js */
	function CodeTree.initTreeList0(arr) {
		var obj = new this.Node(0, codeTree.getDataID, '<c:out value = "${theForm.systemCode.name}" />', 0, '', '', '', '', ''); 
		return obj ;
	}
	
	/* overide codeTree.js */
	function CodeTree.getPageUrl(cid) {
		var selectedId = this.TreeList[cid].selfId;
		var imageType = this.TreeList[cid].imgType;
		
		listFrame.location = '<c:url value = '/common/systemcode.do?step=${theForm.entryStep}&systemModuleName=${theForm.systemModuleName}&parentCode=' />' + selectedId + '&paginater.page=0';;
	}

	function Global.afterOnload(){
		codeTree.getUrl(0);
	}
</script>

</body>
</html>