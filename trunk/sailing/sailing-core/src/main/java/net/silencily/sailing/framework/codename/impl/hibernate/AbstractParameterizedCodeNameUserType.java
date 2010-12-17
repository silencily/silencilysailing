package net.silencily.sailing.framework.codename.impl.hibernate;

import java.util.Properties;

import net.silencily.sailing.exception.UnexpectedException;
import net.silencily.sailing.framework.codename.CodeName;

import org.apache.commons.lang.StringUtils;
import org.hibernate.usertype.ParameterizedType;

/**
 * <p>ʹ������������Ϊ<code>UserType</code>���õ�<code>CodeName</code>����, ����
 * ���þ����<code>CodeName</code>������</p>����һ������<pre>
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
    
    /** ���������ƵĲ������� */
    protected String KEY_CLASS_NAME = "class.name";
    
    protected Class clazz;
    
    protected ClassLoader cl = Thread.currentThread().getContextClassLoader();

    public final void setParameterValues(Properties parameters) {
        String className = null;
        if (parameters != null) {
            className = parameters.getProperty(KEY_CLASS_NAME);
        }
        if (StringUtils.isBlank(className)) {
            throw new NullPointerException("û�ж������" + KEY_CLASS_NAME + "��ֵ");
        }
        try {
            clazz = Class.forName(className, true, cl);
            internalParameterValues(parameters);
        } catch (ClassNotFoundException e) {
            throw new UnexpectedException("û���ҵ���" + className, e);
        }
    }
    
    /**
     * ����һ��{@link #KEY_CLASS_NAME}���͵�ʵ��, ��Ϊ��<code>CodeName</code>���͵�
     * ����, ���Է����������
     * @return {@link #KEY_CLASS_NAME}���͵�ʵ��
     */
    protected CodeName instance() {
        try {
            return (CodeName) clazz.newInstance();
        } catch (Exception e) {
            throw new UnexpectedException("���ܴ���" + clazz.getName() + "��ʵ��", e);
        }
    }
    
    /**
     * ��������������Ҫ�����������Զ������
     * @param parameters ������<code>hibernate mapping</code>�еĲ���
     */
    protected void internalParameterValues(Properties parameters) {}
}
