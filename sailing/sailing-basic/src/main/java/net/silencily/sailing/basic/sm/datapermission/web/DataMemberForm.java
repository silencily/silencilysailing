package net.silencily.sailing.basic.sm.datapermission.web;

import java.util.ArrayList;
import java.util.List;

import net.silencily.sailing.basic.sm.domain.TblCmnEntityMember;
import net.silencily.sailing.basic.sm.domain.TblCmnUserMember;
import net.silencily.sailing.common.crud.service.CommonService;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.struts.BaseFormPlus;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;

public class DataMemberForm extends BaseFormPlus {

	// -------------------------属性------------------------//

	private TblCmnEntityMember bean;

	private String userId;

	private String parentId;

	private String searchScope;

	private String updateScope;

	private String createScope;

	private String deleteScope;

	private List list;

	private List member = new ArrayList();

	/**
	 * 获得持久化类的对象
	 * 
	 * @return
	 */
	public TblCmnEntityMember getBean() {
		if (bean == null) {
			if (StringUtils.isBlank(getOid())) {
				bean = new TblCmnEntityMember();
			} else {
				bean = (TblCmnEntityMember) getService().load(
						TblCmnEntityMember.class, getOid());
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

	public void setBean(TblCmnEntityMember bean) {
		this.bean = bean;
	}

	public List getMember() {
		return member;
	}

	public void setMember(List member) {
		this.member = member;
	}

	public TblCmnUserMember getMember(int i) throws IndexOutOfBoundsException {
		while (member.size() <= i) {
			member.add(null);
		}
		TblCmnUserMember detail = (TblCmnUserMember) member.get(i);
		String SId = request.getParameter("member[" + i + "].id");
		if (StringUtils.isNotBlank(SId)) {
			detail = (TblCmnUserMember) getService().load(
					TblCmnUserMember.class, SId);
		}
		if (null == detail) {
			detail = new TblCmnUserMember();
		}
		// 级联保存方式；
		member.set(i, detail);

		return detail;
	}

	public void setMember(TblCmnUserMember exp, int index)
			throws IndexOutOfBoundsException {
		this.member.set(index, exp);
	}

	public String getParentId() {
		if (StringUtils.isBlank(parentId)) {
			parentId = request.getParameter("parentId");
		}
		if (StringUtils.isBlank(parentId)) {
			parentId = "root";
		}
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getCreateScope() {
		return createScope;
	}

	public void setCreateScope(String createScope) {
		this.createScope = createScope;
	}

	public String getDeleteScope() {
		return deleteScope;
	}

	public void setDeleteScope(String deleteScope) {
		this.deleteScope = deleteScope;
	}

	public void setSearchScope(String searchScope) {
		this.searchScope = searchScope;
	}

	public String getUpdateScope() {
		return updateScope;
	}

	public void setUpdateScope(String updateScope) {
		this.updateScope = updateScope;
	}

	public String getSearchScope() {
		// TODO Auto-generated method stub
		return searchScope;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
