package net.silencily.sailing.basic.uf.funtree.service;

import java.util.List;

import net.silencily.sailing.framework.core.ServiceBase;


public interface DisplayFunTreeService  extends ServiceBase {
	public static String SERVICE_NAME = "uf.displayFunTree";

	List getFunTree(String userId,String prefix) throws Exception;
}
