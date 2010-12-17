<%--
    @version:$Id: deptEntry.jsp,v 1.1 2010/12/10 10:56:43 silencily Exp $
    @since:$Date: 2010/12/10 10:56:43 $
    @进入页面
--%>

<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf" />
<script type="text/javascript" src = "<c:out value='${initParam["publicResourceServer"]}'/>/public/scripts/xmlhttp.js"></script> 
<script type="text/javascript" src = "<c:out value='${initParam["publicResourceServer"]}'/>/public/scripts/codeTree.js"></script>
<html>
<body class="list_body">
	<form name="instrument_entry" method="post" action="">
		<table width="100%" border=0 cellpadding="0" cellspacing="0" height="100%">
		   <div class="main_title"><div>部门管理</div></div>
		    <tr valign="top">
		        <td width="20%" id="tdId_1">
					<div id="span_menu"  class="tree"></div>
		        </td>
		        <td width="5" style="cursor:e-resize" onmousedown="setCapture()" onmouseup="releaseCapture();" onmousemove="StyleControl.dragWidth(tdId_1,30)" bgcolor="#eeeeee">&nbsp;</td>
		        <td> 
		            <div class="main_body">
		                <div id="divId_panel"></div>
		            </div>
		            <entryPanel:show
						panelList=" 列表 #/sm/TblSmDeptAction.do?step=list&mainTableClassName=TblCmnDept&paginater.page=0&parentCode=,
									详细 #/sm/TblSmDeptAction.do?step=edit,
									状态变更 #/sm/TblSmDeptStatusRecAction.do?step=edit&paginater.page=0&parentCode=,
									部门合并 #/sm/TblSmDeptAction.do?step=info&paginater.page=0&parentCode=,
									上级部门变更 #/sm/TblSmDeptPareRecAction.do?step=info&parentCode="
						/>
		        </td>
		    </tr>
		</table>
	</form>
	<jsp:directive.include file="tree.jspf"/>
</body>
</html>
