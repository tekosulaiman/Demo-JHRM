package org.app.portofolio.webui.hr.transaction.employee.validator;

import java.util.Map;

import org.app.portofolio.webui.hr.common.collections.CommonMessages;
import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;

public class TrsEmployeeJobFormValidator extends AbstractValidator {
	
	@Override
	public void validate(ValidationContext validationContext) {
		Map<String,Property> properties = validationContext.getProperties(validationContext.getProperty().getBase());
		
		initComponents(properties);
		
		notEmptyValidation(validationContext);
		
	}
	
	/**
	 * 
	 * @param properties
	 */
	private void initComponents(Map<String, Property> properties) {
		
	}

	/**
	 * 
	 * @param validationContext
	 */
	private void notEmptyValidation(ValidationContext validationContext) {
		String message = CommonMessages.NOT_EMPTY_ERROR_MESSAGE;
	}
}
