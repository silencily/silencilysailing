package net.silencily.sailing.common.crud.tag;

import javax.servlet.jsp.PageContext;

import net.silencily.sailing.context.CreateAndSaveButtonCtrlCommon;
import net.silencily.sailing.context.OperType;

import org.springframework.orm.hibernate3.HibernateTemplate;


public class TagDefautPolicy extends TagSecurityDefautPolicy {


	
	public void securityParameterPopulate(String rwCtrlType,String permissionCode){

	}
	
	
	//设置页面状态:OperType.ADD新增页/OperType.EDIT编辑页/OperType.VIEW查看页
	public int pageType(){
		if (pageContext.getRequest().getParameter("oid") == null 
				|| pageContext.getRequest().getParameter("oid").toString().length()==0){
			//当前的URL对应的默认读写控制权限
			if(2==CreateAndSaveButtonCtrlCommon.getPageRWCtrlType()){
				return OperType.ADD;
			}else{
				return OperType.EDIT; 
			}
		}
		else {
			return OperType.EDIT;
		}
	}
	

	public VisionStatusInfo compomentPermission(String rwCtrlType,
			String permissionCode, String wfPermissionCode,
			PageContext pageContext) {
		this.pageContext = pageContext;
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
	
	
	//查看页设置
	public VisionStatusInfo inviewPagePermission(HibernateTemplate hibernateTemplate){
		visionStatusInfo.setPageType(ITagSecurityPolicy.VIEWPAGE);
		visionStatusInfo.setVisiableStatus(ITagSecurityPolicy.VISIBLE);
		visionStatusInfo.setEditableStatus(ITagSecurityPolicy.EDITABLE);
		return visionStatusInfo;
	}
	//编辑页设置
	public VisionStatusInfo inEditPagePermission(HibernateTemplate hibernateTemplate){
		visionStatusInfo.setPageType(ITagSecurityPolicy.EDITPAGE);
		visionStatusInfo.setVisiableStatus(ITagSecurityPolicy.VISIBLE);
		visionStatusInfo.setEditableStatus(ITagSecurityPolicy.EDITABLE);
		return visionStatusInfo;
	}
}
