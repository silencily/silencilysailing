package net.silencily.sailing.basic.wf.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.opensymphony.workflow.spi.SimpleStep;

public class SimpleStepWithSavedValues extends SimpleStep
{

    public SimpleStepWithSavedValues()
    {
        values = new ArrayList(30);
    }

    public List getValues()
    {
        return values;
    }

    public void setValues(List values)
    {
        this.values = values;
    }

    public Map getOriginalKeyAndValue()
    {
        Map ret = new HashMap(values.size());
        Iterator it = values.iterator();
        do
        {
            if(!it.hasNext())
                break;
            Map map = (Map)it.next();
            String k = (String)map.get("key");
            String v = (String)map.get("value");
            Matcher matcher = pattern.matcher(k);
            if(matcher.matches())
            {
                String stepId = matcher.group(1);
                String key = matcher.group(2);
                if(String.valueOf(getId()).equals(stepId))
                    ret.put(key, v);
            } else
            if((matcher = opinionPattern.matcher(k)).matches())
            {
                if(String.valueOf(getId()).equals(matcher.group(1)))
                    ret.put("opinion", v);
            } else
            {
                ret.put(k, v);
            }
        } while(true);
        return ret;
    }

    private static Pattern pattern = Pattern.compile("^([\\d]+)\\.(.+)");
    private static Pattern opinionPattern = Pattern.compile("opinion\\.([\\d]+)$");
    private List values;

}
