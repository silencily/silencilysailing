package net.silencily.sailing.framework.exception;

import net.silencily.sailing.exception.BaseBusinessException;

/**
 * 这个异常用于表现一个<tt>业务实体</tt>的属性是无效的, 常用于验证来自于前端<code>form</code>
 * 中的数据失败时的异常
 * @@InvalidPropertyValueException: 错误:属性{0}的值是{1}, {2}
 * @author scott
 * @since 2006-3-30
 * @version $Id: InvalidPropertyValueException.java,v 1.1 2010/12/10 10:54:28 silencily Exp $
 *
 */
public class InvalidPropertyValueException extends BaseBusinessException {
    
    /**
     * 属性验证失败异常
     * @param propertyName 引起错误的属性名描述, 比如:<tt>姓名</tt>
     * @param value        引起错误的值, 比如:<tt>买买提提买买买买提提买买买买提提买买买买提提买买</tt>
     * @param cause        引起错误的原因, 比如:<tt>中国人不应该有这么长的名字,哪来的?</tt>
     */
    public InvalidPropertyValueException(String propertyName, String value, String cause) {
        super(new Object[] {propertyName, value, cause});
    }
}
