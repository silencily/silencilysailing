package net.silencily.sailing.exception;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.resource.CodedMessageSource;

/**
 * <p><code>ExceptionHandler</code>�Ļ���ʵ��, ʵ�ּܹ�ȱʡ���쳣����. ���ﴦ����ǿ�������
 * ��û�д�������쳣, �ڴ����в����Ѵ����������ٴ�֮��. �����ڷ�������һ��ɾ�����ݿ��һ����¼
 * �Ĳ���<pre>
 *   // ɾ��һ���ͻ�����, ����׳�<code>ForeignKeyConstraintException</code>�쳣,˵��
 *   // ��ݶ����Ѿ��г������Ʒ�����, ������������Щ�������Ʒ����ɾ������. 
 *   // ���ų����������ҵ���쳣
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
 * </pre>. ���������������������Ĵ���Χ֮��, �������Ҫ�����������<ol>
 * <li>�Ƿ�û�л���<code>log</code>�쳣</li>
 * <li>�Ƿ񽫶�ջ��Ϣ������ҳ����</li>
 * <li>�����쳣�Ĳ���ϰ��</li></ol>
 * </p>
 * <p>�쳣ͨ����Ҫչ��Ϊ�ɶ�����Ϣ, ���ǵĴ���ʽ���������ļ��ж���ĳ���쳣�Ŀͻ�����Ϣ, ����ȷ��
 * �ķ�������ȷ�����쳣ʱ, �쳣��������Զ��ؼ�����������Ϣ������Ҫʱչ�ֳ���
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
                logger.debug("û�ж���key=[" + key + "]����Ϣ");
            }
            return null;
        }
        return codedMessageSource.getMessage(key, info.getParams());
    }

    public void log(Log logger, ExceptionInfo info) {
        throw new UnsupportedOperationException("û��ʵ�ֵķ���");
    }
}
