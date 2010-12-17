/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project framework
 */
package net.silencily.sailing.utils;

import java.util.Collection;
import java.util.Iterator;

import net.silencily.sailing.framework.persistent.BaseDto;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/**
 * @since 2006-6-15
 * @author 王政
 * @version $Id: DtoUtils.java,v 1.1 2010/12/10 10:54:21 silencily Exp $
 */
public abstract class DtoUtils {
	
	/**
	 * 判断一个 dto 是否处于 Transient(自由状态), 此时的实体对象和数据库中的记录无关联，只是一个普通的 JavaBean
	 * @param baseDto the dto
	 * @return whether the dto is Transient
	 */
	public static boolean isTransient(BaseDto baseDto) {
		return baseDto == null || ( baseDto != null && StringUtils.isBlank(baseDto.getId()));
	}
		
	/**
	 * 得到一个 dto 集合中的最大 sequenceNo, 一般用于 hibernate list 中 inverse = "true" 时手动给 sequenceNo 赋值
	 * @see BaseDto#getSequenceNo()
	 * @param dtos collection fill with {@link BaseDto}
	 * @return 最大的 sequenceNo, 如果 dtos 为 null 或空集合, 返回 -1
	 */
	public static int getMaxSequenceNo(Collection dtos) {
		int max = -1;
		
		if (dtos == null || CollectionUtils.isEmpty(dtos)) {
			return max;
		}
		
		for (Iterator iter = dtos.iterator(); iter.hasNext(); ) {
			Object o = iter.next();
			Assert.isInstanceOf(BaseDto.class, o, "只支持 BaseDTO 类型");
			BaseDto baseDto = (BaseDto) o;
			if (baseDto.getSequenceNo() != null && baseDto.getSequenceNo().intValue() > max) {
				max = baseDto.getSequenceNo().intValue();
			}
		}
		
		return max;
	}
	
}
