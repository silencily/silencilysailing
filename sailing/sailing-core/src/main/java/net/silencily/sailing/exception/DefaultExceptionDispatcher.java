package net.silencily.sailing.exception;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ClassUtils;

/**
 * <p>异常处理架构中<code>ExceptionDispatcher</code>的缺省实现, 应用中不需要对这个类扩展. 仅仅
 * 利用它提供的扩展机制就应该满足需求了. 查找异常处理器的顺序是首先查找发生的异常的类相关的处理器,
 * 如果没有找到, 依次查找异常的父类, 直到找到为止, 如果没有找到使用缺省的异常处理器处理.</p>
 * <p>注意: 要求异常处理器是线程安全的, 因为这里每次返回同一个实例</p>
 * @author scott
 * @since 2006-4-1
 * @version $Id: DefaultExceptionDispatcher.java,v 1.1 2010/12/10 10:54:25 silencily Exp $
 *
 */
public class DefaultExceptionDispatcher implements ExceptionDispatcher {
    private Map handleres = new HashMap(20);
    
    /**
     * 如果没有为一个异常找到恰当的异常处理器就返回这个处理器, 为什么没有在<code>handleres</code>
     * 配置这个<code>last-line</code>呢? 就是想减少配置的复杂性
     */
    private BaseExceptionHandler fallbackExceptionHandler;
    
    public void setExceptionHandlers(Map map) {
        this.handleres.putAll(map);        
    }
    
    protected BaseExceptionHandler getFallbackExceptionHandler() {
        if (fallbackExceptionHandler == null) {
            this.fallbackExceptionHandler = new DefaultBaseExceptionHandler();
        }
        
        return this.fallbackExceptionHandler;
    }

    public BaseExceptionHandler dipacher(Throwable ex) {
        BaseExceptionHandler handler = (BaseExceptionHandler) handleres.get(ex.getClass());
        if (handler == null) {
            handler = (BaseExceptionHandler) this.handleres.get(
                findSuperClass(handleres.keySet().iterator(), ex.getClass()));
        }
        
        if (handler == null) {
            handler = getFallbackExceptionHandler();
        }

        return handler;
    }
    
    /**
     * 从{@link #handleres handleres}的<code>key</code>的迭代器中找出参数<code>clazz</code>
     * 最近的父类
     * @param clazz 异常类型
     * @return 参数<code>clazz</code>最近的父类, 如果参数没有直接的父类返回<code>null</code>
     */
    private Class findSuperClass(Iterator iterator, Class clazz) {
        Class ret = null;
        List classes = ClassUtils.getAllSuperclasses(clazz);
        int minIndex = -1;
        while (iterator.hasNext()) {
            Class candidate = (Class) iterator.next();
            int candidateIndex = classes.indexOf(candidate);
            if (minIndex == -1 || (candidateIndex != -1 && candidateIndex < minIndex)) {
                minIndex = candidateIndex;
            }
        }
        
        if (minIndex > -1) {
            ret = (Class) classes.get(minIndex);
        }

        return ret;
    }
}
