package net.silencily.sailing.basic.uf.domain;

import net.silencily.sailing.hibernate3.CodeWrapperPlus;
import net.silencily.sailing.hibernate3.EntityPlus;


/**
 * TblUfNewsFdbk generated by MyEclipse Persistence Tools
 */

public class TblUfNewsAttach extends EntityPlus implements
		java.io.Serializable {

	// Fields

 
	private TblUfNews tblUfNews;

	private String fileName;
	
	private String description;

	private String savePath;
	
    private String replacePath;

    private CodeWrapperPlus pubResource;

    // Property accessors

 
	public TblUfNews getTblUfNews() {
		return this.tblUfNews;
	}

	public void setTblUfNews(TblUfNews tblUfNews) {
		this.tblUfNews = tblUfNews;
	}

    /**
     * @return the replacePath
     */
    public String getReplacePath() {
        return replacePath;
    }

    /**
     * @param replacePath the replacePath to set
     */
    public void setReplacePath(String replacePath) {
        this.replacePath = replacePath;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the savePath
     */
    public String getSavePath() {
        return savePath;
    }

    /**
     * @param savePath the savePath to set
     */
    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public CodeWrapperPlus getPubResource() {
        if (this.pubResource == null) {
            this.pubResource = new CodeWrapperPlus();
            this.pubResource.setCode("0");
        }
        return this.pubResource;
    }

    public void setPubResource(CodeWrapperPlus pubResource) {
        this.pubResource = pubResource;
    }

    public void setPubResource(String pubResource) {
        this.pubResource = new CodeWrapperPlus();
        this.pubResource.setCode(pubResource);
    }


}