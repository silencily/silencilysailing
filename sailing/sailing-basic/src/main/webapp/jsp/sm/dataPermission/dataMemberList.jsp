<%-- jsp1.2 --%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf"/>

<script type="text/javascript">
var msgInfo_ = new msgInfo();
if (CurrentPage == null) {
	var CurrentPage = {};
}
        CurrentPage.validation = function () {
	    return Validator.Validate(document.forms[0],5);
}
 
		CurrentPage.create = function() {
		if($('oid')){		
			$('oid').value = '';
		}
		$('step').value = 'info';
		TableSort.setNoSelect();
		TableSort.dblClick();
	}
	    CurrentPage.remove = function(oid) {
		if (!confirm(msgInfo_.getMsgById('UF_I012_A_0'))) {
			return false;
		} 
		FormUtils.post(document.forms[0], '<c:url value = "/sm/DataMemberAction.do?step=delete&oid="/>'+ oid );
	}


CurrentPage.query = function() {
	if (document.getElementsByName("paginater.page") != null) {
	  	  document.getElementsByName("paginater.page").value = 0;
	}
	
	
	FormUtils.post(document.forms[0], '<c:url value="/sm/DataMemberAction.do?step=${theForm.step}"/>');
}

Global.displayOperaButton();

</script>
<body class="list_body">
<form name="form">
<input type="hidden" name="step" value="<c:out value = "${theForm.step}" />" />	
<input type="hidden" name="	" value="<c:out value='${theForm.bean.id}'/>"/>
<jsp:directive.include file="memberSearch.jspf"/>
<div class="update_subhead">
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td><span class="switch_open" onClick="StyleControl.switchDiv(this,$('allHidden'))" title="伸缩节点">检索结果</span></td>
		</tr>
	</table>
</div>
<div id="allHidden">
<div id="divId_scrollLing" class="list_scroll">
	<table border="0" cellpadding="0" cellspacing="0" class="Listing" id="listtable" onClick="TableSort.sortColumn(event)">
		<table:table name="viewBean" del="true" box="radio"/>
	</table>
</div>
<div class="list_bottom"><c:set var="paginater.forwardUrl"value="/sm/DataMemberAction.do?step=list" scope="page" /> 
	<%@ include file="/decorators/paginater.jspf"%>
</div>
</div>
</form>
</body>