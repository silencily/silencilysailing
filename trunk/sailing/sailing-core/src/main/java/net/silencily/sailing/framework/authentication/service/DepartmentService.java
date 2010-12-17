package net.silencily.sailing.framework.authentication.service;

import java.util.List;

import net.silencily.sailing.framework.authentication.entity.Department;
import net.silencily.sailing.framework.service.ServiceBaseWithNotAllowedNullParamters;

/**
 * 检索部门信息的服务, 因为部门信息的特殊性所以把这个服务从人事管理拿出来, 以满足没有人事管理
 * 时仍然提出部门信息的检索, 这个服务的实现与{@link AuthenticationService}相同, 也属于集成
 * 范畴的服务, 即使存在人事管理系统
 * TODO:现在仅仅{@link #listChildren(String)}和{@link #listDepartmentCodeNames(String)}
 * 支持{@link Department#isHasChildren()}方法, 补齐！
 * @author zhangli
 * @version $Id: DepartmentService.java,v 1.1 2010/12/10 10:54:23 silencily Exp $
 * @since 2007-4-30
 */
public interface DepartmentService extends ServiceBaseWithNotAllowedNullParamters {
    
    String SERVICE_NAME = "system.departmentService";
    
    /**
     * 检索组织机构中的根节点, 这个节点是一个企业, 比如一个电厂
     * @return 组织机构中的根节点, 不返回<code>null</code>
     */
    Department top();
    
    /**
     * 根据部门<code>id</code>值检索部门信息
     * @param departmentId 要检索的部门的<code>id</code>
     * @return 指定<code>id</code>的部门信息, 不返回<code>null</code>
     * @throws NullPointerException 如果参数是<code>null</code>
     * @throws IllegalArgumentException 如果指定<code>id</code>部门没找到 
     */
    Department loadDepartment(String departmentId);
    
    /**
     * 检索一个部门及这个部门下的子部门, 返回的结果同级顺序是部门的排列顺序
     * @param code 要检索的部门编码
     * @return 部门的列表, 至少有一个元素, 元素类型是{@link Department}
     * @throws IllegalArgumentException 如果不存在这个部门
     */
    List listDepartments(String code);
    
    /**
     * 检索指定<code>id</code>的所有子部门
     * @param parentId 上级部门
     * @return 上级部门的子部门, 元素是{@link DepartmentCodeName}
     */
    List listChildren(String parentId);

    /**
     * 检索一个部门及这个部门的子部门的{@link DepartmentCodeName}, 返回的结果的第一个
     * 元素是参数<code>code</code>, 按照深度优先先序遍历的顺序, 这个方法常用于业务实体
     * 中定义了{@link DepartmentCodeName}属性, 要检索一个部门及所有子部门的情况, 正像
     * 返回的元素类型, 这个方法是一种轻量的, 不要期望返回完整的部门信息
     * 
     * @param code 以这个部门为根检索这个部门下所有的子部门
     * @return 这个部门以及子部门的{@link DepartmentCodeName}, 至少包含一个元素
     * @throws IllegalArgumentException 如果不存在这个部门
     */
    List listDepartmentCodeNames(String code);
}
