/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project material
 */
package net.silencily.sailing.security.rule;

/**
 * @since 2006-8-6
 * @author java2enterprise
 * @version $Id: AccessRuleUtils.java,v 1.1 2010/12/10 10:54:27 silencily Exp $
 */
public abstract class AccessRuleUtils {
	
    public static boolean isPermitted(AccessRule accessRule, int permissionToCheck) {
        return ((accessRule.getObjectMask() & permissionToCheck) == permissionToCheck);
    }
	
    public static boolean isObjectReadable(AccessRule accessRule) {
    	return isPermitted(accessRule, AccessRule.READ) || isPermitted(accessRule, AccessRule.ADMINISTRATION);
    }
    
    public static boolean isObjectWritable(AccessRule accessRule) {
    	return isPermitted(accessRule, AccessRule.WRITE) || isPermitted(accessRule, AccessRule.ADMINISTRATION);
    }
    
    public static boolean isObjectCreatble(AccessRule accessRule) {
    	return isPermitted(accessRule, AccessRule.CREATE) || isPermitted(accessRule, AccessRule.ADMINISTRATION);
    }
    
    public static boolean isObjectDeletable(AccessRule accessRule) {
    	return isPermitted(accessRule, AccessRule.DELETE) || isPermitted(accessRule, AccessRule.ADMINISTRATION);
    }
    
}
