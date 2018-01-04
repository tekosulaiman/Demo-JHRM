package org.app.portofolio.webui.security.groupright;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;

import org.module.sysadmin.model.SecGroup;
import org.module.sysadmin.model.SecGroupright;
import org.module.sysadmin.model.SecRight;
import org.module.sysadmin.service.SecurityService;

/**
*
* @author tekosulaiman@yahoo.com
*/
public class SecGroupRightDialogVM {
	
	@Wire("#comboboxGroup")
	private Combobox comboboxGroup;
	
	@Wire("#listBoxRightAvaible")
	private Listbox listBoxRightAvaible;
	
	@Wire("#listBoxRightSelected")
	private Listbox listBoxRightSelected;
		
	private SecGroup secGroup;
	private List<SecGroup> secGroups;
	
	private SecRight secRight;
	private List<SecRight> secRights;
	
	private SecGroupright secGroupright;
	private List<SecGroupright> secGrouprights;
	
	@WireVariable
	private SecurityService securityService;
	
	private List<SecRight> avaibleSecSecGrouprights;
	private List<SecRight> selectedSecSecGrouprights;
	
	private ListModelList<SecRight> listModelListAvaibleSecGrouprights;
	private ListModelList<SecRight> listModelListSelectedSecGrouprights;

	@AfterCompose
	public void setupComponents(@ContextParam(ContextType.VIEW) Component component,
		@ExecutionArgParam("object") Object object,
		@ExecutionArgParam("secGroupright") SecGroupright secGroupright,
		@ExecutionArgParam("secGroup") SecGroup secGroup,
		@ExecutionArgParam("secRights") List<SecRight> secRights) {
		
		Selectors.wireComponents(component, this, false);
		
		if(secGroup == null && secRight == null){
			this.secGroup = new SecGroup();
			this.secRight = new SecRight();
			
			secGroups = securityService.getAllGroups();
			
			avaibleSecSecGrouprights = securityService.getAllRights();
			listModelListAvaibleSecGrouprights = new ListModelList<SecRight>(avaibleSecSecGrouprights);
			listBoxRightAvaible.setModel(listModelListAvaibleSecGrouprights);
			
			listModelListSelectedSecGrouprights= new ListModelList<SecRight>();
			listBoxRightSelected.setModel(listModelListSelectedSecGrouprights);
			
			this.secGroupright = new SecGroupright();
		}else {
			/* Jika Dalam Posisi Edit */
			this.secGroup = secGroup;
			
			secGroups = securityService.getAllGroups();
			
			avaibleSecSecGrouprights = securityService.getAllRights();
			listModelListAvaibleSecGrouprights = new ListModelList<SecRight>(avaibleSecSecGrouprights);
			listBoxRightAvaible.setModel(listModelListAvaibleSecGrouprights);

			selectedSecSecGrouprights = secRights;
			listModelListSelectedSecGrouprights = new ListModelList<SecRight>(secRights);
			listBoxRightSelected.setModel(listModelListSelectedSecGrouprights);
			
			notInAvailableRight();
		}
	}
	
	public void notInAvailableRight() {
		List<SecRight> listTemp = new ArrayList<SecRight>();
		for (SecRight secRightAvailable : listModelListAvaibleSecGrouprights) {
			for (SecRight secRightSelected : listModelListSelectedSecGrouprights) {
				if (secRightAvailable.getRigId().equals(secRightSelected.getRigId())) {
					listTemp.add(secRightAvailable);
				}
			}
		}
		
		listModelListAvaibleSecGrouprights.removeAll(listTemp);
	}
	
	@Command
	public void doClose(){
		System.out.println("doClose");
		BindUtils.postGlobalCommand(null, null, "refreshAfterClose", null);
	}
	
	@Command
	public void doSelect(){
		SecGroup secGroup = (SecGroup) comboboxGroup.getSelectedItem().getValue();
		comboboxGroup.setAttribute("comboboxGroup", secGroup);
	}
	
