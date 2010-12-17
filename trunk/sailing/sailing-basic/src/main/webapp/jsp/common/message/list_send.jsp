
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ include file = "/decorators/default.jspf" %>

<c:set var="contextPath" value="${initParam['publicResourceServer']}"/>
<c:set var="imgSpace" value="${contextPath}/image/common/top_space.gif"/>
<c:set var="actionName" value="/common/messageAction.do"/>

<body>
<form name="f" action="">
<input type="hidden" name="step" value="<c:out value = "${theForm.step}" />" />	

<div>

	<div class="list_explorer"> 
		<span class="switch_open" onClick="StyleControl.switchDiv(this,tabId_listing)" title="点击收缩表格">
			我发送的消息列表
		</span>
		
		<img src='<c:out value="${imgSpace}"/>'/>	
		<img src='<c:out value="${imgSpace}"/>'/>
		<c:forEach var="item1" items="${theForm.messageStatus}" varStatus="status1">
		<a href="<c:url value='/common/messageAction.do?step=listSendForStatus&status=${item1.status.code}'/>">
			<img src="<c:out value='${contextPath}'/>/image/common/<c:out value='${item1.status.description}'/>" alt="<c:out value='${item1.status.name}'/>"/> 
			<c:out value='${item1.status.name}'/><c:out value='${item1.num}'/>条</a>
			<img src='<c:out value="${imgSpace}"/>'/>
		</c:forEach>
		 
	</div>

	<div id="divId_scrollLing" class="list_scroll">
	<table border="0" cellpadding="0" cellspacing="0" class="Listing" id="tabId_listing" onClick="TableSort.sortColumn(event)">
	<thead>
	  <tr>
		<td width=5%>&nbsp;</td>
		<td width=5%>附件</td>
		<td width=20%>接受人</td>
		<td width=45%>标题 </td>		
		<td width=15%>日期 </td>
		<td >&nbsp;</td>
	  </tr>
	</thead>
	<tbody>

	 <c:forEach var="item" items="${theForm.results}" varStatus="status">
	 <c:set var="persons" value=""/>
	 <tr>
		<td align="center">
			<input type="hidden" name="oid" value="<c:out value = "${item.id}" />"/>
			<img src="<c:out value='${contextPath}'/>/image/common/<c:out value = '${item.status.description}' />" alt="<c:out value = '${item.status.name}' />"/>
		</td>
		<td >
			<c:if test = "${item.attachement == true}" >
			<img src="<c:out value='${contextPath}'/>/image/common/mail_attach.gif"/>
			</c:if>
			&nbsp;
		</td>
		<td >
			<c:forEach items = "${item.acceptPersons}" var = "item1" varStatus = "status1" >
				<c:set var="persons" value = "${item1.acceptPerson.chineseName}，${persons}" />
			</c:forEach>
			<div style="width:150px"  class="font_overflow" title="<c:out value="${persons}"/>">
				<c:out value="${persons}"/>
			</div>
		</td>
		<td >
			<c:out value = "${item.title}" />
		</td>
		<td class="news_list_time font_money">
			<fmt:formatDate value = '${item.sendDate}' pattern = 'yyyy-MM-dd HH:mm:ss' />
		</td>
		<td class="ListingLink">
			<input type="button" class="list_delete" title="删除" onclick="CurrentPage.remove('<c:out value = "${item.id}" />')"/>
		</td>
	 </tr>
	 </c:forEach>

	</tbody>
	</table>
	<div class="list_bottom"> 
		<c:set var = "paginater.forwardUrl" value = "${actionName}" scope = "page" />
		<%@ include file = "/decorators/paginater.jspf" %>
	</div>
	</div>

</div>
</form>
</body>
</html>

<script type="text/javascript">
var CurrentPage = {};

CurrentPage.remove = function(oid) {
	if (! confirm(" 删除的数据不能恢复, 您确定要删除吗 ? ")) {
		return;
	}
	FormUtils.post(document.forms[0], '<c:url value="${actionName}?step=remove&oid="/>'+ oid);
}

CurrentPage.query = function() {
    if (document.getElementsByName("paginater.page") != null) {
        document.getElementsByName("paginater.page").value = 0;
    }    
    FormUtils.post(document.forms[0], '<c:url value="${actionName}"/>');
}

//refactor tableSort.js
parent.panel.listFrame = parent.panel.dataArr[2][3];
function TableSort.dblClick(){
	if(parent.panel){
		var nextNum =  parseInt(parent.panel.selectId+1,10);
		var sid = TableSort.lastClick.cells[0].childNodes[0].value;
		var url = parent.panel.dataArr[nextNum][2]+"&oid="+sid;
		parent.panel.newPanel = new Array(nextNum,"",url);
		parent.panel.click(nextNum);
		parent.panel.newPanel = new Array();
	}
}
parent.panel.beforeClick = function(index){
	if(!TableSort.lastClick)
		return true;
	var sid  = TableSort.lastClick.cells[0].childNodes[0].value;
	var nextNum =  3;
	switch(index){
		case 3:
			var url = parent.panel.dataArr[nextNum][2]+"&oid="+sid;
			parent.panel.newPanel = new Array(nextNum,"",url);
			break;		
	}
	return true;
}
</script>