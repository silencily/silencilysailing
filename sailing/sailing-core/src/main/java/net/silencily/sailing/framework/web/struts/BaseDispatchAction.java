package net.silencily.sailing.framework.web.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.exception.ExceptionInfo;
import net.silencily.sailing.framework.exception.FileuploadExceededException;
import net.silencily.sailing.utils.MessageUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.upload.MultipartRequestHandler;
import org.apache.struts.util.ModuleUtils;

/**
 * 基于<code>struts</code>架构实现客户化异常信息的机制, 所有的<code>Action</code>类必须扩展
 * 这个基类(如果有特殊的原因我们一起讨论一下). 处理过的异常重新抛出, 可能是系统预定义的两个异常的子
 * 类, 也可能是其它的类型, 所以定义<code>struts's ExceptionHandler</code>时一定要在最后捕获
 * <code>Exception</code>类型的异常, 在页面中直接造型为<code>ExceptionInfo</code>以得到更
 * 好的错误信息
 * 
 * @author scott
 * @since 2006/03/21
 * @version $Id: BaseDispatchAction.java,v 1.1 2010/12/10 10:54:21 silencily Exp $
 * @version $Revision: 1.1 $
 */
public abstract class BaseDispatchAction extends DispatchAction {
    public static final String GLOBAL_INPUT = "input";
    public static final String INPUT_SUFFIX = "Input";
    
    /* 没有使用 DispatchAction's log, 名称不符合命名习惯 */
    protected Log logger = LogFactory.getLog(this.getClass());
    
    /**
     * <p>仅仅处理调用 Action.execute() 方法时抛出的异常, 其它的异常仍然由<code>DispatchAction</code>
     * 来处理, 同时所有的异常可以造型为<code>ExceptionInfo</code>, 以便更好地提供信息</p>
     * <p>没有处理超类的其它异常是为了遵循<code>web</code>应用的<code>404</code>等规范, 这种
     * 异常我们在<code>web.xml</code>的配置中统一处理</p>
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        
        
        ActionForward forward = null;
        String name = getName(mapping, form, request, response);
        try {
            validate(mapping, form, request, response);
            forward = dispatchMethod(mapping, form, request, response, name);
        } catch (Exception e) {
            ExceptionInfo info = ServiceProvider.getExceptionHandler().handle(e, this.getClass(), name);
            String msg = "调用方法[" + name + "]发生错误";
            if (logger.isInfoEnabled()) {
                logger.info(msg, info.getThrowable());
            }
            /*
             * 当在 struts' action 中配置了 input 属性时直接返回到发生错误的输入页面, 如果没有配置直接抛出异常
             */
            MessageUtils.addErrorMessage(request, info.getErrorInformation());
            request.setAttribute("application.exception", info.getThrowable());
            String forwardName = getExceptionForwardName(mapping, form, request, response);
            if (mapping.getInputForward() != null) {
                forward = mapping.getInputForward();
            } else if (mapping.findForward(forwardName) != null) {
                forward = mapping.findForward(forwardName);
            } else {
                throw new Exception(info.getThrowable());
            }
        } finally {
            if (form != null && form.getMultipartRequestHandler() != null) {
                form.getMultipartRequestHandler().rollback();
            }
        }

        return forward;
    }
    
    /**
     * 这个方法可以被子类覆盖, 以提供特定的判断跳转方法的策略. 通常用于与其它系统集成时使用
     * @return 要执行的方法名称
     */
    protected String getName(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response) throws Exception {

        String parameter = mapping.getParameter();
        if (parameter == null) {
            String message = messages.getMessage("dispatch.handler", mapping
                .getPath());
            log.error(message);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                message);
            return (null);
        }

        return request.getParameter(parameter);
    }
    
    /**
     * 检索发生异常时要跳转的<code>forward</code>名称, 这里提供了缺省实现, 特殊的处理可以覆盖这个
     * 方法
     */
    protected String getExceptionForwardName(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response) throws Exception {
        return getName(mapping, form, request, response) + INPUT_SUFFIX;
    }

    /**
     * <P>验证在进入<code>action</code>之前发生的错误, 主要是验证发生在<code>ActionForm's validation</code>
     * 中的定制错误(这类错误从<code>ActionForm's validation</code>返回<code>null</code>或者
     * 在<code>struts action config</code>中<code>validate=false</code>), 如果是由文件上
     * 传产生的错误将直接抛出</P>
     * <P>这里不处理使用这种机制验证错误而在<code>action config's validate=false</code>
     * 的情况</P>
     */
    private void validate(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        if (form == null) {
            /* 确实存在不定义form-bean的用法, 尽管不提倡, 所以这里不能作为异常处理 */
            if (logger.isDebugEnabled()) {
                logger.debug("form-bean是null,可能没有为[" + mapping.getPath() + "]定义name属性");
            }
            return;
        }
        
        BaseActionForm theForm = (BaseActionForm) form;
        ModuleConfig moduleConfig = ModuleUtils.getInstance().getModuleConfig(request, getServlet().getServletContext());

        if (theForm.getException() != null) {
            throw theForm.getException();
        } else {
            Boolean maxLengthExceeded =
                (Boolean) request.getAttribute(
                    MultipartRequestHandler.ATTRIBUTE_MAX_LENGTH_EXCEEDED);
            if ((maxLengthExceeded != null) && (maxLengthExceeded.booleanValue())) {
                throw new FileuploadExceededException("上传文件大小不能超过[" 
                    + moduleConfig.getControllerConfig().getMaxFileSize() + "]");
            }
        }
    }
}
