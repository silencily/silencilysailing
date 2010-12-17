/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project publicresource
 */
package net.silencily.sailing.common.numbering.service.impl;

import net.silencily.sailing.common.numbering.NumberingConfigure;
import net.silencily.sailing.framework.utils.DaoHelper;


/**
 * 编码服务的<code>HIBERNATE</code>实现, 采用了业务主键:)
 * @since 2007-1-12
 * @author scott
 * @version $Id: HibernateNumberingService.java,v 1.1 2010/12/10 10:54:19 silencily Exp $
 */
public class HibernateNumberingService extends AbstractNumberingService {

    protected NumberingConfigure loadEntity(String businessName) {
        return (NumberingConfigure) DaoHelper.getHibernateTemplate()
            .get(NumberingConfigure.class, businessName);
    }

    protected void update(NumberingConfigure nc) {
        DaoHelper.getHibernateTemplate().update(nc);
        DaoHelper.getHibernateTemplate().flush();
    }
}
