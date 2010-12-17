<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%--
    @version:$Id: directoryEntry.jsp,v 1.1 2010/12/10 10:56:26 silencily Exp $
    @since:$Date: 2010/12/10 10:56:26 $
    @进入页面
--%>


<jsp:directive.include file="/decorators/default.jspf" />
<script type="text/javascript" src = "<c:out value='${initParam["publicResourceServer"]}'/>/public/scripts/xmlhttp.js"></script> 
<script type="text/javascript" src = "<c:out value='${initParam["publicResourceServer"]}'/>/public/scripts/codeTree.js"></script>
<html>
<body class="list_body">
<script language="javascript">
	var CurrentPage = {};
	
  CurrentPage.creatMenu = function creatFolder() {
  		$('oid').value = '';
	  	var pat = new RegExp('([\?\&])parentCode=[^\&]*');
	  	var parentCode = document.getElementById("parentCode").value;
	  	var strTemp = parentCode +";org;c"
	  	var url = panel.dataArr[1][2];
		if (pat.test(url)) {
	   	panel.dataArr[1][2]=url.replace(pat, RegExp.$1 + "parentCode="+ strTemp);
    	panel.click(1);
    	panel.dataArr[1][2]=url.replace(pat, RegExp.$1 + "&parentCode=");	
		}
	}
</script>
	
	<form name="instrument_entry" method="post" action="">
	<input type="hidden" name="oid"/>
	<input type="hidden" name="parentCode" id="parentCode" value="<c:out value = "${theForm.parentCode}"/>"/>
		<table width="100%" border=0 cellpadding="0" cellspacing="0" height="100%">
		<div class="main_title"><div>角色目录管理</div></div>
		    <tr valign="top">
		        <td width="20%" id="tdId_1">
		            <!--div class="update_subhead" id="list_button" align="right">
						<input type="button" align="right" class="list_create" id="btn_add1" title="点击新增一个目录节点" onClick="CurrentPage.creatMenu();"/>
					</div-->
					<div id="span_menu"  class="tree"></div>
		        </td>
		        <td width="5" style="cursor:e-resize" onMouseDown="setCapture()" onMouseUp="releaseCapture();" onMouseMove="StyleControl.dragWidth(tdId_1,30)" bgcolor="#eeeeee">&nbsp;</td>
		        <td> 
		            <div class="main_body">
		                <div id="divId_panel"></div>
		            </div>
		            <script type="text/javascript">
		                var arr = new Array();
		                arr[0] = [" 列表 ", "", "<c:url value='/sm/RoleAction.do?step=list&paginater.page=0&flg=org&parentCode='/>"];
		                arr[1] = [" 详细 ", "", "<c:url value='/sm/RoleAction.do?step=detail&flg=org&parentCode='/>"];
		                var panel = new Panel.panelObject("panel", arr, 0, "<c:out value = "${initParam['publicResourceServer']}/image/main/"/>");
		                document.getElementById("divId_panel").innerHTML = panel.display();
		                Global.displayOperaButton = function(){ };
		            </script> 
		        </td>
		    </tr>
		</table>
	</form>
	<jsp:directive.include file="directoryTree.jspf"/>
</body>
</html>
