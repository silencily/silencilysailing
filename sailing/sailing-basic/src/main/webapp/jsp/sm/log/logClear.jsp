<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf"/>
<jsp:directive.include file="/decorators/edit.jspf"/>
<html>
<body class="main_body">
<form action="/sm/logManageAction.do" method="post">
	<input type="hidden" name="oid" value="<c:out value = '${theForm.oid}' />" />
	<input type="hidden" name="step" value="<c:out value = '${theForm.step}' />" />
	<div class="update_subhead" >
    <span class="switch_open" onClick="StyleControl.switchDiv(this, $('supplierQuery'))" title="点击这里进行查询">清理时间设定</span>
</div>
<div id="supplierQuery" style="display:">
	<table width="100%"  border="0" cellpadding="0" cellspacing="0" class="Detail" id="divId_flaw_search_common" style="display:">
	    <tr>
	    	 <td class="attribute">初始时间</td>
	    	 <td>
	    	 <input type="text" value="" name="startTime" readonly
				"<c:out value='${pageScope.textdisable}'/>" /><input type="button" value="" id="input_date" onClick="DateComponent.setDay(this,document.getElementsByName('startTime')[0])"
				<c:out value='${pageScope.buttondisable}'/> />
	    	 </td>
			<td class="attribute">结束时间</td>           
	       	<td>
	       	<input type="text" value="" name="endTime" readonly
				"<c:out value='${pageScope.textdisable}'/>" /><input type="button" value="" id="input_date" onClick="DateComponent.setDay(this,document.getElementsByName('endTime')[0])"
				<c:out value='${pageScope.buttondisable}'/> />
	       	</td>
 	    </tr>
  	</table>
 <div align="right"><input type="button" value="日志清理" name="" class="opera_display" title="清理日志" onClick="CurrentPage.clear();"></div>
</div>
</form>
</body>
</html>
<script type="text/javascript">
var msgInfo_ = new msgInfo();
	if (CurrentPage == null) {
   		var CurrentPage = {};
	}
	 CurrentPage.clear= function () {
	 	CurrentPage.initValideInput();
		if (!CurrentPage.validation()) {
				return false;
			}
	 	var date1=document.getElementById('startTime').value;
	 	var date2=document.getElementById('endTime').value;
		if (DateUtils.CompareDate(date1,date2)<0){
					Validator.clearValidateInfo();
					Validator.warnMessage(msgInfo_.getMsgById('SM_I016_C_0'));
					return false;	
				}
		if (CurrentPage.CheckDay($('startTime').value) == false ) {
					Validator.clearValidateInfo();
					Validator.warnMessage(msgInfo_.getMsgById('SM_I017_C_0'));
					return false;
				}
		if (CurrentPage.CheckDay($('endTime').value) == false ) {
					Validator.clearValidateInfo();
					Validator.warnMessage(msgInfo_.getMsgById('SM_I018_C_0'));
					return false;
				}
		FormUtils.post(document.forms[0], '<c:url value="/sm/logManageAction.do?step=delete&end="/>'+date2+"&start="+date1);
	}
	CurrentPage.validation = function () {
		return Validator.Validate(document.forms[0], 4);
	}
	CurrentPage.initValideInput = function () {
		document.getElementById('startTime').dataType = 'Require';
		document.getElementById('startTime').msg = msgInfo_.getMsgById('SM_I019_C_0');
		document.getElementById('endTime').dataType = 'Require';
		document.getElementById('endTime').msg = msgInfo_.getMsgById('SM_I020_C_0');
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
			CurrentPage.CheckDay = function (day) {
				if (day != ""){
					if(DateUtils.CompareDate(s,day)>0)
						return false;
					}
				else{
					return true;
				}
			}
</script>