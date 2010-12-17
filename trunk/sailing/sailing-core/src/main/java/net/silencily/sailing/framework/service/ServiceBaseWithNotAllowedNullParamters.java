package net.silencily.sailing.framework.service;

import net.silencily.sailing.framework.core.ServiceBase;

/**
 * 基础的服务接口, 只要方法接受参数就不允许是<code>null</code>, 对于<code>String</code>
 * 类型的参数不允许是<code>empty string</code>, 如果参数是空值抛出<code>NullPointerException</code>,
 * 省去了在每个方法开始出编写验证参数的代码
 * @author zhangli
 * @version $Id: ServiceBaseWithNotAllowedNullParamters.java,v 1.1 2010/12/10 10:54:24 silencily Exp $
 * @since 2007-4-22
 */
public interface ServiceBaseWithNotAllowedNullParamters extends ServiceBase {

}
