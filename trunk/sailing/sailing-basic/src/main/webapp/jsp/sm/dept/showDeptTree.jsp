<%--
    @version:$Id: showDeptTree.jsp,v 1.1 2010/12/10 10:56:43 silencily Exp $
    @since:$Date: 2010/12/10 10:56:43 $
--%>

<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<html>
<head>
<title>����ѡ��</title>
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
				<td>����ѡ��</td>
		    	<td align="right">
	 				<input type="button" value="ȷ ��" title="���ȷ��" class="opera_display" onClick="doSave()">
	 				<input type="button" value="ȡ ��" title="�������" class="opera_display" onClick="doCancel()">	
				</td>
			</tr>
		</table>
	</div>
	<%--��������һ��������div��--%>
	<div style="overflow:scroll;width:495; height:370">
	<form name="instrument_entry" method="post" action="">
		<table width="100%" border=0 cellpadding="0" cellspacing="0">
		    <tr valign="top">
		        <td width="100%" id="tdId_1">
					<div id="span_menu" ></div>
		        </td>
           		<script language="javascript">
		                var arr = new Array();
						//ѡ�(��ǩ)�ؼ�
		                var panel = new Panel.panelObject("panel", arr, 0, "<c:out value = "${initParam['publicResourceServer']}/image/main/"/>");
		                //��ʾѡ���
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
		//�õ�����id������name
		if (codeTree.returnList == null) {
			alert("��ѡ��һ�����š�");
			return false;
		}
	  	var deptId=codeTree.returnList[0].selfId;
	  	var deptName=codeTree.returnList[0].nodeName;
	  	//�ѵõ���ֵ��д����
	  	var obj = new deptInfo(deptId,deptName);
	  	window.returnValue=obj;
	  	//�ر�
	  	window.close();
	}
</script>
</html>
