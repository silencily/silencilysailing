<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf"/>
<html">
<head>
<title>输入用户名和口令</title>

</head>
<body>
	<table id="c" width="100%" border="0" cellspacing="0" cellpadding="0" class="Detail">

		<tr>
		  <td class="attribute">用户名</td>
		  <td><input name="username" type="text" class="input_text" value=""/></td>
		  </tr>
		  <tr>
		  <td class="attribute">密码</td>
		  <td><input name="password" type="text" class="input_text" value=""/></td>
	    </tr>
		<tr>
          <td align="right"><input type="button" name="Submit3" value="确定" onclick="returnOk()"/></td>
		  <td><input type="button" name="Submit4" value="取消" onclick="window.returnValue='cancel';window.close()"/></td>
	  </tr>
	</table>
<script type="text/javascript">
returnOk = function()
{
	var base = '<c:out value="${param.elementBaseName}"/>'
	var name_e = "password";
	var pwd_e = "username";
	if(base&&base!='null'&&base!='undefined')
	{
	 name_e= base+'.'+name_e;
	 pwd_e= base+'.'+pwd_e;
	 }
	var nameObj = parent.document.getElementsByName(name_e)[0];
	if(nameObj)
		nameObj.value = document.getElementsByName("username")[0].value;
	var pwdObj = parent.document.getElementsByName(pwd_e)[0];
	if(pwdObj)
		pwdObj.value = document.getElementsByName("password")[0].value;
	window.returnValue = "ok";
	window.close();	
	return;	
}
</script>
</body>
</html>
