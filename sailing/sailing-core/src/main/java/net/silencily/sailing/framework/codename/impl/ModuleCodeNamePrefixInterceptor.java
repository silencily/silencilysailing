package net.silencily.sailing.framework.codename.impl;

import java.util.List;

import net.silencily.sailing.exception.UnexpectedException;
import net.silencily.sailing.framework.codename.AbstractModuleCodeName;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * <p>用于为描述在{@link CodeName}中的第一种代码设置前缀, 当保存时为代码增加"模块名"前缀
 * , 当把代码检索出来时去掉前缀. 以避免不同的模块之间在异地开发时可能造成的代码重复</p>
 * <p>当修改{@link AbstractModuleCodeNameService}时一定要同时注意这个类, 这个类作用于
 * 那里的方法的参数和返回值</p>
 * @author zhangli
 * @version $Id: ModuleCodeNamePrefixInterceptor.java,v 1.1 2010/12/10 10:54:27 silencily Exp $
 * @since 2007-3-30
 * @see AbstractModuleCodeNameService
 * @see AbstractModuleCodeName
 */
public class ModuleCodeNamePrefixInterceptor implements MethodInterceptor {

    /**
     * 当检索, 保存{@link AbstractModuleCodeName}时, 为参数增加<code>module name prefix</code>,
     * 并且从返回的结果中的<code>code</code>去掉<code>module name prefix</code>
     */
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Object[] arguments = methodInvocation.getArguments();
        if (arguments != null) {
            if (arguments.length == 1 && AbstractModuleCodeName.class.isInstance(arguments[0])) {
                /* save, update */
                String code = ((AbstractModuleCodeName) arguments[0]).getCode();
                ((AbstractModuleCodeName) arguments[0]).setCode(prefixCodeWithModuleName(arguments[0].getClass(), code));
            } else if (arguments.length == 2 
                && arguments[0].getClass() == Class.class 
                && arguments[1].getClass() == String.class) {
                /* list, load */
                arguments[1] = prefixCodeWithModuleName((Class) arguments[0], (String) arguments[1]); 
            }
        }
        Object ret = methodInvocation.proceed();
        if (ret != null) {
            if (AbstractModuleCodeName.class.isInstance(ret)) {
                AbstractModuleCodeName cn = (AbstractModuleCodeName) ret;
                ret = unprefixCodeWithModuleName(cn);
            } else if (List.class.isInstance(ret)) {
                CollectionUtils.transform((List) ret, unprefixTransformer);
            }
        }
        return ret;
    }
    
    /**
     * 复制一个<code>CodeName</code>, 把<code>persistent</code>转变为<code>detached</code>
     * @param cn 要复制的<code>persistent</code>对象
     * @return 一个属性完全相同的实例
     */
    private AbstractModuleCodeName unprefixCodeWithModuleName(AbstractModuleCodeName cn) {
        AbstractModuleCodeName ret = (AbstractModuleCodeName) cn.clone();
        String moduleName = ret.getModuleName();
        if (ret.getCode().startsWith(moduleName)) {
            ret.setCode(cn.getCode().substring(moduleName.length() + 1));
        }
        return ret;
    }
    
    /**
     * 根据参数<code>clazz's getModuleName()</code>为检索方法的代码参数加前缀
     * @param clazz {@link AbstractModuleCodeName}的子类
     * @param code 检索这个<code>code</code>的<code>CodeName</code>
     * @return 增加了模块名称前缀的代码
     */
    private String prefixCodeWithModuleName(Class clazz, String code) {
        AbstractModuleCodeName cn = null;
        try {
            cn = (AbstractModuleCodeName) clazz.newInstance();
        } catch (Exception e) {
            throw new UnexpectedException("不能实例化" + clazz.getName(), e);
        }
        return new StringBuffer(cn.getModuleName()).append(".").append(code).toString();
    }
    
    private Transformer unprefixTransformer = new UnprefixTransformer();
    
    private class UnprefixTransformer implements Transformer {
        public Object transform(Object element) {
            AbstractModuleCodeName cn = (AbstractModuleCodeName) element;
            return unprefixCodeWithModuleName(cn);
        }
    }
}
