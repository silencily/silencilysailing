/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project cardAssistant
 */
package net.silencily.sailing.framework.transfer;


/**
 * 需要导入功能的实体需要实现此接口
 * @since 2005-9-26
 * @author 王政
 * @version $Id: TransferImportable.java,v 1.1 2010/12/10 10:54:19 silencily Exp $
 */
public interface TransferImportable {
		
	/**
	 * 得到实体 Id 
	 * @return
	 */
	String getEntityIdAsString();
	
	/**
	 * 导入数据成功后的其他信息
	 * @return
	 */
	Object getOdditionalInfo();
	
	/**
	 * 是否导入成功
	 * @return whether import succeessed
	 */
	boolean isImportSuccessed();
	
	/**
	 * 数据是否可删除
	 * @return whether the data can be removed
	 */
	boolean isCanBeRemoved();
	
	/**
	 * 
	 * @param odditionalInfo
	 */
	void setOdditionalInfo(Object odditionalInfo);
	
	/**
	 * 
	 * @param importSuccessed
	 */
	void setImportSuccessed(boolean importSuccessed);
	
	/**
	 * 
	 * @param canBeRemoved
	 */
	void setCanBeRemoved(boolean canBeRemoved);
	
}
