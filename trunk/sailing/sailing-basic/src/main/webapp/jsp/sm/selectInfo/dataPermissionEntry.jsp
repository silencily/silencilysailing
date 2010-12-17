<%--
    @version:$Id: dataPermissionEntry.jsp,v 1.1 2010/12/10 10:56:17 silencily Exp $
    @since:$Date: 2010/12/10 10:56:17 $
--%>

<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf" />
<script type="text/javascript" src = "<c:out value='${initParam["publicResourceServer"]}'/>/public/scripts/xmlhttp.js"></script> 
<script type="text/javascript" src = "<c:out value='${initParam["publicResourceServer"]}'/>/public/scripts/codeTree.js"></script>
<html>
<base target ="_self">
<body class="list_body" >

<form name="instrument_entry" method="post" action="">
<input type="hidden" name="userId" value="<c:out value='${theForm.userId}'/>"/>
<div class="update_subhead" >
    <span class="switch_open" onClick="StyleControl.switchDiv(this, $('setDefault'))" title="">设置权限默认值</span>
</div>
<div id="setDefault" style="display">
	<table width="100%"  border="0" cellpadding="0" cellspacing="0" class="Detail" style="display:">
	 </table>
</div>
<!--		<div class="update_subhead" id="list_button" align="right">-->
<!--			<input type="button" class="opera_select" id="decide" onClick="CurrentPage.myfunc();"/>-->
<!--			<input name="button" type="button"  class="opera_cancel" id="cancelname" onClick="window.close();"/>-->
<!--		</div>	-->
		<table width="100%" border=0 cellpadding="0" cellspacing="0" height="100%">
		    <tr valign="top">
		        <td width="30%" id="tdId_1">
		            <div class="update_subhead" id="list_button">&nbsp;</div>
					<div id="span_menu"  class="tree"></div>
		        </td>
		        <td width="5" style="cursor:e-resize" onMouseDown="setCapture()" onMouseUp="releaseCapture();" onMouseMove="StyleControl.dragWidth(tdId_1,30)" bgcolor="#eeeeee">&nbsp;</td>
		        <td>
		            <div class="main_body">
		                <div id="divId_panel"></div>
		            </div>
		            <script type="text/javascript">
		                var arr = new Array();
		                var userId = $('userId').value;
		                arr[0] = ["权限详细","", "<c:url value='/sm/DataMemberAction.do?step=info&parentId=&userId='/>"+ userId];
		                var panel = new Panel.panelObject("panel", arr, 0, "<c:out value = "${initParam['publicResourceServer']}/image/main/"/>");
		                document.getElementById("divId_panel").innerHTML = panel.display();
		            </script>
		        </td>
		    </tr>
		</table>
	</form>
	<jsp:directive.include file="dataPermissionTree.jspf"/>
</body>
</html>
