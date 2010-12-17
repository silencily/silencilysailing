package net.silencily.sailing.basic.wf.impl;

import java.util.Map;

import net.silencily.sailing.basic.wf.entry.WorkflowAction;
import net.silencily.sailing.basic.wf.entry.WorkflowStep;

import org.springframework.context.ApplicationEvent;

public class WorkflowTransitionEvent extends ApplicationEvent
{

    public WorkflowTransitionEvent(Map map)
    {
        super(map);
        fromStep = (WorkflowStep)map.get("from.step");
        toStep = (WorkflowStep)map.get("to.step");
        action = (WorkflowAction)map.get("action");
        transientVars = (Map)map.get("data");
    }

    public WorkflowAction getAction()
    {
        return action;
    }

    public WorkflowStep getFromStep()
    {
        return fromStep;
    }

    public WorkflowStep getToStep()
    {
        return toStep;
    }

    public Map getTransientVars()
    {
        return transientVars;
    }

    public static final String KEY_FROM_STEP = "from.step";
    public static final String KEY_TO_STEP = "to.step";
    public static final String KEY_ACTION = "action";
    public static final String KEY_DATA = "data";
    private WorkflowStep fromStep;
    private WorkflowStep toStep;
    private WorkflowAction action;
    private Map transientVars;
}
