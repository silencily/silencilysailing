<%--
    @version:$Id: roleInfoEntry2.jsp,v 1.1 2010/12/10 10:56:17 silencily Exp $
    @since:$Date: 2010/12/10 10:56:17 $
    在列表而不是树上进行选择，区别于roleInfoEntry.jsp
--%>

<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf" />
<script type="text/javascript" src = "<c:out value='${initParam["publicResourceServer"]}'/>/public/scripts/xmlhttp.js"></script> 
<script type="text/javascript" src = "<c:out value='${initParam["publicResourceServer"]}'/>/public/scripts/codeTree.js"></script>
<html>
<base target ="_self">
<body class="list_body" >
<script language="javascript">
		if (CurrentPage == null) {
    	var CurrentPage = {};
		} 
/*  	CurrentPage.myfunc = function () {
  		var strFlg = document.getElementById("strFlg").value;
	 	var strIds = new Array();
	  	var imageType;
	  	var index = 0 ;
	  	var sId ="";
	  	var rolName=""; 
	  	for (var i = 0; i < codeTree.returnList.length; i++){
	  		imageType = codeTree.returnList[i].imgType;
	  		if (imageType == "2") {
	  			if (strFlg == "flow"){
	  				sId += codeTree.returnList[i].selfId + ";";
	  			
	  				rolName += codeTree.returnList[i].nodeName +";";
	  				
	  			}else{
					strIds[index++] = codeTree.returnList[i].selfId;	  			
	  			}
	  			
	  		}
	  	}
	  	if (strFlg == "flow"){
	  		strIds[0] = sId;
	  		strIds[1] = rolName;
	  	}
	   	if (strIds.length == 0) {
	  		alert("请选择至少一个权限");
	  		return;
	  	}
		window.returnValue=strIds;	
		window.close();	
	}
	*/


</script>
<form name="instrument_entry" method="post" action="">
<input type="hidden" id= "consignFlg" value="<c:out value='${theForm.consignFlg}'/>"/>	

<div class="main_title">
	<div>角色选择</div>
</div>

<input type="hidden" name="oid"  id= "oid" value="<c:out value='${theForm.oid}'/>"/>
<input type="hidden" name="strFlg"  id= "strFlg" value="<c:out value='${theForm.strFlg}'/>"/>		
		<table width="100%" border=0 cellpadding="0" cellspacing="0" height="100%">
		    <tr valign="top">
		        <td width="30%" id="tdId_1">
					<div id="span_menu"  class="tree"></div>
		        </td>
		        <td width="5" style="cursor:e-resize" onmousedown="setCapture()" onmouseup="releaseCapture();" onmousemove="StyleControl.dragWidth(tdId_1,30)" bgcolor="#eeeeee">&nbsp;</td>
		        <td>
		            <div class="main_body">
		                <div id="divId_panel"></div>
		            </div>
		            <script type="text/javascript">
		                var arr = new Array();
		                arr[0] = ["列表","", "<c:url value='/sm/RoleAction.do?step=selectInfo2&paginater.page=0&parentCode='/>"];
		                var panel = new Panel.panelObject("panel", arr, 0, "<c:out value = "${initParam['publicResourceServer']}/image/main/"/>");
		                document.getElementById("divId_panel").innerHTML = panel.display();
		            </script>
		        </td>
		    </tr>
		</table>
	</form>
	<jsp:directive.include file="tree2.jspf"/>
</body>
</html>
