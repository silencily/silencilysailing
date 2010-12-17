package net.silencily.sailing.basic.wf.function;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import net.silencily.sailing.basic.wf.WorkflowInfo;
import net.silencily.sailing.basic.wf.db.WfDBConnection;
import net.silencily.sailing.basic.wf.dto.FlowTaskInfo;
import net.silencily.sailing.basic.wf.util.WfUtils;
import net.silencily.sailing.common.context.WorkFlowFormContext;
import net.silencily.sailing.security.SecurityContextInfo;

import org.apache.commons.lang.StringUtils;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

public class PassRoundPostTaskFunction implements FunctionProvider {
	public void execute(Map tvar, Map args, PropertySet ps)
			throws WorkflowException {
		WfUtils util = new WfUtils();
		WorkflowInfo wfInfo = (WorkflowInfo) tvar.get("dto");
		List signerList = wfInfo.getSignerList();
				
		try {
			String roleCode = util.getPassRoundNextRole(wfInfo.getWorkflowName(), wfInfo.getCurrentStep().getId());
			util.setTransactionConn(WfDBConnection.getConnection());
			util.getTransactionConn().setAutoCommit(false);
			if(util.isLaster(wfInfo)){
				if (util.isPassRoundWithoutSigner(wfInfo.getWorkflowName(), util.getPassRoundNextStepId(wfInfo.getWorkflowName(), wfInfo.getCurrentStep().getId()))) {
					wfInfo.setSignerList(WorkFlowFormContext.getEmpOfRole(util.getRoles(wfInfo.getWorkflowName(), util.getPassRoundNextStepId(wfInfo.getWorkflowName(), wfInfo.getCurrentStep().getId()))));
				}else if (signerList.size() < 1 && StringUtils.isNotBlank(roleCode)) {
					FlowTaskInfo fti = new FlowTaskInfo();
					fti.setRoleSignFlag("Y");						
					fti.setRoleSignRole(roleCode);
					fti.setRoleSignDept("deptName");
					fti.setToStepId(util.getPassRoundNextStepId(wfInfo.getWorkflowName(), wfInfo.getCurrentStep().getId()));
					fti.setToStepName(util.getStepName(wfInfo.getWorkflowName(), fti.getToStepId()));
					signerList.add(fti);
					wfInfo.setSignerList(signerList);
				} 
				util.setSigner(wfInfo);		
				
				boolean isSendMail = ("true".equals((String) args.get("send.mail"))) ? true
						: false;
				if(isSendMail){
					WorkFlowFormContext.workFlowSendMessage(wfInfo);
				}
			}
			util.postFlowTaskInfo(wfInfo,
					SecurityContextInfo.getCurrentUser());
			util.getTransactionConn().commit();
		} catch (Exception e) {
			try {
				util.getTransactionConn().rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				throw new WorkflowException();
			}
			e.printStackTrace();
			throw new WorkflowException("passRound Exception");
		} finally {
			try {
				util.getTransactionConn().setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
				throw new WorkflowException();
			}
			if (util.getTransactionConn() != null) {
				try {
					util.getTransactionConn().close();
				} catch (SQLException e) {
					e.printStackTrace();
					throw new WorkflowException();
				}
			}
		}
	}
}
