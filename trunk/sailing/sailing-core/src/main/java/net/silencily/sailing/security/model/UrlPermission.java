package net.silencily.sailing.security.model;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 功能权限属性
 * 
 * @author yushn
 * @version 1.0
 */
public class UrlPermission implements Serializable{

	/**
	 * 相关数据项权限的默认读取控制类型
	 * 参考{@link RWCtrlType}
	 */
	private int rwCtrlType;
	/**
	 * 只读权限的数据访问类型
	 * 参考{@link DataAccessType}
	 */
	private int dataAccessLevelRead;
	/**
	 * 可编辑权限的数据访问类型
	 * 参考{@link DataAccessType}
	 */
	private int dataAccessLevelWrite;
	
	/**
	 * 相关的数据项权限,key为数据项权限标识，value为{@link FieldPermission}类型。
	 */
	private HashMap fieldPerms; 
	
	public UrlPermission()
	{
		
	}
	public UrlPermission(int rwCtrlType,int dalRead,int dalWrite,HashMap fieldPerms)
	{
		this.rwCtrlType = rwCtrlType;
		this.dataAccessLevelRead = dalRead;
		this.dataAccessLevelWrite = dalWrite;
		this.fieldPerms = fieldPerms;
	}

	public HashMap getFieldPerms() {
		return fieldPerms;
	}
	public void setFieldPerms(HashMap fieldPerms) {
		this.fieldPerms = fieldPerms;
	}
	public int getDataAccessLevelRead() {
		return dataAccessLevelRead;
	}
	public void setDataAccessLevelRead(int dataAccessLevelRead) {
		this.dataAccessLevelRead = dataAccessLevelRead;
	}
	public int getDataAccessLevelWrite() {
		return dataAccessLevelWrite;
	}
	public void setDataAccessLevelWrite(int dataAccessLevelWrite) {
		this.dataAccessLevelWrite = dataAccessLevelWrite;
	}
	public int getRwCtrlType() {
		return rwCtrlType;
	}
	public void setRwCtrlType(int rwCtrlType) {
		this.rwCtrlType = rwCtrlType;
	}
}
