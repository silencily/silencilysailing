package net.silencily.sailing.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.context.CreateAndSaveButtonCtrlCommon;
import net.silencily.sailing.context.OperType;
import net.silencily.sailing.exception.DataAccessDenyException;
import net.silencily.sailing.exception.ExceptionInfo;
import net.silencily.sailing.framework.exception.FileuploadExceededException;
import net.silencily.sailing.framework.web.struts.BaseDispatchAction;
import net.silencily.sailing.utils.MessageInfo;
import net.silencily.sailing.utils.MessageUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.upload.MultipartRequestHandler;
import org.apache.struts.util.ModuleUtils;


/**
 * @author zhaoyf
 *
 */
public class DispatchActionPlus extends BaseDispatchAction {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
	        HttpServletRequest request, HttpServletResponse response)
	        throws Exception {
		// TODO Auto-generated method stub
		 ActionForward forward = null;
	        String name = getName(mapping, form, request, response);
	        try {
	            validate(mapping, form, request, response);
	            forward = dispatchMethod(mapping, form, request, response, name);
	        } catch(DataAccessDenyException accessDenyException){
	        	request.setAttribute("dataAccessDenyMessage", accessDenyException.getMessage());
	        	throw accessDenyException;
	        } catch (Exception e) {
	        	/**
	        	 * ���ڵ��Խ׶Σ���ʽ����֮ǰһ��Ҫע�͵�
	        	 */
	        	e.printStackTrace();
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
	            return mapping.findForward("globalMessage");
	        } finally {
	            if (form != null && form.getMultipartRequestHandler() != null) {
	                form.getMultipartRequestHandler().rollback();
	            }
	        }
	       //����һЩ��ͨ�Ĵ��������ǰΪ���ҳ������£��޸���ϢΪ����ǰΪ��ʾ��
			if(CreateAndSaveButtonCtrlCommon.disableCreateButton()&&CreateAndSaveButtonCtrlCommon.disableSaveButton()){
			    if("2".equals(CreateAndSaveButtonCtrlCommon.getFormInTheWorkFlowForCommonButton())){			    	
			    	net.silencily.sailing.context.BusinessContext.setOperType(OperType.VIEW);
					if(null!=request.getAttribute("_com_coheg_messages_key_")){
						MessageUtils.clearMessages(request);
						MessageUtils.addMessage(request, MessageInfo.factory().getMessage(
						"AM_I139_P_0"));
					}
			    }
			}
			
	        return forward;
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
        
        BaseFormPlus theForm = (BaseFormPlus) form;
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
