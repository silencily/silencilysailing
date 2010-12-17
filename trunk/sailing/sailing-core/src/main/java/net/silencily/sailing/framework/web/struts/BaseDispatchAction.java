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
 * ����<code>struts</code>�ܹ�ʵ�ֿͻ����쳣��Ϣ�Ļ���, ���е�<code>Action</code>�������չ
 * �������(����������ԭ������һ������һ��). ��������쳣�����׳�, ������ϵͳԤ����������쳣����
 * ��, Ҳ����������������, ���Զ���<code>struts's ExceptionHandler</code>ʱһ��Ҫ����󲶻�
 * <code>Exception</code>���͵��쳣, ��ҳ����ֱ������Ϊ<code>ExceptionInfo</code>�Եõ���
 * �õĴ�����Ϣ
 * 
 * @author scott
 * @since 2006/03/21
 * @version $Id: BaseDispatchAction.java,v 1.1 2010/12/10 10:54:21 silencily Exp $
 * @version $Revision: 1.1 $
 */
public abstract class BaseDispatchAction extends DispatchAction {
    public static final String GLOBAL_INPUT = "input";
    public static final String INPUT_SUFFIX = "Input";
    
    /* û��ʹ�� DispatchAction's log, ���Ʋ���������ϰ�� */
    protected Log logger = LogFactory.getLog(this.getClass());
    
    /**
     * <p>����������� Action.execute() ����ʱ�׳����쳣, �������쳣��Ȼ��<code>DispatchAction</code>
     * ������, ͬʱ���е��쳣��������Ϊ<code>ExceptionInfo</code>, �Ա���õ��ṩ��Ϣ</p>
     * <p>û�д�����������쳣��Ϊ����ѭ<code>web</code>Ӧ�õ�<code>404</code>�ȹ淶, ����
     * �쳣������<code>web.xml</code>��������ͳһ����</p>
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
            String msg = "���÷���[" + name + "]��������";
            if (logger.isInfoEnabled()) {
                logger.info(msg, info.getThrowable());
            }
            /*
             * ���� struts' action �������� input ����ʱֱ�ӷ��ص��������������ҳ��, ���û������ֱ���׳��쳣
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
     * ����������Ա����า��, ���ṩ�ض����ж���ת�����Ĳ���. ͨ������������ϵͳ����ʱʹ��
     * @return Ҫִ�еķ�������
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
     * ���������쳣ʱҪ��ת��<code>forward</code>����, �����ṩ��ȱʡʵ��, ����Ĵ�����Ը������
     * ����
     */
    protected String getExceptionForwardName(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response) throws Exception {
        return getName(mapping, form, request, response) + INPUT_SUFFIX;
    }

    /**
     * <P>��֤�ڽ���<code>action</code>֮ǰ�����Ĵ���, ��Ҫ����֤������<code>ActionForm's validation</code>
     * �еĶ��ƴ���(��������<code>ActionForm's validation</code>����<code>null</code>����
     * ��<code>struts action config</code>��<code>validate=false</code>), ��������ļ���
     * �������Ĵ���ֱ���׳�</P>
     * <P>���ﲻ����ʹ�����ֻ�����֤�������<code>action config's validate=false</code>
     * �����</P>
     */
    private void validate(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        if (form == null) {
            /* ȷʵ���ڲ�����form-bean���÷�, ���ܲ��ᳫ, �������ﲻ����Ϊ�쳣���� */
            if (logger.isDebugEnabled()) {
                logger.debug("form-bean��null,����û��Ϊ[" + mapping.getPath() + "]����name����");
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
                throw new FileuploadExceededException("�ϴ��ļ���С���ܳ���[" 
                    + moduleConfig.getControllerConfig().getMaxFileSize() + "]");
            }
        }
    }
}
