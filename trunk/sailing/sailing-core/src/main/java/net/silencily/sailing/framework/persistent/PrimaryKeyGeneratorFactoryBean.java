package net.silencily.sailing.framework.persistent;

import net.silencily.sailing.utils.UUIDGenerator;

import org.springframework.beans.factory.FactoryBean;

/**
 * �������ɳ־û���ʹ�õ�����, ���ɵ�������<code>String</code>����, ������<b>32</b>λ, ʵ��
 * �����ɲ���ʹ��<code>UUIDGenerator</code>
 * @author scott
 * @since 2006-3-28
 * @version $Id: PrimaryKeyGeneratorFactoryBean.java,v 1.1 2010/12/10 10:54:27 silencily Exp $
 *
 */
public class PrimaryKeyGeneratorFactoryBean implements FactoryBean {

    public Object getObject() throws Exception {
        return (String) new UUIDGenerator().generate();
    }

    public Class getObjectType() {
        return String.class;
    }

    public boolean isSingleton() {
        return false;
    }
}
