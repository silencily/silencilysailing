<%--
    @version:$Id: showDeptTree.jsp,v 1.1 2010/12/10 10:56:43 silencily Exp $
    @since:$Date: 2010/12/10 10:56:43 $
--%>

<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<html>
<head>
<title>部门选择</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<jsp:directive.include file="/decorators/default.jspf"/>
<script type="text/javascript" src = "<c:out value='${initParam["publicResourceServer"]}'/>/public/scripts/xmlhttp.js"></script> 
<script type="text/javascript" src = "<c:out value='${initParam["publicResourceServer"]}'/>/public/scripts/codeTree.js"></script>
</head>

<body >
	<div class="main_title">
		<table width="100%" border=0 cellpadding="0" cellspacing="0">
			<tr>
				<td>部门选择</td>
		    	<td align="right">
	 				<input type="button" value="确 定" title="点击确定" class="opera_display" onClick="doSave()">
	 				<input type="button" value="取 消" title="点击返回" class="opera_display" onClick="doCancel()">	
				</td>
			</tr>
		</table>
	</div>
	<%--把树放在一个独立的div中--%>
	<div style="overflow:scroll;width:495; height:370">
	<form name="instrument_entry" method="post" action="">
		<table width="100%" border=0 cellpadding="0" cellspacing="0">
		    <tr valign="top">
		        <td width="100%" id="tdId_1">
					<div id="span_menu" ></div>
		        </td>
           		<script language="javascript">
		                var arr = new Array();
						//选项卡(标签)控件
		                var panel = new Panel.panelObject("panel", arr, 0, "<c:out value = "${initParam['publicResourceServer']}/image/main/"/>");
		                //显示选项卡层
		                panel.display();
		        </script>		        
		    </tr>
	</table>
	</form>
	<jsp:directive.include file="selectTree.jspf"/>	
	</div>
</body>

<script language="javascript" >
	function deptInfo(deptId,deptName){
		this.deptId = deptId;
		this.deptName=deptName;
	}
	function doCancel(){		
			window.close();
	}
	function doSave(){
		//得到部门id，部门name
		if (codeTree.returnList == null) {
			alert("请选择一个部门。");
			return false;
		}
	  	var deptId=codeTree.returnList[0].selfId;
	  	var deptName=codeTree.returnList[0].nodeName;
	  	//把得到的值反写回来
	  	var obj = new deptInfo(deptId,deptName);
	  	window.returnValue=obj;
	  	//关闭
	  	window.close();
	}
</script>
</html>
