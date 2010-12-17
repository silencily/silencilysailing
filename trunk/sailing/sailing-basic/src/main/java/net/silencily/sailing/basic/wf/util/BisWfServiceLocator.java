/**
 * Name: BisWfServiceLocator.java
 * Author: Bis liyan
 */
package net.silencily.sailing.basic.wf.util;

import net.silencily.sailing.basic.wf.service.IWorkflowService;
import net.silencily.sailing.basic.wf.service.impl.BisWfServiceImpl;
import net.silencily.sailing.container.ServiceProvider;

/**
 * One locator for get service of BisWorkflow
 * 为了得到BisWorkflow's service 的一个资源定位器
 * @author Bis liyan
 */
public class BisWfServiceLocator {
	/**
	  * Get a IWorkflowService's implementation
	  * 得到一个IWorkflowService的实例
	  * @see ServiceProvider#getService(String)
	  * @return One instance of IWorkflowService.class {@link IWorkflowService}
	  */
	public static IWorkflowService getWorkflowService() {
		return (IWorkflowService) ServiceProvider.getService(BisWfServiceImpl.SERVICE_NAME);
	}
}
