package net.silencily.sailing.basic.uf.sso.web;

import java.util.Calendar;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.silencily.sailing.basic.security.acegi.userdetails.hibernate.HibernateAuthenticationDao;
import net.silencily.sailing.basic.uf.domain.TblUfSsoEntry;
import net.silencily.sailing.basic.uf.sso.service.SingleSignOnService;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.struts.DispatchActionPlus;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.web.context.support.WebApplicationContextUtils;


/**
 * 
 * @author tongjq
 * @version 1.0
 */
public class SingleSignOnAction extends DispatchActionPlus {

    public static final String AUTHENTICATION_DAO = "security.authenticationDao";
    private static final String FORWARD_LOGIN = "urlLogin";
    /**
     * �����¼��
     * 
     * @param mapping
     *            ActionMapping.
     * @param form
     *            ActionForm
     * @param request
     *            HttpServletRequest
     * @param response
     *            HttpServletResponse
     * 
     * @return ActionForward(list)
     */
    public ActionForward urlLogin( ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response)
                                throws Exception{

        String key = request.getParameter("key");
        if (key == null) {
            key = "";
        }
        SingleSignOnForm theForm = (SingleSignOnForm)form;

//      ��ѯ�����¼���
        TblUfSsoEntry tuse = null;
        try {
            tuse = getService().load(key);
        } catch (Exception e) {
            throw new Exception("��Ч�����ӣ�");
        }

        Calendar nowDate = Calendar.getInstance();
        nowDate.clear(Calendar.MILLISECOND);
        nowDate.clear(Calendar.SECOND);
        nowDate.clear(Calendar.MINUTE);
        nowDate.clear(Calendar.HOUR);
        nowDate.clear(Calendar.HOUR_OF_DAY);
        if ((tuse.getAccessableTimes().intValue() > 0) && !nowDate.getTime().after(tuse.getInvalidTime())) {
//          ���ܵ�¼����>0��������Ч����
            theForm.setUsername(tuse.getEmpCd());
            theForm.setPassword("");
//            SecurityContextInfo.setSingleSignOnUrl(tuse.getUrl());
            //�趨�����¼flag,��¼��֤ʱ�����ж��Ƿ��ǵ����¼
            HibernateAuthenticationDao dao = (HibernateAuthenticationDao) getBean(request, AUTHENTICATION_DAO);
            dao.setIsSingleSignOn(true);
            //�趨�����¼Ŀ��ҳ
            request.getSession().setAttribute("SSO_URL", tuse.getUrl());
            //������ܵ�¼����-1
            tuse.setAccessableTimes(new Long(tuse.getAccessableTimes().intValue() - 1));
            getService().update(tuse);
        } else {
            throw new Exception("�Բ��𣬴������Ѿ�ʧЧ��");
        }
        return mapping.findForward(FORWARD_LOGIN);
    }

    public SingleSignOnService getService(){
        return (SingleSignOnService)ServiceProvider.getService(SingleSignOnService.SERVICE_NAME);
    }

    private Object getBean(HttpServletRequest request, String beanName) {
        ServletContext context = request.getSession().getServletContext();
        return WebApplicationContextUtils.getRequiredWebApplicationContext(context).getBean(beanName);
    } 

}
