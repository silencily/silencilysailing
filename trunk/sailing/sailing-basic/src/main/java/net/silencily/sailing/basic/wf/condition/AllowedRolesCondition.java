package net.silencily.sailing.basic.wf.condition;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.WorkflowException;

import java.util.*;

import net.silencily.sailing.basic.wf.entry.WorkflowStepPrincipalForRole;
import net.silencily.sailing.framework.core.User;
import net.silencily.sailing.utils.ArrayUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.util.Assert;

// Referenced classes of package com.coheg.workflow.impl.condition:
//            StepPermissionCondition

public class AllowedRolesCondition
    implements StepPermissionCondition
{

    public AllowedRolesCondition()
    {
        roles = null;
        roleCodes = null;
    }

    public boolean passesCondition(Map transientVars, Map args, PropertySet ps)
        throws WorkflowException
    {
        return findOverlap(transientVars, args, ps).length > 0;
    }

    /**
     * @deprecated Method findOverlap is deprecated
     */

    protected String[] findOverlap(Map transientVars, Map args, PropertySet ps)
    {
        return findOverlap(transientVars, args);
    }

    protected String[] findOverlap(Map transientVars, Map args)
    {
        if(roleCodes == null)
            init(args);
        if(roleCodes.length == 0)
            return ALLOWED_ANY_ROLES;
        else
            return (String[])ArrayUtils.intersect(roleCodes, getCurrentRolesCode(transientVars, args));
    }

    public static String[] splitIgnoreBlank(String string, String delim)
    {
        Assert.notNull(string);
        Assert.notNull(delim);
        String array[] = string.split("((\\s*" + delim + "\\s*)|\\s+)");
        List list = new ArrayList(array.length);
        for(int i = 0; i < array.length; i++)
            if(StringUtils.isNotBlank(array[i]))
                list.add(array[i]);

        return (String[])list.toArray(new String[0]);
    }

    private String[] getCurrentRolesCode(Map transientVars, Map arguments)
    {
        User user = (User)transientVars.get("current.user");
        List roles = user.getRoles();
        if(roles.size() == 0)
            return new String[0];
        else
            return (String[])roles.toArray(new String[roles.size()]);
    }

    public List getWorkflowStepPrincipal(Map transientVars, Map arguments)
    {
        if(roles == null)
            init(arguments);
        return roles;
    }

    private synchronized void init(Map arguments)
    {
        if(roleCodes == null || roles == null)
        {
            roleCodes = splitIgnoreBlank((String)arguments.get("role.code"), ",");
            roles = new ArrayList(roleCodes.length);
            for(int i = 0; i < roleCodes.length; i++)
            {
                WorkflowStepPrincipalForRole role = new WorkflowStepPrincipalForRole();
                role.setCode(roleCodes[i]);
                try
                {
                    //String rn = SecurityServiceProvider.getRoleManager().loadByRoleName(roleCodes[i]).getDescription();
                    String rn = "nothing";
                	role.setName(rn);
                    roles.add(role);
                    continue;
                }
                catch(ObjectRetrievalFailureException e)
                {
                    if(logger.isInfoEnabled())
                        logger.info("\u68C0\u7D22\u89D2\u8272[" + roleCodes[i] + "]\u7684\u540D\u79F0\u53D1\u751F\u9519\u8BEF, \u5C06\u5728\u8BC4\u4F30\u6B65\u9AA4\u6267\u884C\u6761\u4EF6\u65F6\u5FFD\u7565\u8FD9\u4E2A\u89D2\u8272", e);
                }
            }

        }
    }

//    static Class _mthclass$(String x0)
//    {
//        return Class.forName(x0);
//        ClassNotFoundException x1;
//        x1;
//        throw new NoClassDefFoundError(x1.getMessage());
//    }

    private static Log logger;
    public static final String ROLES_KEY = "role.code";
    protected List roles;
    protected String roleCodes[];
    protected static final String ALLOWED_ANY_ROLES[] = {
        "allowed.any.roles"
    };

    static 
    {
        logger = LogFactory.getLog(AllowedRolesCondition.class);
    }
}
