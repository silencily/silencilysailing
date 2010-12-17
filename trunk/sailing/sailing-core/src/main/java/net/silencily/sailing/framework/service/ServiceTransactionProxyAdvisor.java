package net.silencily.sailing.framework.service;

import java.lang.reflect.Method;
import java.util.Properties;

import net.silencily.sailing.exception.UnexpectedException;
import net.silencily.sailing.framework.core.GlobalParameters;
import net.silencily.sailing.framework.core.ServiceBase;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.interceptor.TransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;

/**
 * <p>����������<code>service</code>������������������, ȱʡ���������ʵ����<code>ServiceBase</code>
 * ��ҵ��������������Ե�, ����������Ϊ��<code>EJB's REQUIRED</code>, ʵ�ʵ�������Ϊ��
 * <code>SerivceTransactionAttributeSource</code>����</p>
 * <p>���<code>Pointcut</code>������ѡ��<code>ServiceBase</code>���, ���ų�������ӿ�
 * {@link net.silencily.sailing.framework.persistent.coheg.persistent.ServiceDaoSupport <code>ServiceDaoSupport</code>}
 * ��ص��κη���, ����<code>getter/setter</code>
 * </p>
 * <p>�������ʹ��ǰҪ�������¼�������<ul>
 * <li><code>TransactionAttributeSource</code>: ��δ�����������, ��������ǿ�ѡ��, ȱʡ
 * ���������ʹ��{@link com.coheg.framework.service.ServiceTransactionAttributeSource}. ���
 * ��������ѡ�����ͨ��<code>setTransactionAttributes</code>����ע�����</li>
 * <li><code>PlatformTransactionManager</code>: ���������, ��������Ǳ����, ͨ������
 * <code>platformTransactionManager</code>������</li></ul></p>
 * 
 * @author scott
 * @since 2006-3-10
 * @version $Id: ServiceTransactionProxyAdvisor.java,v 1.1 2010/12/10 10:54:24 silencily Exp $
 * @see com.coheg.framework.service.ServiceTransactionAttributeSource
 * @see net.silencily.sailing.framework.core.coheg.core.ServiceBase
 *
 */
public class ServiceTransactionProxyAdvisor extends StaticMethodMatcherPointcutAdvisor implements InitializingBean {
    private static Log logger = LogFactory.getLog(ServiceTransactionProxyAdvisor.class);
    
    public static final String DEFAULT_TRANSACTION_MANAGER_NAME = GlobalParameters.MODULE_NAME + "platformTransactionManager";
    
    /**
     * ���ڴ��������������õ����, ȱʡ����ÿ�����񷽷�����<code>EJB's REQUIRED</code>������
     * ��ԭ����, ͬʱ֧����<code>spring's bean factory</code>�����ļ�������<code>description</code>
     * Ԫ����������������������
     */    
    private TransactionAttributeSource transactionAttributeSource =  new ServiceTransactionAttributeSource();

    private PlatformTransactionManager platformTransactionManager;
    
    public void setTransactionAttributeSource(TransactionAttributeSource transactionAttributeSource) {
        this.transactionAttributeSource = transactionAttributeSource;
    }
    
    /**
     * ʵ��ͬ {@link TransactionAspectSupport#setTransactionAttributes(Properties)}
     * @param transactionAttributes
     */
	public void setTransactionAttributes(Properties transactionAttributes) {
		NameMatchTransactionAttributeSource tas = new NameMatchTransactionAttributeSource();
		tas.setProperties(transactionAttributes);
		this.transactionAttributeSource = tas;
	}

    public void setPlatformTransactionManager(PlatformTransactionManager platformTransactionManager) {
        this.platformTransactionManager = platformTransactionManager;
    }
    
    /**
     * ��������չ��{@link ServiceBase}�ķ������ṩ�������
     */
    public boolean matches(Method method, Class targetClass) {
        Class clazz = targetClass;
        if (clazz == null) {
            clazz = method.getDeclaringClass();
        }
        return ServiceBase.class.isAssignableFrom(clazz);
    }

    public void afterPropertiesSet() throws Exception {
        TransactionInterceptor interceptor = new TransactionInterceptor();
        interceptor.setTransactionAttributeSource(this.transactionAttributeSource);
        interceptor.setTransactionManager(platformTransactionManager);
        setAdvice(interceptor);
        try {
            interceptor.afterPropertiesSet();
        } catch (Exception e) {
            String msg = "��������Advisor����";

            if (StringUtils.isNotBlank(e.getMessage())) {
                msg += ":" + e.getMessage();
            }
            UnexpectedException ex = new UnexpectedException(msg, e);
            logger.error(msg, e);
            throw ex;
        }
    }
}
