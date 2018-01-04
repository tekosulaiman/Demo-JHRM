package org.app.portofolio.webui.hr.transaction.employee.model;

import org.app.portofolio.webui.hr.common.components.InlineListItemRenderer;
import org.module.hr.model.MstEducation;
import org.module.hr.model.MstSkill;
import org.module.hr.model.TrsEmployeeSkill;
import org.module.hr.service.EmployeeService;
import org.module.hr.service.QualificationService;
import org.zkoss.bind.BindUtils;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

public class EmployeeSkillListItemRenderer implements ListitemRenderer<TrsEmployeeSkill>{
	
	private EmployeeService employeeService = (EmployeeService) SpringUtil.getBean("employeeService");

	private QualificationService qualificationService = (QualificationService) SpringUtil.getBean("qualificationService");
		
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void render(Listitem item, final TrsEmployeeSkill trsEmployeeSkill, int index) throws Exception{
		Listcell listcell;

		final Button buttonSave = new Button();
		buttonSave.setImage("/images/icons/btn_save.gif");

		final Button buttonEdit = new Button();
		buttonEdit.setImage("/images/icons/btn_edit.gif");

		final Button buttonDelete = new Button();
		buttonDelete.setImage("/images/icons/btn_delete.gif");

		final Button buttonCancel = new Button();
		buttonCancel.setImage("/images/icons/btn_cancel.gif");
		
		final Label labelSkill = new Label();
		final Label labelYearOfExperience = new Label();
		final Label labelComment = new Label();
		
		final Combobox comboboxSkill = new Combobox();
		final Intbox textboxYearOfExperince = new Intbox();
		final Textbox textboxComment = new Textbox();
		
		listcell = new Listcell();
		buttonEdit.setParent(listcell);
		buttonSave.setParent(listcell);
		buttonCancel.setParent(listcell);
		buttonDelete.setParent(listcell);
		listcell.setParent(item);
		
		listcell = new Listcell();
		comboboxSkill.setParent(listcell);
		labelSkill.setParent(listcell);
		listcell.setParent(item);
		
		listcell = new Listcell();
		textboxYearOfExperince.setParent(listcell);
		labelYearOfExperience.setParent(listcell);
		listcell.setParent(item);
		
		listcell = new Listcell();
		textboxComment.setParent(listcell);
		textboxComment.setMultiline(true);
		textboxComment.setHeight("75px");
		labelComment.setParent(listcell);
		listcell.setParent(item);
		
		if (trsEmployeeSkill.getIdEmployeeSkill() == null){
			buttonEdit.setVisible(false);
			buttonDelete.setVisible(false);
			
			comboboxSkill.setModel(new ListModelList<MstSkill>(qualificationService.getAllMstSkill()));
			comboboxSkill.setItemRenderer(new ComboitemRenderer<MstSkill>() {

				@Override
				public void render(Comboitem item, final MstSkill mstSkill, int index) throws Exception {

					item.setLabel(mstSkill.getNameSkill());

					item.setAttribute("data", mstSkill);
				}
			});
		} else {
			buttonSave.setVisible(false);
			buttonCancel.setVisible(false);
			buttonDelete.setVisible(false);
			
			labelSkill.setValue(trsEmployeeSkill.getIdSkill().getNameSkill());
			labelYearOfExperience.setValue(trsEmployeeSkill.getYearOfExperience().toString());
			labelComment.setValue(trsEmployeeSkill.getComment());
			
			comboboxSkill.setVisible(false);
			textboxYearOfExperince.setVisible(false);
			textboxComment.setVisible(false);
			
		}
		
		/*
		 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		 * ++++++++++++++ Function CRUD Event
		 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		 * ++++++++++++++
		 */
		buttonSave.addEventListener(Events.ON_CLICK, new EventListener() {

			@Override
			public void onEvent(Event event) throws Exception {
				 if (trsEmployeeSkill.getIdEmployeeSkill() == null){
					 trsEmployeeSkill.setIdSkill((MstSkill) comboboxSkill.getSelectedItem().getAttribute("data"));
					 trsEmployeeSkill.setYearOfExperience(textboxYearOfExperince.getValue());
					 trsEmployeeSkill.setComment(textboxComment.getValue());
					 
					 employeeService.save(trsEmployeeSkill);
					 
					BindUtils.postGlobalCommand(null, null, "refreshAfterSaveOrUpdateEmployeeSkill", null);
				 } else {
					 trsEmployeeSkill.setIdSkill((MstSkill) comboboxSkill.getSelectedItem().getAttribute("data"));
					 trsEmployeeSkill.setYearOfExperience(textboxYearOfExperince.getValue());
					 trsEmployeeSkill.setComment(textboxComment.getValue());
					 
					 employeeService.update(trsEmployeeSkill);
					 
					BindUtils.postGlobalCommand(null, null, "refreshAfterSaveOrUpdateEmployeeSkill", null);
				 }
					 
			}
		});
		
		buttonEdit.addEventListener(Events.ON_CLICK, new EventListener() {
			public void onEvent(Event event) throws Exception {
				buttonEdit.setVisible(false);
				buttonSave.setVisible(true);
				buttonDelete.setVisible(true);
				
				comboboxSkill.setVisible(true);
				textboxYearOfExperince.setVisible(true);
				textboxComment.setVisible(true);
				
				labelSkill.setVisible(false);
				labelYearOfExperience.setVisible(false);
				labelComment.setVisible(false);
				
				comboboxSkill.setModel(new ListModelList<MstSkill>(qualificationService.getAllMstSkill()));
				comboboxSkill.setItemRenderer(new ComboitemRenderer<MstSkill>() {

					@Override
					public void render(Comboitem item, final MstSkill mstSkill, int index) throws Exception {

						item.setLabel(mstSkill.getNameSkill());

						item.setAttribute("data", mstSkill);
					}
				});
				comboboxSkill.setValue(trsEmployeeSkill.getIdSkill().getNameSkill());
				textboxYearOfExperince.setValue(trsEmployeeSkill.getYearOfExperience());
				textboxComment.setValue(trsEmployeeSkill.getComment());
				
			}
		});
		
		buttonDelete.addEventListener(Events.ON_CLICK, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				Messagebox.show("Do you really want to remove item?", "Confirm", Messagebox.OK | Messagebox.CANCEL,
						Messagebox.EXCLAMATION, new EventListener() {
							public void onEvent(Event event) throws Exception {
								if (((Integer) event.getData()).intValue() == Messagebox.OK) {

									employeeService.delete(trsEmployeeSkill);

									BindUtils.postGlobalCommand(null, null, "refreshAfterSaveOrUpdateEmployeeSkill", null);
								} else {
									return;
								}
							}
						});
			}
		});

		buttonCancel.addEventListener(Events.ON_CLICK, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				BindUtils.postGlobalCommand(null, null, "refreshAfterSaveOrUpdateEmployeeSkill", null);
			}
		});
	}
}
