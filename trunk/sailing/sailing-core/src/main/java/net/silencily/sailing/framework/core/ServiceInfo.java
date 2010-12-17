package net.silencily.sailing.framework.core;

import java.util.Comparator;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.ClassUtils;

import net.silencily.sailing.resource.InitializingResource;

/**
 * ����һ��������Ϣ��˵��, ��;��, ͨ�������������õ������ļ���, ��ʽ��<pre>
 *   &lt;bean id="serviceInfo" class="com.coheg.core.ServiceInfo"&gt;
 *     &lt;property name="description"&gt;����˵��&lt;/property&gt;
 *     &lt;property name="name"&gt;����&lt;/property&gt;
 *   &lt;/bean&gt;
 * </pre>
 * �����Ŀ������<code>springframework's xml bean factory</code>�������ļ���
 * 
 * @author scott
 * @since 2006-2-22
 * @version $Id: ServiceInfo.java,v 1.1 2010/12/10 10:54:25 silencily Exp $
 */
public class ServiceInfo implements InitializingBean, Comparator {
    private Log logger = LogFactory.getLog(ServiceInfo.class);
    
    public static final InitializingResource[] EMPTY_INITIALIZING_RESOURCE = new InitializingResource[0];
    
    /**
     * ������õ�������Ϣ, ����: "OAģ������"
     */
    private String description;
    
    /**
     * ��ϵͳ��������ʱ���ģ��Ӧ���������˳��, ���˳����ͬһ��Ŀ¼�¸����õļ���˳��, �������
     * ���ò���ͬһ���ϼ�������, �Ͳ��������ֱȽϹ�ϵ
     */
    private int order;
    
    /**
     * �����ļ����ڵ���·��, ���ֵ�ɷ�����س�������, ��Ӧ���������ļ���
     */
    private String path;
    
    /**
     * ��Ҫִ�г�ʼ���������ض���ϵͳ�������<code>FQN</code>����
     */
    private String[] initializations;
    
    /**
     * ��Ҫִ�г�ʼ���������ض���ϵͳ�������ʵ��
     */
    private InitializingResource[] initializingResources;
    
    /**
     * ��������ȫ��Ψһ������, �����ڸ���������Ƽ���������õ�<code>bean factory</code>,���
     * û�ж��������������<code>xml</code>�����ļ���<code>ServiceInfo</code>��<code>id</code>
     */
    private String name;

    public String getDescription() {
        return description;
    }

    public void setDescription(String decription) {
        this.description = decription;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String[] getInitializations() {
        return initializations;
    }

    public void setInitializations(String[] initializations) {
        this.initializations = initializations;
    }
    
    /**
     * Ӧ������ʱ�����������������Ҫ����ʼ�����������, ���û���������������Ψһ��ʵ��
     * {@link #EMPTY_INITIALIZING_RESOURCE}
     * @return ��Ҫ����ʼ�����������
     */
    public InitializingResource[] getInitializingResources() {
        if (this.initializations == null || this.initializations.length == 0) {
            return EMPTY_INITIALIZING_RESOURCE;
        }
        
        String msg = null;
        Throwable ex = null;
        this.initializingResources = new InitializingResource[this.initializations.length];
        for (int i = 0; msg == null && i < this.initializations.length; i++) {
            Class clazz = null;
            try {
                clazz = ClassUtils.forName(this.initializations[i]);
                this.initializingResources[i] = (InitializingResource) BeanUtils.instantiateClass(clazz);
            } catch (ClassNotFoundException e) {
                msg = "Ϊϵͳ[" + name + "]��ʼ�����[" + this.initializations[i] + "]ʱû���ҵ������";
                ex = e;
            } catch (ClassCastException e) {
                msg = "Ϊϵͳ[" + name + "]��ʼ�����[" + this.initializations[i] + "]ʱ�������಻��InitializingResource";
                ex = e;
            } catch (Throwable e) {
                ex = e;
                msg = "Ϊϵͳ[" + name + "]��ʼ�����[" + this.initializations[i] + "]ʱ��������:" + e.getMessage();
            }
        }
        if (msg != null) {
            logger.error(msg, ex);
            initializingResources = EMPTY_INITIALIZING_RESOURCE;
        }
        return initializingResources;
    }

    public void setInitializingResources(InitializingResource[] initializingResources) {
        this.initializingResources = initializingResources;
    }

    public int hashCode() {
        return name == null ? 0 : name.hashCode();
    }
    
    /* ��ʹ�������ж�, ��Ϊ�������Ʊ���ȫ��Ψһ */
    public boolean equals(Object o) {
        return (o instanceof ServiceInfo) && name.equals(((ServiceInfo) o).name);
    }

    public int compare(Object o1, Object o2) {
        ServiceInfo info1 = (ServiceInfo) o1;
        ServiceInfo info2 = (ServiceInfo) o2;
        
        return info1.order - info2.order;
    }

    public void afterPropertiesSet() throws Exception {
        if (StringUtils.isBlank(name)) {
            throw new BeanCreationException("����ServiceInfoʱû��ָ������name");
        }
    }
}
