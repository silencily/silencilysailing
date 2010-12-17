package net.silencily.sailing.framework.service;

import java.lang.reflect.Method;

import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttributeSource;

/**
 * <p>对于一些不需要事务的类不生成事务属性, 要排除事务属性的类用<code>List</code>形式的参数描述
 * 给<code>dontProxy</code>属性, 比如要排除<code>ServiceBase</code>本身的方法,就像下面
 * 这样<pre>
 *   &lt;bean id="attributeSource" class="com.coheg.persistent.ServiceTransactionAttributeSource"&gt;
 *      &lt;property name="dontProxy"&gt;
 *        &lt;list&gt;
 *          &lt;value&gt;com.coheg.core.ServiceBase&lt;/value&gt;
 *        &lt;/list&gt;
 *      &lt;/property&gt;
 *   &lt;/bean&gt;
 * </pre></p>
 * 
 * @author scott
 * @since 2006-3-9
 * @version $Id: ServiceTransactionAttributeSource.java,v 1.1 2010/12/10 10:54:24 silencily Exp $
 *
 */
public class ServiceTransactionAttributeSource implements TransactionAttributeSource {
    private TransactionAttribute defaultAttribute = new DefaultTransactionAttribute();

    public TransactionAttribute getTransactionAttribute(Method method, Class targetClass) {
        return defaultAttribute;
    }
}
