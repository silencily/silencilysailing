package net.silencily.sailing.framework.persistent.ibatis;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

import com.ibatis.sqlmap.client.extensions.ParameterSetter;
import com.ibatis.sqlmap.client.extensions.ResultGetter;
import com.ibatis.sqlmap.client.extensions.TypeHandlerCallback;

/**
 * 用于<code>MySql</code>数据库转换正确地保存和检索汉字, 配置在<code>system-sqlmap-config.xml</code>
 * 中
 * @author scott
 * @since 2006-4-10
 * @version $Id: MysqlStringTypeHandlerCallback.java,v 1.1 2010/12/10 10:54:25 silencily Exp $
 *
 */
public class MysqlStringTypeHandlerCallback implements TypeHandlerCallback {
    private static String APP_ENCODING = "GBK";
    private static String MYSQL_ENCODING = "8859_1";

    public void setParameter(ParameterSetter setter, Object parameter)
        throws SQLException {
        
        if (parameter == null) {
            setter.setString((String) null);
        } else {
            String s = (String) parameter;
            try {
                setter.setString(new String(s.getBytes(APP_ENCODING), MYSQL_ENCODING));
            } catch (Exception e) {
                throw handle(e, s);
            }
        }
    }

    public Object getResult(ResultGetter getter) throws SQLException {
        String s = getter.getString();
        String ret = s;
        if (s != null) {
            try {
                ret = new String(s.getBytes(MYSQL_ENCODING), APP_ENCODING);
            } catch (UnsupportedEncodingException e) {
                throw handle(e, s);
            }
        }

        return ret;
    }
    
    private IllegalStateException handle(Exception e, String value) {
        IllegalStateException ex = new IllegalStateException(
            "不支持编码类型[" + MYSQL_ENCODING + "],要编码的参数[" + value + "]");
        ex.initCause(e);
        throw ex;
    }

    public Object valueOf(String s) {
        return s;
    }

}
