<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ include file = "/decorators/default.jspf" %>

<%--
	显示所有部门结构树下用户
	如果指定部门id,则只显示这个部门下所有部门
	treeType 单选=1，多选=2，不选=空
	SelectNodeId 默认选中的节点
--%>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<body style="overflow:hidden "> 
<form name="f"  method="post" >
<div class="main_title">选择用户</div>

<div style="overflow-x:hidden;overflow-y:auto;height:400px;width:100%" class="css_scroll" id="span_menu"></div>

<div class="main_button">  
	 <input type="button" value="确 定" class="opera_normal" onClick="selectTree('select')" >	
	 <input type="button" value="清 空" class="opera_normal" onClick="selectTree('clear')" >		 	 
	 <input type="button" value="取 消" class="opera_normal" onClick="top.close()" > 
</div>

<script type="text/javascript" >
//0代表层次 / 部门名称 / url / target /  部门ID / type / noSelect .....
// * nodeLevel,nodeName,nodeUrl,target,nodeId,nodeType,noSelect,promptInfo,helpUrl,fontCss,isLast,fathId,children
var menuArray=new Array();
var i = 0;

<c:forEach var="item" items="${theForm.userList}" varStatus="status">	
		<c:set var ="imageType" value="2"/>		
		<c:set var ="noSelect" value=""/>
		<c:if test="${item['ISSELECT'] eq '0'}">
			<c:set var ="noSelect" value="true"/>
			<c:set var ="imageType" value="1"/>
		</c:if>
		menuArray[i++]="<c:out value="${item['ORG_LEVEL']-1}"/>,<c:out value="${item['NODENAME']}"/>,<c:out value="${item['NODEID']}"/>,<c:out value="${item['NODEFATHID']}"/>,<c:out value="${item['USERNAME']}"/>,<c:out value="${imageType}"/>,<c:out value="${noSelect}"/>";
</c:forEach>

var mytree = new StaticTree.DepartTree('mytree')
mytree.imagePath    = '<c:out value="${contextPath}"/>/image/tree/column/';
<c:if test="${!empty param.treeType}">
	mytree.TreeType   = <c:out value="${param.treeType}"/>;
</c:if>
var initId = '<c:out value="${param.SelectNodeId}"/>';
if(initId)
	mytree.InitString = ","+initId+",";
mytree.addArray(menuArray);
mytree.display()
//mytree.switchAll();


function selectTree(flag){
	var array = mytree.returnList;	
	if (!array || array.length == 0 || flag == 'clear') {
		array = new Array();
		array[0] = emptyUserObject();
		top.returnValue = array;
		top.close();
		return;
	}
	

	var temp;
	var tempArr = new Array();
	for(var i = 0;i < array.length; i++){
		temp = new userObject();
		
		if (flag == 'select') {
			temp.userId = array[i].nodeUrl;
			temp.userName = array[i].nodeId;//remind the attribute 
			temp.chinese  = array[i].nodeName;
			temp.orgId    = array[i].target;
		} else {
			temp = emptyUserObject();
		}
		tempArr[i]= temp;
	}
	top.returnValue = tempArr;
	top.close();
	return;
}

//以下是返回的用户对象属性
function userObject() {
	this.userId ="";//用户ID
	this.userName = "";//用户帐户名
	this.orgId  = "";//所属ID
	this.chinese = "";//用户中文名
}

function emptyUserObject() {
	var emptyUserObject = new userObject();
	emptyUserObject.userId = '';
	emptyUserObject.userName = '';
	emptyUserObject.chinese  = '';
	emptyUserObject.orgId    = '';
	return emptyUserObject;
}

</script>

</form>
</body>
</html>

