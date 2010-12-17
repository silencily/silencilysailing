<%--
    @version:$Id: scheduleInfo.jsp,v 1.1 2010/12/10 10:56:18 silencily Exp $
    @since $Date: 2010/12/10 10:56:18 $
--%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf"/> 
<script type="text/javascript" src = "<c:out value = "${initParam['publicResourceServer']}/js/form.js"/>"></script>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK"/>
</head>
<body class="main_body" >
<form name="f" action="" method="post">
		<input type="hidden" name="oid" value="<c:out value="${theForm.bean.id}"/>"/>
		<input type="hidden" name="step" value="<c:out value='${theForm.step}'/>"/>
	    <div class="update_subhead">
	    <span class=" switch_open" onClick="StyleControl.switchDiv(this, $('submenu'))" title="��������ڵ�">�ճ̰���</span>
		</div>
		<table width="100%" border="0" cellpadding="0" cellspacing="1" class="Detail" id="submenu"> 
	<c:choose>
	<c:when test="${theForm.bean.completeFlg== 'YIWC'}">
		      	<tr>
				<td class="attribute">����</td>
				<td colspan="3">
					<vision:text 
					rwCtrlType="2" 
					permissionCode="" 
					dataType="1"
					name="temp.bean.title" 
					value="${theForm.bean.title}"
					readonly="true"
					required="true"
					comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagDefautPolicy" />
				</td>
			</tr>
			<tr>
				<td class="attribute">��ʼʱ��</td>
				<td>
					<vision:text
					rwCtrlType="2"
					permissionCode=""
					dataType="3"
					name="temp.bean.begTime"
					value="${theForm.bean.begTime}"
					readonly="true"
					required="true"
					comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagDefautPolicy" />
				</td>
				<td class="attribute">����ʱ��</td>
				<td>
					<vision:text
					rwCtrlType="2"
					permissionCode=""
					dataType="3"
					name="temp.bean.endTime"
					value="${theForm.bean.endTime}"
					required="true"
					readonly="true"
					comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagDefautPolicy" />
				</td>
			</tr>
			<tr>
				<td class="attribute">��ǰ����ʱ��</td>
				<td>
					<vision:choose
					rwCtrlType="2"
					permissionCode=""
					value="${theForm.bean.alertTime}" 
					textName="temp.bean.alertTime"
					valueName="temp.bean.alertTime" 
					source='${theForm.alsetList}'
					required="true"
					readonly="true"
					comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagDefautPolicy"/>
				</td>
				<td class="attribute">�ٴ�����ʱ����</td>
				<td>
					<vision:choose
					rwCtrlType="2"
					permissionCode=""
					value="${theForm.bean.reptAlert}" 
					textName="temp.bean.reptAlert"
					valueName="temp.bean.reptAlert" 
					source='${theForm.reptList}'
					readonly="true"
					comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagDefautPolicy"/>
				</td>
			</tr>
			<tr>
				
				<td class="attribute">�����</td>
				<td colspan="3">
					<input type="checkbox" name="completeFlg" DISABLED <c:if test="${theForm.bean.completeFlg== 'YIWC'}">checked</c:if>>
					<input type="hidden" name="bean.completeFlg.code" value="<c:out value="${theForm.bean.completeFlg}"/>"/>
				</td>
			</tr>
			<tr>
				<td class="attribute">����</td>
				<td colspan="3">
					<vision:text 
					rwCtrlType="2" 
					permissionCode="" 
					dataType="8"
					rows="8"
					name="temp.bean.content" 
					value="${theForm.bean.content}"
					longTextType="true"
					style="width:82%"
					readonly="true"
					comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagDefautPolicy" />
				</td>
			</tr>
	</c:when>
	<c:otherwise>
		      	<tr>
				<td class="attribute">����</td>
				<td colspan="3">
					<vision:text 
					rwCtrlType="2" 
					permissionCode="" 
					dataType="1"
					name="bean.title" 
					value="${theForm.bean.title}"
					maxlength="20"
					required="true"
					comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagDefautPolicy" />
				</td>
			</tr>
			<tr>
				<td class="attribute">��ʼʱ��</td>
				<td>
					<vision:text
					rwCtrlType="2"
					permissionCode=""
					dataType="3"
					name="bean.begTime"
					value="${theForm.bean.begTime}"
					required="true"
					comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagDefautPolicy" />
				</td>
				<td class="attribute">����ʱ��</td>
				<td>
					<vision:text
					rwCtrlType="2"
					permissionCode=""
					dataType="3"
					name="bean.endTime"
					value="${theForm.bean.endTime}"
					required="true"
					comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagDefautPolicy" />
				</td>
			</tr>
			<tr>
				<td class="attribute">��ǰ����ʱ��</td>
				<td>
					<vision:choose
					rwCtrlType="2"
					permissionCode=""
					value="${theForm.bean.alertTime}" 
					textName="temp.bean.alertTime"
					valueName="bean.alertTime" 
					source='${theForm.alsetList}'
					required="true"
					comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagDefautPolicy"/>
				</td>
				<td class="attribute">�ٴ�����ʱ����</td>
				<td>
					<vision:choose
					rwCtrlType="2"
					permissionCode=""
					value="${theForm.bean.reptAlert}" 
					textName="temp.bean.reptAlert"
					valueName="bean.reptAlert" 
					source='${theForm.reptList}'
					comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagDefautPolicy"/>
				</td>
			</tr>
			<tr>
				
				<td class="attribute">�����</td>
				<td colspan="3">
					<input type="checkbox" name="completeFlg" <c:if test="${theForm.bean.completeFlg== 'YIWC'}">checked</c:if>>
					<input type="hidden" name="bean.completeFlg.code" value="<c:out value="${theForm.bean.completeFlg}"/>"/>
				</td>
			</tr>
			<tr>
				<td class="attribute">����</td>
				<td colspan="3">
					<vision:text 
					rwCtrlType="2" 
					permissionCode="" 
					dataType="6"
					rows="8"
					name="bean.content" 
					value="${theForm.bean.content}"
					maxlength="200"
					longTextType="true"
					style="width:82%"
					bisname="����"
					comInvorkeeClassFullName="net.silencily.sailing.common.crud.tag.TagDefautPolicy" />
				</td>
			</tr>
	</c:otherwise>
	</c:choose>
		</table>
