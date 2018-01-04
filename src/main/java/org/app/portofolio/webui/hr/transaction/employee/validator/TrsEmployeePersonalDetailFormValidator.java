package org.app.portofolio.webui.hr.transaction.employee.validator;

import java.util.Map;

import org.app.portofolio.webui.hr.common.collections.CommonMessages;
import org.module.hr.model.MstJobtitle;
import org.module.hr.model.TrsEmployee;
import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;

public class TrsEmployeePersonalDetailFormValidator extends AbstractValidator {
	
	private static final String KEY_FIRST_NAME = "firstName";
	private static final String KEY_LAST_NAME = "lastName";
	private static final String KEY_ID_EMPLOYEE	= "idEmployee";
	
	private String idEmployee;
	private String firstName;
	private String lastName;
	

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
		this.firstName = (String) properties.get(KEY_FIRST_NAME).getValue();
		this.idEmployee = (String) properties.get(KEY_ID_EMPLOYEE).getValue();
		this.lastName = (String) properties.get(KEY_LAST_NAME).getValue();
	}

	/**
	 * 
	 * @param validationContext
	 */
	private void notEmptyValidation(ValidationContext validationContext) {
		String message = CommonMessages.NOT_EMPTY_ERROR_MESSAGE;
		if (firstName.isEmpty()) {
			addInvalidMessage(validationContext, KEY_FIRST_NAME, message);
		}
		
		if (lastName.isEmpty()) {
			addInvalidMessage(validationContext, KEY_LAST_NAME, message);
		}
		
		if (idEmployee.isEmpty()) {
			addInvalidMessage(validationContext, KEY_ID_EMPLOYEE, message);
		}
	}

}
