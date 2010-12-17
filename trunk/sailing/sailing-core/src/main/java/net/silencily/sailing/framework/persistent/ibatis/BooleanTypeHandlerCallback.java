package net.silencily.sailing.framework.persistent.ibatis;

import java.sql.SQLException;
import java.util.Arrays;

import com.ibatis.sqlmap.client.extensions.ParameterSetter;
import com.ibatis.sqlmap.client.extensions.ResultGetter;
import com.ibatis.sqlmap.client.extensions.TypeHandlerCallback;

/**
 * ����<code>domain object</code>��<code>boolean</code>���͵����������ݿ�<code>VARCHAR</code>
 * ����֮���ת��, ��һЩ���ݿ���û��<code>boolean</code>����(����:<code>ORACLE</code>), 
 * Ϊ�˲��������ض������ݿ��Ʒ, ���������ݿ���������е�<code>boolean</code>���͵��ж�ʹ��
 * <code>VARCHAR</code>����ʾ. �����������<code>IBATIS</code>�д�������ת��. Ӧ��ע���
 * ����<code>domain object</code>�����еĲ���ֵ����ʹ��<code>java.lang.Boolean</code>,
 * ԭʼ������<code>IBATIS</code>���޷���������ת��
 * @author scott
 * @since 2006-3-26
 * @version $Id: BooleanTypeHandlerCallback.java,v 1.1 2010/12/10 10:54:25 silencily Exp $
 * TODO: �׳�SQLException ʱʹ�� error code
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
            throw new SQLException("�����ʹ������ת����");
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
            throw new SQLException("���ݿ���booleanֵ����[" + value + "],ֻӦ����yes/no,true/false.1/0");
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
