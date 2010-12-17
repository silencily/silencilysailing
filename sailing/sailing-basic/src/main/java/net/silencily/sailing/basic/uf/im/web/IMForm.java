package net.silencily.sailing.basic.uf.im.web;

import net.silencily.sailing.struts.BaseFormPlus;


/**
 * 
 * @author tongjq
 * @version 1.0
 */
public class IMForm extends BaseFormPlus {
    private String userId;
    private String password;
    private String userNickName;
 
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserNickName() {
        return userNickName;
    }
    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }
    

}
