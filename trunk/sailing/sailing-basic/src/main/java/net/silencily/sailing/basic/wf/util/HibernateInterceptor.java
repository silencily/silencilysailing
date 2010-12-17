package net.silencily.sailing.basic.wf.util;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;

public class HibernateInterceptor extends StaticMethodMatcherPointcutAdvisor{

	public boolean matches(Method arg0, Class arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	

}
