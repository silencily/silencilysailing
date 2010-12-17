/*
 * Copyright 2004-2005 wangz.
 * Project shufe_newsroom
 */
package net.silencily.sailing.security.acegi.intercept.web;

/**
 * ����ĳһ����Դ��������Ȩ
 * @since 2005-8-4
 * @author ����
 * @version $Id: ResourceMapping.java,v 1.1 2010/12/10 10:54:24 silencily Exp $
 */
public class ResourceMapping {
    
	/** ��Դ·��, Ҳ���� url */
    private String resourcePath;
    
    /** ���Է��ʵĽ�ɫ���� */
    private String[] recipients = new String[0];
       
    public ResourceMapping() {    
    }
    
    /**
     * @param recipients
     * @param path
     */
    public ResourceMapping(String path, String[] recipients) {
        this.resourcePath = path;
        this.recipients = recipients;
    }

    /**
     * @return Returns the recipients.
     */
    public String[] getRecipients() {
        return recipients;
    }

    /**
     * @param recipients The recipients to set.
     */
    public void setRecipients(String[] recipients) {
        this.recipients = recipients;
    }

    /**
     * @return Returns the resourcePath.
     */
    public String getResourcePath() {
        return resourcePath;
    }

    /**
     * @param resourcePath The resourcePath to set.
     */
    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    

}



