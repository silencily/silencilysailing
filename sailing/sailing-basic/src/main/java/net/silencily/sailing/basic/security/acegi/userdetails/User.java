package net.silencily.sailing.basic.security.acegi.userdetails;

/**
 * @since 2006-01-19
 * @author qian
 * @author ÍõÕþ
 * @version $Id: User.java,v 1.1 2010/12/10 10:56:48 silencily Exp $
 * @hibernate.class table="security_user"
 */
public class User extends ExtensionalUserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8139887314475188123L;

	/**
	 * Default constructor, only for hibernate use
	 */
	public User() {
		// throw new IllegalArgumentException("Cannot use default constructor");
	}

}
