package net.silencily.sailing.common.crud.tag;

import net.silencily.sailing.context.CreateAndSaveButtonCtrlCommon;
import net.silencily.sailing.context.OperType;
import net.silencily.sailing.security.model.RWCtrlType;

import org.springframework.orm.hibernate3.HibernateTemplate;

public class TagSecurityGobalPolicy extends TagSecurityDefautPolicy {

	// ����ҳ��״̬:OperType.ADD����ҳ/OperType.EDIT�༭ҳ/OperType.VIEW�鿴ҳ
	public int pageType() {
		if (pageContext.getAttribute("oid") == null
				|| pageContext.getAttribute("oid").toString().length() == 0) {
			// ��ǰ��URL��Ӧ��Ĭ�϶�д����Ȩ��
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

	// �鿴ҳ����
	public VisionStatusInfo inviewPagePermission(
			HibernateTemplate hibernateTemplate) {
		visionStatusInfo.setPageType(ITagSecurityPolicy.VIEWPAGE);
		visionStatusInfo.setEditableStatus(ITagSecurityPolicy.UNEDITABLE);
		// �鿴��ɼ�
		if (least == RWCtrlType.SIGHTLESS) {
			visionStatusInfo.setVisiableStatus(ITagSecurityPolicy.UNVISIBLE);
		} else {
			visionStatusInfo.setVisiableStatus(ITagSecurityPolicy.VISIBLE);
		}
		return visionStatusInfo;
	}

	// �༭ҳ����
	public VisionStatusInfo inEditPagePermission(
			HibernateTemplate hibernateTemplate) {
		visionStatusInfo.setPageType(ITagSecurityPolicy.EDITPAGE);

		if (least == RWCtrlType.SIGHTLESS) {
			// �鿴��ɼ�
			visionStatusInfo.setVisiableStatus(ITagSecurityPolicy.UNVISIBLE);
		} else {
			visionStatusInfo.setVisiableStatus(ITagSecurityPolicy.VISIBLE);
		}
		if (least == RWCtrlType.READ_ONLY) {
			// �鿴��ɱ༭
			visionStatusInfo.setEditableStatus(ITagSecurityPolicy.UNEDITABLE);
		} else {
			// �鿴��ɱ༭
			visionStatusInfo.setEditableStatus(ITagSecurityPolicy.EDITABLE);
		}
		return visionStatusInfo;
	}
}
