/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project framework
 */
package net.silencily.sailing.hibernate3;

import net.silencily.sailing.framework.persistent.hibernate3.entity.CodeWrapper;

import org.apache.commons.lang.StringUtils;

/**
 * 代码封装对象, similar with {@link UserWrapper}
 * @since 2006-7-23
 * @author java2enterprise
 * @version $Id: CodeWrapperPlus.java,v 1.1 2010/12/10 10:54:14 silencily Exp $
 */
public class CodeWrapperPlus extends CodeWrapper {

	

	public CodeWrapperPlus()
	{
		super();
		this.code="";
		this.name="";
		
	}
	private boolean isModify=false;
	public boolean isModify() {
		return isModify;
	}
	public void setModify(boolean isModify) {
		this.isModify = isModify;
	}
	public CodeWrapperPlus(String code) {
        super(code,null);
    }
	public CodeWrapperPlus(String code, String name) {
        super(code,name);
    }

	public CodeWrapperPlus(String code, String name, String description) {
		super(code,name,description);
	}
	/**
	 * 描述：
	 */
	private static final long serialVersionUID = 2268012381907524861L;

	public String toString() {
		// TODO Auto-generated method stub
		return this.getCode();
	}
	public void setCode(String code) {
		// TODO Auto-generated method stub
		this.setModify(!code.equals(this.code));
		super.setCode(code);
	}
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if(obj==null)
			return false;
		if(CodeWrapper.class.isAssignableFrom(obj.getClass()))
		{
			if(StringUtils.isBlank(((CodeWrapper)obj).getCode()))
				return StringUtils.isBlank(this.getCode());
			return ((CodeWrapper)obj).getCode().equals(this.getCode());
				
		}
		return false;
	}
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		CodeWrapperPlus cwp=new CodeWrapperPlus();
		cwp.setCode(this.getCode());
		cwp.setDescription(this.getDescription());
		cwp.setName(this.name);
		return cwp;
	}
	
	
	
}
