/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project framework
 */
package net.silencily.sailing.framework.persistent.ibatis;

import java.sql.SQLException;
import java.sql.Types;

import net.silencily.sailing.framework.persistent.hibernate3.entity.UserWrapper;
import net.silencily.sailing.framework.utils.DaoHelper;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;

import com.ibatis.sqlmap.client.extensions.ParameterSetter;
import com.ibatis.sqlmap.client.extensions.ResultGetter;
import com.ibatis.sqlmap.client.extensions.TypeHandlerCallback;

/**
 * Type Handler between {@link UserWrapper} and varchar
 * @since 2006-8-10
 * @author java2enterprise
 * @version $Id: UserWrapperTypeHandlerCallback.java,v 1.1 2010/12/10 10:54:25 silencily Exp $
 */
public class UserWrapperTypeHandlerCallback implements TypeHandlerCallback {
    private static final String loadChinesenameByUsername = "select chinese_name from security_user where username = ?";
    private static int[] paramTypes = new int[] {Types.VARCHAR};
    private static Log logger = LogFactory.getLog(UserWrapperTypeHandlerCallback.class);

	public Object getResult(ResultGetter getter) throws SQLException {
		String value = getter.getString();
        return createFromUsername(value);
	}

	public void setParameter(ParameterSetter setter, Object parameter) throws SQLException {		
		if (parameter == null) {
			setter.setString(null);
		} else if (UserWrapper.class.isInstance(parameter)) {
			UserWrapper userWrapper = (UserWrapper) parameter;
			setter.setString(userWrapper.getUsername());
		} else {
			throw new SQLException("错误地使用类型转换器");
		}
	}

	public Object valueOf(String s) {
        return createFromUsername(s);
	}
	
    private UserWrapper createFromUsername(String username) {
        UserWrapper userWrapper = new UserWrapper();
        userWrapper.setUsername(username);
        userWrapper.setChineseName(loadUsername(username));
        return userWrapper;
    }
    
    /*
     * 因为在架构中无法知道其它的系统, 所以这个方法直接引用了安全的表, 在以后重构到 commons 
     * 模块中, 以避免这个可以说是 bug
     */
    private String loadUsername(String account) {
        if (StringUtils.isBlank(account)) {
            return null;
        }
        String name = null;
        try {
            name = (String) DaoHelper
                .getJdbcTemplate()
                .queryForObject(loadChinesenameByUsername, new String[] {account}, paramTypes, String.class);
        } catch (DataAccessException e) {
            if (logger.isInfoEnabled()) {
                logger.info("根据用户帐号[" + account + "]检索姓名错误,将使用帐号代替姓名", e);
            }
            name = account;
        }
        return name;
    }
}
