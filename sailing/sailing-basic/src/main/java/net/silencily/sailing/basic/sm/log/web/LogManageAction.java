package net.silencily.sailing.basic.sm.log.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.silencily.sailing.basic.sm.domain.TblCmnSysLog;
import net.silencily.sailing.basic.sm.log.service.LogManageService;
import net.silencily.sailing.common.crud.service.CommonService;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.struts.DispatchActionPlus;
import net.silencily.sailing.utils.MessageInfo;
import net.silencily.sailing.utils.MessageUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


public class LogManageAction extends DispatchActionPlus{
	
	public static final String FORWARD_ENTRY="entry";
	
	public static final String FORWARD_LIST="list";
	
	public static final String FORWARD_CLEAR="clear";
	
	/**
	 * 功能描述 调用公用接口
	 */
	private CommonService getService() {
		return (CommonService)ServiceProvider.getService(CommonService.SERVICE_NAME);
	}
	
	/**
	 * 
	 * 功能描述 调用接口方法
	 * @return
	 * 2007-11-30 上午10:25:22
	 * @version 1.0
	 * @author baihe
	 */
	public static LogManageService service() {
		return (LogManageService) ServiceProvider
				.getService(LogManageService.SERVICE_NAME);
	}
	
	/**
	 * 
	 * 功能描述 登录
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-12-3 下午03:57:40
	 * @version 1.0
	 * @author baihe
	 */
	public ActionForward entry(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return mapping.findForward(FORWARD_ENTRY);
	}
	
	/**
	 * 
	 * 功能描述 系统日志列表
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-12-3 下午04:08:43
	 * @version 1.0
	 * @author baihe
	 */
	public ActionForward list(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LogManageForm theForm=(LogManageForm)form;
		TblCmnSysLog bean=theForm.getSysLog();
		List list =null;
		list =getService().getList(TblCmnSysLog.class);
		if(list.isEmpty())
		{
			list=new ArrayList(0);
		}
		theForm.setCmnSysLog(list);
		return mapping.findForward(FORWARD_LIST);
	}
	
	/**
	 * 
	 * 功能描述 日志清理
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-12-3 下午04:08:43
	 * @version 1.0
	 * @author baihe
	 */
	public ActionForward clear(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			return mapping.findForward(FORWARD_CLEAR);
	}
	
	/**
	 * 
	 * 功能描述 删除日志
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-12-4 下午03:08:00
	 * @version 1.0
	 * @author baihe
	 */
	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LogManageForm theForm=(LogManageForm)form;
		TblCmnSysLog bean=theForm.getSysLog();
		String startTime =request.getParameter("start");
		String endTime=request.getParameter("end");
		service().delete(startTime,endTime);
		MessageUtils.addMessage(request, MessageInfo.factory().getMessage("SM_I037_R_2", startTime, endTime));
		return mapping.findForward(FORWARD_CLEAR);
	}

}
