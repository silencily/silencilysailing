package net.silencily.sailing.basic.wf.impl;

import com.opensymphony.workflow.spi.SimpleWorkflowEntry;
import java.util.*;

public class WorkflowEntryWithOid extends SimpleWorkflowEntry
{

    public WorkflowEntryWithOid()
    {
        super(0L, null, 0);
        currentStep = new SimpleStepWithSavedValues();
        historySteps = new ArrayList(30);
    }

    public void setOid(String oid)
    {
        this.oid = oid;
    }

    public String getOid()
    {
        return oid;
    }

    public SimpleStepWithSavedValues getCurrentStep()
    {
        return currentStep;
    }

    public void setCurrentStep(SimpleStepWithSavedValues currentStep)
    {
        this.currentStep = currentStep;
    }

    public List getHistorySteps()
    {
        return historySteps;
    }

    public void setHistorySteps(List historySteps)
    {
        this.historySteps = historySteps;
    }

    private String oid;
    private SimpleStepWithSavedValues currentStep;
    private List historySteps;
    static final Map STATUS_MAPPING = new HashMap() {

    }
;

}
