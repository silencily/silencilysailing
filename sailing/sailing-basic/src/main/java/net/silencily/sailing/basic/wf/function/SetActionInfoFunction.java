package net.silencily.sailing.basic.wf.function;

import java.util.Map;

import net.silencily.sailing.common.dbtime.DBTime;
import net.silencily.sailing.framework.core.ContextInfo;
import net.silencily.sailing.framework.persistent.BaseDto;

import org.apache.commons.beanutils.PropertyUtils;

import com.opensymphony.module.propertyset.PropertySet;

/**
 * @deprecated Class SetActionInfoFunction is deprecated
 */

public class SetActionInfoFunction extends FunctionTemlate
{

    public SetActionInfoFunction()
    {
    }

    protected void doExecute(Map transientVars, Map args, PropertySet ps)
        throws Throwable
    {
        BaseDto dto = (BaseDto)transientVars.get("dto");
        String operatorIdFieldName = (String)args.get("operatorIdField");
        String departmentIdFieldName = (String)args.get("departmentIdField");
        String actionTimeFieldName = (String)args.get("actionTimeField");
        boolean cleanOperatorId = args.get("cleanOperatorId") != null;
        boolean cleanDepartmentId = args.get("cleanDepartmentId") != null;
        boolean cleanActionTime = args.get("cleanActionTime") != null;
        if(operatorIdFieldName != null)
            if(cleanOperatorId)
                PropertyUtils.setProperty(dto, operatorIdFieldName, null);
            else
                PropertyUtils.setProperty(dto, operatorIdFieldName, ContextInfo.getCurrentUser().getUsername());
        if(departmentIdFieldName != null)
            if(cleanDepartmentId)
                PropertyUtils.setProperty(dto, departmentIdFieldName, null);
            else
                PropertyUtils.setProperty(dto, departmentIdFieldName, ContextInfo.getCurrentUser().getOrganizationId());
        if(actionTimeFieldName != null)
            if(cleanActionTime)
                PropertyUtils.setProperty(dto, actionTimeFieldName, null);
            else
                PropertyUtils.setProperty(dto, actionTimeFieldName, DBTime.getDBTime());
    }

    public static final String ACTION_OPERATOR_ID_FIELD_KEY = "operatorIdField";
    public static final String CLEAN_OPERATOR_ID_FIELD_KEY = "cleanOperatorId";
    public static final String ACTION_DEPARTMENT_ID_FIELD_KEY = "departmentIdField";
    public static final String CLEAN_DEPARTMENT_ID_FIELD_KEY = "cleanDepartmentId";
    public static final String ACTION_TIME_FIELD_KEY = "actionTimeField";
    public static final String CLEAN_TIME_FIELD_KEY = "cleanActionTime";
}
