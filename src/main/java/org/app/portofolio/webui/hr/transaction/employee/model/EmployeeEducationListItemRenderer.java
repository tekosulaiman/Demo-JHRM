package org.app.portofolio.webui.hr.transaction.employee.model;

import org.app.portofolio.webui.security.right.model.SecTypeListModelItemRenderer;
import org.module.hr.model.MstEducation;
import org.module.hr.model.TrsEmployeeEducation;
import org.module.hr.model.dto.RelationshipType;
import org.module.hr.service.EmployeeService;
import org.module.hr.service.QualificationService;
import org.module.sysadmin.model.dto.SecType;
import org.zkoss.bind.BindUtils;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

public class EmployeeEducationListItemRenderer implements ListitemRenderer<TrsEmployeeEducation> {

	private EmployeeService employeeService = (EmployeeService) SpringUtil.getBean("employeeService");

	private QualificationService qualificationService = (QualificationService) SpringUtil
			.getBean("qualificationService");

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void render(Listitem item, final TrsEmployeeEducation trsEmployeeEducation, int index) throws Exception {
		Listcell listcell;

		final Button buttonSave = new Button();
		buttonSave.setImage("/images/icons/btn_save.gif");

		final Button buttonEdit = new Button();
		buttonEdit.setImage("/images/icons/btn_edit.gif");

		final Button buttonDelete = new Button();
		buttonDelete.setImage("/images/icons/btn_delete.gif");

		final Button buttonCancel = new Button();
		buttonCancel.setImage("/images/icons/btn_cancel.gif");

		final Label labelLevel = new Label();
		final Label labelInstitite = new Label();
		final Label labelMajorSpecialization = new Label();
		final Label labelYear = new Label();
		final Label labelGpaScore = new Label();
		final Label labelStartDate = new Label();
		final Label labelEndDate = new Label();

		final Combobox comboboxLevel = new Combobox();
		final Textbox textboxInstitute = new Textbox();
		final Textbox textboxMajorSpecialization = new Textbox();
		final Intbox intboxYear = new Intbox();
		final Textbox textboxGpaScore = new Textbox();
		final Datebox dateboxStartDate = new Datebox();
		final Datebox dateboxEndDate = new Datebox();
		
		listcell = new Listcell();
		buttonEdit.setParent(listcell);
		buttonSave.setParent(listcell);
		buttonCancel.setParent(listcell);
		buttonDelete.setParent(listcell);
		listcell.setParent(item);

		listcell = new Listcell();
		comboboxLevel.setParent(listcell);
		comboboxLevel.setReadonly(true);
		labelLevel.setParent(listcell);
		listcell.setParent(item);

		listcell = new Listcell();
		textboxInstitute.setParent(listcell);
		labelInstitite.setParent(listcell);
		listcell.setParent(item);

		listcell = new Listcell();
		textboxMajorSpecialization.setParent(listcell);
		labelMajorSpecialization.setParent(listcell);
		listcell.setParent(item);

		listcell = new Listcell();
		intboxYear.setParent(listcell);
		labelYear.setParent(listcell);
		listcell.setParent(item);

		listcell = new Listcell();
		textboxGpaScore.setParent(listcell);
		labelGpaScore.setParent(listcell);
		listcell.setParent(item);

		listcell = new Listcell();
		dateboxStartDate.setParent(listcell);
		labelStartDate.setParent(listcell);
		listcell.setParent(item);

		listcell = new Listcell();
		dateboxEndDate.setParent(listcell);
		labelEndDate.setParent(listcell);
		listcell.setParent(item);

		if (trsEmployeeEducation.getIdEmployeeEducation() == null) {
			buttonEdit.setVisible(false);
			buttonDelete.setVisible(false);

			comboboxLevel.setModel(new ListModelList<MstEducation>(qualificationService.getAllMstEducation()));
			comboboxLevel.setItemRenderer(new ComboitemRenderer<MstEducation>() {

				@Override
				public void render(Comboitem item, MstEducation mstEducation, int index) throws Exception {

					item.setLabel(mstEducation.getEducationName());

					item.setAttribute("data", mstEducation);
				}
			});
		} else {
			buttonSave.setVisible(false);
			buttonCancel.setVisible(false);
			buttonDelete.setVisible(false);

			labelLevel.setValue(trsEmployeeEducation.getIdEducation().getEducationName());
			labelInstitite.setValue(trsEmployeeEducation.getInstitute());
			labelMajorSpecialization.setValue(trsEmployeeEducation.getMajorSpecialization());
			labelYear.setValue(trsEmployeeEducation.getYear().toString());
			labelGpaScore.setValue(trsEmployeeEducation.getGpaScore());
			labelStartDate.setValue(trsEmployeeEducation.getStartDate().toString());
			labelEndDate.setValue(trsEmployeeEducation.getEndDate().toString());

			comboboxLevel.setVisible(false);
			textboxInstitute.setVisible(false);
			textboxMajorSpecialization.setVisible(false);
			intboxYear.setVisible(false);
			textboxGpaScore.setVisible(false);
			dateboxStartDate.setVisible(false);
			dateboxEndDate.setVisible(false);
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
				if (trsEmployeeEducation.getIdEmployeeEducation() == null) {
					trsEmployeeEducation.setIdEducation((MstEducation) comboboxLevel.getSelectedItem().getAttribute("data"));
					trsEmployeeEducation.setInstitute(textboxInstitute.getValue());
					trsEmployeeEducation.setMajorSpecialization(textboxMajorSpecialization.getValue());
					trsEmployeeEducation.setYear(intboxYear.getValue());
					trsEmployeeEducation.setGpaScore(textboxGpaScore.getValue());
					trsEmployeeEducation.setStartDate(dateboxStartDate.getValue());
					trsEmployeeEducation.setEndDate(dateboxEndDate.getValue());

					employeeService.save(trsEmployeeEducation);

					BindUtils.postGlobalCommand(null, null, "refreshAfterSaveOrUpdateEmployeeEducation", null);
				} else {
					
					trsEmployeeEducation.setIdEducation((MstEducation) comboboxLevel.getSelectedItem().getAttribute("data"));
					trsEmployeeEducation.setInstitute(textboxInstitute.getValue());
					trsEmployeeEducation.setMajorSpecialization(textboxMajorSpecialization.getValue());
					trsEmployeeEducation.setYear(intboxYear.getValue());
					trsEmployeeEducation.setGpaScore(textboxGpaScore.getValue());
					trsEmployeeEducation.setStartDate(dateboxStartDate.getValue());
					trsEmployeeEducation.setEndDate(dateboxEndDate.getValue());

					employeeService.update(trsEmployeeEducation);

					BindUtils.postGlobalCommand(null, null, "refreshAfterSaveOrUpdateEmployeeEducation", null);
				}
			}
		});

		buttonEdit.addEventListener(Events.ON_CLICK, new EventListener() {
			public void onEvent(Event event) throws Exception {
				buttonEdit.setVisible(false);
				buttonSave.setVisible(true);
				buttonDelete.setVisible(true);

				comboboxLevel.setVisible(true);
				textboxInstitute.setVisible(true);
				textboxMajorSpecialization.setVisible(true);
				intboxYear.setVisible(true);
				textboxGpaScore.setVisible(true);
				dateboxStartDate.setVisible(true);
				dateboxEndDate.setVisible(true);
				
				labelLevel.setVisible(false);
				labelInstitite.setVisible(false);
				labelMajorSpecialization.setVisible(false);
				labelYear.setVisible(false);
				labelGpaScore.setVisible(false);
				labelStartDate.setVisible(false);
				labelEndDate.setVisible(false);
				
				comboboxLevel.setModel(new ListModelList<MstEducation>(qualificationService.getAllMstEducation()));
				comboboxLevel.setItemRenderer(new ComboitemRenderer<MstEducation>() {

					@Override
					public void render(Comboitem item,final MstEducation mstEducation, int index) throws Exception {

						item.setLabel(mstEducation.getEducationName());

						item.setAttribute("data", mstEducation);
					}
				});
				comboboxLevel.setValue(trsEmployeeEducation.getIdEducation().getEducationName());
				
				textboxInstitute.setValue(trsEmployeeEducation.getInstitute());
				textboxMajorSpecialization.setValue(trsEmployeeEducation.getMajorSpecialization());
				intboxYear.setValue(trsEmployeeEducation.getYear());
				textboxGpaScore.setValue(trsEmployeeEducation.getGpaScore());
				dateboxStartDate.setValue(trsEmployeeEducation.getStartDate());
				dateboxEndDate.setValue(trsEmployeeEducation.getEndDate());
			}
		});

		buttonDelete.addEventListener(Events.ON_CLICK, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				Messagebox.show("Do you really want to remove item?", "Confirm", Messagebox.OK | Messagebox.CANCEL,
						Messagebox.EXCLAMATION, new EventListener() {
							public void onEvent(Event event) throws Exception {
								if (((Integer) event.getData()).intValue() == Messagebox.OK) {

									employeeService.delete(trsEmployeeEducation);

									BindUtils.postGlobalCommand(null, null, "refreshAfterSaveOrUpdateEmployeeEducation", null);
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
				BindUtils.postGlobalCommand(null, null, "refreshAfterSaveOrUpdateEmployeeEducation", null);
			}
		});
	}
}
