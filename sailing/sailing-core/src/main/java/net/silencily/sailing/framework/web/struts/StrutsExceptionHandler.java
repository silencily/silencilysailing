package net.silencily.sailing.framework.web.struts;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.silencily.sailing.utils.MessageUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;

/**
 * <p>用于<code>struts</code>架构的异常处理器,将异常放到<code>struts</code>异常配置中
 * 的<code>key</code>属性中, 并跳转到<code>path</code>属性的页面. 如果没有定义这个属性
 * 就返回到输入页面</p>
 * <p>在<code>struts</code>中配置异常的规范如下<ul>
 * <li><code>key</code>命名:异常.key, 例如捕获<code>Exception</code>异常的配置的<code>
 * key</code>命名是<code>exception.key</code></li>
 * <li>显示信息的页面命名: 异常名.jsp, 还使用上面的例子就是<code>exception.jsp</code></li>
 * </ul></p>
 * 
 * @author scott
 * @since 2006-3-24
 * @version $Id: StrutsExceptionHandler.java,v 1.1 2010/12/10 10:54:20 silencily Exp $
 *
 */
public class StrutsExceptionHandler extends ExceptionHandler {

    /**
     * 保存异常信息到<code>request</code>或<code>session</code>, 不使用<code>struts</code>
     * 的<code>key</code>, 避免了在页面中导入必须的包. 我们使用<code>ExceptionConfig</code>
     * 中的<code>key</code>属性, 在我们的异常架构中这个值已经没有意义. 这当然不是必须的, 如果
     * 有其他的策略可以扩展这个类
     */
    public ActionForward execute(Exception ex, ExceptionConfig ae, ActionMapping mapping, ActionForm formInstance, HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String key = getExceptionKeyName();
        if (key == null) {
            key = ae.getKey();
        }
        
        if ("request".equals(ae.getScope())) {
            request.setAttribute(key, ex);
        } else {
            request.getSession().setAttribute(key, ex);
        }
        
        ActionForward forward = null;
        if (ae.getPath() != null) {
            forward = new ActionForward(ae.getPath());
        } else {
            MessageUtils.addErrorMessage(request, ex.getMessage());
            forward = mapping.getInputForward();
        }
        
        return forward;
    }

    /**
     * 可以覆盖这个方法提供其它的保存异常的<code>key</code>
     * @return 保存异常的<code>key</code>
     */
    protected String getExceptionKeyName() {
        return null;
    }

}
