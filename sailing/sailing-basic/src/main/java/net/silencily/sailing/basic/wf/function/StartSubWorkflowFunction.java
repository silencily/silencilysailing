package net.silencily.sailing.basic.wf.function;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import net.silencily.sailing.basic.wf.WorkflowInfo;
import net.silencily.sailing.basic.wf.db.WfDBConnection;
import net.silencily.sailing.basic.wf.dto.FlowTaskInfo;
import net.silencily.sailing.basic.wf.entry.WorkflowRelation;
import net.silencily.sailing.basic.wf.util.BisWfServiceLocator;
import net.silencily.sailing.basic.wf.util.WfUtils;
import net.silencily.sailing.common.context.WorkFlowFormContext;
import net.silencily.sailing.common.crud.service.CommonService;
import net.silencily.sailing.container.ServiceProvider;

import org.apache.commons.lang.StringUtils;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

public class StartSubWorkflowFunction implements FunctionProvider {
	
	private CommonService getService() {
		return (CommonService)ServiceProvider.getService(CommonService.SERVICE_NAME);
	}
	public void execute(Map tvar, Map args, PropertySet ps)
			throws WorkflowException {
		WorkflowInfo superWfInfo = (WorkflowInfo) tvar.get("dto");
		List signerList = superWfInfo.getSignerList();
		WorkflowInfo subWfInfo = (WorkflowInfo) tvar.get("sub.workflow.pojo");
		if (subWfInfo == null) {
			throw new WorkflowException("the sub workflow pojo is null");
		}
		//String subClass = (String) tvar.get("sub.workflow.pojo.type");
		String subClass = WfUtils.getUnProxyClassName(subWfInfo.getClass().getName());
		String unlockId = (String) args.get("unlock.action.id");
		WorkflowRelation wr = new WorkflowRelation();		
		WfUtils util = new WfUtils();
		//WorkflowInfo subWfInfo = null;
		try {
			util.setTransactionConn(WfDBConnection.getConnection());
			util.getTransactionConn().setAutoCommit(false);
			
			//subWfInfo = (WorkflowInfo)Class.forName(subClass).newInstance();			
			getService().saveOrUpdate(subWfInfo); 			
			
			wr.setSuperOid(superWfInfo.getId());
			wr.setSuperClass(WfUtils.getUnProxyClassName(superWfInfo.getClass().getName()));
			wr.setSubOid(subWfInfo.getId());
			wr.setSubClass(subClass);
			wr.setUnlockId(unlockId);
			wr.setStepId(superWfInfo.getCurrentStep().getId());//父子流
			BisWfServiceLocator.getWorkflowService().initializeSubWorkflow(wr, subWfInfo);	
			//给子流发任务
			subWfInfo.setSignerList(signerList);
			String nextStepRole = BisWfServiceLocator.getWorkflowService().getRoles(subWfInfo.getWorkflowName(), "1");
			
			if(signerList != null && signerList.size() > 0){
				for (int i=0; i<signerList.size(); i++){
					FlowTaskInfo fti = (FlowTaskInfo)signerList.get(i);
					fti.setCurrStepId("superflow_" + superWfInfo.getCurrentStep().getId());
					fti.setCurrStepName("superflow_" + superWfInfo.getCurrentStep().getName());
					fti.setToStepId(subWfInfo.getCurrentStep().getId());
					fti.setToStepName(subWfInfo.getCurrentStep().getName());
				}
			} else if (StringUtils.isNotBlank(nextStepRole)){
				FlowTaskInfo fti = new FlowTaskInfo();
				fti.setRoleSignFlag("Y");
				fti.setCurrStepId("superflow_" + superWfInfo.getCurrentStep().getId());
				fti.setCurrStepName("superflow_" + superWfInfo.getCurrentStep().getName());
				fti.setToStepId(subWfInfo.getCurrentStep().getId());
				fti.setToStepName(subWfInfo.getCurrentStep().getName());
				fti.setRoleSignRole(nextStepRole);
				fti.setRoleSignDept("deptName");
				List signers = subWfInfo.getSignerList();
				signerList.add(fti);
				subWfInfo.setSignerList(signers);
			} 
//			send mail
			boolean isSendMail = ("true".equals((String) args.get("send.mail"))) ? true
					: false;
			if(isSendMail){
				WorkFlowFormContext.workFlowSendMessage(subWfInfo);
			}
			util.setSigner(subWfInfo);
			//util.postFlowTaskInfo(superWfInfo, com.qware.security.SecurityContextInfo.getCurrentUser());
			util.getTransactionConn().commit();
		} catch (Exception e) {		
			try {
				util.getTransactionConn().rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				throw new WorkflowException();
			}			
			e.printStackTrace();
			throw new WorkflowException("start sub workflow exception");
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
