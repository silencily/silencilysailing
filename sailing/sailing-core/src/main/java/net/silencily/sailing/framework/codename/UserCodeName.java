package net.silencily.sailing.framework.codename;

import java.util.Iterator;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;

import net.silencily.sailing.framework.authentication.entity.Principal;
import net.silencily.sailing.framework.authentication.entity.Role;
import net.silencily.sailing.framework.authentication.entity.User;
import net.silencily.sailing.framework.utils.AssertUtils;

/**
 * 用于业务实体引用{@link User}作为属性, 在保存业务实体时把用户登录名<code>username</code> 写到实体相关的数据库表,
 * 检索业务实体时把保存在业务实体相关的数据库表中的用户名转换成实际 的{@link User}, 在实际应用中绝大多数情况下要使用这类组件,
 * 而不是显式地建立引用, 特别是指 使用其他系统维护的实体, 而且仅仅是读取这个相关实体的属性的情况
 * 
 * @author zhangli
 * @version $Id: UserCodeName.java,v 1.1 2010/12/10 10:54:16 silencily Exp $
 * @since 2007-4-8
 */
public class UserCodeName extends User implements Principal {

	/**
	 * 这个用户是否拥有参数指定的角色
	 * 
	 * @param roleCode
	 *            角色编码, 注意这个参数可以是","分隔的角色编码列表
	 * @return 如果拥有参数指定的角色返回<code>true</code>
	 * @throws NullPointerException
	 *             如果参数是<code>null</code>,<code>empty string</code>
	 */
	public boolean hasRole(String roleCode) {
		AssertUtils.notNull(roleCode);
		Set roleCodes = org.springframework.util.StringUtils
				.commaDelimitedListToSet(roleCode);
		boolean ret = false;
		for (Iterator it = roleCodes.iterator(); !ret && it.hasNext();) {
			final String r = (String) it.next();

			ret = CollectionUtils.exists(getRoles(), new Predicate() {
				public boolean evaluate(Object element) {
					return r.equals(((Role) element).getCode());
				}
			});
		}
		return ret;
	}

	public UserCodeName copyFrom(User user) {
		setCode(user.getUsername());
		setName(user.getName());
		setDepartment(user.getDepartment());
		setDepartments(user.getDepartments());
		setRoles(user.getRoles());
		return this;
	}

	/**
	 * 检索用户的登录名
	 * 
	 * @return 用户登录名
	 */
	public String getCode() {
		return getUsername();
	}

	public void setCode(String code) {
		setUsername(code);
	}

	public boolean pass(User user) {
		return user.getUsername().equals(getUsername());
	}

	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}
		if (getClass() != o.getClass()) {
			return false;
		}
		UserCodeName d = (UserCodeName) o;
		return StringUtils.equals(d.getCode(), this.getCode());
	}

	public int hashCode() {
		if (StringUtils.isBlank(getCode())) {
			return super.hashCode();
		} else {
			return getClass().hashCode() * 29 + getCode().hashCode();
		}
	}

	public static void main(String[] args) {
		String s1 = null;
		String s2 = null;
		System.out.println(StringUtils.equals(s1, s2));
	}
}
