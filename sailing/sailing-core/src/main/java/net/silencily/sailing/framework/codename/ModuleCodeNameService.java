package net.silencily.sailing.framework.codename;

import java.util.Collection;
import java.util.Map;

import net.silencily.sailing.framework.SystemConstants;
import net.silencily.sailing.framework.core.ServiceBase;

/**
 * 检索{@link CodeName}的服务, 注意因为<code>CodeName</code>属于实体类型, 所以所有方法的
 * 返回值, 包括集合中的元素一定可以<code>cast</code>为<code>Entity</code>
 * @author zhangli
 * @since 2007-3-16
 * @version $Id: ModuleCodeNameService.java,v 1.1 2010/12/10 10:54:16 silencily Exp $
 * @see CodeName
 */
public interface ModuleCodeNameService extends ServiceBase {
    
    String SERVICE_NAME = SystemConstants.MODULE_NAME + ".moduleCodeNameService";
    
    /**
     * 用于控制{@link #list(Class)}方法返回的结果集数量, 如果超过这个上限会抛出
     * {@link TooManyCodeNameDefinedException}, 这是一个缺省值, 具体的实现
     * 可以通过配置修改这个参数
     */
    int MAX_RESULT_SET = 1000;
    
    /**
     * 检索指定类型和名称的{@link CodeName}, 可以返回<code>null</code>的原因是在某些时期
     * 可能增加了几个代码, 过了那个阶段这些代码不再使用了, 而引用这些代码的数据仍然由这些代码的
     * 信息, 这样可能就出现这种情况, 但是代码采用只加不删的办法显然是过时的策略, 可以有其他的方案
     * 处理这类情况
     * @param clazz 实现了{@link CodeName}的专业等具体实现类的<code>Class</code>
     * @param code  要检索的<code>code</code>值
     * @return 要检索的{@link CodeName}, <b>可能返回<code>null</code></b>
     * @throws NullPointerException 如果任何一个参数是<code>null</code>或<code>empty</code>
     * @throws ClassCastException 如果参数<code>codeNameType</code>不是<code>CodeName</code>类型
     */
    CodeName load(Class codeNameType, String code);
    
    /**
     * <p>列出指定类型的所有{@link CodeName}. 再次强调的是{@link CodeName}是用来统一地表现系
     * 统中没有特定业务功能的代码类, 这些数据量不会很大, 如果定义的{@link CodeName}是大量的业
     * 务数据, 比如竟然用{@link CodeName}来定义物资编码那么结果可能是<code>out of memory</code>
     * ,{@link #MAX_RESULT_SET}参数目的就是限制这种可能的意外, 当结果集超过这个限制时余下的数据
     * 不会检索进来, 这个接口的实现类可以配置这个参数, 结果集的顺序是按照{@link CodeName#getOrder()}返回值排序的</p>
     * @param codeNameType {@link CodeName}具体实现类的<code>Class</code>
     * @return 指定类型的所有代码, 如果没有结果集返回<code>Collections.EMPTY_LIST</code>
     * @throws NullPointerException 如果参数是<code>null</code>
     * @throws ClassCastException 如果参数<code>codeNameType</code>不是<code>CodeName</code>类型
     */
    Collection list(Class codeNameType);
    
    /**
     * 执行与{@link #list(Class)相同的功能, 返回一个有序的<code>Map</code>
     * @return <code>Map's key</code>是{@link CodeName#getCode()}的值, <code>Map's value</code>
     * 就是{@link CodeName}本身, 其顺序与{@link CodeName}本身的顺序相同, 如果没有结果集返回
     * <code>Collections.EMPTY_MAP</code>
     * @throws NullPointerException 如果参数是<code>null</code>
     * @throws ClassCastException 如果参数<code>codeNameType</code>不是<code>CodeName</code>类型
     * @see #list(Class)
     */
    Map map(Class codeNameType);
    
    /**
     * 列出以参数为父编码的所有的子编码
     * @param clazz      要检索的代码类型
     * @param parentCode 编码
     * @return 以参数为父编码的所有的子编码, 元素类型是参数<code>clazz</code>指定的类型,
     *         如果没有数据返回<code>Collections.EMPTY_LIST</code>
     * @throws NullPointerException 如果参数是<code>null</code>,<code>empty</code>
     */
    Collection list(Class clazz, String parentCode);

}
