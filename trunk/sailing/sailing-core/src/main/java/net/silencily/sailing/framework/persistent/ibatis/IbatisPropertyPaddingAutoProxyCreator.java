package net.silencily.sailing.framework.persistent.ibatis;

import java.lang.reflect.Method;

import net.silencily.sailing.framework.utils.DaoHelper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.orm.ibatis.SqlMapClientOperations;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

/**
 * ���ڴ������ȱʡ����ֵ��<code>aop</code>������, ��Ծ����<code>or mapping</code>����
 * ��д���Ե�<code>BeforeAdvice</code>, ����<code>optimistic lock</code>��<code>
 * AroundAdvice</code>
 * @author scott
 * @since 2006-4-13
 * @version $Id: IbatisPropertyPaddingAutoProxyCreator.java,v 1.1 2010/12/10 10:54:25 silencily Exp $
 *
 */
public class IbatisPropertyPaddingAutoProxyCreator extends AbstractAutoProxyCreator implements InitializingBean {
    private static Log logger = LogFactory.getLog(SqlMapClientTemplate.class);
    private final static String INSERT = "insert";

    protected Object[] getAdvicesAndAdvisorsForBean(Class beanClass, String beanName, TargetSource customTargetSource) throws BeansException {
        if (SqlMapClientOperations.class.isAssignableFrom(beanClass)) {
            return new Object[] {new PropertyPaddingBeforeAdvice()};
        }
        
        return DO_NOT_PROXY;
    }
    
    /** ������������ʱ, ��д<code>id</code>,<code>createdTime</code>,<code>operatorId</code>��ȱʡֵ */
    private static class PropertyPaddingBeforeAdvice extends StaticMethodMatcherPointcutAdvisor implements MethodBeforeAdvice {
        public PropertyPaddingBeforeAdvice() {
            setAdvice(this);
        }

        public void before(Method method, Object[] args, Object target) throws Throwable {
            /* SqlMapClientOperations's insert(String, Object) */
            if (args.length == 2) {
                try {
                    DaoHelper.getPropertyPadding().padding(args[1]);
                } catch (Throwable e) {
                    /* ��Զ���������׳��쳣 */
                    logger.warn(e.getMessage(), e);
                }
            }
        }

        public boolean matches(Method method, Class targetClass) {
            return INSERT.equals(method.getName());
        }        
    }

    public void afterPropertiesSet() throws Exception {
        setProxyTargetClass(true);
    }
}
