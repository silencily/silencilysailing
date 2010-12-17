package com.power.vfs;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class FileObject implements Serializable, Comparable{
	public FileObject()
    {
        properties = new HashMap();
    }

    public FileObject(String name)
    {
        properties = new HashMap();
        this.name = name;
    }

    public FileObject(String name, String type)
    {
        properties = new HashMap();
        this.name = name;
        this.type = type;
    }

    public boolean isDirectory()
    {
        return "directory".equals(type);
    }

    public String getName()
    {
        if(StringUtils.isNotBlank(name) && !name.startsWith("/"))
            name = "/" + name;
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Object getProperty(String key)
    {
        return properties.get(key);
    }

    public Map getProperties()
    {
        return properties;
    }

    void addProperty(String key, Object value)
    {
        if(key != null && value != null)
            properties.put(key, value);
    }

    void setProperties(Map properties)
    {
        this.properties = properties;
    }

    public String getPath()
    {
        String n = getName();
        if(StringUtils.isBlank(n))
            throw new FileObjectException("\u6587\u4EF6\u5B9E\u4F53\u6CA1\u6709\u540D\u79F0", new IllegalArgumentException("\u6587\u4EF6\u5B9E\u4F53\u6CA1\u6709\u540D\u79F0"));
        if("/".equals(n))
            return n;
        int countOfSlash = StringUtils.countMatches(n, "/");
        if(countOfSlash == 1)
        {
            return "/";
        } else
        {
            int pos = n.lastIndexOf('/');
            return n.substring(0, pos);
        }
    }

    public String getFileName()
    {
        String ret = name;
        if(name != null)
        {
            int pos = name.lastIndexOf('/');
            if(pos > -1)
                ret = name.substring(pos + 1);
        }
        return ret;
    }

    public String getType()
    {
        if(type != null)
            return type;
        String ret = null;
        if(StringUtils.isNotBlank(getFileName()))
        {
            int dot = getFileName().lastIndexOf('.');
            if(dot > -1)
                ret = getFileName().substring(dot + 1);
        }
        return ret;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public int hashCode()
    {
        return getExclusiveIdentifier().hashCode();
    }

    public boolean equals(Object o)
    {
        if(this == o)
            return true;
        if(o == null)
            return false;
        if(o.getClass() == (com.power.vfs.FileObject.class))
        {
            FileObject f = (FileObject)o;
            if(getExclusiveIdentifier() != null && getExclusiveIdentifier().equals(f.getExclusiveIdentifier()))
                return true;
        }
        return false;
    }

    public String toString()
    {
        StringBuffer sb = (new StringBuffer("FileObject:")).append("name=").append(name).append(",path=").append(path).append(",type=").append(type).append("is directory=").append(isDirectory()).append("properties=").append(properties);
        return sb.toString();
    }

    private String getExclusiveIdentifier()
    {
        String path = null;
        if(getProperties() != null)
            path = (String)getProperties().get("REAL_PATH");
        if(path == null)
            path = name;
        if(path == null)
            path = String.valueOf(super.hashCode());
        return path;
    }

    public int compareTo(Object o)
    {
        if(o == null)
            throw new NullPointerException("\u6BD4\u8F83\u6587\u4EF6\u5B9E\u4F53\u65F6\u8981\u6BD4\u8F83\u7684\u5BF9\u8C61\u662Fnull");
        if(equals(o))
            return 0;
        if(o.getClass() == (com.power.vfs.FileObject.class))
        {
            FileObject fo = (FileObject)o;
            return getExclusiveIdentifier().compareTo(fo.getExclusiveIdentifier());
        } else
        {
            throw new ClassCastException("\u6BD4\u8F83\u6587\u4EF6\u5B9E\u4F53\u65F6\u8981\u6BD4\u8F83\u7684\u5BF9\u8C61\u662F" + o.getClass().getName());
        }
    }

    private String name;
    private String path;
    private String type;
    private Map properties;

}
