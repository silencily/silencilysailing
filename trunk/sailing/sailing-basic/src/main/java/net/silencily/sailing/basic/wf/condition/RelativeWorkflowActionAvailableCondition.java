package net.silencily.sailing.basic.wf.condition;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.Condition;
import com.opensymphony.workflow.WorkflowException;
import java.util.Map;

public class RelativeWorkflowActionAvailableCondition
    implements Condition
{

    public RelativeWorkflowActionAvailableCondition()
    {
    }

    public boolean passesCondition(Map transientVars, Map args, PropertySet ps)
        throws WorkflowException
    {
        return transientVars.containsKey(AVAILABLE_TRANSIENT_VAR_KEY);
    }

//    static Class _mthclass$(String x0)
//    {
//        return Class.forName(x0);
//        ClassNotFoundException x1;
//        x1;
//        throw new NoClassDefFoundError(x1.getMessage());
//    }

    public static final Class AVAILABLE_TRANSIENT_VAR_KEY;

    static 
    {
        AVAILABLE_TRANSIENT_VAR_KEY = RelativeWorkflowActionAvailableCondition.class;
    }
}
