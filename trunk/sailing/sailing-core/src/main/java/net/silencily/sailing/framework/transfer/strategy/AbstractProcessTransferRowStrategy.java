/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project cardAssistant
 */
package net.silencily.sailing.framework.transfer.strategy;

import java.io.IOException;
import java.io.InputStream;

import net.silencily.sailing.framework.transfer.exceptions.ErrorWhenReadInputStreamException;

import org.springframework.util.Assert;

/**
 * @since 2005-9-28
 * @author 王政
 * @version $Id: AbstractProcessTransferRowStrategy.java,v 1.1 2010/12/10 10:54:17 silencily Exp $
 */
public abstract class AbstractProcessTransferRowStrategy {
	
	/**
	 * 在读输入流之前检查可用性
	 * @param inputStream the inputStream
	 * @throws IllegalArgumentException if the input stream is null
	 * @throws ErrorWhenReadInputStreamException when IOException catched
	 */
	protected final void prepareImport(InputStream inputStream) throws ErrorWhenReadInputStreamException {
		Assert.notNull(inputStream, " inputStream must be specified. ");
		try {
			Assert.isTrue(inputStream.available() != 0, " inputStream has no content ");
		} catch (IOException e) {
			throw new ErrorWhenReadInputStreamException(" can't read inputstream ", e);
		}
	}
	
}
