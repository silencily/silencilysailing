
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ include file = "/decorators/tablibs.jsp" %>

msgObj.getMessage = function(){
	var arr = new Array();
	var temp;
	<c:forEach var="item" items="${theForm.results}" varStatus="status">
		temp        = new this.message();
		temp.id     = '<c:out value = "${item.message.id}" />';
		temp.sender = '<c:out value = "${item.message.author.chineseName}" />';
		temp.title  = '<c:out value = "${item.message.title}" />';		
		temp.link   = '<c:url value="/common/messageAction.do?step=entry&tab=1&messageId="/>'+temp.id;
		temp.time   = '<fmt:formatDate value = '${item.message.sendDate}' pattern = 'yyyy-MM-dd HH:mm:ss' />';
		temp.more   = '<c:url value="/common/messageAction.do?step=entry"/>';
		arr[<c:out value = "${status.index}" />] = temp;
	</c:forEach>
	return arr;
}
msgObj.windowOpen();