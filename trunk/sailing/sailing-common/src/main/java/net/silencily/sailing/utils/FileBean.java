package net.silencily.sailing.utils;


import java.io.File;


 /**
 * @author ’‘“ª∑«
 * @version 2007-1-15
 * @see
 */
public class FileBean {

	private String id=null;
	private String name=null;
	private String parentId=null;
	private boolean isFolder;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public boolean isFolder() {
		return isFolder;
	}
	public void setFolder(boolean isFolder) {
		this.isFolder = isFolder;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public File getFile()
	{
		return new File(id);
	}
}
