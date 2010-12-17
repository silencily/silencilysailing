package net.silencily.sailing.container.support;


/**
 * ��ϵͳ�е�<code>beans</code>������Դ��֯�����ṹ, ������ʾ���ṹ�е���Դ�ڵ�, ����
 * {@link net.silencily.sailing.container.ServiceProvider <code>ServiceProvider</code>}
 * ��ʼ������
 * 
 * @author scott
 * @since 2005-12-27
 * @version $Id: ConfigurationResourceTree.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 *
 */
public class ConfigurationResourceTree implements Comparable {
    /**
     * ������Դȫ·������, ����<code>path + "/" + name</code>
     */
    private String resourceFullName;

    public ConfigurationResourceTree(String resourceFullName) {
        this.resourceFullName = resourceFullName;
    }
    
    /**
     * ����������Դȫ·������, ����<code>path + "/" + name</code>
     * @return ������Դȫ·������, ����<code>path + "/" + name</code>
     */
    public String getResourceFullName() {
        return this.resourceFullName;
    }

    /**
     * ������ǰ������Դ��<code>url style</code>·��, <b>��������Դ����</b>
     * @return ��ǰ������Դ��<code>url style</code>·��
     */
    public String getPath() {
        if (slashPosition() == -1) {
            return new String();
        } else {
            return this.resourceFullName.substring(0, slashPosition());
        }
    }
    
    /**
     * ������ǰ������Դ������, ������·����Ϣ
     * @return ��ǰ������Դ������
     */
    public String getName() {
        return this.resourceFullName.substring(slashPosition() + 1);
    }
    
    /**
     * ������ǰ��Դ���ϼ�·��, ���ڸ�Ŀ¼�µ���Դ����<code>empty string</code>
     * @return ��ǰ��Դ���ϼ�·��
     */
    public String getParentPath() {
        String ret = "";
        if (getPath().split("/").length > 1) {
            ret = getPath().replaceAll("/[^/]+$", "");
        }
        
        return ret;
    }
    
    /**
     * ���㵱ǰ��Դ·���ļ�����
     * @return ��ǰ��Դ·���ļ�����
     */
    public int getPathLevel() {
        return getPath().equals(new String()) ? 0 : getPath().split("/").length;
    }
    /**
     * �жϲ����ǵ�ǰ��Դ����·����һ����Դ
     * 
     * @param tree Ҫ�жϵ���Դ
     * @return ��������ǵ�ǰ��Դ��ֱ�ӵ��ӽڵ㷵��<code>true</code>
     */
    public boolean hasChild(ConfigurationResourceTree tree) {
        return tree.getParentPath().startsWith(getPath());
    }
    
    /**
     * ������ǰ��Դ·�������һ��"/"��λ��
     * @return ��ǰ��Դ·�������һ��"/"��λ��
     */
    private int slashPosition() {
        return this.resourceFullName.lastIndexOf('/');
    }
    
    /**
     * �������: Ŀ¼����������ں���, ���������ͬ, ����·��(��������Դ����)��ĸ˳����, ���·
     * ����ͬ������Դ������
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
