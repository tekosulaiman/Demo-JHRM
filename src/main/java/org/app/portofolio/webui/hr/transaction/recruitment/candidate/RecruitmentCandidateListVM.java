package org.app.portofolio.webui.hr.transaction.recruitment.candidate;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;

import org.app.portofolio.common.menu.util.JHRMAdditionalZulPath;
import org.app.portofolio.webui.hr.common.collections.ArgKey;
import org.app.portofolio.webui.hr.common.collections.ModalAction;
import org.module.hr.model.TrsJobCandidate;
import org.module.hr.service.RecruitmentService;
import org.module.hr.service.UploadFileService;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Listbox;

public class RecruitmentCandidateListVM {

	/*---------------- Zul ----------------*/
	@Wire("#listboxJobCandidate")
	private Listbox listboxJobCandidate;
	
	/* ------------- Services -------------*/
	@WireVariable
	private RecruitmentService recruitmentService;
	
	@WireVariable
	private UploadFileService uploadFileService;
	/* --------------- Beans --------------*/
	private List<TrsJobCandidate> trsJobCandidates;
	private TrsJobCandidate selectedTrsJobCandidate;
	private static final String SELECTED_JOB_CANDIDATE = "selectedCandidate";
	
	@AfterCompose
	public void setupComponents(@ContextParam(ContextType.VIEW) Component component,
		@ExecutionArgParam("object") Object object) {
		
		Selectors.wireComponents(component, this, false);
		
		constructListJobCandidate();
	}
	
	@Command
	public void doNew(){
		HashMap<String, Object> arg = new HashMap<>();
		arg.put(ArgKey.MODAL_ACTION_KEY, ModalAction.NEW);
		Executions.createComponents(JHRMAdditionalZulPath.Recruitment.Candidate.DIALOG_FORM, null, arg);
	}
	
	@Command
	public void doDetail(){
		HashMap<String, Object> arg = new HashMap<>();
		arg.put(ArgKey.MODAL_ACTION_KEY, ModalAction.DETAIL);
		Executions.createComponents(JHRMAdditionalZulPath.Recruitment.Candidate.DIALOG_FORM, null, arg);
	}
	
	/**
	 * Fungsi yang digunakan untuk menerapkan data yang diambil dari JobCandidate dalam bentuk daftar.
	 * 
	 */
	@GlobalCommand("refreshAfterSaveCandidate")
	@NotifyChange("trsJobCandidates")
	public void constructListJobCandidate() {
		trsJobCandidates = recruitmentService.getAllTrsJobCandidate();
	}
	
	@Command
	public void doDownload(@BindingParam(SELECTED_JOB_CANDIDATE) TrsJobCandidate selectedTrsJobCandidate) throws FileNotFoundException {
		Filedownload.save(uploadFileService.getResumeFile(selectedTrsJobCandidate), null);
	}
	
	/* ---------- GETTER-SETTER --------------*/
	
	public List<TrsJobCandidate> getTrsJobCandidates() {
		return trsJobCandidates;
	}

	public void setTrsJobCandidates(List<TrsJobCandidate> trsJobCandidate) {
		this.trsJobCandidates = trsJobCandidate;
	}

	public TrsJobCandidate getSelectedTrsJobCandidate() {
		return selectedTrsJobCandidate;
	}

	public void setSelectedTrsJobCandidate(TrsJobCandidate selectedTrsJobCandidate) {
		this.selectedTrsJobCandidate = selectedTrsJobCandidate;
	}
}
