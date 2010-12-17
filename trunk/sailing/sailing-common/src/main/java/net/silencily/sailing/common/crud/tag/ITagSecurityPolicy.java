package net.silencily.sailing.common.crud.tag;

import javax.servlet.jsp.PageContext;
import org.springframework.orm.hibernate3.HibernateTemplate;

public interface ITagSecurityPolicy {

	public final static int UNVISIBLE = 1;
	public final static int VISIBLE = 2;
	public final static int UNEDITABLE = 3;
	public final static int EDITABLE = 4;
	public final static int VIEWPAGE = 5;
	public final static int EDITPAGE = 6;
	
	public void securityParameterPopulate(String rwCtrlType,String permissionCode,String wfPermissionCode);
	public VisionStatusInfo compomentPermission(String rwCtrlType,String permissionCode,String wfPermissionCode,PageContext pageContext);
	public int pageType();
	public VisionStatusInfo addPagePermission();
	public boolean workFlowIsNeedData(String key);
	public VisionStatusInfo inEditPagePermission(HibernateTemplate hibernateTemplate);
	public VisionStatusInfo inviewPagePermission(HibernateTemplate hibernateTemplate);
}
