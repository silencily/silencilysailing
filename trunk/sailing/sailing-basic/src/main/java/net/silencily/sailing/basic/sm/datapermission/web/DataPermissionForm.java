package net.silencily.sailing.basic.sm.datapermission.web;




import java.util.ArrayList;
import java.util.List;


import net.silencily.sailing.basic.sm.domain.TblCmnEntity;
import net.silencily.sailing.basic.sm.domain.TblCmnEntityMember;
import net.silencily.sailing.common.crud.service.CommonService;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.struts.BaseFormPlus;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;


public class DataPermissionForm extends BaseFormPlus {


	// -------------------------属性------------------------//

	
	private TblCmnEntity bean;

    private List list;
    
    private List member = new ArrayList();
    

    



	/**
	 * 获得持久化类的对象 
	 * @return
	 */
	public TblCmnEntity getBean() {
		if (bean == null) {
			if (StringUtils.isBlank(getOid())) {
				bean = new TblCmnEntity();
			}
			else {
				bean = (TblCmnEntity)getService().load(TblCmnEntity.class, getOid());
			}
		}
		return bean;		
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}
	
	/**
	 * 调用共同Service()接口
	 */
	private CommonService getService() {
		return (CommonService) ServiceProvider
				.getService(CommonService.SERVICE_NAME);
	}


	public void setBean(TblCmnEntity bean) {
		this.bean = bean;
	}
	
	public List getMember() {
		return member;
	}

	public void setMember(List member) {
		this.member = member;
	}

	public TblCmnEntityMember getMember(int i) throws IndexOutOfBoundsException {
		while (member.size() <= i) {
			member.add(null);
		}
		TblCmnEntityMember detail = (TblCmnEntityMember) member.get(i);
		String SId = request.getParameter("member[" + i + "].id");
		if (StringUtils.isNotBlank(SId)) {
			detail = (TblCmnEntityMember) getService().load(TblCmnEntityMember.class, SId);
		}
		if (null == detail) {
			detail = new TblCmnEntityMember();
		}
		// 级联保存方式；
		member.set(i, detail);

		return detail;
	}

	
	public void setMember(TblCmnEntityMember exp,int index) throws IndexOutOfBoundsException {
		this.member.set(index, exp);
	}
		
	}


