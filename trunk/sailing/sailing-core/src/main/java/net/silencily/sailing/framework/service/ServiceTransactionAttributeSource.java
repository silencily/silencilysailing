package net.silencily.sailing.framework.service;

import java.lang.reflect.Method;

import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttributeSource;

/**
 * <p>����һЩ����Ҫ������಻������������, Ҫ�ų��������Ե�����<code>List</code>��ʽ�Ĳ�������
 * ��<code>dontProxy</code>����, ����Ҫ�ų�<code>ServiceBase</code>����ķ���,��������
 * ����<pre>
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
