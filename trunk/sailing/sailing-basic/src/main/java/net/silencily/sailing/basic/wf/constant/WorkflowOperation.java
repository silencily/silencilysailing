package net.silencily.sailing.basic.wf.constant;

public interface WorkflowOperation {
	//取消操作
	public static final String KILLED = "666666";
	//初始化操作
	public static final String INIT = "888888";
	//结束节点
	public static final String END = "777777";
	//结束操作
	public static final String TO_END = "999999";
	//挂起操作
	public static final String SUSPEND = "555555";
	//激活操作
	public static final String ACTIVATE = "333333";
}
