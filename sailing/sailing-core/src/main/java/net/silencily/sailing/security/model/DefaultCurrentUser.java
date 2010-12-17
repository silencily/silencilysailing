package net.silencily.sailing.security.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;


public class DefaultCurrentUser implements Serializable,CurrentUser  {
	/**
	 * 用户名
	 */
	private String userName;
	/**
	 * 用户id（用户表主键）
	 */
	private String userId;
	/**
	 * 员工信息表id
	 */
	private String empId;
	/**
	 * 用户所在部门
	 */
	private Dept dept;
	/**
	 * 用户拥有的功能权限,key为页面url，value为{@link UrlPermission}类型。
	 */
	private HashMap urlPermissions;
	/**
	 * 用户拥有的角色，,key为角色ID,value为角色标识。
	 */
	private HashMap roles;
	/**
	 * 用户所在部门及子部门ID，集合内为String 类型的部门ID
	 */
	private HashSet subDeptIds;
	
	/**
	 * 登录用户对应的员工编号(登录名)
	 */
	private String empCd;
	
	private Map<String, CmnEntity> dataSecurityMap = null;
	
    /**
     * 
     * 取得当前用户的所有角色
     * @return key为roleId,value为roleCd
     * 2007-11-20 下午03:06:14
     * @version 1.0
     * @author yushn
     */
    public HashMap getRoles() {
        return roles;
    }
    /**
     * 
     * 判断当前用户是否拥有指定的角色
     * @param roleID 角色ID
     * @return
     * 2007-11-20 下午03:06:14
     * @version 1.0
     * @author yushn
     */
	public boolean hasRole(String roleId)
	{
		return roles.containsKey(roleId);
	}
    /**
     * 
     * 判断当前用户是否拥有传入的角色集中的角色
     * @param roleId 角色ID集，元素为String类型，HashSet类型是为了快速比对
     * @return 当前用户拥有的角色ID集与传入的角色集的交集不为空就返回true,否则返回false
     * 2007-11-20 下午02:49:32
     * @version 1.0
     * @author yushn
     */
	public boolean hasRole(HashSet roleId)
	{
		Set base;
		Set loop;
		if(null == roles||null == roleId)
			return false;
		if(roles.size() > roleId.size())
		{
			base = roles.keySet();
			loop = roleId;
		}
		else
		{
			base = roleId;
			loop = roles.keySet();
		}
		for(Iterator it = loop.iterator();it.hasNext();)
		{
			if(base.contains(it.next()))
			{
				return true;
			}
		}
		return false;
	}
    /**
     * 
     * 判断当前用户是否拥有指定的角色
     * @param roleCode 角色标识代码
     * @return
     * 2007-11-20 下午03:06:14
     * @version 1.0
     * @author tongjq
     */
    public boolean hasRoleCd(String roleCode)
    {
        return roles.containsValue(roleCode);
    }
    /**
     * 
     * 判断当前用户是否拥有传入的角色集中的角色
     * @param roleCodes 用户代码集，元素为String类型，HashSet类型是为了快速比对
     * @return 当前用户拥有的角色集与传入的角色集的交集不为空就返回true,否则返回false
     * 2007-11-20 下午02:49:32
     * @version 1.0
     * @author tongjq
     */
    public boolean hasRoleCd(HashSet roleCodes)
    {
        Set base;
        Set loop;
        if(null == roles||null == roleCodes)
            return false;
        if(roles.size() > roleCodes.size())
        {
            base = new HashSet(roles.values());
            loop = roleCodes;
        }
        else
        {
            base = roleCodes;
            loop = new HashSet(roles.values());
        }
        for(Iterator it = loop.iterator();it.hasNext();)
        {
            if(base.contains(it.next()))
            {
                return true;
            }
        }
        return false;
    }
	/**
	 * 
	 * 获取当前用户在指定页面下的指定数据项的权限类型
	 * 如果配置了具体数据项的权限类型，则返回配置的权限类型，否则返回页面的默认权限类型。
	 * @param pageUrl 页面url
	 * @param permissionCode 数据项权限标识代码
	 * @return 数据项的可权限类型
	 * 		包括：不可见、只读、可编辑
	 * 		编程时可使用{@link RWCtrlType}中的常量属性进行判断
	 * 2007-11-20 下午03:35:48
	 * @version 1.0
	 * @author yushn
	 */
	public int getFieldRWCtrlType(String pageUrl,String permissionCode)
	{
		UrlPermission urlp = (UrlPermission)urlPermissions.get(pageUrl);
		if(null == urlp)
			return RWCtrlType.SIGHTLESS;
		HashMap fps = urlp.getFieldPerms();
		if(null == fps || StringUtils.isBlank(permissionCode))
			return urlp.getRwCtrlType();
		FieldPermission fp = (FieldPermission)fps.get(permissionCode);
		if(null == fp)//未配置数据项权限则使用页面默认的读写控制类型
			return urlp.getRwCtrlType();
		return fp.getRwCtrlType();
	}
	/**
	 * 获取指定页面的默认读写控制类型。
	 * 功能描述
	 * @param pageUrl
	 * @return 数据项的可权限类型
	 * 		包括：不可见、只读、可编辑
	 * 		编程时可使用{@link RWCtrlType}中的常量属性进行判断
	 * 2007-11-30 上午11:19:30
	 * @version 1.0
	 * @author yushn
	 */
	public int getPageDefaultRWCtrlType(String pageUrl)
	{
		UrlPermission urlp = (UrlPermission)urlPermissions.get(pageUrl);
		if(null == urlp)
			return RWCtrlType.READ_ONLY;
		return urlp.getRwCtrlType()==RWCtrlType.EDIT ? RWCtrlType.EDIT : RWCtrlType.READ_ONLY;
	}
	/**
	 * 获取当前用户对指定记录的读写控制类型
	 * 功能描述
	 * @param deptId 记录的创建者部门Id
	 * @param empCd	 记录的创建者系统登录名称
	 * @return 读写控制类型
	 * 		编程时可使用{@link RWCtrlType}中的常量属性进行判断
	 * 2007-11-28 下午07:19:12
	 * @version 1.0
	 * @author yushn
	 */
	public int getRowRWCtrlType(String pageUrl,String deptId,String empCd)
	{
		UrlPermission urlp = (UrlPermission)urlPermissions.get(pageUrl);
		if(null == urlp)
			return RWCtrlType.SIGHTLESS;
		int wt = urlp.getDataAccessLevelWrite();
		switch(wt)
		{
		case DataAccessType.SYSTEM:
			return RWCtrlType.EDIT;
		case DataAccessType.DEPT:
			//对于部门的比较还要判断传入的部门是否是当前用户所在部门的子部门!!!
			if(this.subDeptIds.contains(deptId))
			{
				return RWCtrlType.EDIT;
			}
		case DataAccessType.SELF:
			//对于部门的比较还要判断传入的部门是否是当前用户所在部门的子部门!!!
			if(this.subDeptIds.contains(deptId)&&StringUtils.equals(this.empCd, empCd))
			{
				return RWCtrlType.EDIT;
			}
		default://数据访问级别位“禁止”
			return RWCtrlType.READ_ONLY;
		}
	}
	/**
	 * 
	 * 获取指定页面所有数据项的默认权限类型
	 * @param pageUrl
	 * @return 页面所有数据项的默认权限类型
	 * 		只有只读和可编辑两种类型
	 * 		编程时可使用{@link RWCtrlType}中的常量属性进行判断
	 * 2007-11-20 下午05:04:41
	 * @version 1.0
	 * @author yushn
	 */
//	public int getPagePermission(String pageUrl)
//	{
//		UrlPermission urlp = (UrlPermission)urlPermissions.get(pageUrl);
//		if(null == urlp)
//			return RWCtrlType.SIGHTLESS;
//		return urlp.getDataAccessLevel();
//	}
	public String getUserId(){
		return this.userId;
	}
	/**
	 * 获取登录用户对应的员工编号(登录名)
	 * 功能描述
	 * @return
	 * 2007-11-28 下午07:04:27
	 * @version 1.0
	 * @author yushn
	 */
	public String getEmpCd(){
		return this.empCd;
	}
	/**
	 * 获取登录用户中文名
	 * 用于显示
	 * @return
	 * 2007-11-26 下午07:25:17
	 * @version 1.0
	 * @author yushn
	 */
	public String getUserName(){
		return this.userName;
	}
	/**
	 * 获取部门id（主键）
	 * 此接口是为了方便判断部门级访问权限
	 * @return
	 * 2007-11-26 下午07:25:46
	 * @version 1.0
	 * @author yushn
	 */
	public String getDeptId(){
		return this.dept.getId();
	}
	/**
	 * 获取登录用户部门名称
	 * 用于显示
	 * @return
	 * 2007-11-26 下午07:30:19
	 * @version 1.0
	 * @author yushn
	 */
	public String getDeptName(){
		return this.dept.getName();
	}
	
