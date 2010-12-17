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
 * 组织机构维护<code>form</code>, 注意其中的{@link #getBean()}是核心所在, 利用这个模式
 * 我们可以极大地简化从一个请求组装<code>domain object</code>的过程
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

	/* 原状态 */
	private String beginstate;

	/* 新状态 */
	private String endstate;

	/* 原部门 */
	private String beginPDN;

	/* 原部门id */
	private String beginPDI;

	/* 新部门 */
	private String endPDN;

	private String departmentnameNew;	
	
	private SysCodeList scl = SysCodeList.factory();
	
	/* 显示部门选择对话框的树类型 */
	private String treeType;
	
	/**
	 * 获得{@link THrDept}的实例, 以便<code>web tier framework</code>可以开始
	 * 组装来自页面的参数, 页面仅仅处理需要修改的参数而不是把所有的属性都体现在页面的<code>hidden field</code> 中,
	 * 这是<code>java</code>社区最普通流行的一种方案, 当然还有其它的方案, 但是稍复杂一些,
	 * 所以我们仅仅采用这种粗粒度但简单有效的办法
	 * 
	 * @return {@link THrDept}的实例
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
	 * 获得{@link TblCmnDeptStatusRec}的实例, 以便<code>web tier framework</code>可以开始
	 * 组装来自页面的参数, 页面仅仅处理需要修改的参数而不是把所有的属性都体现在页面的<code>hidden field</code> 中,
	 * 这是<code>java</code>社区最普通流行的一种方案, 当然还有其它的方案, 但是稍复杂一些,
	 * 所以我们仅仅采用这种粗粒度但简单有效的办法
	 * 
	 * @return {@link TblCmnDeptStatusRec}的实例
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
	 * 在调用这个方法时, <code>web tier framework</code>可能还没有开始组装{@link #parentCode}
	 * 这个属性, 也就是说从{@link #getParentCode()}不能返回请求参数"parentCode"的值, 我们主动从
	 * <code>request</code>中获取这个值, 以便方法{@link #getBean()}可以正确执行
	 * </p>
	 * <p>
	 * 尽管不是每一个功能都需要这个参数, 但是统一地处理可以简化编程
	 * </p>
	 * 
	 * @return 请求参数<code>parentCode</code>的值
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
