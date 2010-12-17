package net.silencily.sailing.basic.wf.service;

import java.util.List;

import net.silencily.sailing.framework.core.ServiceBase;

/**
 * 
 * @author yangxl
 * @version 1.0
 */
public interface NapeEditService extends ServiceBase{

	/**
	 * ¿É±à¼­ÏîSERVICEÃû
	 */
	public static final String SERVICE_NAME = "wf.NapeEdit";
	
	public List getList(Class c,String nameoper);

}
