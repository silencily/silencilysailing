package net.silencily.sailing.basic.wf.condition;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.Condition;
import com.opensymphony.workflow.WorkflowException;
import java.util.Map;

public class ReturnTrueCondition
    implements Condition
{

    public ReturnTrueCondition()
    {
    }

    public boolean passesCondition(Map transientVars, Map args, PropertySet ps)
        throws WorkflowException
    {
        return true;
    }
}

