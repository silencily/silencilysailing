<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%><%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %><%--
	��ʽ�������ļ����ӽڵ�
	��ʾָ������id�¸�����µ��ӽڵ�
	������û�ʽ���ط�ʽ��ÿ�ζ�ȡһ���ڵ����ӽڵ㡣
	Ĭ������£����ڵ㲻��ѡ��
	treeType ��ѡ=1����ѡ=2����ѡ=��
	SelectNodeId Ĭ��ѡ�еĽڵ�
--%><c:forEach var="item" items="${theForm.codeList}" varStatus="status"><c:set var ="flag" value="|||"/><c:if test="${status.index == 0}"><c:set var ="flag" value=""/></c:if><c:set var ="isFather" value="0"/><c:if test="${item['NODETYPE'] == '0'}"><c:set var ="isFather" value="1"/></c:if><c:out value="${flag}"/><c:out value="${item['NODEID']}"/>###<c:out value="${item['NODENAME']}"/>###<c:out value="${isFather}"/>###<c:out value="${item['ID']}"/></c:forEach>