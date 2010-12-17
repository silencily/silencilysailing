/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project publicresource
 */

package net.silencily.sailing.common.message.service.web;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.silencily.sailing.common.CommonServiceLocator;
import net.silencily.sailing.common.fileupload.VfsUploadFiles;
import net.silencily.sailing.common.message.dto.CommonMessage;
import net.silencily.sailing.common.message.dto.CommonMessageAccept;
import net.silencily.sailing.common.message.service.CommonMessageService;
import net.silencily.sailing.common.transfer.VfsHttpClientResultUtils;
import net.silencily.sailing.framework.web.struts.BaseDispatchAction;
import net.silencily.sailing.utils.DtoUtils;
import net.silencily.sailing.utils.MessageUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.power.vfs.FileObjectManager;

/**
 * 消息的 Action 
 * @since 2006-10-03
 * @author pillarliu 
 * @version $Id: CommonMessageAction.java,v 1.1 2010/12/10 10:54:17 silencily Exp $
 */
public class CommonMessageAction extends BaseDispatchAction{

	private static final String FORWARD_ENTRY  = "entry";
	private static final String FORWARD_INFO   = "info";
	private static final String FORWARD_DETAIL_SEND   = "detailSend";
	private static final String FORWARD_DETAIL_ACCEPT = "detailAccept";
	private static final String FORWARD_LIST_SEND   = "listSend";
	private static final String FORWARD_LIST_ACCEPT = "listAccept";
	private static final String FORWARD_LIST_REMIND = "listRemind";
	private static final String FORWARD_LIST_REMINDTEXT = "listRemindText";	
	
	public ActionForward entry(
		ActionMapping mapping, 
		ActionForm form, 
		HttpServletRequest request, 
		HttpServletResponse response) 
		throws Exception {
		
		return mapping.findForward(FORWARD_ENTRY);
	}
	
	/** 列出发送的消息*/
	public ActionForward listSend(
		ActionMapping mapping, 
		ActionForm form, 
		HttpServletRequest request, 
		HttpServletResponse response) 
		throws Exception {
		
		CommonMessageForm theForm = (CommonMessageForm) form;
		theForm.setResults(getAttachementForSendResult(getService().findSendMessage()));
		theForm.setMessageStatus(getService().statForSendStatus());
		return mapping.findForward(FORWARD_LIST_SEND);
	}
	
	/**分状态查询发送的消息*/
	public ActionForward listSendForStatus(
		ActionMapping mapping, 
		ActionForm form, 
		HttpServletRequest request, 
		HttpServletResponse response) 
		throws Exception {
		
		CommonMessageForm theForm = (CommonMessageForm) form;
		theForm.setResults(getAttachementForSendResult(getService().findSendMessageForStatus(theForm.getStatus())));
		theForm.setMessageStatus(getService().statForSendStatus());
		return mapping.findForward(FORWARD_LIST_SEND);
	}
	
	/**列出接受的消息*/
	public ActionForward listAccept(
		ActionMapping mapping, 
		ActionForm form, 
		HttpServletRequest request, 
		HttpServletResponse response) 
		throws Exception {
		
		CommonMessageForm theForm = (CommonMessageForm) form;
		theForm.setResults(getAttachementForAcceptResult(getService().findAcceptMessage()));
		theForm.setMessageStatus(getService().statForAcceptStatus());
		return mapping.findForward(FORWARD_LIST_ACCEPT);
	}
	
	/**将未阅读的消息以提醒方式展示：*/
	public ActionForward listRemind(
		ActionMapping mapping, 
		ActionForm form, 
		HttpServletRequest request, 
		HttpServletResponse response) 
		throws Exception {
		
		CommonMessageForm theForm = (CommonMessageForm) form;
		theForm.setResults(getService().findAcceptMessageForStatus(CommonMessageAccept.ACCEPT_STATUS_NONE));
		return mapping.findForward(FORWARD_LIST_REMIND);
	}
	
	public ActionForward listRemindText(
		ActionMapping mapping, 
		ActionForm form, 
		HttpServletRequest request, 
		HttpServletResponse response) 
		throws Exception {
		
		CommonMessageForm theForm = (CommonMessageForm) form;
		theForm.setResults(getService().findAcceptMessageForStatus(CommonMessageAccept.ACCEPT_STATUS_NONE));
		return mapping.findForward(FORWARD_LIST_REMINDTEXT);
	}
	
