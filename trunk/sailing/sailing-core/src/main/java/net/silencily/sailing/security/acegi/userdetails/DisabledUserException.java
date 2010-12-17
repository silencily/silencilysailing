package net.silencily.sailing.security.acegi.userdetails;

import org.acegisecurity.BadCredentialsException;

/**
 * 
 * @author tongjq
 * @version 1.0
 */
public class DisabledUserException extends BadCredentialsException {

    public DisabledUserException(String msg) {
        super(msg);
    }

    public DisabledUserException(String msg, Throwable t) {
        super(msg, t);
    }

}
