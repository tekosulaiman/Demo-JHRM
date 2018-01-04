package org.app.portofolio.webui.hr.transaction.recruitment.vacancy;

import java.util.HashMap;
import java.util.List;

import org.app.portofolio.UserWorkspace;
import org.app.portofolio.common.menu.util.JHRMAdditionalZulPath;
import org.app.portofolio.webui.hr.common.collections.ArgKey;
import org.app.portofolio.webui.hr.common.collections.ModalAction;
import org.module.hr.model.TrsJobVacancy;
import org.module.hr.service.RecruitmentService;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class RecruitmentVacancyListVM {

	/*---------------- Zul ----------------*/
	@Wire("#listboxJobVacancy")
	private Listbox listboxJobVacancy;
	
	/* ------------- Services -------------*/
	@WireVariable
	private RecruitmentService recruitmentService;
	
	/* --------------- Beans --------------*/
	private List<TrsJobVacancy> trsJobVacancies;
	private TrsJobVacancy selectedTrsJobVacancy;
	private Boolean listheaderActiveAllowed;
	
	@AfterCompose
	public void setupComponents(@ContextParam(ContextType.VIEW) Component component, @ExecutionArgParam("object") Object object) {
		Selectors.wireComponents(component, this, false);
		
		final UserWorkspace workspace = new UserWorkspace();
		listheaderActiveAllowed = workspace.isAllowed("component_Listheader_Active_Vacancy_List");
		
		if (listheaderActiveAllowed) {
			trsJobVacancies = recruitmentService.getAllTrsJobVacancy();
		} else {
			HashMap<String, Object> params = new HashMap<>();
			params.put("active", true);
			trsJobVacancies = recruitmentService.getTrsJobVacancyByRequest(params);
		}
	}
	
	@Command
	public void doNew(){
		HashMap<String, Object> arg = new HashMap<>();
		arg.put(ArgKey.MODAL_ACTION_KEY, ModalAction.NEW);
		Executions.createComponents(JHRMAdditionalZulPath.Recruitment.Vacancy.DIALOG_FORM, null, arg);
	}
	
	@Command
	public void doDetail(){
		HashMap<String, Object> arg = new HashMap<>();
		arg.put(ArgKey.MODAL_ACTION_KEY, ModalAction.DETAIL);
		arg.put(ArgKey.SELECTED_TRANSACTION_JOB_VACANCY, this.selectedTrsJobVacancy);
		Executions.createComponents(JHRMAdditionalZulPath.Recruitment.Vacancy.DIALOG_FORM, null, arg);
	}
	
	@Command
	public void doDelete(){
		final ListModelList<TrsJobVacancy> listModelListtrsJobVacancy = (ListModelList) listboxJobVacancy.getModel();
				
				if(listboxJobVacancy.getSelectedIndex() == -1){
					Messagebox.show("There is no selected record?", "Confirm", Messagebox.OK, Messagebox.ERROR);
				}else{
					Messagebox.show("Do you really want to remove item?", "Confirm", Messagebox.OK | Messagebox.CANCEL, Messagebox.EXCLAMATION, new EventListener() {
					    public void onEvent(Event event) throws Exception {    	
					 		if (((Integer) event.getData()).intValue() == Messagebox.OK) {
					 			for(TrsJobVacancy trsJobVacancy : listModelListtrsJobVacancy){
					 				if(listModelListtrsJobVacancy.isSelected(trsJobVacancy)){
					 					recruitmentService.delete(trsJobVacancy);
					 				}
					 			}
					 			
					 			BindUtils.postGlobalCommand(null, null, "refreshTrsJobVacancyList", null);
					 		}else{
					 			return;
					 		}
					 	}
					});
				}
	}
	
	@GlobalCommand("refreshTrsJobVacancyList")
	@NotifyChange("trsJobVacancies")
	public void refreshTrsJobVacancyList() {
		trsJobVacancies = recruitmentService.getAllTrsJobVacancy();
	}
	
	
	/* ---------- GETTER-SETTER --------------*/
	
	public List<TrsJobVacancy> getTrsJobVacancies() {
		return trsJobVacancies;
	}
	
	public void setTrsJobVacancies(List<TrsJobVacancy> trsJobVacancies) {
		this.trsJobVacancies = trsJobVacancies;
	}
	
	public TrsJobVacancy getSelectedTrsJobVacancy() {
		return selectedTrsJobVacancy;
	}
	
	public void setSelectedTrsJobVacancy(TrsJobVacancy selectedTrsJobVacancy) {
		this.selectedTrsJobVacancy = selectedTrsJobVacancy;
	}
	
	public Boolean getListheaderActiveAllowed() {
		return listheaderActiveAllowed;
	}
	
	public void setListheaderActiveAllowed(Boolean listheaderActiveAllowed) {
		this.listheaderActiveAllowed = listheaderActiveAllowed;
	}
}
