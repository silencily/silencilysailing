package net.silencily.sailing.basic.wf.proxy;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.InvocationHandler;
import java.sql.Connection;

public class ProxyConnection implements InvocationHandler{
	private static final String CLOSE = "close";
	private static final String SET_AUTO_COMMIT = "setAutoCommit";
	private static final String COMMIT = "commit";
	private static final String ROLL_BACK = "rollback";
	private Object object;
	
	public Connection proxy(Object obj){
		object = obj;
		return (Connection)Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(), this);
	}
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object ret = null;
		String methodName = method.getName();
		
		if (CLOSE.equals(methodName)		
			||SET_AUTO_COMMIT.equals(methodName)
			||COMMIT.equals(methodName)
			||ROLL_BACK.equals(methodName)) {
			//do nothing
		} else {
			ret = method.invoke(object, args);
		}
		return ret;
	}
}
