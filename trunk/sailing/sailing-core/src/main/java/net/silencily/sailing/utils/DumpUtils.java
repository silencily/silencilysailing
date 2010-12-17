package net.silencily.sailing.utils;

import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.silencily.sailing.exception.ExceptionUtils;

import org.apache.commons.beanutils.BeanUtilsBean;

/**
 * һ��ִ�и�������ת����<code>Utility</code>, Work in Processing.
 * 
 * @author scott
 * @since 2005-12-24
 * @version $Id: DumpUtils.java,v 1.1 2010/12/10 10:54:21 silencily Exp $
 *
 */
public abstract class DumpUtils {
    private static Pattern pattern = Pattern.compile("([^=]+)\\s*=\\s*(.*)");
    
    /**
     * <p>����������ȡ����, ����<code>..=...</code>�ĸ�ʽ, �ѵ�һ��"="��ǰ���ַ�����Ϊ
     * <code>Map's key</code>, ����Ľ���Ϊ<code>Map's value</code></p>
     * <p>�����ʹ������ƽ̨���ַ�����, ���������ȷ�ı��뺺��, ������ϵͳ�Ĳ�������</p>
     *  
     * @param in ������Ϊ�ַ�������������Դ, �����ı���ʽ��<code>text, properties</code>
     * @return �������<code>Map</code>
     * @throws IllegalStateException �����ȡ���ݷ�������
     * @throws IllegalArgumentException ���������<code>null</code>
     */
    public static Map readMapFromInputStream(InputStream in) throws IllegalStateException {
        if (in == null) {
            throw new IllegalArgumentException("����������ȡMap���������null");
        }
        Map ret = new HashMap();
        String line = null;
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        
        try {
            while ((line = br.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.matches()) {
                    String key = matcher.group(1);
                    String value = matcher.group(2);
                    ret.put(key, value);
                }
            }
        } catch (IOException e) {
            IllegalStateException ex = 
                new IllegalStateException("����������ȡMap����," + e.getMessage());
            ex.initCause(e);
            throw ex;
        }
        
        return ret;
    }
    
    /**
     * <p>����һ�������, ��<code>Properties</code>��ʽд<code>map</code>�����ݵ�����������
     * , <code>Map's key</code>��<code>Properties's key</code>, <code>Map's value</code>
     * ��<code>Properties's value</code></p>
     * 
     * @param map Ҫ�����<code>Map</code>
     * @throws IllegalStateException ���д���ݷ�������
     * @throws IllegalArgumentException ����κ�һ��������<code>null</code>
     */
    public static void writeMapToOutputStream(Map map, OutputStream out) throws IllegalStateException {
        if (map == null || out == null) {
            throw new IllegalArgumentException("дMap�����ݵ������ʱ���������null");
        }
        
        if (map.size() == 0) {
            return;
        }
        
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));

        try {
            for (Iterator it = map.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry entry = (Map.Entry) it.next();
                
                String key = String.valueOf(entry.getKey());
                Object obj = entry.getValue();
                String value = null;
                if (obj == null) {
                    value = "";
                } else {
                    value = String.valueOf(obj);
                }

                writer.write(key + "=" + value);
                writer.newLine();
            }
            
            writer.flush();
        } catch (IOException e) {
            IllegalStateException ex = new IllegalStateException(
                "дMap���ݵ�Properties��ʽ������������쳣," + e.getMessage());
            ex.initCause(e);
            throw ex;
        }
    }
    
    /**
     * ����һ��������ַ�����ʾ, ��������<code>domain object</code>��<code>toString</code>��,
     * <b>��������<code>struts</code>��Ϊǰ�˵�Ӧ����</b>, ʹ��<code>struts</code>�����
     * ����Ϊ�����Ѿ�������ͨ��<code>web</code>��Ķ��������˻���
     * @param o  Ҫת����<code>domain object</code>����
     * @return ����һ���������е�����ֵ���ַ���
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static String classToString(Object o) 
        throws IllegalArgumentException, IllegalAccessException {
        
        StringBuffer sb = new StringBuffer();
        try {
            PropertyDescriptor[] descs = BeanUtilsBean.getInstance().getPropertyUtils().getPropertyDescriptors(o.getClass());
            String separator = System.getProperty("line.separator");
            for (int i = 0; i < descs.length; i++) {
                if (descs[i].getReadMethod() != null) {
                    if (sb.length() > 0) {
                        sb.append(separator);
                    }
                    sb.append(descs[i].getName()).append("=").append(descs[i].getReadMethod().invoke(o, null));
                }
            }
        } catch (InvocationTargetException e) {
        	throw new RuntimeException(ExceptionUtils.getActualThrowable(e.getTargetException()));
		}
        
        return sb.toString();
    }
    
    /**
     * ����һ��<code>bean</code>��<code>PropertyDescriptor</code>, ͨ�����<code>bean</code>
     * ��<code>domain object</code>, Ҳ����<code>DTO</code>
     * @param bean Ҫ�������Ե�<code>bean</code>ʵ��
     * @return ���������������
     */
    public static PropertyDescriptor[] getPropertyDescriptorsForBean(Object bean) {
        return BeanUtilsBean.getInstance().getPropertyUtils().getPropertyDescriptors(bean);
    }

}

