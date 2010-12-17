<!-- 
<jsp:directive.page contentType="text/html; charset=GBK"/>
-->
<jsp:directive.include file="/decorators/default.jspf"/>
<html>
<head>
<style type="text/css">
<!--
body {
	background-color: #E4EDFA;
}
-->
</style>

<meta http-equiv="Content-Type" content="text/html; charset=GBK"/>

</head>
 <body>
<div id="space"></div>
<div id="span_menu"  class="tree_new"></div>
 <script language="JavaScript">

var mytree = new StaticTree.DepartTree('mytree','<%= request.getContextPath() %>/img/tree/column/');
mytree.imagePath  = '<%= request.getContextPath() %>/img/tree/';

mytree.add("0,操作功能,#,,,0");
<c:forEach items="${theForm.treeList}" var="treeList_" varStatus="status">
 mytree.add("<c:out value='${treeList_}' escapeXml="true" />");
</c:forEach>

mytree.display();

top.topTree = mytree;

//点击事件
function clickDepartChild(inum,ischeck)
{
	var panelObject = top.document.frames["under"].document.frames["mainFrame"].panel;
	//如没有panel对象，则不进行画面迁移提示
	var panelObject = panelObject?panelObject:false;
	if(ischeck && panelObject){
		if(panelObject.checkallvalue && !panelObject.checkValues()){
			return false;
		}
	}
	if(this.DeptList[inum].noSelect)
		return ;
	if(!this.getSelect(inum))
		return;
	if(this.TreeType == 2)
	{
		if(this.getObject('box',inum).checked )
		   this.getObject('box',inum).checked = false;			
		else
		   this.getObject('box',inum).checked = true;
	}
	/**-- 为了动态显示当前位置增加的处理脚本 Start --*/
	var userAgent = navigator.userAgent.toLowerCase();
	var is_webtv = userAgent.indexOf('webtv') != -1;
	var is_kon = userAgent.indexOf('konqueror') != -1;
	var is_mac = userAgent.indexOf('mac') != -1;
	var is_saf = userAgent.indexOf('applewebkit') != -1 || navigator.vendor == 'Apple Computer, Inc.';
	var is_opera = userAgent.indexOf('opera') != -1 && opera.version();
	var is_moz = (navigator.product == 'Gecko' && !is_saf) && userAgent.substr(userAgent.indexOf('firefox') + 8, 3);
	var is_ns = userAgent.indexOf('compatible') == -1 && userAgent.indexOf('mozilla') != -1 && !is_opera && !is_webtv && !is_saf;
	var is_ie = (userAgent.indexOf('msie') != -1 && !is_opera && !is_saf && !is_webtv) && userAgent.substr(userAgent.indexOf('msie') + 5, 3);
	var iselect = inum;
	var lvlName = "";
	for (;;){
		var node = this.DeptList[iselect];
		if (typeof node != 'object'){
			break;
		}
		if (lvlName == ""){
			lvlName = node.nodeName;
		}else{
			lvlName = node.nodeName + "\\" + lvlName;
		}
		iselect = node.fathId;
	}
	//	window.parent.topFrame.document.getElementById("location").innerHTML = lvlName;	
	top.document.getElementById("location").innerHTML = lvlName;
	/**-- 为了动态显示当前位置增加的处理脚本 End --*/
	this.selectBox(inum);
}
 </script>
 </body>
</html>