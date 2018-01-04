package org.app.portofolio.webui.hr.transaction.recruitment.vacancy.validator;

import java.util.Map;

import org.app.portofolio.webui.hr.common.collections.CommonMessages;
import org.module.hr.model.MstJobtitle;
import org.module.hr.model.TrsEmployee;
import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;

public class TrsJobVacancyFormValidator extends AbstractValidator{
	
	private static final String KEY_ID_JOB_TITLE 	= "idJobTitle";
	private static final String KEY_NAME			= "name";
	private static final String KEY_ID_EMPLOYEE		= "idEmployee";
	private static final String KEY_NO_OF_POSITIONS	= "noOfPositions";
	
	private MstJobtitle idJobTitle;
	private String name;
	private TrsEmployee idEmployee;
	private Object noOfPositions;
	
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
		this.idJobTitle = (MstJobtitle) properties.get(KEY_ID_JOB_TITLE).getValue();
		this.name = (String) properties.get(KEY_NAME).getValue();
		this.idEmployee = (TrsEmployee) properties.get(KEY_ID_EMPLOYEE).getValue();
		this.noOfPositions = properties.get(KEY_NO_OF_POSITIONS).getValue();
	}

	/**
	 * 
	 * @param validationContext
	 */
	private void notEmptyValidation(ValidationContext validationContext) {
		String message = CommonMessages.NOT_EMPTY_ERROR_MESSAGE;
		if (idJobTitle == null) {
			addInvalidMessage(validationContext, KEY_ID_JOB_TITLE, message);
		}
		
		if (name.isEmpty()) {
			addInvalidMessage(validationContext, KEY_NAME, message);
		}
		
		if (idEmployee == null) {
			addInvalidMessage(validationContext, KEY_ID_EMPLOYEE, message);
		}
		
		if (noOfPositions == null) {
			addInvalidMessage(validationContext, KEY_NO_OF_POSITIONS, message);
		} else {
			if (noOfPositions instanceof String) {
				if(((String) noOfPositions).isEmpty()) {
					addInvalidMessage(validationContext, KEY_NO_OF_POSITIONS, message);
				}
			}
		}
	}
}
