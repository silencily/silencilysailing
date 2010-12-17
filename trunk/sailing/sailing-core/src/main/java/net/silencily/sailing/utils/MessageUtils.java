/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project framework
 */
package net.silencily.sailing.utils;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Action 中用以设置提示信息给用户的帮助类, 业务页面需要包含 /decorator/messages.jspf 用以显示
 * @since 2006-6-13
 * @author 王政
 * @version $Id: MessageUtils.java,v 1.1 2010/12/10 10:54:21 silencily Exp $
 */
public abstract class MessageUtils {
	
	/** 用来存放 success messages 的 reqeust key 值 */
	public static final String COHEG_MESSAGE_KEY = "_com_coheg_messages_key_";
	
	/** 用来存放 warn messages 的 request key 值 */
	public static final String COHEG_WARN_MESSAGE_KEY = "_com_coheg_warn_messages_key_";
	
	/** 用来存放 error messages 的 reqeust key 值 */
	public static final String COHEG_ERROR_MESSAGE_KEY = "_com_coheg_error_messages_key_";
	
	/** 默认新增成功的提示信息 */
	public static final String DEFAULT_ADD_SUCCESS_MESSAGE = " 新增成功, 您现在可以修改这条记录 ";
	
	/** 默认修改成功的提示信息 */
	public static final String DEFAULT_EDIT_SUCCESS_MESSAGE = " 修改成功, 您现在可以继续修改这条记录 ";
	
	/** 默认删除成功的提示信息 */
	public static final String DEFAULT_DELETE_SUCCESS_MESSAGE = " 记录已被删除 ";
	
	
	/** 是否禁止message */
	private static boolean enabled = true;
	
	/**
	 * 禁止消息机制
	 *
	 */
	public static void disableMessage() {
		enabled = false;
	}
	
	/**
	 * 激活消息机制
	 *
	 */
	public static void enableMessage() {
		enabled = true;
	}
	
	/**
	 * 消息机制是否激活
	 * @return 消息机制是否激活
	 */
	public static boolean isEnabled() {
		return enabled;
	}
	
	/**
	 * 增加一条提示信息
	 * @param request the request
	 * @param message the message
	 */
	public static void addMessage(HttpServletRequest request, String message) {
		innerAddMessage(request, message, COHEG_MESSAGE_KEY);
	}

	/**
	 * 清空提示信息
	 * @param request the request
	 */
	public static void clearMessages(HttpServletRequest request) {
		innerClearMessage(request, COHEG_MESSAGE_KEY);
	}
	
	/**
	 * 增加一条警告信息
	 * @param request the request
	 * @param message the message
	 */
	public static void addWarnMessage(HttpServletRequest request, String warnMessage) {
		innerAddMessage(request, warnMessage, COHEG_WARN_MESSAGE_KEY);
	}
	
	/**
	 * 清空警告信息
	 * @param request the request
	 */
	public static void clearWarnMessages(HttpServletRequest request) {
		innerClearMessage(request, COHEG_WARN_MESSAGE_KEY);
	}
	
	/**
	 * 增加一条错误信息
	 * @param request the request
	 * @param message the message
	 */
	public static void addErrorMessage(HttpServletRequest request, String errorMessage) {
		innerAddMessage(request, errorMessage, COHEG_ERROR_MESSAGE_KEY);
	}
	
    /**
     * 判断是否已经设置了错误信息, 设置了错误信息表明当前执行过程已经发生了错误
     * @param request 当前执行过程的<code>HttpServletRequest</code>
     * @return 如果已经设置了错误信息, 就是已经发生了错误返回<code>true</code>,否则<code>false</code>
     */
    public static boolean isExistError(HttpServletRequest request) {
        List list = (List) request.getAttribute(COHEG_ERROR_MESSAGE_KEY);
        return list != null && list.size() > 0;
    }

	/**
	 * 清空错误信息
	 * @param request the request
	 */
	public static void clearErrorMessages(HttpServletRequest request) {
		innerClearMessage(request, COHEG_ERROR_MESSAGE_KEY);
	}
	
	private static void innerAddMessage(HttpServletRequest request, String message, String messageKey) {
		if (!isEnabled()) {
			return;
		}
		
		List list = (List) request.getAttribute(messageKey);
		if (list == null) {
			list = new ArrayList();
		}
		String[] messages = MiscUtils.splitWithLineSeparator(StringUtils.trimToEmpty(message));
		for (int i = 0; i < messages.length; i++) {
            list.add(escape(messages[i]));
		}
		request.setAttribute(messageKey, list);
	}
	
	private static void innerClearMessage(HttpServletRequest request, String messageKey) {
		if (!isEnabled()) {
			return;
		}
		request.removeAttribute(messageKey);
	} 
	
    private static final char[] ESCAPE_CHARS = {'\'', '"'};
    private static String escape(String orig) {
        if (StringUtils.isBlank(orig)) {
            return orig;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < orig.length(); i++) {
            if (ArrayUtils.indexOf(ESCAPE_CHARS, orig.charAt(i)) > -1) {
                sb.append('\\');
            }
            sb.append(orig.charAt(i));
        }
        return sb.toString();
    }
}
