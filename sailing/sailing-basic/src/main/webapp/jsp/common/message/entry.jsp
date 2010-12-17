<%-- jsp1.2 --%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/common.jspf"/>
<script type="text/javascript" src = "<c:out value = "${initParam['publicResourceServer']}/public/scripts/panel.js" />"></script>

<body class="list_body" >	
<div class="main_title">
	<img src="<c:url value='/image/common/oa_email.gif'/>" alt="我的消息" align="middle" /> 消息管理
</div>
<div class="main_body">
	<div id="divId_panel">
		<!-- 在这里显示选项卡-->
	</div>	
</div>

<script type="text/javascript">
//定义选项卡
var arr = new Array();
var i = 0;
var def = 0;
<c:set var="detailUrl" value="/common/messageAction.do?step=detailAccept"/>
<c:if test="${!empty param.tab}">
	def = <c:out value="${param.tab}"/>;
	<c:set var="detailUrl" value="${detailUrl}&oid=${param.messageId}"/>
	<%-- 这个方式设置 default.jsp 中关闭消息 
    top.msgObj.windowClose();
	--%>
</c:if>
arr[i++] = ["接受消息列表", "", "<c:url value = '/common/messageAction.do?step=listAccept&paginater.page=0' />"];
arr[i++] = ["消息明细", "", "<c:url value = '${detailUrl}' />"];
arr[i++] = ["发送消息列表", "", "<c:url value = '/common/messageAction.do?step=listSend&paginater.page=0' />"];
arr[i++] = ["发送消息", "", "<c:url value = '/common/messageAction.do?step=info' />"];

var panel = new Panel.panelObject("panel", arr, def, 
	"<c:out value = "${initParam['publicResourceServer']}/image/main/" />");
document.getElementById("divId_panel").innerHTML = panel.display();
</script>
</body>
</html>


