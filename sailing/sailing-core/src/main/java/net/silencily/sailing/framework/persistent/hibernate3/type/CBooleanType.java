package net.silencily.sailing.framework.persistent.hibernate3.type;

import org.hibernate.type.BooleanType;

/**
 * @since 2005-10-26
 * @author Ç®°²´¨
 * @version $Id: CBooleanType.java,v 1.1 2010/12/10 10:54:27 silencily Exp $
 */
public class CBooleanType extends BooleanType{

	/** use serialVersionUID from JDK 1.0.2 for interoperability */
	private static final long serialVersionUID = -7824642794364576044L;

	public String objectToSQLString(Object value) throws Exception {
		return ( ( (Boolean) value ).booleanValue() ) ? "'t'" : "'f'";
	}

}
