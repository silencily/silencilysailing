package net.silencily.sailing.framework.persistent.filter;

import net.silencily.sailing.framework.core.GlobalParameters;

/**
 * 用于操作持久化层操作条件的服务, 提供了注册<code>DtoMetadata</code>的方法. 一个<code>
 * DtoMetadata</code>提供<code>DTO</code>和保存其属性的持久化实体之间的对应关系
 * @author scott
 * @since 2006-5-3 2:04
 * @version $Id: ConditionService.java,v 1.1 2010/12/10 10:54:16 silencily Exp $
 * @see DtoMetadata
 * @see DefaultDtoMetadata
 */
public interface ConditionService {
    String SERVICE_NAME = GlobalParameters.MODULE_NAME + ".conditionService";
    
    /**
     * 为一个<code>DTO</code>注册一个元数据处理器, 如果已经存在相同类型的处理器, 就用新的
     * 替换旧的. 参数必须提供是什么类型的<code>Metadata</code>
     * @param metadata    要注册的<code>DtoMetadata</code>
     * @return <b>如果类型<code>dto</code>还没有注册就返回<code>null</code></b>, 否则
     *         返回原有的<code>DtoMetadata</code>
     * @throws NullPointerException 如果参数是<code>null</code>         
     * @throws IllegalArgumentException 如果参数没有提供要服务的<code>DTO</code>的类型, 
     *         (<code>DtoMetadata.getDtoTpye() == null</code>)
     */
    DtoMetadata registry(DtoMetadata metadata);
    
    /**
     * 取消类型为<code>type</code>的<code>DtoMetadata</code>的注册
     * @param type 要取消元信息注册的<code>dto</code>类型
     * @return 如果成功取消返回<code>true</code>, 如果没有这个类型的源信息返回<code>false</code>
     * @throws NullPointerException 如果参数是<code>null</code>
     */
    boolean deregistry(Class type);
    
    /**
     * 为参数指定的<code>dto</code>检索元信息, 如果这个<code>dto</code>没有注册元信息或
     * 参数是<code>null</code>, 返回缺省的<code>DefaultDtoMetadata</code>
     * @param dto 要检索元信息的<code>dto</code>类型
     * @return 这个<code>dto</code>的元信息
     */
    DtoMetadata getDtoMetadata(Class dto);
}
