package net.silencily.sailing.framework.persistent.ibatis;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import net.silencily.sailing.exception.UnexpectedException;
import net.silencily.sailing.framework.persistent.filter.ReworkingParameter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.engine.execution.SqlExecutor;
import com.ibatis.sqlmap.engine.impl.ExtendedSqlMapClient;
import com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate;
import com.ibatis.sqlmap.engine.impl.SqlMapSessionImpl;
import com.ibatis.sqlmap.engine.mapping.statement.BaseStatement;

/**
 * <code>iBATIS's SqlMapClient</code>�Ĵ�����, �������һ�����<code>iBATIS</code>�־�
 * �����Ե�ʵ��, ��ǰ<code>iBATIS</code>�汾��<code>2.1.7</code>. ���ɵĴ���������, ���
 * <code>DTO</code>����<code>sql statement</code>����
 * @author scott
 * @since 2006-5-4
 * @version $Id: SqlMapProxy.java,v 1.1 2010/12/10 10:54:25 silencily Exp $
 *
 */
public class SqlMapProxy {
    private Log logger;
    /** <code>SqlMapClient's openSession</code>�������� */
    public static final String METHOD_OPEN_SESSION = "openSession";
    
    /** <code>ExtendedSqlMapClient's getSqlExecutor</code>�������� */
    public static final String METHOD_GET_SQL_EXECUTOR = "getSqlExecutor";
    
    /** <code>ExtendedSqlMapClient's getDelegate</code>�������� */
    public static final String METHOD_GET_DELEGATE = "getDelegate";

    /**
     * Ҫ�����<code>SqlMapExecutor</code>����, <code>key</code>�Ƿ�������, <code>
     * value</code>��<code>MethodExecutor</code>��ʵ��
     */
    private Map sqlMapExecutorMethods;
    
    /**
     * Ҫ�����<code>SqlExecutor</code>����, <code>key</code>�Ƿ�������, <code>
     * value</code>��<code>MethodExecutor</code>��ʵ��
     */
    private Map sqlExecutorMethods;
    
    private ExtendedSqlMapClient proxiedSqlMapClient;
    
    private SqlMapExecutorDelegate proxiedSqlMapExecutorDelegate;
    
    private SqlExecutor proxiedSqlExecutor;

    public SqlMapProxy() {
        this.logger = LogFactory.getLog(SqlMapProxy.class);
    }
    
    public SqlMapProxy(Log logger) {
        this.logger = logger;
    }

    public Map getSqlMapExecutorMethods() {
        if (sqlMapExecutorMethods == null) {
            sqlMapExecutorMethods = Collections.EMPTY_MAP;
        }
        return sqlMapExecutorMethods;
    }

    public void setSqlMapExecutorMethods(Map sqlMapExecutorMethods) {
        this.sqlMapExecutorMethods = sqlMapExecutorMethods;
    }
    

    public Map getSqlExecutorMethods() {
        if (sqlExecutorMethods == null) {
            sqlExecutorMethods = Collections.EMPTY_MAP;
        }
        return sqlExecutorMethods;
    }

    public void setSqlExecutorMethods(Map sqlExecutorMethods) {
        this.sqlExecutorMethods = sqlExecutorMethods;
    }

    public ExtendedSqlMapClient proxy(SqlMapClient sqlMapClient) {
        ClassLoader cl = getClass().getClassLoader();
        ExtendedSqlMapClient extendedSqlMapClient = (ExtendedSqlMapClient) sqlMapClient;
        
        proxiedSqlMapClient = (ExtendedSqlMapClient) Proxy.newProxyInstance(
            cl, 
            new Class[] {ExtendedSqlMapClient.class}, 
            new SqlMapClientInvocationHandler(extendedSqlMapClient));

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(SqlExecutor.class);
        enhancer.setCallback(new SqlExecutorInvocationHandler(getSqlExecutorMethods()));
        proxiedSqlExecutor = (SqlExecutor) enhancer.create();

        enhancer = new Enhancer();
        enhancer.setSuperclass(SqlMapExecutorDelegate.class);
        enhancer.setCallback(new SqlMapExecutorDelegateMethodInterceptor(extendedSqlMapClient));
        proxiedSqlMapExecutorDelegate = (SqlMapExecutorDelegate) enhancer.create();
        
        return proxiedSqlMapClient;
    }
    
    private class SqlMapClientInvocationHandler implements InvocationHandler {
        private ExtendedSqlMapClient extendedSqlMapClient;
        private ThreadLocal localSqlMapSession = new ThreadLocal();

