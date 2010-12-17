package net.silencily.sailing.basic.wf.web;

import net.silencily.sailing.basic.wf.domain.TblWfEditInfo;
import net.silencily.sailing.common.crud.service.CommonService;
import net.silencily.sailing.container.ServiceProvider;

import org.apache.commons.lang.StringUtils;

public class PopedomEditForm extends BisWorkflowFormSupport{

	/**
	 * 描述：
	 * 属性名：serialVersionUID
	 * 属性类型：long
	 */
	private static final long serialVersionUID = 1L;

	private TblWfEditInfo bean;
	
	private String callinfo;
	
	public String getCallinfo() {
		return callinfo;
	}

	public void setCallinfo(String callinfo) {
		this.callinfo = callinfo;
	}

	/**
	 * 
	 * @return
	 */
	public TblWfEditInfo getBean() {
		if (bean == null) {
			if (StringUtils.isBlank(getOid())) {
				bean = new TblWfEditInfo();
			} else {
				bean = (TblWfEditInfo) ((CommonService) ServiceProvider.getService(CommonService.SERVICE_NAME)).load(
						TblWfEditInfo.class, getOid());
			}
		}
		return bean;
	}

	/**
	 * 
	 * @param bean
	 */
	public void setBean(TblWfEditInfo bean) {
		this.bean = bean;
	}
	
	/**
	  * 获取共通服务
	  * @return
	  */
	 private CommonService getService() {
	 	return (CommonService)ServiceProvider.getService(CommonService.SERVICE_NAME);
	 }

}
