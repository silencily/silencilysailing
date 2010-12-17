/*jadclipse*/// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) radix(10) lradix(10) 
package net.silencily.sailing.basic.wf.function;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.silencily.sailing.common.dbtime.DBTime;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.validation.DataBinder;

import com.opensymphony.module.propertyset.PropertySet;

public class PropertySetterFunction extends FunctionTemlate
{

    public PropertySetterFunction()
    {
        pattern = Pattern.compile("\\{([a-zA-Z0-9\\.\\-_]+)\\}");
    }

    protected void doExecute(Map transientVars, Map args, PropertySet ps)
        throws Throwable
    {
        Object bean = transientVars.get("dto");
        if(bean == null)
            return;
        Map mapping = new HashMap(2);
        mapping.put("user", transientVars.get("current.user"));
        mapping.put("datetime", getCurrentDateTime());
        Iterator it = args.entrySet().iterator();
        do
        {
            if(!it.hasNext())
                break;
            java.util.Map.Entry entry = (java.util.Map.Entry)it.next();
            String propertyName = (String)entry.getKey();
            String placeholder = (String)entry.getValue();
            if(!"class.name".equals(propertyName) && !StringUtils.isBlank(propertyName))
            {
                Object value = null;
                if(StringUtils.isNotBlank(placeholder))
                {
                    Matcher matcher = pattern.matcher(placeholder);
                    if(matcher.matches())
                        value = parsesValue(transientVars, mapping, matcher.group(1));
                    else
                        value = placeholder;
                    setPropertyValue(bean, propertyName, value);
                }
            }
        } while(true);
    }

    private Object parsesValue(Map transientVars, Map mapping, String placeholder)
    {
        String key = placeholder;
        String remainder = null;
        Object value = null;
        int dotPos = placeholder.indexOf('.');
        if(dotPos > -1)
        {
            key = placeholder.substring(0, dotPos);
            remainder = placeholder.substring(dotPos + 1);
        }
        if(mapping.containsKey(key))
            value = mapping.get(key);
        else
        if(transientVars.containsKey(key))
            value = transientVars.get(key);
        else
            throw new IllegalArgumentException("\u4E0D\u80FD\u89E3\u6790\u914D\u7F6E[" + placeholder + "],\u65E0\u6CD5\u627E\u5230key[" + key + "]\u5BF9\u5E94\u7684\u6570\u636E");
        if(remainder != null)
            value = getPropertyValue(value, remainder);
        return value;
    }

    private void setPropertyValue(Object bean, String propertyName, Object value)
    {
        MutablePropertyValues pvs = new MutablePropertyValues();
        pvs.addPropertyValue(propertyName, value);
        DataBinder binder = new DataBinder(bean);
        binder.bind(pvs);
    }

    private Object getPropertyValue(Object bean, String propertyName)
    {
        BeanWrapper bw = new BeanWrapperImpl(bean);
        return bw.getPropertyValue(propertyName);
    }

    private Date getCurrentDateTime()
    {
        return DBTime.getDBTime();
    }

    public static final String KEY_USER = "user";
    public static final String KEY_DATETIME = "datetime";
    private static final String CLASS_NAME = "class.name";
    private Pattern pattern;
}
