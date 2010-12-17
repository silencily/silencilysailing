package net.silencily.sailing.container;

import net.silencily.sailing.framework.persistent.filter.ConditionService;

/**
 * ����ϵͳ�г��õ�<code>service</code>��<code>utility</code>��
 * @author scott
 * @since 2006-4-27
 * @version $Id: CommonServiceProvider.java,v 1.1 2010/12/10 10:54:26 silencily Exp $
 *
 */
public abstract class CommonServiceProvider {
    public static ConditionService getConditionService() {
        return (ConditionService) ServiceProvider.getService(ConditionService.SERVICE_NAME);
    }
}
