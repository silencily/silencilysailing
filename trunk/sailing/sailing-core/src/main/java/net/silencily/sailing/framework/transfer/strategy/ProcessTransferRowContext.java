/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project cardAssistant
 */
package net.silencily.sailing.framework.transfer.strategy;

import net.silencily.sailing.framework.transfer.meta.FileType;

import org.springframework.util.Assert;

/**
 * <class>ProcessTransferRowContext</class> �� {@link com.coheg.framework.transfer.strategy.ProcessTransferRowStrategy} �� Factory
 * @see com.coheg.framework.transfer.strategy.ProcessTransferRowStrategy
 * @since 2005-9-27
 * @author ����
 * @version $Id: ProcessTransferRowContext.java,v 1.1 2010/12/10 10:54:17 silencily Exp $
 */
public abstract class ProcessTransferRowContext {
	
	/**
	 * �����ļ����͵õ���Ӧ�Ĵ������
	 * @param fileType the filType
	 * @return the strategy
	 * @throws InternalError if the fileType object is an illegal object
	 * @see FileType
	 */
	public static ProcessTransferRowStrategy getStrategy(FileType fileType) throws InternalError {
		Assert.notNull(fileType, " fileType is required ");
		if (FileType.XLS.equals(fileType)) {
			return new XlsStrategy();
		} else if (FileType.TXT.equals(fileType)) {
			return new TxtStrategy();
		}
		throw new InternalError(fileType + " is an illegal object ! ");
	}
	
}
