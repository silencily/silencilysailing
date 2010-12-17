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
     * 单点登录。
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

//      查询单点登录入口
        TblUfSsoEntry tuse = null;
        try {
            tuse = getService().load(key);
        } catch (Exception e) {
            throw new Exception("无效的链接！");
        }

        Calendar nowDate = Calendar.getInstance();
        nowDate.clear(Calendar.MILLISECOND);
        nowDate.clear(Calendar.SECOND);
        nowDate.clear(Calendar.MINUTE);
        nowDate.clear(Calendar.HOUR);
        nowDate.clear(Calendar.HOUR_OF_DAY);
        if ((tuse.getAccessableTimes().intValue() > 0) && !nowDate.getTime().after(tuse.getInvalidTime())) {
//          可能登录次数>0并且在有效期内
            theForm.setUsername(tuse.getEmpCd());
            theForm.setPassword("");
//            SecurityContextInfo.setSingleSignOnUrl(tuse.getUrl());
            //设定单点登录flag,登录认证时用来判断是否是单点登录
            HibernateAuthenticationDao dao = (HibernateAuthenticationDao) getBean(request, AUTHENTICATION_DAO);
            dao.setIsSingleSignOn(true);
            //设定单点登录目标页
            request.getSession().setAttribute("SSO_URL", tuse.getUrl());
            //单点可能登录次数-1
            tuse.setAccessableTimes(new Long(tuse.getAccessableTimes().intValue() - 1));
            getService().update(tuse);
        } else {
            throw new Exception("对不起，此链接已经失效！");
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
