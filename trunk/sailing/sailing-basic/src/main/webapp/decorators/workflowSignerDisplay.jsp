<!DOCTYPE   HTML   PUBLIC   "-//W3C//DTD   HTML   4.0   Transitional//EN " >
<jsp:directive.include file="/decorators/default.jspf"/>


<html>
<body>
<%
	
%>
<div class="main_title">
<div>待办人显示</div>
</div>
<form name="form">

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
    <thead>
      <tr>       
        <td nowrap="nowrap">员工编号</td>
        <td nowrap="nowrap">姓名</td>
        <td nowrap="nowrap">职务</td>
        <td nowrap="nowrap">所属部门</td>
      </tr>
    </thead>
    <tbody>
        <% 	String taskId = request.getParameter("taskId");
        	String currStepId = "";
        	String oid = "";
        	String workflowName = "";
        	currStepId = request.getParameter("currStepId");
        	oid = request.getParameter("oid");
        	workflowName = request.getParameter("workflowName");
        	java.util.List list = new java.util.ArrayList();
        	
        	//if (taskId == null || "".equals(taskId) || "null".equals(taskId)) {
        	//	list = com.qware.wf.util.WfCommonTools.getPointedUserOfStepWithoutTaskId(workflowName, currStepId, oid);
        	//} else {
        	//	list = com.qware.wf.util.WfCommonTools.castEmpsByCds(
            //    		com.qware.wf.util.BisWfServiceLocator.getWorkflowService().getPointedUserOfStep(taskId));
        	//}
            list = com.qware.wf.util.WfCommonTools.getPointedUserOfStepWithoutTaskId(workflowName, currStepId, oid);
        	for (int i=0; i<list.size(); i++) {		
        		com.qware.sm.domain.TblCmnUser emp = (com.qware.sm.domain.TblCmnUser)list.get(i);
        %>
        <tr>
			<td>
				<center><%= emp.getEmpCd()%>&nbsp;</center>
			</td>			
			<td>
				<center><%= emp.getEmpName()%>&nbsp;</center>
			</td>
			<td><center>
				<%
				com.qware.hibernate3.CodeWrapperPlus cwp = (com.qware.hibernate3.CodeWrapperPlus)emp.getWorkDuty();
				if (cwp == null) 
					out.print("havn't any code wrapper plus"); 
				else
					out.print(cwp.getName());
				%>
				&nbsp;
				</center>
			</td>	
			<td><center>
				<%
				com.qware.sm.domain.TblCmnDept dept = (com.qware.sm.domain.TblCmnDept)emp.getTblCmnDept();
				if (dept == null)
					out.print("havn't any dept");
				else
					out.print(dept.getDeptName());
				%>	
				&nbsp;
				</center>
			</td>
		</tr>
	     <% }%>
    </tbody>
  </table>
</div>

</body>
</html>

