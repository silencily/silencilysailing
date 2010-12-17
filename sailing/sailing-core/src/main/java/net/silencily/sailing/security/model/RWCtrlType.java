package net.silencily.sailing.security.model;

/**
 * 读写控制类型
 * @author yushn
 * @version 1.0
 */
public interface RWCtrlType {
	/**
	 * 不可见
	 */
	int SIGHTLESS = 0;
	/**
	 * 只读
	 */
	int READ_ONLY = 1;
	/**
	 * 可编辑
	 */
	int EDIT = 2;
}
