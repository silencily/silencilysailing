<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>

<jsp:directive.include file="/decorators/default.jspf" />
<html>
	<head>
<!-- 
		
-->

<meta http-equiv="Content-Type" content="text/html; charset=GBK"/>

		<script type="text/javascript">    
    //--ȡ��select��ֵ--
	//----------------------------------------------------------------------------------------------------------
	if (CurrentPage == null) {
		var CurrentPage = {};
	}
	/*-----------------------------------------------------------------------------------
	|������  ��CurrentPage.submit()
	|�������ܣ����ҳ���ύǰ�ļ�飨������ʾ���ֻ��ѡ���������������ʱҳ���ύ
	|----------------------------------------------------------------------------------*/	
	CurrentPage.submit = function () {
		var i=0;
		var chkBox = document.getElementsByName("chk2");
		//����ʱ����Ƿ�Ϊ������ż����
		for (var j = 0;j < chkBox.length;j++){		
		    if (chkBox[j].checked){ i = i + 1;}
		}
		if (i % 2 != 0){
		    alert("���涨�Ʊ�����ż����㶨����" + i + "������¶��ƣ�");
		    return false;
		}
		//����Ƿ�û�����룬������Ϊ1-6֮������֣������˳���Ƿ��ظ���
		var patrn = /^\d{2}$/;
		for (var j = 0;j < chkBox.length;j++){
		    var objTxt = document.getElementsByName("desk[" + j + "].displaySort")[0];
		    if (!objTxt.readOnly){
			    if (!patrn.test(objTxt.value)){
			       alert("��ʾ˳�������д�ұ�������λ������");
			       objTxt.focus();
			       return false;
			    }
			    if (checkDuple(objTxt.value,j)){
			       alert("��ʾ˳�����ظ������������롣");
			       //objTxt.focus(); 
			       return false;
			    }
		    }
		}
		FormUtils.post(document.forms[0], '<c:url value='/uf/desk/TblColumnOrderAction.do?step=update'/>');
	}
	/*-----------------------------------------------------------------------------------
	|������  ��checkDuple()
	|�������ܣ������ʾ˳���Ƿ��ظ����ظ������棬���򷵻ؼ١�
	|----------------------------------------------------------------------------------*/	
	checkDuple = function(strValue,i){
	    var checkBox = document.getElementsByName("chk2");
	    for (var x = 0; x < checkBox.length;x++){
	        if (x == i){
	           continue;
	        }
	        var objTxt = document.getElementsByName("desk[" + x + "].displaySort")[0];
	        if (!objTxt.readOnly){
		        if (objTxt.value == strValue){
		            return true; 
		        }
	        }
	    }
	    return false;
	}
