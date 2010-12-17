package net.silencily.sailing.basic.wf.entry;

import org.apache.commons.lang.builder.HashCodeBuilder;

public abstract class WorkflowStepPrincipal
{

    public WorkflowStepPrincipal()
    {
    }

    public WorkflowStepPrincipal(String code, String name)
    {
        this.code = code;
        this.name = name;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String toString()
    {
        StringBuffer buf = new StringBuffer();
        buf.append("code=").append(code).append(",name=").append(name);
        return buf.toString();
    }

    public boolean equals(Object o)
    {
        if(this == o)
            return true;
        if(!getClass().isInstance(o))
            return false;
        if(hashCode() == o.hashCode())
            return true;
        else
            return super.equals(o);
    }

    public int hashCode()
    {
        return getClass().hashCode() * 29 + HashCodeBuilder.reflectionHashCode(this);
    }

    private String code;
    private String name;
}
