package net.silencily.sailing.framework.codename.impl.hibernate;

import java.util.Properties;

import net.silencily.sailing.exception.UnexpectedException;
import net.silencily.sailing.framework.codename.CodeName;

import org.apache.commons.lang.StringUtils;
import org.hibernate.usertype.ParameterizedType;

/**
 * <p>使用类型名称作为<code>UserType</code>配置的<code>CodeName</code>基类, 必须
 * 配置具体的<code>CodeName</code>类名称</p>这是一个例子<pre>
 *        &lt;property name="type" column="type_code" length="4000"&gt;
 *           &lt;type name="com.coheg.framework.codename.impl.hibernate.ModuleCodeNameUserType"&gt;
 *               &lt;param name="class.name"&gt;com.coheg.jobplan.codename.JobPlanModuleCodeName&lt;/param&gt;
 *           &lt;/type&gt;
 *       &lt;/property&gt;
 * </pre>
 * @author zhangli
 * @version $Id: AbstractParameterizedCodeNameUserType.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 * @since 2007-4-30
 */
public abstract class AbstractParameterizedCodeNameUserType implements ParameterizedType {
    
    /** 配置类名称的参数名称 */
    protected String KEY_CLASS_NAME = "class.name";
    
    protected Class clazz;
    
    protected ClassLoader cl = Thread.currentThread().getContextClassLoader();

    public final void setParameterValues(Properties parameters) {
        String className = null;
        if (parameters != null) {
            className = parameters.getProperty(KEY_CLASS_NAME);
        }
        if (StringUtils.isBlank(className)) {
            throw new NullPointerException("没有定义参数" + KEY_CLASS_NAME + "的值");
        }
        try {
            clazz = Class.forName(className, true, cl);
            internalParameterValues(parameters);
        } catch (ClassNotFoundException e) {
            throw new UnexpectedException("没有找到类" + className, e);
        }
    }
    
    /**
     * 创建一个{@link #KEY_CLASS_NAME}类型的实例, 因为是<code>CodeName</code>类型的
     * 类型, 所以返回这个类型
     * @return {@link #KEY_CLASS_NAME}类型的实例
     */
    protected CodeName instance() {
        try {
            return (CodeName) clazz.newInstance();
        } catch (Exception e) {
            throw new UnexpectedException("不能创建" + clazz.getName() + "的实例", e);
        }
    }
    
    /**
     * 具体的子类根据需要加载其它的自定义参数
     * @param parameters 配置在<code>hibernate mapping</code>中的参数
     */
    protected void internalParameterValues(Properties parameters) {}
}
