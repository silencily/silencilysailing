<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<html>
<head>
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
a:hover {color:blue;}
-->
</style>
</head>
	<jsp:directive.include file="/decorators/default.jspf" />
	
	<body>
<!--	    <p>-->
<!--		<h1>-->
<!--			<c:out value="${theForm.displayNo}" />-->
<!--			--->
<!--			<c:out value="${theForm.displayTitle}" />-->
<!--		</h1>-->
	    <div class="update_subhead">
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td>
						 <span class="switch_open" onclick="StyleControl.switchDiv(this,$('listtable'))" title="�����ڵ�">�б�</span>
					</td>
				</tr>
			</table>
		</div>
		<div id="divId_scrollLing" class="list_scroll">
			<table border="0" cellpadding="0" cellspacing="0" class="Listing" id="listtable"  onClick="TableSort.sortColumn(event)">
				<thead>
					<tr>
						<td>���</td>
						<td>����</td>
						<td>�����</td>
						<td>������</td>
						<td>�Ƿ���Ҫ����</td>
						<td>����ʱ��</td>
					</tr>
				</thead>
				<tbody>
				<c:forEach var="item" items="${theForm.listDesktop}"
					varStatus="status">
					<tr>
					 <td align="left"><c:out value='${status.index + 1 }' />.</td>
					 <td align="center"> <a
						href="<c:url value="/uf/desk/TblColumnDisplayAction.do?step=detail&id=${item.id}" />"
						><c:out value='${item.title}' /> </a> 
					 </td>
					 <td align="center"><c:out value="${item.author}"/></td>
					 <td align="center">
					     <c:choose>
					        <c:when test="${item.imptLvl==1}">
					            <c:out value="��"/>
					        </c:when>
					        <c:when test="${item.imptLvl==2}">
					            <c:out value="��"/>
					        </c:when>
					        <c:when test="${item.imptLvl==3}">
					            <c:out value="��"/>
					        </c:when>
					        <c:otherwise>
					    		<c:out value="δ֪"/>
					        </c:otherwise>
					    </c:choose>
					 </td>
					 <td align="center">
						 <c:choose>
					        <c:when test="${item.feedbackFlg==0}">
								<c:out value="��"/>
					        </c:when>
					        <c:when test="${item.feedbackFlg==1}">
								<c:out value="��"/>
					        </c:when>
					        <c:otherwise>
								<c:out value="��"/>
					        </c:otherwise>				        
						</c:choose>
					 </td>
					 <td align="center">
						<c:out value="${item.spublishTime}"/>
				     </td>
				    </tr>
				</c:forEach>
				</tbody>
			</table> 
			<div class="list_bottom">

			</div>
		</div>
		</br>		
	    <p>
		<center><input type=button class="go_back" onclick="javascript:history.back()"></center>
	</body>
</html>