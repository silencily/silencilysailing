package net.silencily.sailing.security.model;

import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;

/**
 * 当前登录用户，提供当前登录用户相关的安全信息
 * @author yushn
 * @version 1.0
 */
public interface CurrentUser {
    /**
     * 
     * 取得当前用户的所有角色
     * @return key为roleId,value为roleCd
     * 2007-11-20 下午03:06:14
     * @version 1.0
     * @author yushn
     */
    public HashMap getRoles();
	/**
	 * 
	 * 判断当前用户是否拥有指定的角色
	 * @param roleID 角色ID
	 * @return
	 * 2007-11-20 下午03:06:14
	 * @version 1.0
	 * @author yushn
	 */
	public boolean hasRole(String roleId);
	/**
	 * 
	 * 判断当前用户是否拥有传入的角色集中的角色
	 * @param roleId 角色ID集，元素为String类型，HashSet类型是为了快速比对
	 * @return 当前用户拥有的角色ID集与传入的角色集的交集不为空就返回true,否则返回false
	 * 2007-11-20 下午02:49:32
	 * @version 1.0
	 * @author yushn
	 */
	public boolean hasRole(HashSet roleId);
    /**
     * 
     * 判断当前用户是否拥有指定的角色
     * @param roleCode 角色标识代码
     * @return
     * 2007-11-20 下午03:06:14
     * @version 1.0
     * @author tongjq
     */
    public boolean hasRoleCd(String roleCode);
    /**
     * 
     * 判断当前用户是否拥有传入的角色集中的角色
     * @param roleCodes 用户代码集，元素为String类型，HashSet类型是为了快速比对
     * @return 当前用户拥有的角色集与传入的角色集的交集不为空就返回true,否则返回false
     * 2007-11-20 下午02:49:32
     * @version 1.0
     * @author tongjq
     */
    public boolean hasRoleCd(HashSet roleCodes);
	/**
	 * 
	 * 获取当前用户在指定页面下的指定数据项的读写控制类型
	 * 如果配置了具体数据项的读写控制类型，则返回配置的读写控制类型，否则返回页面的默认权限类型。
	 * @param pageUrl 页面url
	 * @param permissionCode 数据项权限标识代码
	 * @return 数据项的可权限类型
	 * 		包括：不可见、只读、可编辑
	 * 		编程时可使用{@link RWCtrlType}中的常量属性进行判断
	 * 2007-11-20 下午03:35:48
	 * @version 1.0
	 * @author yushn
	 */
	public int getFieldRWCtrlType(String pageUrl,String permissionCode);
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
	public int getPageDefaultRWCtrlType(String pageUrl);
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
	//public int getPagePermission(String pageUrl);
	/**
	 * 获取当前用户对指定记录的读写控制类型
	 * 功能描述
	 * @param deptId 记录的创建者部门
	 * @param userId	记录的创建者
	 * @return 读写控制类型
	 * 		编程时可使用{@link RWCtrlType}中的常量属性进行判断
	 * 2007-11-28 下午07:19:12
	 * @version 1.0
	 * @author yushn
	 */
	public int getRowRWCtrlType(String pageUrl,String deptId,String userId);
	/**
	 * 获取登录用户id(主键)
	 * 此接口是为了方便系统管理委托权限和对记录级数据访问权限的判断
	 * @return
	 * 2007-11-26 下午07:23:56
	 * @version 1.0
	 * @author yushn
	 */
	public String getUserId();
	/**
	 * 获取登录用户对应的员工编号(登录名)
	 * 功能描述
	 * @return
	 * 2007-11-28 下午07:04:27
	 * @version 1.0
	 * @author yushn
	 */
	public String getEmpCd();
	/**
	 * 获取登录用户中文名
	 * 用于显示
	 * @return
	 * 2007-11-26 下午07:25:17
	 * @version 1.0
	 * @author yushn
	 */
	public String getUserName();
	/**
	 * 获取部门id（主键）
	 * 此接口是为了方便判断部门级访问权限
	 * @return
	 * 2007-11-26 下午07:25:46
	 * @version 1.0
	 * @author yushn
	 */
	public String getDeptId();
	/**
	 * 获取登录用户部门名称
	 * 用于显示
	 * @return
	 * 2007-11-26 下午07:30:19
	 * @version 1.0
	 * @author yushn
	 */
	public String getDeptName();
	/**
	 * 
	 * 获取登录名
	 * @return
	 * 2007-11-30 下午05:10:00
	 * @version 1.0
	 * @author yushn
	 */
	public String getLoginName();
	/**
	 * 获取权限
	 * @return
	 * 2007-12-05 下午04:30:00
	 * @version 1.0
	 * @author tongjq
	 */
	public HashMap getUrlPermissions();
    /**
     * 获取部门以及子部门
     * @return
     * 2007-12-05 下午04:30:00
     * @version 1.0
     * @author tongjq
     */
    public HashSet getSubDeptIds();

    /**
     * 获取登录员工信息表id
     * @return
     * 2007-11-26 下午07:30:19
     * @version 1.0
     * @author yushn
     */
    public String getEmpId();
    /**
     * 获取班组ID，不是班组的时候返回的是空值（""）。
     */
    public String getGroupId();
    
    public Dept getDept();
    
	/**
	 * 获取真实的部门id,考虑到可能是班组的情况
	 * @return
	 */
	public String getRealDeptId();
	
	public Map<String, CmnEntity> getDataSecurityMap();
}