package net.silencily.sailing.common.crud.tag;

import net.silencily.sailing.context.BusinessContext;
import net.silencily.sailing.context.OperType;
import net.silencily.sailing.security.model.RWCtrlType;

import org.springframework.orm.hibernate3.HibernateTemplate;


/**
 * 
 * @author wenjb
 * @version 1.0
 */
public class TagSecurity4ListTotalPolicy extends TagSecurityDefautPolicy {

	// ����ҳ��״̬:OperType.ADD����ҳ/OperType.EDIT�༭ҳ/OperType.VIEW�鿴ҳ
	public int pageType() {

		// ��������û�������ֲ����������������
		try {
			if (!BusinessContext.isNull()) {
				if (BusinessContext.getOperType() == OperType.NotAddForNotData) {
					return OperType.VIEW;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//�б�û�����������
		// if (pageContext.getRequest().getParameter("oid") == null
		// || pageContext.getRequest().getParameter("oid").toString()
		// .length() == 0) {
		// return OperType.ADD;
		//		}

		// �Ƿ�������Ȩ��
		// if(this.rwCtrlType != -1){
		// switch (this.rwCtrlType) {
		// case RWCtrlType.EDIT:
		// return OperType.EDIT;
		// case RWCtrlType.READ_ONLY:
		// return OperType.VIEW;
		// default:
		// return OperType.VIEW;
		// }
		// }else{
		// if(!BusinessContext.isNull()){
		// if(BusinessContext.getOperType()==OperType.EDIT){
		// return OperType.EDIT;
		// }else if(BusinessContext.getOperType()==OperType.VIEW){
		// return OperType.VIEW;
		// }else {
		// return OperType.ADD;
		// }
		// }else {
		// return OperType.EDIT;
		// }
		// }

		try {
			if (!BusinessContext.isNull()) {
				if (BusinessContext.getOperType() == OperType.EDIT) {
					return OperType.EDIT;
				} else if (BusinessContext.getOperType() == OperType.VIEW) {
					return OperType.VIEW;
				} else {
					throw new Exception("���������趨����!");
				}
			} else if (this.rwCtrlType != -1) {
				switch (this.rwCtrlType) {
				case RWCtrlType.EDIT:
					return OperType.EDIT;
				case RWCtrlType.READ_ONLY:
					return OperType.VIEW;
				default:
					return OperType.VIEW;
				}
			} else {
				throw new Exception("�޷��жϻ�������!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return OperType.EDIT;
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