	/**
	 * 
	 * 获取登录名
	 * @return
	 * 2007-11-30 下午05:10:00
	 * @version 1.0
	 * @author yushn
	 */
	public String getLoginName()
	{
		return empCd;
	}

	/**
	 * 获取权限
	 * @return
	 * 2007-12-05 下午04:30:00
	 * @version 1.0
	 * @author tongjq
	 */
	public HashMap getUrlPermissions()
	{
		return urlPermissions;
	}
	
	public Dept getDept() {
		return dept;
	}
	public void setDept(Dept dept) {
		this.dept = dept;
	}
	public void setRoles(HashMap roles) {
		this.roles = roles;
	}
	public void setUrlPermissions(HashMap urlPermissions) {
		this.urlPermissions = urlPermissions;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setEmpCd(String empCd) {
		this.empCd = empCd;
	}
	public HashSet getSubDeptIds() {
		return subDeptIds;
	}
	public void setSubDeptIds(HashSet subDeptIds) {
		this.subDeptIds = subDeptIds;
	}
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	/**
	 * 获取班组ID
	 * @return 如果是班组返回的是班组id，否则为空值。
	 */
	public String getGroupId(){
		//设定返回值的初始值。
		String groupId = "";
		//获取id，可能是部门id也可能是班组id
		String id = this.dept.getId();
		
		//获取部门id，如果id中是班组时，该班组所属的部门id保存在这里。
		String deptId = this.dept.getDeptId();
		
		//因为Id中可能是部门id，也可能是班组id，而deptId中是部门id，所以当是班组时，id与deptId不等。
		if(!"".equals(id) && !"".equals(deptId) && !id.equalsIgnoreCase(deptId)){
			groupId = id;
		}
		
		return groupId;
	}
	/**
	 * 获取真实的部门id,考虑到可能是班组的情况
	 * @return
	 */
	public String getRealDeptId()
	{
		//获取id，可能是部门id也可能是班组id
		String id = this.dept.getId();
		
		//获取部门id，如果id中是班组时，该班组所属的部门id保存在这里。
		String deptId = this.dept.getDeptId();
	
		if(!"".equals(id) && !"".equals(deptId) && !id.equalsIgnoreCase(deptId)){
			return deptId;
		}else{
			return id;
		}
	}
    public Map<String, CmnEntity> getDataSecurityMap() {
        return dataSecurityMap;
    }
    public void setDataSecurityMap(Map<String, CmnEntity> dataSecurityMap) {
        this.dataSecurityMap = dataSecurityMap;
    }
	
}
