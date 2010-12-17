package net.silencily.sailing.security.model;

/**
 * 数据访问类型
 * @author yushn
 * @version 1.0
 */
public interface DataAccessType {
	/**
	 * 系统级:没有范围限制
	 */
	int SYSTEM = 0;
	/**
	 * 部门级:只能访问与自己同部门的用户创建的数据
	 */
	int DEPT = 1;
	/**
	 * 个人级：只能访问自己创建的数据
	 */
	int SELF = 2;
	/**
	 * 禁止：不可访问
	 */
	int FORBID = 3;
}
