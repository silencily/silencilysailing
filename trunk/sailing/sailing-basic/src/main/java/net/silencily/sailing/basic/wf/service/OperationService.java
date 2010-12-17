package net.silencily.sailing.basic.wf.service;

import net.silencily.sailing.framework.core.ServiceBase;

/**
 * @author ¸ð½¨±¦
 *
 */
public interface OperationService extends ServiceBase{
	public static final String SERVICE_NAME = "wf.OperationService";
	
	public Object load(Class c, String id);

}
