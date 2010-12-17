package net.silencily.sailing.framework.authentication.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>表现一个功能, 这些功能是表现实现了业务需求的一个功能点, 或者是程序内部的功能和对其他应用
 * 暴露的<code>web service</code>功能</p>
 * <p>与{@link User},{@link Department},{@link Role}相同, 这个类是实际安全实现的镜像
 * 类, 本身不被持久化(in K366, when ran between kunming to shilin, yunnan)</p>
 * @author scott
 * @since ${Date}
 * @version $Id: Function.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 */
public class Function implements Serializable {
    
    /** 功能代码, 按照"系统"."模块"."功能"分层命名格式, 例如"humanresource.pay.query" */
    private String code;
    
    /** 功能名称, 这个名称将显示在功能维护, 授权和用户可操作功能列表中 */
    private String name;
    
    /** 功能的简要描述, 这个值用于功能维护和界面提示 */
    private String description;
    
    /** 
     * 在<code>http</code>,<code>web service</code>情况下用户的实际请求要体现在对这个
     * 值的请求上
     */
    private String url;
    
    /**
     * 请求功能时接受的参数, 参数支持<code>${code}</code>格式的定义, 在执行时由架构动态
     * 地解析为实际的值
     */
    private Map parameters = new HashMap();

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map getParameters() {
        return parameters;
    }

    public void setParameters(Map parameters) {
        this.parameters = parameters;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
