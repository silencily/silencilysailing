package net.silencily.sailing.basic.uf.news.web;

import java.io.FileInputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.silencily.sailing.basic.sm.user.service.UserManageService;
import net.silencily.sailing.basic.uf.column.service.ColumnManageService;
import net.silencily.sailing.basic.uf.domain.TblUfColumn;
import net.silencily.sailing.basic.uf.domain.TblUfNews;
import net.silencily.sailing.basic.uf.news.service.NewsPublishService;
import net.silencily.sailing.common.dbtime.DBTime;
import net.silencily.sailing.common.fileload.FileLoadBean;
import net.silencily.sailing.common.fileload.service.FileLoadService;
import net.silencily.sailing.common.fileupload.VfsUploadFile;
import net.silencily.sailing.common.fileupload.VfsUploadFiles;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.framework.web.view.ComboSupportList;
import net.silencily.sailing.security.SecurityContextInfo;
import net.silencily.sailing.struts.DispatchActionPlus;
import net.silencily.sailing.utils.MessageInfo;
import net.silencily.sailing.utils.MessageUtils;
import net.silencily.sailing.utils.Tools;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


/**
 * @author wangchc
 *
 */
public class NewsPublishAction extends DispatchActionPlus {
    private static String FORWARD_ENTRY = "entry";
    private static String FORWARD_LIST = "list";
    private static String FORWARD_INFO = "info";
    private static String FORWARD_MORE = "more";
    private static String PROPERTY_RESOURCE_URL = "RESOURCE_URL";
    private static String RESOURCE_FILE = "/WEB-INF/classes/resource.properties";
    private static String RESOURCE_PATH = "resourceroot/public/";
    /**
     * 跳转到Entry页面。
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
     * @return ActionForward(entry)
     */
    public ActionForward entry(ActionMapping mapping, 
                                ActionForm form,
                                HttpServletRequest request, 
                                HttpServletResponse response)
            throws Exception {      
        return mapping.findForward(FORWARD_ENTRY);
    }
    /**
     * 跳转到List页面。
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
    public ActionForward list( ActionMapping mapping, 
                                ActionForm form,
                                HttpServletRequest request, 
                                HttpServletResponse response)
            throws Exception {
        NewsPublishForm theForm = (NewsPublishForm)form;
        List l = getService().list();
        theForm.setList(l);
//        theForm.setColumnList(getCMService().getColumnList(false));
        if (!theForm.getConditions().containsKey("allColumns")) {
            theForm.getConditions().put("allColumns", getCMService().getColumnList(true));
        }
        return mapping.findForward(FORWARD_LIST);
    }
    /**
     * 跳转到info页面。(如果oid为空则新增信息，否则显示详细信息)
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
     * @return ActionForward(info)
     */
    public ActionForward info( ActionMapping mapping, 
                                ActionForm form,
                                HttpServletRequest request, 
                                HttpServletResponse response)
            throws Exception {      
        NewsPublishForm theForm = (NewsPublishForm)form;
        ComboSupportList csl = getCMService().getColumnList(true);
        if (csl.size() <= 0) {
            MessageUtils.addMessage(request, MessageInfo.factory().getMessage("UF_E001_R_0"));
        }
        theForm.setColumnList(csl);
        Properties prop = getProperties();
        String resUrl = null;
        if (prop != null) {
            resUrl = prop.getProperty(PROPERTY_RESOURCE_URL);
        }
        if (StringUtils.isBlank(resUrl)) {
            resUrl = "";
        }
        theForm.setResourceUrl(resUrl);
        if (StringUtils.isBlank(theForm.getBean().getId())){
        //新建时
            MessageUtils.addMessage(request, MessageInfo.factory().getMessage("UF_I009_P_0"));
            theForm.getBean().setTblUfColumn(new TblUfColumn());
//            Map map = csl.getContentMap();
//            for(Iterator iter = map.keySet().iterator(); iter.hasNext();){
//                theForm.getBean().getTblUfColumn().setId((String) iter.next());
//                break;
//            }
        } else if(theForm.getBean().getCanEdit()) {
        //编辑时
            MessageUtils.addMessage(request, MessageInfo.factory().getMessage("UF_I016_P_0"));
            theForm.setTblUfColumnid(theForm.getBean().getTblUfColumn().getId());
        } else {
        //显示时
            MessageUtils.addMessage(request, MessageInfo.factory().getMessage("UF_I010_C_0"));
            theForm.setTblUfColumnid(theForm.getBean().getTblUfColumn().getId());
        }
        return mapping.findForward(FORWARD_INFO);
    }
    
