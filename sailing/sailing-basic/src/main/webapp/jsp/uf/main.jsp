<!--

-->
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
<title> - 发电企业信息管理系统 - </title>

</head>
	  <frameset rows="*" cols="180,8,*" framespacing="0" frameborder="0" border="0" id="buttonFrame">
			<frame src="<c:out value="${initParam['publicResourceServer']}/uf/funTree/DisplayFunTreeAction.do?step=display"/>" name="leftFrame" noresize="true" scrolling="yes" id="leftFrame" noResize/>
			<frame src="<c:out value="${initParam['publicResourceServer']}/jsp/uf/frame_midden.jsp"/>"  name="middenFrame" scrolling="no" noResize/>
			<frame src="<c:out value="${initParam['publicResourceServer']}${theForm.mainUrl}"/>" name="mainFrame" id="mainFrame" scrolling="yes" noResize/>
	  </frameset>
<noframes>

</noframes>
<body>

</body>

</html>
