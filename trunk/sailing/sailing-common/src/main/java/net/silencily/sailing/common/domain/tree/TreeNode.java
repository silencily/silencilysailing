package net.silencily.sailing.common.domain.tree;

import java.util.List;

/**
 * <p>���ڱ�ʾ���Ա�ʾ�����νṹ�е����ڵ�, ��������õ���������<code>jsp</code>�б���������
 * ���Ǻܴ�����νṹ</p>
 * <p>���Բο���Ϣ������Ŀ��������<code>API</code>��ʵ��ǡ����ʹ������ӿڵ�����</p>
 * @author scott
 * @since 2006-4-6
 * @version $Id: TreeNode.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 */
public interface TreeNode {
    
    /**
     * ��������ڵ�ı���
     * @return ����ڵ�ı���
     */
    String getCaptain();
    
    /**
     * ����Ψһ��ʶ����ڵ��<code>id</code>, ��һЩʵ���п�����<code>url</code>, �����ֵ
     * ȡ����ʵ��
     * @return ��ʶ����ڵ��<code>id</code>
     */
    String getId();
    
    /**
     * ��������ڵ�������ӽڵ�, �ӽڵ��˳����ʵ���ض���, ���ܰ���ĳ��˳��, Ҳ�����ǰ��ղ���˳��
     * @return ��ǰ�ڵ�������ӽڵ�, <code>List</code>��Ԫ��������<code>TreeNode</code>,
     *         ���û���ӽڵ㷵��<code>EMPTY_LIST</code>
     */
    List getChildren();
    
    /**
     * һ��<code>utility</code>����, ��������ڵ������е��ӽڵ���ӽڵ���ӽڵ�, һֱ�ݹ���ȥ
     * ֱ��Ҷ�ӽڵ�Ϊֹ. ���ص�����Ԫ�ص�˳����������ڵ�Ϊ�������еĽڵ㶼չ��, �Ӹ���ʼ�������
     * ������˳��
     * @return ����ڵ�������ӽڵ�, <code>List</code>��Ԫ��������<code>TreeNode</code>,
     *         ���û���ӽڵ㷵��<code>EMPTY_LIST</code>
     */
    List getHierarchyChildren();
    
    /**
     * ��ǰ�ڵ��Ƿ���<code>leaf</code>�ڵ�, ͨ��һ���ڵ㲻�������ӽڵ�ŷ���<code>true</code>,
     * ���һ����Ҷ�ӽڵ�û���ӽڵ������������<code>false</code>, ֻ��<code>getChildren().length == 0</code>
     * @return �����<code>leaf</code>�ڵ㷵��<code>true</code>, ����<code>false</code>
     */
    boolean isLeaf();
    
    /**
     * ���ص�ǰ�ڵ��ʾ��������Ϣ, ������һ��<code>domain object</code>, ���Է���<code>null</code>
     * @return ��ǰ�ڵ��ʾ��������Ϣ
     */
    Object getData();

    /**
     * Ϊ��ǰ�ڵ�����һ���ӽڵ�, �������ӽڵ��е�˳��ȡ����ʵ��, ������ĩβ, �����ǰ���ĳ�����򷽷�
     * �趨��˳��
     * @param node Ҫ���ӵ��ӽڵ�
     * @throws NullPointerException ���������<code>null</code>
     * @throws IllegalStateException �������Υ����ʵ�ֵ�Լ��, ���������ظ��Ľڵ��
     */
    void add(TreeNode node);
    
    /**
     * ɾ����ǰ�ڵ��ֱ���ӽڵ�
     * @param node Ҫɾ�����ӽڵ�
     * @throws NullPointerException ���������<code>null</code>
     * @throws BaseBusinessException �������Υ����ʵ�ֵ�Լ��, ���統ǰ�ڵ�û�в���ָ�����ӽڵ�
     */
    void remove(TreeNode node);
    
    /**
     * ��ǰ�ڵ��Ƿ����<code>id</code>�Ǹ���ֵ���ӽڵ�, ���ݹ鵽�ӽڵ���ӽڵ���ӽڵ�. ע��
     * Ҫ�ж�������νṹ���Ƿ���������Ľڵ�, ��ǰ�ڵ�������������ĸ��ڵ�
     * @param id Ҫ����<code>id</code>
     * @return �����ǰ�ڵ�����������ӽڵ�, ��һ����ֱ���ӽڵ�, ����<code>true</code>, ����
     *         <code>false</code>
     */
    boolean existWithId(String id);
    
    /**
     * <p>�ڵ�ǰ�ڵ����ҳ�<code>id</code>�Ǹ���ֵ���ӽڵ�, �ݹ鵽�ӽڵ���ӽڵ���ӽڵ�. ע��
     * Ҫ��������νṹ�����ҳ��������<code>id</code>�Ľڵ�, ��ǰ�ڵ�������������ĸ��ڵ�
     * , ��������������, ����˵���ӽڵ㿪ʼ����</p>
     * <p>����������������ڻ�ø��ڵ��ʹ�ø��ڵ�<code>id</code>��Ϊ��������������, ���Ҫ��
     * �����������֮ǰ�ж��Ƿ��Ǹ��ڵ��<code>id</code></p>
     * @param id Ҫ�����Ľڵ��<code>id</code>
     * @return ��ǰ�ڵ��¾������<code>id</code>ֵ�Ľڵ�
     * @throws NullPointerException �������<code>id</code>��<code>null</code>��
     *         <code>empty</code>
     * @throws IllegalArgumentException ���û���ҵ��������<code>id</code>ֵ�Ľڵ�
     */
    TreeNode findById(String id);
    
    /**
     * ���ر�ʾ����ڵ���ַ�����ʽ, �����ʵ�ֿ������չʾ���ض��ı�����ʽ
     * @return ��������ڵ���ַ��� 
     */
    String toString();
}
