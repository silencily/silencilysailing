/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project cardAssistant
 */
package net.silencily.sailing.framework.web.webwork.interceptor;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.util.Assert;

import com.opensymphony.xwork.ActionContext;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.XWorkMessages;
import com.opensymphony.xwork.interceptor.AroundInterceptor;
import com.opensymphony.xwork.interceptor.PreResultListener;
import com.opensymphony.xwork.util.LocalizedTextUtil;
import com.opensymphony.xwork.util.OgnlValueStack;

/**
 * <class>ConversionErrorByTypeInterceptor</class> �� {@link com.opensymphony.xwork.interceptor.ConversionErrorInterceptor}
 * �Ĳ�֮ͬ�������Ǹ��� Action �е� ����������ȡ������Ϣ, �� i18n �ļ����������ü��� : <p>
 *   
 *   invalid.fieldtype.[your class full name] = [your error message] <p>
 *   
 * �㲻��Ҫ���������Ƿ�����, {@link ConversionErrorByTypeInterceptor} �Ѿ����������ʹ���Ϊ����Ԫ������
 *   
 * @see com.opensymphony.xwork.interceptor.ConversionErrorInterceptor
 * @since 2005-9-23
 * @author ����
 * @version $Id: ConversionErrorByTypeInterceptor.java,v 1.1 2010/12/10 10:54:24 silencily Exp $
 */
public class ConversionErrorByTypeInterceptor extends AroundInterceptor {
    //~ Static fields/initializers /////////////////////////////////////////////

    public static final String ORIGINAL_PROPERTY_OVERRIDE = "original.property.override";
    
    public static final String CONVERSION_ERROR_BY_TYPE_PROPERTY_PREFIX = "invalid.fieldtype.";
    
    //~ Methods ////////////////////////////////////////////////////////////////
    //modify by Moxie,ȡ������ĵ�һ��Ԫ��
    protected Object getOverrideExpr(ActionInvocation invocation, Object value) {
    	if(value == null){
    		return value;
    	}
        return "'" + ((String[])value)[0] + "'";
    }

    protected void after(ActionInvocation dispatcher, String result) throws Exception {
    }

    protected void before(ActionInvocation invocation) throws Exception {
        ActionContext invocationContext = invocation.getInvocationContext();
        Map conversionErrors = invocationContext.getConversionErrors();
        OgnlValueStack stack = invocationContext.getValueStack();

        HashMap fakie = null;

        for (Iterator iterator = conversionErrors.entrySet().iterator(); iterator.hasNext();) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String propertyName = (String) entry.getKey();
            Object value = entry.getValue();

            if (shouldAddError(propertyName, value)) {
            	PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(invocation.getAction(), propertyName);
            	Class clazz = descriptor.getPropertyType();
            	
            	String elementClassName = clazz.getName();
            	if (clazz.isArray()) {
            		Assert.isTrue(elementClassName.indexOf("[") != -1, " Array Class Name must contains '[' ");
            		Assert.isTrue(elementClassName.indexOf(";") != -1,  " Array Class Name must contains ';' ");
            		elementClassName = elementClassName.substring(elementClassName.lastIndexOf("[") + 2, elementClassName.lastIndexOf(";"));
            	}
            	
            	
            	String message = getConversionErrorMessage(elementClassName, stack, value == null ? "" : ((String[])value)[0]);
                
                String addFieldErrorExpression = "addFieldError('" + propertyName + "','" + message + "')";
                stack.findValue(addFieldErrorExpression);

                if (fakie == null) {
                    fakie = new HashMap();
                }

                fakie.put(propertyName, getOverrideExpr(invocation, value));
            }
        }

        if (fakie != null) {
            // if there were some errors, put the original (fake) values in place right before the result
            stack.getContext().put(ORIGINAL_PROPERTY_OVERRIDE, fakie);
            invocation.addPreResultListener(new PreResultListener() {
                    public void beforeResult(ActionInvocation invocation, String resultCode) {
                        Map fakie = (Map) invocation.getInvocationContext().get(ORIGINAL_PROPERTY_OVERRIDE);

                        if (fakie != null) {
                            invocation.getStack().setExprOverrides(fakie);
                        }
                    }
                });
        }
    }
    
    public static String getConversionErrorMessage(String className, OgnlValueStack stack, Object value) {
        String defaultMessage = LocalizedTextUtil.findDefaultText(XWorkMessages.DEFAULT_INVALID_FIELDVALUE, ActionContext.getContext().getLocale(), new Object[] {
        		className
        	});
        String getTextExpression = "getText('" + CONVERSION_ERROR_BY_TYPE_PROPERTY_PREFIX + className + "','" + defaultMessage + "')";
        String message = (String) stack.findValue(getTextExpression);

        if (message == null) {
            message = defaultMessage;
        }

        return "�������ֵ [ " + value + " ] ����, ԭ���� : " + message;
    }
    


    protected boolean shouldAddError(String propertyName, Object value) {
        return true;
    }

}
