<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf"/> 
<html>
<body>
<script language="javascript">
	var CurrentPage = {};
	/**---------------------------------------------------------------------
	| 单个删除操作->点表格最后的删除按钮时执行的操作
	----------------------------------------------------------------------*/
	var msgInfo_ = new msgInfo();
	if (CurrentPage == null) {
   		var CurrentPage = {};
	}

	CurrentPage.remove = function(oid) {
		if (!confirm(msgInfo_.getMsgById('UF_I012_A_0'))) {
			return false;
		} 
		FormUtils.post(document.forms[0], '<c:url value = "/uf/news/NewsPublishAction.do?step=delete&oid="/>'+ oid );
	}
	/**---------------------------------------------------------------------
	| 新增操作->通过点顶部的“新增”按钮实现。
	----------------------------------------------------------------------*/
	CurrentPage.create = function() {
		if($('oid')){		
			$('oid').value = '';
		}
		$('step').value = 'info';
		TableSort.setNoSelect();
		TableSort.dblClick();
	}
	/**---------------------------------------------------------------------
	| 新增操作->通过点顶部的“新增”按钮实现。
	----------------------------------------------------------------------*/
	CurrentPage.query = function() {
		if (document.getElementsByName("paginater.page") != null) {
	    	document.getElementsByName("paginater.page").value = 0;
			}

		FormUtils.post(document.forms[0], '<c:url value="/uf/news/NewsPublishAction.do?step=${theForm.step}"/>');
	}
	/*-----------------------------------------------------------------------------------
	|函数命  ：Global.afterOnload()
	|函数功能：页面加载完成后进行的处理(如果列表为空，则自动跳转到新增)。
	|----------------------------------------------------------------------------------*/
	Global.afterOnload = function(){
//	    var tbl  = listtable;
//    	if (tbl.rows.length < 2){ //表头算一行
//    	   CurrentPage.create();
//    	}
	}
	/**----------------------------------------------------------------------------
	* 批量删除选中的行->通过按“批量删除”按钮实现
	-----------------------------------------------------------------------------*/
/*	CurrentPage.batchDelete = function(){
   		var tblList = document.getElementById("listtable");
   		var delflg = false;
		//var selectId = "";
		var chkbox = document.getElementsByName("detailIds");
		for (var i = 0;i < chkbox.length;i++){
		    if (chkbox[i].checked){
		       delflg = true;
		    }
		}
		if (delflg==false){
		    alert("请选择要删除的行！");
		    return;
		}
	    if (!confirm(' 你确定要删除选中的所有记录吗 ? ')) {
			return false;
		} 
		for (var i = 0;i < chkbox.length;i++){
		    if (chkbox[i].checked){ 
		        chkbox[i].disabled = true;
		        tblList.rows( findRow(chkbox[i]).rowIndex).disabled=true;
				tblList.rows( findRow(chkbox[i]).rowIndex).className="disable";
		/*        if (selectId == ""){
		            selectId = chkbox[i].value;
		        }else{
		            selectId = selectId + "|" + chkbox[i].value;
		        }*/
		/*    }
		}
		//FormUtils.post(document.forms[0], '<c:url value = "/am/TblAmEqAssettypeAction.do?step=delete"/>&oid=' + selectId + "&parentCode=" + $('parentCode').value);
		/*if(parent.codeTree){
			parent.codeTree.Update(parent.codeTree.SelectId);
		}*/
/*	}*/
	/**----------------------------------------------------------------------------
	| 保存->通过公用的“保存”按钮将删除操作提交数据库
	-----------------------------------------------------------------------------*/
/*	CurrentPage.submit = function(){	    
	    var chkbox = document.getElementsByName("detailIds");
	    var delflg = false
	    for (var i = 0; i < chkbox.length;i++){
	        if (chkbox[i].disabled==true){
	            delflg = true;
	        }
	    }
	    if (delflg == false){
	        alert("没有要删除的行，请确认！");
	        return;
	    }
	    var selectId = ""
		if (!confirm("确定是否将删除操作影响的数据提交到数据库？？")) {
	        return false;
	    }
	    for (var i = 0;i < chkbox.length;i++){
		    if (chkbox[i].checked&&chkbox[i].disabled){ 
		        if (selectId == ""){
		            selectId = chkbox[i].value;
		        }else{
		            selectId = selectId + "|" + chkbox[i].value;
		        }
		    }
		}
		FormUtils.post(document.forms[0], '<c:url value = "/am/TblAmEqAssettypeAction.do?step=delete"/>&oid=' + selectId + "&parentCode=" + $('parentCode').value);
	    
	} */
	/**----------------------------------------------------------------------------
	| 定制显示--控制列表表格的宽度，每页显示行数，显示列数等
	-----------------------------------------------------------------------------*/
