package net.silencily.sailing.basic.wf.web;

public class StepsVo {

	private String vnum;
	private String vsel;
	private String vvalue;
	
	public StepsVo(String vnum,String vsel,String vvalue){
		this.vnum= vnum;
		this.vsel = vsel;
		this.vvalue = vvalue;
	}
	public String getVnum() {
		return vnum;
	}
	public void setVnum(String vnum) {
		this.vnum = vnum;
	}
	public String getVsel() {
		return vsel;
	}
	public void setVsel(String vsel) {
		this.vsel = vsel;
	}
	public String getVvalue() {
		return vvalue;
	}
	public void setVvalue(String vvalue) {
		this.vvalue = vvalue;
	}
}
