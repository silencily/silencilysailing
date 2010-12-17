/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project cardAssistant
 */
package net.silencily.sailing.framework.web.webwork.validator;

import com.opensymphony.xwork.validator.ValidationException;
import com.opensymphony.xwork.validator.validators.FieldValidatorSupport;

/**
 * ��֤ minLength �� maxLength ������Դ���, �� {@link com.opensymphony.xwork.validator.validators.StringLengthFieldValidator} ��ͬ 
 * @since 2005-10-20
 * @author ����
 * @version $Id: StringLengthIfExistFieldValidator.java,v 1.1 2010/12/10 10:54:27 silencily Exp $
 */
public class StringLengthIfExistFieldValidator extends FieldValidatorSupport {
	
    //~ Instance fields ////////////////////////////////////////////////////////

    private boolean doTrim = true;
    private int maxLength = -1;
    private int minLength = -1;

    //~ Methods ////////////////////////////////////////////////////////////////

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public int getMinLength() {
        return minLength;
    }

    public void setTrim(boolean trim) {
        doTrim = trim;
    }

    public boolean getTrim() {
        return doTrim;
    }

    public void validate(Object object) throws ValidationException {
        String fieldName = getFieldName();
        String val = (String) getFieldValue(fieldName, object);

        if (val == null) {
            
        	/** 
             * ע�������� {@link com.opensymphony.xwork.validator.validators.StringLengthFieldValidator#validate(java.lang.Object)}  �Ĳ�ͬ,
             * ��� value == null, ˵��ҳ����û�д�����, ����Բ�����֤ 
             */ 
        	return;
        } 
        
        if (doTrim) {
            val = val.trim();
        }

        if ((minLength > -1) && (val.length() < minLength)) {
            addFieldError(fieldName, object);
        } else if ((maxLength > -1) && (val.length() > maxLength)) {
            addFieldError(fieldName, object);
        }
    }

}
