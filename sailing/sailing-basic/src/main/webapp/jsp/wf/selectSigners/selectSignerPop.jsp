<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<!DOCTYPE   HTML   PUBLIC   "-//W3C//DTD   HTML   4.0   Transitional//EN " >
<jsp:directive.include file="/decorators/default.jspf"/>
<script type="text/javascript">
var msgInfo_ = new msgInfo();
if (CurrentPage == null) {
  var CurrentPage = {};
}  

CurrentPage.query = function() {
		if (document.getElementsByName("paginater.page") != null) {
	    	document.getElementsByName("paginater.page")[0].value = 0;
			}
		FormUtils.post(document.forms[0], '<c:url value="/wf/CommonAction.do?step=querySigners&className=${param.className}&oid=${param.oid}&stepId=${param.stepId}"/>');
		}

</script>
<html>
<body class="list_body">
<div class="main_title">
<div>待办人选择</div>
</div>
<input type="hidden" name="paginater.page" value="<c:out value='${theForm.paginater.page}'/>"/>
<input type="hidden" name="step" value="querySigners" />
<div class="list_explorer">
  <table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
      <td>
        <span class="switch_open" onClick="StyleControl.switchDiv(this, $('metersTable'))" title="点击收缩表格">
           已选择的待办人
        </span>
      </td>
      <td>
          <div class="list_button">
		   <input type="button" class="opera_normal" value="添加" title="添加到本次选择范围" onclick="AddChecked()" />
		   <input type="button" class="opera_normal" value="确定" title="添加到本次选择范围" onclick="returnSelect()" />
		   <input type="button" class="opera_normal" value="取消" title="查看已选择的待办人"  onclick="window.close()"/>      
 		</div>     
      </td>
    </tr>
  </table>
</div>
<div id="divId_scrollLing" class="list_scroll">
  <table border="0" cellpadding="0" cellspacing="0" class="Listing" id="metersTable">
    <thead>
      <tr>
		<td nowrap="nowrap">&nbsp;</td>
        <td nowrap="nowrap">员工编号</td>
        <td nowrap="nowrap">姓名</td>
        <td nowrap="nowrap">职务</td>
        <td nowrap="nowrap">所属部门</td>
      </tr>
    </thead>
    <tbody>
    </tbody>
  </table>
</div>
<input type="hidden" name="step" value="<c:out value = "${theForm.step}" />" /> 

	<div class="list_explorer">
  <table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
      <td>
        <span class="switch_open" onClick="StyleControl.switchDiv(this, $('meterTable'))" title="点击收缩表格">
           待办人列表
        </span>
      </td>
    </tr>
  </table>
</div>
<div id="divId_scrollLing" class="list_scroll">
  <table border="0" cellpadding="0" cellspacing="0" class="Listing" id="meterTable" >
    <input type="hidden" name="checkall" onClick="CurrentPage.selectedAll()" title="是否全选"/>
    <thead>
      <tr>
        <td nowrap="nowrap"><input id='detailIdsForPrintAll' type='checkbox' onclick="FormUtils.checkAll(this,document.getElementsByName('oid'))" title="是否全选"/></td>
        <td nowrap="nowrap">员工编号</td>
        <td nowrap="nowrap">姓名</td>
        <td nowrap="nowrap">职务</td>
        <td nowrap="nowrap">所属部门</td>
      </tr>
    </thead>
    <tbody>
         <c:forEach items = "${signers}" var = "item" varStatus = "status" >
				<tr id="requQues_trId_[<c:out value='${status.index}'/>]" >
				
					<td nowrap="nowrap" align="center">
					<input type="hidden" id="mtlist[<c:out value='${status.index}'/>].id" value="<c:out value='${item.empCd}' />" name="requQues_[<c:out value='${status.index}'/>].id"/>
					<input id='detailIdsForPrintAll' 
					onclick="FormUtils.check($('detailIdsForPrintAll'),document.getElementsByName('oid'))"  
					type="checkbox" name="oid"  value="<c:out value = "${item.empCd}" />" />
				
					</td>				
					<td>
						<c:out value='${item.empCd}' />&nbsp;
					</td>
					<td>
						<c:out value='${item.empName}' />&nbsp;
					</td>
					<td>
						<c:out value='${item.affiliateSpecialty.name}' />&nbsp;
					</td>	
					<td>
						<c:out value='${item.tblCmnDept.deptName}' />&nbsp;
					</td>
				</tr>
	        </c:forEach>
    </tbody>
  </table>
