package net.silencily.sailing.framework.persistent;

import net.silencily.sailing.utils.UUIDGenerator;

import org.springframework.beans.factory.FactoryBean;

/**
 * 用于生成持久化层使用的主键, 生成的主键是<code>String</code>类型, 长度是<b>32</b>位, 实际
 * 的生成策略使用<code>UUIDGenerator</code>
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
