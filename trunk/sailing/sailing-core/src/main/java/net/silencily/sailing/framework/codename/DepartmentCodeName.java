package net.silencily.sailing.framework.codename;

import net.silencily.sailing.framework.authentication.entity.Department;
import net.silencily.sailing.framework.authentication.entity.Principal;
import net.silencily.sailing.framework.authentication.entity.User;

import org.apache.commons.lang.StringUtils;

/**
 * <p>����ҵ��ʵ�������ò�����Ϊ���Ե�<code>CodeName</code>��, ����ҵ��ʵ��ʱ, ��ص�
 * ���������д�������Ƽ���ص���Ϣ, ����ҵ��ʵ��ʱ, ����������Ѳ��ŵ�<code>id</code>
 * д�ص�ҵ��ʵ����ص����ݿ����, Ϊʲô��<code>CodeName</code>������<code>Department</code>
 * :ԭ������������ʹ�û�����ͬ, ʵ��ϸ��Ҳ��ͬ, ������<code>equals</code></p>
 * <p>��Ϊ����, ��϶ȵȷ��������, �����Ƽ������ַ�ʽʹ�û�����ʵ��, �����ǽ���ǿ����, ��
 * ������Ҫ���õ�ʵ����ȫ��������ϵͳ����ά��, ����ʵ���õ������Ժ��ٵ������. ͬʱ��Ϊһ��
 * �����Ĺ淶, ��Ҫ����ʵ���"CodeName"�������������͵�<code>CodeName</code></p>
 * @author zhangli
 * @version $Id: DepartmentCodeName.java,v 1.1 2010/12/10 10:54:16 silencily Exp $
 * @since 2007-4-8
 * @see CodeName
 */
public class DepartmentCodeName extends Department implements Principal {
    
    public DepartmentCodeName copyFrom(Department department) {
        setCode(department.getId());
        setName(department.getName());
        setParent(department.getParent());
        setProperties(department.getProperties());
        setHasChildren(department.isHasChildren());
        return this;
    }
    
    /**
     * ��������<code>id</code>�ķ���
     * @return ����<code>id</code>
     */
    public String getCode() {
        return getId();
    }
    
    /**
     * ����һ�����ŵ�<code>id</code>, ����������������޸�{@link Department}, �����޸�
     * ҵ��ʵ�����õĲ��ŵ�<code>id</code>, ������<code>java bean</code>�е���������,
     * ������ҳ�����޸���һ��ҵ��ʵ�����ز���
     * @param code 
     */
    public void setCode(String code) {
        setId(code);
    }
    
    /**
     * �жϲ���ָ�����û��Ƿ������������
     * @return �������������ŷ���<code>true</code>
     */
    public boolean pass(User user) {
        return user.getDepartment().getId().equals(getId());
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
        DepartmentCodeName d = (DepartmentCodeName) o;
        return StringUtils.equals(getCode(), d.getCode());
    }
    
    public int hashCode() {
        if (StringUtils.isBlank(getCode())) {
            return super.hashCode();
        } else {
            return getClass().hashCode() * 29 + getCode().hashCode();
        }
    }
}
