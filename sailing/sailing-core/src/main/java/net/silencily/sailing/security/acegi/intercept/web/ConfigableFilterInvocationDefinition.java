/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project security
 */
package net.silencily.sailing.security.acegi.intercept.web;

/**
 * <class>ConfigableFilterInvocationDefinition</class> 支持 Perl5 和 ant Path 两种风格的资源配置方式
 * @since 2006-1-19
 * @author 王政
 * @version $Id: ConfigableFilterInvocationDefinition.java,v 1.1 2010/12/10 10:54:24 silencily Exp $
 */
public interface ConfigableFilterInvocationDefinition {
	
	/** The Perl5 expression  */
	String PERL5_KEY = "PATTERN_TYPE_PERL5";
    
    /** The ant path expression */
    String ANT_PATH_KEY = "PATTERN_TYPE_APACHE_ANT";
	
    /** 标准分隔符 */
    String STAND_DELIM_CHARACTER = ",";
    
    /**
     * Set resource expression, the value must be {@link #PERL5_KEY_REG_EXP} or {@link #ANT_PATH_KEY}
     * @see #PERL5_KEY
     * @see #ANT_PATH_KEY
     * @param resourceExpression the resource expression
     */
    void setResourceExpression(String resourceExpression);
    
    /**
     * 
     * @return resource expression
     */
    String getResourceExpression();
    
    /**
     * Set whether convert url to lowercase before comparison
     * @param convertUrlToLowercaseBeforeComparison whether convertUrlToLowercaseBeforeComparison
     */
    void setConvertUrlToLowercaseBeforeComparison(boolean convertUrlToLowercaseBeforeComparison);
	
    /**
     * 
     * @return whether convert url to lowercase before comparison
     */
    boolean isConvertUrlToLowercaseBeforeComparison();
   
}
