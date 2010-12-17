package net.silencily.sailing.basic.sm.dept.web;

import java.util.List;

import net.silencily.sailing.basic.sm.domain.TblCmnDept;
import net.silencily.sailing.basic.sm.domain.TblCmnDeptStatusRec;
import net.silencily.sailing.code.SysCodeList;
import net.silencily.sailing.common.crud.service.CommonService;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.struts.BaseFormPlus;

import org.apache.commons.lang.StringUtils;

/**
 * ��֯����ά��<code>form</code>, ע�����е�{@link #getBean()}�Ǻ�������, �������ģʽ
 * ���ǿ��Լ���ؼ򻯴�һ��������װ<code>domain object</code>�Ĺ���
 * 
 * @author gaojing
 * @version $Id: TblSmDeptForm.java,v 1.1 2010/12/10 10:56:44 silencily Exp $
 * @since 2007-8-29
 */
public class TblSmDeptForm extends BaseFormPlus {
	
	private List list;

	private TblCmnDept bean;

	private TblCmnDeptStatusRec statusBean;

	private String parentCode;

	private String root;

	/* ԭ״̬ */
	private String beginstate;

	/* ��״̬ */
	private String endstate;

	/* ԭ���� */
	private String beginPDN;

	/* ԭ����id */
	private String beginPDI;

	/* �²��� */
	private String endPDN;

	private String departmentnameNew;	
	
	private SysCodeList scl = SysCodeList.factory();
	
	/* ��ʾ����ѡ��Ի���������� */
	private String treeType;
	
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
				bean = TblSmDeptAction.service().newInstance(getParentCode());
			}
			else {
//				bean = TblCmnDeptAction.service().load(getOid());
				bean = (TblCmnDept)getService().load(TblCmnDept.class, getOid());
			}
		}
		return bean;
	}

	public void setBean(TblCmnDept bean) {
		this.bean = bean;
	}

	/**
	 * ���{@link TblCmnDeptStatusRec}��ʵ��, �Ա�<code>web tier framework</code>���Կ�ʼ
	 * ��װ����ҳ��Ĳ���, ҳ�����������Ҫ�޸ĵĲ��������ǰ����е����Զ�������ҳ���<code>hidden field</code> ��,
	 * ����<code>java</code>��������ͨ���е�һ�ַ���, ��Ȼ���������ķ���, �����Ը���һЩ,
	 * �������ǽ����������ִ����ȵ�����Ч�İ취
	 * 
	 * @return {@link TblCmnDeptStatusRec}��ʵ��
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
		//20071212 PHRJGBM00001 START
		if (parentCode == null || "".equals(parentCode)) {
			parentCode = request.getParameter("parentCode");
		}
		if (parentCode == null || "".equals(parentCode)) {
			parentCode = TblCmnDept.ROOT_NODE_CODE;
		}
		//20071212 PHRJGBM00001 END
		return parentCode;
	}

	public void setParentCode(String parentId) {
		this.parentCode = parentId;
	}

	public TblCmnDept getRootNode() {
		return bean;
	}

	public void setRootNode(TblCmnDept bean) {
		this.bean = bean;
	}

	public String getRoot() {
		return root;
	}

	public void setRoot(String root) {
		this.root = root;
	}

	public String getBeginstate() {
		return beginstate;
	}

	public void setBeginstate(String beginstate) {
		this.beginstate = beginstate;
	}

	public String getEndstate() {
		return endstate;
	}

	public void setEndstate(String endstate) {
		this.endstate = endstate;
	}

	public String getBeginPDN() {
		return beginPDN;
	}

	public void setBeginPDN(String beginPDN) {
		this.beginPDN = beginPDN;
	}

	public String getEndPDN() {
		return endPDN;
	}

	public void setEndPDN(String endPDN) {
		this.endPDN = endPDN;
	}

	public String getDepartmentnameNew() {
		return departmentnameNew;
	}

	public void setDepartmentnameNew(String departmentnameNew) {
		this.departmentnameNew = departmentnameNew;
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	public String getBeginPDI() {
		return beginPDI;
	}

	public void setBeginPDI(String beginPDI) {
		this.beginPDI = beginPDI;
	}

	/**
	 * @return the scl
	 */
	public SysCodeList getScl() {
		return scl;
	}

	/**
	 * @param scl the scl to set
	 */
	public void setScl(SysCodeList scl) {
		this.scl = scl;
	}
	
	private CommonService getService() {
		return (CommonService)ServiceProvider.getService(CommonService.SERVICE_NAME);
	}

	public String getTreeType() {
		return treeType;
	}

	public void setTreeType(String treeType) {
		this.treeType = treeType;
	}
}
