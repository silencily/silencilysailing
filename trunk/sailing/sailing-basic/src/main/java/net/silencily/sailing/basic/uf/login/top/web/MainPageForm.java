package net.silencily.sailing.basic.uf.login.top.web;

import net.silencily.sailing.struts.BaseFormPlus;


/**
 * @author wangchc
 *
 */
public class MainPageForm extends BaseFormPlus {
	private String loginUserName;
    private String mainUrl;

	/**
	 * @return loginUserName
	 */
	public String getLoginUserName() {
		return loginUserName;
	}

	/**
	 * @param loginUserName Ҫ���õ� loginUserName
	 */
	public void setLoginUserName(String loginUserName) {
		this.loginUserName = loginUserName;
	}

    /**
     * @return mainUrl
     */
    public String getMainUrl() {
        return mainUrl;
    }

    /**
     * @param ssoUrl Ҫ���õ� ssoUrl
     */
    public void setMainUrl(String mainUrl) {
        this.mainUrl = mainUrl;
    }
	
}
