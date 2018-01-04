package org.app.portofolio.webui.security.rolegroup;

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
import org.module.sysadmin.model.SecRole;
import org.module.sysadmin.model.SecRolegroup;
import org.module.sysadmin.service.SecurityService;

/**
*
* @author tekosulaiman@yahoo.com
*/
public class SecRoleGroupDialogVM {

	@Wire("#comboboxRole")
	private Combobox comboboxRole;
	
	@Wire("#listBoxGroupAvaible")
	private Listbox listBoxGroupAvaible;
	
	@Wire("#listBoxGroupSelected")
	private Listbox listBoxGroupSelected;
		
	private SecRole secRole;
	private List<SecRole> secRoles;
	
	private SecGroup secGroup;
	private List<SecGroup> secGroups;
	
	private SecRolegroup secRolegroup;
	private List<SecRolegroup> secRolegroups;
	
	@WireVariable
	private SecurityService securityService;
	
	private List<SecGroup> avaibleSecRolegroups;
	private List<SecGroup> selectedSecRolegroups;
	
	private ListModelList<SecGroup> listModelListAvaibleSecRolegroups;
	private ListModelList<SecGroup> listModelListSelectedSecRolegroups;

	@AfterCompose
	public void setupComponents(@ContextParam(ContextType.VIEW) Component component,
		@ExecutionArgParam("object") Object object,
		@ExecutionArgParam("secRolegroup") SecRolegroup secRolegroup,
		@ExecutionArgParam("secRole") SecRole secRole,
		@ExecutionArgParam("secGroups") List<SecGroup> secGroups) {
		
		Selectors.wireComponents(component, this, false);
		
		if(secRole == null && secGroup == null){
			this.secRole = new SecRole();
			this.secGroup = new SecGroup();
			
			secRoles = securityService.getAllRoles();
			
			avaibleSecRolegroups = securityService.getAllGroups();
			listModelListAvaibleSecRolegroups = new ListModelList<SecGroup>(avaibleSecRolegroups);
			listBoxGroupAvaible.setModel(listModelListAvaibleSecRolegroups);
			
			listModelListSelectedSecRolegroups= new ListModelList<SecGroup>();
			listBoxGroupSelected.setModel(listModelListSelectedSecRolegroups);
			
			this.secRolegroup = new SecRolegroup();
		}else{
			/* Jika Dalam Posisi Edit */
			this.secRole = secRole;
			
			secRoles = securityService.getAllRoles();
			
			avaibleSecRolegroups = securityService.getAllGroups();
			listModelListAvaibleSecRolegroups = new ListModelList<SecGroup>(avaibleSecRolegroups);
			listBoxGroupAvaible.setModel(listModelListAvaibleSecRolegroups);

			selectedSecRolegroups = secGroups;
			listModelListSelectedSecRolegroups = new ListModelList<SecGroup>(secGroups);
			listBoxGroupSelected.setModel(listModelListSelectedSecRolegroups);
			
			notInAvailableGroup();
		}
	}
	
	public void notInAvailableGroup() {
		List<SecGroup> listTemp = new ArrayList<SecGroup>();
		for (SecGroup secGroupAvailable : listModelListAvaibleSecRolegroups) {
			for (SecGroup secGroupSelected : listModelListSelectedSecRolegroups) {
				if (secGroupAvailable.getGrpId().equals(secGroupSelected.getGrpId())) {
					listTemp.add(secGroupAvailable);
				}
			}
		}
		
		listModelListAvaibleSecRolegroups.removeAll(listTemp);
	}
	
	@Command
	public void doClose(){
		System.out.println("doClose");
		BindUtils.postGlobalCommand(null, null, "refreshAfterClose", null);
	}
	
	@Command
	public void doSelect(){
		SecRole secRole = (SecRole) comboboxRole.getSelectedItem().getValue();
		comboboxRole.setAttribute("comboboxRole", secRole);
	}
	
