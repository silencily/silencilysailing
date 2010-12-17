<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ include file = "/common/taglibs.jsp" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>系统消息</title>
<link rel="stylesheet"  href="<ww:url value = "'/styles/friendblog.css'" />">
</head>

<BODY>
<DIV id=container> </DIV>
<DIV id=toptabsdiv> </DIV>
</DIV>
<DIV id=gray-background>
  <DIV class=body>
    <H3 align="left">系统消息</H3>
    <DIV class=note-box>
      <DIV style="FLOAT: left; WIDTH:420px; PADDING-BOTTOM:10px;">
        <table width="98%" cellspacing="0" cellpadding="0">
          <tr>
            <td width="30%" nowrap><img src="<ww:url value = "'/images/exception.gif'" />" width="84" height="83" title="消息"></td>
            <td width="70%" nowrap> 系统发生意外错误，请重新尝试！ <a href="javascript:history.go(-1);">点这里返回上一页</a>
							<br> 错误信息 : 
              <%-- Exception Message --%>
              <c:out value = '${com_coheg_webwork_exception}' />
            </td>
          </tr>
        </table>
      </DIV>
      <BR style="CLEAR: both">
    </div>
  </DIV>
</DIV>
</DIV>
</BODY>
</HTML>
