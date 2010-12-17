/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project security
 */
package net.silencily.sailing.security.acegi.ui.sso;

/**
 * @since 2006-7-13
 * @author java2enterprise
 * @version $Id: SSOLoginRunner.java,v 1.1 2010/12/10 10:54:26 silencily Exp $
 */
public class SSOLoginRunner implements Runnable {

	private SingleSignOnService singleSignOnService;	
	private CrossThreadPassedValue passedValue;
	
	public SSOLoginRunner(SingleSignOnService singleSignOnService, CrossThreadPassedValue passedValue) {			
		this.singleSignOnService = singleSignOnService;
		this.passedValue = passedValue;
	}
	
	public void run() {
		singleSignOnService.loginSuccess(this.passedValue);
	}	
}
