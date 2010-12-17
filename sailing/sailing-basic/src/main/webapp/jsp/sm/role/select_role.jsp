<%--
    @version:$Id: select_role.jsp,v 1.1 2010/12/10 10:56:27 silencily Exp $
    @since $Date: 2010/12/10 10:56:27 $
    @组织的列表页面
--%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf"/>
<script type="text/javascript" src = "<c:out value='${initParam["publicResourceServer"]}'/>/public/scripts/xmlhttp.js"></script> 
<script type="text/javascript" src = "<c:out value='${initParam["publicResourceServer"]}'/>/public/scripts/codeTree.js"></script>
<html>
<script language="javascript">
		if (CurrentPage == null) {
    	var CurrentPage = {};
		} 

  	CurrentPage.myfunc = function () {
	  	window.opener.document.getElementById("oid").value= codeTree.TreeList[codeTree.SelectId].selfId;
	  	window.opener.document.getElementById("fatherDisplayName").value = codeTree.TreeList[codeTree.SelectId].nodeName;
	  	window.close();
	}

</script>
<body class="list_body">
<form name="f" action="" method="post">
<input type="hidden" name="oid"  id="oid" value="<c:out value='${theForm.oid}'/>"/>
<input type="hidden" name="parentCode"  id="parentCode" value="<c:out value='${theForm.parentCode}'/>"/>
<input type="hidden" name="strFlg"  id="hflg" value="<c:out value='${theForm.strFlg}'/>"/>
<input type="hidden" name="strCreate"  id="hcreat" value="<c:out value='${theForm.strCreate}'/>"/>
<table width="100%" border=0 cellpadding="0" cellspacing="0" height="100%">
	 <tr>
	 	<td>
		<div class="list_group">
			<div class="list_title" id="list_title">选择角色<span class="list_notes"></span> </div>
		</div>	
	 	</td>
	 </tr>
	 <tr valign="top">
	     <td width="100%" id="tdId_2">
			<div class="update_subhead" id="list_button" align="right">
				<input type="button" class="opera_select" onClick="CurrentPage.myfunc();"/>
				<input name="button" type="button"  class="opera_cancel" id="cancelname" onClick="window.close();"/>
			</div>
			<div id="span_menu"  class="tree"></div>
	     </td>
	</tr>
	</table>
</form>
<jsp:directive.include file="select_roletree.jspf"/>
</body>
</html>
