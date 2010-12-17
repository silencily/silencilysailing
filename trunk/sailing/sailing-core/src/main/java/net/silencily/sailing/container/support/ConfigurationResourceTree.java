package net.silencily.sailing.container.support;


/**
 * 把系统中的<code>beans</code>配置资源组织成树结构, 这个类表示树结构中的资源节点, 用于
 * {@link net.silencily.sailing.container.ServiceProvider <code>ServiceProvider</code>}
 * 初始化配置
 * 
 * @author scott
 * @since 2005-12-27
 * @version $Id: ConfigurationResourceTree.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 *
 */
public class ConfigurationResourceTree implements Comparable {
    /**
     * 配置资源全路径名称, 就是<code>path + "/" + name</code>
     */
    private String resourceFullName;

    public ConfigurationResourceTree(String resourceFullName) {
        this.resourceFullName = resourceFullName;
    }
    
    /**
     * 检索配置资源全路径名称, 就是<code>path + "/" + name</code>
     * @return 配置资源全路径名称, 就是<code>path + "/" + name</code>
     */
    public String getResourceFullName() {
        return this.resourceFullName;
    }

    /**
     * 检索当前配置资源的<code>url style</code>路径, <b>不包括资源名称</b>
     * @return 当前配置资源的<code>url style</code>路径
     */
    public String getPath() {
        if (slashPosition() == -1) {
            return new String();
        } else {
            return this.resourceFullName.substring(0, slashPosition());
        }
    }
    
    /**
     * 检索当前配置资源的名称, 不包括路径信息
     * @return 当前配置资源的名称
     */
    public String getName() {
        return this.resourceFullName.substring(slashPosition() + 1);
    }
    
    /**
     * 检索当前资源的上级路径, 对于根目录下的资源返回<code>empty string</code>
     * @return 当前资源的上级路径
     */
    public String getParentPath() {
        String ret = "";
        if (getPath().split("/").length > 1) {
            ret = getPath().replaceAll("/[^/]+$", "");
        }
        
        return ret;
    }
    
    /**
     * 计算当前资源路径的级别数
     * @return 当前资源路径的级别数
     */
    public int getPathLevel() {
        return getPath().equals(new String()) ? 0 : getPath().split("/").length;
    }
    /**
     * 判断参数是当前资源所在路径下一级资源
     * 
     * @param tree 要判断的资源
     * @return 如果参数是当前资源下直接的子节点返回<code>true</code>
     */
    public boolean hasChild(ConfigurationResourceTree tree) {
        return tree.getParentPath().startsWith(getPath());
    }
    
    /**
     * 检索当前资源路径中最后一个"/"的位置
     * @return 当前资源路径中最后一个"/"的位置
     */
    private int slashPosition() {
        return this.resourceFullName.lastIndexOf('/');
    }
    
    /**
     * 排序策略: 目录层数大的排在后面, 如果层数相同, 按照路径(不包括资源名称)字母顺序排, 如果路
     * 径相同按照资源名称排
     */
    public int compareTo(Object o) {
        ConfigurationResourceTree tree = (ConfigurationResourceTree) o;

        int ret = getPath().compareTo(tree.getPath());
        if (ret == 0) {
            ret = getName().compareTo(tree.getName());
        }

        return ret;
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer("CnfigurationResourceTree:")
            .append("resourceFullName=")
            .append(this.resourceFullName)
            .append(",path=")
            .append(getPath())
            .append(",name=")
            .append(getName());
        
        return sb.toString();        
    }
}
