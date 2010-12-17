package net.silencily.sailing.framework.business.principals;

import java.lang.reflect.Constructor;

import net.silencily.sailing.exception.UnexpectedException;
import net.silencily.sailing.framework.authentication.entity.Principal;
import net.silencily.sailing.framework.authentication.entity.User;

/**
 * ��������һ��������, �����ǲ���, ��λ��������Ա, ��������{@link Principal}������
 * @author zhangli
 * @version $Id: PrincipalType.java,v 1.1 2010/12/10 10:54:21 silencily Exp $
 * @since 2007-6-7
 */
public class PrincipalType implements Principal {
    
    /** �����{@link Principal}������ */
    private Class type;
    
    /** �����ߵĴ��� */
    private String code;
    
    /** ���������� */
    private String name;
    
    private Principal principal;
    
    public PrincipalType() {
        super();
    }
    
    public PrincipalType(String code, String name, Class type) {
        this.code = code;
        this.name = name;
        this.type = type;
    }

    public boolean pass(User user) {
        return getPrincipal().pass(user);
    }
    
    public Principal getPrincipal() {
        if (principal == null) {
            try {
                Constructor constructor = type.getConstructor(new Class[] {String.class, String.class});
                principal = (Principal) constructor.newInstance(new String[] {code, name});
            } catch (NullPointerException e) {
                throw new NullPointerException("����PrincipalType��Principalʱû������type����");
            } catch (Exception e) {
                throw new UnexpectedException("����ͨ��������(String,String)ʵ��������[" + type + "]", e);
            }
        }
        return principal;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }
    
    public int hashCode() {
        int c = getClass().hashCode();
        if (type == null) {
            c = super.hashCode();
        } else {
            c = c * 29 + getPrincipal().hashCode();
        }
        return c;
    }
    
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!PrincipalType.class.isInstance(o)) {
            return false;
        }
        PrincipalType p = (PrincipalType) o;
        if (type == null || p.type == null) {
            return false;
        }
        return getPrincipal().equals(p.getPrincipal());
    }
}
