<%--
    @version:$Id: selectEntry.jsp,v 1.1 2010/12/10 10:56:17 silencily Exp $
    @since:$Date: 2010/12/10 10:56:17 $
--%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ include file="/decorators/default.jspf"%>
<script type="text/javascript" src = "<c:out value='${initParam["publicResourceServer"]}'/>/public/scripts/xmlhttp.js"></script> 
<script type="text/javascript" src = "<c:out value='${initParam["publicResourceServer"]}'/>/public/scripts/codeTree.js"></script>
<html>
<body class="list_body">
<div class="main_title"> <div>人员选择</div></div>
<div class="main_body">
	<div id="divId_panel">
		<!-- 在这里显示选项卡-->
	</div>	
</div>
		            <script type="text/javascript">
		                var arr = new Array();
		                arr[0] = ["人员选择","", "<c:url value='/sm/SelectInfoAction.do?step=selectInfo&paginater.page=0'/>"];
		                var panel = new Panel.panelObject("panel", arr, 0, "<c:out value = "${initParam['publicResourceServer']}/image/main/"/>");
		                document.getElementById("divId_panel").innerHTML = panel.display();
		            </script>
</body>
</html>
