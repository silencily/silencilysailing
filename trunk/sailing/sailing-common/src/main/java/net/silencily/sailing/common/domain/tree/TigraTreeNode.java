package net.silencily.sailing.common.domain.tree;


/**
 * <p><code>TreeNode</code>�ĳ���ʵ��, ����<code>tigra's tree menu</code>��������ݽṹ��
 * ʽ������. ��ǰ�˿��Է������װ<code>tigra</code>���οؼ�. ��ʹ<code>TreeNode</code>��
 * Ϊһ��<code>Composite</code></p>
 * <p><b>ע��</b>Ҫ���������������{@link TreeNode#getCaptain() <code>getCaptain()</code>}
 * ��{@link TreeNode#getId() <code>getId()</code>}���ܷ���<code>null</code>, ������׳�
 * <code>NullPointerException</code>, ʹ�����Ը�������, ��װ�����ʱӦ����֤��������������Ӧ��
 * ����</p>
 * <p>�������νṹ�ʺ������������������, ��Ҫ�ڴ�������(10000+)ʹ�����ַ���</p>
 * @author scott
 * @since 2006-4-7
 * @version $Id: TigraTreeNode.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 *
 */
public abstract class TigraTreeNode extends AbstractTreeNode {
    
    /** <code>tigra's tree</code>���ݵı���ռλ�� */
    private static String TIGRA_TREE_CAPTAIN = "captain";
    
    /** <code>tigra's tree</code>���ݵ�<code>url</code>ռλ�� */    
    private static String TIGRA_TREE_URL = "id";
    
    /** <code>tigra's tree</code>���ݵ��ӽڵ�ռλ�� */
    private static String TIGRA_TREE_CHILDREN = "<children>";
        
    /** <code>tigra's tree</code>����һ���ڵ�����ݸ�ʽ */
    private static String TIGRA_TREE_DATA = "<" + TIGRA_TREE_CAPTAIN + "," + TIGRA_TREE_URL + "," + TIGRA_TREE_CHILDREN + ">";

    /**
     * ����<code>tigra</code>���οؼ���һ���ڵ�����ݸ�ʽ, ��ʽ��[����, <b>url</b>��<code>id</code>, ͬ����ʽ���ӽڵ�]
     * @return <code>String</code>, ��ʾ<code>tigra</code>���οؼ���һ���ڵ�����ݸ�ʽ
     */
    public final String toString() {
        String data = new String(TIGRA_TREE_DATA);
        data = data.replaceFirst(TIGRA_TREE_CAPTAIN, "'" + this.getCaptain() + "'");
        data = data.replaceFirst(TIGRA_TREE_URL, "'" + this.getId() + "'");

        for (int i = 0; i < this.getChildren().size(); i++) {
            data = data.replaceFirst(TIGRA_TREE_CHILDREN, ((TreeNode) this.getChildren().get(i)).toString() + "," + TIGRA_TREE_CHILDREN); 
        }

        return data.replaceFirst(TIGRA_TREE_CHILDREN, "").replace('<', '[').replace('>', ']');
    }
}
