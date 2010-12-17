package net.silencily.sailing.basic.sm.datapermission.service;

import java.util.List;

import net.silencily.sailing.basic.sm.domain.TblCmnEntity;
import net.silencily.sailing.framework.core.ServiceBase;


public interface TblDataPermissionService extends ServiceBase {
	
	public static final String SERVICE_NAME = "cmn.entry";

	public List list(Class c, String oid);
	
	public List getList(Class c, String oid);
	
	List list(TblCmnEntity config, String radio);

}
