package net.silencily.sailing.resource;

import net.silencily.sailing.framework.exception.NoSuchMessageException;



/**
 * <p>这个接口使用编码检索写在配置文件中的信息, 实现了信息与编码的分离, 目的是用于异常信
 * 息, 提示信息等客户化信息. 配置文件是标准的<code>java.util.Properties</code>文件的格式,
 * 但不需要做编码转义(escape)处理, 所有的配置文件<b>一定</b>要在<code>conf</code>类路径下,
 * 配置文件名一定要匹配{@link GlobalParameters#MESSAGE_CODED_CONFIGURATION_LOCATION<code>后缀名</code>}这个模式. 配置信息是
 * 按照目录层次分层加载的, 目录结构应该体现出架构及各应用子系统的逻辑结构, 一个配置层次的组织例子
 * 像下面这样
 * <pre>
 *   + conf
 *     - root-messagecoded.properties
 *     + oa
 *       - oa-messagecoded.properties
 *       + senddoc
 *         - oa-senddoc-messagecoded.properties
 *       + receivedoc
 *         - oa-receive-messagecoded.properties
 *   ...
 * </pre>
 * 如果同一级配置出现相同<code>key</code>的信息就按照文件名的自然顺序(英文字母顺序, 文件名当然
 * 应该是英文字母命名的)先加载的屏蔽后加载的, 如果不是同级目录发生命名冲突, 上一级屏蔽下一级的同名
 * 信息, 这样做的目的是防止新插接进来的业务子系统覆盖掉已经使用的子系统的信息</p>
 * <p>实现应该缓存已经加载的信息, 对于采用什么策略刷新缓存应该由这个组件所在的容器来管理</p>
 * 
 * @author scottcaptain
 * @since 2005-12-19
 * @version $Id: CodedMessageSource.java,v 1.1 2010/12/10 10:54:25 silencily Exp $
 */
public interface CodedMessageSource {
    String SERVICE_NAME = "CodedMessageSource";
    
    /**
     * 检索指定编码的信息, 这个编码的命名方式最好使用可读性好的字符串, 比如显示人员信息的标题的
     * <code>user.folk</code>
     * 
     * @param code 要查找信息的<code>code</code>
     * @return 编码对应的信息
     * @throws IllegalArgumentException 如果参数是<code>null</code>或<code>empty</code>
     * @throws NoSuchMessageException 如果没有找到相应的信息
     */
    String getMessage(String code);

    /**
     * 检索指定编码的信息, 这个编码的命名方式最好使用可读性好的字符串, 比如显示人员信息的标题的
     * <code>user.folk</code>
     * 
     * @param code 要查找信息的<code>code</code>
     * @return 编码对应的信息, 如果没有找到信息, 返回第二个参数, 不管这个参数的值是什么
     * @throws IllegalArgumentException 如果参数是<code>null</code>或<code>empty</code>
     */    
    String getMessage(String code, String defaultMsg);
    
    /**
     * 检索指定编码的信息, 使用参数替换占位符, 替换规则参见{@link java.text.MessageFormat MessageFormat}
     * 类
     * 
     * @param code
     * @param args
     * @return 编码后的信息
     * @throws IllegalArgumentException 如果参数是<code>null</code>或<code>empty</code>
     * @throws NoSuchMessageException 如果没有找到相应的信息
     */
    String getMessage(String code, Object[] args);
    
    /**
     * 检索指定编码的信息, 使用参数替换占位符, 替换规则参见{@link java.text.MessageFormat MessageFormat}
     * 类
     * 
     * @param code
     * @param args
     * @return 编码后的信息, 如果没找到对应的信息, 返回缺省的信息
     * @throws IllegalArgumentException 如果参数是<code>null</code>或<code>empty</code>
     */
    String getMessage(String code, Object[] args, String defaultMessage);
    
    /**
     * 检查是否包含指定编码的信息, 这个方法用于细致地控制程序的信息, 如果直接使用{@link #getMessage(String) getMessage}
     * , 在没有要检索的<code>code</code>时会抛出异常, 使用这个方法可以避免这种情况
     * 
     * @param code 要检查是否包含<code>code</code>的值
     * @return 如果存在这样的外部化信息返回<code>true</code>, 否则<code>false</code>
     */
    boolean exists(String code);
}
