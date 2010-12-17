package net.silencily.sailing.basic.sm.perameter.web;

import java.util.List;

import net.silencily.sailing.basic.sm.domain.TblCmnSysParameter;
import net.silencily.sailing.common.crud.service.CommonService;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.struts.BaseFormPlus;

import org.apache.commons.lang.StringUtils;

public class ParameterManageForm extends BaseFormPlus{
	
	private TblCmnSysParameter sysParameter;
	

	public TblCmnSysParameter getSysParameter() {
		if(sysParameter==null){
			if(StringUtils.isBlank(getOid())){
				sysParameter=new TblCmnSysParameter();
			}
			else{
				sysParameter=(TblCmnSysParameter)cService().load(TblCmnSysParameter.class, getOid());
			}
		}
		return sysParameter;
	}

	public void setSysParameter(TblCmnSysParameter sysParameter) {
		this.sysParameter = sysParameter;
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
