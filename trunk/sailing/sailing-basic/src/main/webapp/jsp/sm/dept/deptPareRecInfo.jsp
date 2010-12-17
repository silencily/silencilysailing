<%--
    @version:$Id: deptPareRecInfo.jsp,v 1.1 2010/12/10 10:56:43 silencily Exp $
    @since $Date: 2010/12/10 10:56:43 $
    @name:�ϼ����ű��
--%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/edit.jspf"/>
<jsp:directive.include file="/decorators/default.jspf"/>
<html>
<body class="main_body">
<form name="f" action="" method="post">
<input type="hidden" name="parentCode" value="<c:out value = "${theForm.parentCode}"/>"/>
<input type="hidden" name="oid" value="<c:out value='${theForm.bean.id}'/>"/>
<input type="hidden" name="step" value="<c:out value='${theForm.step}'/>"/>
<c:set var="parent" value="${theForm.bean.parent}"/>
<input type="hidden" name="beginPDI" value="<c:out value='${theForm.bean.id}'/>"/>
<input type="hidden" name="beginPDN" value="<c:out value='${parent.deptName}'/>"/>
<input type="hidden" name="endPDI" value="<c:out value='${parent.id}'/>"/>
<input type="hidden" name="endPDN" value="<c:out value='${parent.deptName}'/>"/>
<input type="hidden" id="dpn" name="dpn" value="<c:out value='${parent.deptName}'/>"/>
<input type="hidden" name="step" value="save"/>

	<jsp:directive.include file="deptParent.jspf"/>
	<div class="update_subhead">
		 <span class="switch_open" onClick="StyleControl.switchDiv(this,submenudept)" title="�����ڵ�">�ϼ����ű��</span>
	</div>
	<table border="0" cellpadding="0" cellspacing="0" class="Detail" id="submenudept">
		<tr>
			<td class="attribute">���ʱ��</td>
			<td><input type="text"	value="" id="input_text" name="pareBean.changeTime" class="readonly" readonly="readonly"/><input type="button" value="" id="input_date" onClick="DateComponent.setDay(this,document.getElementsByName('pareBean.changeTime')[0])" />
			<span class="font_request">*</span>&nbsp;
			<script language=JavaScript>
				var enabled = 0; today = new Date();
				var date;
				date =(today.getYear()) + "-" + (today.getMonth() + 1 ) + "-" + today.getDate();
				if(document.getElementById('pareBean.changeTime').value==""){
				document.getElementById('pareBean.changeTime').value = date;
			}
			</script>
			<%--pareDept--%>
			<td class="attribute">�ϼ�����</td>
			<%--deptId--%>
			<input type="hidden" id="parentId" name="bean.parent.parentCd" value="<c:out value='${parent.deptCd}'/>" class="readonly" readonly="readonly"/>
			<td><input name="departmentnameNew" value="<c:out value='${parent.deptName}'/>" class="readonly" readonly="readonly">&nbsp;<span class="font_request">*</span>
			<input type="button" id="select_fromtree" name="deptChange" title="���ѡ����"
			onClick="javascript:showCgmlTreeWindow(parentId, 'bean.parent.parentCd', 'departmentnameNew', '', '1');"/>
			</td>
		</tr>
		<tr>
			<td class="attribute">&nbsp;��    ע</td>			
			<td colspan="3">
					<input bisname="��ע" maxlength="500" type="text" style="width:440px" id="textArea" name="pareBean.remark" class="input_long" <c:out value='${pageScope.textdisabled}'/>value="<c:out value = '${pareBean.remark}'/>"/>
				<span title="<c:out value='${pareBean.remark}' escapeXml='true'/>">
					<input type="button" id="edit_longText" onClick="definedWin.openLongTextWin(document.getElementsByName('pareBean.remark')[0]);" />
				</span>
			</td>
		</tr>
</table>
<div class="update_subhead">
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td>
				<span class="switch_open" onClick="StyleControl.switchDiv(this, $('listtable1'))" title="�����ڵ�">�������</span>
			</td>
		</tr>
	</table>
