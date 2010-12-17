package net.silencily.sailing.framework.persistent.hibernate3.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.silencily.sailing.framework.utils.DatabaseUtils;

import org.hibernate.type.StringType;

/**
 * @since 2005-10-26
 * @author Ç®°²´¨
 * @version $Id: CStringType.java,v 1.1 2010/12/10 10:54:27 silencily Exp $
 */
public class CStringType extends StringType{

	private static final long serialVersionUID = -9215360287351254459L;
	
	public CStringType() {
		super();
	}

	public Object get(ResultSet rs, String name) throws SQLException {
			return DatabaseUtils.transformString2GBKAndTrim(rs.getString(name));
	}

	public void set(PreparedStatement st, Object value, int index) throws SQLException {
		st.setString(index, DatabaseUtils.transformString2ISOIfNecessarilyAndTrim((String) value));
	}
	
	
}
