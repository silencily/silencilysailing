<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ include file = "/decorators/default.jspf" %>

<%--
	��ʽ������ʾ�Ĵ�����
	���ָ������id(��Ӧ�������code�ֶ�����),��ֻ��ʾ������������д���ڵ�
	Ĭ������£����ڵ㲻��ѡ��
	treeType ��ѡ=1����ѡ=2����ѡ=��
	SelectNodeId Ĭ��ѡ�еĽڵ�
--%>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<html>
<head>
<title>��ʾ������</title>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<script type="text/javascript" src="<c:out value='${contextPath}'/>/public/scripts/codeTree.js"></script>

</head> 

<body style="overflow:hidden "> 
<form name="f"  method="post" >
<div class="main_title">ѡ�����</div>

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

//--��ʼ��[0]�ڵ�
function CodeTree.initTreeList0(arr){
	var obj = new this.Node(0,codeTree.getDataID,'������',0,true,'',0,0); 
	this.LastClickChild=0;
	return obj ;
}
/**
 *  ���ݽڵ���sid,��ȡxmlhttp �����ַ���
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
		alert('��ѡ��һ�����룡');
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
	//����definedWin��ʵ��
	//ѡ��
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

