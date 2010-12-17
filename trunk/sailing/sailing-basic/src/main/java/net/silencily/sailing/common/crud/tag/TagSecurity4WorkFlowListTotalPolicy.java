package net.silencily.sailing.common.crud.tag;

import java.util.Map;

import net.silencily.sailing.common.context.WorkFlowFormContext;
import net.silencily.sailing.context.BusinessContext;
import net.silencily.sailing.context.OperType;

import org.springframework.orm.hibernate3.HibernateTemplate;


public class TagSecurity4WorkFlowListTotalPolicy extends
		TagSecurity4ListTotalPolicy {

	String tag = null;

	public void securityParameterPopulate(String rwCtrlType,
			String permissionCode, String wfPermissionCode) {
		try {
			tag = WorkFlowFormContext.getTag();
		} catch (Exception e) {
			tag = null;
		}

		// �� TAG=1ʱ����ʾΪ�������ڲ���,��Ϊ1������£���ʼ��װ
		if (!("1".equals(tag))) {
			super.securityParameterPopulate(rwCtrlType, permissionCode,
					wfPermissionCode);
		} else {
			// �������ڲ���
			this.wfPermissionCode = wfPermissionCode;
		}
	}

	/**
	 * ����˵������ȡҳ������
	 */
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
		// �б�û�����������
		// ����������Ļ���ֱ�ӷ���
		// if (pageContext.getRequest().getParameter("oid") == null
		// || pageContext.getRequest().getParameter("oid").toString()
		// .length() == 0) {
		// return OperType.ADD;
		// }
		if ("1".equals(tag)) {
			// ��ʾ��ǰ��pageTypeδ�趨
			// �������ڲ��ı��Ƿ�ֻ���ǿɱ༭����
			return OperType.EDIT;
		} else {
			// �������ⲿ�ı������ù�ͨ��ҳ����������
			return super.pageType();
		}
	}

	/**
	 * 
	 * �������� �༭ҳ��ĳ���ֶ����Ȩ�ޡ�
	 * 
	 * @param id
	 * @return 2007-12-5 ����10:26:31
	 * @version 1.0
	 * @author wenjb
	 */
	public VisionStatusInfo inEditPagePermission(
			HibernateTemplate hibernateTemplate) {
		// ��ʱ���δ˷�����Ȩ�޹�������
		String tag = null;
		tag = WorkFlowFormContext.getTag();
		// ��TAG��1ʱ����ʾΪ�������ڲ���
		if ("1".equals(tag)) {
			visionStatusInfo.setPageType(ITagSecurityPolicy.EDITPAGE);
			visionStatusInfo.setVisiableStatus(ITagSecurityPolicy.VISIBLE);
			// �õ�WorkflowFormContext�е����ݣ�
			String fieldName = this.wfPermissionCode;
			Map permissionMap = WorkFlowFormContext.getPermissionMap();
			String fieldStatus = WorkFlowFormContext.getFieldStatus();
			// �õ��ַ�����Y������û��ֵ
			String editableStatus = new String("");
			switch (Integer.parseInt(fieldStatus)) {
			case 1:
				editableStatus = "Y";
				break;
			case 2:
				editableStatus = "N";
				break;
			case 3:
				if ("".equals(fieldName) || null == fieldName) {
					editableStatus = "N";
				} else {
					editableStatus = (String) permissionMap.get(fieldName);
				}
				break;
			default:
				editableStatus = "N";
				break;

			}
			if ("Y".equals(editableStatus)) {
				visionStatusInfo.setEditableStatus(ITagSecurityPolicy.EDITABLE);
			} else {
				visionStatusInfo
						.setEditableStatus(ITagSecurityPolicy.UNEDITABLE);
			}
			return visionStatusInfo;
		} else {
			super.inEditPagePermission(hibernateTemplate);
			return visionStatusInfo;
		}
	}

	// �����⣬ֻ���ڱ༭ҳ��ʱ�ܵ�������ķ���
	/**
	 * ����˵�����˷���ֻ�Թ������ڲ�����Ȩ�޿�����Ч �жϵ�ǰ�������Ƿ��Ǳ��������ġ�
	 * key��ҳ���ϲ��ֿɱ༭������£������wfpermission���Ե�ֵ
	 */
	public boolean workFlowIsNeedData(String key) {
		Map permissionIsNeedMap = WorkFlowFormContext.getPermissionIsNeedMap();
		// ��ǰ���ǲ��ֿɱ༭��������棬����û�б����������õ�ʱ��
		if (null == permissionIsNeedMap || 0 == permissionIsNeedMap.size()) {
			return false;
		}
		// �б�������MAP���ϴ��ڵ�ʱ��������ڣ�����TRUE
		if (permissionIsNeedMap.containsKey(key)) {
			return true;
		} else {
			return false;
		}
	}
}
