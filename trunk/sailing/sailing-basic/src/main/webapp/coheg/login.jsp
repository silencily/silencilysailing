
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page language="java"  contentType="text/html;charset=gb2312" pageEncoding = "GBK" %>
<%@ include file="/common/taglibs.jsp"%>

<%@ page import = "org.acegisecurity.ui.webapp.AuthenticationProcessingFilter" %>
<%@ page import = "net.silencily.sailing.security.acegi.ui.webapp.InternetSupportAuthenticationProcessingFilterEntryPoint" %> 

<html>
<head>
<title> - 欢迎使用 发电企业资源管理套件解决方案 - 登录页面 - -</title>

<script type="text/javascript"  src="<c:url value="/coheg/script/general.js"/>"></script>
<script type="text/javascript"  src="<c:url value="/coheg/script/global.js"/>"></script>
<script type="text/javascript"  src="<c:url value="/coheg/script/validator.js"/>"></script> 
	
<style type="text/css">
<!--
body  {
	font: 100% Verdana, Arial, Helvetica, sans-serif;
	background: #ffffff;
	margin: 0; /* it's good practice to zero the margin and padding of the body element to account for differing browser defaults */
	padding: 0;
	text-align: center; /* this centers the container in IE 5* browsers. The text is then set to the left aligned default in the #container selector */
	color: #000000;
}
#pagelogin {
	background-color: #C9DFF9;
	width: 100%;
	background-image: url(<c:url value="/images/pagebg.gif" />);
	height: 600px;
	font-family: Verdana, Arial, Helvetica, sans-serif;
	font-size: 12px;
	float: left;
}
#left {
	background-color: #E6F1FE;
	width: 540px;
	float: left;
}
#contentlogin {
	margin-top: 150px;
	height: 394px;
}
#right {
	float: left;
	height: 356px;
	width: 338px;
	background-image: url(<c:url value="/images/rightlogin.gif" />);
	background-repeat: no-repeat;
	text-align: left;
}
#loginbox {
	margin-top: 140px;
	margin-left: 110px;
	height: 160px;
	width: 180px;
	text-align: left;
}
#boxusername {
	float: left;
}
#pw {
	float: left;
	margin-top: 12px;
}
#notice {
	float: left;
	margin-top: 13px;
	width: 150px;
	color: #FF6600;
	background-repeat: no-repeat;
	padding-left: 0px;
	background-position: 1px 0px;
	line-height: 22px;
}
#buttons {
	float: left;
	margin-top: 12px;
	width: 180px;
	color: #FF6600;
}
#copyright {
	margin-top: 40px;
	font-size: 11px;
	text-align: center;
	float: left;
	width: 100%;
}
#mid {
	height: 394px;
	margin-right: auto;
	margin-left: auto;
	width: 100%;
	margin-top: 100px;
}
.inputline {
	border: 1px solid #6699CC;
	height: 14px;
	width: 140px;
	float: left;
	line-height: 14px;
}
.btnlogin {
	background-image: url(<c:url value="/images/btnlogin.gif" />);
	margin: 0px;
	padding: 0px;
	height: 23px;
	width: 58px;
	border-top-width: 0px;
	border-right-width: 0px;
	border-bottom-width: 0px;
	border-left-width: 0px;
	border-top-style: none;
	border-right-style: none;
	border-bottom-style: none;
	border-left-style: none;
}
.btnreset {
	background-image: url(<c:url value="/images/btnreset.gif" />);
	margin: 0px;
	padding: 0px;
	height: 23px;
	width: 58px;
	border-top-width: 0px;
	border-right-width: 0px;
	border-bottom-width: 0px;
	border-left-width: 0px;
	border-top-style: none;
	border-right-style: none;
	border-bottom-style: none;
	border-left-style: none;
}
#copyright a {
	color: #345CB9;
}

#c {
	margin-right: auto;
	margin-left: auto;
	width: 878px;
}
.style1 {font-size: 11px}

-->
</style>
</head>

