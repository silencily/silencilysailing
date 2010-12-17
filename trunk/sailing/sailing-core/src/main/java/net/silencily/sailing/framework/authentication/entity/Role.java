package net.silencily.sailing.framework.authentication.entity;

import java.io.Serializable;

import net.silencily.sailing.exception.UnexpectedException;

/**
 * һ���û�ӵ�еĽ�ɫ
 * @author Scott Captain
 * @since 2006-6-18
 * @version $Id: Role.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 */
public class Role implements Serializable, Cloneable {
    /** һ����ɫ�ı���, ͨ���ڳ������������ֵ, �����ƿ��ܻ�仯 */
    private String code;
    
    /** ��ɫ���� */
    private String name;
    
    /** ��ɫ�����Ĳ���, ����ϵͳ�Ĺ��ܽ�ɫû��������� */
    private Department department;
    
    /** 
     * �����ɫ�Ƿ����Ϊһ����λ, ������ϵͳ�Ĺ��ܽ�ɫ, ��λ��Ӧ��ʵ�������о���ĸ�λ, ��
     * ����ʵ����������, ��ϵͳ���ܽ�ɫ�ǿ�����ʵ�ֹ��ܺ��ֿ�����Ա�����
     */
    private boolean job;
    
    /** 
     * �ж������ɫ����Ϊһ����λ����ϵͳ���ܽ�ɫ
     * @return ����Ǹ�λ����<code>true</code>
     */
    public boolean isJob() {
        return job;
    }

    public void setJob(boolean job) {
        this.job = job;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Object clone() {
        Object ret = null;
        try {
            ret = super.clone();
        } catch (CloneNotSupportedException e) {
            throw new UnexpectedException("����clone Role", e);
        }
        return ret;
    }
}
