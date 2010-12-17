package net.silencily.sailing.basic.wf;

import java.util.List;
import java.util.Map;

import net.silencily.sailing.basic.wf.entry.WorkflowStep;
import net.silencily.sailing.framework.core.ServiceBase;

public interface WorkflowService
    extends ServiceBase
{
	public abstract String getStepName(String name, int x);   
	
	public abstract String getPkForStep(WorkflowInfo workflowinfo);
	
	public abstract void suspend(WorkflowInfo workflowinfo);
	
	public abstract void activate(WorkflowInfo workflowinfo);
	
	public abstract void terminate(WorkflowInfo workflowinfo, String value, String opinion);
	
	public abstract void terminate(WorkflowInfo workflowinfo);
	
	public abstract void loadWorkflowInfoWithOutAction(WorkflowInfo workflowinfo);
	
	public abstract String getCurrentStepId(WorkflowInfo workflowinfo);
	
    public abstract List getTodoList(String s);

    public abstract List getTodoList(String s, String s1);

    public abstract List fetchInitialActions(WorkflowInfo workflowinfo);

    public abstract void loadWorkflowInfo(WorkflowInfo workflowinfo);

    public abstract void doTransition(WorkflowInfo workflowinfo, Map map);

    public abstract void initializeWorkflow(WorkflowInfo workflowinfo);

    public abstract void clear(WorkflowInfo workflowinfo);

    public abstract void clear();

    public abstract void killWorkflow(WorkflowInfo workflowinfo, String opinion);
    
    public abstract void killWorkflow(WorkflowInfo workflowinfo);

    public abstract WorkflowStep getCurrentStep(WorkflowInfo workflowinfo);

    public abstract void fillHistorySteps(WorkflowInfo workflowinfo);

    public abstract boolean exists(String s);

    public static final String SERVICE_NAME = "workflow.workflowService";
}
