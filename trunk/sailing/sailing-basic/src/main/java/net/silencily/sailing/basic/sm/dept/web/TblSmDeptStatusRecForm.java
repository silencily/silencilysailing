package net.silencily.sailing.basic.sm.dept.web;

import java.util.List;

import net.silencily.sailing.basic.sm.dept.service.TblSmDeptService;
import net.silencily.sailing.basic.sm.domain.TblCmnDept;
import net.silencily.sailing.basic.sm.domain.TblCmnDeptStatusRec;
import net.silencily.sailing.common.crud.service.CommonService;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.struts.BaseFormPlus;

import org.apache.commons.lang.StringUtils;


/**
 * ��֯����ά��<code>form</code>, ע�����е�{@link #getBean()}�Ǻ�������, �������ģʽ
 * ���ǿ��Լ���ؼ򻯴�һ��������װ<code>domain object</code>�Ĺ��� �ϼ����ű��(DeptPareRecForm)
 * @author gaojing
 * @version $Id: TblSmDeptStatusRecForm.java,v 1.1 2010/12/10 10:56:44 silencily Exp $
 * @since 2007-8-29
 */
public class TblSmDeptStatusRecForm extends BaseFormPlus {

	private TblCmnDeptStatusRec statusBean;

	private List list;

	private TblCmnDept bean;

	private String parentCode;

	private String beginstate;
	
	public String getBeginstate() {
		
		return beginstate;
	}
	
	public void setBeginstate(String beginstate) {
		
		this.beginstate = beginstate;
	}

	/**
	 * ���{@link THrDept}��ʵ��, �Ա�<code>web tier framework</code>���Կ�ʼ
	 * ��װ����ҳ��Ĳ���, ҳ�����������Ҫ�޸ĵĲ��������ǰ����е����Զ�������ҳ���<code>hidden field</code> ��,
	 * ����<code>java</code>��������ͨ���е�һ�ַ���, ��Ȼ���������ķ���, �����Ը���һЩ,
	 * �������ǽ����������ִ����ȵ�����Ч�İ취
	 * 
	 * @return {@link THrDept}��ʵ��
	 */
	
	//�õ��Ļ��ǲ��ŵ�bean����ΪserviceDept()���ǲ��ŵ�service()
	
	public TblCmnDept getBean() {
		
		if (bean == null) {
			
			if (StringUtils.isBlank(getOid())) {
				
				bean = service().newInstance(getParentCode());
			}
			else {
				
				bean = (TblCmnDept)getService().load(TblCmnDept.class, getOid());
			}
		}return bean;
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
	
	public TblCmnDeptStatusRec getStatusBean() {
		
		if (statusBean == null) {
			
			statusBean = new TblCmnDeptStatusRec();
			
			statusBean.setTblCmnDept(getBean());
		}
		return statusBean;
	}
	
	public void setStatusBean(TblCmnDeptStatusRec statusBean) {
		
		this.statusBean = statusBean;
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
//			
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
	
	public List getList() {
		
		return list;
	}
	
	public void setList(List list) {
		
		this.list = list;
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