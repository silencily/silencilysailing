package net.silencily.sailing.basic.uf.sso.web;

import net.silencily.sailing.struts.BaseFormPlus;

/**
 * 
 * @author tongjq
 * @version 1.0
 */
public class SingleSignOnForm extends BaseFormPlus {
    private String username;
    private String password;
 
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    

}
