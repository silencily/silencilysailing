/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project publicresource
 */
package net.silencily.sailing.common;

/**
 * @since 2006-7-27
 * @author java2enterprise
 * @version $Id: CommonConstants.java,v 1.1 2010/12/10 10:54:16 silencily Exp $
 */
public interface CommonConstants {

	String MODULE_NAME = "common";

	/** 公共模块的权限名 */
	String PERMISS_MANAGE_ADDRESS = "common.addressManager";
	
	/**消息附件上传的路径*/
	String MESSAGE_VFS_ROOT = MODULE_NAME + "/message/";
	/**通讯簿的图片*/
	String ADDRESS_VFS_ROOT  = MODULE_NAME + "/addressbook/";
	/**部门的图片*/
	String DEPART_VFS_ROOT  = MODULE_NAME + "/department/";

	/** 文件的临时存放路径，据说要做定时任务清除，不过最好还是用过的文件就删掉。 */
	String TMP_VFS_ROOT = "temporary/";
	
	//~ 代码表常量
	/** 计量单位 */
	String CODE_UNIT_TYPE = "unit";
    /** 机组 */
	String CODE_ENGINERY_GROUP = "engineryGroup";
	/** 专业 */
	String CODE_SPECIALTY = "specialty";
	
}
