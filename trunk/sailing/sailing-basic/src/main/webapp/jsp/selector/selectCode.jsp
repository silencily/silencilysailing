<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ include file = "/decorators/default.jspf" %>

<%--
	显示指定代码树
	如果指定代码id,则只显示这个代码下所有代码节点
	默认情况下，根节点不可选择
	treeType 单选=1，多选=2，不选=空
	SelectNodeId 默认选中的节点
    SelfNodeId   不让选择的节点（其相应子节点也不可选）
--%>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<html>
<head>
<title>显示指定节点代码</title>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<script type="text/javascript" src="<c:out value='${contextPath}'/>/public/scripts/staticTree.js"></script>

</head> 

<body style="overflow:hidden "> 
<form name="f"  method="post" >
<div class="main_title">选择代码</div>

<div  style="overflow-x:hidden;overflow-y:auto;height:400px;width:100%" class="css_scroll" id="span_menu"></div>

<script type="text/javascript" >
//0代表层次 / 部门名称 / url / target /  部门ID / type / noSelect .....
// * nodeLevel,nodeName,nodeUrl,target,nodeId,nodeType,noSelect,promptInfo,helpUrl,fontCss,isLast,fathId,children
var menuArray=new Array();
var i = 0;

<c:forEach var="item" items="${theForm.codeList}" varStatus="status">	
		<c:set var ="noSelect" value=""/>
		<c:if test="${item['NODELEVEL'] == '0'}">
			<c:set var ="noSelect" value="${item['NODELEVEL']}"/>
		</c:if>
		menuArray[i++]="<c:out value="${item['NODELEVEL']}"/>,<c:out value="${item['NODENAME']}"/>,<c:out value="${item['NODEFATHID']}"/>,<c:out value="${item['ID']}"/>,<c:out value="${item['NODEID']}"/>,1,<c:out value="${noSelect}"/>";
</c:forEach>

var mytree = new StaticTree.DepartTree('mytree')
mytree.imagePath    = '<c:out value="${contextPath}"/>/image/tree/column/';
<c:if test="${!empty param.treeType}">
	mytree.TreeType   = <c:out value="${param.treeType}"/>;
</c:if>
var initId = '<c:out value="${param.SelectNodeId}"/>';
var selfId = '<c:out value="${param.SelfNodeId}"/>';
if(initId)
	mytree.InitString = ","+initId+",";
if(selfId)
	mytree.NotSelect  = selfId;
mytree.addArray(menuArray);
mytree.display()
//mytree.switchAll();


function selectTree(){
	var arr = mytree.returnList;
	if(!arr || arr.length==0){
		alert("请选择你需要的模块！");
		return
	}
	top.definedWin.returnOpenTreeValue(arr);
}
//置空
function emptyCode() {
	var temp = new Object();
	temp.nodeName = '';
	temp.nodeId   = '';
	temp.target   = '';
	return temp;
}

CurrentPage = {};

CurrentPage.onLoadSelect = function(){		
	//重载definedWin中实现
	//选择
	top.definedWin.selectListing = function(inum) {
		selectTree();		
	}
}
</script>

</form>
</body>
</html>

