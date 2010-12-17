package net.silencily.sailing.framework.core;

public interface ServiceExtendedConfigurationStrategy {
    boolean isExtendedConfiguration(String content);
    
    Object adapter(Class interfaceName);
}
