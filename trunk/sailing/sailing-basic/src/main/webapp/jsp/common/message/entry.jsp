<%-- jsp1.2 --%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/common.jspf"/>
<script type="text/javascript" src = "<c:out value = "${initParam['publicResourceServer']}/public/scripts/panel.js" />"></script>

<body class="list_body" >	
<div class="main_title">
	<img src="<c:url value='/image/common/oa_email.gif'/>" alt="�ҵ���Ϣ" align="middle" /> ��Ϣ����
</div>
<div class="main_body">
	<div id="divId_panel">
		<!-- ��������ʾѡ�-->
	</div>	
</div>

<script type="text/javascript">
//����ѡ�
var arr = new Array();
var i = 0;
var def = 0;
<c:set var="detailUrl" value="/common/messageAction.do?step=detailAccept"/>
<c:if test="${!empty param.tab}">
	def = <c:out value="${param.tab}"/>;
	<c:set var="detailUrl" value="${detailUrl}&oid=${param.messageId}"/>
	<%-- �����ʽ���� default.jsp �йر���Ϣ 
    top.msgObj.windowClose();
	--%>
</c:if>
arr[i++] = ["������Ϣ�б�", "", "<c:url value = '/common/messageAction.do?step=listAccept&paginater.page=0' />"];
arr[i++] = ["��Ϣ��ϸ", "", "<c:url value = '${detailUrl}' />"];
arr[i++] = ["������Ϣ�б�", "", "<c:url value = '/common/messageAction.do?step=listSend&paginater.page=0' />"];
arr[i++] = ["������Ϣ", "", "<c:url value = '/common/messageAction.do?step=info' />"];

var panel = new Panel.panelObject("panel", arr, def, 
	"<c:out value = "${initParam['publicResourceServer']}/image/main/" />");
document.getElementById("divId_panel").innerHTML = panel.display();
</script>
</body>
</html>


