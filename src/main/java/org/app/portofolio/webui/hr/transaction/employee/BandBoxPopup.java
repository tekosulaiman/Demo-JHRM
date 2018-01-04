package org.app.portofolio.webui.hr.transaction.employee;

import java.util.HashMap;
import java.util.List;

import org.module.hr.model.MstMembership;
import org.module.hr.model.TrsEmployeeMembership;
import org.module.hr.service.EmployeeService;
import org.module.hr.service.QualificationService;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Bandbox;

public class BandBoxPopup {
	
	
	@WireVariable
	private QualificationService qualificationService;
	
	@WireVariable
	private EmployeeService employeeService;
	
	private Boolean isBandboxOpen;
	
	private TrsEmployeeMembership trsEmployeeMembership;
	
	private MstMembership mstMembership;
	private List<MstMembership> mstMemberships;
	private MstMembership selectedMstMembership;
	
	@Init
	public void setupComponents(@ContextParam(ContextType.VIEW) Component component,
			@ExecutionArgParam("object") Object object,
			@ExecutionArgParam("arg") TrsEmployeeMembership trsEmployeeMembership) {
 		mstMemberships = qualificationService.getAllMstMembership();
 		isBandboxOpen = Boolean.FALSE;
 		this.trsEmployeeMembership = trsEmployeeMembership;
 		
	}
	
	public QualificationService getQualificationService() {
		return qualificationService;
	}



	public void setQualificationService(QualificationService qualificationService) {
		this.qualificationService = qualificationService;
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
	
	public Boolean getIsBandboxOpen() {
		return isBandboxOpen;
	}
	
	public void setIsBandboxOpen(Boolean isBandboxOpen) {
		this.isBandboxOpen = isBandboxOpen;
	}
	
	
	
	@NotifyChange({ "*" })
	public void setSelectedMstMembership(MstMembership selectedMstMembership) {
		this.selectedMstMembership = selectedMstMembership;
		isBandboxOpen = Boolean.FALSE;
		trsEmployeeMembership.setIdMembership(selectedMstMembership);
		/*HashMap<String, Object> arg = new HashMap<>();
		arg.put("mstMembership", this.selectedMstMembership);
		arg.put("employeeMembershipID", this.employeeMembershipID);
		BindUtils.postGlobalCommand(null, null, "updateMembership", arg);*/
	}
}
