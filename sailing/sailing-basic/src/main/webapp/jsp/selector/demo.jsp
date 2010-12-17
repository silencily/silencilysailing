<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ include file = "/decorators/default.jspf" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<html>
<head>
<title>显示所有部门树</title>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">

</head> 

<body style="overflow:hidden "> 

<h3>树的演示</h3>

<h4>选择部门</h4>
部门名称：<input type="text" name="deptName"/><input type="button" class="select_fromtree" onclick="openDept()"><br/>
部门ID  : <input type="text" name="deptId"/><br/>

<h4>选择用户</h4>
用户名称：<input type="text" name="userName"/><input type="button" class="select_fromtree" onclick="openUser()"><br/>
用户ID  : <input type="text" name="userId"/><br/>
所属部门: <input type="text" name="orgId"/><br/>

<h4>选择代码</h4>
代码名称：<input type="text" name="codeName"/><input type="button" class="select_fromtree" onclick="openCode()"> <input type="button" class="select_fromtree" onclick="openCodeTree()"><br/>
代码ID  : <input type="text" name="codeId"/><br/>
代码CODE: <input type="text" name="codeCode"/><br/>

<script>
function openDept(){
	var deptName = document.getElementsByName("deptName")[0];
	var deptId   = document.getElementsByName("deptId")[0];
	var url = "<c:out value='${contextPath}'/>/common/ui/selector.do?step=selectDept&treeType=1";
	if(deptId.value){
		url += "&SelectNodeId="+deptId.value;
	}
	var obj = StyleControl.openDialogInFrame(url,600,500);
	if(obj){
		deptName.value = obj[0].nodeName;
		deptId.value = obj[0].nodeId;
	}
}

function openUser(){
	var userId = document.getElementsByName("userId")[0];
	var url = "<c:out value='${contextPath}'/>/common/ui/selector.do?step=selectUser&treeType=2";//多选
	if(userId.value){
		url += "&SelectNodeId="+userId.value;
	}
	var obj = StyleControl.openDialogInFrame(url,600,500);
	if(obj){
		var uid="",uname="",oid="";
		for(var i=0;i<obj.length;i++){
			uid += obj[i].userId + ",";
			uname += obj[i].userName+ ",";
			oid  += obj[i].orgId+ ",";//借用
		}
		document.getElementsByName("userName")[0].value = uname;
		userId.value = uid;
		document.getElementsByName("orgId")[0].value = oid;
	}
}

function openCode(){
	var cname = document.getElementsByName("codeName")[0];
	var cid   = document.getElementsByName("codeId")[0];
	var ccode = document.getElementsByName("codeCode")[0];
	var url = "<c:out value='${contextPath}'/>/common/ui/selector.do?step=selectCode&treeType=1&id=assessType";
	//注意这里id就是转送code值
	if(ccode.value){
		url += "&SelectNodeId="+ccode.value;
	}
	var obj = StyleControl.openDialogInFrame(url,600,500);
	if(obj){
		cname.value = obj[0].nodeName;
		cid.value = obj[0].target;//借用
		ccode.value = obj[0].nodeId;
	}
}

//缓式加载树
function openCodeTree(){
	var cname = document.getElementsByName("codeName")[0];
	var cid   = document.getElementsByName("codeId")[0];
	var ccode = document.getElementsByName("codeCode")[0];
	//注意这里id就是转送code值,如果不指定，则默认为0，显示所有代码
	var url = "<c:out value='${contextPath}'/>/jsp/selector/selectCode1.jsp?treeType=1&id=equipmentCode";
	if(ccode.value){
		url += "&SelectNodeId="+ccode.value;
	}
	var obj = StyleControl.openDialogInFrame(url,600,500);
	if(obj){
		cname.value = obj[0].nodeName;
		cid.value = obj[0].nodeKind;//借用
		ccode.value = obj[0].selfId;
	}
}
</script>
</body>
</html>