</div>
<div id="divId_scrollLing" class="list_scroll">
	<table border="0" cellpadding="0" cellspacing="0" class="Listing" id="listtable1"  onClick="TableSort.sortColumnOnly(event)">
		<thead>
			<tr>
				<td type='Number' style='width:30px;'>���</td>
				<td>���ʱ��</td>
				<td>������</td>
				<td>����ʱ��</td>
				<td>ԭ�ϼ�����</td>
				<td>���ϼ�����</td>
				<td>��ע</td>
			</tr>
		</thead>
		<tbody>	

		<c:forEach var="item" items="${theForm.bean.tblCmnDeptPareRecs}" varStatus="status">
			<tr>
				<input type="hidden" name="oid" value="<c:out value="${item.id}"/>"/>
				<td align="right"><c:out value="${status.count}"/>&nbsp;</td>
				<td align="center"><fmt:formatDate value="${item.changeTime}" pattern = "yyyy-MM-dd"/>&nbsp;</td>
				<td align="left"><c:out value="${item.operater}" escapeXml='true'/>&nbsp;</td>
				<td align="center"><fmt:formatDate value = "${item.operateTime}" pattern = "yyyy-MM-dd HH:mm:ss" />&nbsp;</td>
				<td align="left"><c:out value="${item.beginParent}" escapeXml='true'/>&nbsp;</td>				
				<td align="left"><c:out value="${item.endParent}" escapeXml='true'/>&nbsp;</td>
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
		if (CurrentPage == null) {
			var CurrentPage = {};
		}
		
		CurrentPage.reset = function () {
			document.f.reset();
		}
		
		CurrentPage.submit = function () {
			if (!CurrentPage.validation()) {
				return;
			}
			<%--��֤ʱ���Ƿ���δ������--%>
			if (CurrentPage.CheckDay() == false) {
				return false;
			}
			if(!verifyAllform()){return false;}
			if(document.getElementById('dpn').value==document.getElementById('departmentnameNew').value){
				Validator.warnMessage(msgInfo_.getMsgById('HR_I039_C_1',['���������']));
				return;
			}
			if (!confirm(msgInfo_.getMsgById('HR_I030_A_1',[document.getElementById('departmentnameNew').value]))) {
				return;
			}
			
			FormUtils.post(document.forms[0], '<c:url value='/sm/TblSmDeptPareRecAction.do?step=save'/>');
		}
		CurrentPage.validation = function () {
			return Validator.Validate(document.forms[0],5);
		}
		<%--У��--%>
		CurrentPage.initValideInput = function () {
			document.getElementById('pareBean.changeTime').dataType = 'Require';
			document.getElementById('pareBean.changeTime').msg = msgInfo_.getMsgById('HR_I068_C_1',['���ʱ��']);
		}
		<%--��֤ʱ���Ƿ���δ������--%>
		CurrentPage.CheckDay=function(){
			var today= new Date();
			var month=today.getMonth()+1;
			month=month<10? "0"+month:month;
			var day=today.getDate();
			day=day<10? "0"+day:day;
		   	<%--����Ϊ yyyy-mm-dd ��ʽ--%>
		    var strToday= today.getFullYear()+"-"+month +"-"+ day;
		    //window.alert(strToday);
	   		if($('pareBean.changeTime').value!=""){
		 		if (DateUtils.CompareDate (strToday,($('pareBean.changeTime').value))>0){
					Validator.warnMessage(msgInfo_.getMsgById('HR_I072_C_1',['���ʱ��']));
					document.getElementById('pareBean.changeTime').focus();
						return false;
				}
    		}
    	}
		CurrentPage.initValideInput();
		
		function Validator.afterSuccessMessage(){
			if(parent.codeTree){
				parent.codeTree.Update(parent.codeTree.SelectId);
			}
		}
	</script>
</form>
</body>
<script type="text/javascript" src = "<c:out value = "${initParam['publicResourceServer']}/js/checkonchangevalues.js" />"></script>
</html>
