<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ include file = "/common/taglibs.jsp" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>ϵͳ��Ϣ</title>
<link rel="stylesheet"  href="<ww:url value = "'/styles/friendblog.css'" />">
</head>

<BODY>
<DIV id=container> </DIV>
<DIV id=toptabsdiv> </DIV>
</DIV>
<DIV id=gray-background>
  <DIV class=body>
    <H3 align="left">ϵͳ��Ϣ</H3>
    <DIV class=note-box>
      <DIV style="FLOAT: left; WIDTH:420px; PADDING-BOTTOM:10px;">
        <table width="98%" cellspacing="0" cellpadding="0">
          <tr>
            <td width="30%" nowrap><img src="<ww:url value = "'/images/exception.gif'" />" width="84" height="83" title="��Ϣ"></td>
            <td width="70%" nowrap> ϵͳ����������������³��ԣ� <a href="javascript:history.go(-1);">�����ﷵ����һҳ</a>
							<br> ������Ϣ : 
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
