package net.silencily.sailing.common.basiccode.web;

import net.silencily.sailing.struts.BaseFormPlus;


public class CommonBasicCodeInitForm extends BaseFormPlus {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * ���������Ϣ
	 */
	private String message;

	/**
	 * ҳ����Կ���
	 */
	private String flag[];

	public String[] getFlag() {
		return flag;
	}

	public void setFlag(String flag[]) {
		this.flag = flag;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
