<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ include file = "/common/taglibs.jsp" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>系统消息</title>
<link rel="stylesheet"  href="<ww:url value = "'/styles/friendblog.css'" />">
</head>

<BODY>
<DIV id=gray-background>
  <DIV class=body>
    <H3 align="left">系统消息</H3>
    <DIV class=note-box>
      <DIV style="FLOAT: left; WIDTH:420px; PADDING-BOTTOM:10px;">
        <table width="98%" cellspacing="0" cellpadding="0">
          <tr>
            <td width="30%" nowrap><img src="<ww:url value = "'/images/success.gif'" />" width="84" height="83" title="消息"></td>
            <td width="70%" nowrap>
						<script>
							document.writeln(window.dialogArguments);
						</script>
            </td>
          </tr>
					 <tr>
            <td colspan="2" nowrap align="center">
							<input type="button" value=" 关  闭 " onClick="window.close()" class="button">
						</td>
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


