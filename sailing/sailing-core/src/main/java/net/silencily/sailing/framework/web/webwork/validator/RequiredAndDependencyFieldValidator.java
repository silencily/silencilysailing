package net.silencily.sailing.framework.web.webwork.validator;

import com.opensymphony.xwork.validator.ValidationException;
import com.opensymphony.xwork.validator.validators.FieldValidatorSupport;

/**
 * 字段不能为null或""
 * @since 2005-9-21
 * @author 钱安川
 * @version $Id: RequiredAndDependencyFieldValidator.java,v 1.1 2010/12/10 10:54:27 silencily Exp $
 */
public class RequiredAndDependencyFieldValidator extends FieldValidatorSupport{
	private String dependencyField = null;
	private String dependencyFieldEqualValue = null;

	public void validate(Object object) throws ValidationException {
		String fieldName = getFieldName();
        Object value = this.getFieldValue(fieldName, object);
        
        Object dependencyValue = this.getFieldValue(dependencyField,object);
        Object equalValue = this.getFieldValue(dependencyFieldEqualValue,object);
        
        if((dependencyValue!=null && dependencyValue.equals(equalValue)) && (value == null || value.equals(""))){
        	addFieldError(fieldName, object);
        }
	}

	public String getDependencyField() {
		return dependencyField;
	}

	public void setDependencyField(String dependencyField) {
		this.dependencyField = dependencyField;
	}

	public String getDependencyFieldEqualValue() {
		return dependencyFieldEqualValue;
	}

	public void setDependencyFieldEqualValue(String dependencyFieldValue) {
		this.dependencyFieldEqualValue = dependencyFieldValue;
	}
}
