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
 * Action ������������ʾ��Ϣ���û��İ�����, ҵ��ҳ����Ҫ���� /decorator/messages.jspf ������ʾ
 * @since 2006-6-13
 * @author ����
 * @version $Id: MessageUtils.java,v 1.1 2010/12/10 10:54:21 silencily Exp $
 */
public abstract class MessageUtils {
	
	/** ������� success messages �� reqeust key ֵ */
	public static final String COHEG_MESSAGE_KEY = "_com_coheg_messages_key_";
	
	/** ������� warn messages �� request key ֵ */
	public static final String COHEG_WARN_MESSAGE_KEY = "_com_coheg_warn_messages_key_";
	
	/** ������� error messages �� reqeust key ֵ */
	public static final String COHEG_ERROR_MESSAGE_KEY = "_com_coheg_error_messages_key_";
	
	/** Ĭ�������ɹ�����ʾ��Ϣ */
	public static final String DEFAULT_ADD_SUCCESS_MESSAGE = " �����ɹ�, �����ڿ����޸�������¼ ";
	
	/** Ĭ���޸ĳɹ�����ʾ��Ϣ */
	public static final String DEFAULT_EDIT_SUCCESS_MESSAGE = " �޸ĳɹ�, �����ڿ��Լ����޸�������¼ ";
	
	/** Ĭ��ɾ���ɹ�����ʾ��Ϣ */
	public static final String DEFAULT_DELETE_SUCCESS_MESSAGE = " ��¼�ѱ�ɾ�� ";
	
	
	/** �Ƿ��ֹmessage */
	private static boolean enabled = true;
	
	/**
	 * ��ֹ��Ϣ����
	 *
	 */
	public static void disableMessage() {
		enabled = false;
	}
	
	/**
	 * ������Ϣ����
	 *
	 */
	public static void enableMessage() {
		enabled = true;
	}
	
	/**
	 * ��Ϣ�����Ƿ񼤻�
	 * @return ��Ϣ�����Ƿ񼤻�
	 */
	public static boolean isEnabled() {
		return enabled;
	}
	
	/**
	 * ����һ����ʾ��Ϣ
	 * @param request the request
	 * @param message the message
	 */
	public static void addMessage(HttpServletRequest request, String message) {
		innerAddMessage(request, message, COHEG_MESSAGE_KEY);
	}

	/**
	 * �����ʾ��Ϣ
	 * @param request the request
	 */
	public static void clearMessages(HttpServletRequest request) {
		innerClearMessage(request, COHEG_MESSAGE_KEY);
	}
	
	/**
	 * ����һ��������Ϣ
	 * @param request the request
	 * @param message the message
	 */
	public static void addWarnMessage(HttpServletRequest request, String warnMessage) {
		innerAddMessage(request, warnMessage, COHEG_WARN_MESSAGE_KEY);
	}
	
	/**
	 * ��վ�����Ϣ
	 * @param request the request
	 */
	public static void clearWarnMessages(HttpServletRequest request) {
		innerClearMessage(request, COHEG_WARN_MESSAGE_KEY);
	}
	
	/**
	 * ����һ��������Ϣ
	 * @param request the request
	 * @param message the message
	 */
	public static void addErrorMessage(HttpServletRequest request, String errorMessage) {
		innerAddMessage(request, errorMessage, COHEG_ERROR_MESSAGE_KEY);
	}
	
    /**
     * �ж��Ƿ��Ѿ������˴�����Ϣ, �����˴�����Ϣ������ǰִ�й����Ѿ������˴���
     * @param request ��ǰִ�й��̵�<code>HttpServletRequest</code>
     * @return ����Ѿ������˴�����Ϣ, �����Ѿ������˴��󷵻�<code>true</code>,����<code>false</code>
     */
    public static boolean isExistError(HttpServletRequest request) {
        List list = (List) request.getAttribute(COHEG_ERROR_MESSAGE_KEY);
        return list != null && list.size() > 0;
    }

	/**
	 * ��մ�����Ϣ
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
