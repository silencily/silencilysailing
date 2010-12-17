<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf"/>
<html>
<body class="list_body" >	
	<form name="instrument_entry" method="post" action="">
		<table width="100%" border=0 cellpadding="0" cellspacing="0" height="100%">
		   <div class="main_title"><div>数据权限管理</div></div>
		    <tr valign="top">
		        <td width="5" style="cursor:e-resize" onMouseDown="setCapture()" onMouseUp="releaseCapture();" onMouseMove="StyleControl.dragWidth(tdId_1,30)" bgcolor="#eeeeee">&nbsp;</td>
		        <td> 
		            <div class="main_body">
		                <div id="divId_panel"></div>
		            </div>
		            <script type="text/javascript">
		                var arr = new Array();
		                arr[0] = [" 列表 ", "", "<c:url value = '/sm/DataPermissionAction.do?step=list&paginater.page=0'/>"];
						arr[1] = [" 详细 ", "", "<c:url value = '/sm/DataPermissionAction.do?step=info'/>"];
		                var panel = new Panel.panelObject("panel", arr, 0, "<c:out value = "${initParam['publicResourceServer']}/image/main/"/>");
		                document.getElementById("divId_panel").innerHTML = panel.display();
		                Global.displayOperaButton = function(){ };
		            </script> 
		        </td>
		    </tr>
		</table>
	</form>
</body>
</html>
