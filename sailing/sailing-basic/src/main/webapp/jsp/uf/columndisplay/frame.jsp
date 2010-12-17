<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<html>
<head>
<jsp:directive.include file="/decorators/default.jspf" />
<jsp:directive.include file="/decorators/edit.jspf"/>
<style type="text/css">
.titleindex1
 {
	background-image: url(<c:out value="${initParam['publicResourceServer']}"/>/img/titlebg.jpg);
	font-weight: bolder;
	color: #4776C4;
	text-align: left;
	background-repeat: repeat-x;
	height: 24px;
	margin-right: 0px;
	margin-left: 0px;
	padding: 0px;
	width: 100%;
}
.titleleft1 {
	background-image: url(<c:out value="${initParam['publicResourceServer']}"/>/img/titleleft.jpg);
	text-align: right;
	width: 30px;
	float: left;
	margin: 0px;
	padding-right: 0px;
	padding-bottom: 0px;
	padding-left: 0px;
	line-height: 24px;
	height: 24px;
}
.titleright1 {
	background-image: url(<c:out value="${initParam['publicResourceServer']}"/>/img/titleright.jpg);
	background-position: right;
	background-repeat: no-repeat;
	float: right;
	width: 30px;
	line-height: 24px;
	height: 24px;
}
body {
	background-color: #EEF3F9;
}
</style>

</head>

