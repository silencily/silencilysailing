
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%--
    @version:$Id: select_permission.jsp,v 1.1 2010/12/10 10:56:39 silencily Exp $
    @since $Date: 2010/12/10 10:56:39 $
    @组织的列表页面
--%>

<jsp:directive.include file="/decorators/default.jspf"/>
<script type="text/javascript" src = "<c:out value='${initParam["publicResourceServer"]}'/>/public/scripts/xmlhttp.js"></script> 
<script type="text/javascript" src = "<c:out value='${initParam["publicResourceServer"]}'/>/public/scripts/codeTree.js"></script>
<html>
<script language="javascript">
		if (CurrentPage == null) {
    	var CurrentPage = {};
		} 

 /* 	CurrentPage.myfunc = function () {
	  	window.opener.document.getElementById("parentCode").value= codeTree.TreeList[codeTree.SelectId].selfId;
	  	window.opener.document.getElementById("fatherDisplayName").value = codeTree.TreeList[codeTree.SelectId].nodeName;
	  	window.close();
	}
	*/
top.definedWin.selectListing = function(){
	var parentCode = codeTree.TreeList[codeTree.SelectId].selfId;
	var fatherName = codeTree.TreeList[codeTree.SelectId].nodeName;
	var arr = new Array();
	arr[0] = parentCode;
	arr[1] = fatherName;
	top.definedWin.listObject(arr);
}
</script>
<body>
<form name="f" action="" method="post">
<div class="main_title">
<div>选择父节点</div>
</div>
<input type="hidden" name="oid"  id="oid" value="<c:out value='${theForm.oid}'/>"/>
<input type="hidden" name="parentCode"  id="parentCode" value="<c:out value='${theForm.parentCode}'/>"/>
<input type="hidden" name="strCreate"  id="hcreat" value="<c:out value='${theForm.strCreate}'/>"/>
<table width="100%" border=0 cellpadding="0" cellspacing="0" height="100%">
<!--	 <tr>-->
<!--	 	<td>-->
<!--		<div class="list_group">-->
<!--			<div class="list_title" id="list_title">选择组织结构<span class="list_notes"></span> </div>-->
<!--		</div>	-->
<!--	 	</td>-->
<!--	 </tr>-->
	 <tr valign="top">
	     <td width="100%" id="tdId_2">
<!--			<div class="update_subhead" id="list_button" align="right">-->
<!--				<input type="button" class="opera_select" id="decide" title=""  onClick="CurrentPage.myfunc();"/>-->
<!--				<input name="button" type="button"  class="opera_cancel" id="cancelname" title="" onClick="window.close();"/>-->
<!--			</div>-->
			<div id="span_menu"  class="tree"></div>
	     </td>
	</tr>
	</table>
	</form>
	<jsp:directive.include file="select_permissiontree.jspf"/>
</body>
</html>
