package org.app.portofolio.webui.hr.transaction.employee.model;

import org.app.portofolio.webui.hr.common.components.InlineListItemRenderer;
import org.module.hr.model.MstEducation;
import org.module.hr.model.MstLanguage;
import org.module.hr.model.TrsEmployeeLanguage;
import org.module.hr.model.dto.RelationshipType;
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
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

public class EmployeeLanguageListItemRenderer implements ListitemRenderer<TrsEmployeeLanguage> {

	private EmployeeService employeeService = (EmployeeService) SpringUtil.getBean("employeeService");

	private QualificationService qualificationService = (QualificationService) SpringUtil
			.getBean("qualificationService");

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void render(Listitem item, final TrsEmployeeLanguage trsEmployeeLanguage, int index) throws Exception {
		Listcell listcell;

		final Button buttonSave = new Button();
		buttonSave.setImage("/images/icons/btn_save.gif");

		final Button buttonEdit = new Button();
		buttonEdit.setImage("/images/icons/btn_edit.gif");

		final Button buttonDelete = new Button();
		buttonDelete.setImage("/images/icons/btn_delete.gif");

		final Button buttonCancel = new Button();
		buttonCancel.setImage("/images/icons/btn_cancel.gif");

		final Label labelLanguage = new Label();
		final Label labelFluency = new Label();
		final Label labelCompetency = new Label();
		final Label labelComment = new Label();

		final Combobox comboboxLanguage = new Combobox();
		final Combobox comboboxFluency = new Combobox();
		final Combobox comboboxCompetency = new Combobox();
		final Textbox textboxComment = new Textbox();
		
		listcell = new Listcell();
		buttonEdit.setParent(listcell);
		buttonSave.setParent(listcell);
		buttonCancel.setParent(listcell);
		buttonDelete.setParent(listcell);
		listcell.setParent(item);

		listcell = new Listcell();
		comboboxLanguage.setParent(listcell);
		labelLanguage.setParent(listcell);
		listcell.setParent(item);

		listcell = new Listcell();
		comboboxFluency.setParent(listcell);
		labelFluency.setParent(listcell);
		listcell.setParent(item);

		listcell = new Listcell();
		comboboxCompetency.setParent(listcell);
		labelComment.setParent(listcell);
		listcell.setParent(item);

		listcell = new Listcell();
		textboxComment.setParent(listcell);
		textboxComment.setMultiline(true);
		textboxComment.setHeight("75px");
		labelComment.setParent(listcell);
		listcell.setParent(item);

		if (trsEmployeeLanguage.getIdEmployeeLanguage() == null) {
			buttonEdit.setVisible(false);
			buttonDelete.setVisible(false);

			comboboxLanguage.setModel(new ListModelList<MstLanguage>(qualificationService.getAllMstLanguage()));
			comboboxLanguage.setItemRenderer(new ComboitemRenderer<MstLanguage>() {

				@Override
				public void render(Comboitem item, MstLanguage mstLanguage, int index) throws Exception {
					item.setLabel(mstLanguage.getNameLanguage());
					item.setAttribute("data", mstLanguage);
				}
			});
			
			comboboxFluency.setModel(new ListModelList<RelationshipType>(employeeService.getAllRelationshipType()));
			comboboxFluency.setItemRenderer(new ComboitemRenderer<RelationshipType> () {
				@Override
				public void render(Comboitem item, RelationshipType relationshipType, int index) throws Exception {
					item.setLabel(relationshipType.getStpTypname());
					item.setAttribute("data", relationshipType);
				}
			});
			
			comboboxCompetency.setModel(new ListModelList<RelationshipType>(employeeService.getAllRelationshipType()));
			comboboxCompetency.setItemRenderer(new ComboitemRenderer<RelationshipType> () {
				@Override
				public void render(Comboitem item, RelationshipType relationshipType, int index) throws Exception {
					item.setLabel(relationshipType.getStpTypname());
					item.setAttribute("data", relationshipType);
				}
			});
			
		} else {
			buttonSave.setVisible(false);
			buttonCancel.setVisible(false);
			buttonDelete.setVisible(false);

//			RelationshipType fluencyType = employeeService.getRelationshipTypeById(Integer.valueOf(trsEmployeeLanguage.getFluency()));
//			RelationshipType competencyType = employeeService.getRelationshipTypeById(Integer.valueOf(trsEmployeeLanguage.getCompetency()));
			
			labelLanguage.setValue(trsEmployeeLanguage.getIdLanguage().getNameLanguage());
//			labelFluency.setValue(fluencyType.getStpTypname());
//			labelCompetency.setValue(competencyType.getStpTypname());
			labelComment.setValue(trsEmployeeLanguage.getComment());

			comboboxCompetency.setVisible(false);
			comboboxFluency.setVisible(false);
			comboboxLanguage.setVisible(false);
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
				
				trsEmployeeLanguage.setComment(textboxComment.getValue());
				trsEmployeeLanguage.setCompetency(comboboxCompetency.getSelectedIndex());
				trsEmployeeLanguage.setFluency(comboboxFluency.getSelectedIndex());
				trsEmployeeLanguage.setIdLanguage((MstLanguage) comboboxLanguage.getSelectedItem().getAttribute("data"));

				if (trsEmployeeLanguage.getIdEmployeeLanguage() == null) {

					 employeeService.save(trsEmployeeLanguage);
					 BindUtils.postGlobalCommand(null, null, "refreshAfterSaveOrUpdateEmployeeLanguage", null);
				} else {

					 employeeService.save(trsEmployeeLanguage);
					 BindUtils.postGlobalCommand(null, null, "refreshAfterSaveOrUpdateEmployeeLanguage", null);
				}
			}
		});
		buttonEdit.addEventListener(Events.ON_CLICK, new EventListener() {
			public void onEvent(Event event) throws Exception {
				buttonEdit.setVisible(false);
				buttonSave.setVisible(true);
				buttonDelete.setVisible(true);
				
				comboboxLanguage.setVisible(true);
				comboboxFluency.setVisible(true);
				comboboxCompetency.setVisible(true);
				textboxComment.setVisible(true);
				
				labelComment.setVisible(false);
				labelCompetency.setVisible(false);
				labelFluency.setVisible(false);
				labelLanguage.setVisible(false);
				
				comboboxLanguage.setModel(new ListModelList<MstLanguage>(qualificationService.getAllMstLanguage()));
				comboboxLanguage.setItemRenderer(new ComboitemRenderer<MstLanguage>() {
					@Override
					public void render(Comboitem item, MstLanguage mstLanguage, int index) throws Exception {
						item.setLabel(mstLanguage.getNameLanguage());
						item.setAttribute("data", mstLanguage);
					}
				});
				
				comboboxLanguage.setValue(trsEmployeeLanguage.getIdLanguage().getNameLanguage());
				
//				RelationshipType fluencyType = employeeService.getRelationshipTypeById(Integer.valueOf(trsEmployeeLanguage.getFluency()));
//				RelationshipType competencyType = employeeService.getRelationshipTypeById(Integer.valueOf(trsEmployeeLanguage.getCompetency()));
				
				comboboxFluency.setModel(new ListModelList<RelationshipType>(employeeService.getAllRelationshipType()));
				comboboxFluency.setItemRenderer(new ComboitemRenderer<RelationshipType> () {
					@Override
					public void render(Comboitem item, RelationshipType relationshipType, int index) throws Exception {
						item.setLabel(relationshipType.getStpTypname());
						item.setAttribute("data", relationshipType);
					}
				});
				
				
				
//				comboboxFluency.setValue(fluencyType.getStpTypname());
				
				comboboxCompetency.setModel(new ListModelList<RelationshipType>(employeeService.getAllRelationshipType()));
				comboboxCompetency.setItemRenderer(new ComboitemRenderer<RelationshipType> () {
					@Override
					public void render(Comboitem item, RelationshipType relationshipType, int index) throws Exception {
						item.setLabel(relationshipType.getStpTypname());
						item.setAttribute("data", relationshipType);
					}
				});
				
//				comboboxCompetency.setValue(competencyType.getStpTypname());
				textboxComment.setValue(trsEmployeeLanguage.getComment());
			}
		});

		buttonDelete.addEventListener(Events.ON_CLICK, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				Messagebox.show("Do you really want to remove item?", "Confirm", Messagebox.OK | Messagebox.CANCEL,
						Messagebox.EXCLAMATION, new EventListener() {
							public void onEvent(Event event) throws Exception {
								if (((Integer) event.getData()).intValue() == Messagebox.OK) {

									employeeService.delete(trsEmployeeLanguage);

									BindUtils.postGlobalCommand(null, null, "refreshAfterSaveOrUpdateEmployeeLanguage", null);
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
				BindUtils.postGlobalCommand(null, null, "refreshAfterSaveOrUpdateEmployeeLanguage", null);
			}
		});
	}
}
