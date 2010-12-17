/*
 * Created on 2004-12-8
 */
package net.silencily.sailing.framework.web.webwork.interceptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.ActionProxy;
import com.opensymphony.xwork.ActionSupport;
import com.opensymphony.xwork.interceptor.Interceptor;
import com.opensymphony.xwork.interceptor.PreResultListener;

/**
 * 注意：只对继承 ActionSupport 的 Action 做事务拦截
 * @since 2005-8-24
 * @author 钱安川 
 *         E-mail:qac@coheg.com.cn
 * @version $Id: XWorkTransactionInterceptor.java,v 1.1 2010/12/10 10:54:24 silencily Exp $
 */
public class XWorkTransactionInterceptor implements Interceptor{
	
	public static final String TRANSACTION_MANAGER = "transactionManager";
	
	private static ThreadLocal currentTransactionStatus = new ThreadLocal();
	
	private static Log logger = LogFactory.getLog(XWorkTransactionInterceptor.class);
	
	private PlatformTransactionManager transactionManager;

	public void destroy() {
		
	}
	public void init() {
		
	}

	/**
	 * @see com.opensymphony.xwork.interceptor.Interceptor#intercept(com.opensymphony.xwork.ActionInvocation)
	 */
	public String intercept(final ActionInvocation invocation) throws Exception {
		if(!isPermissionCheck((Action)invocation.getAction()) || isIgnoreTransaction(invocation)){
			return invocation.invoke();
		}
		
		final TransactionAttribute transAtt = new DefaultTransactionAttribute(TransactionDefinition.PROPAGATION_REQUIRED);
		final TransactionStatus status[] = new TransactionStatus[1];
		TransactionStatus oldTransactionStatus = null;
		
		if(logger.isInfoEnabled()){
			logger.info("Getting transaction for action '" + getDetails(invocation.getProxy()) + "'");
		}
				
		status[0] = getTransactionManager().getTransaction(transAtt);
		oldTransactionStatus = (TransactionStatus)currentTransactionStatus.get();
		currentTransactionStatus.set(status[0]);
		String retVal = null;
		
		try{
			//在addPreResultListener中进行事务的提交
			//这样，事务提交在Action执行之后，result执行之前调用。
			if(status[0] != null)
				invocation.addPreResultListener(new PreResultListener() {
					public void beforeResult(ActionInvocation actionInvocation, String s) {
						
						if(!status[0].isCompleted()){
							if(logger.isInfoEnabled()){						
								logger.info("Committing transaction for " + getDetails(invocation.getProxy()) + " before result");
							}
							getTransactionManager().commit(status[0]);
						}
						/**
						if(logger.isInfoEnabled()){
							logger.info("Opening new transaction for " + getDetails(invocation.getProxy()) + " result");
						}
							
						status[0] = getTransactionManager().getTransaction(transAtt);
						*/
	                }
				});
	            retVal = invocation.invoke();
	        } 
			catch(Exception ex) {
	            if(status[0] != null) {
	            	onThrowable(invocation, transAtt, status[0], ex);
	            }	    
	            //return ExceptionHandlerInterceptor.GLOBAL_ERROR;
	            throw ex;
	        } 
			catch(Throwable t) {
	            if(status[0] != null) {
	            	onThrowable(invocation, transAtt, status[0], t);
	            }	      
	            //return ExceptionHandlerInterceptor.GLOBAL_ERROR;
	            throw new RuntimeException(t);
	        }
			finally{
	            if(transAtt != null) {
	            	currentTransactionStatus.set(oldTransactionStatus);
	            }               
	        }
			
	        if(status[0] != null && !status[0].isCompleted()){
	        	if(logger.isInfoEnabled()){
	            	logger.info("Invoking commit for transaction on method '" + getDetails(invocation.getProxy()) + "'");
	            }
	                
	            getTransactionManager().commit(status[0]);
	        }
	        return retVal;

	}
	/**
	 * 
	 * @param action
	 * @return
	 */
	private boolean isPermissionCheck(Action action){
	 	return action instanceof ActionSupport;
	}
	
	private boolean isIgnoreTransaction(ActionInvocation invocation){
		boolean isIgnore = false;
		String actionName = invocation.getInvocationContext().getName();
		if(actionName.indexOf("!doDefault") >0){
			isIgnore = true;
		}
		if(((ActionSupport)invocation.getAction()).hasErrors()){
			isIgnore = true;
		}
		return isIgnore;
	}
	
	public void setTransactionManager(PlatformTransactionManager transactionManager){
		this.transactionManager = transactionManager;
	}

	public static TransactionStatus currentTransactionStatus() throws Exception{
		TransactionStatus status = (TransactionStatus)currentTransactionStatus.get();
		if(status == null) {
			throw new RuntimeException("No transaction status in scope");
		} else {
			return status;
		}		
	}
	
	public PlatformTransactionManager getTransactionManager(){
        if(transactionManager == null) {
        	transactionManager = (PlatformTransactionManager) 
        							WebApplicationContextUtils.getRequiredWebApplicationContext(ServletActionContext.getServletContext()).getBean(TRANSACTION_MANAGER);
        }
        return transactionManager;
    }
	
	private String getDetails(ActionProxy proxy){
		String methodName = proxy.getConfig().getMethodName();
		if(methodName == null){
			methodName = "execute";
		}
		String actionClazz = proxy.getConfig().getClassName();
		
		return proxy.getNamespace() + "/" + proxy.getActionName() + ".action (" + actionClazz + "." + methodName + "())";
	}
	
	private void onThrowable(ActionInvocation invocation, TransactionAttribute txAtt, TransactionStatus status, Throwable ex){
		try{
	 		if(txAtt.rollbackOn(ex)){
	 			logger.error("Invoking rollback for transaction on action '" + getDetails(invocation.getProxy()) + "' due to throwable: " + ex, ex);
	 			getTransactionManager().rollback(status);
	 			//populateException(invocation,ex);
	 		} else{
	 			if(logger.isInfoEnabled()){
	 				logger.debug("Action '" + getDetails(invocation.getProxy()) + "' threw throwable but this does not force transaction rollback: " + ex, ex);
	 			}
	 			getTransactionManager().commit(status);
	            }
	        }catch(Exception e){
	            logger.error("@@@^^^@@@ Attempted rollback caused exception: " + e, e);
	           //populateException(invocation,e);
	            e.printStackTrace();
	        }
	    }
	
}
