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
 * ����ҵ��ʵ������{@link User}��Ϊ����, �ڱ���ҵ��ʵ��ʱ���û���¼��<code>username</code> д��ʵ����ص����ݿ��,
 * ����ҵ��ʵ��ʱ�ѱ�����ҵ��ʵ����ص����ݿ���е��û���ת����ʵ�� ��{@link User}, ��ʵ��Ӧ���о�����������Ҫʹ���������,
 * ��������ʽ�ؽ�������, �ر���ָ ʹ������ϵͳά����ʵ��, ���ҽ����Ƕ�ȡ������ʵ������Ե����
 * 
 * @author zhangli
 * @version $Id: UserCodeName.java,v 1.1 2010/12/10 10:54:16 silencily Exp $
 * @since 2007-4-8
 */
public class UserCodeName extends User implements Principal {

	/**
	 * ����û��Ƿ�ӵ�в���ָ���Ľ�ɫ
	 * 
	 * @param roleCode
	 *            ��ɫ����, ע���������������","�ָ��Ľ�ɫ�����б�
	 * @return ���ӵ�в���ָ���Ľ�ɫ����<code>true</code>
	 * @throws NullPointerException
	 *             ���������<code>null</code>,<code>empty string</code>
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
	 * �����û��ĵ�¼��
	 * 
	 * @return �û���¼��
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
