<%--
    @version:$Id: permissionInfoEntry.jsp,v 1.1 2010/12/10 10:56:17 silencily Exp $
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
<div class="update_subhead" >
    <span class="switch_open" onClick="StyleControl.switchDiv(this, $('setDefault'))" title="">设置权限默认值</span>
</div>
<div id="setDefault" style="display">
	<table width="100%"  border="0" cellpadding="0" cellspacing="0" class="Detail" style="display:">
	    <tr>
			<td class="attribute">列表数据显示范围</td>
			<td>
			<ec:composite value='0'  valueName = "temp.readAccessLevel" textName = "temp.conditions(readAccessLevel).value" source = "${theForm.readAccessLevelComboList}"/>
			</td>	    
	    	<td class="attribute">数据编辑控制</td>
			<td>
			<ec:composite value='2'  valueName = "temp.rwCtrl" textName = "temp.conditions(rwCtrl).value" source = "${theForm.rwCtrlComboList}"/>
			</td>
			<td class="attribute">数据编辑范围</td>
			<td>
			<ec:composite value='0'  valueName = "temp.writeAccessLevel" textName = "temp.conditions(writeAccessLevel).value" source = "${theForm.writeAccessLevelComboList}"/>
			</td>
	    </tr>
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
		                arr[0] = ["权限详细","", "<c:url value='/sm/permissionAction.do?step=permissionInfo&parentCode='/>"];
		                var panel = new Panel.panelObject("panel", arr, 0, "<c:out value = "${initParam['publicResourceServer']}/image/main/"/>");
		                document.getElementById("divId_panel").innerHTML = panel.display();
		            </script>
		        </td>
		    </tr>
		</table>
	</form>
	<jsp:directive.include file="permissionTree.jspf"/>
<script language="javascript">
		if (CurrentPage == null) {
    	var CurrentPage = {};
		}
		
top.definedWin.selectListing = function(){
  	var arr = new Array();
  	var rwCtrl = document.getElementsByName("temp.rwCtrl")[0].value;
  	var readAccessLevel =  document.getElementsByName("temp.readAccessLevel")[0].value;
  	var writeAccessLevel = document.getElementsByName("temp.writeAccessLevel")[0].value;
  	if(rwCtrl==""){
  		rwCtrl = "2";
  	}
 	if(readAccessLevel==""){
 		readAccessLevel = "0";
  	}
  	if(writeAccessLevel==""){
  		writeAccessLevel = "0";
  	}
  	arr[0]=rwCtrl;
  	arr[1]=readAccessLevel;
  	arr[2]=writeAccessLevel;
  	var imageType;
  	var index = 3;
	var temp = "";
  	for (var i = 0; i < codeTree.returnList.length; i++){
		var fatherid = codeTree.returnList[i].fatherId;
		var uid = codeTree.TreeList[fatherid].selfId;
  		imageType = codeTree.returnList[i].imgType;
  		//if (imageType == "2" || imageType == "3") {
		//temp+=codeTree.returnList[i].selfId+";";
		//if(imageType == "2"
       if( !codeTree.getObj('box',uid).checked || imageType=="3")
		{
			if(codeTree.returnList[i].selfId!="root")
	   		arr[index++] = codeTree.returnList[i].selfId;
  		}
  	}
   	if (arr.length == 3) {
  		alert("请选择至少一个权限");
  		return;
  	}
	top.definedWin.listObject(arr);
}

/* 	CurrentPage.myfunc = function () {
  	var strIds = new Array();
  	var imageType;
  	var index = 0;
  	for (var i = 0; i < codeTree.returnList.length; i++){
  		imageType = codeTree.returnList[i].imgType;
  		if (imageType == "2" || imageType == "3") {
   			strIds[index++] = codeTree.returnList[i].selfId;
   			
  		}
  	}
   	if (strIds.length == 0) {
  		alert("请选择至少一个权限");
  		return;
  	}
  	
	window.returnValue=strIds;	
	window.close();	

	}*/
	
</script>
</body>
</html>
