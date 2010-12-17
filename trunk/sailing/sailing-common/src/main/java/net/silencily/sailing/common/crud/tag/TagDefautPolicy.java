package net.silencily.sailing.common.crud.tag;

import javax.servlet.jsp.PageContext;

import net.silencily.sailing.context.CreateAndSaveButtonCtrlCommon;
import net.silencily.sailing.context.OperType;

import org.springframework.orm.hibernate3.HibernateTemplate;


public class TagDefautPolicy extends TagSecurityDefautPolicy {


	
	public void securityParameterPopulate(String rwCtrlType,String permissionCode){

	}
	
	
	//����ҳ��״̬:OperType.ADD����ҳ/OperType.EDIT�༭ҳ/OperType.VIEW�鿴ҳ
	public int pageType(){
		if (pageContext.getRequest().getParameter("oid") == null 
				|| pageContext.getRequest().getParameter("oid").toString().length()==0){
			//��ǰ��URL��Ӧ��Ĭ�϶�д����Ȩ��
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
	
	
	//�鿴ҳ����
	public VisionStatusInfo inviewPagePermission(HibernateTemplate hibernateTemplate){
		visionStatusInfo.setPageType(ITagSecurityPolicy.VIEWPAGE);
		visionStatusInfo.setVisiableStatus(ITagSecurityPolicy.VISIBLE);
		visionStatusInfo.setEditableStatus(ITagSecurityPolicy.EDITABLE);
		return visionStatusInfo;
	}
	//�༭ҳ����
	public VisionStatusInfo inEditPagePermission(HibernateTemplate hibernateTemplate){
		visionStatusInfo.setPageType(ITagSecurityPolicy.EDITPAGE);
		visionStatusInfo.setVisiableStatus(ITagSecurityPolicy.VISIBLE);
		visionStatusInfo.setEditableStatus(ITagSecurityPolicy.EDITABLE);
		return visionStatusInfo;
	}
}
