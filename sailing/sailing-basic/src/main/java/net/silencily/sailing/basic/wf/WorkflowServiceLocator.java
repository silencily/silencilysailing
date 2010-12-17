package net.silencily.sailing.basic.wf;

import net.silencily.sailing.container.ServiceProvider;

public abstract class WorkflowServiceLocator
{

    public WorkflowServiceLocator()
    {
    }

    public static WorkflowService getWorkflowService()
    {
        return (WorkflowService)ServiceProvider.getService("workflow.workflowService");
    }

    public static WorkflowSearchService getWorkflowSearchService()
    {
        return (WorkflowSearchService)ServiceProvider.getService("workflow.workflowSearchService");
    }
}
