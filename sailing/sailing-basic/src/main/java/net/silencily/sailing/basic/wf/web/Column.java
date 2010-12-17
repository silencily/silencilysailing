package net.silencily.sailing.basic.wf.web;

public class Column {
	private String fid;
	private String fname;
	private String fcode;
	private String feditor;

	public Column(String fid,String fname,String fcode,String feditor)
	{
		this.fid = fid;
		this.fname = fname;
		this.fcode = fcode;
		this.feditor = feditor;
	}

	public String getFcode() {
		return fcode;
	}

	public void setFcode(String fcode) {
		this.fcode = fcode;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}
	
	public String getFeditor() {
		return feditor;
	}

	public void setFeditor(String feditor) {
		this.feditor = feditor;
	}
}
