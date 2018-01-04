package org.app.portofolio.webui.hr.master.job.paygrade;

import java.util.List;

import org.app.portofolio.webui.hr.master.job.paygrade.model.MstPaygradeListItemRenderer;
import org.module.hr.model.MstPaygrade;
import org.module.hr.service.JobService;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Listbox;

public class PayGradeListVM {
	
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Wire component
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	@Wire("#listBoxPayGrade")
	private Listbox listBoxPayGrade;

	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Service yang dibutuhkan sesuai bisnis proses
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	private MstPaygrade mstPaygrade;
	private List<MstPaygrade> mstPaygrades;
	@WireVariable
	private JobService masterJobService;
	private MstPaygradeListItemRenderer mstPaygradeListItemRenderer;

	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Function Custom sesuai kebutuhan
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Inisialize Methode MVVM yang pertama kali dijalankan
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	@AfterCompose
	public void setupComponents(@ContextParam(ContextType.VIEW) Component component,
		@ExecutionArgParam("object") Object object) {
		
		Selectors.wireComponents(component, this, false);
	}
	
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Function CRUD Event
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	@Command
	public void doNew(){
		Executions.createComponents("/WEB-INF/pages/module_hr/master/job/paygrade/payGradeDialog.zul", null, null);
	}
	
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Getter Setter
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	public MstPaygrade getMstPaygrade() {
		return mstPaygrade;
	}

	public void setMstPaygrade(MstPaygrade mstPaygrade) {
		this.mstPaygrade = mstPaygrade;
	}

	public List<MstPaygrade> getMstPaygrades() {
		return mstPaygrades;
	}

	public void setMstPaygrades(List<MstPaygrade> mstPaygrades) {
		this.mstPaygrades = mstPaygrades;
	}

	public JobService getMasterJobService() {
		return masterJobService;
	}

	public void setMasterJobService(JobService masterJobService) {
		this.masterJobService = masterJobService;
	}

	public MstPaygradeListItemRenderer getMstPaygradeListItemRenderer() {
		return mstPaygradeListItemRenderer;
	}

	public void setMstPaygradeListItemRenderer(MstPaygradeListItemRenderer mstPaygradeListItemRenderer) {
		this.mstPaygradeListItemRenderer = mstPaygradeListItemRenderer;
	}
}