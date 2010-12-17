/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project publicresource
 */
package net.silencily.sailing.common.numbering.service;

import net.silencily.sailing.common.CommonConstants;
import net.silencily.sailing.framework.core.ServiceBase;


/**
 * 生成编号的服务
 * @since 2007-1-12
 * @author scott
 * @version $Id: NumberingService.java,v 1.1 2010/12/10 10:54:20 silencily Exp $
 * @see NumberingConfigure
 */
public interface NumberingService extends ServiceBase {
    
    String SERVICE_NAME = CommonConstants.MODULE_NAME + ".numberingService";
    
    /**
     * 根据参数指定的服务名生成编号
     * @param businessName  已经配置的编号生成规则的业务名称
     * @return 生成的编号, 不返回空值
     * @throws IllegalArgumentException 如果编号格式错误
     * @throws NullPointerException 如果参数是空值或没有找到指定名称的配置
     */
    String number(String businessName);
    
    /**
     * 根据参数指定的服务名生成数字格式的编号
     * @param businessName  已经配置的编号生成规则的业务名称
     * @return 生成的编号, 不返回空值
     * @throws NullPointerException 如果参数是空值或没有找到指定名称的配置
     */
    Double numberInDigit(String businessName);
}