	@Command
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void doSave(){
		
		if(secRole.getRolId() == null){
			System.out.println("doSave");
			
			SecRole secRole = (SecRole) comboboxRole.getAttribute("comboboxRole");
			secRolegroup.setRolId(secRole);
			
			ListModelList<SecGroup> listModelListSecGroups = (ListModelList) listBoxGroupSelected.getModel();
			for(SecGroup secGroup : listModelListSecGroups){
				secRolegroup.setGrpId(secGroup);
				
				securityService.save(secRolegroup);
			}
		}else{
			System.out.println("doUpdate");
			
			/*Delete semua detail*/
			ListModelList<SecGroup> listModelListDeleteSecGroups = (ListModelList) listBoxGroupAvaible.getModel();
			for(SecGroup secGroup : listModelListDeleteSecGroups){
				
				SecRolegroup secRolegroup = securityService.getRolegroupByRoleAndGroup(secRole, secGroup);
				
				if (secRolegroup != null) {
					securityService.delete(secRolegroup);	
				}
			}
			
			/*Insert lagi di detail*/
			SecRolegroup secRolegroup = new SecRolegroup();
			secRolegroup.setRolId(secRole);
			
			ListModelList<SecGroup> listModelListSaveSecGroups = (ListModelList) listBoxGroupSelected.getModel();
			for(SecGroup secGroup : listModelListSaveSecGroups){
				secRolegroup.setGrpId(secGroup);
				SecRolegroup secRolegroupSelected = securityService.getRolegroupByRoleAndGroup(secRole, secGroup);
				
				if (secRolegroupSelected == null) {
					securityService.save(secRolegroup);	
				}
			}
		}
	}
	
	@Command
	@NotifyChange({"listModelListAvaibleSecRolegroups","listModelListSelectedSecRolegroups"})
	public void doChooseAll(){
		listModelListSelectedSecRolegroups.addAll(listModelListAvaibleSecRolegroups);
		listModelListAvaibleSecRolegroups.clear();
	}
	
	@Command
	@NotifyChange({"listModelListAvaibleSecRolegroups","listModelListSelectedSecRolegroups"})
	public void doChoose(){
		Set<SecGroup> set = listModelListAvaibleSecRolegroups.getSelection();
		listModelListSelectedSecRolegroups.addAll(set);
		listModelListAvaibleSecRolegroups.removeAll(set);		
	}
	
	@Command
	@NotifyChange({"listModelListAvaibleSecRolegroups","listModelListSelectedSecRolegroups"})
	public void doRemove(){
		Set<SecGroup> set = listModelListSelectedSecRolegroups.getSelection();
		listModelListAvaibleSecRolegroups.addAll(set);
		listModelListSelectedSecRolegroups.removeAll(set);
	}
	
	@Command
	@NotifyChange({"listModelListAvaibleSecRolegroups","listModelListSelectedSecRolegroups"})
	public void doRemoveAll(){
		listModelListAvaibleSecRolegroups.addAll(listModelListSelectedSecRolegroups);
		listModelListSelectedSecRolegroups.clear();
	}

	public SecRole getSecRole() {
		return secRole;
	}

	public void setSecRole(SecRole secRole) {
		this.secRole = secRole;
	}

	public List<SecRole> getSecRoles() {
		return secRoles;
	}

	public void setSecRoles(List<SecRole> secRoles) {
		this.secRoles = secRoles;
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

	public SecRolegroup getSecRolegroup() {
		return secRolegroup;
	}

	public void setSecRolegroup(SecRolegroup secRolegroup) {
		this.secRolegroup = secRolegroup;
	}

	public List<SecRolegroup> getSecRolegroups() {
		return secRolegroups;
	}

	public void setSecRolegroups(List<SecRolegroup> secRolegroups) {
		this.secRolegroups = secRolegroups;
	}

	public SecurityService getSecurityService() {
		return securityService;
	}

	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}

	public List<SecGroup> getAvaibleSecRolegroups() {
		return avaibleSecRolegroups;
	}

	public void setAvaibleSecRolegroups(List<SecGroup> avaibleSecRolegroups) {
		this.avaibleSecRolegroups = avaibleSecRolegroups;
	}

	public List<SecGroup> getSelectedSecRolegroups() {
		return selectedSecRolegroups;
	}

	public void setSelectedSecRolegroups(List<SecGroup> selectedSecRolegroups) {
		this.selectedSecRolegroups = selectedSecRolegroups;
	}

	public ListModelList<SecGroup> getListModelListAvaibleSecRolegroups() {
		return listModelListAvaibleSecRolegroups;
	}

	public void setListModelListAvaibleSecRolegroups(ListModelList<SecGroup> listModelListAvaibleSecRolegroups) {
		this.listModelListAvaibleSecRolegroups = listModelListAvaibleSecRolegroups;
	}

	public ListModelList<SecGroup> getListModelListSelectedSecRolegroups() {
		return listModelListSelectedSecRolegroups;
	}

	public void setListModelListSelectedSecRolegroups(ListModelList<SecGroup> listModelListSelectedSecRolegroups) {
		this.listModelListSelectedSecRolegroups = listModelListSelectedSecRolegroups;
	}
}