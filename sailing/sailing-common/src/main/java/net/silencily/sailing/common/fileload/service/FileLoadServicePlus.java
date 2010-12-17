package net.silencily.sailing.common.fileload.service;

import net.silencily.sailing.struts.BaseFormPlus;


/**
 * @author wenjb
 *
 */
public interface FileLoadServicePlus extends FileLoadService {
	
	public static final String SERVICE_NAME = "common.FileLoadServicePlus";
	
	public String initFileList(BaseFormPlus baseFormPlus,Class c,String oid);
	
	public String saveFileList(BaseFormPlus baseFormPlus,Class c,String oid);
	
	public String deleteLogicFileList(Class c,String oid);
	
}
