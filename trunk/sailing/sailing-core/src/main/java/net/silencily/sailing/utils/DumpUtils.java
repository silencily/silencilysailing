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
 * 一个执行各种数据转储的<code>Utility</code>, Work in Processing.
 * 
 * @author scott
 * @since 2005-12-24
 * @version $Id: DumpUtils.java,v 1.1 2010/12/10 10:54:21 silencily Exp $
 *
 */
public abstract class DumpUtils {
    private static Pattern pattern = Pattern.compile("([^=]+)\\s*=\\s*(.*)");
    
    /**
     * <p>从输入流读取数据, 按照<code>..=...</code>的格式, 把第一个"="号前面字符解析为
     * <code>Map's key</code>, 后面的解析为<code>Map's value</code></p>
     * <p>这个类使用运行平台的字符编码, 如果不能正确的编码汉字, 检查操作系统的参数设置</p>
     *  
     * @param in 可以作为字符输入流的输入源, 比如文本格式的<code>text, properties</code>
     * @return 解析后的<code>Map</code>
     * @throws IllegalStateException 如果读取数据发生错误
     * @throws IllegalArgumentException 如果输入是<code>null</code>
     */
    public static Map readMapFromInputStream(InputStream in) throws IllegalStateException {
        if (in == null) {
            throw new IllegalArgumentException("从输入流读取Map输入参数是null");
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
                new IllegalStateException("从输入流读取Map错误," + e.getMessage());
            ex.initCause(e);
            throw ex;
        }
        
        return ret;
    }
    
    /**
     * <p>给定一个输出流, 以<code>Properties</code>格式写<code>map</code>的内容到这个输出流中
     * , <code>Map's key</code>是<code>Properties's key</code>, <code>Map's value</code>
     * 是<code>Properties's value</code></p>
     * 
     * @param map 要输入的<code>Map</code>
     * @throws IllegalStateException 如果写数据发生错误
     * @throws IllegalArgumentException 如果任何一个输入是<code>null</code>
     */
    public static void writeMapToOutputStream(Map map, OutputStream out) throws IllegalStateException {
        if (map == null || out == null) {
            throw new IllegalArgumentException("写Map的内容到输出流时输入参数是null");
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
                "写Map数据到Properties格式的输出流发生异常," + e.getMessage());
            ex.initCause(e);
            throw ex;
        }
    }
    
    /**
     * 返回一个对象的字符串表示, 典型用于<code>domain object</code>的<code>toString</code>中,
     * <b>仅适用于<code>struts</code>作为前端的应用中</b>, 使用<code>struts</code>的组件
     * 是因为那里已经对所有通过<code>web</code>层的对象反射做了缓存
     * @param o  要转换的<code>domain object</code>对象
     * @return 包含一个对象所有的域及其值的字符串
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
     * 返回一个<code>bean</code>的<code>PropertyDescriptor</code>, 通常这个<code>bean</code>
     * 是<code>domain object</code>, 也就是<code>DTO</code>
     * @param bean 要解析属性的<code>bean</code>实例
     * @return 解析后的属性描述
     */
    public static PropertyDescriptor[] getPropertyDescriptorsForBean(Object bean) {
        return BeanUtilsBean.getInstance().getPropertyUtils().getPropertyDescriptors(bean);
    }

}

