package net.silencily.sailing.basic.wf.condition;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.Condition;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.spi.Step;
import java.util.*;
import org.apache.commons.lang.ArrayUtils;

public class GlobalActionUnAvailableStepsCondition
    implements Condition
{

    public GlobalActionUnAvailableStepsCondition()
    {
    }

    public boolean passesCondition(Map transientVars, Map args, PropertySet ps)
        throws WorkflowException
    {
        String unAvailableSteps = (String)args.get("unAvailableSteps");
        if(unAvailableSteps == null)
            unAvailableSteps = "";
        String unAvaliableStepArray[] = AllowedRolesCondition.splitIgnoreBlank(unAvailableSteps, ",");
        for(Iterator currentSteps = ((Collection)transientVars.get("currentSteps")).iterator(); currentSteps.hasNext();)
        {
            Step step = (Step)currentSteps.next();
            if(ArrayUtils.indexOf(unAvaliableStepArray, String.valueOf(step.getStepId())) > -1)
                return false;
        }

        return true;
    }

    public static final String UN_AVAILABLE_STEPS_KEY = "unAvailableSteps";
}

