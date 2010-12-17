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
 * ����������Ҫ��Ԫ��Ϣ, ������ʵ�����Լ����������Ƽ���, ������̳��� {@link com.coheg.framework.transfer.meta.TransferMetaData}, 
 * ����Ҳ�������ڸ�ʽ���ı������, Ĭ��ʵ���� {@link com.coheg.framework.transfer.meta.DefaultImportMetaData}
 * 
 * @since 2005-9-25
 * @author ����
 * @version $Id: ImportMetaData.java,v 1.1 2010/12/10 10:54:26 silencily Exp $
 */
public interface ImportMetaData extends TransferMetaData {
	
	/**
	 * �õ���������Ӧ��ʵ������Լ����������Ƽ���, �������ƽ��������� ui ��ȡ�ѵ������ݵ�ֵ, ������ʵ��������Ƕ�׵�, ������������ѭ ognl ���ʽ����,
	 * �����������ƽ�ֱ����ʾ�� ui ��, һ�����͵����ÿ������� <p>
	 * 
	 * 	&lt;bean id="icInfoImportMetaData" class="com.coheg.framework.transfer.meta.DefaultImportMetaData"&gt;<br>
	 *	&nbsp;&nbsp;&lt;property name="entityProperties"&gt;<br>
	 *		&nbsp;&nbsp;&nbsp;&nbsp;&lt;props&gt;<br>
	 *			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;prop key="idCardNo"&gt;���֤��&lt;/prop&gt;<br>
	 *			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;prop key="customerNo"&gt;�ͻ����&lt;/prop&gt;<br>
	 *			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;prop key="icCardNo"&gt;IC����&lt;/prop&gt;<br>
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
