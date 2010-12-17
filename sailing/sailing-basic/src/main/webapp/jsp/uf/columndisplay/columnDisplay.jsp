<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<html>
<head>
<jsp:directive.include file="/decorators/default.jspf" />

</head>

<body>
<table border="0" height="240">
<tr height="20"><td>
<h1>
	<c:out value="${theForm.displayNo}"/> - <c:out value="${theForm.displayTitle}"/>
</h1>
</td></tr>
<tr height="210"><td>
<c:forEach var="item" items="${theForm.listDesktop}" varStatus="status">
     <c:if test='${status.index < 10}'>
     	<c:out value='${status.index + 1 }'/>.<a href = "<c:url value="/uf/desk/TblColumnDisplayAction.do?step=detail&id=${item.id}" />" target=_blank><c:out value='${item.title}'/></a><br>
	</c:if>
</c:forEach>
</td></tr>
<tr  height="10"><td>
<div align="right"><a href="<c:url value="/uf/desk/TblColumnDisplayAction.do?step=listlanmu&sortno=${theForm.sortno}&flag=more" />" target=_blank>¸ü¶à...</a></div>
</td></tr>
</table>
</body>
</html>