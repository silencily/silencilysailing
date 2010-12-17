<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ include file = "/decorators/default.jspf" %>

<%--
	缓式加载显示的代码树
	如果指定代码id(对应代码表中code字段数据),则只显示这个代码下所有代码节点
	默认情况下，根节点不可选择
	treeType 单选=1，多选=2，不选=空
	SelectNodeId 默认选中的节点
--%>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<html>
<head>
<title>显示代码树</title>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<script type="text/javascript" src="<c:out value='${contextPath}'/>/public/scripts/codeTree.js"></script>

</head> 

<body style="overflow:hidden "> 
<form name="f"  method="post" >
<div class="main_title">选择代码</div>

<div  id="span_menu"></div>

<script type="text/javascript" >

var codeTree = new CodeTree.CodeTree('codeTree')
codeTree.imagePath    = '<c:out value="${contextPath}"/>/image/tree/column/';
<c:if test="${!empty param.treeType}">
	codeTree.TreeType   = <c:out value="${param.treeType}"/>;
</c:if>
var initId = '<c:out value="${param.SelectNodeId}"/>';
if(initId)
	codeTree.InitString = ","+initId+",";
codeTree.dispPlace = 'span_menu';
codeTree.hostUrl   = '<%=request.getContextPath()%>/common/ui/selector.do?step=getCodeTreeNode&id=';
codeTree.imagePath = '<%=request.getContextPath()%>/image/tree/'; 
<c:set var ="getDataID" value="0"/>
<c:if test="${!empty param.id}">
	<c:set var="getDataID" value="${param.id}"/>
</c:if>
codeTree.getDataID    = '<c:out value="${getDataID}"/>'   ;//init first node data 
codeTree.Load(0);

//--初始化[0]节点
function CodeTree.initTreeList0(arr){
	var obj = new this.Node(0,codeTree.getDataID,'代码树',0,true,'',0,0); 
	this.LastClickChild=0;
	return obj ;
}
/**
 *  根据节点编号sid,获取xmlhttp 返回字符串
 */
function CodeTree.getXmlHttpString(obj)
{
	var sid = obj.selfId;
	var str = XMLHttpEngine.getResponseText(this.hostUrl + sid, false);
	if(typeof str == 'undefined') {
		str = '';
	}		
	str = str.replace(/\n/gi,"");
	return str;
}

function selectTree() {
	var array = codeTree.returnList ;
	if (!array || array.length == 0) {
		alert('请选择一个代码！');
		return;
	}
	top.definedWin.returnOpenTreeValue(array);	
}

function emptyCode() {
	var emptyCode = new Object();
	emptyCode['selfId'] = '';
	emptyCode['nodeName'] = '';
	return emptyCode;
}

CurrentPage = {};

CurrentPage.onLoadSelect = function(){		
	//重载definedWin中实现
	//选择
	top.definedWin.selectListing = function(inum) {
		selectTree();		
	}
	/*
	top.definedWin.closeListing = function(){
		var array = new Array(emptyCode());
		top.definedWin.returnOpenTreeValue(array);
	}*/
}

</script>

</form>
</body>
</html>

