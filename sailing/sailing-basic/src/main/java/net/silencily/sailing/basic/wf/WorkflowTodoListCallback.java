package net.silencily.sailing.basic.wf;

import java.util.List;

import net.silencily.sailing.framework.persistent.filter.Condition;

public interface WorkflowTodoListCallback
{

    public abstract List retrieve(Condition condition);
}

