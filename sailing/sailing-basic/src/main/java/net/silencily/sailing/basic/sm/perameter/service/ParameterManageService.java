package net.silencily.sailing.basic.sm.perameter.service;

import net.silencily.sailing.basic.sm.domain.TblCmnSysParameter;
import net.silencily.sailing.framework.core.ServiceBase;

public interface ParameterManageService extends ServiceBase{
	/**
	 * 
	 */
	public static final String SERVICE_NAME = "sm.parameterManageService";
	
	/**
	 * 
	 * 功能描述 根据系统参数标识获得系统参数实例
	 * @param parameterSign
	 * @return
	 * 2008-1-10 下午08:25:57
	 * @version 1.0
	 * @author baihe
	 */
	public TblCmnSysParameter getTblSysParameter(String parameterSign);
	
	/**
	 * 
	 * 功能描述 根据系统参数标识获得系统参数实例
	 * @param parameterSign
	 * @return
	 * 2008-1-10 下午08:25:57
	 * @version 1.0
	 * @author baihe
	 */
	public String getSysParameter(String parameterSign);
}
