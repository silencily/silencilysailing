package net.silencily.sailing.basic.sm.domain;

// Generated 2007-11-14 16:40:27 by Hibernate Tools 3.2.0.b9

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.silencily.sailing.hibernate3.EntityPlus;

/**
 * TblCmnUserPermission generated by hbm2java
 */
public class TblCmnUserPermission extends EntityPlus implements java.io.Serializable {

	private static Map readAccessLevelMap = new HashMap();
	private static Map writeAccessLevelMap=new HashMap();
	private static Map rwCtrlMap=new HashMap();
	static{
		readAccessLevelMap.put("0", "ϵͳ��");
		readAccessLevelMap.put("1", "���ż�");
		readAccessLevelMap.put("2", "���˼�");
	}
	static{
		writeAccessLevelMap.put("0", "ϵͳ��");
		writeAccessLevelMap.put("1", "���ż�");
		writeAccessLevelMap.put("2", "���˼�");
	}
	static{
		rwCtrlMap.put("1", "ֻ��");
		rwCtrlMap.put("2", "�ɱ༭");
	}

	private TblCmnPermission tblCmnPermission;

	private TblCmnUser tblCmnUserByUserId;

	private TblCmnUser tblCmnUserByConsignerId;

	private String readAccessLevel;
	
	private String writeAccessLevel;
	
	private String rwCtrl;

	private Date beginTime;

	private Date invalidTime;

	public TblCmnUserPermission() {
	}


	public TblCmnPermission getTblCmnPermission() {
		return this.tblCmnPermission;
	}

	public void setTblCmnPermission(TblCmnPermission tblCmnPermission) {
		this.tblCmnPermission = tblCmnPermission;
	}

	public TblCmnUser getTblCmnUserByUserId() {
		return this.tblCmnUserByUserId;
	}

	public void setTblCmnUserByUserId(TblCmnUser tblCmnUserByUserId) {
		this.tblCmnUserByUserId = tblCmnUserByUserId;
	}

	public TblCmnUser getTblCmnUserByConsignerId() {
		return this.tblCmnUserByConsignerId;
	}

	public void setTblCmnUserByConsignerId(TblCmnUser tblCmnUserByConsignerId) {
		this.tblCmnUserByConsignerId = tblCmnUserByConsignerId;
	}

	public Date getBeginTime() {
		return this.beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getInvalidTime() {
		return this.invalidTime;
	}

	public void setInvalidTime(Date invalidTime) {
		this.invalidTime = invalidTime;
	}

	public String getReadAccessLevelName()
	{
		return (String)readAccessLevelMap.get(readAccessLevel);
	}
	
	public String getWriteAccessLevelName()
	{
		return (String)writeAccessLevelMap.get(writeAccessLevel);
	}
	
	public String getRwCtrlName()
	{
		return (String)rwCtrlMap.get(rwCtrl);
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


	public static Map getReadAccessLevelMap() {
		return readAccessLevelMap;
	}


	public static void setReadAccessLevelMap(Map readAccessLevelMap) {
		TblCmnUserPermission.readAccessLevelMap = readAccessLevelMap;
	}


	public static Map getWriteAccessLevelMap() {
		return writeAccessLevelMap;
	}


	public static void setWriteAccessLevelMap(Map writeAccessLevelMap) {
		TblCmnUserPermission.writeAccessLevelMap = writeAccessLevelMap;
	}


	public static Map getRwCtrlMap() {
		return rwCtrlMap;
	}


	public static void setRwCtrlMap(Map rwCtrlMap) {
		TblCmnUserPermission.rwCtrlMap = rwCtrlMap;
	}


}