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
	// Ŀǰ��Ȩ�޵�ʹ�����������������ݿ�
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
		// // Ŀǰ����ǰ���URL�п��ܲ�һ��,����Ȩ����Ҫ�ظ�����.
		// // ����permissionCode��ֵSecurityReadonly�������,��Ϊֻ��ҳ��
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

		// ���Ѱ�������Ļ��棬��������б�ǩȫ����LABEL��ʽ��ʾ
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
		// �������ݵ�ʱ����Ϊֻ�� ��������б�ǩȫ����LABEL��ʽ��ʾ
		if (pageContext.getAttribute("oid") == null
				|| pageContext.getAttribute("oid").toString().length() == 0) {
			if (2 != CreateAndSaveButtonCtrlCommon.getPageRWCtrlType()) {
				this.rwCtrlType = RWCtrlType.READ_ONLY;
				least = RWCtrlType.READ_ONLY;
			}
		}
		//05/06�޸���������ֻ������£����κ������������ã�������Ϊֻ��
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

		// // ������ʱ�򣬸��ݵ�ǰURL����ȡpageRWCtrlType
		// if (pageContext.getRequest().getParameter("oid") == null
		// || pageContext.getRequest().getParameter("oid").toString()
		// .length() == 0) {
		// CurrentUser currentUser = SecurityContextInfo.getCurrentUser();
		// String url = SecurityContextInfo.getCurrentPageUrl();
		// int pageRWCtrlType = currentUser.getPageDefaultRWCtrlType(url);
		// if (2 != pageRWCtrlType) {
		// // ���Ĭ�ϵ�Ȩ�޲��ǿɱ༭�Ļ���ֱ����ת����Ȩ����ʾҳ��
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
		// ����ҳ
		if (operType == OperType.ADD) {
			visionStatusInfo = addPagePermission();
		}// �鿴ҳ
		else if (operType == OperType.VIEW) {
			visionStatusInfo = inviewPagePermission(hibernateTemplate);
		}// �༭ҳ
		else if (operType == OperType.EDIT) {
			visionStatusInfo = inEditPagePermission(hibernateTemplate);
		}

		return visionStatusInfo;
	}

	// �õ�ҳ��״̬
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
