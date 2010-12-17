package net.silencily.sailing.basic.wf.function;

import java.sql.SQLException;
import java.util.Map;

import net.silencily.sailing.basic.wf.WorkflowInfo;
import net.silencily.sailing.basic.wf.db.WfDBConnection;
import net.silencily.sailing.basic.wf.entry.WorkflowRelation;
import net.silencily.sailing.basic.wf.util.BisWfServiceLocator;
import net.silencily.sailing.basic.wf.util.WfUtils;
import net.silencily.sailing.security.SecurityContextInfo;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

public class RelationPostTaskFunction implements FunctionProvider {

		public void execute(Map tvar, Map args, PropertySet ps)
				throws WorkflowException {
			WfUtils util = new WfUtils();		
			WorkflowInfo wfInfo = (WorkflowInfo) tvar.get("dto");
						
			try {
				util.setTransactionConn(WfDBConnection.getConnection());
				util.getTransactionConn().setAutoCommit(false);
				
				if (util.hasSupFlow(wfInfo)) {
					WorkflowRelation wr = new WorkflowRelation();
					wr.setSubOid(wfInfo.getId());
					wr.setSubClass(WfUtils.getUnProxyClassName(wfInfo.getClass()));
					BisWfServiceLocator.getWorkflowService().unlockSuperWorkflow(wr);				
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
				throw new WorkflowException("relation Exception");
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
