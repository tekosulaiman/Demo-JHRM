package org.app.portofolio.webui.hr.transaction.recruitment.candidate.validator;

import java.util.Map;

import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;

public class TrsJobCandidateFormValidator extends AbstractValidator{
	
	private static final String KEY_FIRSTNAME 		= "firstName";
	private static final String KEY_EMAIL			= "email";
	private static final String KEY_CONTACT_NO		= "contactNo";
	private static final String KEY_VACANCY			= "vacancy";
	
	private String vacancy;
	private String firstName;
	private String email;
	private String contactNo;
	
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
		this.firstName= (String) properties.get(KEY_FIRSTNAME).getValue();
		this.email= (String) properties.get(KEY_EMAIL).getValue();
		this.contactNo= (String) properties.get(KEY_CONTACT_NO).getValue();
//		this.vacancy = (String) properties.get(KEY_VACANCY).getValue();
	}
	
	/**
	 * 
	 * @param validationContext
	 */
	private void notEmptyValidation(ValidationContext validationContext) {
		/*String message = CommonMessages.NOT_EMPTY_ERROR_MESSAGE;
		
		if (firstName.isEmpty()) {
			addInvalidMessage(validationContext, KEY_FIRSTNAME, message);
		}
		
		if (email.isEmpty()) {
			addInvalidMessage(validationContext, KEY_EMAIL, message);
		}
		
		if (contactNo.isEmpty()) {
			addInvalidMessage(validationContext, KEY_CONTACT_NO, message);
		}
		
		if (vacancy == null) {
			addInvalidMessage(validationContext, KEY_VACANCY, message);
		}*/
	}
}
