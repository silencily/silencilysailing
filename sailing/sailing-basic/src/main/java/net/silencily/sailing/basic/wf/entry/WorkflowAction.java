package net.silencily.sailing.basic.wf.entry;

import java.util.Collections;
import java.util.Map;

public class WorkflowAction implements java.io.Serializable
{
    private String id;
    private String name;
    private WorkflowStep nextStep;
    private Map metadata;

    public WorkflowAction()
    {
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public WorkflowStep getNextStep()
    {
        return nextStep;
    }

    public void setNextStep(WorkflowStep nextStep)
    {
        this.nextStep = nextStep;
    }

    public Map getMetadata()
    {
        if(metadata == null)
            return Collections.EMPTY_MAP;
        else
            return metadata;
    }

    public void setMetadata(Map metadata)
    {
        this.metadata = metadata;
    }	
}
