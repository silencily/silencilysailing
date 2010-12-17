package net.silencily.sailing.security.model;

import java.io.Serializable;
import java.util.HashMap;

/**
 * ����Ȩ������
 * 
 * @author yushn
 * @version 1.0
 */
public class UrlPermission implements Serializable{

	/**
	 * ���������Ȩ�޵�Ĭ�϶�ȡ��������
	 * �ο�{@link RWCtrlType}
	 */
	private int rwCtrlType;
	/**
	 * ֻ��Ȩ�޵����ݷ�������
	 * �ο�{@link DataAccessType}
	 */
	private int dataAccessLevelRead;
	/**
	 * �ɱ༭Ȩ�޵����ݷ�������
	 * �ο�{@link DataAccessType}
	 */
	private int dataAccessLevelWrite;
	
	/**
	 * ��ص�������Ȩ��,keyΪ������Ȩ�ޱ�ʶ��valueΪ{@link FieldPermission}���͡�
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
