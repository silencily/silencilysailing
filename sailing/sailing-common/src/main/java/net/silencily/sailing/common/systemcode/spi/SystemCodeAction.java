/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project publicresource
 */
package net.silencily.sailing.common.systemcode.spi;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.silencily.sailing.common.CommonServiceLocator;
import net.silencily.sailing.common.domain.tree.FlatTreeUtils;
import net.silencily.sailing.common.systemcode.SystemCode;
import net.silencily.sailing.framework.web.struts.BaseDispatchAction;
import net.silencily.sailing.framework.web.view.ComboSupportList;
import net.silencily.sailing.utils.MessageUtils;

import org.apache.commons.collections.KeyValue;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.util.Assert;

/**
 * @since 2006-9-20
 * @author java2enterprise
 * @version $Id: SystemCodeAction.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 */
public class SystemCodeAction extends BaseDispatchAction {
	
	private static final String FORWARD_FRAME = "frame";
	private static final String FORWARD_ENTRY = "entry";
	private static final String FORWARD_LIST = "list";
	private static final String FORWARD_INFO = "info";
	private static final String FORWARD_LIST_FOR_SELECT = "listForSelect";
		
	public ActionForward frame(
		ActionMapping mapping, 
		ActionForm form,
		HttpServletRequest request, 
		HttpServletResponse response)
		throws Exception {
		
		SystemCodeForm systemCodeForm = (SystemCodeForm) form;
		if (systemCodeForm.getOid() == null) {
			systemCodeForm.setOid(SystemCode.TREE_NODE_ROOT_ID);
		}
		SystemCode systemCode = getService().load(systemCodeForm.getSystemModuleName(), systemCodeForm.getOid());
		systemCodeForm.setSystemCode(systemCode);
		return mapping.findForward(FORWARD_FRAME);		
	}
	
	public ActionForward entry(
		ActionMapping mapping, 
		ActionForm form,
		HttpServletRequest request, 
		HttpServletResponse response)
		throws Exception {
		
		return mapping.findForward(FORWARD_ENTRY);		
	}
	
	public ActionForward loadTree(
		ActionMapping mapping, 
		ActionForm form,
		HttpServletRequest request, 
		HttpServletResponse response)
		throws Exception {
		
		SystemCodeForm systemCodeForm = (SystemCodeForm) form;
		String rootId = systemCodeForm.getOid() == null ? SystemCode.TREE_NODE_ROOT_ID : systemCodeForm.getOid();		
		Collection treeNodes = getService().findByParentCodeWithContextInfo(systemCodeForm.getSystemModuleName(), rootId);
		String serializedTreeContent = FlatTreeUtils.serialize(treeNodes, false);
        response.setContentType("text/plain; charset=GBK");	
		response.getWriter().write(serializedTreeContent);	
		return null;	
	}
	
	public ActionForward list(
		ActionMapping mapping, 
		ActionForm form,
		HttpServletRequest request, 
		HttpServletResponse response)
		throws Exception {
		
		doList(form);
		return mapping.findForward(FORWARD_LIST);	
	}
	
	private void doList(ActionForm form) {
		SystemCodeForm systemCodeForm = (SystemCodeForm) form;
		List list = getService().findByParentCodeWithContextInfo(systemCodeForm.getSystemModuleName(), systemCodeForm.getParentCode());
		systemCodeForm.setResults(list);
	}
	
	public ActionForward listForSelect(
		ActionMapping mapping, 
		ActionForm form,
		HttpServletRequest request, 
		HttpServletResponse response)
		throws Exception {
		
		doList(form);
		return mapping.findForward(FORWARD_LIST_FOR_SELECT);	
	}
	
	public ActionForward comboSupportList(
		ActionMapping mapping, 
		ActionForm form,
		HttpServletRequest request, 
		HttpServletResponse response)
		throws Exception {
		
		SystemCodeForm systemCodeForm = (SystemCodeForm) form;		
		Assert.notNull(systemCodeForm.getSystemModuleName(), " systemModuleName is required. ");
		Assert.notNull(systemCodeForm.getParentCode(), " type is required. ");		
		ComboSupportList comboSupportList = getService().getCodeList(systemCodeForm.getSystemModuleName(), systemCodeForm.getParentCode());
		List list = comboSupportList.convertData2KeyValues();
		StringBuffer sb = new StringBuffer();
		for (Iterator iter = list.iterator(); iter.hasNext(); ) {
			KeyValue keyValue = (KeyValue) iter.next();
			sb.append(keyValue.getKey());
			sb.append(ComboSupportList.ELEMENT_SEPARATOR);
			sb.append(keyValue.getValue());
			if (iter.hasNext()) {
				sb.append(ComboSupportList.TEXT_VALUE_SEPARATOR);
			}
		}
		
		response.setContentType("text/plain; charset=GBK");	
		response.getWriter().write(sb.toString());	
		return null;		
	}
	
	
	public ActionForward info(
		ActionMapping mapping, 
		ActionForm form,
		HttpServletRequest request, 
		HttpServletResponse response)
		throws Exception {
	
		return mapping.findForward(FORWARD_INFO);	
	}
	
	public ActionForward save(
		ActionMapping mapping, 
		ActionForm form,
		HttpServletRequest request, 
		HttpServletResponse response)
		throws Exception {
		
		SystemCodeForm systemCodeForm = (SystemCodeForm) form;
		if (StringUtils.isBlank(systemCodeForm.getOid())) {
			getService().save(systemCodeForm.getSystemModuleName(), systemCodeForm.getSystemCode());
			systemCodeForm.setOid(systemCodeForm.getSystemCode().getCode());
			MessageUtils.addMessage(request, MessageUtils.DEFAULT_ADD_SUCCESS_MESSAGE);
		} else {
			getService().update(systemCodeForm.getSystemModuleName(), systemCodeForm.getSystemCode());
			MessageUtils.addMessage(request, MessageUtils.DEFAULT_EDIT_SUCCESS_MESSAGE);
		}		
		return mapping.findForward(FORWARD_INFO);	
	}
	
	public ActionForward delete(
		ActionMapping mapping, 
		ActionForm form,
		HttpServletRequest request, 
		HttpServletResponse response)
		throws Exception {
		
		SystemCodeForm systemCodeForm = (SystemCodeForm) form;
		getService().delete(systemCodeForm.getSystemModuleName(), systemCodeForm.getOid());
		MessageUtils.addMessage(request, MessageUtils.DEFAULT_DELETE_SUCCESS_MESSAGE);
		return list(mapping, systemCodeForm, request, response);
	}
	
	private SystemCodeCRUDService getService() {
		return CommonServiceLocator.getSystemCodeCRUDService();
	}
}
