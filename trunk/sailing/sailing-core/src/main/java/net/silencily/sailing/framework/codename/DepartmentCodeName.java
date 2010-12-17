package net.silencily.sailing.framework.codename;

import net.silencily.sailing.framework.authentication.entity.Department;
import net.silencily.sailing.framework.authentication.entity.Principal;
import net.silencily.sailing.framework.authentication.entity.User;

import org.apache.commons.lang.StringUtils;

/**
 * <p>用于业务实体中引用部门作为属性的<code>CodeName</code>类, 检索业务实体时, 相关的
 * 组件负责填写部门名称及相关的信息, 保存业务实体时, 相关组件负责把部门的<code>id</code>
 * 写回到业务实体相关的数据库表中, 为什么是<code>CodeName</code>而不是<code>Department</code>
 * :原因是这两个类使用环境不同, 实现细节也不同, 尤其是<code>equals</code></p>
 * <p>作为性能, 耦合度等方面的因素, 我们推荐以这种方式使用基础的实体, 而不是建立强关联, 特
 * 别是在要引用的实体完全有其它子系统负责维护, 而且实际用到的属性很少的情况下. 同时作为一个
 * 命名的规范, 以要引用实体加"CodeName"来定义这种类型的<code>CodeName</code></p>
 * @author zhangli
 * @version $Id: DepartmentCodeName.java,v 1.1 2010/12/10 10:54:16 silencily Exp $
 * @since 2007-4-8
 * @see CodeName
 */
public class DepartmentCodeName extends Department implements Principal {
    
    public DepartmentCodeName copyFrom(Department department) {
        setCode(department.getId());
        setName(department.getName());
        setParent(department.getParent());
        setProperties(department.getProperties());
        setHasChildren(department.isHasChildren());
        return this;
    }
    
    /**
     * 检索部门<code>id</code>的方法
     * @return 部门<code>id</code>
     */
    public String getCode() {
        return getId();
    }
    
    /**
     * 设置一个部门的<code>id</code>, 这个方法不是用来修改{@link Department}, 而是修改
     * 业务实体引用的部门的<code>id</code>, 常用于<code>java bean</code>中的设置属性,
     * 比如在页面上修改了一个业务实体的相关部门
     * @param code 
     */
    public void setCode(String code) {
        setId(code);
    }
    
    /**
     * 判断参数指定的用户是否属于这个部门
     * @return 如果属于这个部门返回<code>true</code>
     */
    public boolean pass(User user) {
        return user.getDepartment().getId().equals(getId());
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (getClass() != o.getClass()) {
            return false;
        }
        DepartmentCodeName d = (DepartmentCodeName) o;
        return StringUtils.equals(getCode(), d.getCode());
    }
    
    public int hashCode() {
        if (StringUtils.isBlank(getCode())) {
            return super.hashCode();
        } else {
            return getClass().hashCode() * 29 + getCode().hashCode();
        }
    }
}
