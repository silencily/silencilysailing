<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<html>
<head>
<jsp:directive.include file="/decorators/default.jspf" />

<title><c:out value="${theForm.tblufNews.title}"/> </title>
<style type=text/css>
<!--
.go_back{	
    background-image:url(<c:out value = "${initParam['publicResourceServer']}/image/main/click_goback.gif"/>);
	background-color:#ffffff;
	width:45px;
	height:16px;
    border:0px solid #fff;
	cursor:pointer;
}
#btnsave1,.btnsave1 {
	background-image: url(<c:out value = "${initParam['publicResourceServer']}/img/save.gif"/>);
	margin: 0px;
	padding: 0px;
	height: 22px;
	width: 60px;
	border-top-style: none;
	border-right-style: none;
	border-bottom-style: none;
	border-left-style: none;
  background-repeat:no-repeat; 
  background-color:transparent;
  cursor:pointer;
}
-->
</style>
</head>

<body>
<script type="text/javascript">
	window.focus();
	var msgInfo_ = new msgInfo();
	if (CurrentPage == null) {
		var CurrentPage = {};
	}
	CurrentPage.validation = function () {
		var feedback = document.getElementsByName("tblufNewsFdbk.feedback")[0].value.trim();
		var feedbackLength = mb_strlen(feedback);
		if(feedbackLength <= 0 || feedbackLength > 100) {
			Validator.clearValidateInfo();
			Validator.warnMessage(msgInfo_.getMsgById('UF_I018_C_4',['反馈内容',feedbackLength,1,100]));
			return false;
		}
		return true;
	}
	CurrentPage.submit = function () {
		if (!CurrentPage.validation()) {
			return;
		}
		FormUtils.post(document.forms[0], '<c:url value='/uf/desk/TblColumnDisplayAction.do?step=saveFdbk'/>');
	}
	CurrentPage.download = function(obj) {
	    FormUtils.post(document.forms[0], obj.href, false);
	    return false;
	}
	function mb_strlen(str) {
		var len = 0;
		for(var i = 0; i < str.length; i++) {
			len += str.charCodeAt(i) < 0 || str.charCodeAt(i) > 255 ? 2 : 1;
		}
		return len;
	}
</script>
<form name="f" action="" method="post">
<input type="hidden" name="oid"  value="<c:out value='${theForm.tblufNewsFdbk.id}'/>" />
<input type="hidden" name="id"  value="<c:out value='${theForm.tblufNews.id}'/>" />
<br>
<p>
	<h1>
		<center><c:out value="${theForm.tblufNews.title}"/> </center>
	</h1>
<p>
<center>
所属栏目：<c:out value="${theForm.tblufNews.tblUfColumn.columnNm}"/>&nbsp;
发布人：<c:out value="${theForm.tblufNews.publisherName}"/>&nbsp;
发布时间：<fmt:formatDate value="${theForm.tblufNews.publishTime}" pattern="yyyy-MM-dd HH:mm"/>&nbsp;
<c:if test="${theForm.tblufNews.author!=null}">
作者:<c:out value="${theForm.tblufNews.author}"/>&nbsp;
</c:if>
<c:if test="${theForm.tblufNews.source!=null}">
来源:<c:out value="${theForm.tblufNews.source}"/>
</c:if>
</center>
<hr>
<p>
<c:out value="${theForm.tblufNews.content}" escapeXml="false" /> 
</p>
<br>
<c:forEach var="item" items="${theForm.tblufNews.tblUfNewsAttach}" varStatus="status">
    <c:if test='${status.index == 0}'>
		<hr>
		相关附件:<br>
	</c:if>
	<a href="<c:url value="/uf/desk/TblColumnDisplayAction.do?step=download&savepath=${item.savePath }&filename=${item.fileName}"/>" onclick="return CurrentPage.download(this);"><c:out value="${item.fileName }"/></a><br>
</c:forEach>
<c:if test="${theForm.tblufNews.tblUfColumn.feedbackFlg.code=='1'}">
<hr>
	<center>
	<table border="0" cellpadding="0" cellspacing="0" width="100%" class="Detail">
	<tr><td align="center">反馈内容:</td></tr>
	<tr><td align="center">
		<textarea id="textArea" name="tblufNewsFdbk.feedback" rows="4" class="input_long"
		style="width:82%"><c:out value="${theForm.tblufNewsFdbk.feedback}"/></textarea>
	</td></tr>
	</table>
	<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr><td align="center"><input type="button" class="btnsave1" onclick="CurrentPage.submit()" value=" " /></td></tr>
	</table>
	</center>
<br>
</c:if>
<!--<p>-->
<!--	<center>-->
<!--	<input type=button  class="go_back" onclick="javascript:history.back()">-->
<!--	</center>-->
<!--<p>-->
</form>
</body>
</html>
