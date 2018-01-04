package org.app.portofolio.webui.hr.transaction.employee.model;

import java.util.List;

import org.module.hr.model.MstMembership;
import org.module.hr.model.TrsEmployee;
import org.module.hr.model.TrsEmployeeEmergencyContact;
import org.module.hr.model.TrsEmployeeMembership;
import org.module.hr.service.EmployeeService;
import org.module.hr.service.QualificationService;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Bandpopup;
import org.zkoss.zul.Button;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;

public class MembershipListItemRenderer implements ListitemRenderer<TrsEmployeeMembership>{
	
	@WireVariable
	private QualificationService masterQualificationService;
	
	private EmployeeService employeeService = (EmployeeService) SpringUtil
			.getBean("employeeService");
	
//	final Bandbox bandbox = new Bandbox();
	
	private MstMembership mstMembership;
	private List<MstMembership> mstMemberships;
	private MstMembership selectedMstMembership;
	
	
	@AfterCompose
	public void setupComponents(@ContextParam(ContextType.VIEW) Component component,
			@ExecutionArgParam("object") Object object) {
		mstMemberships = masterQualificationService.getAllMstMembership();
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void render(Listitem item, final TrsEmployeeMembership trsEmployeeMembership, int index) throws Exception {
		Listcell listcell;
		
		final Button buttonSave = new Button();
		buttonSave.setImage("/images/icons/btn_save.gif");

		final Button buttonEdit = new Button();
		buttonEdit.setImage("/images/icons/btn_edit.gif");

		final Button buttonDelete = new Button();
		buttonDelete.setImage("/images/icons/btn_delete.gif");

		final Button buttonCancel = new Button();
		buttonCancel.setImage("/images/icons/btn_cancel.gif");
		
		final Label labelMembership = new Label();
		
		listcell = new Listcell();
		buttonEdit.setParent(listcell);
		buttonSave.setParent(listcell);
		buttonCancel.setParent(listcell);
		buttonDelete.setParent(listcell);
		listcell.setParent(item);
		
		final Include include = new Include();
		include.setSrc("/WEB-INF/pages/module_hr/transaction/employee/tab_membership/bandBoxPopupMembership.zul");
		include.setDynamicProperty("arg", trsEmployeeMembership);
		
		listcell = new Listcell();
		include.setParent(listcell);
		labelMembership.setParent(listcell);
		listcell.setParent(item);
		
		if (trsEmployeeMembership.getIdEmployeeMembership() == null) {
			buttonEdit.setVisible(false);
			buttonDelete.setVisible(false);
		} else {
			buttonSave.setVisible(false);
			buttonCancel.setVisible(false);
			buttonDelete.setVisible(false);

			labelMembership.setValue(trsEmployeeMembership.getIdMembership().getNameMembership());
			include.setVisible(false);
		}
	
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Function CRUD Event
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	buttonSave.addEventListener(Events.ON_CLICK, new EventListener() {
		@Override
		public void onEvent(Event event) throws Exception {
			if(trsEmployeeMembership.getIdEmployeeMembership() == null){
				employeeService.save(trsEmployeeMembership);
				BindUtils.postGlobalCommand(null, null, "refreshAfterSaveOrUpdate", null);
			}else{
				employeeService.update(trsEmployeeMembership);
				BindUtils.postGlobalCommand(null, null, "refreshAfterSaveOrUpdate", null);
			}
		}
	});
	
	buttonEdit.addEventListener(Events.ON_CLICK, new EventListener() {
		public void onEvent(Event event) throws Exception {		
			buttonEdit.setVisible(false);
			buttonSave.setVisible(true);
			buttonDelete.setVisible(true);
			
			include.setVisible(true);
			
			labelMembership.setVisible(false);
			
		}					
	});
	
	buttonDelete.addEventListener(Events.ON_CLICK, new EventListener() {
		@Override
		public void onEvent(Event event) throws Exception {
			Messagebox.show("Do you really want to remove item?", "Confirm", Messagebox.OK | Messagebox.CANCEL, Messagebox.EXCLAMATION, new EventListener() {
			    public void onEvent(Event event) throws Exception {    	
			 		if (((Integer) event.getData()).intValue() == Messagebox.OK) {
			 			include.getAttribute("selectedMstMembership");
			 			employeeService.delete(trsEmployeeMembership);
			 			
			 			BindUtils.postGlobalCommand(null, null, "refreshAfterSaveOrUpdate", null);
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
			BindUtils.postGlobalCommand(null, null, "refreshAfterSaveOrUpdate", null);
		}
	});
	}
	
	@GlobalCommand
	public void updateMembership(@ExecutionArgParam("mstMembership") MstMembership mstMembership){
		selectedMstMembership = mstMembership;
	}

	public QualificationService getMasterQualificationService() {
		return masterQualificationService;
	}

	public void setMasterQualificationService(QualificationService masterQualificationService) {
		this.masterQualificationService = masterQualificationService;
	}

	public MstMembership getMstMembership() {
		return mstMembership;
	}

	public void setMstMembership(MstMembership mstMembership) {
		this.mstMembership = mstMembership;
	}

	public List<MstMembership> getMstMemberships() {
		return mstMemberships;
	}

	public void setMstMemberships(List<MstMembership> mstMemberships) {
		this.mstMemberships = mstMemberships;
	}

	public MstMembership getSelectedMstMembership() {
		return selectedMstMembership;
	}

	@NotifyChange({ "*" })
	public void setSelectedMstMembership(MstMembership selectedMstMembership) {
		this.selectedMstMembership = selectedMstMembership;
	}

}
