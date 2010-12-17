<%--
    @version:$Id: deptInfo.jsp,v 1.1 2010/12/10 10:56:43 silencily Exp $
    @since $Date: 2010/12/10 10:56:43 $
    @组织的详细页面
--%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ include file = "/decorators/default.jspf"%>
<%@ include file = "/decorators/common.jspf"%>

<jsp:directive.include file="/decorators/edit.jspf"/>
<html>
<body class="main_body">
<form name="f" action="" method="post">
<input type="hidden" name="parentCode" value="<c:out value = '${theForm.parentCode}' escapeXml='true'/>"/>
<input type="hidden" name="oid" value="<c:out value='${theForm.bean.id}'/>"/>
<input type="hidden" name="dpCD" value="<c:out value='${theForm.bean.deptCd}' escapeXml='true'/>" />
<input type="hidden" name="step" value="save"/>
<c:set var="parent" value="${theForm.bean.parent}"/>
<div class="update_subhead">
	<span class="switch_open" onClick="StyleControl.switchDiv(this,$('parenttable'))" title="伸缩节点">基本信息</span>
</div>
<table border="0" cellpadding="0" cellspacing="0" class="Detail" id="parenttable">
	<tr>
		<td class="attribute">部门编码</td>
		<td><input bisname="部门编码" maxlength="20" name="bean.deptCd" value="<c:out value='${theForm.bean.deptCd}'/>"/>
		<span class="font_request">*</span>&nbsp;
		</td>
		<td class="attribute">上级部门</td>
		<td><input name="dummy.parent.deptName" value="<c:out value='${parent.deptName}'/>"class="readonly" readonly="readonly"/>
		</td>			
	</tr>														
	<tr>
		<td class="attribute">部门名称</td>
		<td><input bisname="部门编码" maxlength="50" name="bean.deptName" value="<c:out value='${theForm.bean.deptName}'/>"/>
		<span class="font_request">*</span>&nbsp;
		</td>   
		<td class="attribute">部门状态</td>
		<td><input name="bean.deptState" value="<c:out value='${theForm.bean.deptState}'/>" class="readonly" readonly="readonly"/>
		</td>
	</tr>
	<tr>
		<td class="attribute">部门负责人</td>
		<td>
		<input id="bean.deptCharge.empName" name="bean.deptCharge" value="<c:out value="${theForm.bean.deptCharge}"/>" class="readonly" readonly="readonly"/>
		<input type="button" id="edit_query" title="点击选择人员" onClick="selectEmp('bean.deptCharge', definedWin, 'radio')" />
		<input type="button" id="opera_clear" onClick="FormUtils.cleanValues($('bean.deptCharge'))" />
		</td>
		<td class="attribute">部门性质</td>
		<td>
		<ec:composite value="${theForm.bean.deptProperty}" valueName = "bean.deptProperty.code" textName ="temp.bean.deptProperty.code" source = '${theForm.sysCodes["HR"]["BMXZ"]}'/>
		</td>
		<script language=JavaScript>
			var state0='正在使用';
			var state1='停止使用';
			if(document.getElementById('bean.deptState').value==0){
			document.getElementById('bean.deptState').value = state0;
			}else if(document.getElementById('bean.deptState').value==1){
			document.getElementById('bean.deptState').value = state1;
			}else if(document.getElementById('bean.deptState').value==""){
			document.getElementById('bean.deptState').value =state0;
			}
		</script>	
	</tr>
	<tr>
		<td class="attribute">部门定员人数</td>
		<td><input bisname="部门定员人数" maxlength="5" name="bean.expectPerson" value="<c:out value='${theForm.bean.expectPerson}'/>" style="text-align:right"/></td>
	</tr>
	<tr>
		<td class="attribute">联系电话</td>
		<td><input bisname="联系电话" maxlength="20" name="bean.telephone" value="<c:out value='${theForm.bean.telephone}'/>"></td>
		<td class="attribute">传真</td>
		<td><input bisname="传真" maxlength="20" name="bean.fax" value="<c:out value='${theForm.bean.fax}'/>"/></td>
	</tr>
	<tr>
		<td class="attribute">电子邮箱</td>
		<td><input bisname="电子邮箱" maxlength="100" name="bean.email" value="<c:out value='${theForm.bean.email}'/>"></td>
		<td class="attribute">网站地址</td>
		<td><input bisname="网站地址" maxlength="100" name="bean.webAdress" value="<c:out value='${theForm.bean.webAdress}'/>" /></td>
	</tr>
	<tr>
		<td class="attribute">办公地址</td>
		<td><input bisname="办公地址" maxlength="500" name="bean.deptAddress" value="<c:out value='${theForm.bean.deptAddress}'/>"/></td>
		<td class="attribute">&nbsp;显示顺序</td>
		<td><input bisname="显示顺序" maxlength="4" name="bean.showSequence" value="<c:out value='${theForm.bean.showSequence}'/>" style="text-align:right"/></td>
	</tr>
	<tr>
		<td class="attribute">备注</td>
		<td colspan="3">
		<input bisname="备注" maxlength="500" type="text" style="width:440px" id="textArea" name="bean.remark" class="input_long" value="<c:out value = '${theForm.bean.remark}'/>"/>
