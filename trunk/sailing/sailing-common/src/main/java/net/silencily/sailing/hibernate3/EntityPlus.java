package net.silencily.sailing.hibernate3;

import java.util.HashMap;
import java.util.Map;

import net.silencily.sailing.context.BusinessContext;
import net.silencily.sailing.framework.persistent.Entity;
import net.silencily.sailing.security.SecurityContextInfo;
import net.silencily.sailing.security.model.CurrentUser;
import net.silencily.sailing.security.model.RWCtrlType;

import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * @author zhaoyf
 * 
 */
public abstract class EntityPlus extends Entity {
	
	public static final int isReflectCopyEntity = 1;
	public static final int isNotReflectCopyEntity = 0;

	private Map dict = new HashMap();

	// 增加标签权限属性rwCtrlType 使用当前数据访问权限，得到属性值
	private int rwCtrlType;

	public int getRwCtrlType() {
		// 判断当前页面，采用什么样的权限机制，和主表一致，还是采用从表自身的权限
		String calculateRwCtrlTypeByID = BusinessContext
				.getCalculateRwCtrlTypeByID();
		String key;
		if(null == calculateRwCtrlTypeByID){
			return 2; 
		}
		if ("".equals(calculateRwCtrlTypeByID)) {
			// 采用列表页的权限
			key = BusinessContext.getUserSetedOid();
			try {
				Integer integer = (Integer) (SecurityContextInfo
						.getRwCtrlTypeMap().get(key));
				rwCtrlType = integer.intValue();
			} catch (Exception e) {
				// e.printStackTrace();
				rwCtrlType = 2;
			}
//			if (rwCtrlType == RWCtrlType.EDIT) {
//				if (!getDataAccessLevelU()) {
//					rwCtrlType = RWCtrlType.READ_ONLY;
//				}
//			}
			

		} else {
			// 采用当前的ID去获取值
			key = this.getId();
			try {
				Integer integer = (Integer) (SecurityContextInfo
						.getRwCtrlTypeMap().get(key));
				rwCtrlType = integer.intValue();
			} catch (Exception e) {
				// 重新计算
				CurrentUser currentUser = SecurityContextInfo.getCurrentUser();
				String pageUrl = SecurityContextInfo.getCurrentPageUrl();
				String deptId = this.getCreatorDept().getCode();
				String userId = this.getCreator().getCode();
				rwCtrlType = currentUser.getRowRWCtrlType(pageUrl, deptId,
						userId);
			}
//			if (rwCtrlType == RWCtrlType.EDIT) {
//				if (!getDataAccessLevelU()) {
//					rwCtrlType = RWCtrlType.READ_ONLY;
//				}
//			}
			
		}
		if (rwCtrlType == RWCtrlType.EDIT) {
			if (!getDataAccessLevelU()) {
				rwCtrlType = RWCtrlType.READ_ONLY;
			}
		}
		return rwCtrlType;
	}

	public void setRwCtrlType(int rwCtrlType) {
		this.rwCtrlType = rwCtrlType;
	}

	public Map getDict() {
		return dict;
	}

	public void setDict(Map dict) {
		this.dict = dict;
	}
	
	// 供列表页,不采用VIEWBEAN方式,控制当前用户对记录的操作权限 
	private int rwCtrlTypeForList;

	public int getRwCtrlTypeForList() {
		this.rwCtrlTypeForList = 2;
		try {
			Integer integer = (Integer) (SecurityContextInfo
					.getRwCtrlTypeMap().get(this.getId()));
			rwCtrlTypeForList = integer.intValue();
		} catch (Exception e) {
			// e.printStackTrace();
			rwCtrlTypeForList = 2;
		}
		return rwCtrlTypeForList;
	}

	public void setRwCtrlTypeForList(int rwCtrlTypeForList) {
		this.rwCtrlTypeForList = rwCtrlTypeForList;
	}
	
	//业务实体历史记录保存
	private int reflectCopyEntityProperties = isNotReflectCopyEntity;

	public int getReflectCopyEntityProperties() {
		return reflectCopyEntityProperties;
	}

	public void setReflectCopyEntityProperties(int reflectCopyEntityProperties) {
		this.reflectCopyEntityProperties = reflectCopyEntityProperties;
	}

	//权限:增 删 改 查
	private boolean dataAccessLevelC = true;
	private boolean dataAccessLevelD = true;
	private boolean dataAccessLevelU = true;
	private boolean dataAccessLevelS = true;

	public boolean getDataAccessLevelC() {
		return dataAccessLevelC;
	}

	public void setDataAccessLevelC(boolean dataAccessLevelC) {
		this.dataAccessLevelC = dataAccessLevelC;
	}

	public boolean getDataAccessLevelD() {
		return dataAccessLevelD;
	}

	public void setDataAccessLevelD(boolean dataAccessLevelD) {
		this.dataAccessLevelD = dataAccessLevelD;
	}

	public boolean getDataAccessLevelU() {
		//dataAccessLevelU = getHibernateTemplate().getDataAccessLevel(this, 'U');
		return dataAccessLevelU;
	}

	public void setDataAccessLevelU(boolean dataAccessLevelU) {
		this.dataAccessLevelU = dataAccessLevelU;
	}

	public boolean getDataAccessLevelS() {
		return dataAccessLevelS;
	}

	public void setDataAccessLevelS(boolean dataAccessLevelS) {
		this.dataAccessLevelS = dataAccessLevelS;
	}
	

//	private EnhancehibernateTemplatePlus getHibernateTemplate() {
//		HibernateTemplate hibernateTemplate = (HibernateTemplate)ServiceProvider.getService("common.hibernateTemplate");
//		if (hibernateTemplate instanceof EnhancehibernateTemplatePlus) {
//			return (EnhancehibernateTemplatePlus)hibernateTemplate;
//		} else {
//			System.err.print("没找到hibernateTemplate");
//			return new EnhancehibernateTemplatePlus();
//		}
//	}

}
