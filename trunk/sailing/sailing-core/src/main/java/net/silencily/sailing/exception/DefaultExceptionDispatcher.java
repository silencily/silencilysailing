package net.silencily.sailing.exception;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ClassUtils;

/**
 * <p>�쳣����ܹ���<code>ExceptionDispatcher</code>��ȱʡʵ��, Ӧ���в���Ҫ���������չ. ����
 * �������ṩ����չ���ƾ�Ӧ������������. �����쳣��������˳�������Ȳ��ҷ������쳣������صĴ�����,
 * ���û���ҵ�, ���β����쳣�ĸ���, ֱ���ҵ�Ϊֹ, ���û���ҵ�ʹ��ȱʡ���쳣����������.</p>
 * <p>ע��: Ҫ���쳣���������̰߳�ȫ��, ��Ϊ����ÿ�η���ͬһ��ʵ��</p>
 * @author scott
 * @since 2006-4-1
 * @version $Id: DefaultExceptionDispatcher.java,v 1.1 2010/12/10 10:54:25 silencily Exp $
 *
 */
public class DefaultExceptionDispatcher implements ExceptionDispatcher {
    private Map handleres = new HashMap(20);
    
    /**
     * ���û��Ϊһ���쳣�ҵ�ǡ�����쳣�������ͷ������������, Ϊʲôû����<code>handleres</code>
     * �������<code>last-line</code>��? ������������õĸ�����
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
     * ��{@link #handleres handleres}��<code>key</code>�ĵ��������ҳ�����<code>clazz</code>
     * ����ĸ���
     * @param clazz �쳣����
     * @return ����<code>clazz</code>����ĸ���, �������û��ֱ�ӵĸ��෵��<code>null</code>
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
