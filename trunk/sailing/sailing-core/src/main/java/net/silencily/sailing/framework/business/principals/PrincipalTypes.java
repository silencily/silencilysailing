package net.silencily.sailing.framework.business.principals;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.silencily.sailing.exception.UnexpectedException;
import net.silencily.sailing.framework.authentication.entity.Principal;
import net.silencily.sailing.framework.authentication.entity.User;
import net.silencily.sailing.framework.utils.AssertUtils;
import net.silencily.sailing.framework.utils.ScalableList;

import org.apache.commons.lang.StringUtils;

/**
 * ����ҵ��ʵ�������, ������ЩȺ��, ����Ա, ��λ, ���ſ��Է���ҵ��ʵ��, �����ж��{@link PrincipalType}
 * ,��ʾ�����˶��ַ�����, ����������Լ�ĳЩ���ſ��Է���һ��ʵ��, �������{@link #principals}
 * û���κ�Ԫ����ô�ͱ�ʾ����Ҫ���ƶ�ʵ��ķ���
 * @author zhangli
 * @version $Id: PrincipalTypes.java,v 1.1 2010/12/10 10:54:21 silencily Exp $
 * @since 2007-6-7
 */
public class PrincipalTypes implements Principal {
    
    /**
     * ��������{@link Principal}������, �ֱ���{@link UsersCodeName}, {@link DepartmentsCodeName}
     */
    private List principals = new ScalableList(3);
    
    /**
     * ������ָ�����û��Ƿ�ͨ����֤, ���ǻ������û��б���, ���߾��и�λ�б��е�һ����λ, ����
     * ���ڲ����б��е�һ�����Ż���������, ֻҪ��һ������������֤����ͨ��
     * @param user Ҫ�жϵ��û�
     * @return ����û����㶨�������,����û�ж�����������<code>true</code>
     */
    public boolean pass(User user) {
        AssertUtils.notNull(user);
        boolean ret = principals.size() == 0;
        for (Iterator it = principals.iterator(); !ret && it.hasNext(); ) {
            PrincipalType p = (PrincipalType) it.next();
            ret = (p != null) && p.pass(user);
        }
        return ret;
    }
    
    /**
     * ����������Ͷ���ķ�����
     * @return ������Ͷ���ķ�����
     */
    public List getPrincipalTypes() {
        return this.principals;
    }
    
    /**
     * �����ʵ��������д��<code>String</code>�ĸ�ʽ, ������ʵ��û�ж��������߾ͷ���<code>null</code>
     * @return �������ʵ����<code>String</code>
     */
    public String flat() {
        StringBuffer buf = new StringBuffer();
        for (Iterator it = principals.iterator(); it.hasNext(); ) {
            PrincipalType p = (PrincipalType) it.next();
            if (StringUtils.isNotBlank(p.getCode())) {
                if (buf.length() > 0) {
                    buf.append(";");
                }
                buf.append(p.getPrincipal().getClass().getName()).append(":").append(p.getCode());
            }
        }
        if (buf.length() == 0) {
            return null;
        }
        return buf.toString();
    }
    
    /**
     * �ӱ���һ��<code>PrincipalTypes</code>��<code>String</code>����ʵ��
     * @param str ����һ��<code>PrincipalTypes</code>��<code>String</code>
     */
    public void deflat(String str) {
        if (StringUtils.isNotBlank(str)) {
            this.principals = new ArrayList(3);
            String[] ps = str.split(";");
            for (int i = 0; i < ps.length; i++) {
                String[] cv = ps[i].split(":");
                String className = cv[0];
                String codes = cv[1];
                try {
                    Class type = Class.forName(className);
                    addPrincipalType(new PrincipalType(codes, null, type));
                } catch (ClassNotFoundException e) {
                    throw new UnexpectedException("���ܴ�[" + className + "]����Class", e);
                }
            }
        }
    }

    public PrincipalType getPrincipals(int index) {
        return (PrincipalType) principals.get(index);
    }

    public void setPrincipals(int index, PrincipalType principalType) {
        this.principals.set(index, principalType);
    }
    
    public void addPrincipalType(PrincipalType principalType) {
        this.principals.add(principalType);
    }

    public String getCode() {
        return iterate(new PrincipalCallback() {
            public String execute(PrincipalType principalType) {
                return principalType.getCode();
            }
        });
    }

    public String getName() {
        return iterate(new PrincipalCallback() {
            public String execute(PrincipalType principalType) {
                return principalType.getName();
            }
        });
    }
    
    private String iterate(PrincipalCallback callback) {
        StringBuffer buf = new StringBuffer();
        for (Iterator it = this.principals.iterator(); it.hasNext(); ) {
            if (buf.length() > 0) {
                buf.append(",");
            }
            buf.append(callback.execute((PrincipalType) it.next()));
        }
        return buf.toString();
    }
    
    public interface PrincipalCallback {
        String execute(PrincipalType principalType);
    }
}
