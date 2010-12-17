/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project publicresource
 */
package net.silencily.sailing.common.numbering.service.impl;

import net.silencily.sailing.common.numbering.NumberingConfigure;
import net.silencily.sailing.common.numbering.service.NumberingService;

import org.apache.commons.lang.StringUtils;


/**
 * @since 2007-1-12
 * @author scott
 * @version $Id: AbstractNumberingService.java,v 1.1 2010/12/10 10:54:19 silencily Exp $
 */
public abstract class AbstractNumberingService implements NumberingService {

    public synchronized String number(String businessName) {
        NumberingConfigure nc = load(businessName);
        String ret = nc.number();
        update(nc);
        return ret;
    }

    public synchronized Double numberInDigit(String businessName) {
        NumberingConfigure nc = load(businessName);
        Double ret = nc.next();
        update(nc);
        return ret;
    }
    
    private NumberingConfigure load(String businessName) {
        if (StringUtils.isBlank(businessName)) {
            throw new NullPointerException("生成编号时参数是空值");
        }
        NumberingConfigure nc = loadEntity(businessName);
        if (nc == null) {
            throw new NullPointerException("生成编号时没有找到名称是[" + businessName + "]的配置");
        }
        return nc;
    }
    
    protected abstract NumberingConfigure loadEntity(String businessName);
    protected abstract void update(NumberingConfigure nc);
}
