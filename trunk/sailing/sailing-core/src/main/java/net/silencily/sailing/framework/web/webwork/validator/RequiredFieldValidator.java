package net.silencily.sailing.framework.web.webwork.validator;

import com.opensymphony.xwork.validator.ValidationException;
import com.opensymphony.xwork.validator.validators.FieldValidatorSupport;

/**
 * �ֶβ���Ϊnull��""
 * @since 2005-9-20
 * @author Ǯ����
 * @version $Id: RequiredFieldValidator.java,v 1.1 2010/12/10 10:54:27 silencily Exp $
 */
public class RequiredFieldValidator extends FieldValidatorSupport{

	public void validate(Object object) throws ValidationException {
		String fieldName = getFieldName();
        Object value = this.getFieldValue(fieldName, object);

        if (value == null || value.equals("")) {
            addFieldError(fieldName, object);
        }
	}

}
