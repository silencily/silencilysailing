package net.silencily.sailing.basic.wf;

import java.util.Map;

public interface WorkflowDataCallback extends java.io.Serializable
{

    public abstract void setData(Map map);

    public abstract Map getData() throws Exception;
}
