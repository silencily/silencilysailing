package net.silencily.sailing.framework.persistent.search;

import net.silencily.sailing.framework.persistent.filter.Paginater;

/**
 * 保存系统中查询数据的信息, 包括检索条件, 翻页等信息. 这个类是通用查询架构中的一部分, 用于
 * 收集参数
 * @author scott
 * @since 2006-4-29
 * @version $Id: SearchInformation.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 *
 */
public class SearchInformation {
    /** 持久化层的查询条件 */
    private SearchCondition[] searchConditions;
    
    /** 持久化层的分页信息 */
    private Paginater paginater;
    
    /** 是否忽略这个查询条件, 可能用于在某些情况下不使用已有的查询条件 */
    private boolean ignoreSearchCondition = false;
    
    /** 是否忽略分页信息 */
    private boolean ignorePaginater = false;
    
    public SearchInformation() {
        super();
    }
    
    public SearchInformation(SearchCondition[] searchConditions, Paginater paginater) {
        this.searchConditions = searchConditions;
        this.paginater = paginater;
    }

    public boolean isIgnorePaginater() {
        return ignorePaginater;
    }

    public void setIgnorePaginater(boolean ignorePaginater) {
        this.ignorePaginater = ignorePaginater;
    }

    public boolean isIgnoreSearchCondition() {
        return ignoreSearchCondition;
    }

    public void setIgnoreSearchCondition(boolean ignoreSearchCondition) {
        this.ignoreSearchCondition = ignoreSearchCondition;
    }

    public Paginater getPaginater() {
        return paginater;
    }

    public void setPaginater(Paginater paginater) {
        this.paginater = paginater;
    }

    public SearchCondition[] getSearchConditions() {
        return searchConditions;
    }

    public void setSearchConditions(SearchCondition[] searchConditions) {
        this.searchConditions = searchConditions;
    }
}
