package net.silencily.sailing.common.report.service.excelimpl;

import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import bsh.EvalError;


/**
 * @author zhangli
 * @version $Id: RowCallback.java,v 1.1 2010/12/10 10:54:15 silencily Exp $
 * @since 2007-5-8
 */
public interface RowCallback {
    
    void row(int index, Object obj) throws EvalError, RowsExceededException, WriteException;
}
