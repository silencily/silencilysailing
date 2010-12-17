package net.silencily.sailing.common.domain;

import java.io.Serializable;

import net.silencily.sailing.exception.UnexpectedException;
import net.silencily.sailing.framework.persistent.Entity;

import org.apache.commons.lang.StringUtils;

/**
 * <p>用于业务实体中保存可能很长的字符串, 这种字符串长度保存在数据库中的<code>VARCHAR</code>
 * 列中可能超过限制, 这种属性在页面中经常作为大文本或者是<code>html</code>在线编辑器的内容,
 * 出于方便这个属性常以这种方式定义在业务实体中<pre>
 *     ......
 *     private String name;
 *     private LargeText note = new LargeText(this);
 *     ......
 * </pre>,否则在保存到数据库之前必须设置{@link #owner}属性</p>
 * <p>这个类表现的字符串实际写到<code>vfs</code>中, 一是避免了数据超过限制的可能性, 二是
 * 避免了<code>CLOB</code>类型的初始化数据的不便, 三是不同的数据库产品之间的一致性</p>
 * <p>这个类是对<code>String</code>类型的封装, 其中的{@link #owner}属性是为了方便持久化</p>
 * @author zhangli
 * @version $Id: LargeText.java,v 1.1 2010/12/10 10:54:16 silencily Exp $
 * @since 2007-4-26
 */
@SuppressWarnings("serial")
public class LargeText implements Serializable, Cloneable {
    
    private static final String TAG_PATTERN = "(<[^>]+>)|([\\n\\r]+)|(\\&#?[^\\&#;]+;)|(#[0-9a-zA-Z]+;)";
    
    private static final String NEW_LINE_PATTERN = "[\\r\\n\\\\]+";
    
    public LargeText() {
        super();
    }
    
    public LargeText(Entity owner) {
        this.owner = owner;
    }
    
    /** 字符串的内容 */
    private String content;
    
    /** 拥有这个属性的业务实体 */
    private Entity owner;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Entity getOwner() {
        return owner;
    }

    public void setOwner(Entity owner) {
        this.owner = owner;
    }
    
    /**
     * 去掉内容中的<code>html</code>标记和换行回车控制字符, 返回纯文本格式的内容
     * @return 纯文本格式的内容
     */
    public String getPlainContent() {
        if (StringUtils.isNotBlank(getContent())) {
            return getContent().replaceAll(TAG_PATTERN, new String());
        }
        return getContent();
    }
    
    /**
     * 现在的页面显示方式是用<code>hidden field</code>保存内容, 当内容中包含控制字符时
     * 造成页面错误, 提供这个方法以消除这种可能
     * @return 排除掉回车,换行,<b>\</b>的内容
     */
    public String getWithoutNewLineContent() {
        if (StringUtils.isNotBlank(getContent())) {
            return getContent().replaceAll(NEW_LINE_PATTERN, new String());
        }
        return getContent();
    }

    public Object clone() {
        LargeText lt = null;
        try {
            lt = (LargeText) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new UnexpectedException(getClass().getName() + " throw it?!", e);
        }
        return lt;
    }
}
