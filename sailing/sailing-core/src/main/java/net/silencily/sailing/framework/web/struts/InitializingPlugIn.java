package net.silencily.sailing.framework.web.struts;

import java.beans.IndexedPropertyDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;

import net.silencily.sailing.framework.codename.CodeName;
import net.silencily.sailing.framework.codename.EnumerationCodeName;
import net.silencily.sailing.framework.persistent.filter.Column;
import net.silencily.sailing.framework.utils.CodeNameConverter;
import net.silencily.sailing.framework.utils.ColumnConverter;
import net.silencily.sailing.framework.utils.DateConverter;
import net.silencily.sailing.framework.utils.EnumerationConverter;
import net.silencily.sailing.framework.utils.SqlDateConverter;
import net.silencily.sailing.framework.utils.SqlTimestampConverter;
import net.silencily.sailing.framework.utils.StringConverter;
import net.silencily.sailing.framework.web.struts.support.ExtendableIndexedProperty;
import net.silencily.sailing.framework.web.struts.support.ListBasedExtendableIndexedProperty;
import net.silencily.sailing.framework.web.struts.support.SimpleExtendableIndexedProperty;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;

/**
 * 初始化<code>web tier</code>的<code>utility</code>组件
 * @author scott
 * @since 2006-4-5
 * @version $Id: InitializingPlugIn.java,v 1.1 2010/12/10 10:54:19 silencily Exp $
 *
 */
public class InitializingPlugIn implements PlugIn {
    /*
    private Pattern pattern = Pattern.compile("([^\\[]+)\\[([0-9]+)\\]");
    */

    public void init(ActionServlet servlet, ModuleConfig config) throws ServletException {
        BeanUtilsBean bean = new BeanUtilsBean(customConvertUtilsBean(), customPropertyUtilsBean());
        BeanUtilsBean.setInstance(bean);
        registryConverters(BeanUtilsBean.getInstance().getConvertUtils());
        /* shrink the null elements on indexed properties */
        CustomRequestProcessorTemplate.registry(new ShrinkNullElements());
        /* populate the parameters for ContextInfo */
        CustomRequestProcessorTemplate.registry(new RetrieveParametersForContextInfo());
    }

    public void destroy() {
        BeanUtilsBean.getInstance().getConvertUtils().deregister(String.class);
        BeanUtilsBean.getInstance().getConvertUtils().deregister(Integer.class);
        BeanUtilsBean.getInstance().getConvertUtils().deregister(Long.class);
        BeanUtilsBean.getInstance().getConvertUtils().deregister(Double.class);
        BeanUtilsBean.getInstance().getConvertUtils().deregister(BigDecimal.class);      
        CustomRequestProcessorTemplate.deregistry();
    }
    
    private ConvertUtilsBean customConvertUtilsBean() {
        ConvertUtilsBean convertUtilsBean = new ConvertUtilsBean();
        registryConverters(convertUtilsBean);
        return convertUtilsBean;
    }
    
    /**
     * 注册参数值类型转换器, 只要转换器类型是<code>Converter</code>并且位于这个类下<code>
     * support</code>子包中就做为转换器注册
     * @param convertUtilsBean
     */
    protected void registryConverters(ConvertUtilsBean convertUtilsBean) {
        convertUtilsBean.register(new StringConverter(), String.class);
        convertUtilsBean.register(new DateConverter(), Date.class);
        convertUtilsBean.register(new SqlDateConverter(), java.sql.Date.class);
        convertUtilsBean.register(new SqlTimestampConverter(), Timestamp.class);
        convertUtilsBean.register(new ColumnConverter(), Column.class);
        convertUtilsBean.register(new CodeNameConverter(), CodeName.class);
        convertUtilsBean.register(new EnumerationConverter(), EnumerationCodeName.class);
        
        // 注册默认值为 null 的类型转换器
        convertUtilsBean.register(new IntegerConverter(null), Integer.class);
        convertUtilsBean.register(new LongConverter(null), Long.class);
        convertUtilsBean.register(new DoubleConverter(null), Double.class);
        convertUtilsBean.register(new BigDecimalConverter(null), BigDecimal.class);
    }
    