	@Command
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void doSave(){
		
		if(secGroup.getGrpId() == null){
			System.out.println("doSave");
			
			SecGroup secGroup = (SecGroup) comboboxGroup.getAttribute("comboboxGroup");
			secGroupright.setGrpId(secGroup);
			
			ListModelList<SecRight> listModelListSecRights = (ListModelList) listBoxRightSelected.getModel();
			for(SecRight secRight : listModelListSecRights){
				secGroupright.setRigId(secRight);
				
				securityService.save(secGroupright);
			}
		}else{
			System.out.println("doUpdate");
			
			/*Delete semua detail*/
			ListModelList<SecRight> listModelListDeleteSecRights = (ListModelList) listBoxRightAvaible.getModel();
			for(SecRight secRight : listModelListDeleteSecRights){
				
				SecGroupright secGroupright = securityService.getGroupRightByGroupAndRights(secGroup, secRight);
				
				if (secGroupright != null) {
					securityService.delete(secGroupright);	
				}
			}
			
			/*Insert lagi di detail*/
			SecGroupright secGroupright = new SecGroupright();
			secGroupright.setGrpId(secGroup);
			
			ListModelList<SecRight> listModelListSaveSecRights = (ListModelList) listBoxRightSelected.getModel();
			for(SecRight secRight : listModelListSaveSecRights){
				secGroupright.setRigId(secRight);
				SecGroupright secGrouprightSelected = securityService.getGroupRightByGroupAndRights(secGroup, secRight);
				
				if (secGrouprightSelected == null) {
					securityService.save(secGroupright);	
				}
			}
		}
	}
	
	@Command
	@NotifyChange({"listModelListAvaibleSecGrouprights","listModelListSelectedSecGrouprights"})
	public void doChooseAll(){
		listModelListSelectedSecGrouprights.addAll(listModelListAvaibleSecGrouprights);
		listModelListAvaibleSecGrouprights.clear();
	}
	
	@Command
	@NotifyChange({"listModelListAvaibleSecGrouprights","listModelListSelectedSecGrouprights"})
	public void doChoose(){
		Set<SecRight> set = listModelListAvaibleSecGrouprights.getSelection();
		listModelListSelectedSecGrouprights.addAll(set);
		listModelListAvaibleSecGrouprights.removeAll(set);		
	}
	
	@Command
	@NotifyChange({"listModelListAvaibleSecGrouprights","listModelListSelectedSecGrouprights"})
	public void doRemove(){
		Set<SecRight> set = listModelListSelectedSecGrouprights.getSelection();
		listModelListAvaibleSecGrouprights.addAll(set);
		listModelListSelectedSecGrouprights.removeAll(set);
	}
	
	@Command
	@NotifyChange({"listModelListAvaibleSecGrouprights","listModelListSelectedSecGrouprights"})
	public void doRemoveAll(){
		listModelListAvaibleSecGrouprights.addAll(listModelListSelectedSecGrouprights);
		listModelListSelectedSecGrouprights.clear();
	}

	public SecGroup getSecGroup() {
		return secGroup;
	}

	public void setSecGroup(SecGroup secGroup) {
		this.secGroup = secGroup;
	}

	public List<SecGroup> getSecGroups() {
		return secGroups;
	}

	public void setSecGroups(List<SecGroup> secGroups) {
		this.secGroups = secGroups;
	}

	public SecRight getSecRight() {
		return secRight;
	}

	public void setSecRight(SecRight secRight) {
		this.secRight = secRight;
	}

	public List<SecRight> getSecRights() {
		return secRights;
	}

	public void setSecRights(List<SecRight> secRights) {
		this.secRights = secRights;
	}

	public SecGroupright getSecGroupright() {
		return secGroupright;
	}

	public void setSecGroupright(SecGroupright secGroupright) {
		this.secGroupright = secGroupright;
	}

	public List<SecGroupright> getSecGrouprights() {
		return secGrouprights;
	}

	public void setSecGrouprights(List<SecGroupright> secGrouprights) {
		this.secGrouprights = secGrouprights;
	}

	public SecurityService getSecurityService() {
		return securityService;
	}

	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}

	public List<SecRight> getAvaibleSecSecGrouprights() {
		return avaibleSecSecGrouprights;
	}

	public void setAvaibleSecSecGrouprights(List<SecRight> avaibleSecSecGrouprights) {
		this.avaibleSecSecGrouprights = avaibleSecSecGrouprights;
	}

	public List<SecRight> getSelectedSecSecGrouprights() {
		return selectedSecSecGrouprights;
	}

	public void setSelectedSecSecGrouprights(List<SecRight> selectedSecSecGrouprights) {
		this.selectedSecSecGrouprights = selectedSecSecGrouprights;
	}

	public ListModelList<SecRight> getListModelListAvaibleSecGrouprights() {
		return listModelListAvaibleSecGrouprights;
	}

	public void setListModelListAvaibleSecGrouprights(ListModelList<SecRight> listModelListAvaibleSecGrouprights) {
		this.listModelListAvaibleSecGrouprights = listModelListAvaibleSecGrouprights;
	}

	public ListModelList<SecRight> getListModelListSelectedSecGrouprights() {
		return listModelListSelectedSecGrouprights;
	}

	public void setListModelListSelectedSecGrouprights(ListModelList<SecRight> listModelListSelectedSecGrouprights) {
		this.listModelListSelectedSecGrouprights = listModelListSelectedSecGrouprights;
	}
}