<!--		<textarea id="textArea" name="bean.remark" rows="1" class="input_long" style="width:85%"><c:out value = "${theForm.bean.remark}"/></textarea>-->
		<span title="<c:out value='${theForm.bean.remark}' escapeXml='true'/>">
			<input type="button" id="edit_longText" onClick="definedWin.openLongTextWin(document.getElementsByName('bean.remark')[0]);" />
		</span></td>
	</tr>
	<tr>
		<td class="attribute">&nbsp;大事记</td>
		<td colspan="3">
		<input bisname="大事记" maxlength="200" type="text" style="width:440px" id="textArea" name="bean.memorabilia" class="input_long" value="<c:out value = '${theForm.bean.memorabilia}'/>"/>
<!--		<textarea id="textArea" name="bean.memorabilia" rows="1" class="input_long" style="width:85%"><c:out value = "${theForm.bean.memorabilia}"/></textarea>-->
		<span title="<c:out value='${theForm.bean.memorabilia}' escapeXml='true'/>">
			<input type="button" id="edit_longText" onClick="definedWin.openLongTextWin(document.getElementsByName('bean.memorabilia')[0]);" />
		</span></td>
	</tr>
</table>
	
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
		if(!verifyAllform()){return false;}
		<%--all 验证--%>
		if(CurrentPage.ValidateAll()==false){
			return false;
		}
		if (CurrentPage.CheckNum_expectPerson(document.getElementById('bean.expectPerson')) == false) {
	    	return false;
		}
		if (CurrentPage.CheckNum_showSequence(document.getElementById('bean.showSequence')) == false) {
	    	return false;
		}
		FormUtils.post(document.forms[0], '<c:url value='/sm/TblSmDeptAction.do'/>');
	}
	CurrentPage.validation = function () {
		return Validator.Validate(document.forms[0],5);
	}
	
	CurrentPage.initValideInput = function () {
		document.getElementById('bean.deptCd').dataType = 'Require';
		document.getElementById('bean.deptCd').msg =  msgInfo_.getMsgById('HR_I068_C_1',['部门编码']);
		document.getElementById('bean.deptName').dataType = 'Require';
		document.getElementById('bean.deptName').msg =  msgInfo_.getMsgById('HR_I068_C_1',['部门名称']);
		document.getElementById('bean.expectPerson').dataType = 'Integer';
		document.getElementById('bean.expectPerson').msg =  msgInfo_.getMsgById('HR_I069_C_1',['部门定员人数']);
		document.getElementById('bean.showSequence').dataType = 'Integer';
		document.getElementById('bean.showSequence').msg = msgInfo_.getMsgById('HR_I069_C_1',['显示顺序']);
	}
	
	CurrentPage.initValideInput();
	
	CurrentPage.ValidateAll = function () {
    	var success=true;
    <%--
    if ($('bean.webAdress').value!=""){
    	if (CurrentPage.CheckURL($('bean.webAdress').value)==false)
           {
             Validator.warnMessage ( '网站地址格式错误'); 
             success=false;
           } 
	}
	--%>
	<%--
  	if ($('bean.email').value!=""){ 
    	if (CurrentPage.CheckEmail($('bean.email').value)==false)
           {
             Validator.warnMessage (msgInfo_.getMsgById('HR_I078_C_1',['电子邮箱'])); 
             success=false;
           } 
   }
   if($('bean.telephone').value!=""){
   		if (CurrentPage.CheckPhone($('bean.telephone').value)==false)
           {
             Validator.warnMessage ( msgInfo_.getMsgById('HR_I077_C_1',['联系电话'])); 
             success=false;
           }
   }
   if($('bean.fax').value!=""){
   		if (CurrentPage.CheckPhone($('bean.fax').value)==false){
             Validator.warnMessage ( msgInfo_.getMsgById('HR_I079_C_1',['传真'])); 
             success=false;
        }
   	}
   	--%>
   	return success;
}
	
	 //验证URL
	 CurrentPage.CheckURL=function (url){
	    var pattern= /^http:\/\/[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\':+!]*([^<>\"\"])*$/;
	    if (!pattern.test(url))
	    {
	         return false;             
	    }
	    else
	     return true;
	}
	 //验证 Email 
	 CurrentPage.CheckEmail=function (email){
	    var pattern=/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
	    if (!pattern.test(email))
	    {
	      return false;             
	    }
	    return true;
	}
	//验证 Phone, Fax 
	CurrentPage.CheckPhone=function (number){
	    var pattern=/^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/;
	    if (!pattern.test(number))
	    {
	      return false;
	    }
	    return true;
	}
	
	CurrentPage.create = function() {
		$('bean.deptCd').value = '';
		$('oid').value = '';
		$('step').value = 'edit';
		FormUtils.post(document.forms[0], '<c:url value='/sm/TblSmDeptAction.do'/>');
	}

	CurrentPage.CheckNum_expectPerson = function(name){
		var str=name.value
        if (!isNaN(str)||!str=="")
     	{
			var number = parseInt(str);	
			if (number >= 0 && number <= 10000 || str=="")
			{
				return true;
			}else{
				Validator.warnMessage(msgInfo_.getMsgById('HR_I071_C_2',['部门定员人数','0~10000']));
				name.focus();
         		return false;
         	}
      	}else{	
 			return false;  		
 	  	}
	}
	
	CurrentPage.CheckNum_showSequence = function(name){
		var str=name.value
        if (!isNaN(str)||!str=="")
     	{
			var number = parseInt(str);	
			if (number >= 0 && number <= 1000 || str=="")
			{
				return true;
			}else{
				Validator.warnMessage(msgInfo_.getMsgById('HR_I071_C_2',['显示顺序','0~1000']));
				name.focus();
         		return false;
         	}
      	}else{	
 			return false;  		
 	  	}
	}
	function Validator.afterSuccessMessage(){
		if(parent.codeTree){
			parent.codeTree.Update(parent.codeTree.SelectId);
		}
	}
	<%--负责人选择--%>
	CurrentPage.selectEmp = function(empInfoPre){
		definedWin.openListingUrl(empInfoPre,'<c:url value='/sm/SelectInfoAction.do?step=selectInfo'/>');
	}
</script>
</form>
</body>
<script type="text/javascript" src = "<c:out value = "${initParam['publicResourceServer']}/js/checkonchangevalues.js" />"></script>
</html>
