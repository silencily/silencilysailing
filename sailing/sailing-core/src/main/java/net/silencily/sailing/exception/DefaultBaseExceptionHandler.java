package net.silencily.sailing.exception;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.resource.CodedMessageSource;

/**
 * <p><code>ExceptionHandler</code>的基础实现, 实现架构缺省的异常处理. 这里处理的是开发过程
 * 中没有处理掉的异常, 在代码中捕获并已处理的情况不再此之列. 比如在方法中有一个删除数据库表一条记录
 * 的操作<pre>
 *   // 删除一个客户订单, 如果抛出<code>ForeignKeyConstraintException</code>异常,说明
 *   // 这份订单已经有出库的物品相关联, 必须清理了这些出库的物品才能删除订单. 
 *   // 接着程序处理了这个业务异常
 *   try {
 *     order.deleteOrder(orderDto);
 *   } catch (ForeignKeyConstraintException e) {
 *     for (Iterator it = order.getItems().iterator; it.hasNext(); ) {
 *       ItemDto itemDto = (Item) it.next();
 *       item.deleteItem(itemDto);
 *     }
 *     order.deleteOrder(orderDto);
 *   }
 * 
 * </pre>. 上面的这种情况不在这个类的处理范围之内, 这个类主要处理的问题是<ol>
 * <li>是否没有或多次<code>log</code>异常</li>
 * <li>是否将堆栈信息出现在页面上</li>
 * <li>吞噬异常的不良习惯</li></ol>
 * </p>
 * <p>异常通常是要展现为可读的信息, 我们的处理方式是在配置文件中定义某类异常的客户化信息, 当在确定
 * 的方法发生确定的异常时, 异常处理可以自动地检索到这条信息并在需要时展现出来
 * @author scott
 * @since 2006-3-22
 * @version $Id: DefaultBaseExceptionHandler.java,v 1.1 2010/12/10 10:54:24 silencily Exp $
 *
 */
public class DefaultBaseExceptionHandler extends  AbstractBaseExceptionHandler {
    private static Log logger = LogFactory.getLog(DefaultBaseExceptionHandler.class);

    private CodedMessageSource codedMessageSource = ServiceProvider.getCodedMessageSource();

    protected String getMessage(String key, ExceptionInfo info) {
        if (!codedMessageSource.exists(key)) {
            if (logger.isDebugEnabled()) {
                logger.debug("没有定义key=[" + key + "]的信息");
            }
            return null;
        }
        return codedMessageSource.getMessage(key, info.getParams());
    }

    public void log(Log logger, ExceptionInfo info) {
        throw new UnsupportedOperationException("没有实现的方法");
    }
}
