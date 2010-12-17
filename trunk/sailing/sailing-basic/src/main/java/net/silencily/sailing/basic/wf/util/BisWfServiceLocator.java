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
 * Ϊ�˵õ�BisWorkflow's service ��һ����Դ��λ��
 * @author Bis liyan
 */
public class BisWfServiceLocator {
	/**
	  * Get a IWorkflowService's implementation
	  * �õ�һ��IWorkflowService��ʵ��
	  * @see ServiceProvider#getService(String)
	  * @return One instance of IWorkflowService.class {@link IWorkflowService}
	  */
	public static IWorkflowService getWorkflowService() {
		return (IWorkflowService) ServiceProvider.getService(BisWfServiceImpl.SERVICE_NAME);
	}
}
