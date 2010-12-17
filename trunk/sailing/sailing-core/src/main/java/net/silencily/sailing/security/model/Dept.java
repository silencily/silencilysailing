package net.silencily.sailing.security.model;

import java.io.Serializable;

/**
 * 部门
 * @author yushn
 * @version 1.0
 */
public class Dept implements Serializable{
	//班组/部门ID
    private String id;
    //班组/部门名称
	private String name;
    //部门ID
    private String deptId;
    //部门名称
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
