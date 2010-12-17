<%--
    @version:$Id: deptStatusInfo.jsp,v 1.1 2010/12/10 10:56:43 silencily Exp $
    @since $Date: 2010/12/10 10:56:43 $
    @name:状态变更
--%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ include file="/decorators/edit.jspf" %>
<%@ include file="/decorators/default.jspf" %>
<html>
	<body class="main_body">
	<form name="f" action="" method="post">
		<input type="hidden" name="parentCode" value="<c:out value = "${theForm.parentCode}"/>" />
		<input type="hidden" name="oid"	value="<c:out value='${theForm.bean.id}'/>" />
		<input type="hidden" name="step" value="<c:out value='${theForm.step}'/>" />
		<input type="hidden" name="beginstate" value="<c:out value='${theForm.bean.deptState}'/>" />
		<c:set var="parent" value="${theForm.bean.parent}" />
		<%@ include file="deptParent.jspf" %>
		<div class="update_subhead">
			<span class="switch_open" onClick="StyleControl.switchDiv(this,submenudept)" title="伸缩节点">状态变更</span>
		</div>
		<table border="0" cellpadding="0" cellspacing="0" class="Detail" id="submenudept">
			<tr>
				<td class="attribute">正在使用</td>
				<td>
					<input type="radio" name="statusBean.tblCmnDept.deptState" value="0" onClick="CurrentPage.selectRadio0('selectRadio');" <c:if test="${theForm.bean.deptState == '0'}">checked</c:if>>
				</td>
				<td class="attribute">停止使用</td>
				<td>
					<input type="radio" name="statusBean.tblCmnDept.deptState" value="1"onClick="CurrentPage.selectRadio1('selectRadio');"<c:if test="${theForm.bean.deptState == '1'}">checked</c:if>>
				</td>
			</tr>
			<tr>
				<td class="attribute">变更时间</td>
				<td colspan="3">
					<input type="text" value="" id="input_text" name="statusBean.changeTime" class="readonly" readonly="readonly"/><input type="button" 
					    value="" id="input_date" onClick="DateComponent.setDay(this,document.getElementsByName('statusBean.changeTime')[0])" />
					<span class="font_request">*</span>&nbsp;
				</td>
				<script language=JavaScript>
					var enabled = 0; today = new Date();
					var date;
					date =(today.getYear()) + "-" + (today.getMonth() + 1 ) + "-" + today.getDate();
					if(document.getElementById('statusBean.changeTime').value==""){
						document.getElementById('statusBean.changeTime').value = date;
					}
				</script>
			</tr>
			<tr>
				<td class="attribute">备 注</td>
				<td colspan="3">
					<input bisname="备注" maxlength="500" type="text" style="width:440px" id="textArea" name="statusBean.remark" class="input_long" <c:out value='${pageScope.textdisabled}'/>value="<c:out value = '${theForm.statusBean.remark}'/>"/>
					<span title="<c:out value='${theForm.statusBean.remark}'/>">
						<input type="button" id="edit_longText"	onClick="definedWin.openLongTextWin(document.getElementsByName('statusBean.remark')[0]);" />
					</span>
				</td>
			</tr>
		</table>
		<div class="update_subhead">
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td>
						<span class="switch_open" onClick="StyleControl.switchDiv(this, $('listtable1'))" title="伸缩节点">状态变更履历</span>
					</td>
				</tr>
			</table>
		</div>
		<div id="divId_scrollLing" class="list_scroll">
			<table border="0" cellpadding="0" cellspacing="0" class="Listing" id="listtable1" onClick="TableSort.sortColumnOnly(event)">
				<thead>
					<tr>
						<td type='Number' style='width:30px;'>序号</td>
						<td>变更时间</td>
						<td>操作人</td>
						<td>操作时间</td>
						<td>原状态</td>
						<td>新状态</td>
						<td>备注</td>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="item" items="${theForm.bean.tblCmnDeptStatusRecs}" varStatus="status">
						<tr>
							<input type="hidden" name="oid" value="<c:out value="${item.id}"/>" />
							<td align="right"><c:out value="${status.count}" />&nbsp;</td>
							<td align="center"><fmt:formatDate value="${item.changeTime}" pattern="yyyy-MM-dd" />&nbsp;</td>
							<td align="left"><c:out value="${item.operator}" />&nbsp;</td>
							<td align="center"><fmt:formatDate value="${item.operateTime}" pattern="yyyy-MM-dd HH:mm:ss" />&nbsp;</td>
							<td align="center">
								<c:choose>
									<c:when test="${item.beginState== '0'}">
										<font color="Red"><c:out value="正在使用" /></font>
									</c:when>
									<c:otherwise>
										<c:choose>
											<c:when test="${item.beginState == '1'}">
												<c:out value="停止使用" />
											</c:when>
										</c:choose>
									</c:otherwise>
								</c:choose>&nbsp;
							</td>
							<td align="center">
								<c:choose>
									<c:when test="${item.endState== '0'}">
										<font color="Red"><c:out value="正在使用" /></font>
									</c:when>
									<c:otherwise>
										<c:choose>
											<c:when test="${item.endState == '1'}">
												<c:out value="停止使用" />
											</c:when>
										</c:choose>
									</c:otherwise>
								</c:choose>&nbsp;
							</td>
							<td style="width:130px;">
								<input type="text" id="textArea" name="changes[<c:out value='${status.index}'/>].remark" class="input_long" style="width:100px;" readonly value="<c:out value='${item.remark}'/>"/>
								<input type="button" id="edit_longText" title="<c:out value='${item.remark}' escapeXml='true'/>" onClick="definedWin.openLongTextWin(document.getElementsByName('changes[<c:out value='${status.index}'/>].remark')[0],'',true);"/>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<script type="text/javascript">
			var msgInfo_ = new msgInfo();
			//--取出select的值--
			document.getElementById('statusBean.tblCmnDept.deptState').value="<c:out value='${theForm.statusBean.tblCmnDept.deptState}'/>";
			var textName="组织部门";
			var url = '<c:url value ="/sm/TblSmDeptStatusRecAction.do?step=list"/>'
			if (CurrentPage == null) {
				var CurrentPage = {};
			}
			
			CurrentPage.submit = function () {
				if (!CurrentPage.validation()) {
					return;
				}
				if(!verifyAllform()){return false;}
				<%--验证时间是否是未来日期--%>
				if (CurrentPage.CheckDay() == false) {
					return false;
				}
				$('step').value = 'save';
				FormUtils.post(document.forms[0], '<c:url value='/sm/TblSmDeptStatusRecAction.do'/>');
			}

			CurrentPage.validation = function () {
				return Validator.Validate(document.forms[0],5);
			}
			
			CurrentPage.initValideInput = function () {
				document.getElementById('statusBean.changeTime').dataType = 'Require';
				document.getElementById('statusBean.changeTime').msg = msgInfo_.getMsgById('HR_I068_C_1',['变更时间']);
			}
			
			CurrentPage.initValideInput();
			
			<%--验证时间是否是未来日期--%>
			
			CurrentPage.CheckDay=function(){
				var today= new Date();
				var month=today.getMonth()+1;
				month=month<10? "0"+month:month;
				var day=today.getDate();
				day=day<10? "0"+day:day;
				<%--日期为 yyyy-mm-dd 格式--%>
				var strToday= today.getFullYear()+"-"+month +"-"+ day;
				if($('statusBean.changeTime').value!=""){
					if (DateUtils.CompareDate (strToday,($('statusBean.changeTime').value))>0){
						Validator.warnMessage(msgInfo_.getMsgById('HR_I072_C_1',['变更时间']));
						document.getElementById('statusBean.changeTime').focus();
						return false;
					}
				}
			}
			
			function Validator.afterSuccessMessage(){
				if(parent.codeTree){
					parent.codeTree.Update(parent.codeTree.SelectId);
				}
			}
			
			CurrentPage.selectRadio0 = function(selectRadio){
				if(document.getElementById('statusBean.tblCmnDept.deptState').checked == true){
        			$('statusBean.tblCmnDept.deptState').value="0";
        		}
        	}
        	
        	CurrentPage.selectRadio1 = function(selectRadio){
				if(document.getElementById('statusBean.tblCmnDept.deptState').checked == true){
        			$('statusBean.tblCmnDept.deptState').value="1";
        		}
        	}
		</script>
		<script type="text/javascript" src = "<c:out value = "${initParam['publicResourceServer']}/js/checkonchangevalues.js" />"></script>
	</form>
	</body>
</html>