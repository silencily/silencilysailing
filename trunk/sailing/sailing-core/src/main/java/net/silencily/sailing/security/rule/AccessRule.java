/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project material
 */
package net.silencily.sailing.security.rule;

/**
 * ���������ʹ���, ���������� ACL ��Ȩ, �����ƶ���ֻ��Ҫʵ�� {@link #getObjectMask()} ����,
 * ������������ {@link AccessRuleUtils} ����
 * @see AccessRuleUtils
 * @since 2006-8-5
 * @author java2enterprise
 * @version $Id: AccessRule.java,v 1.1 2010/12/10 10:54:27 silencily Exp $
 */
public interface AccessRule {
	
    /** ��������Ļ�������Ȩ�޶��� */
    int NOTHING = 0; // 0
    int ADMINISTRATION = (int) Math.pow(2, 0); // 1
    int READ = (int) Math.pow(2, 1); // 2
    int WRITE = (int) Math.pow(2, 2); // 4  
    // �Ƿ���Դ���, Ŀǰ��������ڵ�������,  ָ��ǰ�ڵ��Ƿ���Դ����¼��ڵ�
    int CREATE = (int) Math.pow(2, 3); // 8
    int DELETE = (int) Math.pow(2, 4); // 16

    /** ������������Ȩ��, �ɻ���Ȩ����� */
    int READ_WRITE_CREATE_DELETE = READ | WRITE | CREATE | DELETE; // 30
    int READ_WRITE_CREATE = READ | WRITE | CREATE; // 14
    int READ_WRITE = READ | WRITE; // 6
    int READ_WRITE_DELETE = READ | WRITE | DELETE; // 22
	
    /**
     * ��������ʵ���Ŀ���Ȩ��
     * @return Ԥ�����Ȩ��֮һ
     */
    int getObjectMask();
    
    /**
     * �����Ƿ�ɶ�
     * @see AccessRuleUtils#isObjectReadable(AccessRule)
     */
	boolean isObjectReadable();
	
	/**
	 * �����Ƿ���Դ���
	 * @see AccessRuleUtils#isObjectCreatble(AccessRule)
	 */
	boolean isObjectCreatable();
	
	/**
	 * �����Ƿ��д
	 * @see AccessRuleUtils#isObjectWritable(AccessRule)
	 */
	boolean isObjectWritable();
	
	/**
	 * �����Ƿ��ɾ��
	 * @see AccessRuleUtils#isObjectDeletable(AccessRule)
	 */
	boolean isObjectDeletable();
    
    /**
     * �����������õ�ĳ���ԵĿ���Ȩ��
     * TODO ��δʵ��
     * @param attributeName ������
     * @return Ԥ�����Ȩ��֮һ
     */
    int getAttributeMask(String attributeName);
}
