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
 * @author ����
 * @version $Id: DtoUtils.java,v 1.1 2010/12/10 10:54:21 silencily Exp $
 */
public abstract class DtoUtils {
	
	/**
	 * �ж�һ�� dto �Ƿ��� Transient(����״̬), ��ʱ��ʵ���������ݿ��еļ�¼�޹�����ֻ��һ����ͨ�� JavaBean
	 * @param baseDto the dto
	 * @return whether the dto is Transient
	 */
	public static boolean isTransient(BaseDto baseDto) {
		return baseDto == null || ( baseDto != null && StringUtils.isBlank(baseDto.getId()));
	}
		
	/**
	 * �õ�һ�� dto �����е���� sequenceNo, һ������ hibernate list �� inverse = "true" ʱ�ֶ��� sequenceNo ��ֵ
	 * @see BaseDto#getSequenceNo()
	 * @param dtos collection fill with {@link BaseDto}
	 * @return ���� sequenceNo, ��� dtos Ϊ null ��ռ���, ���� -1
	 */
	public static int getMaxSequenceNo(Collection dtos) {
		int max = -1;
		
		if (dtos == null || CollectionUtils.isEmpty(dtos)) {
			return max;
		}
		
		for (Iterator iter = dtos.iterator(); iter.hasNext(); ) {
			Object o = iter.next();
			Assert.isInstanceOf(BaseDto.class, o, "ֻ֧�� BaseDTO ����");
			BaseDto baseDto = (BaseDto) o;
			if (baseDto.getSequenceNo() != null && baseDto.getSequenceNo().intValue() > max) {
				max = baseDto.getSequenceNo().intValue();
			}
		}
		
		return max;
	}
	
}
