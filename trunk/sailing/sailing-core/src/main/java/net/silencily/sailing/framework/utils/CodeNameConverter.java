package net.silencily.sailing.framework.utils;

import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.PropertyUtils;

import net.silencily.sailing.exception.UnexpectedException;
import net.silencily.sailing.framework.codename.CodeName;

/**
 * 把页面的代码转成<code>CodeName</code>
 * @author zhangli
 * @version $Id: CodeNameConverter.java,v 1.1 2010/12/10 10:54:19 silencily Exp $
 * @since 2007-4-20
 */
public class CodeNameConverter implements Converter {

    public Object convert(Class type, Object value) {
        CodeName cn = null;
        try {
            cn = (CodeName) type.newInstance();
            PropertyUtils.setProperty(cn, "code", value);
        } catch (Exception e) {
            throw new UnexpectedException("不能创建[" + type.getName() + "]的实例", e);
        }
        return cn;
    }
}
