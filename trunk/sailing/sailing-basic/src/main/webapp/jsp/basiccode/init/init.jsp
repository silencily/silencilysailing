<%--
    @version:$Id: init.jsp,v 1.1 2010/12/10 10:56:48 silencily Exp $
    @since $Date: 2010/12/10 10:56:48 $
--%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<jsp:directive.include file="/decorators/default.jspf" />
	<script language="javascript">
		var CurrentPage = {};		
		CurrentPage.init = function(oid) {			
			FormUtils.post(document.forms[0], '<c:url value = "/common/basiccodeinit.do?step=save"/>&oid='+ oid );
		}		
	</script>	
	<body>
	<div class="main_title">
		<div>ϵͳ�������ά��</div>
	</div>
		<c:out value="${theForm.message}"/>
		<form name="f">
		<center>
			<table border="0" cellpadding="0" cellspacing="0" class="Listing">
				<tr><td>
					<input type="button" onClick="CurrentPage.init('hr')" value="���¹���" <c:out value="${theForm.flag[0]}"/>>
				</td>
				<td>
					<input type="button" onClick="CurrentPage.init('gr')" value="��������" <c:out value="${theForm.flag[1]}"/>>
				</td>
				<td>
					<input type="button" onClick="CurrentPage.init('am')" value="�ʲ�����" <c:out value="${theForm.flag[2]}"/>>
				</td>
				<td>
					<input type="button" onClick="CurrentPage.init('rm')" value="���ʹ���" <c:out value="${theForm.flag[3]}"/>>
				</td>
				<td>
					<input type="button" onClick="CurrentPage.init('fm')" value="ȼ�Ϲ���" <c:out value="${theForm.flag[4]}"/>>
				</td>
				<td>
					<input type="button" onClick="CurrentPage.init('st')" value="�ۺ�ͳ��" <c:out value="${theForm.flag[5]}"/>>
				</td>
				<td>
					<input type="button" onClick="CurrentPage.init('oa')" value="�칫�Զ���" <c:out value="${theForm.flag[6]}"/>>
				</td>
				<td>
					<input type="button" onClick="CurrentPage.init('uf')" value="һ�廯���" <c:out value="${theForm.flag[7]}"/>>
				</td>
				<td>
					<input type="button" onClick="CurrentPage.init('sm')" value="ϵͳ����" <c:out value="${theForm.flag[8]}"/>>	
				</td>
				<td>
					<input type="button" onClick="CurrentPage.init('cm')" value="��ͬ����" <c:out value="${theForm.flag[9]}"/>>	
				</td>
				</tr>
			</table><br>
			<div class="list_explorer">
				<select name="subID" >
					<option  value="HR">���¹���</option>
					<option  value="GR">��������</option>
					<option  value="AM">�ʲ�����</option>
					<option  value="RM">���ʹ���</option>
					<option  value="FM">ȼ�Ϲ���</option>
					<option  value="ST">�ۺ�ͳ��</option>
					<option  value="OA">�칫�Զ���</option>
					<option  value="UF">һ�廯���</option>
					<option  value="SM">ϵͳ����</option>
					<option  value="CM">��ͬ����</option>
				</select>
				<input type="button" onClick="a(<c:out value='${subID}'/>)" value="����Ϊ��ʼ״̬">
				�����������ɾ����ѡ��ϵͳ�������������
			</div>
		</center>			
		</form>
	</body>
	<script language="javascript">
		function a(subID){
			if (!confirm(' �����������ɾ����ѡ��ϵͳ�Ļ������������, ��ȷ��Ҫ������? ')) {
				return false;
			}			
			FormUtils.post(document.forms[0], '<c:url value = "/common/basiccodeinit.do?step=reInit"/>&oid='+ subID);
		}				
	</script>