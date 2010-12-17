package net.silencily.sailing.basic.sm.domain;

// Generated 2007-11-14 16:40:27 by Hibernate Tools 3.2.0.b9

import java.util.HashMap;
import java.util.Map;

import net.silencily.sailing.hibernate3.EntityPlus;

/**
 * TblCmnRolePermission generated by hbm2java
 */
public class TblCmnRolePermission extends EntityPlus implements
		java.io.Serializable {

	private static Map readAccessLevelMap = new HashMap();
	private static Map writeAccessLevelMap = new HashMap();
	private static Map rwCtrlMap = new HashMap();
	static {
		readAccessLevelMap.put("0", "系统级");
		readAccessLevelMap.put("1", "部门级");
		readAccessLevelMap.put("2", "个人级");
	}
	static {
		writeAccessLevelMap.put("0", "系统级");
		writeAccessLevelMap.put("1", "部门级");
		writeAccessLevelMap.put("2", "个人级");
	}
	static {
		rwCtrlMap.put("1", "只读");
		rwCtrlMap.put("2", "可编辑");
	}

	private TblCmnPermission tblCmnPermission;

	private TblCmnRole tblCmnRole;

	private String readAccessLevel;

	private String writeAccessLevel;

	private String rwCtrl;

	public TblCmnRolePermission() {
	}

	public TblCmnPermission getTblCmnPermission() {
		return this.tblCmnPermission;
	}

	public void setTblCmnPermission(TblCmnPermission tblCmnPermission) {
		this.tblCmnPermission = tblCmnPermission;
	}

	public TblCmnRole getTblCmnRole() {
		return this.tblCmnRole;
	}

	public void setTblCmnRole(TblCmnRole tblCmnRole) {
		this.tblCmnRole = tblCmnRole;
	}

	public String getReadAccessLevelName() {
		return (String) readAccessLevelMap.get(readAccessLevel);
	}

	public String getWriteAccessLevelName() {
		return (String) writeAccessLevelMap.get(writeAccessLevel);
	}

	public String getRwCtrlName() {
		return (String) rwCtrlMap.get(rwCtrl);
	}

	public static Map getReadAccessLevelMap() {
		return readAccessLevelMap;
	}

	public static void setReadAccessLevelMap(Map readAccessLevelMap) {
		TblCmnRolePermission.readAccessLevelMap = readAccessLevelMap;
	}

	public static Map getWriteAccessLevelMap() {
		return writeAccessLevelMap;
	}

	public static void setWriteAccessLevelMap(Map writeAccessLevelMap) {
		TblCmnRolePermission.writeAccessLevelMap = writeAccessLevelMap;
	}

	public static Map getRwCtrlMap() {
		return rwCtrlMap;
	}

	public static void setRwCtrlMap(Map rwCtrlMap) {
		TblCmnRolePermission.rwCtrlMap = rwCtrlMap;
	}

	public String getReadAccessLevel() {
		return readAccessLevel;
	}

	public void setReadAccessLevel(String readAccessLevel) {
		this.readAccessLevel = readAccessLevel;
	}

	public String getWriteAccessLevel() {
		return writeAccessLevel;
	}

	public void setWriteAccessLevel(String writeAccessLevel) {
		this.writeAccessLevel = writeAccessLevel;
	}

	public String getRwCtrl() {
		return rwCtrl;
	}

	public void setRwCtrl(String rwCtrl) {
		this.rwCtrl = rwCtrl;
	}

}