</script>
	</head>

	<body>
		<form name="f" method="post">
			<div class="main_title">				
				��������
			</div>
			<input name="step" value="list" type="hidden" />
			<input name="infoInformation.columnId" value="news_1" type="hidden" />
			<input name="paginater.page" value="0" type="hidden" />
			<input name="paginater.pageSize" value="15" type="hidden" />

			<div class="list_explorer">
				<span class="switch_open"
					onClick="StyleControl.switchDiv(this,listtable)" title="����������">
					������Ŀ�б� </span>
			
			</div>
			<table border="0" cellpadding="0" cellspacing="0" class="Listing"
				id="listtable">
				<thead>
					<tr>
						<td align="center" width="14%" onClick="TableSort.sortColumn(event);doAfterSort();">
							��Ŀ����
						</td>
						<td align="center" width="14%">
							�Ƿ��ѡ
						</td>
						<td width="19%" align="center" onClick="TableSort.sortColumn(event);doAfterSort();">
							��ʾ˳��
						</td>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="item" items="${theForm.listDesktop}"
						varStatus="status">
						<tr>
							<td width="14%" nowrap="true">
								<input type=hidden name="desk[<c:out value="${status.index}"/>].sequenceNo"
									value="<c:out value="${item.sequenceNo}"/>">
								<input type=hidden
									name="desk[<c:out value="${status.index}"/>].delFlg"
									value="<c:out value="${item.delFlg}"/>">
								<input type=hidden
									name="desk[<c:out value="${status.index}"/>].id"
									 <c:forEach var="itemOrder" items="${item.tblUfColumnOrders}" varStatus="statusOrder">
									    <c:if test="${itemOrder!=null&&itemOrder.empCd==theForm.loginCd}">										
											value="<c:out value="${itemOrder.id}">0</c:out>"	
										</c:if>										
										</c:forEach>	
									>
								<input type=hidden
								    name="parentid[<c:out value="${status.index}"/>].id"
								    value="<c:out value="${item.id}" />" >	
								<!-- <c:out value="${item.id}" /> -->								<c:out value="${item.columnNm}" />								
							</td>
							<td width="5%" nowrap="true" align="center">
							    <c:choose>
							        <c:when test='${item.columnFlg!="UF_LMQF_01"}'>
							            <!-- aaaaa -->
										<input type=hidden
											name="desk[<c:out value="${status.index}"/>].isSelect"
											value="1">
									</c:when>
									<c:otherwise>
									   <!-- bbbbb -->
									   <input type=hidden
											name="desk[<c:out value="${status.index}"/>].isSelect"
									   <c:forEach var="itemOrder" items="${item.tblUfColumnOrders}" varStatus="statusOrder">
									    <c:if test="${itemOrder!=null&&itemOrder.empCd==theForm.loginCd}">										
											value="<c:out value="${itemOrder.isSelect}">0</c:out>"	
										</c:if>										
										</c:forEach>	
										>
									</c:otherwise>		
							    </c:choose>
								<c:choose>
									<c:when
										test='${item.columnFlg!="UF_LMQF_01"}'>
										<!-- cccc -->
										<input name="chk2" type="checkbox" checked disabled=true
											onclick="doCheck(<c:out value='${status.index}'/>);" />&nbsp;		</c:when>										
									<c:otherwise>
										<!-- dddd -->
										<input name="chk2" type="checkbox"
										<c:forEach var="itemOrder" items="${item.tblUfColumnOrders}" varStatus="statusOrder">
											<c:if test="${itemOrder!=null&&itemOrder.empCd==theForm.loginCd&&itemOrder.isSelect==1}">
												 checked											
											</c:if>		
										</c:forEach>
										id="chk2[<c:out value='${status.index}'/>]"
										onclick="doCheck(<c:out value='${status.index}'/>);" />&nbsp;
									</c:otherwise>
								</c:choose>
							</td>
							<td nowrap="true" align="center">
								<c:choose>
								    <c:when test='${item.columnFlg!="UF_LMQF_01"}'>
								        <!-- fff -->
								        <input
											name="desk[<c:out value="${status.index}"/>].displaySort"
								        <c:forEach var="itemOrder" items="${item.tblUfColumnOrders}" varStatus="statusOrder">
									        <c:if test="${itemOrder!=null&&itemOrder.empCd==theForm.loginCd}">									       
												value="<c:out value="${itemOrder.displaySort}"></c:out>"
											</c:if>
										</c:forEach>											
										type="text" class="normal" onblur="doChange(this);" maxlength="2"
										onchange="doChange(this);" style="text-align: right;"/>										
								    </c:when> 
									<c:otherwise>
									    <!-- gggg -->
										<input
											name="desk[<c:out value="${status.index}"/>].displaySort"
											type="text"								    
											<c:forEach var="itemOrder" items="${item.tblUfColumnOrders}" varStatus="statusOrder">
										        <c:if test="${itemOrder!=null&&itemOrder.empCd==theForm.loginCd&&itemOrder.isSelect==1}">									    
													value="<c:out value="${itemOrder.displaySort}"></c:out>" class="normal" 
												</c:if>
												<c:if test="${itemOrder!=null&&itemOrder.empCd==theForm.loginCd&&itemOrder.isSelect!=1}">									    
													value="<c:out value="${itemOrder.displaySort}"></c:out>" class="readonly"  READONLY
												</c:if>	
											</c:forEach>	
											onblur="doChange(this);" onchange="doChange(this);" style="text-align: right;" maxlength="2" />
									</c:otherwise>									
								</c:choose>
								<div style="display:none" id="hiddiv[<c:out value="${status.index}"/>]"></div>
								<script type="text/javascript">
									document.getElementById("hiddiv[<c:out value="${status.index}"/>]").innerText
									= document.getElementsByName("desk[<c:out value="${status.index}"/>].displaySort")[0].value;
								</script>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</form>
		<script language="javascript" type="text/javascript">
	/*-----------------------------------------------------------------------------------
	|������  ��doCheck()
	|�������ܣ���ҳ������ѡ��ʱ���еĴ���ѡ��ʱ����ʾ˳�򡱶οɱ༭�����򲻿ɡ�
	|----------------------------------------------------------------------------------*/
	doCheck = function(i){
	    var n = 0;
//    	var objChk = document.getElementsByName("chk2")[i];
    	var objChk = document.getElementById("chk2[" + i + "]");
    	var chkBox = document.getElementsByName("chk2");
		var objHdn = document.getElementsByName("desk[" + i + "].isSelect")[0];
		var objTxt = document.getElementsByName("desk[" + i + "].displaySort")[0];
		if (objChk.checked){
		    /*for (var j=0;j<chkBox.length;j++){
		        if (chkBox[j].checked){ n = n + 1;}
		    }
		    if (n > 6 ){
		        objChk.checked = false
		        alert("�������ֻ����ʾ6������¶��ƣ�");
		        return false;
		    }*/
		    objHdn.value = "1";
		    objTxt.readOnly = false;
		    //objTxt.style.backgroundColor="#ffffff";
		    objTxt.className="normal";
		}else{
		    objHdn.value = "0";
		    objTxt.readOnly = true;
		    //objTxt.style.backgroundColor="#c0c0c0";
		    objTxt.className="readonly";
		}
	}
	doAfterSort = function(){
		var i = 0;
		while (document.getElementsByName("desk[" + i + "].isSelect")[0]) {
			var objHdn = document.getElementsByName("desk[" + i + "].isSelect")[0];
	    	var objChk = document.getElementById("chk2[" + i + "]");
	    	if (objHdn.value == "1") {
	    		objChk.checked = true;
	    	} else {
	    		objChk.checked = false;
	    	}
			i++;
		}
	}
	/*-----------------------------------------------------------------------------------
	|������  ��doChange()
	|�������ܣ��ı������ݸı�ʱ����Ƿ�����,����ʱ���֡�
	|----------------------------------------------------------------------------------*/
	doChange = function(o){
	    var patrn = /^\d*$/;
	    if (!patrn.test(o.value) || o.value == ""){
           o.value = "00";
	    } 
	    patrn = /^\d{2}$/;
	    if (!patrn.test(o.value)){
	       o.value = "0" + o.value;
	    } 
		var txtName = o.name
		var i1 = txtName.indexOf("["); 
		var i2 = txtName.indexOf("]");
		var idx = txtName.substring(i1+1, i2);
		document.getElementById("hiddiv[" + idx + "]").innerText = o.value;
	}
	/*-----------------------------------------------------------------------------------
	|������  ��Global.afterOnload()
	|�������ܣ�ҳ�������ɺ���еĴ���(�����ʾ˳��ֵΪ�գ�������Ϊֻ��)��
	|----------------------------------------------------------------------------------*/
	Global.afterOnload = function(){
	    var n = 0;
    	var chkBox = document.getElementsByName("chk2");		
		for (var j = 0;j < chkBox.length;j++){
		    var objTxt = document.getElementsByName("desk[" + j + "].displaySort")[0];
		    if ((!objTxt.value)&&(!chkBox[j].checked)){
		        objTxt.readOnly = true;
                //objTxt.style.backgroundColor="#c0c0c0";			    
			    objTxt.className="readonly";
		    }
		}
	}
	</script>
	</body>
</html>
