<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf"/>
<html>
<head>
<script type="text/javascript" src = "<c:out value='${initParam["publicResourceServer"]}'/>/public/scripts/xmlhttp.js"></script> 
</head>
<body class="list_body" >	
<div class="main_title"> <div>工作流管理</div></div>
<div class="main_body">
	<div id="divId_panel">
		<!-- 在这里显示选项卡-->
	</div>	
</div>

<entryPanel:show
			panelList=" 列表 #/sm/wfManageAction.do?step=list&paginater.page=0,"
			/>

</body>
</html>

