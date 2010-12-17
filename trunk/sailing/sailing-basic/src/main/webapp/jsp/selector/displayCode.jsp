<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%><%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %><%--
	缓式加载树的加载子节点
	显示指定代码id下根层次下的子节点
	这里采用缓式加载方式，每次都取一个节点下子节点。
	默认情况下，根节点不可选择
	treeType 单选=1，多选=2，不选=空
	SelectNodeId 默认选中的节点
--%><c:forEach var="item" items="${theForm.codeList}" varStatus="status"><c:set var ="flag" value="|||"/><c:if test="${status.index == 0}"><c:set var ="flag" value=""/></c:if><c:set var ="isFather" value="0"/><c:if test="${item['NODETYPE'] == '0'}"><c:set var ="isFather" value="1"/></c:if><c:out value="${flag}"/><c:out value="${item['NODEID']}"/>###<c:out value="${item['NODENAME']}"/>###<c:out value="${isFather}"/>###<c:out value="${item['ID']}"/></c:forEach>