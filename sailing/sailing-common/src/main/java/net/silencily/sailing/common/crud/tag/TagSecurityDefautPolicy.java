package net.silencily.sailing.common.crud.tag;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import net.silencily.sailing.context.CreateAndSaveButtonCtrlCommon;
import net.silencily.sailing.context.OperType;
import net.silencily.sailing.security.SecurityContextInfo;
import net.silencily.sailing.security.model.CurrentUser;
import net.silencily.sailing.security.model.RWCtrlType;

import org.apache.commons.lang.StringUtils;
import org.springframework.orm.hibernate3.HibernateTemplate;


public abstract class TagSecurityDefautPolicy implements ITagSecurityPolicy {

	PageContext pageContext = null;

	VisionStatusInfo visionStatusInfo = new VisionStatusInfo();

	// HibernateTemplate hibernateTemplate = (HibernateTemplate) ServiceProvider
	// .getService("common.hibernateTemplate");
	// 目前，权限的使用无须自行连接数据库
	HibernateTemplate hibernateTemplate = null;

	int rwCtrlType = -1;

	String permissionCode = "";

	String wfPermissionCode = "";

	int field;

	int least;

	public void securityParameterPopulate(String rwCtrlType,
			String permissionCode, String wfPermissionCode) {
		CurrentUser currentUser = SecurityContextInfo.getCurrentUser();
		if (StringUtils.isNotBlank(rwCtrlType)) {
			this.rwCtrlType = Integer.parseInt(rwCtrlType);
		}
		this.permissionCode = permissionCode;
		this.wfPermissionCode = wfPermissionCode;
		// if (StringUtils.isNotBlank(permissionCode)) {
		// // 目前保存前后的URL有可能不一致,导致权限需要重复配置.
		// // 流外permissionCode的值SecurityReadonly如果存在,就为只读页面
		// if ("SecurityReadonly".equals(permissionCode)) {
		// field = RWCtrlType.READ_ONLY;
		// } else {
		// field = currentUser.getFieldRWCtrlType(SecurityContextInfo
		// .getCurrentPageUrl(), permissionCode);
		// }
		// least = this.rwCtrlType > field ? field : this.rwCtrlType;
		// } else {
		//			
		// least = this.rwCtrlType;
		// }

		if ("SecurityReadonly".equals(permissionCode)) {
			field = RWCtrlType.READ_ONLY;
		} else {
			field = currentUser.getFieldRWCtrlType(SecurityContextInfo
					.getCurrentPageUrl(), permissionCode);
		}
		least = this.rwCtrlType > field ? field : this.rwCtrlType;

		// 从已办任务进的画面，画面的所有标签全部以LABEL形式显示
		String urlKey = "";
		try {
			HttpSession session = SecurityContextInfo.getSession();
			urlKey = (String) session.getAttribute("urlKey");
		} catch (Exception e) {
			urlKey = "";
		}
		if ("alreadyList".equals(urlKey) || "passroundList".equals(urlKey)
				|| "system".equals(urlKey)) {
			this.rwCtrlType = RWCtrlType.READ_ONLY;
			least = RWCtrlType.READ_ONLY;
		}
		// 当无数据的时候，又为只度 画面的所有标签全部以LABEL形式显示
		if (pageContext.getAttribute("oid") == null
				|| pageContext.getAttribute("oid").toString().length() == 0) {
			if (2 != CreateAndSaveButtonCtrlCommon.getPageRWCtrlType()) {
				this.rwCtrlType = RWCtrlType.READ_ONLY;
				least = RWCtrlType.READ_ONLY;
			}
		}
		//05/06修改需求，增加只读情况下，屏蔽后续的所有设置，数据项为只读
		int pageRWCtrlType = 2;
		try {
			String url = SecurityContextInfo.getCurrentPageUrl();
			pageRWCtrlType = currentUser.getPageDefaultRWCtrlType(url);
			if(1 == pageRWCtrlType){
				this.rwCtrlType = RWCtrlType.READ_ONLY;
				least = RWCtrlType.READ_ONLY;
			}
		} catch (Exception e) {
			pageRWCtrlType = 2;
		}
	}

	public VisionStatusInfo compomentPermission(String rwCtrlType,
			String permissionCode, String wfPermissionCode,
			PageContext pageContext) {
		this.pageContext = pageContext;

		// // 新增的时候，根据当前URL，获取pageRWCtrlType
		// if (pageContext.getRequest().getParameter("oid") == null
		// || pageContext.getRequest().getParameter("oid").toString()
		// .length() == 0) {
		// CurrentUser currentUser = SecurityContextInfo.getCurrentUser();
		// String url = SecurityContextInfo.getCurrentPageUrl();
		// int pageRWCtrlType = currentUser.getPageDefaultRWCtrlType(url);
		// if (2 != pageRWCtrlType) {
		// // 如果默认的权限不是可编辑的话，直接跳转到无权限提示页面
		// try {
		// String qware = pageContext.getServletContext()
		// .getInitParameter("publicResourceServer");
		// String url2 = qware + "/405.jsp";
		// ServletResponse srp = pageContext.getResponse();
		// HttpServletResponse hsp = (HttpServletResponse) srp;
		// hsp.sendRedirect(url2);
		// } catch (IllegalStateException e) {
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }
		// }
		securityParameterPopulate(rwCtrlType, permissionCode, wfPermissionCode);
		int operType = pageType();
		// 新增页
		if (operType == OperType.ADD) {
			visionStatusInfo = addPagePermission();
		}// 查看页
		else if (operType == OperType.VIEW) {
			visionStatusInfo = inviewPagePermission(hibernateTemplate);
		}// 编辑页
		else if (operType == OperType.EDIT) {
			visionStatusInfo = inEditPagePermission(hibernateTemplate);
		}

		return visionStatusInfo;
	}

	// 得到页面状态
	public abstract int pageType();

	public VisionStatusInfo addPagePermission() {
		visionStatusInfo.setPageType(ITagSecurityPolicy.EDITPAGE);
		visionStatusInfo.setVisiableStatus(ITagSecurityPolicy.VISIBLE);
		visionStatusInfo.setEditableStatus(ITagSecurityPolicy.EDITABLE);
		return visionStatusInfo;
	}

	public boolean workFlowIsNeedData(String key) {
		return false;
	}

	public abstract VisionStatusInfo inviewPagePermission(
			HibernateTemplate hibernateTemplate);

	public abstract VisionStatusInfo inEditPagePermission(
			HibernateTemplate hibernateTemplate);
}
