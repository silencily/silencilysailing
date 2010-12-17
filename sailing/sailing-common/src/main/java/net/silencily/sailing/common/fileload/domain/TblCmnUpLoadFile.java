package net.silencily.sailing.common.fileload.domain;

import net.silencily.sailing.common.fileload.FileLoadBean;


public class TblCmnUpLoadFile extends net.silencily.sailing.hibernate3.EntityPlus {

	private static final long serialVersionUID = -3700755168634641247L;

	private String name;

	private String savePath;
	
	private String describle;

	private FileLoadBean flb = new FileLoadBean();

	public String getSavePath() {
		return savePath;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	public FileLoadBean getFlb() {
		return flb;
	}

	public void setFlb(FileLoadBean flb) {
		this.flb = flb;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getDescrible() {
		return describle;
	}

	public void setDescrible(String describle) {
		this.describle = describle;
	}

}
