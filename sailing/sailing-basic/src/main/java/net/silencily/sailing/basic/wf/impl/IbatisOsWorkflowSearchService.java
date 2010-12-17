package net.silencily.sailing.basic.wf.impl;

import net.silencily.sailing.basic.wf.WorkflowInfo;
import net.silencily.sailing.framework.utils.DaoHelper;

public class IbatisOsWorkflowSearchService extends OsWorkflowSearchService
{

    public IbatisOsWorkflowSearchService()
    {
    }

    protected WorkflowEntryWithOid loadWorkflowEntryWithOidFromDbms(WorkflowInfo info)
    {
        return (WorkflowEntryWithOid)DaoHelper.getSqlMapClientTemplate().queryForObject(IbatisOsWorkflowService.LIST_SPECIFIED_OID_WORKFLOW_ENTRY, info.getId());
    }
}
