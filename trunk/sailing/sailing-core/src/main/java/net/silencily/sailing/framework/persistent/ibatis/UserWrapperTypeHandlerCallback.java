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
			throw new SQLException("�����ʹ������ת����");
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
     * ��Ϊ�ڼܹ����޷�֪��������ϵͳ, �����������ֱ�������˰�ȫ�ı�, ���Ժ��ع��� commons 
     * ģ����, �Ա����������˵�� bug
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
                logger.info("�����û��ʺ�[" + account + "]������������,��ʹ���ʺŴ�������", e);
            }
            name = account;
        }
        return name;
    }
}
