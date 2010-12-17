/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project framework
 */
package net.silencily.sailing.framework.persistent.ibatis;

import java.sql.SQLException;

import com.ibatis.sqlmap.client.extensions.ParameterSetter;
import com.ibatis.sqlmap.client.extensions.ResultGetter;
import com.ibatis.sqlmap.client.extensions.TypeHandlerCallback;

import net.silencily.sailing.framework.persistent.hibernate3.entity.CodeWrapper;

/**
 * 业务实体中<code>CodeWrapper</code>类型处理器的<code>IBATIS</code>实现
 * @since 2006-10-24
 * @author scott
 * @version $Id: CodeWrapperTypeHandlerCallback.java,v 1.1 2010/12/10 10:54:25 silencily Exp $
 */
public class CodeWrapperTypeHandlerCallback implements TypeHandlerCallback {

    public Object valueOf(String str) {
        CodeWrapper cw = new CodeWrapper();
        cw.setCode(str);
        return cw;
    }

    public Object getResult(ResultGetter getter) throws SQLException {
        CodeWrapper codeWrapper = new CodeWrapper();
        codeWrapper.setCode(getter.getString());
        return codeWrapper;
    }

    public void setParameter(ParameterSetter setter, Object value) throws SQLException {
        String code = null;
        if (value != null && value instanceof CodeWrapper) {
            code = ((CodeWrapper) value).getCode();
        }
        setter.setString(code);
    }
}
