<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>系统消息</title>
<link rel="stylesheet"  href="<c:out value = "${initParam['staticResourceServer']}/css/friendblog.css" />">
</head>

<BODY>
<DIV id=gray-background>
  <DIV class=body>
    <H3 align="left">系统消息</H3>
    <DIV class=note-box>
      <DIV style="FLOAT: left; WIDTH:420px; PADDING-BOTTOM:10px;">
        <table width="100%" cellspacing="0" cellpadding="0">
          <tr>
            <td width="30%" nowrap><img src="<c:out value = "${initParam['staticResourceServer']}/image/success.gif" />" width="84" height="83" title="消息"></td>
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
