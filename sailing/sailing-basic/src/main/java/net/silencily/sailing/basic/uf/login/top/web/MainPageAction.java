package net.silencily.sailing.basic.uf.login.top.web;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.silencily.sailing.security.SecurityContextInfo;
import net.silencily.sailing.security.model.CurrentUser;
import net.silencily.sailing.struts.DispatchActionPlus;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.apache.commons.lang.StringUtils;
/**
 * @author wangchc
 *
 */
public class MainPageAction extends DispatchActionPlus {
	private static final String FORWARD_TOP = "top";
    private static final String FORWARD_MAIN = "main";
    private static final String MAIN_URL = "/uf/desk/TblColumnDisplayAction.do?step=listColumn";
	/**
	 * 首页面TopFrame信息取得。
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
	 * @return ActionForward(top)
	 */
	public ActionForward getTop( ActionMapping mapping, 
			                    ActionForm form,
			                    HttpServletRequest request, 
			                    HttpServletResponse response)
			throws Exception {	
		MainPageForm theForm = (MainPageForm)form;
		theForm.setLoginUserName(SecurityContextInfo.getCurrentUser().getUserName());
        String mainUrl = (String)request.getSession().getAttribute("SSO_URL");

        if (StringUtils.isBlank(mainUrl)) {
            mainUrl = MAIN_URL;
        } else {
            request.getSession().removeAttribute("SSO_URL");
        }
        theForm.setMainUrl(URLEncoder.encode(mainUrl, "GBK"));
        
		// 这有个BUG,调用以后,基础编码表数据出不来
        //往session中添加CurrentUser用户信息,解决在EnhancehibernateTemplatePlus中无发查寻得到基础编码表的BUG
        HttpSession session= request.getSession();
        CurrentUser currentUser = SecurityContextInfo.getCurrentUser();
        if(null!=session.getAttribute("currentUser")){
        	session.removeAttribute("currentUser");
        	session.setAttribute("currentUser", currentUser);
        }else{
        	session.setAttribute("currentUser", currentUser);
        }
        
        
        return mapping.findForward(FORWARD_TOP);
	}

    /**
     * 首页面main信息取得。
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
     * @return ActionForward(main)
     */
    public ActionForward getMain( ActionMapping mapping, 
                                ActionForm form,
                                HttpServletRequest request, 
                                HttpServletResponse response)
            throws Exception {              
        return mapping.findForward(FORWARD_MAIN);
    }

}
