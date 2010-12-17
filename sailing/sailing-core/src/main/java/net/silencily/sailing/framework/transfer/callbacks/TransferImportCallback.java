/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project cardAssistant
 */
package net.silencily.sailing.framework.transfer.callbacks;

import net.silencily.sailing.framework.transfer.TransferImportRow;
import net.silencily.sailing.framework.transfer.TransferImportable;


/**
 * 
 * @since 2005-9-25
 * @author ÍõÕþ
 * @version $Id: TransferImportCallback.java,v 1.1 2010/12/10 10:54:26 silencily Exp $
 */
public interface TransferImportCallback {
	
	/**
	 * 
	 * @param transferRow
	 * @return
	 * @throws Exception
	 */
	TransferImportable doImport(TransferImportRow transferRow) throws Exception;
	
}
