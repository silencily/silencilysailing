package net.silencily.sailing.basic.wf.condition;

import com.opensymphony.workflow.Condition;
import java.util.List;
import java.util.Map;

public interface StepPermissionCondition
    extends Condition
{

    public abstract List getWorkflowStepPrincipal(Map map, Map map1);
}
