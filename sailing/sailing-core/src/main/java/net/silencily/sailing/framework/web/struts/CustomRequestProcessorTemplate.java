package net.silencily.sailing.framework.web.struts;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.silencily.sailing.framework.core.ContextInfo;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.RequestProcessor;

/**
 * 对<code>struts's RequestProcessor</code>的扩展, 用于从<code>out-of-box</code>加入
 * 所需的功能
 * @author scott
 * @since 2006-5-7
 * @version $Id: CustomRequestProcessorTemplate.java,v 1.1 2010/12/10 10:54:20 silencily Exp $
 *
 */
public class CustomRequestProcessorTemplate extends RequestProcessor {
    protected static Set executors = new LinkedHashSet(10);
    
    protected void processPopulate(
        HttpServletRequest request,
        HttpServletResponse response,
        ActionForm form,
        ActionMapping mapping) throws ServletException {
        
        passRequest2Form(form, request);
        /* 为了防止胀血模型的干扰, 暂时禁用条件. 在胀血模型中还没等真正使用条件的查询执行, 那些属性的查询就已经开始执行了 */
        ContextInfo.concealQuery();

        try {
        	super.processPopulate(request, response, form, mapping);
            
            for (Iterator it = executors.iterator(); it.hasNext(); ) {
                ((ProcessPopulateExecutor) it.next()).execute(request, response, form, mapping);
            }
        } catch (Exception e) {
        	if (BaseActionForm.class.isInstance(form)) {
                /* 把异常处理延迟到 dispatch action 中, 以符合ui异常处理 */
        		((BaseActionForm) form).setException(e);
        	} else {
        		if (ServletException.class.isInstance(e)) {
        			throw (ServletException) e;
        		}
        		throw new ServletException(e);
        	}
        }
    }
    
    /**
     * <p>把{@link RequestContext}信息放在<code>request</code>, 方便程序操作, 对于
     * 没有经过<code>ActionServlet</code>的操作可能有问题, 比如直接的<code>jsp</code>
     * 或<code>servlet</code></p>
     * <p>因为目前安全实现不确定, 所以没有在<code>filter</code>中进行这个更加恰当的工作,
     * 同时加入<code>RequestContext</code>时不负责清除线程, 如果出现复用同一个线程的情况
     * 简单地覆盖同名资源</p>
     */
    protected HttpServletRequest processMultipart(HttpServletRequest request) {
        HttpServletRequest req = super.processMultipart(request);
        /*
        RequestContext rc = (RequestContext) ServiceProvider.getService(RequestContextFactoryBean.SERVICE_NAME);
        req.setAttribute(RequestContextFactoryBean.SERVICE_NAME, rc);
        */
        return req;
    }

    public interface ProcessPopulateExecutor {
        /* 注意: form == null 的情况 */
        void execute(
            HttpServletRequest request,
            HttpServletResponse response,
            ActionForm form,
            ActionMapping mapping) throws ServletException;
    }
    
    /**
     * 注册在<code>RequestProcessor's processPopulate</code>方法执行后要执行的动作, 这个
     * 方法应该在系统启动时调用
     * @param executor 在<code>RequestProcessor's processPopulate</code>方法执行后要执行的动作
     */
    public static void registry(ProcessPopulateExecutor executor) {
        if (executor != null) {
            executors.add(executor);
        }
    }

    private void passRequest2Form(ActionForm form, HttpServletRequest request) {
        if (form instanceof BaseActionForm) {
            ((BaseActionForm) form).request = request;
        }
    }
    
    public static void deregistry() {
        executors = null;
    }
}
