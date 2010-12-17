package net.silencily.sailing.exception;

import org.springframework.dao.DataAccessException;

public class DataAccessDenyException extends DataAccessException {

	private static final long serialVersionUID = 1L;

	public DataAccessDenyException(String msg) {
		super(msg);
	}

}
