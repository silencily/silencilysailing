<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ include file="/common/taglibs.jsp"%>
<ww:include page = "/common/header.jsp" />


<table width="100%" border="1" class="listtable" cellspacing="0" cellpadding="0">
	<tr height="430">
		<td nowrap>
		<ww:select 
				name = "#request['leftId']"
				list = "#request.remove('leftMap')"
				required = "false"
				id = "eventButton"
				multiple = "true" 
				size = "#request['listSize']" 
			/>
		</td>
		<td class="moveOptions" width=80>
			<input name="moveRight" id="moveRight" type="button" 
			    onclick="moveSelectedOptions(document.getElementById('<ww:property value = "#request['leftId']" />'), document.getElementById('<ww:property value = "#request['rightId']" />'), true)"
			    value="&gt;&gt;"><br />
			<input name="moveAllRight" id="moveAllRight" type="button"
			    onclick="moveAllOptions(document.getElementById('<ww:property value = "#request['leftId']" />'), document.getElementById('<ww:property value = "#request['rightId']" />'), true)"
			    value="全部 &gt;&gt;"><br />
			<input name="moveLeft" id="moveLeft" type="button"
			    onclick="moveSelectedOptions(document.getElementById('<ww:property value = "#request['rightId']" />'), document.getElementById('<ww:property value = "#request['leftId']" />'), true)"
			    value="&lt;&lt;"><br />
			<input name="moveAllLeft" id="moveAllLeft" type="button"
			    onclick="moveAllOptions(document.getElementById('<ww:property value = "#request['rightId']" />'), document.getElementById('<ww:property value = "#request.remove('leftId')" />'), true)"
			    value="全部 &lt;&lt;"><br />
		    </td>
		<td nowrap>
		<ww:select 
				name = "#request.remove('rightId')"
				list = "#request.remove('rightMap')"
				required = "false"
				id = "eventButton"
				multiple = "true" 
				size = "#request['listSize']" 
			/>
		</td>
	</tr>
</table>