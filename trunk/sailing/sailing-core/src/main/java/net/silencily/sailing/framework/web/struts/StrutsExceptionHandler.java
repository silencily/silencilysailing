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
 * <p>����<code>struts</code>�ܹ����쳣������,���쳣�ŵ�<code>struts</code>�쳣������
 * ��<code>key</code>������, ����ת��<code>path</code>���Ե�ҳ��. ���û�ж����������
 * �ͷ��ص�����ҳ��</p>
 * <p>��<code>struts</code>�������쳣�Ĺ淶����<ul>
 * <li><code>key</code>����:�쳣.key, ���粶��<code>Exception</code>�쳣�����õ�<code>
 * key</code>������<code>exception.key</code></li>
 * <li>��ʾ��Ϣ��ҳ������: �쳣��.jsp, ��ʹ����������Ӿ���<code>exception.jsp</code></li>
 * </ul></p>
 * 
 * @author scott
 * @since 2006-3-24
 * @version $Id: StrutsExceptionHandler.java,v 1.1 2010/12/10 10:54:20 silencily Exp $
 *
 */
public class StrutsExceptionHandler extends ExceptionHandler {

    /**
     * �����쳣��Ϣ��<code>request</code>��<code>session</code>, ��ʹ��<code>struts</code>
     * ��<code>key</code>, ��������ҳ���е������İ�. ����ʹ��<code>ExceptionConfig</code>
     * �е�<code>key</code>����, �����ǵ��쳣�ܹ������ֵ�Ѿ�û������. �⵱Ȼ���Ǳ����, ���
     * �������Ĳ��Կ�����չ�����
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
     * ���Ը�����������ṩ�����ı����쳣��<code>key</code>
     * @return �����쳣��<code>key</code>
     */
    protected String getExceptionKeyName() {
        return null;
    }

}
