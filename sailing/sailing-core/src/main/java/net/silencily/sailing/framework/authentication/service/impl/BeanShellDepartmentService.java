package net.silencily.sailing.framework.authentication.service.impl;

import java.util.List;
import java.util.Map;

import net.silencily.sailing.framework.authentication.entity.Department;
import net.silencily.sailing.framework.authentication.service.DepartmentService;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 部门服务的<code>BeanShell</code>集成实现
 * @author zhangli
 * @version $Id: BeanShellDepartmentService.java,v 1.1 2010/12/10 10:54:27 silencily Exp $
 * @since 2007-5-1
 */
public class BeanShellDepartmentService extends BeanShellIntegrationService implements DepartmentService {
    
    private static final String KEY_JDBC_TEMPLATE = "jdbcTemplate";
    private static final String KEY_TOP_DEPARTMENT = "topDepartment";
    
    private JdbcTemplate jdbcTemplate;
    
    private String topDepartment = "0000";
    
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public void setTopDepartment(String top) {
        this.topDepartment = top;
    }
    
    private DepartmentService getSerivce() {
        return (DepartmentService) service;
    }
    
    public List listDepartments(String code) {
        List result = getSerivce().listDepartments(code);
        if (result.size() == 0) {
            throw new IllegalArgumentException("没有指定编码的部门[" + code + "]");
        }
        return result;
    }

    public List listDepartmentCodeNames(String code) {
        List result = getSerivce().listDepartmentCodeNames(code);
        if (result.size() == 0) {
            throw new IllegalArgumentException("没有指定编码的部门[" + code + "]");
        }
        return result;
    }

    public Department loadDepartment(String departmentId) {
        return getSerivce().loadDepartment(departmentId);
    }

    public Department top() {
        return getSerivce().top();
    }

    protected Class getServiceClass() {
        return DepartmentService.class;
    }

    protected void addBeanShellRequiredVariables(Map variables) {
        variables.put(KEY_JDBC_TEMPLATE, jdbcTemplate);
        variables.put(KEY_TOP_DEPARTMENT, topDepartment);
    }

    public List listChildren(String parentId) {
        return getSerivce().listChildren(parentId);
    }
}
