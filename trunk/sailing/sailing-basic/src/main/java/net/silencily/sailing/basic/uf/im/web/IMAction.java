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
     * IM��¼��
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
            //��¼�û���֤
            TblCmnUser tcu = this.doAuthProcess(theForm,response);
            if (tcu == null) {
            //��֤���ɹ�
                return null;
            }
    
            //��ȡ�û��б�
            List list = null;
            try {
                list = this.getService().list();
            } catch (Exception e) {
                response.getWriter().print(this.getErrorText(DB_READ_ERROR));
                return null;
            }

            if (list.size() <= 0) {
            //�û�������
                response.getWriter().print(this.getErrorText(USER_NOT_FOUND));
                return null;
            }

            //дHEADER
            String text = this.getHeaderText(ACTION_SUCCESS);
            //дDETAIL
            try {
                for (int i = 0; i < list.size(); i++) {
                    tcu = (TblCmnUser) list.get(i);
                    text += this.getDetailText(tcu);
                }
            } catch (Exception e) {
                response.getWriter().print(this.getErrorText(DB_READ_ERROR));
                return null;
            }
            //дFOOTER
            text += this.getFooterText();

            response.getWriter().print(text);

        } catch (Exception e) {
            //��������޷�����xml
            e.printStackTrace();
        }
        return null;
    }

    /**
     * �޸�IM�ǳơ�
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
            //��¼�û���֤
            TblCmnUser tcu = this.doAuthProcess(theForm,response);
            if (tcu == null) {
            //��֤���ɹ�
                return null;
            }
            if (StringUtils.isBlank(theForm.getUserNickName())){
                response.getWriter().print(this.getErrorText(NO_NICK_NAME_INPUT));
                return null;
            }
            
            //��ȡ��Ϣ������Ϣ
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

            //дHEADER
            String text = this.getHeaderText(ACTION_SUCCESS);
            //дFOOTER
            text += this.getFooterText();

            response.getWriter().print(text);

        } catch (Exception e) {
            //��������޷�����xml
            e.printStackTrace();
        }
        return null;
    }

    public IMService getService(){
        return (IMService)ServiceProvider.getService(IMService.SERVICE_NAME);
    }

    /**
     * 
     * �������� ��¼��֤
     * @param theForm 
     * @param response
     * @return ��Ա��Ϣ
     * @throws Exception
     * 2007-12-13 ����11:27:25
     * @version 1.0
     * @author tongjq
     */
    private TblCmnUser doAuthProcess (IMForm theForm,HttpServletResponse response) 
                              throws Exception {
        //��ȡ�û���Ϣ
        TblCmnUser tcu = null;
        try {
            tcu = this.getService().find(theForm.getUserId());
        } catch (Exception e) {
            response.getWriter().print(this.getErrorText(DB_READ_ERROR));
            return null;
        }

        if (tcu == null) {
        //�û�������
            response.getWriter().print(this.getErrorText(USER_NOT_FOUND));
            return null;
        }

        //������֤
        if (!tcu.getPassword().equals(SecurityUtils.passwordHex(theForm.getPassword()))) {
            response.getWriter().print(this.getErrorText(LOGIN_FALSE));
            return null;
        }
        return tcu;
    }

    /**
     * 
     * �������� ȡ��xml��ͷ��������
     * @param type ��������0Ϊ����
     * @return ͷ��������
     * 2007-12-13 ����01:07:33
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
     * �������� ȡ��xml����ϸ������
     * @param tcu ��Ա��Ϣ
     * @return ��ϸ������
     * 2007-12-13 ����01:09:12
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
     * �������� ȡ��xml�Ľ���������
     * @return ����������
     * 2007-12-13 ����01:13:30
     * @version 1.0
     * @author tongjq
     */
    private String getFooterText(){
        String text = "</cell></lwData>";
        return text;
    }
    
    /**
     * 
     * �������� ȡ���쳣����ʱ��xml������
     * @param type ������
     * @return xml������
     * 2007-12-13 ����01:15:06
     * @version 1.0
     * @author tongjq
     */
    private String getErrorText(String type) {
        String text = this.getHeaderText(type);
        text += this.getFooterText();
        return text;
    }
}
