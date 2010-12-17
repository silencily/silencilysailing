package net.silencily.sailing.basic.uf.im.web;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.silencily.sailing.basic.sm.domain.TblCmnMsgConfig;
import net.silencily.sailing.basic.sm.domain.TblCmnUser;
import net.silencily.sailing.basic.uf.im.service.IMService;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.struts.DispatchActionPlus;
import net.silencily.sailing.utils.SecurityUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


/**
 * 
 * @author tongjq
 * @version 1.0
 */
public class IMAction extends DispatchActionPlus {
    private static String ACTION_SUCCESS = "0";
    private static String LOGIN_FALSE = "1";
    private static String USER_NOT_FOUND = "2";
    private static String NO_NICK_NAME_INPUT = "3";
    private static String NO_IM_CONFIG_FOUND = "4";
    private static String DB_READ_ERROR = "5";
    private static String DB_WRITE_ERROR = "6";
    
    /**
     * IM登录。
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
    public ActionForward login( ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response){
        IMForm theForm = (IMForm)form;

        try {
            response.setContentType("text/html;charset=gb2312");
            //登录用户验证
            TblCmnUser tcu = this.doAuthProcess(theForm,response);
            if (tcu == null) {
            //验证不成功
                return null;
            }
    
            //获取用户列表
            List list = null;
            try {
                list = this.getService().list();
            } catch (Exception e) {
                response.getWriter().print(this.getErrorText(DB_READ_ERROR));
                return null;
            }

            if (list.size() <= 0) {
            //用户不存在
                response.getWriter().print(this.getErrorText(USER_NOT_FOUND));
                return null;
            }

            //写HEADER
            String text = this.getHeaderText(ACTION_SUCCESS);
            //写DETAIL
            try {
                for (int i = 0; i < list.size(); i++) {
                    tcu = (TblCmnUser) list.get(i);
                    text += this.getDetailText(tcu);
                }
            } catch (Exception e) {
                response.getWriter().print(this.getErrorText(DB_READ_ERROR));
                return null;
            }
            //写FOOTER
            text += this.getFooterText();

            response.getWriter().print(text);

        } catch (Exception e) {
            //输出错误，无法返回xml
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 修改IM昵称。
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
    public ActionForward update( ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response){
        IMForm theForm = (IMForm)form;

        try {
            response.setContentType("text/html;charset=gb2312");
            //登录用户验证
            TblCmnUser tcu = this.doAuthProcess(theForm,response);
            if (tcu == null) {
            //验证不成功
                return null;
            }
            if (StringUtils.isBlank(theForm.getUserNickName())){
                response.getWriter().print(this.getErrorText(NO_NICK_NAME_INPUT));
                return null;
            }
            
            //获取消息配置信息
            TblCmnMsgConfig tcfc = null;
            try {
                Set set = tcu.getMsgConfigs();
                if (set != null && set.size() > 0) {
                    TblCmnMsgConfig[] tcmcArray = (TblCmnMsgConfig[])set.toArray(new TblCmnMsgConfig[1]);
                    tcfc = tcmcArray[0];
                } else {
                    response.getWriter().print(this.getErrorText(NO_IM_CONFIG_FOUND));
                    return null;
                }
            } catch (Exception e) {
                response.getWriter().print(this.getErrorText(DB_READ_ERROR));
                return null;
            }

            try {
                tcfc.setImSign(theForm.getUserNickName());
                this.getService().update(tcfc);
            } catch (Exception e) {
                response.getWriter().print(this.getErrorText(DB_WRITE_ERROR));
                return null;
            }

            //写HEADER
            String text = this.getHeaderText(ACTION_SUCCESS);
            //写FOOTER
            text += this.getFooterText();

            response.getWriter().print(text);

        } catch (Exception e) {
            //输出错误，无法返回xml
            e.printStackTrace();
        }
        return null;
    }

    public IMService getService(){
        return (IMService)ServiceProvider.getService(IMService.SERVICE_NAME);
    }

    /**
     * 
     * 功能描述 登录验证
     * @param theForm 
     * @param response
     * @return 人员信息
     * @throws Exception
     * 2007-12-13 上午11:27:25
     * @version 1.0
     * @author tongjq
     */
    private TblCmnUser doAuthProcess (IMForm theForm,HttpServletResponse response) 
                              throws Exception {
        //获取用户信息
        TblCmnUser tcu = null;
        try {
            tcu = this.getService().find(theForm.getUserId());
        } catch (Exception e) {
            response.getWriter().print(this.getErrorText(DB_READ_ERROR));
            return null;
        }

        if (tcu == null) {
        //用户不存在
            response.getWriter().print(this.getErrorText(USER_NOT_FOUND));
            return null;
        }

        //密码验证
        if (!tcu.getPassword().equals(SecurityUtils.passwordHex(theForm.getPassword()))) {
            response.getWriter().print(this.getErrorText(LOGIN_FALSE));
            return null;
        }
        return tcu;
    }

    /**
     * 
     * 功能描述 取得xml的头部文字列
     * @param type 处理结果，0为正常
     * @return 头部文字列
     * 2007-12-13 下午01:07:33
     * @version 1.0
     * @author tongjq
     */
    private String getHeaderText(String type){
        String text = "<?xml version=\"1.0\" encoding=\"GBK\"?><lwData docId=\"1\" opname=\"IMAction\">";
        text += "<cell name=\"UserList\" type=\"" + type + "\" dim=\"1\">";
        return text;
    }

    /**
     * 
     * 功能描述 取得xml的详细文字列
     * @param tcu 人员信息
     * @return 详细文字列
     * 2007-12-13 下午01:09:12
     * @version 1.0
     * @author tongjq
     */
    private String getDetailText(TblCmnUser tcu){
        String userNickName = "";
        Set set = tcu.getMsgConfigs();
        if (set != null && set.size() > 0) {
            TblCmnMsgConfig[] tcmc = (TblCmnMsgConfig[])set.toArray(new TblCmnMsgConfig[1]);
            userNickName = tcmc[0].getImSign();
            if (userNickName == null) {
                userNickName = "";
            }
        }
        String text = "<record>";
        text += "<cell name=\"userCD\" type=\"8\" dim=\"0\">"+ tcu.getEmpCd() +"</cell>";
        text += "<cell name=\"userName\" type=\"8\" dim=\"0\">" + tcu.getEmpName() + "</cell>";
        text += "<cell name=\"userNickName\" type=\"8\" dim=\"0\">" + userNickName + "</cell>";
        text += "</record>";
        return text;
    }

    /**
     * 
     * 功能描述 取得xml的结束文字列
     * @return 结束文字列
     * 2007-12-13 下午01:13:30
     * @version 1.0
     * @author tongjq
     */
    private String getFooterText(){
        String text = "</cell></lwData>";
        return text;
    }
    
    /**
     * 
     * 功能描述 取得异常处理时的xml文字列
     * @param type 处理结果
     * @return xml文字列
     * 2007-12-13 下午01:15:06
     * @version 1.0
     * @author tongjq
     */
    private String getErrorText(String type) {
        String text = this.getHeaderText(type);
        text += this.getFooterText();
        return text;
    }
}
