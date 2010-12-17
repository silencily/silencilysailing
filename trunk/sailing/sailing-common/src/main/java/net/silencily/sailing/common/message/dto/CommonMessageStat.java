/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project publicresource
 */
package net.silencily.sailing.common.message.dto;

import net.silencily.sailing.common.systemcode.SystemCode;
import net.silencily.sailing.framework.persistent.BaseDto;

/**
 * 消息的统计项: 状态项 和 统计数值
 * 接受消息状态统计：未阅读 / 已阅读 等
 * 发送消息状态统计：草稿 / 已发送 / 删除
 * @since 2006-10-23
 * @author pillarliu 
 * @version $Id: CommonMessageStat.java,v 1.1 2010/12/10 10:54:19 silencily Exp $
 */
public class CommonMessageStat  extends BaseDto{
	
	private static final long serialVersionUID = 1L;
	
	/**状态项：手工load装入*/
	private SystemCode status;
	
	/**状态代码*/
	private String code;
	
	/**统计得出的数值*/
	private Integer num;

	
	public Integer getNum() {
		if(num == null){
			return new Integer(0);
		}
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public SystemCode getStatus() {
		return status;
	}

	public void setStatus(SystemCode status) {
		this.status = status;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
