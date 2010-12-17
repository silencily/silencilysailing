/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project cardAssistant
 */
package net.silencily.sailing.framework.transfer;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.HibernateException;
import org.springframework.dao.DataAccessException;
import org.springframework.util.Assert;

/**
 * ����ת�뵼�뵼�������г��ֵ��쳣��Ϣ,  �õ�����Ϣ������ʾ��ҳ����, �����ע���µ��쳣���ͺ���Ӧ���쳣��ʾ��Ϣ
 * @since 2005-9-27
 * @author ����
 * @version $Id: TransferExceptionMessageTranslator.java,v 1.1 2010/12/10 10:54:19 silencily Exp $
 */
public abstract class TransferExceptionMessageTranslator {
	
	private static Map exceptionMap = new HashMap();
	
	static {
		deregister();
	}
	
	/**
	 * ���� exceptionClass �õ���Ӧ���쳣��Ϣ, ����ò���, ��һֱ���ϵݹ�ȡ�������ʾ��Ϣֱ�� {@link Exception}
	 * @param exceptionClass the exception class
	 * @return the message
	 */
	public static String lookup(Class exceptionClass) {
		Assert.notNull(exceptionClass, " must be specified ");
		if (exceptionClass == Exception.class) {
			return "δ֪�쳣";
		}
		
		String message = (String) exceptionMap.get(exceptionClass);
		return message != null ? message : lookup(exceptionClass.getSuperclass());
	}
	
	/**
	 * ��ʼ�������쳣ת����Ϣ
	 *
	 */
	public static void deregister() {
		register(Exception.class, " δ֪�쳣 ");
		register(RuntimeException.class, " ����ʱ�쳣 ");
		register(DataAccessException.class, " ���������쳣 ");
		register(HibernateException.class, "���������쳣");
		register(SQLException.class, "���������쳣");
	}
	
	/**
	 * ע��һ���µ��쳣ת����Ϣ
	 * @param exceptionClass �쳣����
	 * @param message ��Ӧ����ʾ��Ϣ
	 */
	public static void register(Class exceptionClass, String message) {
		checkArguments(exceptionClass);
		exceptionMap.put(exceptionClass, message);
	}
	
	/**
	 * ȡ��ע��һ���µ��쳣ת����Ϣ
	 * @param exceptionClass �쳣����
	 */
	public static void deregister(Class exceptionClass) {
		checkArguments(exceptionClass);
		exceptionMap.remove(exceptionClass);
	}
	
	/**
	 * @param exceptionClass
	 */
	private static void checkArguments(Class exceptionClass) {
		Assert.notNull(exceptionClass, " exceptionClass must be specified ");
		Assert.isTrue(Exception.class.isAssignableFrom(exceptionClass), " the " + exceptionClass + " must be subclass of " + Exception.class);
	}
	
	
	
	
	
	
	
}
