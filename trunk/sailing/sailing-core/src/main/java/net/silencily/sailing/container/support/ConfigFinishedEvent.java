package net.silencily.sailing.container.support;

import org.springframework.context.ApplicationEvent;

/**
 * ��־ϵͳ�������ù�����ɵ��¼�, ����¼�����ҪĿ�����ڷ�����ʱ���ȿ��������������ź�
 */
@SuppressWarnings("serial")
public class ConfigFinishedEvent extends ApplicationEvent {
    public ConfigFinishedEvent() {
        super("config finished");
    }
}