</div>
<div class="list_bottom">
				<c:set var="paginater.forwardUrl" value="/wf/CommonAction.do?step=querySigners&className=${param.className}&oid=${param.oid}&stepId=${param.stepId}" scope="page" />
				<%@ include file="/decorators/paginater.jspf"%>
				
			</div>

<script type="text/javascript">  
  if (CurrentPage == null) {
    var CurrentPage = {};
  }  


  CurrentPage.validation = function () {
    return Validator.Validate(document.forms[0], 4);
  }
  
  function viewSelectedReqPlans(buttonObject) {
    if (buttonObject.value == '查看') {
      buttonObject.value = '隐藏';
      buttonObject.title = '隐藏已选择的待办人';
      //document.getElementById('batchSelectedReqPlansDiv').style.display = '';  
    } else {
      buttonObject.value = '查看';
      buttonObject.title = '查看已选择的待办人';
      //document.getElementById('batchSelectedReqPlansDiv').style.display = 'none';      
    }
    Global.setHeight();
  }
  var listId ="";
   function AddChecked(){
        var tblCheck = document.getElementById('meterTable');
        var tblSelect = document.getElementById('metersTable');
        var index = tblSelect.getElementsByTagName("tr").length-1;
        for (var i=1 ;i<tblCheck.getElementsByTagName("tr").length;i++){
           
            if (document.getElementsByName("oid")[i - 1].checked ){
            	var oid = document.getElementsByName("oid")[i - 1].value;
            	if (listId.indexOf(","+oid,0) != -1){
            		continue;
            	}
            	listId = listId+","+oid+",";
                var insTr = tblSelect.insertRow();
                var cheTr = tblCheck.rows(i);
                for (var j = 0;j < cheTr.getElementsByTagName("td").length;j ++){
                	var insTd = insTr.insertCell();
                	if (j == 0){
                		var id = document.getElementById('mtlist['+(i-1)+'].id').value;
                	    var idhtml = '<input type="hidden" name="detailId_'+(index++)+'" value="'+id+'"/>';
                	    var delhtml = '<input type="button" row="'+oid+'" class="list_delete"title="删除" onclick="delSelectRow(this);"/>';
                	    insTd.innerHTML = idhtml+delhtml;
						insTd.align="center";
                	}else{
                	    insTd.innerHTML = cheTr.cells(j).innerHTML;
                	}
                }
                    
            }
        }
		parent.document.getElementById("windowFrame").style.height=document.body.scrollHeight+1+"px";
    }

 function delSelectRow(item){
        var tblSelect = document.getElementById("metersTable");
        var curRow = item.parentNode.parentNode;
        tblSelect.deleteRow(curRow.rowIndex); 
        var t = ","+item.row;
       var s = listId.indexOf(t,0);
		
        if (s != -1){
        	
            listId = listId.substring(0,s)+listId.substring(s+t.length,listId.length);
       
        }
    }
function returnSelect(){			

		var rows=new Array();
		var dd=document.getElementsByName('detailIds');
		
		var tblSelect = document.getElementById('metersTable');
        for (var i=1 ;i<tblSelect.getElementsByTagName("tr").length;i++){
           var fields = new Array();
           var cheTr = tblSelect.rows(i);
           var tds = cheTr.getElementsByTagName("td");
			
           fields[0] = document.getElementsByName('detailId_'+(i-1))[0].value;
           fields[1] = tds[1].innerText;
           fields[2] = tds[2].innerText;
           fields[3] = tds[3].innerText;
		   fields[4] = tds[4].innerText;
           
           rows[i-1] = fields; 
        }
		if (rows.length==0){
			alert(msgInfo_.getMsgById('SM_I008_A_0'));
		}else{
			window.returnValue=rows;	
			window.close();	
			}
	}	
</script>

</body>
</html>

