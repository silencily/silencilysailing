<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ include file = "/decorators/default.jspf" %>

<%--
	��ʾ���в��Žṹ��
	���ָ������id,��ֻ��ʾ������������в���
	Ĭ�ϸ��ڵ㲻��ѡ��
	treeType ��ѡ=1����ѡ=2����ѡ=��
	SelectNodeId Ĭ��ѡ�еĽڵ�
    SelfNodeId   ����ѡ��Ľڵ㣨����Ӧ�ӽڵ�Ҳ����ѡ��
--%>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<body style="overflow:hidden "> 
<form name="f"  method="post" >
<div class="main_title">ѡ����</div>

<div id="span_menu"></div>

<script type="text/javascript" >
//0������ / �������� / url / target /  ����ID / type / noSelect .....
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
		alert('��ѡ��һ�����ţ�');
		return;
	}
	top.definedWin.returnOpenTreeValue(array);
	return;
}
//�ÿ�
function emptyDepartment() {
	var emptyDepatment = new Object();
	emptyDepatment.nodeName = '';
	emptyDepatment.nodeId   = '';
	emptyDepatment.target   = '';
	return emptyDepatment;
}

CurrentPage = {};

CurrentPage.onLoadSelect = function(){		
	//����definedWin��ʵ��
	//ѡ��
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

