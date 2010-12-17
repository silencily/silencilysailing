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
						 <span class="switch_open" onclick="StyleControl.switchDiv(this,$('listtable'))" title="伸缩节点">列表</span>
					</td>
				</tr>
			</table>
		</div>
		<div id="divId_scrollLing" class="list_scroll">
			<table border="0" cellpadding="0" cellspacing="0" class="Listing" id="listtable"  onClick="TableSort.sortColumn(event)">
				<thead>
					<tr>
						<td>序号</td>
						<td>标题</td>
						<td>拟稿人</td>
						<td>紧急度</td>
						<td>是否需要反馈</td>
						<td>发布时间</td>
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
					            <c:out value="高"/>
					        </c:when>
					        <c:when test="${item.imptLvl==2}">
					            <c:out value="中"/>
					        </c:when>
					        <c:when test="${item.imptLvl==3}">
					            <c:out value="低"/>
					        </c:when>
					        <c:otherwise>
					    		<c:out value="未知"/>
					        </c:otherwise>
					    </c:choose>
					 </td>
					 <td align="center">
						 <c:choose>
					        <c:when test="${item.feedbackFlg==0}">
								<c:out value="是"/>
					        </c:when>
					        <c:when test="${item.feedbackFlg==1}">
								<c:out value="否"/>
					        </c:when>
					        <c:otherwise>
								<c:out value="是"/>
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