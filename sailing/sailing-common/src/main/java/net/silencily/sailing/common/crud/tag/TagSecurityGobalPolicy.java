package net.silencily.sailing.common.crud.tag;

import net.silencily.sailing.context.CreateAndSaveButtonCtrlCommon;
import net.silencily.sailing.context.OperType;
import net.silencily.sailing.security.model.RWCtrlType;

import org.springframework.orm.hibernate3.HibernateTemplate;

public class TagSecurityGobalPolicy extends TagSecurityDefautPolicy {

	// 设置页面状态:OperType.ADD新增页/OperType.EDIT编辑页/OperType.VIEW查看页
	public int pageType() {
		if (pageContext.getAttribute("oid") == null
				|| pageContext.getAttribute("oid").toString().length() == 0) {
			// 当前的URL对应的默认读写控制权限
			if (2 == CreateAndSaveButtonCtrlCommon.getPageRWCtrlType()) {
				return OperType.ADD;
			}
		}
		switch (this.rwCtrlType) {
		case RWCtrlType.EDIT:
			return OperType.EDIT;
		case RWCtrlType.READ_ONLY:
			return OperType.VIEW;
		default:
			return OperType.VIEW;
		}
	}

	// 查看页设置
	public VisionStatusInfo inviewPagePermission(
			HibernateTemplate hibernateTemplate) {
		visionStatusInfo.setPageType(ITagSecurityPolicy.VIEWPAGE);
		visionStatusInfo.setEditableStatus(ITagSecurityPolicy.UNEDITABLE);
		// 查看项不可见
		if (least == RWCtrlType.SIGHTLESS) {
			visionStatusInfo.setVisiableStatus(ITagSecurityPolicy.UNVISIBLE);
		} else {
			visionStatusInfo.setVisiableStatus(ITagSecurityPolicy.VISIBLE);
		}
		return visionStatusInfo;
	}

	// 编辑页设置
	public VisionStatusInfo inEditPagePermission(
			HibernateTemplate hibernateTemplate) {
		visionStatusInfo.setPageType(ITagSecurityPolicy.EDITPAGE);

		if (least == RWCtrlType.SIGHTLESS) {
			// 查看项不可见
			visionStatusInfo.setVisiableStatus(ITagSecurityPolicy.UNVISIBLE);
		} else {
			visionStatusInfo.setVisiableStatus(ITagSecurityPolicy.VISIBLE);
		}
		if (least == RWCtrlType.READ_ONLY) {
			// 查看项不可编辑
			visionStatusInfo.setEditableStatus(ITagSecurityPolicy.UNEDITABLE);
		} else {
			// 查看项可编辑
			visionStatusInfo.setEditableStatus(ITagSecurityPolicy.EDITABLE);
		}
		return visionStatusInfo;
	}
}
