package net.silencily.sailing.basic.wf.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.silencily.sailing.basic.wf.WorkflowInfo;
import net.silencily.sailing.basic.wf.WorkflowSearchService;
import net.silencily.sailing.basic.wf.WorkflowService;
import net.silencily.sailing.basic.wf.condition.StepPermissionCondition;
import net.silencily.sailing.basic.wf.entry.WorkflowAction;
import net.silencily.sailing.basic.wf.entry.WorkflowStep;
import net.silencily.sailing.exception.UnexpectedException;
import net.silencily.sailing.security.SecurityContextInfo;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import com.opensymphony.workflow.AbstractWorkflow;
import com.opensymphony.workflow.Condition;
import com.opensymphony.workflow.FactoryException;
import com.opensymphony.workflow.WorkflowContext;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.loader.AbstractDescriptor;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.workflow.loader.ConditionDescriptor;
import com.opensymphony.workflow.loader.ConditionsDescriptor;
import com.opensymphony.workflow.loader.PermissionDescriptor;
import com.opensymphony.workflow.loader.StepDescriptor;
import com.opensymphony.workflow.loader.WorkflowDescriptor;

public abstract class OsWorkflowSearchService
    implements WorkflowSearchService
{
    static class CustomAbstractWorkflow extends AbstractWorkflow
        implements WorkflowContext
    {

        public String getCaller()
        {
            return SecurityContextInfo.getCurrentUser().getEmpCd();
        }

        public void setRollbackOnly()
        {
            TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
        }

        public boolean evaluate(ConditionsDescriptor csd, Map transientVars, Collection principals)
            throws WorkflowException
        {
            if(csd == null || csd.getConditions().size() == 0)
                return true;
            List conds = csd.getConditions();
            boolean ret = "AND".equals(csd.getType());
            for(Iterator it = conds.iterator(); it.hasNext();)
            {
                AbstractDescriptor ad = (AbstractDescriptor)it.next();
                boolean passing = true;
                if(ad instanceof ConditionDescriptor)
                {
                    long wid = ((Long)transientVars.get("workflow.id")).longValue();
                    ConditionDescriptor cond = (ConditionDescriptor)ad;
                    String type = cond.getType();
                    Map args = new HashMap(cond.getArgs());
                    java.util.Map.Entry mapEntry;
                    for(Iterator iterator = args.entrySet().iterator(); iterator.hasNext(); mapEntry.setValue(getConfiguration().getVariableResolver().translateVariables((String)mapEntry.getValue(), transientVars, getPropertySet(wid))))
                        mapEntry = (java.util.Map.Entry)iterator.next();

                    Condition condition = null;
                    if(!conditionCache.containsKey(cond))
                        synchronized(conditionCache)
                        {
                            condition = getResolver().getCondition(type, args);
                            if(condition == null)
                            {
                                context.setRollbackOnly();
                                throw new WorkflowException("Could not load condition");
                            }
                            conditionCache.put(cond, condition);
                        }
                    condition = (Condition)conditionCache.get(cond);
                    try
                    {
                        passing = condition.passesCondition(transientVars, args, getPropertySet(wid));
                        if(cond.isNegate())
                            passing = !passing;
                        if(condition instanceof StepPermissionCondition)
                        {
                            List pri = ((StepPermissionCondition)condition).getWorkflowStepPrincipal(transientVars, args);
                            principals.addAll(pri);
                        }
                    }
                    catch(Exception e)
                    {
                        context.setRollbackOnly();
                        if(e instanceof WorkflowException)
                            throw (WorkflowException)e;
                        else
                            throw new WorkflowException("Unknown exception encountered when checking condition " + condition, e);
                    }
                } else
                if(ad instanceof ConditionsDescriptor)
                    passing = evaluate((ConditionsDescriptor)ad, transientVars, principals);
                if("AND".equals(csd.getType()))
                    ret &= passing;
                else
                    ret |= passing;
            }

            return ret;
        }

        protected Map conditionCache;

        public CustomAbstractWorkflow()
        {
            conditionCache = Collections.synchronizedMap(new HashMap(30));
            super.context = this;
        }
    }


    public OsWorkflowSearchService()
    {
        abstractWorkflow = new CustomAbstractWorkflow();
    }

    public void setWorkflowService(WorkflowService workflowService)
    {
        this.workflowService = workflowService;
    }

    public WorkflowStep loadConciseCurrentStep(WorkflowInfo info)
    {
        WorkflowEntryWithOid ww = loadWorkflowEntryWithOidFromDbms(info);
        if(ww != null && ww.getCurrentStep() != null && ww.getCurrentStep().getId() != 0L)
        {
            if(abstractWorkflow.getWorkflowDescriptor(ww.getWorkflowName()) == null)
                throw new IllegalStateException("\u6CA1\u6709\u627E\u5230\u5DE5\u4F5C\u6D41\u914D\u7F6E\u6587\u4EF6,\u68C0\u67E5\u5DE5\u4F5C\u6D41\u914D\u7F6E[" + ww.getWorkflowName() + "]");
            StepDescriptor step = abstractWorkflow.getWorkflowDescriptor(ww.getWorkflowName()).getStep(ww.getCurrentStep().getStepId());
            if(step == null)
                return null;
            SimpleStepWithSavedValues ss = ww.getCurrentStep();
            Map map = populateMap(info, ww);
            WorkflowStep s = new WorkflowStep();
            
            //历史步骤信息
            s.setEntryStepId(ww.getCurrentStep().getId());
            
            s.setId(String.valueOf(step.getId()));
            s.setName(step.getName());
            s.setData(map);
            s.setStartTime(ss.getStartDate());
            s.setMetadata(step.getMetaAttributes());
            s.setPrincipal(Collections.EMPTY_LIST);
            s.setHasPermission(Boolean.FALSE);
            WorkflowDescriptor wd = abstractWorkflow.getWorkflowDescriptor(info.getWorkflowName());
            if(wd != null && wd.getMetaAttributes() != null)
            {
                String wn = (String)wd.getMetaAttributes().get("workflow.name");
                if(StringUtils.isBlank(wn))
                    wn = info.getWorkflowName();
                s.setDescription(wn);
            }
            List permissions = step.getPermissions();
            if(permissions != null && permissions.size() > 0)
            {
                PermissionDescriptor pd = (PermissionDescriptor)permissions.get(0);
                if(pd != null && pd.getRestriction() != null && pd.getRestriction().getConditionsDescriptor() != null)
                {
                    Set principals = new LinkedHashSet(10);
                    boolean hasPermission = false;
                    try
                    {
                        hasPermission = abstractWorkflow.evaluate(pd.getRestriction().getConditionsDescriptor(), map, principals);
                    }
                    catch(WorkflowException e)
                    {
                        abstractWorkflow.setRollbackOnly();
                        throw new UnexpectedException("\u8BC4\u4F30\u6B65\u9AA4[" + step.getName() + "]permission\u9519\u8BEF", e);
                    }
                    s.setHasPermission(Boolean.valueOf(hasPermission));
                    List l = new ArrayList(principals.size());
                    l.addAll(principals);
                    s.setPrincipal(l);
                }
            }
            return s;
        } else
        {
            return null;
        }
    }

    public List listStepDefinitions(String workflowName)
    {
        WorkflowDescriptor wd = null;
        try
        {
            wd = abstractWorkflow.getConfiguration().getWorkflow(workflowName);
        }
        catch(FactoryException e)
        {
            throw new IllegalArgumentException("\u6CA1\u6709\u540D\u79F0\u662F[" + workflowName + "]\u7684\u914D\u7F6E");
        }
        List steps = wd.getSteps();
        List result = new ArrayList(steps.size());
        WorkflowStep step;
        for(Iterator it = steps.iterator(); it.hasNext(); result.add(step))
        {
            StepDescriptor sd = (StepDescriptor)it.next();
            step = new WorkflowStep();
            step.setId(String.valueOf(sd.getId()));
            step.setName(sd.getName());
            step.setMetadata(sd.getMetaAttributes());
        }

        return result;
    }

    public void fillWorkflowInfo(WorkflowInfo info)
    {
        info.setInitialActions(workflowService.fetchInitialActions(info));
        WorkflowEntryWithOid ww = loadWorkflowEntryWithOidFromDbms(info);
        WorkflowDescriptor wd = abstractWorkflow.getWorkflowDescriptor(info.getWorkflowName());
        if(wd == null)
            throw new IllegalStateException("\u6CA1\u6709\u52A0\u8F7D\u5230\u540D\u79F0\u662F[" + info.getWorkflowName() + "]\u7684\u5DE5\u4F5C\u6D41\u5B9A\u4E49");
        StepDescriptor sd = null;
        if(ww != null && ww.getCurrentStep() != null && ww.getCurrentStep().getId() != 0L && (sd = wd.getStep(ww.getCurrentStep().getStepId())) != null)
        {
            Map map = populateMap(info, ww);
            Map data = ww.getCurrentStep().getOriginalKeyAndValue();
            map.put("data", data);
            info.setData(data);
            WorkflowStep step = new WorkflowStep();
            
            //历史步骤信息
            step.setEntryStepId(ww.getCurrentStep().getId());
            
            step.setStartTime(ww.getCurrentStep().getStartDate());
            step.setId(String.valueOf(ww.getCurrentStep().getStepId()));
            step.setName(sd.getName());
            step.setMetadata(((Map) (sd.getMetaAttributes() != null ? sd.getMetaAttributes() : ((Map) (new HashMap(10))))));
            step.getMetadata().put("step.entry.id", String.valueOf(ww.getCurrentStep().getId()));
            step.setHasPermission(Boolean.FALSE);
            step.setPrincipal(Collections.EMPTY_LIST);
            String wn = info.getWorkflowName();
            if(wd.getMetaAttributes() != null && wd.getMetaAttributes().containsKey("workflow.name"))
                wn = (String)wd.getMetaAttributes().get("workflow.name");
            step.setDescription(wn);
            Iterator it = ((Iterator) (sd.getPermissions() != null ? sd.getPermissions().iterator() : ((Iterator) (IteratorUtils.EMPTY_ITERATOR))));
            if(it.hasNext())
            {
                PermissionDescriptor pd = (PermissionDescriptor)it.next();
                if(pd != null && pd.getRestriction() != null && pd.getRestriction().getConditionsDescriptor() != null)
                {
                    List principals = new ArrayList(10);
                    boolean hasPermission = false;
                    try
                    {
                        hasPermission = abstractWorkflow.evaluate(pd.getRestriction().getConditionsDescriptor(), map, principals);
                    }
                    catch(WorkflowException e)
                    {
                        abstractWorkflow.setRollbackOnly();
                        throw new UnexpectedException("\u8BC4\u4F30\u6B65\u9AA4[" + step.getName() + "]permission\u9519\u8BEF", e);
                    }
                    step.setHasPermission(Boolean.valueOf(hasPermission));
                    step.setPrincipal(principals);
                }
            } else
            {
                step.setHasPermission(Boolean.TRUE);
            }
            if(step.isAllowed())
            {
                int actionIds[] = abstractWorkflow.getAvailableActions(ww.getId(), map);
                List actions = new ArrayList(actionIds.length);
                for(int i = 0; i < actionIds.length; i++)
                {
                    ActionDescriptor ad = wd.getAction(actionIds[i]);
                    WorkflowAction wa = new WorkflowAction();
                    wa.setId(String.valueOf(actionIds[i]));
                    wa.setName(ad.getName());
                    wa.setMetadata(ad.getMetaAttributes() != null ? ad.getMetaAttributes() : Collections.EMPTY_MAP);
                    int nextStepId = ad.getUnconditionalResult().getStep();
                    if(nextStepId == -1)
                    {
                        WorkflowStep nextStep = new WorkflowStep();
                        nextStep.setId(step.getId());
                        nextStep.setName(step.getName());
                        wa.setNextStep(nextStep);
                    } else
                    if(nextStepId != 0)
                    {
                        StepDescriptor st = wd.getStep(nextStepId);
                        if(st == null)
                            throw new NullPointerException("\u5DE5\u4F5C\u6D41\u914D\u7F6E\u9519\u8BEF:\u5DE5\u4F5C\u6D41" + info.getWorkflowName() + "\u6CA1\u6709\u5B9A\u4E49id=" + nextStepId + "\u7684\u6B65\u9AA4");
                        WorkflowStep nextStep = new WorkflowStep();
                        nextStep.setId(String.valueOf(nextStepId));
                        nextStep.setName(st.getName());
                        wa.setNextStep(nextStep);
                    }
                    actions.add(wa);
                }

                step.setAvailableActions(actions);
            }
            info.setCurrentStep(step);
        } else
        {
            info.setCurrentStep(null);
        }
        List historySteps = ww == null ? Collections.EMPTY_LIST : ww.getHistorySteps();
        List workflowSteps = new ArrayList(historySteps.size());
        for(int i = 0; i < historySteps.size(); i++)
        {
            SimpleStepWithSavedValues hs = (SimpleStepWithSavedValues)historySteps.get(i);
            StepDescriptor hsd = wd.getStep(hs.getStepId());
            if(hsd == null)
                continue;
            WorkflowStep s = new WorkflowStep();
            
            //历史步骤信息
            s.setEntryStepId(hs.getId());
            
            s.setId(String.valueOf(hs.getStepId()));
            s.setName(hsd.getName());
            s.setStartTime(hs.getStartDate());
            s.setFinishTime(hs.getFinishDate());
            s.setUsername(hs.getCaller());
            s.setData(hs.getOriginalKeyAndValue());
            s.setOpinion((String)hs.getOriginalKeyAndValue().get("opinion"));
            Map meta = hsd.getMetaAttributes();
            s.setMetadata(((Map) (meta != null ? meta : ((Map) (new HashMap())))));
            s.getMetadata().put("step.entry.id", String.valueOf(hs.getId()));
            s.setActionId(String.valueOf(hs.getActionId()));
            ActionDescriptor ad = hsd.getAction(hs.getActionId());
            if(ad != null)
            {
                WorkflowAction wa = new WorkflowAction();
                wa.setId(s.getActionId());
                wa.setName(ad.getName());
                s.setAction(wa);
            }
            workflowSteps.add(s);
        }

        info.setHistorySteps(workflowSteps);
    }

    private Map populateMap(WorkflowInfo info, WorkflowEntryWithOid ww)
    {
        Map map = new HashMap(10);
        map.put("dto", info);
        map.put("dto.id", info.getId());
        map.put("current.user", SecurityContextInfo.getCurrentUser());
        map.put("data", ww.getCurrentStep().getOriginalKeyAndValue());
        map.put("workflow.id", new Long(ww.getId()));
        return map;
    }

    protected abstract WorkflowEntryWithOid loadWorkflowEntryWithOidFromDbms(WorkflowInfo workflowinfo);

    protected CustomAbstractWorkflow abstractWorkflow;
    private WorkflowService workflowService;
}
