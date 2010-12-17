<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ include file="/common/taglibs.jsp"%>

<jsp:useBean id="currentPageNumber" type="java.lang.Integer" scope="request"/>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">

<div class="listbox_page">

	<pg:first export="firstPageUrl=pageUrl" unless="current">
		<a class="page" href="javascript:MM_page('<%= firstPageUrl %>')"><img src="<c:url value= '/public/icon/first.gif' />" border="0" alt="首页" align="absmiddle"></a>
	</pg:first>
	
	<!--
	<pg:skip export="skipBackPageUrl=pageUrl" pages="<%= -10 %>">
		<a href="javascript:MM_page('<%= skipBackPageUrl %>')">上10页</a>
	</pg:skip>
	-->
	
	<pg:prev export="prevPageUrl=pageUrl">
		<a class="page" href="javascript:MM_page('<%= prevPageUrl %>')"><img src="<c:url value= '/public/icon/previous.gif' />" border="0" alt="上页" align="absmiddle"></a>
	</pg:prev>
	
	<pg:pages>
	<%
		if (pageNumber == currentPageNumber) {
	%> 
		<span class="page"><b><%= pageNumber %></b></span>
	<%
		} else {
	%> 
		<a class="page" href="javascript:MM_page('<%= pageUrl %>')"><%= pageNumber %></a> 
	<%
		}
	%>
	</pg:pages>
	
	<pg:next export="nextPageUrl=pageUrl">
		<a class="page" href="javascript:MM_page('<%= nextPageUrl %>')"><img src="<c:url value= '/public/icon/next.gif' />" border="0" alt="下页" align="absmiddle" ></a>
	</pg:next>
	
	<!--
	<pg:skip export="skipForwardPageUrl=pageUrl" pages="<%= 10 %>">
		<a href="javascript:MM_page('<%= skipForwardPageUrl %>')">下10页</a>
	</pg:skip>
	-->
	
	<pg:last export="lastPageUrl=pageUrl" unless="current">
		<a class="page" href="javascript:MM_page('<%= lastPageUrl %>')"><img src="<c:url value= '/public/icon/last.gif' />" border="0" alt="末页" align="absmiddle"></a>
	</pg:last>
	
	共 <span class="page_sum"><c:out value = '${param.totalPageNumber}' /></span> 页  <span class="page_sum"><c:out value = '${param.totalCount}' /></span> 条记录

</div>

<!--
每页显示<input type="text" name="maxPageItems" size="5">条记录
-->

<script language="javascript">
function MM_page(url)
{
	if (document.forms[0])
	{
		document.forms[0].target = '_self';
		document.forms[0].method = 'post';
		document.forms[0].action = url;
		document.forms[0].submit();
	}
	else
	{
		location.href = url;
	}

}
</script>

