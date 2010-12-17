<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ page import = "com.opensymphony.xwork.ActionContext" %>
<%@ page import = "com.coheg.framework.dao.PaginationSupport" %>
<%@ page import = "com.coheg.framework.util.RequestHelper" %>
<%@ include file="/common/taglibs.jsp"%>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">


<%		
	// Note : the action use this page must implements com.coheg.framework.web.webwork.Paginationable interface 
	Integer totalCountWrapper = (Integer) ActionContext.getContext().getValueStack().findValue("paginationSupport.totalCount");
	Integer maxPageItemsWrapper = (Integer) ActionContext.getContext().getValueStack().findValue("paginationSupport.maxPageItems");
	Integer maxIndexPagesWrapper = (Integer) ActionContext.getContext().getValueStack().findValue("paginationSupport.maxIndexPages");
	String index = (String) ActionContext.getContext().getValueStack().findValue("paginationSupport.index");
	
	int totalCount = totalCountWrapper == null ? 0 : totalCountWrapper.intValue();
	int maxPageItems = maxPageItemsWrapper == null ? PaginationSupport.DEFAULT_MAX_PAGE_ITEMS : maxPageItemsWrapper.intValue();
	int maxIndexPages = maxIndexPagesWrapper == null ? PaginationSupport.DEFAULT_MAX_INDEX_PAGES : maxIndexPagesWrapper.intValue();
	index = index == null ? PaginationSupport.DEFALUT_INDEX : index;

	int totalPageNumber = ( totalCount % maxPageItems == 0 ) ? ( totalCount / maxPageItems ) : ( totalCount / maxPageItems + 1 );
	

	StringBuffer rootPathBuffer = new StringBuffer();
	rootPathBuffer.append(request.getScheme());
	rootPathBuffer.append("://");
	rootPathBuffer.append(request.getServerName());
	rootPathBuffer.append(":");
	rootPathBuffer.append(request.getServerPort());
	rootPathBuffer.append(request.getContextPath());
	rootPathBuffer.append("/");
	String rootPath = rootPathBuffer.toString();
	
	//必须传进来的参数, 如果不传, 将定位到应用根路径
	String pageForwardUrl = RequestHelper.getParam(request, "pageForwardUrl", rootPath);
%>

<pg:pager
	items = "<%= totalCount %>"
	index = "<%= index %>"
	url = "<%= pageForwardUrl%>"
	maxPageItems = "<%= maxPageItems %>"
	maxIndexPages = "<%= maxIndexPages %>"
	isOffset = "<%= true %>"
	export = "offset, currentPageNumber=pageNumber"
	scope = "request" >
	
	<%-- save pager offset during form changes --%>
	<input type="hidden" name="pager.offset" value="<%= offset %>">
	
	<pg:index>
		<jsp:include page = '/common/texticon.jsp' flush = 'true'>
			<jsp:param name = 'totalCount' value = '<%= totalCount%>' />
			<jsp:param name = 'totalPageNumber' value = '<%= totalPageNumber%>' />
		</jsp:include>
	</pg:index>

</pg:pager>






