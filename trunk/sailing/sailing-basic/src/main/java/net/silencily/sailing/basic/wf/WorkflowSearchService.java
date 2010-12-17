package net.silencily.sailing.basic.wf;

import java.util.List;

import net.silencily.sailing.basic.wf.entry.WorkflowStep;
import net.silencily.sailing.framework.core.ServiceBase;

public interface WorkflowSearchService
    extends WorkflowConstants, ServiceBase
{

    public abstract List listStepDefinitions(String s);

    public abstract WorkflowStep loadConciseCurrentStep(WorkflowInfo workflowinfo);

    public abstract void fillWorkflowInfo(WorkflowInfo workflowinfo);

    public static final String SERVICE_NAME = "workflow.workflowSearchService";
}

