package org.app.portofolio.webui.hr.transaction.employee.model;

import java.util.Date;

import org.app.portofolio.webui.hr.common.components.InlineListItemRenderer;
import org.module.hr.model.TrsEmployeeExprience;
import org.module.hr.model.dto.RelationshipType;
import org.module.hr.service.EmployeeService;
import org.zkoss.bind.BindUtils;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

public class EmployeeExperienceListItemRenderer implements ListitemRenderer<TrsEmployeeExprience>{
	
	private EmployeeService employeeService = (EmployeeService) SpringUtil
			.getBean("employeeService");
	
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void render(Listitem item, final TrsEmployeeExprience trsEmployeeExprience, int index) throws Exception {
		Listcell listcell;

		final Button buttonSave = new Button();
		buttonSave.setImage("/images/icons/btn_save.gif");

		final Button buttonEdit = new Button();
		buttonEdit.setImage("/images/icons/btn_edit.gif");

		final Button buttonDelete = new Button();
		buttonDelete.setImage("/images/icons/btn_delete.gif");

		final Button buttonCancel = new Button();
		buttonCancel.setImage("/images/icons/btn_cancel.gif");
		
		final Label labelCompany = new Label();
		final Label labelJobTitle = new Label();
		final Label labelFrom = new Label();
		final Label labelTo = new Label();
		final Label labelComment = new Label();
		
		final Textbox textboxCompany = new Textbox();
		final Textbox textboxJobTitle = new Textbox();
		final Datebox dateboxFrom = new Datebox();
		final Datebox dateboxTo = new Datebox();
		final Textbox textboxComment = new Textbox();
		
		listcell = new Listcell();
		buttonEdit.setParent(listcell);
		buttonSave.setParent(listcell);
		buttonCancel.setParent(listcell);
		buttonDelete.setParent(listcell);
		listcell.setParent(item);
		
		listcell = new Listcell();
		textboxCompany.setParent(listcell);
		labelCompany.setParent(listcell);
		listcell.setParent(item);
		
		listcell = new Listcell();
		textboxJobTitle.setParent(listcell);
		labelJobTitle.setParent(listcell);
		listcell.setParent(item);
		
		listcell = new Listcell();
		dateboxFrom.setParent(listcell);
		labelFrom.setParent(listcell);
		listcell.setParent(item);
		
		listcell = new Listcell();
		dateboxTo.setParent(listcell);
		labelTo.setParent(listcell);
		listcell.setParent(item);
		
		listcell = new Listcell();
		textboxComment.setParent(listcell);
		textboxComment.setMultiline(true);
		textboxComment.setHeight("75px");
		labelComment.setParent(listcell);
		listcell.setParent(item);
		
		if (trsEmployeeExprience.getIdExprience() == null){
			buttonEdit.setVisible(false);
			buttonDelete.setVisible(false);
		} else {
			buttonSave.setVisible(false);
			buttonCancel.setVisible(false);
			buttonDelete.setVisible(false);
			
			labelCompany.setValue(trsEmployeeExprience.getCompany());
			labelJobTitle.setValue(trsEmployeeExprience.getJobTitle());
			labelFrom.setValue(trsEmployeeExprience.getFrom().toString());
			labelTo.setValue(trsEmployeeExprience.getTo().toString());
			labelComment.setValue(trsEmployeeExprience.getComment());
			
			textboxCompany.setVisible(false);
			textboxJobTitle.setVisible(false);
			dateboxFrom.setVisible(false);
			dateboxTo.setVisible(false);
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
				if (trsEmployeeExprience.getIdExprience() == null) {
					trsEmployeeExprience.setCompany(textboxCompany.getValue());
					trsEmployeeExprience.setJobTitle(textboxJobTitle.getValue());
					trsEmployeeExprience.setFrom(dateboxFrom.getValue());
					trsEmployeeExprience.setTo(dateboxTo.getValue());
					trsEmployeeExprience.setComment(textboxComment.getValue());
					
					employeeService.save(trsEmployeeExprience);

					BindUtils.postGlobalCommand(null, null, "refreshAfterSaveOrUpdateEmployeeExprience", null);
				} else {
					trsEmployeeExprience.setCompany(textboxCompany.getValue());
					trsEmployeeExprience.setJobTitle(textboxJobTitle.getValue());
					trsEmployeeExprience.setFrom(dateboxFrom.getValue());
					trsEmployeeExprience.setTo(dateboxTo.getValue());
					trsEmployeeExprience.setComment(textboxComment.getValue());
					
					employeeService.update(trsEmployeeExprience);

					BindUtils.postGlobalCommand(null, null, "refreshAfterSaveOrUpdateEmployeeExprience", null);
				}
			}
		});

		buttonEdit.addEventListener(Events.ON_CLICK, new EventListener() {
			public void onEvent(Event event) throws Exception {
				buttonEdit.setVisible(false);
				buttonSave.setVisible(true);
				buttonDelete.setVisible(true);

				textboxCompany.setVisible(true);
				textboxJobTitle.setVisible(true);
				dateboxFrom.setVisible(true);
				dateboxTo.setVisible(true);
				textboxComment.setVisible(true);

				labelCompany.setVisible(false);
				labelJobTitle.setVisible(false);
				labelFrom.setVisible(false);
				labelTo.setVisible(false);
				labelComment.setVisible(false);

				textboxCompany.setValue(trsEmployeeExprience.getCompany());
				textboxJobTitle.setValue(trsEmployeeExprience.getJobTitle());
				dateboxFrom.setValue(trsEmployeeExprience.getFrom());
				dateboxTo.setValue(trsEmployeeExprience.getTo());
				textboxComment.setValue(trsEmployeeExprience.getComment());

			}
		});

		buttonDelete.addEventListener(Events.ON_CLICK, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				Messagebox.show("Do you really want to remove item?", "Confirm", Messagebox.OK | Messagebox.CANCEL,
						Messagebox.EXCLAMATION, new EventListener() {
							public void onEvent(Event event) throws Exception {
								if (((Integer) event.getData()).intValue() == Messagebox.OK) {

									employeeService.delete(trsEmployeeExprience);

									BindUtils.postGlobalCommand(null, null, "refreshAfterSaveOrUpdateEmployeeExprience", null);
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
				BindUtils.postGlobalCommand(null, null, "refreshAfterSaveOrUpdateEmployeeExprience", null);
			}
		});
	}
	
}
