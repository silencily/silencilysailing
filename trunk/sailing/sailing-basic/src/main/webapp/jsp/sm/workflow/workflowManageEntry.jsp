<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf"/>
<html>
<head>
<script type="text/javascript" src = "<c:out value='${initParam["publicResourceServer"]}'/>/public/scripts/xmlhttp.js"></script> 
</head>
<body class="list_body" >	
<div class="main_title"> <div>����������</div></div>
<div class="main_body">
	<div id="divId_panel">
		<!-- ��������ʾѡ�-->
	</div>	
</div>

<entryPanel:show
			panelList=" �б� #/sm/wfManageAction.do?step=list&paginater.page=0,"
			/>

</body>
</html>

