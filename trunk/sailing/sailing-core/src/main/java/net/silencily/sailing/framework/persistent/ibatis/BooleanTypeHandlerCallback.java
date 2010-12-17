package net.silencily.sailing.framework.persistent.ibatis;

import java.sql.SQLException;
import java.util.Arrays;

import com.ibatis.sqlmap.client.extensions.ParameterSetter;
import com.ibatis.sqlmap.client.extensions.ResultGetter;
import com.ibatis.sqlmap.client.extensions.TypeHandlerCallback;

/**
 * 处理<code>domain object</code>的<code>boolean</code>类型的属性与数据库<code>VARCHAR</code>
 * 类型之间的转换, 在一些数据库中没有<code>boolean</code>类型(比如:<code>ORACLE</code>), 
 * 为了不依赖于特定的数据库产品, 所以在数据库设计中所有的<code>boolean</code>类型的列都使用
 * <code>VARCHAR</code>来表示. 这个类用于在<code>IBATIS</code>中处理这种转换. 应当注意的
 * 是在<code>domain object</code>中所有的布尔值必须使用<code>java.lang.Boolean</code>,
 * 原始类型在<code>IBATIS</code>中无法处理这种转换
 * @author scott
 * @since 2006-3-26
 * @version $Id: BooleanTypeHandlerCallback.java,v 1.1 2010/12/10 10:54:25 silencily Exp $
 * TODO: 抛出SQLException 时使用 error code
 */
public class BooleanTypeHandlerCallback implements TypeHandlerCallback {
    private static String[] TRUE_STRING = {"yes", "true", "1"};
    
    private static String[] FALSE_STRING = {"no", "false", "0"};
    
    private static String TRUE = "yes";
    
    private static String FALSE = "no";
    
    static {
        Arrays.sort(TRUE_STRING);
        Arrays.sort(FALSE_STRING);
    }

    public void setParameter(ParameterSetter setter, Object parameter) throws SQLException {
        if (parameter == null) {
            setter.setString((String) null);
        } else if (parameter instanceof Boolean) {
            Boolean b = (Boolean) parameter;
            if (b.booleanValue()) {
                setter.setString(TRUE);
            } else {
                setter.setString(FALSE);
            }
        } else {
            throw new SQLException("错误地使用类型转换器");
        }
    }

    public Object getResult(ResultGetter getter) throws SQLException {
        String value = getter.getString();
        Boolean ret = null;
        if (value == null) {
            //
        } else if (Arrays.binarySearch(TRUE_STRING, value.toLowerCase()) > -1) {
            ret = Boolean.TRUE;
        } else if (Arrays.binarySearch(FALSE_STRING, value.toLowerCase()) > -1) {
            ret = Boolean.FALSE;
        } else {
            throw new SQLException("数据库中boolean值错误[" + value + "],只应该是yes/no,true/false.1/0");
        }
        
        return ret;
    }

    public Object valueOf(String s) {
        Boolean ret = null;
        if (s != null) {
            String value = s.toLowerCase();
            if (Arrays.binarySearch(TRUE_STRING, value) > -1) {
                ret = Boolean.TRUE;
            } else if (Arrays.binarySearch(FALSE_STRING, value) > -1) {
                ret = Boolean.FALSE;
            }
        }

        return ret;
    }
}
