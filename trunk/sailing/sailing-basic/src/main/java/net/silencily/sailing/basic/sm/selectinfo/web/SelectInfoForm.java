package net.silencily.sailing.basic.sm.selectinfo.web;

import java.util.ArrayList;
import java.util.List;

import net.silencily.sailing.basic.sm.domain.TblCmnUserMember;
import net.silencily.sailing.common.crud.service.CommonService;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.struts.BaseFormPlus;

import org.apache.commons.lang.StringUtils;




/**
 * @author liudl
 *
 */
public class SelectInfoForm extends BaseFormPlus {

	private List infoList = new ArrayList();
	
	private String checkType;

	
	public List getInfoList() {
		return infoList;
	}


	public void setInfoList(List infoList) {
		this.infoList = infoList;
	}
	
	public String getCheckType() {
		return checkType;
	}
	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}
	
	
	public TblCmnUserMember getInfoList(int i) throws IndexOutOfBoundsException {
		while (infoList.size() <= i) {
			infoList.add(null);
		}
		TblCmnUserMember detail = (TblCmnUserMember) infoList.get(i);
		String SId = request.getParameter("infoList[" + i + "].id");
		if (StringUtils.isNotBlank(SId)) {
			detail = (TblCmnUserMember) getService().load(TblCmnUserMember.class, SId);
		}
		if (null == detail) {
			detail = new TblCmnUserMember();
		}
		// 级联保存方式；
		infoList.set(i, detail);

		return detail;
	}

	
	public void setInfoList(TblCmnUserMember exp,int index) throws IndexOutOfBoundsException {
		this.infoList.set(index, exp);
	}
	
	private CommonService getService() {
		return (CommonService)ServiceProvider.getService(CommonService.SERVICE_NAME);
	}
}
