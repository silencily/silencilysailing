<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ include file = "/decorators/default.jspf" %>

<%--
	显示所有部门结构树
	如果指定部门id,则只显示这个部门下所有部门
	默认根节点不可选择
	treeType 单选=1，多选=2，不选=空
	SelectNodeId 默认选中的节点
    SelfNodeId   不让选择的节点（其相应子节点也不可选）
--%>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<body style="overflow:hidden "> 
<form name="f"  method="post" >
<div class="main_title">选择部门</div>

<div id="span_menu"></div>

<script type="text/javascript" >
//0代表层次 / 部门名称 / url / target /  部门ID / type / noSelect .....
// * nodeLevel,nodeName,nodeUrl,target,nodeId,nodeType,noSelect,promptInfo,helpUrl,fontCss,isLast,fathId,children
var menuArray=new Array();
var i = 0;

<c:forEach var="item" items="${theForm.deptList}" varStatus="status">	
		<c:set var ="noSelect" value=""/>
		<c:if test="${! item['ISSELECT'] eq '1' || item['NODEID'] == '0'}">
			<c:set var ="noSelect" value="${item['ISSELECT']}"/>
		</c:if>
		menuArray[i++]="<c:out value="${item['ORG_LEVEL']-1}"/>,<c:out value="${item['NODENAME']}"/>,#,<c:out value="${item['NODEFATHID']}"/>,<c:out value="${item['NODEID']}"/>,<c:out value="${item['ORG_LEVEL']-1}"/>,<c:out value="${noSelect}"/>";
</c:forEach>

var mytree = new StaticTree.DepartTree('mytree','<c:out value="${contextPath}"/>/image/tree/column/');
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
	var array = mytree.returnList;
	if (!array || array.length == 0) {
		alert('请选择一个部门！');
		return;
	}
	top.definedWin.returnOpenTreeValue(array);
	return;
}
//置空
function emptyDepartment() {
	var emptyDepatment = new Object();
	emptyDepatment.nodeName = '';
	emptyDepatment.nodeId   = '';
	emptyDepatment.target   = '';
	return emptyDepatment;
}

CurrentPage = {};

CurrentPage.onLoadSelect = function(){		
	//重载definedWin中实现
	//选择
	top.definedWin.selectListingExtend[top.definedWin.modalNum] = function(){
		selectTree();		
	}
	/*
	top.definedWin.closeListing = function(){
		var array = new Array(emptyDepartment());
		top.definedWin.returnOpenTreeValue(array);
	}*/
}

</script>

</form>
</body>
</html>

