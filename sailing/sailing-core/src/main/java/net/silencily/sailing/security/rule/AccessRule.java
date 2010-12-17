/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project material
 */
package net.silencily.sailing.security.rule;

/**
 * 领域对象访问规则, 方法来自于 ACL 授权, 被控制对象只需要实现 {@link #getObjectMask()} 方法,
 * 其他方法调用 {@link AccessRuleUtils} 即可
 * @see AccessRuleUtils
 * @since 2006-8-5
 * @author java2enterprise
 * @version $Id: AccessRule.java,v 1.1 2010/12/10 10:54:27 silencily Exp $
 */
public interface AccessRule {
	
    /** 所有允许的基本操作权限定义 */
    int NOTHING = 0; // 0
    int ADMINISTRATION = (int) Math.pow(2, 0); // 1
    int READ = (int) Math.pow(2, 1); // 2
    int WRITE = (int) Math.pow(2, 2); // 4  
    // 是否可以创建, 目前仅针对树节点有意义,  指当前节点是否可以创建下级节点
    int CREATE = (int) Math.pow(2, 3); // 8
    int DELETE = (int) Math.pow(2, 4); // 16

    /** 所有允许的组合权限, 由基本权限组成 */
    int READ_WRITE_CREATE_DELETE = READ | WRITE | CREATE | DELETE; // 30
    int READ_WRITE_CREATE = READ | WRITE | CREATE; // 14
    int READ_WRITE = READ | WRITE; // 6
    int READ_WRITE_DELETE = READ | WRITE | DELETE; // 22
	
    /**
     * 所属对象实例的控制权限
     * @return 预定义的权限之一
     */
    int getObjectMask();
    
    /**
     * 对象是否可读
     * @see AccessRuleUtils#isObjectReadable(AccessRule)
     */
	boolean isObjectReadable();
	
	/**
	 * 对象是否可以创建
	 * @see AccessRuleUtils#isObjectCreatble(AccessRule)
	 */
	boolean isObjectCreatable();
	
	/**
	 * 对象是否可写
	 * @see AccessRuleUtils#isObjectWritable(AccessRule)
	 */
	boolean isObjectWritable();
	
	/**
	 * 对象是否可删除
	 * @see AccessRuleUtils#isObjectDeletable(AccessRule)
	 */
	boolean isObjectDeletable();
    
    /**
     * 根据属性名得到某属性的控制权限
     * TODO 尚未实现
     * @param attributeName 属性名
     * @return 预定义的权限之一
     */
    int getAttributeMask(String attributeName);
}
