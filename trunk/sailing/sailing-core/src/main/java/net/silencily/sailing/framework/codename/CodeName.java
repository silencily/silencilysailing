package net.silencily.sailing.framework.codename;

/**
 * <p>系统中使用的代码名称接口, 这类实体广泛地用于各个子系统, 他们有共同的特征就是由代码和
 * 显示名称组成, 一个实体的属性常实现这个接口, 用于保存<code>code</code>, 而展现<code>name</code></p>
 * <p>系统中共有三类代码<ul>
 * <li>各个子系统使用的代码, 这类代码保存在数据库中, 没有业务特征而且是分类管理的, 比如人事
 * 管理子系统中的民族, 籍贯, 应用程序需要知道分类代码, 象"民族", "籍贯", 各个分类下的代码 
 * 用户可以维护,程序不会依赖于某个代码, 用户新增或删除了某个代码不会影响程序的运行. 系统对这
 * 类代码做了通用的实现, 每个子系统使用的代码要扩展一个缺省的实现{@link AbstractModuleCodeName}</li>
 * <li>映射某个业务实体代码, 这类代码典型有{@link User}, {@link Department}以及表现各个
 * 子系统业务实体的象"设备", "物资"等, 这类代码的特点是在业务实体中有一个对其它业务实体的引用
 * , 而这类引用仅仅是表现为一个或一组代码名称, 实际并不需要建立一个真正的引用关系, 就是需要把
 * 名称展现出来, 比如班组中每个人的工器具清单, 不需要与物资建立强关联. 这样做的原因是多方面的
 * , 首先是没有物资管理子系统班组管理照常使用, 而且也可以分离系统之间在编译级别的耦合</li>
 * <li>基于配置的代码类, 这类代码定义在配置文件中, 由开发人员定义, 用于程序, 如果没有这类代码
 * 程序无法正常使用, 常见的有"信息发布中的信息类型,文字,图片,视频", 工作流中的环节, 这类代码
 * 都对应着特定的程序处理, 不是用户增加一个代码就可以实现一种新的业务的, 也不需要提供界面维护</li></ul>
 * </p>
 * <div>TODO: 通过<code>Proxy</code>来自动实现基于业务实体(第二种)的<code>CodeName</code>,以
 * 方便获取除了<code>code</code>和<code>name</code>外的其他属性</div>
 * @author zhangli
 * @since 2007-3-16
 * @version $Id: CodeName.java,v 1.1 2010/12/10 10:54:16 silencily Exp $
 * @see AbstractModuleCodeName
 */
public interface CodeName extends Cloneable {
    /**
     * 检索这个实例的<code>code</code>
     * @return 实例的<code>code</code>
     */
    String getCode();
    
    /**
     * 检索这个实例的<code>name</code>
     * @return 这个实例的<code>name</code>
     */
    String getName();
}
