<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf"/> 
<html>
<body>
<script language="javascript">
	var CurrentPage = {};
	/**---------------------------------------------------------------------
	| ����ɾ������->��������ɾ����ťʱִ�еĲ���
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
	| ��������->ͨ���㶥���ġ���������ťʵ�֡�
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
	| ��������->ͨ���㶥���ġ���������ťʵ�֡�
	----------------------------------------------------------------------*/
	CurrentPage.query = function() {
		if (document.getElementsByName("paginater.page") != null) {
	    	document.getElementsByName("paginater.page").value = 0;
			}

		FormUtils.post(document.forms[0], '<c:url value="/uf/news/NewsPublishAction.do?step=${theForm.step}"/>');
	}
	/*-----------------------------------------------------------------------------------
	|������  ��Global.afterOnload()
	|�������ܣ�ҳ�������ɺ���еĴ���(����б�Ϊ�գ����Զ���ת������)��
	|----------------------------------------------------------------------------------*/
	Global.afterOnload = function(){
//	    var tbl  = listtable;
//    	if (tbl.rows.length < 2){ //��ͷ��һ��
//    	   CurrentPage.create();
//    	}
	}
	/**----------------------------------------------------------------------------
	* ����ɾ��ѡ�е���->ͨ����������ɾ������ťʵ��
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
		    alert("��ѡ��Ҫɾ�����У�");
		    return;
		}
	    if (!confirm(' ��ȷ��Ҫɾ��ѡ�е����м�¼�� ? ')) {
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
	| ����->ͨ�����õġ����桱��ť��ɾ�������ύ���ݿ�
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
	        alert("û��Ҫɾ�����У���ȷ�ϣ�");
	        return;
	    }
	    var selectId = ""
		if (!confirm("ȷ���Ƿ�ɾ������Ӱ��������ύ�����ݿ⣿��")) {
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
	| ������ʾ--�����б���Ŀ�ȣ�ÿҳ��ʾ��������ʾ������
	-----------------------------------------------------------------------------*/
/*	CurrentPage.customizeDisplay = function(){
	    alert("Customize Display");
	    var tblList = document.getElementById("listtable");
	    i = 2;
	    if(!tblList.childNodes[0].childNodes[0].childNodes[i])   
            {alert("�����ڸ��У�");   
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
	| �ڷ��������ز�����ʾ��Ϣ�������еĲ���->���»�����ʾ
	-----------------------------------------------------------------------------*/
	/*function Validator.afterSuccessMessage(){
		if(parent.codeTree){		    
			parent.codeTree.Update(parent.codeTree.SelectId);
			//parent.panel.click(0);
		}
	}*/
	/**--------------------------------------------------------------------------
	| ���ұ����Ҫ�������С�
	| ����e���¼�Դ�����߻��߲����¼�Դ�ߡ�
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
				<span class="switch_open" onClick="StyleControl.switchDiv(this, $('divId_scrollLing'))" title="����������">��Ϣ�б�</span>
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
				<td nowrap="nowrap" width="10%" align="center">������Ŀ</td>
				<td nowrap="nowrap" width="30%" align="center">����</td>
				<td nowrap="nowrap" width="15%" align="center">�ؼ���</td>
				<td nowrap="nowrap" width="10%" align="center">�Ƿ��Ƽ�</td>
				<td nowrap="nowrap" width="10%" align="center">�Ƿ񷢲�</td>
				<td nowrap="nowrap" width="10%" align="center">����ʱ��</td>
				<td nowrap="nowrap" width="8%" align="center">��Ч��</td>
				<td nowrap="nowrap" width="7%" align="center">����</td>
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
                    &nbsp;<input type="button" class="list_delete" onclick="CurrentPage.remove('<c:out value="${item.id}"/>')" title="���ɾ��" <c:if test="${!item.canEdit}">style="display:none"</c:if> />&nbsp;
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