    private PropertyUtilsBean customPropertyUtilsBean() {
        /* 这个类正确工作的要求是 form-bean 中的索引属性必须严格按照 java bean 规范的一组属性四个方法 */
        return new PropertyUtilsBean() {
            private ExtendableIndexedProperty extendableIndexedProperty;
            private ListBasedExtendableIndexedProperty listBasedExtendableIndexedProperty;

            {
                extendableIndexedProperty = new SimpleExtendableIndexedProperty(this);
                listBasedExtendableIndexedProperty = new ListBasedExtendableIndexedProperty();
            }

            /* 这个方法用于解决 form-bean 的 IndexedProperty 时动态扩展 List 或数组, 必须定义 getList() 和 getList(int) 一对方法才能应用这种机制 */
            public Object getIndexedProperty(Object bean, String name, int index) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
                PropertyDescriptor desc = getPropertyDescriptor(bean, name);
                if (desc instanceof IndexedPropertyDescriptor && desc.getWriteMethod() != null) {
                    extendableIndexedProperty.extend(bean, name, index);
                    Method reader = ((IndexedPropertyDescriptor) desc).getIndexedReadMethod();
                    return reader.invoke(bean, new Object[] {new Integer(index)});
                } else {
                    Method method = listBasedExtendableIndexedProperty.getReadMethod(bean, name);
                    if (method != null && List.class.isAssignableFrom(method.getReturnType())) {
                        listBasedExtendableIndexedProperty.extend(bean, name, index);
                        Method m = listBasedExtendableIndexedProperty.getIndexedReadMethod(bean, name);
                        if (m != null) {
                            return m.invoke(bean, new Integer[] {new Integer(index)});
                        }
                        return null;
                    }
                }
                
                return super.getIndexedProperty(bean, name, index);
            }
            /*
            public Object getIndexedProperty(Object bean, String name) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
                Matcher matcher = pattern.matcher(name);
                if (matcher.matches()) {
                    String propName = matcher.group(1);
                    Integer index = Integer.valueOf(matcher.group(2));
                    PropertyDescriptor desc = getPropertyDescriptor(bean, propName);
                    if (List.class.isAssignableFrom(desc.getReadMethod().getReturnType()) || (desc instanceof IndexedPropertyDescriptor && desc.getWriteMethod() != null)) {
                        extendableIndexedProperty.extend(bean, propName, index.intValue());
                        Method reader = ((IndexedPropertyDescriptor) desc).getIndexedReadMethod();
                        return reader.invoke(bean, new Object[] {index});
                    }
                }
                
                return super.getIndexedProperty(bean, name);
            }
            */
            
            /*
             * 这个方法解决 struts's mapped property 的不足, 处理没有被 PropertyUtils 
             * 解释为 MappedProperty 的属性的检索
             */
            public Object getMappedProperty(Object bean, String name, String key) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
                Object ret = super.getMappedProperty(bean, name, key);
                if (ret == null) {
                    Method m = getGetterWidthParameter(bean, name);
                    if (m != null) {
                        ret = m.invoke(bean, new Object[] {key});
                    }
                }
                return ret;
            }
            
            /* 返回指定属性的<tt>getXxxx(String)</tt>类型的方法 */
            private Method getGetterWidthParameter(Object bean, String name) {
                String methodName = null;
                if (name.length() > 1) {
                    methodName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
                } else {
                    methodName = "get" + name.toUpperCase();
                }
                
                Method m = null;
                try {
                    m = bean.getClass().getMethod(methodName, new Class[] {String.class});
                } catch (Exception e) {
                    m = null;
                }
                
                return m;
            }
        };
    }
}
