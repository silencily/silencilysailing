package net.silencily.sailing.security.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 角色
 * @author yushn
 * @version 1.0
 */
public class Role implements Serializable{
	/**
	 * 角色名称
	 */
	String name;
	/**
	 * 角色标识代码,用来唯一识别一个角色
	 */
	String code;

}
