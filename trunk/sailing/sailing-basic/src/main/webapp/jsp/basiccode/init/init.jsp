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
		<div>系统编码初期维护</div>
	</div>
		<c:out value="${theForm.message}"/>
		<form name="f">
		<center>
			<table border="0" cellpadding="0" cellspacing="0" class="Listing">
				<tr><td>
					<input type="button" onClick="CurrentPage.init('hr')" value="人事管理" <c:out value="${theForm.flag[0]}"/>>
				</td>
				<td>
					<input type="button" onClick="CurrentPage.init('gr')" value="发电运行" <c:out value="${theForm.flag[1]}"/>>
				</td>
				<td>
					<input type="button" onClick="CurrentPage.init('am')" value="资产管理" <c:out value="${theForm.flag[2]}"/>>
				</td>
				<td>
					<input type="button" onClick="CurrentPage.init('rm')" value="物资管理" <c:out value="${theForm.flag[3]}"/>>
				</td>
				<td>
					<input type="button" onClick="CurrentPage.init('fm')" value="燃料管理" <c:out value="${theForm.flag[4]}"/>>
				</td>
				<td>
					<input type="button" onClick="CurrentPage.init('st')" value="综合统计" <c:out value="${theForm.flag[5]}"/>>
				</td>
				<td>
					<input type="button" onClick="CurrentPage.init('oa')" value="办公自动化" <c:out value="${theForm.flag[6]}"/>>
				</td>
				<td>
					<input type="button" onClick="CurrentPage.init('uf')" value="一体化框架" <c:out value="${theForm.flag[7]}"/>>
				</td>
				<td>
					<input type="button" onClick="CurrentPage.init('sm')" value="系统管理" <c:out value="${theForm.flag[8]}"/>>	
				</td>
				<td>
					<input type="button" onClick="CurrentPage.init('cm')" value="合同管理" <c:out value="${theForm.flag[9]}"/>>	
				</td>
				</tr>
			</table><br>
			<div class="list_explorer">
				<select name="subID" >
					<option  value="HR">人事管理</option>
					<option  value="GR">发电运行</option>
					<option  value="AM">资产管理</option>
					<option  value="RM">物资管理</option>
					<option  value="FM">燃料管理</option>
					<option  value="ST">综合统计</option>
					<option  value="OA">办公自动化</option>
					<option  value="UF">一体化框架</option>
					<option  value="SM">系统管理</option>
					<option  value="CM">合同管理</option>
				</select>
				<input type="button" onClick="a(<c:out value='${subID}'/>)" value="设置为初始状态">
				这个操作将会删除所选子系统基础编码的数据
			</div>
		</center>			
		</form>
	</body>
	<script language="javascript">
		function a(subID){
			if (!confirm(' 这个操作将会删除所选子系统的基础编码的数据, 您确定要继续吗? ')) {
				return false;
			}			
			FormUtils.post(document.forms[0], '<c:url value = "/common/basiccodeinit.do?step=reInit"/>&oid='+ subID);
		}				
	</script>