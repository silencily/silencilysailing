package net.silencily.sailing.framework.utils;

/**
 * 用于显示业务对象中的姓名, 部门等名称的接口, 方便业务对象中保存了<code>id</code>值, 在
 * 视图展现时显示<code>id</code>对应的名称
 * @author Scott Captain
 * @since 2006-6-13
 * @version $Id: ViewCodeValue.java,v 1.1 2010/12/10 10:54:19 silencily Exp $
 *
 */
public interface ViewCodeValue {
    /** 检索这个实体的<code>id</code>值 */
    String getId();
    
    /** 根据检索到的<code>id</code>值设置名称 */
    void setName();
}
