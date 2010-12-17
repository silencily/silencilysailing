package net.silencily.sailing.basic.sm.dept.web;

import java.util.List;

import net.silencily.sailing.basic.sm.dept.service.TblSmDeptService;
import net.silencily.sailing.basic.sm.domain.TblCmnDept;
import net.silencily.sailing.basic.sm.domain.TblCmnDeptPareRec;
import net.silencily.sailing.common.crud.service.CommonService;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.struts.BaseFormPlus;

import org.apache.commons.lang.StringUtils;


/**
 * ��֯����ά��<code>form</code>, ע�����е�{@link #getBean()}�Ǻ�������, �������ģʽ
 * ���ǿ��Լ���ؼ򻯴�һ��������װ<code>domain object</code>�Ĺ��� �ϼ����ű��(DeptPareRecForm)
 * @author gaojing
 * @version $Id: TblSmDeptPareRecForm.java,v 1.1 2010/12/10 10:56:44 silencily Exp $
 * @since 2007-8-29
 */
public class TblSmDeptPareRecForm extends BaseFormPlus {

	private TblCmnDeptPareRec pareBean;

	private TblCmnDept bean;

	private String parentCode;

	/* (ԭ�ϼ�����id,name) */
	private String beginPDI;

	private String beginPDN;

	/* (���ϼ�����id,name) */
	private String endPDI;

	private String endPDN;

	private String departmentnameNew;

	private List list;

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	public String getDepartmentnameNew() {
		return departmentnameNew;
	}

	public void setDepartmentnameNew(String departmentnameNew) {
		this.departmentnameNew = departmentnameNew;
	}

	/**
	 * ���{@link THrDept}��ʵ��, �Ա�<code>web tier framework</code>���Կ�ʼ
	 * ��װ����ҳ��Ĳ���, ҳ�����������Ҫ�޸ĵĲ��������ǰ����е����Զ�������ҳ���<code>hidden field</code> ��,
	 * ����<code>java</code>��������ͨ���е�һ�ַ���, ��Ȼ���������ķ���, �����Ը���һЩ,
	 * �������ǽ����������ִ����ȵ�����Ч�İ취
	 * 
	 * @return {@link THrDept}��ʵ��
	 */
	public TblCmnDept getBean() {
		if (bean == null) {
			if (StringUtils.isBlank(getOid())) {
//				bean = TblCmnDeptStatusRecAction.serviceDept().newInstance(getParentCode());
				bean = service().newInstance(getParentCode());
			}
			else {
//				bean = TblCmnDeptStatusRecAction.serviceDept().load(getOid());
				bean = (TblCmnDept)getService().load(TblCmnDept.class, getOid());
			}
		}
		return bean;
	}

	public void setBean(TblCmnDept bean) {
		this.bean = bean;
	}

	/**
	 * ���{@link THrDeptStatusRec}��ʵ��, �Ա�<code>web tier framework</code>���Կ�ʼ
	 * ��װ����ҳ��Ĳ���, ҳ�����������Ҫ�޸ĵĲ��������ǰ����е����Զ�������ҳ���<code>hidden field</code> ��,
	 * ����<code>java</code>��������ͨ���е�һ�ַ���, ��Ȼ���������ķ���, �����Ը���һЩ,
	 * �������ǽ����������ִ����ȵ�����Ч�İ취
	 * 
	 * @return {@link THrDeptStatusRec}��ʵ��
	 */
	public TblCmnDeptPareRec getPareBean() {
		if (pareBean == null) {
			pareBean = new TblCmnDeptPareRec();
			pareBean.setTblCmnDept(getBean());
		}

		return pareBean;
	}

	public void setPareBean(TblCmnDeptPareRec pareBean) {
		this.pareBean = pareBean;
	}

	/**
	 * <p>
	 * �ڵ����������ʱ, <code>web tier framework</code>���ܻ�û�п�ʼ��װ{@link #parentCode}
	 * �������, Ҳ����˵��{@link #getParentCode()}���ܷ����������"parentCode"��ֵ, ����������
	 * <code>request</code>�л�ȡ���ֵ, �Ա㷽��{@link #getBean()}������ȷִ��
	 * </p>
	 * <p>
	 * ���ܲ���ÿһ�����ܶ���Ҫ�������, ����ͳһ�ش�����Լ򻯱��
	 * </p>
	 * 
	 * @return �������<code>parentCode</code>��ֵ
	 */
	public String getParentCode() {
		if (StringUtils.isBlank(parentCode)) {
			parentCode = request.getParameter("parentCode");
		}
		//20071229 CHRZZJG00006 START
//		if (parentCode == null) {
//			throw new IllegalArgumentException("�κ���������ṩparentId����");
//		}
	  if (StringUtils.isBlank(parentCode)) {
		   parentCode = TblCmnDept.ROOT_NODE_CODE;
	  }
	  //20071229 CHRZZJG00006 END
		return parentCode;
	}

	public void setParentCode(String parentId) {
		this.parentCode = parentId;
	}

	public String getBeginPDI() {
		return beginPDI;
	}

	public void setBeginPDI(String beginPDI) {
		this.beginPDI = beginPDI;
	}

	public String getBeginPDN() {
		return beginPDN;
	}

	public void setBeginPDN(String beginPDN) {
		this.beginPDN = beginPDN;
	}

	public String getEndPDI() {
		return endPDI;
	}

	public void setEndPDI(String endPDI) {
		this.endPDI = endPDI;
	}

	public String getEndPDN() {
		return endPDN;
	}

	public void setEndPDN(String endPDN) {
		this.endPDN = endPDN;
	}
	/**
	 * ��ȡ��ͨ����
	 * @return
	 */
	private CommonService getService() {
		return (CommonService)ServiceProvider.getService(CommonService.SERVICE_NAME);
	}
	/**
	 * ��ȡTblCmnDept����
	 * @return
	 */
	private TblSmDeptService service() {
		return (TblSmDeptService) ServiceProvider.getService(TblSmDeptService.SERVICE_NAME);
	}
}
