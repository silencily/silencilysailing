package net.silencily.sailing.security.model;

import java.io.Serializable;

/**
 * ����
 * @author yushn
 * @version 1.0
 */
public class Dept implements Serializable{
	//����/����ID
    private String id;
    //����/��������
	private String name;
    //����ID
    private String deptId;
    //��������
    private String deptName;

    public Dept()
	{
		
	}
	public Dept(String id ,String name)
	{
		this.id = id;
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    public String getDeptId() {
        return deptId;
    }
    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }
    public String getDeptName() {
        return deptName;
    }
    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
	
}
