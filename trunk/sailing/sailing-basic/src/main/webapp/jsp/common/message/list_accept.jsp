
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
			发送给我的消息列表
		</span>
		
		<img src='<c:out value="${imgSpace}"/>'/>
		<img src='<c:out value="${imgSpace}"/>'/>
		<c:forEach var="item1" items="${theForm.messageStatus}" varStatus="status1">
		<a href="<c:url value='/common/messageAction.do?step=listAcceptForStatus&status=${item1.status.code}'/>">
			<img src="<c:out value='${contextPath}'/>/image/common/<c:out value='${item1.status.description}'/>" alt="<c:out value='${item1.status.name}'/>"/> 
			<c:out value='${item1.status.name}'/><c:out value='${item1.num}'/>条</a>
			<img src='<c:out value="${imgSpace}"/>'/>
		</c:forEach>
		
	</div>

	<div id="divId_scrollLing">
	<table border="0" cellpadding="0" cellspacing="0" class="Listing" id="tabId_listing" onClick="TableSort.sortColumn(event)">
	<thead>
	  <tr>
		<td width=5%>&nbsp;</td>
		<td width=5%>附件</td>
		<td width=8%>类型</td>
		<td width=45%>标题 </td>
		<td width=10%>发送人 </td>
		<td width=15%>日期 </td>
		<td >&nbsp;</td>
	  </tr>
	</thead>
	<tbody>

	 <c:forEach var="item" items="${theForm.results}" varStatus="status">
	 <tr>
		<td align="center">
			<input type="hidden" name="mid" value="<c:out value = "${item.message.id}" />"/>
			<img src="<c:out value='${contextPath}'/>/image/common/<c:out value = '${item.status.description}' />" alt="<c:out value = '${item.status.name}' />"/>
		</td>
		<td >
			<c:if test = "${item.message.attachement == true}" >
			<img src="<c:out value='${contextPath}'/>/image/common/mail_attach.gif"/>
			</c:if>
			&nbsp;
		</td>
		<td ><c:out value = "${item.message.type.name}" />&nbsp;</td>
		<td >
			<c:out value = "${item.message.title}" />
		</td>
		<td ><c:out value = "${item.message.author.chineseName}" /></td>
		<td class="news_list_time font_money">
			<fmt:formatDate value = '${item.message.sendDate}' pattern = 'yyyy-MM-dd HH:mm:ss' />
		</td>
		<td class="ListingLink">
			<input type="button" class="list_delete" title="删除" onclick="CurrentPage.remove('<c:out value = "${item.message.id}" />')"/>
		</td>
	 </tr>
	 </c:forEach>

	</tbody>
	</table>
  <div class="list_bottom"> 
		<c:set var = "paginater.forwardUrl" value = "/common/messageAction.do" scope = "page" />
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
	FormUtils.post(document.forms[0], '<c:url value="${actionName}?step=removeAccept&oid="/>'+ oid);
}

CurrentPage.query = function() {
    if (document.getElementsByName("paginater.page") != null) {
        document.getElementsByName("paginater.page").value = 0;
    }    
    FormUtils.post(document.forms[0], '<c:url value="${actionName}"/>');
}

//refactor tableSort.js
parent.panel.listFrame = parent.panel.dataArr[0][3];
function TableSort.dblClick(){
	if(parent.panel){
		var url = setPanelUrl();
		parent.panel.newPanel = new Array(1,"",url);
		parent.panel.click(1);
	}
}
parent.panel.beforeClick = function(index){
	if(!TableSort.lastClick)
		return true;
	switch(index){
		case 1:
			var url = setPanelUrl();
			parent.panel.newPanel = new Array(1,"",url);
			break;		
	}
	return true;
}

function setPanelUrl(){
	var sid  = TableSort.lastClick.cells[0].childNodes[0].value;
	var nextNum = 1;
	var url = parent.panel.dataArr[nextNum][2]
		url = url.split("&oid=")[0];
	parent.panel.dataArr[nextNum][2] = url;
	url = url + "&oid="+sid;
	return url;
}
</script>