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
	 * �������� ����ϵͳ������ʶ���ϵͳ����ʵ��
	 * @param parameterSign
	 * @return
	 * 2008-1-10 ����08:25:57
	 * @version 1.0
	 * @author baihe
	 */
	public TblCmnSysParameter getTblSysParameter(String parameterSign);
	
	/**
	 * 
	 * �������� ����ϵͳ������ʶ���ϵͳ����ʵ��
	 * @param parameterSign
	 * @return
	 * 2008-1-10 ����08:25:57
	 * @version 1.0
	 * @author baihe
	 */
	public String getSysParameter(String parameterSign);
}
