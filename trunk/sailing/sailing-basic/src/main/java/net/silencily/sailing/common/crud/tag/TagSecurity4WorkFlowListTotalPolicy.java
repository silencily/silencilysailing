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

		// 当 TAG=1时，表示为工作流内部表单,不为1的情况下，开始组装
		if (!("1".equals(tag))) {
			super.securityParameterPopulate(rwCtrlType, permissionCode,
					wfPermissionCode);
		} else {
			// 工作流内部表单
			this.wfPermissionCode = wfPermissionCode;
		}
	}

	/**
	 * 功能说明：获取页面类型
	 */
	public int pageType() {

		// 有来处理没有数据又不能新增的特殊情况
		try {
			if (!BusinessContext.isNull()) {
				if (BusinessContext.getOperType() == OperType.NotAddForNotData) {
					return OperType.VIEW;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 列表没有新增的情况
		// 如果是新增的话，直接返回
		// if (pageContext.getRequest().getParameter("oid") == null
		// || pageContext.getRequest().getParameter("oid").toString()
		// .length() == 0) {
		// return OperType.ADD;
		// }
		if ("1".equals(tag)) {
			// 表示当前的pageType未设定
			// 工作流内部的表单是否只能是可编辑类型
			return OperType.EDIT;
		} else {
			// 工作流外部的表单，采用共通的页面类型设置
			return super.pageType();
		}
	}

	/**
	 * 
	 * 功能描述 编辑页面某个字段项的权限。
	 * 
	 * @param id
	 * @return 2007-12-5 上午10:26:31
	 * @version 1.0
	 * @author wenjb
	 */
	public VisionStatusInfo inEditPagePermission(
			HibernateTemplate hibernateTemplate) {
		// 暂时屏蔽此方法的权限过滤内容
		String tag = null;
		tag = WorkFlowFormContext.getTag();
		// 当TAG是1时，表示为工作流内部表单
		if ("1".equals(tag)) {
			visionStatusInfo.setPageType(ITagSecurityPolicy.EDITPAGE);
			visionStatusInfo.setVisiableStatus(ITagSecurityPolicy.VISIBLE);
			// 得到WorkflowFormContext中的数据；
			String fieldName = this.wfPermissionCode;
			Map permissionMap = WorkFlowFormContext.getPermissionMap();
			String fieldStatus = WorkFlowFormContext.getFieldStatus();
			// 得到字符串“Y”或者没有值
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

	// 有问题，只有在编辑页面时能调用下面的方法
	/**
	 * 功能说明：此方法只对工作流内部表单的权限控制有效 判断当前数据项是否是必须入力的。
	 * key是页面上部分可编辑的情况下，传入的wfpermission属性的值
	 */
	public boolean workFlowIsNeedData(String key) {
		Map permissionIsNeedMap = WorkFlowFormContext.getPermissionIsNeedMap();
		// 当前不是部分可编辑的情况下面，或者没有必须入力设置的时候
		if (null == permissionIsNeedMap || 0 == permissionIsNeedMap.size()) {
			return false;
		}
		// 有必须入力MAP集合存在的时候，如果存在，返回TRUE
		if (permissionIsNeedMap.containsKey(key)) {
			return true;
		} else {
			return false;
		}
	}
}
