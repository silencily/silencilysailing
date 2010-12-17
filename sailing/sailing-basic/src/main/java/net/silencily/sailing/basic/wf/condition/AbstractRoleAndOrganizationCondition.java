package net.silencily.sailing.basic.wf.condition;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.WorkflowException;
import java.util.Map;

import net.silencily.sailing.framework.core.ContextInfo;

public abstract class AbstractRoleAndOrganizationCondition extends AllowedRolesCondition
{

    public AbstractRoleAndOrganizationCondition()
    {
    }

    protected abstract String getApplyOrganizationId(Map map);

    public boolean passesCondition(Map transientVars, Map args, PropertySet ps)
        throws WorkflowException
    {
        if(!super.passesCondition(transientVars, args, ps))
            return false;
        String applyOrganizationId = getApplyOrganizationId(transientVars);
        if(applyOrganizationId == null)
            return false;
        String remoteUserOrganizationId = ContextInfo.getCurrentUser().getOrganizationId();
        if(remoteUserOrganizationId == null)
            return false;
        else
            //return OrganizationUtils.isDestBelong2Src(SecurityServiceProvider.getOrganizationManager(), applyOrganizationId, remoteUserOrganizationId);
        	return true;
    }
}
