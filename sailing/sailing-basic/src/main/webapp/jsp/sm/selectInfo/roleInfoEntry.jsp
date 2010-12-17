<%--
    @version:$Id: roleInfoEntry.jsp,v 1.1 2010/12/10 10:56:17 silencily Exp $
    @since:$Date: 2010/12/10 10:56:17 $
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
top.definedWin.selectListing = function(){
		var consignFlg = document.getElementById("consignFlg").value;
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
	if(consignFlg=="consign"){
		var startTime = document.getElementById('startTime').value;
		var endTime = document.getElementById('endTime').value;
		if(startTime==""){
		alert("起始时间不能为空");
		return false;
		}
		if(endTime==""){
		alert("结束时间不能为空");
		return false;
		}
		if (DateUtils.CompareDate(startTime,endTime)<0){
					Validator.clearValidateInfo();
					alert("起始时间不能大于失效时间，请重新输入！");
					return false;	
				}
		if (CurrentPage.CheckDay($('startTime').value) == false ) {
					Validator.clearValidateInfo();
					alert("起始时间不能是过去时间，请重新输入！");
					return false;
				}
		if (CurrentPage.CheckDay($('endTime').value) == false ) {
					Validator.clearValidateInfo();
					alert("失效时间不能是过去时间，请重新输入！");
					return false;
				}
		var num = strIds.length;
		strIds[num] = startTime;
		strIds[num+1] = endTime;
	}
	top.definedWin.listObject(strIds);
}
			var d, s = "";
   			d = DateUtils.GetDBDate();
   			s +=d.getYear() + "-";
   			var month = d.getMonth()+1;
   			if(month<10){
   			s += "0" + month + "-";
   			}else{
   			s += month + "-";
   			}
   			var day = d.getDate();
   			if(day <10){
   			s += "0" + day;
   			}else{
   				s += d.getDate();  
   			}
			CurrentPage.CheckDay = function (day) {
				if (day != ""){
					if(DateUtils.CompareDate(s,day)<0)
						return false;
					}
				else{
					return true;
				}
			}
</script>
<form name="instrument_entry" method="post" action="">
<input type="hidden" id= "consignFlg" value="<c:out value='${theForm.consignFlg}'/>"/>	
<c:choose>
<c:when test="${theForm.consignFlg=='consign'}">
<div class="update_subhead" >
    <span class="switch_open" onClick="StyleControl.switchDiv(this, $('setDefault'))" title="">设置委托角色有效起止时间</span>
</div>
<div id="setDefault" style="display">
	<table width="100%"  border="0" cellpadding="0" cellspacing="0" class="Detail" style="display:">
		<tr>
			<td class="attribute">起始时间</td>
			<td>
			<input type="text" id ="startTime" name="role.beginTime" readonly
				"<c:out value='${pageScope.textdisable}'/>" /><input type="button" value="" id="input_date" onClick="DateComponent.setDay(this,document.getElementsByName('role.beginTime')[0])"
				<c:out value='${pageScope.buttondisable}'/> />
			</td>
			<td class="attribute">失效时间</td>
			<td>
			<input type="text" id ="endTime" name="role.invalidTime" readonly
				"<c:out value='${pageScope.textdisable}'/>" /><input type="button" value="" id="input_date" onClick="DateComponent.setDay(this,document.getElementsByName('role.invalidTime')[0])"
				<c:out value='${pageScope.buttondisable}'/> />
			</td>
		</tr>
	 </table>
</div>
</c:when>
<c:otherwise>
<div class="main_title">
	<div>角色选择</div>
</div>
</c:otherwise>
</c:choose>
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
		                arr[0] = ["列表","", "<c:url value='/sm/RoleAction.do?step=selectInfo&paginater.page=0&parentCode='/>"];
		                var panel = new Panel.panelObject("panel", arr, 0, "<c:out value = "${initParam['publicResourceServer']}/image/main/"/>");
		                document.getElementById("divId_panel").innerHTML = panel.display();
		            </script>
		        </td>
		    </tr>
		</table>
	</form>
	<jsp:directive.include file="tree.jspf"/>
</body>
</html>
