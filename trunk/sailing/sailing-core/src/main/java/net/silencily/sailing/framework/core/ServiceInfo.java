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
 * 描述一个配置信息的说明, 用途等, 通常配置在子配置的配置文件中, 格式是<pre>
 *   &lt;bean id="serviceInfo" class="com.coheg.core.ServiceInfo"&gt;
 *     &lt;property name="description"&gt;配置说明&lt;/property&gt;
 *     &lt;property name="name"&gt;名称&lt;/property&gt;
 *   &lt;/bean&gt;
 * </pre>
 * 这个类目的用于<code>springframework's xml bean factory</code>的配置文件中
 * 
 * @author scott
 * @since 2006-2-22
 * @version $Id: ServiceInfo.java,v 1.1 2010/12/10 10:54:25 silencily Exp $
 */
public class ServiceInfo implements InitializingBean, Comparator {
    private Log logger = LogFactory.getLog(ServiceInfo.class);
    
    public static final InitializingResource[] EMPTY_INITIALIZING_RESOURCE = new InitializingResource[0];
    
    /**
     * 这个配置的描述信息, 比如: "OA模块配置"
     */
    private String description;
    
    /**
     * 当系统加载配置时这个模块应该满足这个顺序, 这个顺序是同一级目录下各配置的加载顺序, 如果两个
     * 配置不在同一个上级配置下, 就不存在这种比较关系
     */
    private int order;
    
    /**
     * 配置文件所在的类路径, 这个值由服务加载程序设置, 不应该配置在文件中
     */
    private String path;
    
    /**
     * 需要执行初始化工作的特定子系统的组件的<code>FQN</code>名称
     */
    private String[] initializations;
    
    /**
     * 需要执行初始化工作的特定子系统的组件的实例
     */
    private InitializingResource[] initializingResources;
    
    /**
     * 服务配置全局唯一的名称, 常用于根据这个名称检索这个配置的<code>bean factory</code>,如果
     * 没有定义就是用配置在<code>xml</code>配置文件的<code>ServiceInfo</code>的<code>id</code>
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
     * 应用启动时调用这个方法检索需要做初始化工作的组件, 如果没有这样的组件返回唯一的实例
     * {@link #EMPTY_INITIALIZING_RESOURCE}
     * @return 需要做初始化工作的组件
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
                msg = "为系统[" + name + "]初始化组件[" + this.initializations[i] + "]时没有找到这个类";
                ex = e;
            } catch (ClassCastException e) {
                msg = "为系统[" + name + "]初始化组件[" + this.initializations[i] + "]时描述的类不是InitializingResource";
                ex = e;
            } catch (Throwable e) {
                ex = e;
                msg = "为系统[" + name + "]初始化组件[" + this.initializations[i] + "]时发生错误:" + e.getMessage();
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
    
    /* 仅使用名称判断, 因为配置名称必须全局唯一 */
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
            throw new BeanCreationException("配置ServiceInfo时没有指定名称name");
        }
    }
}
