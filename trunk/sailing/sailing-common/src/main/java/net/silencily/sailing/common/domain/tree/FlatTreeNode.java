/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project publicresource
 */
package net.silencily.sailing.common.domain.tree;

/**
 * ��ƽ���ڵ�, �� {@link TreeNode} ��ͬ����, ��ֻ�����ڵ㱾����Ϣ, ��û�и�����Ϣ, 
 * ������Ϣһ����Ҫͨ�� Service �õ�, ����Ӧ�ó����ǻ�ʽ������
 * @since 2006-7-25
 * @author java2enterprise
 * @version $Id: FlatTreeNode.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 */
public interface FlatTreeNode {

    /**
     * ����Ψһ��ʶ����ڵ��<code>identity</code>, ��һЩʵ���п�����<code>url</code>, �����ֵ
     * ȡ����ʵ��
     * @return ��ʶ����ڵ��<code>identity</code>
     */
	String getIdentity();
	
    /**
     * ��������ڵ�ı���
     * @return ����ڵ�ı���
     */
    String getCaptain();
	
    /**
     * ��ǰ�ڵ��Ƿ���<code>leaf</code>�ڵ�, ͨ��һ���ڵ㲻�������ӽڵ�ŷ���<code>true</code>, �������һ����ҵ�����ϵ�,
     * ���һ����Ҷ�ӽڵ�û���ӽڵ������������<code>false</code>, ֻ��<code>hasChildren() == true</code>
     * @return �����<code>leaf</code>�ڵ㷵��<code>true</code>, ����<code>false</code>
     */
    boolean isLeaf();
    
    /**
     * ��ǰ�ڵ��Ƿ����ӽڵ�, �� {@link #isLeaf()} ��ͬ, �����������˸ýڵ����Ƿ����ӽڵ�, ��ҵ���޹�
     * @return whether has children
     */
    boolean isHasChildren();
	
    /**
     * ���ص�ǰ�ڵ��ʾ��������Ϣ, ������һ��<code>domain object</code>, ���Է���<code>null</code>
     * @return ��ǰ�ڵ��ʾ��������Ϣ
     */
    Object getData();
    
    /**
     * �õ� ui ����ʾ��ͼƬ���� ?
     * @return the image Type
     */
    String getImageType();

    /**
     * �˽ڵ�չʾ�� ui ��ʱ�Ƿ��ѡ, Ĭ��Ӧ��Ϊ true
     * @return whether this node can be selected from ui
     */
    boolean isCanbeSelected();
}
