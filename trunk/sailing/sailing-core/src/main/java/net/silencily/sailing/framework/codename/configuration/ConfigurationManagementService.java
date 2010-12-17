package net.silencily.sailing.framework.codename.configuration;

import net.silencily.sailing.framework.codename.AbstractModuleCodeName;

/**
 * ���ù������, ����ӿ��ṩ���û�ά��������Ϣ, Ҳ�����ɱ�̵�ά��������Ϣ, ÿ�������п�ɾ��
 * �Ϳ��޸���������, ���ڿ����û��Ƿ���Ը�������
 * @author zhangli
 * @version $Id: ConfigurationManagementService.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 * @since 2007-6-18
 */
public interface ConfigurationManagementService extends ConfigurationService {
    
    /**
     * ����һ���µ�����, �����������ʱҪ�������ϼ��ڵ��"�ɴ�������", �������ɴ���������
     * <code>false</code>�׳��쳣, ���û�������Ľڵ���ڻ��߲���<code>false</code>�Ϳ�
     * ������ִ��
     * @param codename Ҫ����������
     * @throws IllegalStateException ������ܴ����������
     */
    void create(AbstractModuleCodeName codename);
    
    /**
     * �������е�������Ϣ, �����������Ҫ���"�ɸ�������", �������<code>true</code>�Ͳ���
     * ִ���������
     * @param codename Ҫ���µ�������Ϣ
     * @throws IllegalStateException ������ܸ����������, ����"�ɸ�������"����<code>true</code>
     */
    void update(AbstractModuleCodeName codename);
    
    /**
     * ɾ�����е�������Ϣ, �����������Ҫ���"��ɾ������", �������<code>true</code>�Ͳ���
     * ִ��ɾ������
     * @param codename Ҫɾ����������Ϣ
     * @throws IllegalStateException �������ɾ���������, ����"��ɾ������"����<code>true</code>
     */
    void delete(AbstractModuleCodeName codename);

}
