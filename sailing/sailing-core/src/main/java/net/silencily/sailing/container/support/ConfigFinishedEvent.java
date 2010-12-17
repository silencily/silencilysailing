package net.silencily.sailing.container.support;

import org.springframework.context.ApplicationEvent;

/**
 * 标志系统加载配置过程完成的事件, 这个事件的主要目的用于发出定时器等可以正常工作的信号
 */
@SuppressWarnings("serial")
public class ConfigFinishedEvent extends ApplicationEvent {
    public ConfigFinishedEvent() {
        super("config finished");
    }
}
