package net.silencily.sailing.security.model;

import java.io.Serializable;

/**
 * 数据项权限属性
 * 
 * @author yushn
 * @version 1.0
 */
public class FieldPermission implements Serializable{
	/**
	 * 读取控制类型
	 * 参考{@link RWCtrlType}
	 */
	private int rwCtrlType;
	public FieldPermission()
	{
		
	}
	public FieldPermission(int rwCtrlType)
	{
		this.rwCtrlType = rwCtrlType;
	}

	public int getRwCtrlType() {
		return rwCtrlType;
	}
	public void setRwCtrlType(int rwCtrlType) {
		this.rwCtrlType = rwCtrlType;
	}
}
