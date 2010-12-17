package net.silencily.sailing.basic.uf.columndisplay.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.silencily.sailing.basic.sm.domain.TblCmnUser;
import net.silencily.sailing.basic.uf.columndisplay.service.TblColumnDisplayService;
import net.silencily.sailing.basic.uf.domain.TblUfColumn;
import net.silencily.sailing.basic.uf.domain.TblUfColumnOrder;
import net.silencily.sailing.basic.uf.domain.TblUfNews;
import net.silencily.sailing.basic.uf.domain.TblUfNewsFdbk;
import net.silencily.sailing.basic.uf.schedule.service.ScheduleService;
import net.silencily.sailing.basic.wf.dto.WfEntry;
import net.silencily.sailing.basic.wf.util.BisWfServiceLocator;
import net.silencily.sailing.common.crud.service.CommonService;
import net.silencily.sailing.common.dbtime.DBTime;
import net.silencily.sailing.common.fileupload.VfsUploadFile;
import net.silencily.sailing.common.fileupload.VfsUploadFiles;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.security.SecurityContextInfo;
import net.silencily.sailing.struts.DispatchActionPlus;
import net.silencily.sailing.utils.MessageInfo;
import net.silencily.sailing.utils.MessageUtils;
import net.silencily.sailing.utils.Tools;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


/**
 * @author wangchc
 *
 * @version 1.0 2007-11-9
 */

public class TblColumnDisplayAction extends DispatchActionPlus {
   private static final String FORWARD_DESK = "desk";
   private static final String FORWARD_DETAIL = "detail";
   private static final String FORWARD_MORE = "more";
   private static final String FORWARD_LIST = "list";
    
	/**
	 * 页面跳转。
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
	 * @return ActionForward(desk)
	 */
    public ActionForward goDesktop(ActionMapping mapping,
								   ActionForm form,
						           HttpServletRequest request,
						           HttpServletResponse response){
		return mapping.findForward(FORWARD_DESK);
    }

	/**
	 * 桌面栏目显示。
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
	 * @return ActionForward(list or more)
	 */
    public ActionForward listColumn( ActionMapping mapping,
		                            ActionForm form,
		                            HttpServletRequest request,
		                            HttpServletResponse response)throws Exception{
	   List lds = getService().list(TblUfColumnOrder.class);
	   TblColumnDisplayForm theForm = (TblColumnDisplayForm)form;
//	   String strSortNo = request.getParameter("sortno");
//	   String strColumnId = request.getParameter("columnId");
//	   String strMore = request.getParameter("flag");
       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
       String today = sdf.format(DBTime.getDBTime());

	   theForm.setToday(today);
	   if (lds.size() > 0){  //已经定制栏目显示时
           for (int i = 0;i < lds.size();i++) {
               TblUfColumnOrder columOrder = (TblUfColumnOrder)lds.get(i);
               TblUfColumn colum = columOrder.getTblUfColumn();
               colum.setTblUfNewses(getService().listNews(colum));
           }
		   theForm.setListDesktop(lds);
//		   theForm.setSortno("custom");
	   } else {  //没有定制栏目显示时
		   List orderList = new ArrayList();
           lds = getService().list();
		   for (int i = 0;i<lds.size(); i++){
			   // get each info
			   TblUfColumn colum = (TblUfColumn)lds.get(i);
			   if (colum == null || "UF_LMQF_01".equals(colum.getColumnFlg().getCode())) {
				   // directly to next one
				   continue;
			   }
			   TblUfColumnOrder tblColOrder = new TblUfColumnOrder();
			   tblColOrder.setTblUfColumn(colum);
               orderList.add(tblColOrder);
		   }
		   theForm.setListDesktop(orderList);
//		   theForm.setSortno("column");
           if (orderList.size() == 0) {
               MessageUtils.addErrorMessage(request, MessageInfo.factory().getMessage("UF_E001_R_0"));
               return mapping.findForward(FORWARD_DESK);
           }
	   }
	   
       //工作流列表
       try {
           theForm.setWfList(getWfData(today));
           theForm.setWfListSize(BisWfServiceLocator.getWorkflowService().getWaitTaskCount(SecurityContextInfo.getCurrentUser(), null));
       } catch (Exception e) {
           e.printStackTrace();
       }
       //日程安排
       theForm.setScheduleList(scheduleService().listNowAfter(today));
       //邮件列表
       theForm.setMailList(new ArrayList());
       //周期任务列表
       theForm.setMissionList(new ArrayList());

       return mapping.findForward(FORWARD_DESK);

    }
   