<script type="text/javascript">
<!--
function initPage(){	
	eval(customaryOnload());	
	document.forms[0].j_username.focus();
	//加入验证,验证类参考validator.js
	var userObj = document.forms[0].j_username;
	var passObj = document.forms[0].j_password;
	userObj.dataType = "Require";
	userObj.msg = "用户名必须填写！";	
	userObj.maxlength = "20";
	passObj.dataType = "Require";
	passObj.msg = "密码必须填写！";
}
//重载于validator.js
function alert_extend(info){
	var txt = document.getElementById("span_validate");
	txt.innerHTML = info;
	txt.className = "errorInfor"
}

try{
	//调节页面高度和宽度
	window.moveTo(0,0);
	window.resizeTo(screen.availWidth,screen.availHeight);
}catch(e){}

//如果页面使用 onload，都要使用如下形式才能保证前一个 onload 不会覆盖。
var customaryOnload = window.onload;
window.onload  = initPage;
//-->

</script>

<body BGCOLOR=#FFFFFF LEFTMARGIN=0 TOPMARGIN=0 MARGINWIDTH=0 MARGINHEIGHT=0>
	
<form action="<c:url value="/j_security_check.auth" />" method="POST" onsubmit="return Validator.Validate(document.forms[0],5)" target="_top" >
<%
	String serviceCallBack = request.getParameter(InternetSupportAuthenticationProcessingFilterEntryPoint.SERVICE_CALLBACK_URL_PARAM);
	if (serviceCallBack == null) {
		serviceCallBack = "";
	}
%>
<input type="hidden" name="<%= InternetSupportAuthenticationProcessingFilterEntryPoint.SERVICE_CALLBACK_URL_PARAM%>"  value="<%= serviceCallBack %>" />
<%
	String lastUserName = "";
	String errMessage = "&nbsp;";
%>
<c:if test="${not empty param.login_error}">
<%
	lastUserName = (String) session.getAttribute(AuthenticationProcessingFilter.ACEGI_SECURITY_LAST_USERNAME_KEY);
	if (lastUserName == null) {
		lastUserName = "";
	}
	String errType = request.getParameter("type");
	if ("1".equals(errType)) {
		errMessage = "该用户不存在!";
	} else if ("2".equals(errType)){
		errMessage = "该用户已被锁住,请联系系统管理员";
	} else if ("3".equals(errType)){
		errMessage = "数据库链接失败!请联系系统管理员";
	} else if ("4".equals(errType)){
		errMessage = "该用户已被禁用,请联系系统管理员";
	} else {
		String errTimes = request.getParameter("times");
	    if (errTimes == null || errTimes.equals("")) {
			errMessage = "密码错误!请输入用户和密码";
	    } else if (errTimes.equals("0")){
			errMessage = "该用户已被锁住,请联系系统管理员";
	    } else {
			errMessage = "密码错误!您还可以尝试" + errTimes + "次";
	    }
	}
%>
</c:if>
<div id="pagelogin">
	<div id="mid">
		<div id="c">
			<div id="left"><img src="<c:url value="/images/leftlogin.gif" />" WIDTH=540 HEIGHT=356 ALT="" ></div>
			<div id="right">
      			<div id="loginbox">
 	     			<div id="boxusername">
						<input name="j_username" type="text" class="inputline" id="textfield" value="<%= lastUserName%>" />
					</div>
					<div id="pw">
              			<input name="j_password" type="password" class="inputline" id="textfield" />
              		</div>
              		<div id="notice"><span id='span_validate'><%=errMessage%></span></div>
              		<div id="buttons">
              			<input name="button" type="submit" class="btnlogin" id="button" value=" ">
              			<input name="button2" type="reset" class="btnreset" id="button2" value=" ">
              		</div>
              	</div>
             </div>
         </div>
         <div id="copyright">Copyright (c) 2007 Qware Corpotation,All rights Reserved. &nbsp;&nbsp;&nbsp; <a href="http://www.qware.com">Contact Us</a></div>
     </div>
 </div>
 <input type="hidden" name="<%= net.silencily.sailing.security.acegi.ui.rememberme.SameDomainTokenBasedRememberMeService.DEFAULT_PARAMETER %>" value="true">
 <input type="hidden" name="<%= net.silencily.sailing.security.acegi.ui.rememberme.SameDomainTokenBasedRememberMeService.DEFAULT_TOKEN_VALIDITY_SECONDS_PARAMETER %>" value="-1"/> 
</form>
</body>
</html>