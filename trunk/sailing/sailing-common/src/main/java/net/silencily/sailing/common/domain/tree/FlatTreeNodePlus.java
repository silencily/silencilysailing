/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project publicresource
 */
package net.silencily.sailing.common.domain.tree;

/**
 * ��ƽ���ڵ�, �и�����Ϣ
 * @since 2008-7-11
 * @author java2enterprise
 * @version $Id: FlatTreeNodePlus.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 */
public interface FlatTreeNodePlus extends FlatTreeNode {

	public String getId();
	public FlatTreeNodePlus getTreeFather(); 
	
}
