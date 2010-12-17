package net.silencily.sailing.basic.wf.function;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;
import java.util.Map;

public abstract class FunctionTemlate
    implements FunctionProvider
{

    public FunctionTemlate()
    {
    }

    protected abstract void doExecute(Map map, Map map1, PropertySet propertyset)
        throws Throwable;

    public void execute(Map transientVars, Map args, PropertySet ps)
        throws WorkflowException
    {
        try
        {
            doExecute(transientVars, args, ps);
        }
        catch(Throwable t)
        {
            throw new WorkflowException("\u6267\u884COSWorkflow's function\u65F6\u9519\u8BEF", t);
        }
    }
}