    /**
     * 新增或者更新信息。
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
     * @return ActionForward(info)
     */
    public ActionForward save( ActionMapping mapping, 
                                ActionForm form,
                                HttpServletRequest request, 
                                HttpServletResponse response)
            throws Exception {  
        NewsPublishForm theForm = (NewsPublishForm)form;
        TblUfNews tuns = theForm.getBean();
//      TblUfNewsFdbk tunf = null;
        tuns.setTblUfColumn(getCMService().load(theForm.getTblUfColumnid()));
        String published = tuns.getPublished().getCode();
        if ("1".equals(published) && tuns.getPublishTime() == null) {
        //发布的时候
            tuns.setPublishTime(DBTime.getDBTime());
        }
        tuns.setPublisherId(SecurityContextInfo.getCurrentUser().getEmpCd());
//      //if ((tuns.getFeedbackFlg().equals("0"))||(tuns.getSendAllFlg().equals("0"))){
//      for (int i = 0;i < theForm.getNewsFdbk().size();i++){
//          tunf = (TblUfNewsFdbk)theForm.getNewsFdbk().get(i);
//          tunf.setTblUfNews(tuns);
//          TblHrEmpInfo thei = (TblHrEmpInfo)getEmpService().load(TblHrEmpInfo.class, ((TblHrEmpInfo)theForm.getExperEmpInfo().get(i)).getId());
//          thei.getEmpCd();
//          tunf.setTblHrEmpInfo(thei);
//          //tunf.setFlushFlag(false);
//          tuns.getTblUfNewsFdbks().add(tunf);
//          //getService().save(tunf);
//      }
//      //} 

        //附件处理
        tuns.setTblUfNewsAttach(new HashSet());
        //上传附件
        String savePath = this.uploadFile(form, request, response);
        for (int i = 0;i < theForm.getAttachement().size();i++){
            if(theForm.getAttachement(i)==null)
                continue;
            if(StringUtils.isBlank(theForm.getAttachement(i).getDelFlg()))
                theForm.getAttachement(i).setDelFlg("0");

            theForm.getAttachement(i).setTblUfNews(tuns);
//            if (StringUtils.isBlank(theForm.getAttachement(i).getId())) {
//               theForm.getAttachement(i).setSavePath(savePath);
//            }
            tuns.getTblUfNewsAttach().add(theForm.getAttachement(i));
        }

        //发布对象处理
        tuns.setTblUfNewsFdbks(new HashSet());
        for (int i = 0;i < theForm.getNewsFdbk().size();i++){
            if(theForm.getNewsFdbk(i)==null)
                continue;
            if(theForm.getNewsFdbk(i).getTblHrEmpInfo()==null) //职工信息空，退出
                continue;           
            if(StringUtils.isBlank(theForm.getNewsFdbk(i).getDelFlg()))
                theForm.getAttachement(i).setDelFlg("0");

            theForm.getNewsFdbk(i).setTblUfNews(tuns);
            theForm.getNewsFdbk(i).getTblHrEmpInfo().getEmpCd();
            tuns.getTblUfNewsFdbks().add(theForm.getNewsFdbk(i));
        }

        getService().save(tuns);

        if ("10".equals(published)) {
        //发布时
            MessageUtils.addMessage(request, MessageInfo.factory().getMessage("UF_I019_C_0"));
            theForm.setBean(new TblUfNews());
        } else if (StringUtils.isBlank(theForm.getOid())) {
        //新建时
            MessageUtils.addMessage(request, MessageInfo.factory().getMessage("UF_I015_R_0"));
        } else {
        //编辑时
            MessageUtils.addMessage(request, MessageInfo.factory().getMessage("UF_I014_R_0"));
        }

        ComboSupportList csl = getCMService().getColumnList(true);
        if (csl.size() <= 0) {
            MessageUtils.addMessage(request, MessageInfo.factory().getMessage("UF_E001_R_0"));
        }
        theForm.setColumnList(csl);

        return mapping.findForward(FORWARD_INFO);
    }
    //信息发布服务
    public NewsPublishService getService(){
        return (NewsPublishService)ServiceProvider.getService(NewsPublishService.SERVICE_NAME);
    }
    //取得栏目信息的服务
    public ColumnManageService getCMService(){
        return (ColumnManageService)ServiceProvider.getService(ColumnManageService.SERVICE_NAME);
    }
    
    public UserManageService getEmpService(){
        return(UserManageService)ServiceProvider.getService(UserManageService.SERVICE_NAME);
    }
    
