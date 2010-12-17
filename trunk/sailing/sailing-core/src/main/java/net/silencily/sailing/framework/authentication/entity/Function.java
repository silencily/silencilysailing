package net.silencily.sailing.framework.authentication.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>����һ������, ��Щ�����Ǳ���ʵ����ҵ�������һ�����ܵ�, �����ǳ����ڲ��Ĺ��ܺͶ�����Ӧ��
 * ��¶��<code>web service</code>����</p>
 * <p>��{@link User},{@link Department},{@link Role}��ͬ, �������ʵ�ʰ�ȫʵ�ֵľ���
 * ��, �������־û�(in K366, when ran between kunming to shilin, yunnan)</p>
 * @author scott
 * @since ${Date}
 * @version $Id: Function.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 */
public class Function implements Serializable {
    
    /** ���ܴ���, ����"ϵͳ"."ģ��"."����"�ֲ�������ʽ, ����"humanresource.pay.query" */
    private String code;
    
    /** ��������, ������ƽ���ʾ�ڹ���ά��, ��Ȩ���û��ɲ��������б��� */
    private String name;
    
    /** ���ܵļ�Ҫ����, ���ֵ���ڹ���ά���ͽ�����ʾ */
    private String description;
    
    /** 
     * ��<code>http</code>,<code>web service</code>������û���ʵ������Ҫ�����ڶ����
     * ֵ��������
     */
    private String url;
    
    /**
     * ������ʱ���ܵĲ���, ����֧��<code>${code}</code>��ʽ�Ķ���, ��ִ��ʱ�ɼܹ���̬
     * �ؽ���Ϊʵ�ʵ�ֵ
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
