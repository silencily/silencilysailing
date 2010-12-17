<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf"/>
<html>
<head>
<title>用户选择列表</title>
<script type="text/javascript">
var wind=window.dialogArguments;
loadok = function()
{  if(wind!=""){ 
	document.getElementById("windowFrame").src=wind;
    wind="";
   }
}
</script>
</head>
<body class="list_body">
<iframe id="windowFrame" onload="loadok()" src="" frameborder="0" width="100%" height="100%"/>
</body>
</html>
 