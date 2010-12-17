package net.silencily.sailing.basic.sm.log.web;

import java.util.List;

import net.silencily.sailing.basic.sm.domain.TblCmnSysLog;
import net.silencily.sailing.common.crud.service.CommonService;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.struts.BaseFormPlus;

import org.apache.commons.lang.StringUtils;


public class LogManageForm extends BaseFormPlus{
	
	private TblCmnSysLog sysLog;
	
	private List cmnSysLog;

	public TblCmnSysLog getSysLog() {
		if(sysLog==null){
			if(StringUtils.isBlank(getOid())){
				sysLog=new TblCmnSysLog();
			}
			else{
				sysLog=(TblCmnSysLog)cService().load(TblCmnSysLog.class, getOid());
			}
		}
		return sysLog;
	}

	public void setSysLog(TblCmnSysLog sysLog) {
		this.sysLog = sysLog;
	}

	public List getCmnSysLog() {
		return cmnSysLog;
	}

	public void setCmnSysLog(List cmnSysLog) {
		this.cmnSysLog = cmnSysLog;
	}
	
	/**
	 * 
	 * 功能描述 调用公用接口
	 * @return
	 * 2007-11-23 下午06:12:06
	 * @version 1.0
	 * @author baihe
	 */
	private CommonService cService() {
		return (CommonService)ServiceProvider.getService(CommonService.SERVICE_NAME);
	}
}
