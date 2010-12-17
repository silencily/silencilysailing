<%--
    @version:$Id: selectMultipleRoleEntry.jsp,v 1.1 2010/12/10 10:56:18 silencily Exp $
    @since:$Date: 2010/12/10 10:56:18 $
    @进入页面
--%>

<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf" />

<script type="text/javascript" src = "<c:out value='${initParam["publicResourceServer"]}'/>/public/scripts/xmlhttp.js"></script> 
<script type="text/javascript" src = "<c:out value='${initParam["publicResourceServer"]}'/>/public/scripts/codeTree.js"></script>
<html>
		<body class="list_body">
	<form name="instrument_entry" method="post" action="">
	<input type="hidden" name="parentCode" id="parentCode" value="<c:out value = "${theForm.parentCode}"/>"/>
	<input type="hidden" name="strFlg" id="strFlg" value="<c:out value = "${theForm.strFlg}"/>"/>
		<table width="100%" border=0 cellpadding="0" cellspacing="0" height="100%">

	<div class="main_title">
		<div>角色选择</div>
	</div>
		    <tr valign="top">
		        <td width="20%" id="tdId_1">
					<div class="update_subhead" id="list_button">&nbsp;</div>
					<div id="span_menu"  class="tree"></div>
		        </td>
		        <td width="5" style="cursor:e-resize" onMouseDown="setCapture()" onMouseUp="releaseCapture();" onMouseMove="StyleControl.dragWidth(tdId_1,30)" bgcolor="#eeeeee">&nbsp;</td>
		        <td> 
		            <div class="main_body">
		                <div id="divId_panel"></div>
		            </div>
		            <script type="text/javascript">
		                var strFlg=document.getElementById("strFlg").value;
		                var url='<c:url value="/sm/RoleAction.do?step=selectMultipleRoleList&paginater.page=0&parentCode=&strFlg="/>'+strFlg;	            
		                var arr = new Array();
		                arr[0] = [" 角色选择 ", "", url];
		                var panel = new Panel.panelObject("panel", arr, 0, "<c:out value = "${initParam['publicResourceServer']}/image/main/"/>");
		                document.getElementById("divId_panel").innerHTML = panel.display();
		            </script> 
		        </td>
		    </tr>
		</table>
	</form>
	 
	<jsp:directive.include file="treeOfMultiple.jspf"/>

</body>
</html>
