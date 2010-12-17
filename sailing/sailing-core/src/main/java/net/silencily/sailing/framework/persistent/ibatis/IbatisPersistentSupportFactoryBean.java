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
 * 访问持久化数据时公用策略的支持类, 包括自动生成主键, 并发控制等操作. 这个类需要在同一个工厂中配置
 * <code>SqlMapClientFactoryBean</code>, 并且把它的名称做为参数传进来, 以获得对它本身的引用
 * 而不是它的产品, 为什么不使用<code>Decorator</code>包装<code>SqlMapClientFactoryBean</code>
 * 呢? 原因是当<code>SqlMapClientFactoryBean</code>改变时会导致这个类的可用性问题
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
     * <p>创建<code>SqlMapClient</code>的代理</p>
     * <p>仅仅创建一个<code>SqlMapClient</code>就足够了, 因为它继承<code>SqlMapExecutor</code>,
     * 所以对于两种使用情形都可以满足
     */
    public void afterPropertiesSet() throws Exception {
        if (this.sqlMapClientFactoryBeanName == null) {
            throw new ConfigurationException("没有配置'SqlMapClientFactoryBean' bean 名称");
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
