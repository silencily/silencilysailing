package net.silencily.sailing.common.domain.tree;


/**
 * <p><code>TreeNode</code>的抽象实现, 生成<code>tigra's tree menu</code>所需的数据结构格
 * 式的数据. 在前端可以方便地组装<code>tigra</code>树形控件. 它使<code>TreeNode</code>成
 * 为一个<code>Composite</code></p>
 * <p><b>注意</b>要求子类的两个方法{@link TreeNode#getCaptain() <code>getCaptain()</code>}
 * 和{@link TreeNode#getId() <code>getId()</code>}不能返回<code>null</code>, 否则会抛出
 * <code>NullPointerException</code>, 使得难以跟踪问题, 组装树结点时应该验证这两个方法所对应的
 * 属性</p>
 * <p>这种树形结构适合于数据量不大的情形, 不要在大数据量(10000+)使用这种方案</p>
 * @author scott
 * @since 2006-4-7
 * @version $Id: TigraTreeNode.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 *
 */
public abstract class TigraTreeNode extends AbstractTreeNode {
    
    /** <code>tigra's tree</code>数据的标题占位符 */
    private static String TIGRA_TREE_CAPTAIN = "captain";
    
    /** <code>tigra's tree</code>数据的<code>url</code>占位符 */    
    private static String TIGRA_TREE_URL = "id";
    
    /** <code>tigra's tree</code>数据的子节点占位符 */
    private static String TIGRA_TREE_CHILDREN = "<children>";
        
    /** <code>tigra's tree</code>数据一个节点的数据格式 */
    private static String TIGRA_TREE_DATA = "<" + TIGRA_TREE_CAPTAIN + "," + TIGRA_TREE_URL + "," + TIGRA_TREE_CHILDREN + ">";

    /**
     * 返回<code>tigra</code>树形控件中一个节点的数据格式, 形式是[标题, <b>url</b>或<code>id</code>, 同样格式的子节点]
     * @return <code>String</code>, 表示<code>tigra</code>树形控件中一个节点的数据格式
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
