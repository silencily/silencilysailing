package net.silencily.sailing.basic.wf.web;

import java.util.LinkedHashMap;
import java.util.Map;

import net.silencily.sailing.framework.web.view.ComboSupportList;

public abstract class WorkflowViewHelper
{

    public WorkflowViewHelper()
    {
    }

    public static ComboSupportList getWorkflowStatusList()
    {
        Map map = new LinkedHashMap();
        map.put("processing", "\u5904\u7406\u4E2D");
        map.put("finish", "\u5DF2\u5B8C\u6210");
        map.put("killed", "\u5DF2\u7EC8\u6B62");
        map.put("archives", "\u5DF2\u5F52\u6863");
        return new ComboSupportList(map);
    }
}
