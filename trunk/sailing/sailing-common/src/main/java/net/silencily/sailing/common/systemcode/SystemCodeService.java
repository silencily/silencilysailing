/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project publicresource
 */
package net.silencily.sailing.common.systemcode;

import java.util.List;

import net.silencily.sailing.framework.core.ServiceBase;
import net.silencily.sailing.framework.web.view.ComboSupportList;


/**
 * ϵͳ������ҵ����ϵͳ�ṩ�� Service 
 * @since 2006-9-19
 * @author java2enterprise
 * @version $Id: SystemCodeService.java,v 1.1 2010/12/10 10:54:20 silencily Exp $
 */
public interface SystemCodeService extends ServiceBase {
	
	/**
	 * ���� ��ϵͳģ����, ���� parent code �õ��¼����뼯��, ���û���¼�����, ���ؿ� list
	 * @param systemModuleName ��ϵͳģ����
	 * @param parentCode parent code
	 * @return list fill with {@link SystemCode}
	 */
	List findByParentCode(String systemModuleName, String parentCode);
	
	/**
	 * ���� ��ϵͳģ����, ���� parent code �õ� {@link ComboSupportList}, ��������ͼ����ʾ
	 * @param systemModuleName ��ϵͳģ����
	 * @param parentCode parent code
	 * @return {@link ComboSupportList}
	 */
	ComboSupportList getCodeList(String systemModuleName, String parentCode);
	
	/**
	 * ���� ��ϵͳģ����, ���� code �õ�ϵͳ����, ������Ҳ���, return null, 
	 * ���� null �������쳣��ԭ���Ǳ��� spring �ع�����
	 * @param systemModuleName ��ϵͳģ����
	 * @param code ���� code
	 * @return system code, ������Ҳ���, return null
	 */
	SystemCode load(String systemModuleName, String code);

	/**
	 * �õ������Ѿ�ע�����ϵͳģ����, �����û����ϵͳע��, ���� new String[0]
	 * @return registered system module names
	 */
	String[] getRegisteredSystemModuleNames();
	
}