/*	CurrentPage.customizeDisplay = function(){
	    alert("Customize Display");
	    var tblList = document.getElementById("listtable");
	    i = 2;
	    if(!tblList.childNodes[0].childNodes[0].childNodes[i])   
            {alert("不存在该列！");   
              return   false;   
            }   
        var l = tblList.rows.length - 1;
        tblList.childNodes[0].childNodes[0].childNodes[i].style.display="none";
	    for (var j = 0;j < l;j++){
	        //tblList.rows[i].Cells(2).visibility="hidden";
	        tblList.childNodes[1].childNodes[j].childNodes[i].style.display="none";
	    }
	} */  
	/**----------------------------------------------------------------------------
	| 在服务器返回操作提示信息后所进行的操作->更新画面显示
	-----------------------------------------------------------------------------*/
	/*function Validator.afterSuccessMessage(){
		if(parent.codeTree){		    
			parent.codeTree.Update(parent.codeTree.SelectId);
			//parent.panel.click(0);
		}
	}*/
	/**--------------------------------------------------------------------------
	| 查找表格内要操作的行。
	| 参数e是事件源发起者或者查找事件源者。
	---------------------------------------------------------------------------*/
/*	function findRow(e) {
		if (e.tagName == "TR") {
		  return e;
		} else if (e.tagName == "BODY") {
		  return null;
		} else {
		  return findRow(e.parentElement);
		}
	}*/
</script>
<form>
<input type="hidden" name="step" value="<c:out value="${theForm.step}"/>"/>
<jsp:directive.include file="newsSearch.jspf"/>
<div class="update_subhead">
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td>
				<span class="switch_open" onClick="StyleControl.switchDiv(this, $('divId_scrollLing'))" title="点击收缩表格">信息列表</span>
			</td>
			<td align="right">			 
			</td>
		</tr>
	</table>
</div>
<div id="divId_scrollLing" class="list_scroll">
	<table border="0" cellpadding="0" cellspacing="0" class="Listing" id="listtable"  onClick="TableSort.sortColumn(event)">
		<thead>
			<tr>
				<td nowrap="nowrap" width="10%" align="center">所属栏目</td>
				<td nowrap="nowrap" width="30%" align="center">标题</td>
				<td nowrap="nowrap" width="15%" align="center">关键字</td>
				<td nowrap="nowrap" width="10%" align="center">是否推荐</td>
				<td nowrap="nowrap" width="10%" align="center">是否发布</td>
				<td nowrap="nowrap" width="10%" align="center">发布时间</td>
				<td nowrap="nowrap" width="8%" align="center">有效期</td>
				<td nowrap="nowrap" width="7%" align="center">操作</td>
			</tr>
		</thead>
		<tbody>
		<c:forEach var="item" items="${theForm.list}" varStatus="status">		
			<tr>
				<td nowrap="nowrap" align="center">
					<input type="hidden" name="oid" value="<c:out value="${item.id}"/>"/>
					<c:out value="${item.tblUfColumn.columnNm}"/>
				</td>
				<td nowrap="nowrap" align="left"><c:out value="${item.title}"/>&nbsp;</td>
				<td nowrap="nowrap" align="left"><c:out value="${item.keyword}"/>&nbsp;</td>
				<td nowrap="nowrap" align="center">
					&nbsp;<c:out value="${item.recommend.name}"/>&nbsp;
                </td>
				<td nowrap="nowrap" align="center">
					&nbsp;<c:out value="${item.published.name}"/>&nbsp;
                </td>
				<td nowrap="nowrap" align="center">
					&nbsp;<fmt:formatDate value="${item.publishTime}" pattern="yyyy-MM-dd HH:mm"/>&nbsp;
				</td>
				<td nowrap="nowrap" align="center">
					&nbsp;<fmt:formatDate value="${item.invalidTime}" pattern="yyyy-MM-dd"/>&nbsp;
				</td>
				<td nowrap="nowrap" align="center">
                    &nbsp;<input type="button" class="list_delete" onclick="CurrentPage.remove('<c:out value="${item.id}"/>')" title="点击删除" <c:if test="${!item.canEdit}">style="display:none"</c:if> />&nbsp;
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table> 
	<div class="list_bottom">
	<c:set var = "paginater.forwardUrl" value = "/uf/news/NewsPublishAction.do?step=list" scope = "page" />
	<%@ include file = "/decorators/paginater.jspf" %>
	</div>
</div>
</form>
</body>
</html>
