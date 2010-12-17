/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project cardAssistant
 */
package net.silencily.sailing.framework.transfer.manager;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import net.silencily.sailing.framework.transfer.TransferImportResult;
import net.silencily.sailing.framework.transfer.exceptions.ImportProcessDataCallbackException;
import net.silencily.sailing.framework.transfer.exceptions.TransferException;
import net.silencily.sailing.framework.transfer.meta.FileType;
import net.silencily.sailing.framework.transfer.meta.ImportMetaData;

/**
 * <class>TransferImportManager</class> 提供了导入数据时需要的方法, 实现类应该是 {@link com.coheg.framework.transfer.core.support.TransferImportDaoSupport} 的子类, 
 * 其中的 {@link com.coheg.framework.transfer.core.TransferImportTemplate} 对 {@link TransferImportManager} 中的方法提供了绝大多数支持, 通常情况下你只需要简单的实现对
 * 欲导入的表进行增删改操作即可
 * @see com.coheg.framework.transfer.core.support.TransferImportDaoSupport
 * @see com.coheg.framework.transfer.core.TransferImportOperations
 * @see com.coheg.framework.transfer.core.TransferImportTemplate
 * @since 2005-9-26
 * @author 王政
 * @version $Id: TransferImportManager.java,v 1.1 2010/12/10 10:54:28 silencily Exp $
 */
public interface TransferImportManager {
	
	/**
	 * 导入数据方法, 在 spring 配置文件中应该做如下配置 : <p>
	 * 
	 *    &lt;prop key="importData"&gt;PROPAGATION_REQUIRES_NEW, +com.coheg.framework.transfer.exceptions.TransferException&lt;/prop&gt;
	 * 
	 * <p>
	 * 实现类只需要调用 {@link com.coheg.framework.transfer.core.TransferImportOperations#importData(InputStream, FileType, Date, TransferImportCallback)}, 
	 * 再简单的实现新增数据功能即可
	 * 
	 * @param in the input stream
	 * @param fileType the fileType
	 * @param importDate 导入日期, 来自于页面
	 * @return the TransferImportResult
	 * @throws TransferException 注意, 抛出此异常时事务应该 commit !!!
	 * @see com.coheg.framework.transfer.core.TransferImportOperations#importData(InputStream, FileType, Date, TransferImportCallback)
	 * @see com.coheg.framework.transfer.callbacks.TransferImportCallback
	 * @see TransferImportResult
	 * @see com.coheg.framework.transfer.TransferImportable
	 */
	TransferImportResult importData(final InputStream in, final FileType fileType, final Date importDate) throws TransferException;
	
	/**
	 * 删除错误数据,  在 spring 配置文件中应该做如下配置 : <p>
	 *   
	 *   &lt;prop key="removeWrongData"&gt;PROPAGATION_REQUIRES_NEW, +com.coheg.framework.transfer.exceptions.ImportProcessDataCallbackException&lt;/prop&gt;
	 *  
	 * <p>
	 * 实现类只需要调用 {@link com.coheg.framework.transfer.core.TransferImportOperations#processDataAfterImported(Serializable[], TransferImportProcessDataCallback)}, 
	 * 再简单的实现删除数据功能即可
	 *   
	 * @param errorIds 从 ui 上取得的实体 id 集合
	 * @return 任意的对象, 实现类可用来返回业务需要的信息
	 * @throws ImportProcessDataCallbackException 注意,　抛出此异常时事务应该 commit !!!
	 * @see com.coheg.framework.transfer.core.TransferImportOperations#processDataAfterImported(Serializable[], TransferImportProcessDataCallback)
	 * 
	 */
	Object removeWrongData(Serializable[] errorIds) throws ImportProcessDataCallbackException;
	
	/**
	 * 确认数据,  在 spring 配置文件中应该做如下配置 : <p>
	 *   
	 *   &lt;prop key="confirmCorrectData"&gt;PROPAGATION_REQUIRES_NEW, +com.coheg.framework.transfer.exceptions.ImportProcessDataCallbackException&lt;/prop&gt;
	 *  
	 * <p>
	 * 实现类只需要调用 {@link com.coheg.framework.transfer.core.TransferImportOperations#processDataAfterImported(Serializable[], TransferImportProcessDataCallback)}, 
	 * 再简单的实现修改数据功能即可
	 * @param errorIds 从 ui 上取得的实体 id 集合
	 *   
	 * @return 任意的对象, 实现类可用来返回业务需要的信息
	 * @throws ImportProcessDataCallbackException 注意,　抛出此异常时事务应该 commit !!!
	 * @see com.coheg.framework.transfer.core.TransferImportOperations#processDataAfterImported(Serializable[], TransferImportProcessDataCallback)
	 * 
	 */
	Object confirmCorrectData(Serializable[] correctIds) throws ImportProcessDataCallbackException;
	
	/**
	 * 得到需要处理的数据, 一般是表中 confirmed 字段为 null 的数据, 注意 List 中的元素类型应该为 {@link TransferImportable} !!!
	 * @return list fill with {@link TransferImportable}
	 */
	List getRequireToBeProcessedData();
		
	/**
	 * 得到导入数据所需要的元信息, 包括日期格式化, 文本间隔符和要导入的实体属性及其中文名称 properteis
	 * @return the ImportMetaData
	 * @see ImportMetaData
	 */
	ImportMetaData getImportMetaData();
	
}
