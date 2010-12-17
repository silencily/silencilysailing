<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf"/>
<html>
<body class="main_body">
<form method="post">
	<input type="hidden" name="oid" value="<c:out value='${theForm.oid}'/>"/>
	<input type="hidden" name="step" value="<c:out value='${theForm.step}'/>"/>
    <input type="hidden" name="workflowStatus" value="scratch"/>

	
	
	<div class="update_subhead">
		<span class=" switch_open" onClick="StyleControl.switchDiv(this, $('orignalPurchaseOrderDiv'))" title="点击收缩表格">从表</span>		
	</div>
	<div id="orignalPurchaseOrderDiv">
        <table border="0" cellpadding="0" cellspacing="0" class="Listing"  id="requirementItemsTable">
        <thead>
        <tr>
            <td>&nbsp;</td>
            <td nowrap="nowrap">序号</td>
            <td nowrap="nowrap">名字</td>
           
        </tr>
        </thead>
        <tbody>
            <c:forEach items="${theForm.hi.hrInfoEms}" var="hi" varStatus="status">
                <c:set var="requirementItemsCount" value="${status.count}"/>
                <tr id="requirementItemsController_trId_[<c:out value="${status.index}" />]">
                    <td>
                    <div class="font_money">
                        <input type="button" class="list_create" onclick="requirementItemsController.addListingRow(requirementItemsTable);" id="requirementItemsController_[<c:out value="${status.index}" />].add" style="display:" />
                        <input type="button" class="list_delete" onclick="requirementItemsController.delRow(requirementItemsTable, this);"  id="requirementItemsController_[<c:out value="${status.index}" />].del" style="display:" row="<c:out value="${status.index}" />" /> 
                        <input type="button" class="list_renew" onclick="requirementItemsController.renRow(requirementItemsTable, this);"   id="requirementItemsController_[<c:out value="${status.index}" />].old" style="display:none" row="<c:out value="${status.index}" />" holdObj="true"/>
                    </div>
                    </td>
                    <td class="font_money" style="width:50px">
                        <input name="hi.hrInfoEms[<c:out value="${status.index}"/>].sequenceNo" value="<c:out value='${status.count}'/>" style="width:100%"/>
                    </td>
                    
                    <td>
                        <input name="hi.hrInfoEms[<c:out value="${status.index}"/>].name" value=""  />
                    </td>
                   
                </tr>
            </c:forEach>
        </tbody>
        </table>
	</div>
</form>
<script type="text/javascript">
    if (CurrentPage == null) {
        var CurrentPage={};
    }
    
    CurrentPage.create = function() {
        $('oid').value = '';
        $('step').value = 'edit';
        document.forms[0].action = CurrentPage.getUrl();
        document.forms[0].submit();
    }

    CurrentPage.selectMaterial=function(requirementItemMaterialsPrefix) {
        definedWin.openListingUrl(requirementItemMaterialsPrefix, '<c:url value='/material/materialDepositoryStockpileAction.do?step=entry&workflowStatus=finish'/>');
    }
    
    CurrentPage.initializeWorkflow = function() {
        if (Validator.Validate(document.forms[0], 4)) {
            $('step').value = 'doTransition';
            document.forms[0].action = CurrentPage.getUrl();
            document.forms[0].submit();
//            parent.panel.click(0);
        }
    }
    
    CurrentPage.getUrl = function() {
        var pat = /([^\?]+).*/;
        if (pat.test(location.href)) {
            return RegExp.$1;
        }
        return location.href;
    }
    
    CurrentPage.submit = function() {
        if (Validator.Validate(document.forms[0], 4)) {
            $('step').value = 'save';
            document.forms[0].action = CurrentPage.getUrl();
            document.forms[0].submit();
        }
    }
    
    // 注意, 动态生成行必须放在 CurrentPage.initValidateInfo 方法后以保证验证信息被复制
    var requirementItemsController=new EditPage("requirementItemsController");
    
    // 设置固定不变的行数
    //requirementItemsController.holdRow=1;
    requirementItemsController.uuidObj="hi.hrInfoEms[0].id";
    requirementItemsController.rowNumber=<c:out value="${pageScope['requirementItemsCount']}" default="0" />;
    requirementItemsController.afterAddRow = function(obj) {
        var row = requirementItemsController.rowNumber - 1;
        if ($('hi.hrInfoEms[' + row + '].sequenceNo') != null) {
            $('hi.hrInfoEms[' + row + '].sequenceNo').value = row + 1;
        }
    }
    
    CurrentPage.initValidateInfo = function () {
		
	}
    
    CurrentPage.initValidateInfo();  
    
    CurrentPage.clean = function(idv, namev) {
        $(idv).value = '';
        $(namev).value = '';
    }  
    
    CurrentPage.selectDepartment = function(objId, objName, treeType, departmentId) {
    	TreeUtils.selectAndSetDepartment(objId, objName, treeType, departmentId);
    }
</script>
</body>
</html>