        public SqlMapClientInvocationHandler(ExtendedSqlMapClient extendedSqlMapClient) {
            this.extendedSqlMapClient = extendedSqlMapClient;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Object ret = null;
            try {
				if (METHOD_OPEN_SESSION.equals(method.getName())) {
				    if (logger.isDebugEnabled()) {
				        logger.debug("��������[SqlMapClient]�ķ���[" + METHOD_OPEN_SESSION + "]");
				    }
				    /* following codes come from iBATIS's SqlMapClientImpl, now there isn't way */
				    SqlMapSessionImpl impl = (SqlMapSessionImpl) localSqlMapSession.get();                
				    if (impl == null || impl.isClosed()) {
				        Enhancer enhancer = new Enhancer();
				        enhancer.setCallback(new SqlMapSessionInvocationHandler(getSqlMapExecutorMethods()));
				        enhancer.setSuperclass(SqlMapSessionImpl.class);
				        impl = (SqlMapSessionImpl) enhancer.create(
				            new Class[] {ExtendedSqlMapClient.class}, 
				            new Object[] {proxy});
				    }
				    ret = impl;
				} else if (METHOD_GET_SQL_EXECUTOR.equals(method.getName())) {
				    if (logger.isDebugEnabled()) {
				        logger.debug("��������[ExtendedSqlMapClient]�ķ���[" + METHOD_GET_SQL_EXECUTOR + "]");
				    }
				    ret = proxiedSqlExecutor;
				} else if (METHOD_GET_DELEGATE.equals(method.getName())) {
				    if (logger.isDebugEnabled()) {
				        logger.debug("��������[ExtendedSqlMapClient]�ķ���[" + METHOD_GET_DELEGATE + "]");
				    }
				    ret = proxiedSqlMapExecutorDelegate;
				} else {
				    if (logger.isDebugEnabled()) {
				        logger.debug("û�д�������[" + method.getDeclaringClass() + "]�ķ���[" + method.getName() + "]");
				    }
				    ret = method.invoke(this.extendedSqlMapClient, args);
				}
			} catch (InvocationTargetException e) {
				throw e.getTargetException();
			}
            
            return ret;
        }
        
    }

    private class SqlMapSessionInvocationHandler implements MethodInterceptor {
        private Map methods;
        
        public SqlMapSessionInvocationHandler(Map methods) {
            this.methods = methods;
        }

        public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
            Object ret;
			try {
				ReworkingParameter executor = (ReworkingParameter) methods.get(method.getName());
				ret = null;
				preExecute(executor, args, method);
				ret = methodProxy.invokeSuper(proxy, args);
				postExecute(executor);
			} catch (InvocationTargetException e) {
				throw e.getTargetException();
			}

            return ret;
        }        
    }
    
    private class SqlExecutorInvocationHandler implements MethodInterceptor {
        private Map methods;
        
        public SqlExecutorInvocationHandler(Map methods) {
            this.methods = methods;
        }

        public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
            Object ret;
			try {
				ReworkingParameter rp = (ReworkingParameter) methods.get(method.getName());
				ret = null;
				preExecute(rp, args, method);
				ret = methodProxy.invokeSuper(proxy, args);
				postExecute(rp);
			} catch (InvocationTargetException e) {
				throw e.getTargetException();
			}

            return ret;
        }
        
    }
    
    /* �������ʵ������һ�� decorator, ʵ�ʵĹ����������� extendedSqlMapClient.getDelegate() ��ʵ���� */
    private class SqlMapExecutorDelegateMethodInterceptor implements MethodInterceptor {
        private ExtendedSqlMapClient extendedSqlMapClient;
        /* ��Щ�����ڲ��������� getMappedStatement(String) */ 
        private String[] methodNames = new String[] {"insert", "queryForList", "queryForObject", "queryForRowHandler", "update"};
        
        public SqlMapExecutorDelegateMethodInterceptor(ExtendedSqlMapClient extendedSqlMapClient) {
            this.extendedSqlMapClient = extendedSqlMapClient;
            Arrays.sort(methodNames);
        }
        
        public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
            Object ret;
			try {
				if (method.getModifiers() != Modifier.PUBLIC) {
				    return methodProxy.invokeSuper(proxy, args);
				}
				
				if (Arrays.binarySearch(methodNames, method.getName()) > -1) {
				    String id = (String) args[1];
				    proxiedSqlMapClient.getDelegate().getMappedStatement(id);
				}

				ret = method.invoke(extendedSqlMapClient.getDelegate(), args);
				
				if ("getMappedStatement".equals(method.getName())) {
				    BaseStatement baseStatement = (BaseStatement) ret;
				    baseStatement.setSqlMapClient(proxiedSqlMapClient);
				}
			} catch (InvocationTargetException e) {
				throw e.getTargetException();
			}
            return ret;
        }
    }
    
    private void preExecute(ReworkingParameter rp, Object[] params, Method method) {
        if (rp != null) {
            String info = "Ϊ[" 
                + method.getDeclaringClass() 
                + "]�ķ���[" 
                + method.getName() 
                + "]ִ��ReworkingParameter";
            
            if (logger.isDebugEnabled()) {
                logger.debug(info);
            }
            
            try {
                rp.rework(params);
            } catch (RuntimeException e) {
                throw new UnexpectedException(info + "," + rp.getClass().getName(), e);
            }
        }
    }
    
    private void postExecute(ReworkingParameter rp) {
        if (rp != null) {
            rp.restore();
        }
    }
}
