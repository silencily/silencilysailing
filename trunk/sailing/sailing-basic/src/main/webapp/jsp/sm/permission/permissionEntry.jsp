<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%><%--
    @version:$Id: permissionEntry.jsp,v 1.1 2010/12/10 10:56:39 silencily Exp $
    @since:$Date: 2010/12/10 10:56:39 $
    @进入页面
--%>


<jsp:directive.include file="/decorators/default.jspf" />
<script type="text/javascript" src = "<c:out value='${initParam["publicResourceServer"]}'/>/public/scripts/xmlhttp.js"></script> 
<script type="text/javascript" src = "<c:out value='${initParam["publicResourceServer"]}'/>/public/scripts/codeTree.js"></script>
<html>
<body class="list_body">
<script language="javascript">
  var myflag = false;	
	Panel.beforeClick = function(index){
		var framename = 'frame_' + panel.name + '_'+ 0;
		var oid = document.frames(framename).document.getElementsByName("oid")[0];
		if (index == 1 && !oid && myflag==false) {
	 		var tempParentCode = codeTree.TreeList[codeTree.SelectId].selfId;
	  		var strFlg = codeTree.TreeList[codeTree.SelectId].imgType;
	 		var pat = new RegExp('([\?\&])parentCode=[^\&]*');
	 		var strFlg = codeTree.TreeList[codeTree.SelectId].imgType;
		  	var parentCode;
		  	if (strFlg == '2') {
	  			parentCode = tempParentCode+";"+"2"+";c";  
		       } else {
		       	parentCode = tempParentCode+";"+"1"+";c";
				
				} 
	 	  	var url = panel.dataArr[1][2];
			if (pat.test(url)) {
			   	panel.dataArr[1][2]=url.replace(pat, RegExp.$1 + "parentCode=" + parentCode);
			}
    	}
		myflag=false;
		if(this.checktaglib){
			return this.checkTaglibValues();
		}
		else if(this.checkallvalue){
			return this.checkValues();
		}else{return true;}
	}

	Panel.afterClick = function(index){
 		var pat = new RegExp('([\?\&])parentCode=[^\&]*');
 	  	var url = panel.dataArr[1][2];
		if (pat.test(url)) {
		  	panel.dataArr[1][2]=url.replace(pat, RegExp.$1 + "&parentCode=");
		}
		this.beforeindex = index;
	}

	var CurrentPage = {};
  CurrentPage.creatRole = function creatFolder() {
 		var tempParentCode = codeTree.TreeList[codeTree.SelectId].selfId;
 		var pat = new RegExp('([\?\&])parentCode=[^\&]*');
 		var parentCode = tempParentCode+";"+"0"+";c"; 
	  	var url = panel.dataArr[1][2];
	  	var strFlg = codeTree.TreeList[codeTree.SelectId].imgType;
		if (strFlg != '0'&&strFlg != '1'&&strFlg != '')
		{
			if (strFlg == '2') {
	 			parentCode = tempParentCode+";"+"2"+";c";  
	       	} else {
	       	parentCode = tempParentCode+";"+"1"+";c";
			
			} 	
			
		}
		if (pat.test(url)) {
		   	panel.dataArr[1][2]=url.replace(pat, RegExp.$1 + "parentCode=" + parentCode);
			myflag=true;
		  	panel.click(1);	
		  	panel.dataArr[1][2]=url.replace(pat, RegExp.$1 + "&parentCode=");
		}

			
	}
</script>
	
	<form name="instrument_entry" method="post" action="">
		<table width="100%" border=0 cellpadding="0" cellspacing="0" height="100%">
		   <div class="main_title"><div>权限管理</div></div>
		    <tr valign="top">
		        <td width="20%" id="tdId_1">
		            <div class="update_subhead" id="list_button" align="right">
						<input type="button" align="right" class="list_create" id="btn_add1" title="点击新增一个目录节点" onClick="CurrentPage.creatRole();"/>
					</div>
					<div id="span_menu"  class="tree"></div>
		        </td>
		        <td width="5" style="cursor:e-resize" onMouseDown="setCapture()" onMouseUp="releaseCapture();" onMouseMove="StyleControl.dragWidth(tdId_1,30)" bgcolor="#eeeeee">&nbsp;</td>
		        <td> 
		            <div class="main_body">
		                <div id="divId_panel"></div>
		            </div>
		            <script type="text/javascript">
		                var arr = new Array();
		                arr[0] = [" 列表 ", "", "<c:url value='/sm/permissionAction.do?step=list&paginater.page=0&parentCode='/>"];
		                arr[1] = [" 详细 ", "", "<c:url value='/sm/permissionAction.do?step=edit&parentCode='/>"];
		                arr[2] = [" 角色 ", "", "<c:url value='/sm/permissionAction.do?step=permissionRole&paginater.page=0&parentCode='/>"];
		                arr[3] = [" 用户(单独授权) ", "", "<c:url value='/sm/permissionAction.do?step=permissionUser&paginater.page=0&parentCode='/>"];
		                var panel = new Panel.panelObject("panel", arr, 0, "<c:out value = "${initParam['publicResourceServer']}/image/main/"/>");
		                document.getElementById("divId_panel").innerHTML = panel.display();
		                Global.displayOperaButton = function(){ };
		            </script> 
		        </td>
		    </tr>
		</table>
	</form>
	<jsp:directive.include file="tree.jspf"/>
</body>
</html>
