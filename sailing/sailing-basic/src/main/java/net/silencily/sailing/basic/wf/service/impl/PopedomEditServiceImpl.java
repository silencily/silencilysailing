package net.silencily.sailing.basic.wf.service.impl;

import net.silencily.sailing.basic.wf.domain.TblWfEditInfo;
import net.silencily.sailing.basic.wf.service.PopedomEditService;
import net.silencily.sailing.basic.wf.web.PopedomEditForm;
import net.silencily.sailing.common.crud.service.CommonService;
import net.silencily.sailing.container.ServiceProvider;

import org.springframework.orm.hibernate3.HibernateTemplate;

public class PopedomEditServiceImpl implements PopedomEditService{
	
	private HibernateTemplate hibernateTemplate;
	
	/**
	 * @return the hibernateTemplate
	 */
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	/**
	 * @param hibernateTemplate the hibernateTemplate to set
	 */
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	public void save(PopedomEditForm theForm) throws Exception {
		
		// 获得合同变更信息实体bean
		TblWfEditInfo tblWfEditInfo=theForm.getBean();
		getCommonService().saveOrUpdate(tblWfEditInfo);
		
	}
	
	public void delete(String oid) {

		//根据oid获得变更合同
		TblWfEditInfo tblWfEditInfo = (TblWfEditInfo) getCommonService().load(
				TblWfEditInfo.class, oid);
		getCommonService().deleteLogic(tblWfEditInfo);
	}
	
	private CommonService getCommonService() {
		
		return (CommonService) ServiceProvider.getService(CommonService.SERVICE_NAME);
	}
}
