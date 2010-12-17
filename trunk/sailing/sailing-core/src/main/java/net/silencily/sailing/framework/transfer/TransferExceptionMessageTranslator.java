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
 * 用于转译导入导出过程中出现的异常信息,  得到的信息用于显示到页面上, 你可以注册新的异常类型和相应的异常提示信息
 * @since 2005-9-27
 * @author 王政
 * @version $Id: TransferExceptionMessageTranslator.java,v 1.1 2010/12/10 10:54:19 silencily Exp $
 */
public abstract class TransferExceptionMessageTranslator {
	
	private static Map exceptionMap = new HashMap();
	
	static {
		deregister();
	}
	
	/**
	 * 根据 exceptionClass 得到相应的异常信息, 如果得不到, 将一直向上递归取父类的提示信息直到 {@link Exception}
	 * @param exceptionClass the exception class
	 * @return the message
	 */
	public static String lookup(Class exceptionClass) {
		Assert.notNull(exceptionClass, " must be specified ");
		if (exceptionClass == Exception.class) {
			return "未知异常";
		}
		
		String message = (String) exceptionMap.get(exceptionClass);
		return message != null ? message : lookup(exceptionClass.getSuperclass());
	}
	
	/**
	 * 初始化所有异常转译信息
	 *
	 */
	public static void deregister() {
		register(Exception.class, " 未知异常 ");
		register(RuntimeException.class, " 运行时异常 ");
		register(DataAccessException.class, " 访问数据异常 ");
		register(HibernateException.class, "访问数据异常");
		register(SQLException.class, "访问数据异常");
	}
	
	/**
	 * 注册一个新的异常转译信息
	 * @param exceptionClass 异常类型
	 * @param message 对应的提示信息
	 */
	public static void register(Class exceptionClass, String message) {
		checkArguments(exceptionClass);
		exceptionMap.put(exceptionClass, message);
	}
	
	/**
	 * 取消注册一个新的异常转译信息
	 * @param exceptionClass 异常类型
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
