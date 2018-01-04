package org.app.portofolio.webui.hr.transaction.employee.model;

import java.util.Date;

import org.app.portofolio.webui.hr.common.components.InlineListItemRenderer;
import org.module.hr.model.MstLicense;
import org.module.hr.model.TrsEmployeeLicense;
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
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

public class EmployeeLicenseListItemRenderer implements ListitemRenderer<TrsEmployeeLicense>{

	private EmployeeService employeeService = (EmployeeService) SpringUtil.getBean("employeeService");
	private QualificationService qualificationService = (QualificationService) SpringUtil
			.getBean("qualificationService");

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void render(Listitem item, final TrsEmployeeLicense trsEmployeeLicense, int index) throws Exception {
		Listcell listcell;

		final Button buttonSave = new Button();
		buttonSave.setImage("/images/icons/btn_save.gif");

		final Button buttonEdit = new Button();
		buttonEdit.setImage("/images/icons/btn_edit.gif");

		final Button buttonDelete = new Button();
		buttonDelete.setImage("/images/icons/btn_delete.gif");

		final Button buttonCancel = new Button();
		buttonCancel.setImage("/images/icons/btn_cancel.gif");
		
		final Label labelLicensetype = new Label();
		final Label labelLicenseNumber = new Label();
		final Label labelIssuedDate = new Label();
		final Label labelExpiryDate = new Label();
		
		final Combobox comboboxLicenseType = new Combobox();
		final Textbox textboxLicenseNumber = new Textbox();
		final Datebox dateboxIssuedDate = new Datebox();
		final Datebox dateboxExpiryDate = new Datebox();
		
		listcell = new Listcell();
		buttonEdit.setParent(listcell);
		buttonSave.setParent(listcell);
		buttonCancel.setParent(listcell);
		buttonDelete.setParent(listcell);
		listcell.setParent(item);
		
		listcell = new Listcell();
		comboboxLicenseType.setParent(listcell);
		labelLicensetype.setParent(listcell);
		listcell.setParent(item);
		
		listcell = new Listcell();
		textboxLicenseNumber.setParent(listcell);
		labelLicenseNumber.setParent(listcell);
		listcell.setParent(item);
		
		listcell = new Listcell();
		dateboxIssuedDate.setParent(listcell);
		labelIssuedDate.setParent(listcell);
		listcell.setParent(item);
		
		listcell =  new Listcell();
		dateboxExpiryDate.setParent(listcell);
		labelExpiryDate.setParent(listcell);
		listcell.setParent(item);
		
		if (trsEmployeeLicense.getIdEmployeeLicense() == null){
			buttonEdit.setVisible(false);
			buttonDelete.setVisible(false);
			comboboxLicenseType.setModel(new ListModelList<MstLicense>(qualificationService.getAllMstLicense()));
			comboboxLicenseType.setItemRenderer(new ComboitemRenderer<MstLicense>() {

				@Override
				public void render(Comboitem item, MstLicense mstLicense, int index) throws Exception {
					item.setLabel(mstLicense.getNameLicense());
					item.setAttribute("data", mstLicense);
				}
				
			});
		} else {
			buttonSave.setVisible(false);
			buttonCancel.setVisible(false);
			buttonDelete.setVisible(false);
			
			labelExpiryDate.setValue(trsEmployeeLicense.getExpiryDate() != null ? trsEmployeeLicense.getExpiryDate().toString() : "");
			labelIssuedDate.setValue(trsEmployeeLicense.getIssuedDate() != null ? trsEmployeeLicense.getIssuedDate().toString() : "");
			labelLicenseNumber.setValue(trsEmployeeLicense.getLicenseNumber());
			labelLicensetype.setValue(trsEmployeeLicense.getIdLicense().getNameLicense());
			
			comboboxLicenseType.setVisible(false);
			textboxLicenseNumber.setVisible(false);
			dateboxExpiryDate.setVisible(false);
			dateboxIssuedDate.setVisible(false);
		}
		
		buttonSave.addEventListener(Events.ON_CLICK, new EventListener() {

			@Override
			public void onEvent(Event event) throws Exception {
				trsEmployeeLicense.setIdLicense((MstLicense) comboboxLicenseType.getSelectedItem().getAttribute("data"));
				trsEmployeeLicense.setLicenseNumber(textboxLicenseNumber.getValue());
				trsEmployeeLicense.setIssuedDate(dateboxIssuedDate.getValue());
				trsEmployeeLicense.setExpiryDate(dateboxExpiryDate.getValue());
				if (trsEmployeeLicense.getIdEmployeeLicense() == null){
					employeeService.save(trsEmployeeLicense);
				} else {
					employeeService.update(trsEmployeeLicense);
				}
				 BindUtils.postGlobalCommand(null, null, "refreshAfterSaveOrUpdateEmployeeLicense", null);
			}
		});
		
		buttonEdit.addEventListener(Events.ON_CLICK, new EventListener() {

			@Override
			public void onEvent(Event event) throws Exception {
				buttonEdit.setVisible(false);
				buttonSave.setVisible(true);
				buttonDelete.setVisible(true);
				
				comboboxLicenseType.setVisible(true);
				textboxLicenseNumber.setVisible(true);
				dateboxExpiryDate.setVisible(true);
				dateboxIssuedDate.setVisible(true);
				
				labelExpiryDate.setVisible(false);
				labelIssuedDate.setVisible(false);
				labelLicenseNumber.setVisible(false);
				labelLicensetype.setVisible(false);
				
				comboboxLicenseType.setModel(new ListModelList<MstLicense>(qualificationService.getAllMstLicense()));
				comboboxLicenseType.setItemRenderer(new ComboitemRenderer<MstLicense>() {

					@Override
					public void render(Comboitem item, MstLicense mstLicense, int index) throws Exception {
						item.setLabel(mstLicense.getNameLicense());
						item.setAttribute("data", mstLicense);
					}
					
				});
				comboboxLicenseType.setValue(trsEmployeeLicense.getIdLicense().getNameLicense());
				textboxLicenseNumber.setValue(trsEmployeeLicense.getLicenseNumber());
				dateboxExpiryDate.setValue(dateboxExpiryDate.getValue());
				dateboxIssuedDate.setValue(dateboxIssuedDate.getValue());
			}
		});
		
		buttonDelete.addEventListener(Events.ON_CLICK, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				Messagebox.show("Do you really want to remove item?", "Confirm", Messagebox.OK | Messagebox.CANCEL,
						Messagebox.EXCLAMATION, new EventListener() {
							public void onEvent(Event event) throws Exception {
								if (((Integer) event.getData()).intValue() == Messagebox.OK) {

									employeeService.delete(trsEmployeeLicense);

									BindUtils.postGlobalCommand(null, null, "refreshAfterSaveOrUpdateEmployeeLicense", null);
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
				BindUtils.postGlobalCommand(null, null, "refreshAfterSaveOrUpdateEmployeeLicense", null);
			}
		});
	}
	
}
