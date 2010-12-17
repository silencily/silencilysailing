package net.silencily.sailing.basic.wf.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.silencily.sailing.framework.utils.DaoHelper;

public class IbatisOsWorkflowService extends OsWorkflowService
{

    public IbatisOsWorkflowService()
    {
        listAllActivatedWorkflowEntry = "listAllActivatedWorkflowEntry";
        clear = "clear";
        listOidFromWorkflowEntryId = "listOidFromWorkflowEntryId";
        getDataFromPropertySet = "getDataFromPropertySet";
        listSpecifiedStepWorkflowEntry = "listSpecifiedStepWorkflowEntry";
        listHistoryStepByOid = "listHistoryStepByOid";
    }

    protected String listOidFromWorkflowEntryId(long id)
    {
        return (String)DaoHelper.getSqlMapClientTemplate().queryForObject(listOidFromWorkflowEntryId, "osff_" + String.valueOf(id));
    }

    protected WorkflowEntryWithOid[] listAllActivatedWorkflowEntry(String workflowName)
    {
        List result = DaoHelper.getSqlMapClientTemplate().queryForList(listAllActivatedWorkflowEntry, workflowName);
        return (WorkflowEntryWithOid[])result.toArray(new WorkflowEntryWithOid[result.size()]);
    }

    public void clear()
    {
        throw new UnsupportedOperationException("unimplemented method");
    }

    protected void removeWorkflowEntry(long wfId)
    {
        DaoHelper.getSqlMapClientTemplate().delete(clear, String.valueOf(wfId));
    }

    protected Map getDataFromPropertySet(String oid)
    {
        return DaoHelper.getSqlMapClientTemplate().queryForMap(getDataFromPropertySet, "oid_" + oid, "KEY", "VALUE");
    }

    protected WorkflowEntryWithOid[] listSpecifiedStepWorkflowEntry(String workflowName, String stepNo)
    {
        Map param = new HashMap(2);
        param.put("name", workflowName);
        param.put("stepNo", stepNo);
        List result = DaoHelper.getSqlMapClientTemplate().queryForList(listSpecifiedStepWorkflowEntry, param);
        return (WorkflowEntryWithOid[])result.toArray(new WorkflowEntryWithOid[result.size()]);
    }

    protected WorkflowEntryWithOid listSpecifiedOidWorkflowEntry(String oid)
    {
        return (WorkflowEntryWithOid)DaoHelper.getSqlMapClientTemplate().queryForObject(LIST_SPECIFIED_OID_WORKFLOW_ENTRY, oid);
    }

    protected List listHistoryStepByOid(String oid)
    {
        return DaoHelper.getSqlMapClientTemplate().queryForList(listHistoryStepByOid, oid);
    }

    private String listAllActivatedWorkflowEntry;
    private String clear;
    private String listOidFromWorkflowEntryId;
    private String getDataFromPropertySet;
    private String listSpecifiedStepWorkflowEntry;
    private String listHistoryStepByOid;
    public static String LIST_SPECIFIED_OID_WORKFLOW_ENTRY = "listSpecifiedOidWorkflowEntry";

}
