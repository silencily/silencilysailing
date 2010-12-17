package net.silencily.sailing.basic.wf;

import java.util.List;

import net.silencily.sailing.basic.wf.entry.WorkflowStep;


public interface WorkflowInfo
    extends WorkflowDataCallback, WorkflowConstants
{
    public abstract String getId();

    public abstract void setId(String s);

    public abstract String getWorkflowName();

    public abstract String getActionId();

    public abstract void setActionId(String s);

    public abstract List getInitialActions();

    public abstract void setInitialActions(List list);

    public abstract String getWorkflowStatus();

    public abstract void setWorkflowStatus(String s);

    public abstract String getWorkflowStatusName();

    public abstract WorkflowStep getCurrentStep();

    public abstract void setCurrentStep(WorkflowStep workflowstep);

    public abstract List getHistorySteps();

    public abstract void setHistorySteps(List list);

    public abstract List getSignerList();

	public abstract void setSignerList(List signerList);
	
	public abstract boolean isRoleSign();
	
	public abstract String getWorkflowEditPath(String oid);
	
	public abstract boolean isWorkflowCanBeInitialized();
	
	public abstract String getInitSplitActionId();
	
	public abstract void setInitSplitActionId(String id);
	
	public abstract String getTaskId();
	
	public abstract void setTaskId(String taskId);
	
	public abstract String getNextStepName();
	
	public abstract void setProcessId(String empCd);
	
	public abstract String getProcessId();
	
	public abstract List getEmpCds();
	
	public abstract void setEmpCds(List empCds);
	
	public abstract void setCurrentStepName(String currentStepName);
	
	public abstract String getCurrentStepName();
	
	public String getTaskName(String title);
	
    public static final String STATUS_SCRATCH = "scratch";
    public static final String STATUS_PROCESSING = "processing";
    public static final String STATUS_SUSPEND = "suspend";
    public static final String STATUS_FINISH = "finish";
    public static final String STATUS_AGREE = "agree";
    public static final String STATUS_REJECT = "reject";
    public static final String STATUS_KILLED = "killed";
    public static final String STATUS_ARCHIVES = "archives";
    public static final String STATUS_UNTREAD = "untread";
    public static final String STATUS_RETAKE = "retake";
    public static final String STATUS_LOCKUP = "lockup";
}
