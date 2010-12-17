/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project cardAssistant
 */
package net.silencily.sailing.framework.transfer.meta;

import java.util.Map;


/**
 * 导入数据需要的元信息, 内容是实体属性及其中文名称集合, 由于其继承了 {@link com.coheg.framework.transfer.meta.TransferMetaData}, 
 * 所以也包含日期格式和文本间隔符, 默认实现是 {@link com.coheg.framework.transfer.meta.DefaultImportMetaData}
 * 
 * @since 2005-9-25
 * @author 王政
 * @version $Id: ImportMetaData.java,v 1.1 2010/12/10 10:54:26 silencily Exp $
 */
public interface ImportMetaData extends TransferMetaData {
	
	/**
	 * 得到欲导入表对应的实体的属性及其中文名称集合, 属性名称将被用于在 ui 上取已导入数据的值, 如果你的实体属性是嵌套的, 属性名称请遵循 ognl 表达式规则,
	 * 属性中文名称将直接显示到 ui 上, 一个典型的配置可能如下 <p>
	 * 
	 * 	&lt;bean id="icInfoImportMetaData" class="com.coheg.framework.transfer.meta.DefaultImportMetaData"&gt;<br>
	 *	&nbsp;&nbsp;&lt;property name="entityProperties"&gt;<br>
	 *		&nbsp;&nbsp;&nbsp;&nbsp;&lt;props&gt;<br>
	 *			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;prop key="idCardNo"&gt;身份证号&lt;/prop&gt;<br>
	 *			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;prop key="customerNo"&gt;客户编号&lt;/prop&gt;<br>
	 *			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;prop key="icCardNo"&gt;IC卡号&lt;/prop&gt;<br>
	 *		&nbsp;&nbsp;&nbsp;&nbsp;&lt;/props&gt;<br>
	 *	&nbsp;&nbsp;&lt;/property&gt;<br>
	 *	&nbsp;&nbsp;&lt;property name="dateFormat"&gt;<br>
	 *		&nbsp;&nbsp;&nbsp;&nbsp;&lt;ref local="defaultDateFormat"&gt;&lt;/ref&gt;<br>
	 *	&nbsp;&nbsp;&lt;/property&gt;<br>
	 *	&nbsp;&nbsp;&lt;property name="txtSeparator" value="|" /&gt;<br>
	 * &lt;/bean&gt;<br>
	 * 
	 * @return the entity properties
	 */
	Map getEntityMap();
	
}
