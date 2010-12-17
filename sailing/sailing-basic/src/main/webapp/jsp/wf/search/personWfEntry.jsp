<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf"/>
<html>
<head>
<script type="text/javascript" src = "<c:out value='${initParam["publicResourceServer"]}'/>/public/scripts/xmlhttp.js"></script> 
</head>
<body class="list_body" >	
<div class="main_title"> <div>浏览个人工作流</div></div>
<div class="main_body">
	<div id="divId_panel">
		<!-- 在这里显示选项卡-->
	</div>	
</div>
<input type="hidden" id="urlkey" value="<%=request.getAttribute("urlkey")==null?"":request.getAttribute("urlkey")%>"/>
<input type="hidden" id="index" value=""/>
<script type="text/javascript">
	var urlkey=document.getElementById("urlkey").value;
	var index;
	if (urlkey==null) {
		index=0;
	}
	if (urlkey=="") {
		index=0;
	}
	if (urlkey=="waitList") {
		index=0;
	}
	if (urlkey=="alreadyList") {
		index=1;
	}
	if (urlkey == 'passroundList') {
		index = 2;
	}
	//if (urlkey=="recieveList") {
	//	index=2;
	//}
	//if (urlkey=="entrustList") {
	//	index=3;
	//}
	//if (urlkey=="allList") {
	//	index=4;
	//}
	document.getElementById("index").value = index;
</script>
<entryPanel:show
			panelList="待办任务#/wf/personWfSearchAction.do?step=waitList&paginater.page=0,
					   已办任务#/wf/personWfSearchAction.do?step=alreadyList&paginater.page=0,
					   传阅任务#/wf/personWfSearchAction.do?step=passroundList&paginater.page=0,"
			selectId="document.getElementById('index').value" 
			/>
</body>
</html>