	/**分状态查询接受的消息*/
	public ActionForward listAcceptForStatus(
		ActionMapping mapping, 
		ActionForm form, 
		HttpServletRequest request, 
		HttpServletResponse response) 
		throws Exception {
		
		CommonMessageForm theForm = (CommonMessageForm) form;
		theForm.setResults(getAttachementForSendResult(getService().findAcceptMessageForStatus(theForm.getStatus())));
		theForm.setMessageStatus(getService().statForAcceptStatus());
		return mapping.findForward(FORWARD_LIST_ACCEPT);
	}
	
	
	/** 新消息/编辑消息 */
	public ActionForward info(
		ActionMapping mapping, 
		ActionForm form, 
		HttpServletRequest request, 
		HttpServletResponse response) 
		throws Exception {
		
		CommonMessageForm theForm = (CommonMessageForm) form;
		if(StringUtils.isBlank(theForm.getOid()) || theForm.getMessage().isScratch()){
			loadUploadFiles(theForm.getMessage());
			return mapping.findForward(FORWARD_INFO);
		}else
			return detailAccept(mapping,form,request,response);
	}
	
	/** 转发消息时候：oid 为要转发的消息ID
	 *  回复的时候，status =  CommonMessageAccept.ACCEPT_STATUS_REPLAY
	 *  TODO 由于没有回写oid,没有记录回复的状态
	 */
	public ActionForward detailSend(
		ActionMapping mapping, 
		ActionForm form, 
		HttpServletRequest request, 
		HttpServletResponse response) 
		throws Exception {
	
		CommonMessageForm theForm = (CommonMessageForm) form;
		CommonMessage message = theForm.getMessage();
		if(theForm.getStatus().equals(CommonMessageAccept.ACCEPT_STATUS_MOVE)){
			message = getService().updateAcceptStatue(message,CommonMessageAccept.ACCEPT_STATUS_FINSITH, CommonMessageAccept.ACCEPT_STATUS_MOVE);
			loadUploadFiles(theForm.getMessage());
			theForm.setOid(null);
		}			
		theForm.setMessage(message);
		return mapping.findForward(FORWARD_DETAIL_SEND);
	}
	
	/** 查看接受的消息 和 查看发出的消息 */
	public ActionForward detailAccept(
		ActionMapping mapping, 
		ActionForm form, 
		HttpServletRequest request, 
		HttpServletResponse response) 
		throws Exception {
	
		CommonMessageForm theForm = (CommonMessageForm) form;
		CommonMessage message = theForm.getMessage();
		message = getService().updateAcceptStatue(message, CommonMessageAccept.ACCEPT_STATUS_NONE,CommonMessageAccept.ACCEPT_STATUS_FINSITH);
		loadUploadFiles(theForm.getMessage());
		theForm.setMessage(message);
		return mapping.findForward(FORWARD_DETAIL_ACCEPT);
	}
	
	/** 发送列表中各个消息判断是否有附件*/
	private List getAttachementForSendResult(List list){
		for (Iterator iter = list.iterator(); iter.hasNext(); ) {
			CommonMessage message = (CommonMessage) iter.next();
			message.setAttachement(isAttachement(message));
		}
		return list;
	}
	/** 接受消息列表中判断是否有附件*/
	private List getAttachementForAcceptResult(List list){
		for (Iterator iter = list.iterator(); iter.hasNext(); ) {
			CommonMessageAccept accept = (CommonMessageAccept) iter.next();
			accept.getMessage().setAttachement(isAttachement(accept.getMessage()));
		}
		return list;
	}
	
	private boolean isAttachement(CommonMessage message){
		loadUploadFiles(message);
		boolean attachement = false;
		VfsUploadFiles vfs = (VfsUploadFiles) message.getUploadFiles();
		if(vfs.getFiles()!=null ){
			if(vfs.getFiles().length>0)
				attachement =  true;
		}	
		return attachement;
	}
	
	//显示附件
	private void loadUploadFiles(CommonMessage message) {
		if (!DtoUtils.isTransient(message)) {
			message.getUploadFiles().setFilePath(message.getUploadFilesPath());
			message.getUploadFiles().read();			
		}
	}
	
