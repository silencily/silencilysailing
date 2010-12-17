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
 * 用于业务实体的属性, 定义哪些群体, 像人员, 岗位, 部门可以访问业务实体, 可能有多个{@link PrincipalType}
 * ,表示定义了多种访问者, 比如个别人以及某些部门可以访问一个实体, 如果属性{@link #principals}
 * 没有任何元素那么就表示不需要控制对实体的访问
 * @author zhangli
 * @version $Id: PrincipalTypes.java,v 1.1 2010/12/10 10:54:21 silencily Exp $
 * @since 2007-6-7
 */
public class PrincipalTypes implements Principal {
    
    /**
     * 集合中是{@link Principal}的子类, 分别是{@link UsersCodeName}, {@link DepartmentsCodeName}
     */
    private List principals = new ScalableList(3);
    
    /**
     * 参数所指定的用户是否通过验证, 就是或者在用户列表中, 或者具有岗位列表中的一个岗位, 或者
     * 属于部门列表中的一个部门或下属部门, 只要有一个条件满足验证就算通过
     * @param user 要判断的用户
     * @return 如果用户满足定义的条件,或者没有定义条件返回<code>true</code>
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
     * 检索这个类型定义的访问者
     * @return 这个类型定义的访问者
     */
    public List getPrincipalTypes() {
        return this.principals;
    }
    
    /**
     * 把这个实例的内容写成<code>String</code>的格式, 如果这个实例没有定义责任者就返回<code>null</code>
     * @return 表现这个实例的<code>String</code>
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
     * 从表现一个<code>PrincipalTypes</code>的<code>String</code>创建实例
     * @param str 表现一个<code>PrincipalTypes</code>的<code>String</code>
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
                    throw new UnexpectedException("不能从[" + className + "]加载Class", e);
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
