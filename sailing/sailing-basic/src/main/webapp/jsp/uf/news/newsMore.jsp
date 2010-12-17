<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf"/> 
<html>
<body>
<script language="javascript">
	var CurrentPage = {};
	/**---------------------------------------------------------------------
	| 单个删除操作->点表格最后的删除按钮时执行的操作
	----------------------------------------------------------------------*/
	var msgInfo_ = new msgInfo();
	if (CurrentPage == null) {
   		var CurrentPage = {};
	}

	/**---------------------------------------------------------------------
	| 新增操作->通过点顶部的“新增”按钮实现。
	----------------------------------------------------------------------*/
	CurrentPage.query = function() {
		if (document.getElementsByName("paginater.page") != null) {
	    	document.getElementsByName("paginater.page").value = 0;
			}

		FormUtils.post(document.forms[0], '<c:url value="/uf/news/NewsPublishAction.do?step=${theForm.step}"/>');
	}
	/*-----------------------------------------------------------------------------------
	|函数命  ：Global.afterOnload()
	|函数功能：页面加载完成后进行的处理(如果列表为空，则自动跳转到新增)。
	|----------------------------------------------------------------------------------*/
	Global.afterOnload = function(){
//	    var tbl  = listtable;
//    	if (tbl.rows.length < 2){ //表头算一行
//    	   CurrentPage.create();
//    	}
	}
</script>
<form>
<input type="hidden" name="step" value="<c:out value="${theForm.step}"/>"/>
<div class="main_title">
	<div>信息浏览</div>
</div>
<jsp:directive.include file="newsSearch.jspf"/>
<div class="update_subhead">
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td>
				<span class="switch_open" onClick="StyleControl.switchDiv(this, $('divId_scrollLing'))"  title="点击收缩表格">信息列表</span>
			</td>
			<td align="right">			 
			</td>
		</tr>
	</table>
</div>
<div id="divId_scrollLing" class="list_scroll">
	<table border="0" cellpadding="0" cellspacing="0" class="Listing" id="listtable"  onClick="TableSort.sortColumn(event)">
		<thead>
			<tr>
				<td nowrap="nowrap" width="10%" align="center">所属栏目</td>
				<td nowrap="nowrap" width="35%" align="center">标题</td>
				<td nowrap="nowrap" width="20%" align="center">关键字</td>
				<td nowrap="nowrap" width="10%" align="center">发布时间</td>
				<td nowrap="nowrap" width="10%" align="center">有效期</td>
				<td nowrap="nowrap" width="15%" align="center">反馈内容</td>
			</tr>
		</thead>
		<tbody>
		<c:forEach var="item" items="${theForm.list}" varStatus="status">		
			<tr>
				<td nowrap="nowrap" align="center">
					<input type="hidden" name="oid" value="<c:out value="${item.id}"/>"/>
					<c:out value="${item.tblUfColumn.columnNm}"/>
				</td>
				<td nowrap="nowrap" align="left">
					<a href = "#" onclick="window.open('<c:url value="/uf/desk/TblColumnDisplayAction.do?step=detail&id=${item.id}" />','newsWindow','resizable=yes,scrollbars=yes')";><c:out value='${item.title}'/></a>
					&nbsp;
				</td>
				<td nowrap="nowrap" align="left"><c:out value="${item.keyword}"/>&nbsp;</td>
				<td nowrap="nowrap" align="center">
					&nbsp;<fmt:formatDate value="${item.publishTime}" pattern="yyyy-MM-dd HH:mm"/>&nbsp;
				</td>
				<td nowrap="nowrap" align="center">
					&nbsp;<fmt:formatDate value="${item.invalidTime}" pattern="yyyy-MM-dd"/>&nbsp;
				</td>
				<td nowrap="nowrap" align="left">
                    <c:out value='${item.myFeedback}'/>&nbsp;
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table> 
	<div class="list_bottom">
	<c:set var = "paginater.forwardUrl" value = "/uf/news/NewsPublishAction.do?step=more" scope = "page" />
	<%@ include file = "/decorators/paginater.jspf" %>
	</div>
</div>
</form>
</body>
</html>