	/**
	 * 详细信息显示。
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
	 * @return ActionForward(detail)
	 */
  public ActionForward detail(ActionMapping mapping,
								ActionForm form,
						        HttpServletRequest request,
						        HttpServletResponse response){
	   String id = request.getParameter("id");
	   String emp_cd =SecurityContextInfo.getCurrentUser().getEmpCd();
	   TblColumnDisplayForm theForm = (TblColumnDisplayForm)form;
	   TblUfNews ufNews = (TblUfNews)getService().loadNews(id);
	   
	   //获取发布人的姓名处理 ------------------ BEGIN
	   if(ufNews != null){
		   //发布人姓名
		   String publisherName = "";
		   //获取发布人的ID
		   String publisherId = ufNews.getPublisherId();
		   publisherId = (publisherId == null)? "" : publisherId.trim();
		   if(!"".equals(publisherId)){
			   TblCmnUser empInfo = getService().getEmpInfo(publisherId);
			   if(empInfo != null){
				   publisherName = empInfo.getEmpName();
			   }
		   }
		   //设置发布人姓名。
		   ufNews.setPublisherName(publisherName);
	   }
	   //获取发布人的姓名处理 ------------------ END
	   
	   if(ufNews.getTblUfColumn().getFeedbackFlg().getCode().equals("1"))
	   {	
		   theForm.setTblufNewsFdbk(new TblUfNewsFdbk());
		   Iterator it = ufNews.getTblUfNewsFdbks().iterator();
		   while(it.hasNext())
		   {	
			   TblUfNewsFdbk tblufNewsFdbk = (TblUfNewsFdbk)it.next();
			   if(tblufNewsFdbk.getTblHrEmpInfo().getEmpCd()!=null && tblufNewsFdbk.getTblHrEmpInfo().getEmpCd().equals(emp_cd))
			   {
				   theForm.setTblufNewsFdbk(tblufNewsFdbk);
				   break;
			   }
		   }
	   }
	   theForm.setTblufNews(ufNews);
	   return mapping.findForward(FORWARD_DETAIL);
  }
  
  /**
   * 
   * 功能描述 保存用户反馈信息
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * 2008-1-4 下午04:44:20
   * @version 1.0
   * @author baihe
   */
  public ActionForward saveFdbk(ActionMapping mapping,
			ActionForm form,
	        HttpServletRequest request,
	        HttpServletResponse response){
		TblColumnDisplayForm theForm = (TblColumnDisplayForm)form;
		TblUfNews tblUfNews = (TblUfNews) service().load(TblUfNews.class, request.getParameter("id"));
		String emp_cd =SecurityContextInfo.getCurrentUser().getEmpCd();
		TblCmnUser empInfo = getService().getEmpInfo(emp_cd);
		TblUfNewsFdbk tblufNewsFdbk = theForm.getTblufNewsFdbk();
		tblufNewsFdbk.setTblUfNews(tblUfNews);
		tblufNewsFdbk.setTblHrEmpInfo(empInfo);
		service().saveOrUpdate(tblufNewsFdbk);
        MessageUtils.addMessage(request, MessageInfo.factory().getMessage("UF_I014_R_0"));
		return detail(mapping, form, request, response);
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
          return detail(mapping, form, request, response);
          //return mapping.findForward(FORWARD_DETAIL);
      }
      return null;
  }

   public TblColumnDisplayService getService(){
		return (TblColumnDisplayService)ServiceProvider.getService(TblColumnDisplayService.SERVICE_NAME);
   }
   
   public CommonService service(){
	   return (CommonService)ServiceProvider.getService(CommonService.SERVICE_NAME);
   }
   
   /**
    * 格式化输出字符串，如果是当前日期，输出hh:mm；否则输出mm-dd。
    * @param wfEntity 需要转换的实体
    * @param today 当前日期
    * @return 转换完了的实体
    */
   private WfEntry formatCommitTime(Object wfEntity,String today){
	   WfEntry wfe = (WfEntry)wfEntity;
	   String commitTime = wfe.getCommitTime();
	   if(today.equals(commitTime.substring(0, 10))){
		   commitTime = commitTime.substring(11, 16);
	   }else{
		   commitTime = commitTime.substring(5, 10);
//		   commitTime = commitTime.replaceAll("-", "/");
	   }
	   wfe.setCommitTime(commitTime);
	   
	   return wfe;
   }
   
   public ScheduleService scheduleService(){
		return (ScheduleService)ServiceProvider.getService(ScheduleService.SERVICE_NAME);
	}
   
   private List getWfData(String today) throws Exception {
       List forWaitting = BisWfServiceLocator.getWorkflowService().findWaitTask(SecurityContextInfo.getCurrentUser(),null,1,10);
       if (forWaitting != null) {
           // loop for saving wf's data to desk viewing format
           for (int i = 0; i < forWaitting.size(); i++) {
               formatCommitTime(forWaitting.get(i),today);
           }
       } else {
           forWaitting = new ArrayList();
       }
       return forWaitting;
   }
}


