/*
 * Created on 2004-4-2
 * ActionPropertyExportInterceptor.java
 */
package net.silencily.sailing.framework.web.webwork.interceptor;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.webwork.WebWorkStatics;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.interceptor.AroundInterceptor;
import com.opensymphony.xwork.interceptor.PreResultListener;

/**
 * Populates HTTP Request Attributes with all gettable properties of the current action.
 * @author Ç®°²´¨
 * @version $Id: ActionPropertyExportInterceptor.java,v 1.1 2010/12/10 10:54:24 silencily Exp $
 */

public class ActionPropertyExportInterceptor extends AroundInterceptor {
	private static final Log log = LogFactory.getLog(ActionPropertyExportInterceptor.class);
	
	protected void before(ActionInvocation invocation) throws Exception {
		if(log.isInfoEnabled()){
			log.info("[***] Start ActionPropertyExportInterceptor");
		}
		invocation.addPreResultListener( new PropertyExporter() );
	}
	
	protected void after(ActionInvocation dispatcher, String result) throws Exception { 
	}    
	
	public static class PropertyExporter implements PreResultListener {
		private static final List   ignore = Arrays.asList(new String[] {"class", "texts"}); //skip getClass,...        //Invoked after Action.execute() but before Result
		//Calls all getters of the action and insert the values into the request
		
		public void beforeResult(ActionInvocation invocation, String resultCode) {
			Map props = extractGetterPropertyValues( invocation.getAction() );
			HttpServletRequest request = getRequest(invocation);
			
			for (Iterator it = props.entrySet().iterator(); it.hasNext();) {
				Map.Entry   e = (Map.Entry) it.next();
				request.setAttribute((String) e.getKey(), e.getValue());
			}
			
		}        
		
		public Map extractGetterPropertyValues(Object bean) {
			PropertyDescriptor[] descr = PropertyUtils.getPropertyDescriptors(bean);
			Map props = new HashMap();
			for (int i = 0; i < descr.length; i++) {
				PropertyDescriptor d = descr[i];
				if (d.getReadMethod() == null) continue;
				if (ignore.contains(d.getName())) continue;
				try {
					props.put(d.getName(), PropertyUtils.getProperty(bean, d.getName()));
				} catch (Exception e) { 
					log.error("props.put(d.getName(), PropertyUtils.getProperty(bean, d.getName())) error...",e);
				}
			}
			return props;
		}      
		  
		public HttpServletRequest getRequest(ActionInvocation invocation) {
			return (HttpServletRequest) invocation.getInvocationContext().get(WebWorkStatics.HTTP_REQUEST);
		}
	}
}
