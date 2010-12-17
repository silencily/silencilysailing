package net.silencily.sailing.basic.sm.user.web;

import java.util.ArrayList;
import java.util.List;

import net.silencily.sailing.basic.sm.domain.TblCmnDept;
import net.silencily.sailing.basic.sm.domain.TblCmnMsgConfig;
import net.silencily.sailing.basic.sm.domain.TblCmnPermission;
import net.silencily.sailing.basic.sm.domain.TblCmnUser;
import net.silencily.sailing.basic.sm.domain.TblCmnUserMember;
import net.silencily.sailing.basic.sm.domain.TblCmnUserPermission;
import net.silencily.sailing.code.SysCodeList;
import net.silencily.sailing.common.crud.service.CommonService;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.framework.web.view.ComboSupportList;
import net.silencily.sailing.struts.BaseFormPlus;

import org.apache.commons.lang.StringUtils;


/**
 * 
 * 
 * @author baihe
 * @version 1.0
 */
public class UserManageForm extends BaseFormPlus {
	
	private ComboSupportList statusComboList = null;
	
	private ComboSupportList readAccessLevelComboList = null;

	private ComboSupportList writeAccessLevelComboList = null;
	
	private ComboSupportList rwCtrlComboList = null;
	
	private ComboSupportList messageComboList = null;
	
	private ComboSupportList nodetypeComboList;
	
	private ComboSupportList urltypeComboList;
	
	private TblCmnUser bean;  //管理用户的实体bean
	
	private TblCmnUserPermission cmnUserPermission;
	
	private TblCmnMsgConfig cmnMsgConfig;
	
	private TblCmnPermission cmnPermission;
	
	private String deptId;
	
	private String deptName;
	
    private List infoList = new ArrayList();
	
	private String checkType;
	
	private List permissions;
	
	private List rolePermission;
	
	private List selectUser;
	
	private List dataPermission = new ArrayList();
	
	private TblCmnUserMember cmnUserMember;
	
	private List user =new ArrayList(0);
	
	private List roleUser;
	
	private String empId;
	
	private String strFlg;
	
	private String userName;
	
	private String password;
	
	private String oldPassword;
	
	private String roleId;
	
	private List role = new ArrayList(0);
	
	private SysCodeList scl = SysCodeList.factory();   //下拉框

	public TblCmnUser getBean() {
		if (bean == null) {
		    if (StringUtils.isBlank(getOid())) {
                bean = new TblCmnUser();
            }
		    else{
		        bean = (TblCmnUser)cService().load(TblCmnUser.class, getOid());  
		    }
	}
		return bean;
	}
	
	public TblCmnUserMember getCmnUserMember() {
        if (cmnUserMember == null) {
            cmnUserMember = (TblCmnUserMember)cService().load(TblCmnUserMember.class, getOid());
        }
        return cmnUserMember;
    }

	public void setCmnUserMember(TblCmnUserMember cmnUserMember) {
		this.cmnUserMember = cmnUserMember;
	}
	
	/**
	 * 
	 * 功能描述
	 * @param bean
	 * 2007-11-23 下午06:12:57
	 * @version 1.0
	 * @author baihe
	 */
	public void setBean(TblCmnUser bean) {
		this.bean = bean;
	}
	
	/**
	 * 
	 * 功能描述
	 * @return
	 * 2007-11-23 下午06:13:06
	 * @version 1.0
	 * @author baihe
	 */
	public SysCodeList getScl() {
		return scl;
	}

	/**
	 * @author baihe
	 * @param scl the scl to set
	 */
	public void setScl(SysCodeList scl) {
		this.scl = scl;
	}
	
	/**
	 * 
	 * 功能描述 调用公用接口
	 * @return
	 * 2007-11-23 下午06:12:06
	 * @version 1.0
	 * @author baihe
	 */
	private CommonService cService() {
		return (CommonService)ServiceProvider.getService(CommonService.SERVICE_NAME);
	}

	/**
	 * 
	 * 功能描述 获取激活状态下拉列表框数据源
	 * @return 
	 * 2007-11-23 下午06:09:06
	 * @version 1.0
	 * @author baihe
	 */
	public ComboSupportList getStatusComboList() {
		if(null == statusComboList)
		{
				statusComboList = new  ComboSupportList(TblCmnUser.getStatusMap());
		}
		return statusComboList;
	}
	
	public void setStatusComboList(ComboSupportList statusComboList) {
		this.statusComboList = statusComboList;
	}
	
	public ComboSupportList getNodetypeComboList() {
		if(null == nodetypeComboList)
		{
			nodetypeComboList = new  ComboSupportList(TblCmnPermission.getNodeTypeMap());
		}		
		
		
		return nodetypeComboList;
	}

	public void setNodetypeComboList(ComboSupportList nodetypeComboList) {
		this.nodetypeComboList = nodetypeComboList;
	}
	
	public ComboSupportList getUrltypeComboList() {
		if(null == urltypeComboList)
		{
			urltypeComboList = new  ComboSupportList(TblCmnPermission.getUrltypeMap());
		}		
		
		return urltypeComboList;
	}

	public void setUrltypeComboList(ComboSupportList urltypeComboList) {
		this.urltypeComboList = urltypeComboList;
	}

	/**
	 * 
	 * 功能描述	获取权限列表信息
	 * @return
	 * 2007-11-23 下午06:13:25
	 * @version 1.0
	 * @author baihe
	 */
	
	public List getPermissions() {
		return permissions;
	}

	public void setPermissions(List permissions) {
		this.permissions = permissions;
	}

