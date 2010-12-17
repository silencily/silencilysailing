/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project framework
 */
package net.silencily.sailing.business;

/**
 * �ɱ���ʼ���ķ���ӿ�, �����Ҫ��ʼ��һЩ���������ڱ�������ݻ�������Դ, ʵ�ִ˽ӿڲ�ע�ᵽ spring ������,
 * �ܹ���������ʼ�����ʱ���Զ�������Щ bean ��ִ������, ע��ִ��˳���ǲ��̶���, Ҳ����ζ������ Initialiazble ��ʵ����
 * ���ܻ��������ڶԷ���ִ�н��
 * @since 2006-7-28
 * @author java2enterprise
 * @version $Id: Initializable.java,v 1.1 2010/12/10 10:54:27 silencily Exp $
 */
public interface Initializable {
	
	/**
	 * spring ������ʼ�����ʱ����, do any thing you want do
	 *
	 */
	void init();
	
}
