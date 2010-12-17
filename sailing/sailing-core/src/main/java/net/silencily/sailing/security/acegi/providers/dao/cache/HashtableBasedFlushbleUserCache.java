/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project security
 */
package net.silencily.sailing.security.acegi.providers.dao.cache;

import java.io.IOException;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.util.Hashtable;

import net.silencily.sailing.security.acegi.providers.dao.FlushableUserCache;

import org.acegisecurity.userdetails.UserDetails;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



/**
 * 不使用 ehcache 作缓存的原因是 UserCache 需要通过 WebService 访问, Cache 不能做序列化
 * @since 2006-3-3
 * @author 王政
 * @version $Id: HashtableBasedFlushbleUserCache.java,v 1.1 2010/12/10 10:54:28 silencily Exp $
 */
public class HashtableBasedFlushbleUserCache implements FlushableUserCache, Serializable {

	//	~ Static fields/initializers =============================================
	
	/** use serialVersionUID from JDK 1.0.2 for interoperability */
	private static final long serialVersionUID = 8041048681387165233L;
	
	private static final ObjectStreamField[] serialPersistentFields	= {new ObjectStreamField("users", Hashtable.class)}; 
	
	private static transient Log logger = LogFactory.getLog(HashtableBasedFlushbleUserCache.class);
	
	
	//	~ Instance fields ========================================================
	
	private Hashtable users = new Hashtable();
	
	
	//	~ Methods ================================================================
	
	public UserDetails getUserFromCache(String username) {
		return (UserDetails) users.get(username);
	}

	public void putUserInCache(UserDetails user) {
		if (logger.isDebugEnabled()) {
			logger.debug("Cache put: " + user);
		}
		users.put(user.getUsername(), user);
	}

	public void removeUserFromCache(String username) {
		if (logger.isDebugEnabled()) {
			logger.debug("Cache remove: " + username);
		}
		users.remove(username);
	}

	public void flushCache() {
		if (logger.isInfoEnabled()) {
			logger.info(" 清空 User 缓存 ");
		}
		users.clear();
	}
	
	/**
	 * For custom serializable
	 */
	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
		out.writeObject(users);
	}
	
	/**
	 * For custom deserializable
	 */
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		users = (Hashtable) in.readObject();
	}

}
