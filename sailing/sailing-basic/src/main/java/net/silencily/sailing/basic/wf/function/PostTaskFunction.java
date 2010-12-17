package net.silencily.sailing.basic.wf.function;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import net.silencily.sailing.basic.wf.WorkflowInfo;
import net.silencily.sailing.basic.wf.db.WfDBConnection;
import net.silencily.sailing.basic.wf.dto.FlowTaskInfo;
import net.silencily.sailing.basic.wf.entry.WorkflowStep;
import net.silencily.sailing.basic.wf.util.WfUtils;
import net.silencily.sailing.common.context.WorkFlowFormContext;
import net.silencily.sailing.security.SecurityContextInfo;

import org.apache.commons.lang.StringUtils;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

public class PostTaskFunction implements FunctionProvider {

	public void execute(Map tvar, Map args, PropertySet ps)
			throws WorkflowException {
		WfUtils util = new WfUtils();		
		WorkflowInfo wfInfo = (WorkflowInfo) tvar.get("dto");
		List signerList = wfInfo.getSignerList();
		
		boolean isUntread = ("true".equals((String) args.get("untread"))) ? true
				: false;
				
		try {
			WorkflowStep nextStep = new WfUtils().getNextStep(wfInfo);
			
			util.setTransactionConn(WfDBConnection.getConnection());
			util.getTransactionConn().setAutoCommit(false);
			
			if (isUntread) {
				wfInfo.setSignerList(util.getUntreadEmp(wfInfo));
			} else if (util.isPassRoundWithoutSigner(wfInfo.getWorkflowName(), nextStep.getId())) {
				wfInfo.setSignerList(WorkFlowFormContext.getEmpOfRole(nextStep.getRoles()));
			} else if (signerList.size() < 1 && StringUtils.isNotBlank(nextStep.getRoles())) {				
				FlowTaskInfo fti = new FlowTaskInfo();
				fti.setRoleSignFlag("Y");					
				fti.setRoleSignRole(nextStep.getRoles());
				fti.setRoleSignDept("deptName");
				signerList.add(fti);				
				wfInfo.setSignerList(signerList);
			}
			util.setSigner(wfInfo);
			
			util.postFlowTaskInfo(wfInfo,
					SecurityContextInfo.getCurrentUser());
			util.alsoUpdateOtherSigner(wfInfo);
			util.getTransactionConn().commit();
			
			boolean isSendMail = ("true".equals((String) args.get("send.mail"))) ? true
					: false;
			if(isSendMail){
				WorkFlowFormContext.workFlowSendMessage(wfInfo);
			}
		} catch (Exception e) {
			try {
				util.getTransactionConn().rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				throw new WorkflowException();
			}
			e.printStackTrace();
			throw new WorkflowException("setSigner Exception");
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