	public TblCmnUserPermission getCmnUserPermission() {
		if(null == cmnUserPermission)
		{
			if(StringUtils.isBlank(getOid())){
				cmnUserPermission=new TblCmnUserPermission();
			}
			else{
				cmnUserPermission=(TblCmnUserPermission)cService().load(TblCmnUserPermission.class, getOid());
			}
			
		}
		return cmnUserPermission;
	}

	public void setCmnUserPermission(TblCmnUserPermission cmnUserPermission) {
		this.cmnUserPermission = cmnUserPermission;
	}

	public TblCmnPermission getCmnPermission() {
		return cmnPermission;
	}

	public void setCmnPermission(TblCmnPermission cmnPermission) {
		this.cmnPermission = cmnPermission;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public List getRole() {
		return role;
	}

	public void setRole(List role) {
		this.role = role;
	}

	public List getRolePermission() {
		return rolePermission;
	}

	public void setRolePermission(List rolePermission) {
		this.rolePermission = rolePermission;
	}

	public ComboSupportList getMessageComboList() {
		if(null == messageComboList)
		{
			messageComboList = new  ComboSupportList(TblCmnMsgConfig.getMessageMap());
		}
		return messageComboList;
	}

	public void setMessageComboList(ComboSupportList messageComboList) {
		this.messageComboList = messageComboList;
	}

	public TblCmnMsgConfig getCmnMsgConfig() {
		if(cmnMsgConfig==null){
			if(StringUtils.isBlank(getMsgOid())){
				cmnMsgConfig=new TblCmnMsgConfig();
			}
			else{
				cmnMsgConfig=(TblCmnMsgConfig)cService().load(TblCmnMsgConfig.class, getMsgOid());
			}
		}

		return cmnMsgConfig;
	}
	public String getMsgOid()
	{
		return (String)request.getParameter("msgOid");
	}
	public void setCmnMsgConfig(TblCmnMsgConfig cmnMsgConfig) {
		this.cmnMsgConfig = cmnMsgConfig;
	}

	public List getSelectUser() {
		return selectUser;
	}

	public void setSelectUser(List selectUser) {
		this.selectUser = selectUser;
	}

	public List getRoleUser() {
		return roleUser;
	}

	public void setRoleUser(List roleUser) {
		this.roleUser = roleUser;
	}
	
	
	/**
	 * 
	 * 功能描述 获取只读数据访问级别下拉列表框数据源
	 * @return
	 * 2007-11-30 下午01:34:23
	 * @version 1.0
	 * @author baihe
	 */
	public ComboSupportList getReadAccessLevelComboList() {
		if(null == readAccessLevelComboList)
		{
			readAccessLevelComboList = new  ComboSupportList(TblCmnUserPermission.getReadAccessLevelMap());
		}
		return readAccessLevelComboList;
	}

	public void setReadAccessLevelComboList(
			ComboSupportList readAccessLevelComboList) {
		this.readAccessLevelComboList = readAccessLevelComboList;
	}
	
	/**
	 * 
	 * 功能描述 获取可编辑数据访问级别下拉列表框数据源
	 * @return
	 * 2007-11-30 下午01:35:18
	 * @version 1.0
	 * @author baihe
	 */
	public ComboSupportList getWriteAccessLevelComboList() {
		if(null == writeAccessLevelComboList)
		{
			writeAccessLevelComboList = new  ComboSupportList(TblCmnUserPermission.getWriteAccessLevelMap());
		}
		return writeAccessLevelComboList;
	}

	public void setWriteAccessLevelComboList(
			ComboSupportList writeAccessLevelComboList) {
		this.writeAccessLevelComboList = writeAccessLevelComboList;
	}
	
	/**
	 * 
	 * 功能描述 获取读写控制下拉列表框数据源
	 * @return
	 * 2007-11-30 下午01:36:29
	 * @version 1.0
	 * @author baihe
	 */
	public ComboSupportList getRwCtrlComboList() {
		if(null == rwCtrlComboList)
		{
			rwCtrlComboList = new  ComboSupportList(TblCmnUserPermission.getRwCtrlMap());
		}
		return rwCtrlComboList;
	}

	public void setRwCtrlComboList(ComboSupportList rwCtrlComboList) {
		this.rwCtrlComboList = rwCtrlComboList;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List getUser() {
		return user;
	}

	public void setUser(List user) {
		this.user = user;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getStrFlg() {
		return strFlg;
	}

	public void setStrFlg(String strFlg) {
		this.strFlg = strFlg;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
		if(!deptId.equals(this.getBean().getTblCmnDept().getId()))
			this.getBean().setTblCmnDept((TblCmnDept)cService().load(TblCmnDept.class, deptId));
		
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		
		this.deptName = deptName;
	}
	
	public List getDataPermission() {
		return dataPermission;
	}

	public void setDataPermission(List dataPermission) {
		this.dataPermission = dataPermission;
	}

	public TblCmnUserMember getDataPermission(int i) throws IndexOutOfBoundsException {
		while (dataPermission.size() <= i) {
			dataPermission.add(null);
		}
		TblCmnUserMember detail = (TblCmnUserMember) dataPermission.get(i);
		String SId = request.getParameter("dataPermission[" + i + "].id");
		if (StringUtils.isNotBlank(SId)) {
			detail = (TblCmnUserMember) cService().load(TblCmnUserMember.class, SId);
		}
		if (null == detail) {
			detail = new TblCmnUserMember();
		}
		// 级联保存方式；
		dataPermission.set(i, detail);

		return detail;
	}

	
	public void setDataPermission(TblCmnUserMember exp,int index) throws IndexOutOfBoundsException {
		this.dataPermission.set(index, exp);
	}
	
	public String getCheckType() {
		return checkType;
	}
	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}
	
	public List getInfoList() {
		return infoList;
	}


	public void setInfoList(List infoList) {
		this.infoList = infoList;
	}
	
}