    /**
     * 跳转到List页面。
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
     * @return ActionForward(more)
     */
    public ActionForward more( ActionMapping mapping, 
                                ActionForm form,
                                HttpServletRequest request, 
                                HttpServletResponse response)
            throws Exception {  
        NewsPublishForm theForm = (NewsPublishForm)form;
        List l = getService().listMore();
        theForm.setList(l);
        if (!theForm.getConditions().containsKey("allColumns")) {
            theForm.getConditions().put("allColumns", getCMService().getColumnList(false));
        }
        return mapping.findForward(FORWARD_MORE);
    }

    /**
     * 删除
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward delete(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

//        ColumnManageForm theForm = (ColumnManageForm) form;
        String oid = request.getParameter("oid");
        
//        theForm.setBean(getService().load(oid));
        getService().deleteAllRel(Tools.split(oid,"\\$"));
//        getService().delete(theForm.getBean());
        MessageUtils.addMessage(request, MessageInfo.factory().getMessage("UF_I013_R_0"));
        return list(mapping, form, request, response);
    }

    /**
     * 文件上传
     */
    public String uploadFile(ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String pubPath = RESOURCE_PATH;
        NewsPublishForm theForm = (NewsPublishForm) form;
        String encoding = request.getCharacterEncoding();
        if ((encoding != null) && (encoding.equalsIgnoreCase("utf-8"))) {
            response.setContentType("text/html; charset=gb2312");// 如果没有指定编码，编码格式为gb2312
        }

        String name = theForm.getBean().getClass().getName().replace('.', '/');
        String saveName = name + "/" + SecurityContextInfo.getCurrentUser().getEmpCd() + "/"
                + (DBTime.getDBTime()).getTime();// 定义文件的保存路径
//        theForm.getFlb().setAbsPath(saveName);
//        this.getFileLoadService().uploadFile(theForm.getFlb());
        //公共文件
        FileLoadBean pubFile = new FileLoadBean();
        pubFile.setAbsPath(pubPath + saveName);
        //个人文件
        FileLoadBean prvFile = new FileLoadBean();
        prvFile.setAbsPath(saveName);
        for (int i = 0;i < theForm.getFlb().getFiles().size();i++) {
            if (theForm.getFlb().getFiles().get(i) == null) {
                continue;
            }
            if (theForm.getAttachement(i).getPubResource().getCode().equals("1")) {
            //公共文件
                pubFile.getFiles().add(theForm.getFlb().getFiles(i));
                theForm.getAttachement(i).setSavePath(pubPath + saveName);
            } else {
            //个人文件
                prvFile.getFiles().add(theForm.getFlb().getFiles(i));
                theForm.getAttachement(i).setSavePath(saveName);
            }
        }
        if (pubFile.getFiles().size() > 0) {
            this.getFileLoadService().uploadFile(pubFile);
        }
        if (prvFile.getFiles().size() > 0) {
            this.getFileLoadService().uploadFile(prvFile);
        }
        return saveName;
    }

    /**
     * 下载文件
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward download(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        String saveName = request.getParameter("savepath");
        VfsUploadFiles files = new VfsUploadFiles(saveName);
        boolean fileIsExist = false;
        files.read();

        String filename = request.getParameter("filename");
        if (null == filename)
            return null;
        VfsUploadFile[] vfsfiles = files.getFiles();
        for (int i = 0; i < vfsfiles.length; i++) {
            VfsUploadFile file = vfsfiles[i];
            if (file.getFileName().equalsIgnoreCase(filename)) {
                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition",
                    "attachment; filename="
                           + Tools.encodeDLFileName(file.getFileName()));
                files.output(file.getFileName(), response.getOutputStream());
                fileIsExist = true;
                break;
            }
        }
        if(!fileIsExist){
            MessageUtils.addErrorMessage(request, MessageInfo.factory().getMessage("CM_I035_C_0"));
            ComboSupportList csl = getCMService().getColumnList(true);
            if (csl.size() <= 0) {
                MessageUtils.addMessage(request, MessageInfo.factory().getMessage("UF_E001_R_0"));
            }
            ((NewsPublishForm)form).setColumnList(csl);
            return mapping.findForward(FORWARD_INFO);
        }
        return null;
    }

    /**
     * 获取Properties
     * @return
     */
    private Properties getProperties() {
        Properties propFile = new Properties(); 
        String path = this.getServlet().getServletContext().getRealPath("/");
        String fileName = path + RESOURCE_FILE;
        try {
            FileInputStream fis = new FileInputStream(fileName);
            propFile.load(fis);
            if (null == propFile) {
                System.out.println("resource.properties文件错误或没有找到!");
            }
            return propFile;
        } catch (Exception e) {
            // 读取文件异常
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 调用共通文件上传Service()接口
     */
    private FileLoadService getFileLoadService() {
        return (FileLoadService) ServiceProvider
                .getService(FileLoadService.SERVICE_NAME);
    }

}
