package net.silencily.sailing.framework.persistent.ibatis;

import net.silencily.sailing.container.ConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.orm.ibatis.SqlMapClientFactoryBean;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * ���ʳ־û�����ʱ���ò��Ե�֧����, �����Զ���������, �������ƵȲ���. �������Ҫ��ͬһ������������
 * <code>SqlMapClientFactoryBean</code>, ���Ұ�����������Ϊ����������, �Ի�ö������������
 * ���������Ĳ�Ʒ, Ϊʲô��ʹ��<code>Decorator</code>��װ<code>SqlMapClientFactoryBean</code>
 * ��? ԭ���ǵ�<code>SqlMapClientFactoryBean</code>�ı�ʱ�ᵼ�������Ŀ���������
 * @author scott
 * @since 2006-4-15
 * @version $Id: IbatisPersistentSupportFactoryBean.java,v 1.1 2010/12/10 10:54:25 silencily Exp $
 *
 */
public class IbatisPersistentSupportFactoryBean implements FactoryBean, InitializingBean, BeanFactoryAware {
    private static Log logger = LogFactory.getLog(SqlMapClientTemplate.class);
    
    private SqlMapClient sqlMapClient;
    
    private SqlMapClient proxySqlMapClient;
    
    private String sqlMapClientFactoryBeanName;
    
    private BeanFactory beanFactory;

    private SqlMapClientFactoryBean sqlMapClientFactoryBean;
    
    private SqlMapProxy sqlMapProxy = new SqlMapProxy(logger);
    
    public void setSqlMapClientFactoryBeanName(String sqlMapClientFactoryBeanName) {
        this.sqlMapClientFactoryBeanName = sqlMapClientFactoryBeanName;
    }
    
    public void setSqlMapProxy(SqlMapProxy sqlMapProxy) {
        this.sqlMapProxy = sqlMapProxy;
    }

    public Object getObject() throws Exception {
        return proxySqlMapClient;
    }

    public Class getObjectType() {
        return this.sqlMapClientFactoryBean.getObjectType();
    }

    public boolean isSingleton() {
        return this.sqlMapClientFactoryBean.isSingleton();
    }
    
    /**
     * <p>����<code>SqlMapClient</code>�Ĵ���</p>
     * <p>��������һ��<code>SqlMapClient</code>���㹻��, ��Ϊ���̳�<code>SqlMapExecutor</code>,
     * ���Զ�������ʹ�����ζ���������
     */
    public void afterPropertiesSet() throws Exception {
        if (this.sqlMapClientFactoryBeanName == null) {
            throw new ConfigurationException("û������'SqlMapClientFactoryBean' bean ����");
        }
        
        this.sqlMapClientFactoryBean = (SqlMapClientFactoryBean) beanFactory
            .getBean(BeanFactory.FACTORY_BEAN_PREFIX + this.sqlMapClientFactoryBeanName);
        this.sqlMapClient = (SqlMapClient) this.sqlMapClientFactoryBean.getObject();
        this.proxySqlMapClient = sqlMapProxy.proxy(sqlMapClient);
    }

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
