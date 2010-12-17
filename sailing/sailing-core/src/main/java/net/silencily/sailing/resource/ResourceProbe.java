package net.silencily.sailing.resource;

/**
 * 从类路径的目录下或<code>jar</code>文件中搜索指定名称的资源, 资源名称可以按照<code>ant</code>
 * 风格的匹配模式指定, 比如:
 * <pre>
 *   <code>/com/88/person*</code> --> com/coheg/person.xml, com/power/personal, etc.
 * </pre>
 * 这些资源名称满足<class>ClassLoader.getResources()</class>检索资源的条件, 主要用于在类路径
 * 的资源中查找资源
 *  
 * @author scottcaptain
 * @since 2005-12-20
 * @version $Id: ResourceProbe.java,v 1.1 2010/12/10 10:54:25 silencily Exp $
 */
public interface ResourceProbe {
    
    /**
     * 判断一个资源名称是否是一个<code>ant-style</code>的匹配模式
     * 
     * @param resourceName 要判断的资源名称
     * @return 如果是<code>ant-style</code>匹配模式返回<code>true</code>, 对于空值返回
     * <code>false</code>
     */
    boolean isPattern(String resourceName);
        
    /**
     * 按照指定模式搜索匹配的资源名, 这些资源名称可以使用<code>ClassLoader</code>
     * 加载, 结果是按照<code>String</code>的比较顺序排序的
     * 
     * @param resourceName 要搜索的资源名称模式, 可以是以","分隔的多个模式
     * @return 搜索到的资源名称数组, 如果没找到资源返回长度为<b>0</b>的数组
     * @throws IllegalArgumentException 如果参数是<code>null</code>或<code>empty</code>
     */
    String[] search(String resourceName);
    
    /**
     * 按照指定模式搜索匹配的资源名, 这些资源名称可以使用<code>ClassLoader</code>加载, 结果
     * 是按照字母比较顺序排序的, 第二个参数用于指定排除模式, 凡是符合这个模式的资源都被排除掉
     * 
     * @param resourceName 要搜索的资源名称模式, 可以是以","分隔的多个模式
     * @param elimination  从结果中排除的资源模式, 可以是以","分隔的多个模式
     * @return 搜索到的资源名称数组, 如果没找到资源返回长度为<b>0</b>的数组
     * @throws IllegalArgumentException 如果参数是<code>null</code>或<code>empty</code>
     */
    String[] search(String resourceName, String elimination);
}