	public ActionForward save(
		ActionMapping mapping, 
		ActionForm form, 
		HttpServletRequest request, 
		HttpServletResponse response) 
		throws Exception {
		
		CommonMessageForm theForm = (CommonMessageForm) form;
		populateStockDays(theForm);		
		if(StringUtils.isNotBlank(theForm.getOid())){
			getService().update(theForm.getMessage());		
		}else{		
			getService().save(theForm.getMessage());
			String oid = theForm.getMessage().getId();				
			theForm.setOid(oid);
		}
		if(CommonMessage.STEND_STATUS_SCRATCH.equals(theForm.getMessage().getStatus().getCode())){
			MessageUtils.addMessage(request, "成功保存消息草稿，您需要选择状态为“发送”才正式发送出去！");
		}else
			MessageUtils.addMessage(request, "成功发送消息！");
		
		saveUploadFiles(theForm.getMessage());
		return info(mapping,form,request,response);
	}

	//批量加入发送人
	private void populateStockDays(CommonMessageForm form){
		CommonMessage stock = form.getMessage();
		stock.removeAllAccept();
		for (int i = 0; i < form.getAcceptPersons().length; i++) {
			stock.addMessageAccepts(form.getAcceptPersons()[i]);		
		}		
	}
	
	//保存上传附件
	private void saveUploadFiles(CommonMessage message) {
		message.getUploadFiles().setFilePath(message.getUploadFilesPath());
		message.getUploadFiles().write();
	}
	
	/**如果是草稿消息，则直接删除（同时删除附件）；
	 * 如果已发送，则将发送状态设置为“删除”的状态，不显示在列表上
	 */
	public ActionForward remove(
		ActionMapping mapping, 
		ActionForm form, 
		HttpServletRequest request, 
		HttpServletResponse response) 
		throws Exception {
		
		CommonMessageForm theForm = (CommonMessageForm) form;
		loadUploadFiles(theForm.getMessage());
		getService().remove(theForm.getMessage());
		if(theForm.getMessage().isScratch()){			
			theForm.getMessage().getUploadFiles().delete();
		}		
		MessageUtils.addMessage(request, MessageUtils.DEFAULT_DELETE_SUCCESS_MESSAGE);
		return listSend(mapping, theForm, request, response);
	}
	
	/**从接受列表中删除：将接受类型该为垃圾*/
	public ActionForward removeAccept(
		ActionMapping mapping, 
		ActionForm form, 
		HttpServletRequest request, 
		HttpServletResponse response) 
		throws Exception {
		
		CommonMessageForm theForm = (CommonMessageForm) form;
		CommonMessage message = theForm.getMessage();
		message = getService().removeAcceptType(message);
		MessageUtils.addMessage(request, MessageUtils.DEFAULT_DELETE_SUCCESS_MESSAGE);
		return listAccept(mapping, theForm, request, response);
	}
	
	//直接显示图片、视频等文件
	public ActionForward media(
        ActionMapping mapping, 
        ActionForm form, 
        HttpServletRequest request, 
        HttpServletResponse response) throws Exception {
        
		CommonMessageForm theForm = (CommonMessageForm) form;
		loadUploadFiles(theForm.getMessage());
		VfsUploadFiles vfsUploadFiles = (VfsUploadFiles) theForm.getMessage().getUploadFiles();
		FileObjectManager fileObjectManager = vfsUploadFiles.getFileObjectManager();
        String fileName = theForm.getFileName();
        if(StringUtils.isNotBlank(fileName)){
        	VfsHttpClientResultUtils.downloadFromVfs(fileObjectManager, request, response, VfsHttpClientResultUtils.CONTENT_DISPOSITION_INLINE, fileName);
        }
		return null;
    }
	
	public ActionForward downloadAttachement(
		ActionMapping mapping, 
		ActionForm form, 
		HttpServletRequest request, 
		HttpServletResponse response) 
		throws Exception {
		
		CommonMessageForm theForm = (CommonMessageForm) form;				
		loadUploadFiles(theForm.getMessage());
		VfsUploadFiles vfsUploadFiles = (VfsUploadFiles) theForm.getMessage().getUploadFiles();
		FileObjectManager fileObjectManager = vfsUploadFiles.getFileObjectManager();
        /* 因为这个请求是通过 get 传进来的, 可能有汉字,所以要编码 */
        String fileName = theForm.getFileName();
        //MiscUtils.recode(theForm.getFileName(), null);
		VfsHttpClientResultUtils.downloadFromVfs(fileObjectManager, request, response, VfsHttpClientResultUtils.CONTENT_DISPOSITION_ATTACHMENT, fileName);
		return null;
	}
	
	private CommonMessageService getService() {
		return CommonServiceLocator.getMessageService();
	}
}
