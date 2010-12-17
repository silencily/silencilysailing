/*
 * Copyright 2004-2005 wangz.
 * Project shufe_newsroom
 */
package net.silencily.sailing.framework.web.webwork;

import net.silencily.sailing.framework.dao.PaginationSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



/**
 * PagerFacade, webwork �汾
 * @since 2005-7-18
 * @author ����
 * @version $Id: PagerFacade.java,v 1.1 2010/12/10 10:54:26 silencily Exp $
 */
public abstract class PagerFacade {
    
    private static Log log = LogFactory.getLog(PagerFacade.class);  
    
    /**
     * ��ȡ Offset ֵ
     * @return offset ֵ
     */
    public static int getOffset() {
        String pagerOffset = ActionHelper.getParam("pager.offset", "0");
        int offset= 0;
        try {
            offset = Integer.parseInt(pagerOffset);
        } catch (NumberFormatException e) {
            if (log.isDebugEnabled()) {
                log.debug("Error during get pager.offset", e);
            }
        }
        return offset;
    }
    
    /**
     * ��ȡ maxPageItems ֵ
     * @return maxPageItems ֵ
     */
    public static int getMaxPageItems() {
        String maxPageItems = ActionHelper.getParam("maxPageItems", String.valueOf(PaginationSupport.DEFAULT_MAX_PAGE_ITEMS));
        int interval = PaginationSupport.DEFAULT_MAX_PAGE_ITEMS;
        try {
            interval = Integer.parseInt(maxPageItems);
        } catch (NumberFormatException e) {
            if (log.isDebugEnabled()) {
                log.debug("Error during get maxPageItems", e);
            }
        }
        return interval;
    }
    
    
    /**
     * ��ȡ DEFAULT_MAX_PAGE_ITEMS ֵ
     * @return DEFAULT_MAX_INDEX_PAGES ֵ
     */
    public static int getMaxIndexPages() {
        String sMaxIndexPages = ActionHelper.getParam("maxIndexPage", String.valueOf(PaginationSupport.DEFAULT_MAX_INDEX_PAGES));
        int maxIndexPages = PaginationSupport.DEFAULT_MAX_INDEX_PAGES;
        try {
            maxIndexPages = Integer.parseInt(sMaxIndexPages);
        } catch (NumberFormatException e) {
            if (log.isDebugEnabled()) {
                log.debug("Error during get maxIndexPage", e);
            }
        }
        return maxIndexPages;
    }
    
    
    public static String getIndex() {
        return ActionHelper.getParam("center", PaginationSupport.DEFALUT_INDEX);
    }
    
}



