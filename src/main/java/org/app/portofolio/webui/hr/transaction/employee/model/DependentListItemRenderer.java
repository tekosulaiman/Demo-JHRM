package org.app.portofolio.webui.hr.transaction.employee.model;

import org.module.hr.model.TrsEmployeeDependent;
import org.module.hr.model.dto.RelationshipType;
import org.module.hr.service.EmployeeService;
import org.zkoss.bind.BindUtils;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;


public class DependentListItemRenderer implements ListitemRenderer<TrsEmployeeDependent> {

	private EmployeeService employeeService = (EmployeeService) SpringUtil
			.getBean("employeeService");

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void render(Listitem item, final TrsEmployeeDependent trsEmployeeDependent, int index) throws Exception {
		Listcell listcell;

		final Button buttonSave = new Button();
		buttonSave.setImage("/images/icons/btn_save.gif");

		final Button buttonEdit = new Button();
		buttonEdit.setImage("/images/icons/btn_edit.gif");

		final Button buttonDelete = new Button();
		buttonDelete.setImage("/images/icons/btn_delete.gif");

		final Button buttonCancel = new Button();
		buttonCancel.setImage("/images/icons/btn_cancel.gif");
		
		final Label labelName = new Label();
		final Label labelRelationship = new Label();
		final Label labelDateOfBirth = new Label();
		
		final Textbox textboxName = new Textbox();
		final Combobox comboboxRelationshipType = new Combobox();
		final Datebox dateboxDateOfBirth = new Datebox();
		

		listcell = new Listcell();
		buttonEdit.setParent(listcell);
		buttonSave.setParent(listcell);
		buttonCancel.setParent(listcell);
		buttonDelete.setParent(listcell);
		listcell.setParent(item);
		
		listcell = new Listcell();
		textboxName.setParent(listcell);
		labelName.setParent(listcell);
		listcell.setParent(item);
		
    	listcell = new Listcell();
    	comboboxRelationshipType.setParent(listcell);
    	comboboxRelationshipType.setReadonly(true);
    	labelRelationship.setParent(listcell);
    	listcell.setParent(item);
    	
    	listcell = new Listcell();
    	dateboxDateOfBirth.setParent(listcell);
    	dateboxDateOfBirth.setReadonly(true);
    	labelDateOfBirth.setParent(listcell);
    	listcell.setParent(item);
		
		if (trsEmployeeDependent.getIdDependent() == null){
			buttonEdit.setVisible(false);
			buttonDelete.setVisible(false);
			
			comboboxRelationshipType.setModel(new ListModelList<RelationshipType>(employeeService.getAllRelationshipType()));
			comboboxRelationshipType.setItemRenderer(new RelationshipListItemRenderer());

		} else {
			buttonSave.setVisible(false);
			buttonCancel.setVisible(false);
			buttonDelete.setVisible(false);
			
        	RelationshipType relationshipType = employeeService.getRelationshipTypeById(trsEmployeeDependent.getRelationship());
        	
        	labelName.setValue(trsEmployeeDependent.getName());
        	labelRelationship.setValue(relationshipType.getStpTypname());
        	labelDateOfBirth.setValue(trsEmployeeDependent.getDateOfBirth() != null ? trsEmployeeDependent.getDateOfBirth().toString() : "");
        	
        	textboxName.setVisible(false);
        	comboboxRelationshipType.setVisible(false);
        	dateboxDateOfBirth.setVisible(false);
		}
		
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Function CRUD Event
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	buttonSave.addEventListener(Events.ON_CLICK, new EventListener() {
		@Override
		public void onEvent(Event event) throws Exception {
			if(trsEmployeeDependent.getIdDependent() == null){
				Comboitem model = comboboxRelationshipType.getSelectedItem();
				RelationshipType RelationshipType = (RelationshipType) model.getAttribute("data");
				
				trsEmployeeDependent.setName(textboxName.getValue());
				trsEmployeeDependent.setRelationship(RelationshipType.getStpId());
				trsEmployeeDependent.setDateOfBirth(dateboxDateOfBirth.getValue());

				employeeService.save(trsEmployeeDependent);
				
				BindUtils.postGlobalCommand(null, null, "refreshAfterSaveOrUpdateEmployeeDependent", null);
			}else{
				Comboitem model = comboboxRelationshipType.getSelectedItem();
				RelationshipType RelationshipType = (RelationshipType) model.getAttribute("data");
				
				trsEmployeeDependent.setName(textboxName.getValue());
				trsEmployeeDependent.setRelationship(RelationshipType.getStpId());
				trsEmployeeDependent.setDateOfBirth(dateboxDateOfBirth.getValue());

				employeeService.update(trsEmployeeDependent);
				
				BindUtils.postGlobalCommand(null, null, "refreshAfterSaveOrUpdateEmployeeDependent", null);
			}
		}
	});
	
	buttonEdit.addEventListener(Events.ON_CLICK, new EventListener() {
		public void onEvent(Event event) throws Exception {		
			buttonEdit.setVisible(false);
			buttonSave.setVisible(true);
			buttonDelete.setVisible(true);
			
			textboxName.setVisible(true);
			comboboxRelationshipType.setVisible(true);
			dateboxDateOfBirth.setVisible(true);
			
			labelName.setVisible(false);
			labelRelationship.setVisible(false);
			labelDateOfBirth.setVisible(false);
			
			textboxName.setValue(trsEmployeeDependent.getName());
			dateboxDateOfBirth.setValue(trsEmployeeDependent.getDateOfBirth());
			
			RelationshipType relationshipType = employeeService.getRelationshipTypeById(trsEmployeeDependent.getRelationship());
			
			comboboxRelationshipType.setModel(new ListModelList<RelationshipType>(employeeService.getAllRelationshipType()));
			comboboxRelationshipType.setItemRenderer(new RelationshipListItemRenderer());
			comboboxRelationshipType.setValue(relationshipType.getStpTypname());
		}					
	});
	
	buttonDelete.addEventListener(Events.ON_CLICK, new EventListener() {
		@Override
		public void onEvent(Event event) throws Exception {
			Messagebox.show("Do you really want to remove item?", "Confirm", Messagebox.OK | Messagebox.CANCEL, Messagebox.EXCLAMATION, new EventListener() {
			    public void onEvent(Event event) throws Exception {    	
			 		if (((Integer) event.getData()).intValue() == Messagebox.OK) {

			 			employeeService.delete(trsEmployeeDependent);
			 			
			 			BindUtils.postGlobalCommand(null, null, "refreshAfterSaveOrUpdateEmployeeDependent", null);
			 		}else{
			 			return;
			 		}
			 	}
			});
		}
	});
	
	buttonCancel.addEventListener(Events.ON_CLICK, new EventListener() {
		@Override
		public void onEvent(Event event) throws Exception {
			BindUtils.postGlobalCommand(null, null, "refreshAfterSaveOrUpdateEmployeeDependent", null);
		}
	});
	}
}