<body>
<form name="f">
<input name="conditions(tblUfColumn.id).name" type="hidden" value="tblUfColumn.id"/>
<input name="conditions(tblUfColumn.id).operator" type="hidden" value="="/>
<input name="conditions(tblUfColumn.id).type" type="hidden" value="java.lang.String"/>
<input name="conditions(tblUfColumn.id).createAlias" type="hidden"  value="false" />
<input name = "conditions(tblUfColumn.id).value" type = "hidden" value = "" />
<table width="100%" cellspacing="0">
	<tr>
    	<c:forEach var="item" items="${theForm.listDesktop}" varStatus="status">
    		<c:if test="${status.index%2==0&&!status.first}">
    		    </tr><tr > 
    		</c:if>
    		<td width="50%" valign="top">
    			<div class="titleindex1" id="title">
	    			<div class="titleleft1" id="titleleft">
	    				<img src="<c:url value="/img/daiban.gif" />" />
	    			</div>
	    			<div class="titleright1" id="titleright">
					<c:if test="${item.tblUfColumn.columnFlg=='UF_LMQF_01'}">
	    				<a href="#" onclick="CurrentPage.moreNews('<c:out value="${item.tblUfColumn.id}" />')"><img src="<c:url value="/img/more.gif" />" border="0" /></a>
					</c:if>
					<c:if test="${item.tblUfColumn.columnFlg=='UF_LMQF_02'}">
	    			 	<a href="<c:url value="/wf/personWfSearchAction.do?step=entry" />"><img src="<c:url value="/img/more.gif" />" border="0" /></a>
					</c:if>
					<c:if test="${item.tblUfColumn.columnFlg=='UF_LMQF_03'}">
	    				<a href="#"><img src="<c:url value="/img/more.gif" />" border="0" /></a>
					</c:if>
					<c:if test="${item.tblUfColumn.columnFlg=='UF_LMQF_04'}">
	    				<a href="<c:url value="/uf/schedule/ScheduleAction.do?step=entry" />"><img src="<c:url value="/img/more.gif" />" border="0" /></a>
					</c:if>
					<c:if test="${item.tblUfColumn.columnFlg=='UF_LMQF_05'}">
	    				<a href="#"><img src="<c:url value="/img/more.gif" />" border="0" /></a>
					</c:if>
	    			</div>
	    			<div class="titletxt" id="titletxt"><c:out value="${item.tblUfColumn.columnNm}"></c:out>
					<c:if test="${item.tblUfColumn.columnFlg=='UF_LMQF_02'}">
	    			 	(<c:out value="${theForm.wfListSize}" />件)
					</c:if>
					</div>
    			</div>
    			
	    		<table border="0" width="100%" cellspacing="0" cellpadding="0" >
					<tr>
      					<td class="spacetitle">&nbsp;</td>
    				</tr>
    				<c:if test="${item.tblUfColumn.columnFlg=='UF_LMQF_01'}">
						<tr>
							<td class="listcontent">
								<table width="96%" border="0" align="center" cellpadding="0" cellspacing="0">
									<c:forEach var="itemNews" items="${item.tblUfColumn.tblUfNewses}" varStatus="statusNews">
									    <c:if test='${statusNews.index < 10}'>
										     <tr>
										     	<td>
										     		<table width="100%" border="0" cellpadding="0" cellspacing="0">
										     			<tr>
															<c:choose>
																<c:when test="${itemNews.recommend.code=='1'}"><td width="82%" class="listRecommend"></c:when>
																<c:otherwise><td width="82%" class="list"></c:otherwise>
															</c:choose>
															<!--<a href = "<c:url value="/uf/desk/TblColumnDisplayAction.do?step=detail&id=${itemNews.id}" />" ><c:out value='${itemNews.title}'/></a>-->
															<a href = "#" onclick="window.open('<c:url value="/uf/desk/TblColumnDisplayAction.do?step=detail&id=${itemNews.id}" />','newsWindow','resizable=yes,scrollbars=yes')";><c:out value='${itemNews.title}'/></a>
											     			<td width="18%" class="time">
														     	<c:out value="${itemNews.spublishTime}"/>
											     			</td>
											     		</tr>
											     	</table>
											   	</td>
											 </tr>
										     <tr>
									            <td class="line"></td>
											</tr>
										</c:if>
									</c:forEach>
									<c:if test="${empty item.tblUfColumn.tblUfNewses}">
								     <tr>
								     	<td class="listNothing">
											目前该栏目没有内容
									   	</td>
									 </tr>
									</c:if>
								</table>
							</td>
						</tr>
					</c:if>
					<c:if test="${item.tblUfColumn.columnFlg=='UF_LMQF_02'}">
						<tr>
							<td class="listcontent">
								<table width="96%" border="0" align="center" cellpadding="0" cellspacing="0">
									<c:forEach var="itemWfe" items="${theForm.wfList}" varStatus="statusWfe">
									     <c:if test='${statusWfe.index < 10}'>
											<tr>
										     	<td>
										     		<table width="100%" border="0" cellpadding="0" cellspacing="0">
										     			<tr>
											     			<td width="82%" class="list" nowrap>
											     				<a href = "<c:url value='${itemWfe.url}'/>&stepId=<c:url value='${itemWfe.stepId}'/>" title="<c:out value='${itemWfe.title}'/>"><c:out value='${itemWfe.titleCutForMainPage}'/></a>
											     			</td>
											     			<td width="18%" class="time">
														     	<c:out value="${itemWfe.commitTime}"/>
											     			</td>
											     		</tr>
											     	</table>
											   	</td>
											 </tr>
										     <tr>
									            <td class="line"></td>
											</tr>										     
										</c:if>
									</c:forEach>
									<c:if test="${empty theForm.wfList}">
								     <tr>
								     	<td class="listNothing">
											目前该栏目没有内容
									   	</td>
									 </tr>
									</c:if>
								</table>
							</td>
						</tr>
					</c:if>
					<c:if test="${item.tblUfColumn.columnFlg=='UF_LMQF_03'}">
						<tr>
							<td class="listcontent">
								<table width="96%" border="0" align="center" cellpadding="0" cellspacing="0">
									<c:forEach var="itemMS" items="${theForm.missionList}" varStatus="statusMS">
									     <c:if test='${statusMS.index < 10}'>
											<tr>
										     	<td>
										     		<table width="100%" border="0" cellpadding="0" cellspacing="0">
										     			<tr>
											     			<td width="82%" class="list">
											     			</td>
											     			<td width="18%" class="time">
											     			</td>
											     		</tr>
											     	</table>
											   	</td>
											 </tr>
										     <tr>
									            <td class="line"></td>
											</tr>										     
										</c:if>
									</c:forEach>
									<c:if test="${empty theForm.missionList}">
								     <tr>
								     	<td class="listNothing">
											目前该栏目没有内容
									   	</td>
									 </tr>
									</c:if>
								</table>
							</td>
						</tr>
					</c:if>		
					<c:if test="${item.tblUfColumn.columnFlg=='UF_LMQF_04'}">
						<tr>
							<td class="listcontent">
								<table width="96%" border="0" align="center" cellpadding="0" cellspacing="0">
									<c:forEach var="schedule" items="${theForm.scheduleList}" varStatus="statusSchedule">
									     <c:if test='${statusSchedule.index < 10}'>
											<tr>
										     	<td>
										     		<table width="100%" border="0" cellpadding="0" cellspacing="0">
										     			<tr>
											     			<td width="82%" class="list">
											     				<a href = "<c:url value="/uf/schedule/ScheduleAction.do?step=entry&panelUrl=/uf/schedule/ScheduleAction.do?step%3Dinfo%26oid%3D${schedule.id}" />" >
											     				<c:out value='${schedule.title}'/>
											     				</a>
											     			</td>
											     			<td width="18%" class="time">
														     	<c:out value="${schedule.displayTime}"/>
											     			</td>
											     		</tr>
											     	</table>
											   	</td>
											 </tr>
										     <tr>
									            <td class="line"></td>
											</tr>										     
										</c:if>
									</c:forEach>
									<c:if test="${empty theForm.scheduleList}">
								     <tr>
								     	<td class="listNothing">
											目前该栏目没有内容
									   	</td>
									 </tr>
									</c:if>
								</table>
							</td>
						</tr>
					</c:if>		
					<c:if test="${item.tblUfColumn.columnFlg=='UF_LMQF_05'}">
						<tr>
							<td class="listcontent">
								<table width="96%" border="0" align="center" cellpadding="0" cellspacing="0">
									<c:forEach var="itemMail" items="${theForm.mailList}" varStatus="statusMail">
									     <c:if test='${statusMail.index < 10}'>
											<tr>
										     	<td>
										     		<table width="100%" border="0" cellpadding="0" cellspacing="0">
										     			<tr>
											     			<td width="82%" class="list">
											     			</td>
											     			<td width="18%" class="time">
											     			</td>
											     		</tr>
											     	</table>
											   	</td>
											 </tr>
										     <tr>
									            <td class="line"></td>
											</tr>										     
										</c:if>
									</c:forEach>
									<c:if test="${empty theForm.mailList}">
								     <tr>
								     	<td class="listNothing">
											目前该栏目没有内容
									   	</td>
									 </tr>
									</c:if>
								</table>
							</td>
						</tr>
					</c:if>		
				</table>
    		</td>
    	</c:forEach>
   	</tr>    
</table>
</form> 
</body>
</html>
<script language="javascript">
if (CurrentPage == null){
    var CurrentPage = {};
}
CurrentPage.notVisibleBtn = function(){
}
CurrentPage.moreNews = function(columnId){
    document.getElementsByName("conditions(tblUfColumn.id).value")[0].value = columnId;
    FormUtils.post(document.forms[0], '<c:url value="/uf/news/NewsPublishAction.do?step=more&paginater.page=0"/>');
    return false;
}

</script>
