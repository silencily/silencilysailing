package net.silencily.sailing.security.acegi.userdetails;

import org.acegisecurity.BadCredentialsException;


/**
 * 
 * @author tongjq
 * @version 1.0
 */
public class RepeatFailureException extends BadCredentialsException {

    public RepeatFailureException(String msg) {
        super(msg);
    }

    public RepeatFailureException(String msg, Throwable t) {
        super(msg, t);
    }

}
