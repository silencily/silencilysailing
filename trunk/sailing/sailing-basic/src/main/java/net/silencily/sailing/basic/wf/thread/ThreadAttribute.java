package net.silencily.sailing.basic.wf.thread;

public interface ThreadAttribute {
	
	public static final ThreadLocal CurrentConn = new ThreadLocal();
	
	public static final ThreadLocal TaskId = new ThreadLocal();
	
}
