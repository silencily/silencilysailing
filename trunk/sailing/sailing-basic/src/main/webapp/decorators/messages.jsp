<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<script>
var PublicMessages = {};
PublicMessages.previousOnLoadHandler = document.body.onload;
document.body.onload = function () {
	eval(PublicMessages.previousOnLoadHandler());
	<c:forEach var="item" items="${requestScope['_com_coheg_messages_key_']}">
		Validator.successMessage('<c:out value = "${item}" escapeXml = "false" />');
	</c:forEach>	
	<c:forEach var="item" items="${requestScope['_com_coheg_warn_messages_key_']}">
		Validator.warnMessage('<c:out value = "${item}"  escapeXml = "false" />');
	</c:forEach>	
	<c:forEach var="item" items="${requestScope['_com_coheg_error_messages_key_']}">
		Validator.errorMessage('<c:out value = "${item}"  escapeXml = "false" />');
	</c:forEach>
}
</script>
