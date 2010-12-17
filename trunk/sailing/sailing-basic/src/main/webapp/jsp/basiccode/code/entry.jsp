<%--
    @version:$Id: entry.jsp,v 1.1 2010/12/10 10:56:44 silencily Exp $
    @since:$Date: 2010/12/10 10:56:44 $
--%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf" />
<script type="text/javascript" src = "<c:out value='${initParam["publicResourceServer"]}'/>/public/scripts/xmlhttp.js"></script> 
<script type="text/javascript" src = "<c:out value='${initParam["publicResourceServer"]}'/>/public/scripts/codeTree.js"></script>

<html>
<body class="list_body" >
	<form name="instrument_entry" method="post" action="">
		<table style="table-layout:fixed" width="100%" border=0 cellpadding="0" cellspacing="0" height="100%">
		   <div class="main_title"><div>基础编码维护</div></div>
		    <tr valign="top">
		        <td width="20%" id="tdId_1">
					<div id="span_menu" class="tree"></div>
		        </td>
		        <td width="5" style="cursor:e-resize" onmousedown="setCapture()" onmouseup="releaseCapture();" onmousemove="StyleControl.dragWidth(tdId_1,30)" bgcolor="#eeeeee">&nbsp;</td>
		        <td>
		            <div class="main_body">
		                <div id="divId_panel" ></div>
		            </div>
		            <entryPanel:show
						panelList=" 列表 #/common/basiccodemanager.do?step=list&paginater.page=0&parentCode=${theForm.root},
									详细 #/common/basiccodemanager.do?step=edit&parentCode="
					/>
		            <script type="text/javascript">
		           		function setParentCd(parentCd) {
		                	arr[1][2]  = "<c:url value='/common/basiccodemanager.do?step=edit'/>";
			                arr[1][2]  += "&parentCode=" + parentCd;
		                }
		            </script>
		        </td>
		    </tr>
		</table>
	</form>
	<jsp:directive.include file="tree.jspf"/>
</body>
</html>