</form>
</body>
<script language="javascript">
	
		if (CurrentPage == null) {
			var CurrentPage = {};
		}
		CurrentPage.reset = function () {
			document.f.reset();
		}
	
		CurrentPage.submit = function()
		{
			if(!CurrentPage.validateItemNumberItems())
			{
				return;
			}
			if(!verifyAllform()){return false;}
			$('step').value = 'save';
			FormUtils.post(document.forms[0], '<c:url value = "/uf/schedule/ScheduleAction.do" />');
		}
		
		CurrentPage.create = function() {
			$('oid').value = '';
			$('step').value ='info';
			FormUtils.post(document.forms[0], '<c:url value= "/uf/schedule/ScheduleAction.do"/>'); 
		}
		
		<%--��֤���м�¼ --%>
		CurrentPage.validateItemNumberItems = function(){

		Validator.clearValidateInfo();	
		
		var title = document.getElementById("bean.title").value;
		var begTime = document.getElementById("bean.begTime").value;
		var endTime = document.getElementById("bean.endTime").value;
		var alertTime = document.getElementById("bean.alertTime").value;
		var completeFlg = document.getElementById("completeFlg");
		if(title == "")
		{
			Validator.warnMessage('�����������');
				return false;
		}
		if(begTime == "")
		{
			Validator.warnMessage('��ʼʱ���������');
				return false;
		}
		if(endTime == "")
		{
			Validator.warnMessage('����ʱ���������');
				return false;
		}
      	if(begTime != "" && endTime != "")
       	{
       		var tem = /[\s-:]/g;
       		var bTime = parseInt(begTime.replace(tem,""));
			var eTime = parseInt(endTime.replace(tem,""));
			if(bTime > eTime)
			{
				Validator.warnMessage('��ʼʱ�䲻�ܴ��ڽ���ʱ��');
    			return false;
			}
	  	}
		if(alertTime == "")
		{
			Validator.warnMessage('��ǰ����ʱ���������');
				return false;
		}
		if ( !completeFlg.checked && CurrentPage.CheckDay(begTime) == false ) {
				Validator.warnMessage('��ʼʱ�䲻��Ϊ��ȥʱ��');
				return false;
				}
		if ( !completeFlg.checked && CurrentPage.CheckDay(endTime) == false ) {
				Validator.warnMessage('����ʱ�䲻��Ϊ��ȥʱ��');
				return false;
		}
		if(completeFlg.checked==true)
		{
			$('bean.completeFlg.code').value = "YIWC";
		}else
		{
			$('bean.completeFlg.code').value = "WEIW";
		}
		return true;
	}
			var d, s = "";
			d = DateUtils.GetDBDate();
			s +=d.getYear() + "-";
			var month = d.getMonth()+1;
			if(month<10){
			s += "0" + month + "-";
			}else{
			s += month + "-";
			}
			var day = d.getDate();
			if(day <10){
			s += "0" + day;
			}else{
				s += d.getDate();  
			}
			var hours = d.getHours();
			s+=" "+hours;
			var minutes = d.getMinutes();
			s+=":"+minutes;
			var seconds= d.getSeconds();
			s+=":"+seconds;
	CurrentPage.CheckDay = function (day) {
		if (day != ""){
			if(s<day)
				return true;
		}
		return false;
	}
 	</script>
</html>