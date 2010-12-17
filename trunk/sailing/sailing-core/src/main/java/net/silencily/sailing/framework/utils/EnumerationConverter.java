package net.silencily.sailing.framework.utils;

import java.lang.reflect.Method;

import org.apache.commons.beanutils.Converter;

import net.silencily.sailing.exception.UnexpectedException;
import net.silencily.sailing.framework.codename.EnumerationCodeName;

/**
 * ����{@link EnumerationCodeName}���͵�����
 * @author zhangli
 * @version $Id: EnumerationConverter.java,v 1.1 2010/12/10 10:54:19 silencily Exp $
 * @since 2007-5-1
 */
public class EnumerationConverter implements Converter {
    public Object convert(Class type, Object value) {
        try {
            Method method = type.getMethod(EnumerationCodeName.METHOD_NAME, EnumerationCodeName.METHOD_PARAMETERS);
            EnumerationCodeName cn = (EnumerationCodeName) method.invoke(null, new String[] {(String) value});
            return cn;
        } catch (Exception e) {
            throw new UnexpectedException("ִ��" + type.getName() + "��ת��ʱ����,���������ʵ��", e);
        }
    }
}
