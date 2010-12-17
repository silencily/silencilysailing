<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>

<html>
	<head>
		<jsp:directive.include file="/decorators/default.jspf" />
		
	</head>

	<body>
		<form name="f" method="post">
		<div class="list_explorer">
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td>
					<span class="switch_open" onClick="StyleControl.switchDiv(this, listtable)"  title="点击收缩表格">栏目列表</span>
				</td>
				<td align="right">
			    </td>
			</tr>
		</table>
		</div>

        <div id="divId_scrollLing" class="list_scroll">
			<table width="100%" border="1" cellpadding="0" cellspacing="0" class="Listing"
				id="listtable" onClick="TableSort.sortColumn(event)">
				<thead>
					<tr>
						<td nowrap="nowrap" width="50">&nbsp;</td>
						<td nowrap="nowrap">栏目名称</td>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="item" items="${theForm.listDesktop}"
						varStatus="status">
						<tr class="">
							<td class="box">
								<div class="font_money">							
									<input type="button" class="list_create" onclick="experBeanController.addListingRow(listtable);" id="experBeanController_[<c:out value="${status.index}"/>].add" <c:if test="${item.columnFlg!=0}">style="display:none"</c:if> /> 									
									<input type="button" class="list_delete" onClick="experBeanController.delRowCur(listtable,this);return false" id="experBeanController_[<c:out value="${status.index}"/>].del" <c:if test="${item.columnFlg!=0}">style="display:none"</c:if> row="<c:out value="${status.index}"/>" name="delBtn" title="删除"/>
									<input type="button" class="list_renew" onClick="experBeanController.renRowCur(listtable,this);return false" id="experBeanController_[<c:out value="${status.index}"/>].old" style="display:none" row="<c:out value="${status.index}"/>" name="oldBtn" holdObj=true title="恢复"/>
								</div>
							</td>
							<td width="14%" nowrap="true">
								<input type="hidden" name="desk[<c:out value="${status.index}"/>].id" value="<c:out value="${item.id}"/>" id="experBeanController_[<c:out value="${status.index}"/>].id"/>
								<input type="hidden" name="desk[<c:out value="${status.index}"/>].delFlg" value="<c:out value="${item.delFlg}"/>"/>
								<input type="hidden" name="desk[<c:out value="${status.index}"/>].version" value="<c:out value="${item.version}"/>"/>
								<input type="hidden" name="desk[<c:out value="${status.index}"/>].columnFlg" value="<c:out value="${item.columnFlg}"/>"/>
								<input type="hidden" name="hasSon[<c:out value="${status.index}"/>]" 
								<c:forEach var="itemOrder" items="${item.tblUfColumnOrders}" varStatus="statusOrder">
								    <c:if test="${statusOrder.last}">										
										value="<c:out value="${statusOrder.count}">0</c:out>"	
									</c:if>										
								</c:forEach>
								/>
								<input type="text" name="desk[<c:out value="${status.index}"/>].columnNm" value="<c:out value="${item.columnNm}"/>" style="width:600px" />				
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			</div>
		</form>
	<script type="text/javascript">
	
		var CurrentPage = {};
		
		var newcount = document.getElementById("listtable").getElementsByTagName("tr").length -1;
		/*CurrentPage.upload = function() { 
			var testTbl =  document.getElementById("listtable");
			var newTr = testTbl.insertRow();			
			var cellhtml;
			//添加2列 
			var newTd0 = newTr.insertCell(); 
			var newTd1 = newTr.insertCell(); 
			//设置列内容和属性 
			cellhtml = '<center><input type="checkbox" name="checkbox' + newcount + '" value="checkbox" checked/>';
			newTd0.innerHTML = cellhtml;
			cellhtml = '<input name="desk[' + newcount + '].columnNm" type="text" style="width:600px" />';
			newTd1.innerHTML = cellhtml;
			newcount++;
		//	Global.setHeight();
		}*/
		
		// 注意, 动态生成行必须放在 CurrentPage.initValidateInfo 方法后以保证验证信息被复制
		var experBeanController = new EditPage("experBeanController");
		
		// 设置固定不变的行数
		experBeanController.holdRow = 0;
		experBeanController.uuidObj   = "desk[2].id";
		experBeanController.rowNumber = 7;
		
		experBeanController.addListingRow = function(tbl){ 
		    var testTbl =  document.getElementById("listtable");
		    var newTr = testTbl.insertRow();	
		    newTr.id="experBeanController_trId_[" + newcount + "]";
			var cellhtml;
			//添加2列 
			var newTd0 = newTr.insertCell(); 
			var newTd1 = newTr.insertCell();
			//设置列内容和属性 
			cellhtml = 	'<div class="font_money">'				+
			"<input type=button class='list_create' onclick='experBeanController.addListingRow(listtable);' id=experBeanController_[" + newcount + "].add  /> " +									
			'<input type="button" class="list_delete" onClick="experBeanController.delRowCur(listtable,this);return false" id="experBeanController_[' + newcount + '].del"  row="' + newcount + '" name="delBtn" title="删除"/>' +
			'<input type="button" class="list_renew" onClick="experBeanController.renRowCur(listtable,this);return false" id="experBeanController_[' + newcount + '].old" style="display:none" row="'+ newcount + '" name="oldBtn" holdObj=true title="恢复"/></div>';
			newTd0.innerHTML = cellhtml;
			cellhtml = 
			"<input type=hidden name='desk[" + newcount + "].id'  id='experBeanController_[" + newcount +"].id'/>"  +
			"<input type=hidden name='desk[" + newcount + "].delFlg' value=0>" + 
			"<input type=hidden name='desk[" + newcount + "].version' />" +
			"<input type=hidden name='desk[" + newcount + "].columnFlg' value='0'/>" + 
			"<input type=hidden name='hasSon[" + newcount + "]' />" 			+
			'<input name="desk[' + newcount + '].columnNm" type="text" style="width:600px" />';
			newTd1.innerHTML = cellhtml;
			newcount++; 
			Global.setHeight();
		}	
		experBeanController.delRowCur = function (table,sel){
			var inum = sel.row;
			
			var sonNum = document.getElementById("hasSon["+inum+"]").value;
			
			if (sonNum > 0){
			    alert("该栏目已经有用户定制使用，如果要删除请取消定制后再删除！");
			    return false;
			}
					
			var del = document.getElementById("desk["+inum+"].delFlg").value;
			
			var str = document.getElementById(experBeanController.name+"_["+inum+"].id").value;
			
			var obj = document.getElementById(experBeanController.name+"_trId_["+inum+"]");
			experBeanController.afterDeleteRow(table,sel)
			if(str ==""){
				if(inum == 0){
					experBeanController.disableButton(obj);
					experBeanController.switchButton(inum,"none","none","")
				}else{
					table.deleteRow(obj.rowIndex);
					Global.setHeight();
				}
			}else{
			
				//experBeanController.disableButton(obj);
				obj.className = "disabled";
				document.getElementById("desk["+inum+"].delFlg").value="1";
				experBeanController.switchButton(inum,"none","none","")
			}
		}
		
		experBeanController.renRowCur = function (table,sel){
			var inum = sel.row;
			document.getElementById("desk["+inum+"].delFlg").value="0";
			experBeanController.renRow(table,sel);
		}
			
	
		/*-----------------------------------------------------------------------------------
		|函数名  ：CurrentPage.submit()
		|函数功能：完成页面提交前的检查（栏目重复名检查）
		|----------------------------------------------------------------------------------*/	
		CurrentPage.submit = function () {
	        var i = 0;
	        var objTxt = document.getElementsByName("desk[" + i + "].columnNm")[0];
	        for(;;){
	           if (typeof objTxt == "object"){
	              i = i + 1;
	              objTxt = document.getElementsByName("desk[" + i + "].columnNm")[0];
	           }else{
	              break;
	           }
	        }
			for (var j = 0;j < i;j++){
			    objTxt = document.getElementsByName("desk[" + j + "].columnNm")[0];
			    if ((objTxt.value == "")||(objTxt.value.trim().length == 0)){
			        alert("栏目名称不能为空，请填写");
			        objTxt.focus();
			        return false;
			    }
			    for (var k = j + 1; k<i;k++){			    			    
				    if (objTxt.value == document.getElementsByName("desk[" + k + "].columnNm")[0].value){
				       alert("栏目名称重复，请更改！");
				       objTxt.focus();
				       return false;
				    }		    
				}    
			}
			FormUtils.post(document.forms[0], '<c:url value='/uf/desk/ColumnManageAction.do?step=update'/>');
		}
	</script>

	</body>
</html>
