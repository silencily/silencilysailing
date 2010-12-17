package net.silencily.sailing.framework.web.struts;

import java.beans.IndexedPropertyDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.silencily.sailing.exception.ExceptionUtils;
import net.silencily.sailing.framework.persistent.BaseDto;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * �ų���װ��������ʱ�����Ŀ�Ԫ��
 * @author scott
 * @since 2006-4-4
 * @version $Id: ShrinkNullElements.java,v 1.1 2010/12/10 10:54:19 silencily Exp $
 */
public class ShrinkNullElements implements CustomRequestProcessorTemplate.ProcessPopulateExecutor {

    public void execute(HttpServletRequest request, HttpServletResponse response, ActionForm form, ActionMapping mapping) throws ServletException {
        if (form == null) {
            return;
        }
        /* �������Լ��� form-bean �и��� isShrink() �������� false ���� shrink ���� */
        boolean shrink = BaseActionForm.class.isInstance(form) && ((BaseActionForm) form).isShrink();
        if (!shrink) {
            return;
        }

        try {
            /* ����ʵ����˫����������޵ݹ�, ������ hibernate one-to-one ������ */
            Set recursive = new HashSet(5);
            recursiveShrink(form, recursive);
        } catch (Throwable e) {
            throw new ServletException(ExceptionUtils.getCauseMessage(e), ExceptionUtils.getActualThrowable(e));
        }
    }
    
    /**
     * �ݹ���ų����������е�<code>null</code>Ԫ��,<code>BaseDto</code>���͵Ķ��������ִ�в���
     * @param obj
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
     */
    private void recursiveShrink(Object value, Set recursive) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        PropertyDescriptor[] indexedDesc = BeanUtilsBean.getInstance().getPropertyUtils().getPropertyDescriptors(value);
        for (int i = 0; i < indexedDesc.length; i++) {
            /* ֻ�����ɶ���д������ */
            if (indexedDesc[i].getReadMethod() != null && indexedDesc[i].getReadMethod().invoke(value, null) != null && indexedDesc[i].getWriteMethod() != null) {
                Object obj = indexedDesc[i].getReadMethod().invoke(value, null);
                if (obj == null) {
                	continue;
                }              
                Object[] params = new Object[1];
                Method writer = indexedDesc[i].getWriteMethod();
                if (indexedDesc[i] instanceof IndexedPropertyDescriptor) {
                    if (obj.getClass().isArray()) {
                        List list = new ArrayList(((Object[]) obj).length);
                        list.addAll(Arrays.asList(((Object[]) obj)));
                        CollectionUtils.filter(list, nullPredicate);
                        params[0] = list.toArray((Object[]) Array.newInstance(((Object[]) obj).getClass().getComponentType(), list.size()));
                    } else if (obj instanceof Collection) {
                        Collection c = (Collection) obj;
                        CollectionUtils.filter(c, nullPredicate);
                        params[0] = c;
                    }
                    writer.invoke(value, params);
                } else if (BaseDto.class.isAssignableFrom(indexedDesc[i].getPropertyType())) {
                    /* ��һ��dot�д���һ��dto���Բ��������� dto ������˫�����, ��ô��Ҫ����ݹ� */
                    if (!recursive.contains(obj)) {
                        recursive.add(obj);
                        recursiveShrink(obj, recursive);
                    }
                    params[0] = obj;
                    writer.invoke(value, params);
                }
            }
        }
    }
    
    private NullElementPredicate nullPredicate = new NullElementPredicate();
    
    private static class NullElementPredicate implements Predicate {
        public boolean evaluate(Object element) {
            return element != null;
        }
    }
}
