<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ include file = "/decorators/default.jspf" %>

<%--
	��ʾָ��������
	���ָ������id,��ֻ��ʾ������������д���ڵ�
	Ĭ������£����ڵ㲻��ѡ��
	treeType ��ѡ=1����ѡ=2����ѡ=��
	SelectNodeId Ĭ��ѡ�еĽڵ�
    SelfNodeId   ����ѡ��Ľڵ㣨����Ӧ�ӽڵ�Ҳ����ѡ��
--%>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<html>
<head>
<title>��ʾָ���ڵ����</title>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<script type="text/javascript" src="<c:out value='${contextPath}'/>/public/scripts/staticTree.js"></script>

</head> 

<body style="overflow:hidden "> 
<form name="f"  method="post" >
<div class="main_title">ѡ�����</div>

<div  style="overflow-x:hidden;overflow-y:auto;height:400px;width:100%" class="css_scroll" id="span_menu"></div>

<script type="text/javascript" >
//0������ / �������� / url / target /  ����ID / type / noSelect .....
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
		alert("��ѡ������Ҫ��ģ�飡");
		return
	}
	top.definedWin.returnOpenTreeValue(arr);
}
//�ÿ�
function emptyCode() {
	var temp = new Object();
	temp.nodeName = '';
	temp.nodeId   = '';
	temp.target   = '';
	return temp;
}

CurrentPage = {};

CurrentPage.onLoadSelect = function(){		
	//����definedWin��ʵ��
	//ѡ��
	top.definedWin.selectListing = function(inum) {
		selectTree();		
	}
}
</script>

</form>
</body>
</html>

