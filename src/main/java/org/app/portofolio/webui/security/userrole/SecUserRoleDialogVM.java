package org.app.portofolio.webui.security.userrole;

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

import org.module.sysadmin.model.SecRole;
import org.module.sysadmin.model.SecUser;
import org.module.sysadmin.model.SecUserrole;
import org.module.sysadmin.service.SecurityService;
import org.module.sysadmin.service.UserService;

/**
 *
 * @author tekosulaiman@yahoo.com
 */
public class SecUserRoleDialogVM {

	@Wire("#comboboxUser")
	private Combobox comboboxUser;
	
	@Wire("#listBoxRoleAvaible")
	private Listbox listBoxRoleAvaible;

	@Wire("#listBoxRoleSelected")
	private Listbox listBoxRoleSelected;

	private SecUser secUser;
	private List<SecUser> secUsers;

	private SecRole secRole;
	private List<SecRole> secRoles;

	private SecUserrole secUserrole;
	private List<SecUserrole> secUserroles;

	@WireVariable
	private UserService userService;
	
	@WireVariable
	private SecurityService securityService;

	private List<SecRole> avaibleSecUserroles;
	private List<SecRole> selectedSecUserroles;
	
	private ListModelList<SecRole> listModelListAvaibleSecUserroles;
	private ListModelList<SecRole> listModelListSelectedSecUserroles;

	@AfterCompose
	public void setupComponents(
		@ContextParam(ContextType.VIEW) Component component,
		@ExecutionArgParam("object") Object object,
		@ExecutionArgParam("secUserrole") SecUserrole secUserrole,
		@ExecutionArgParam("secUser") SecUser secUser,
		@ExecutionArgParam("secRoles") List<SecRole> secRoles) {

		Selectors.wireComponents(component, this, false);
		
		if(secUser == null && secRole == null){
			this.secUser = new SecUser();
			this.secRole = new SecRole();
			
			secUsers = userService.getAllUsers();

			avaibleSecUserroles = securityService.getAllRoles();
			listModelListAvaibleSecUserroles = new ListModelList<SecRole>(avaibleSecUserroles);
			listBoxRoleAvaible.setModel(listModelListAvaibleSecUserroles);

			listModelListSelectedSecUserroles = new ListModelList<SecRole>();
			listBoxRoleSelected.setModel(listModelListSelectedSecUserroles);
			
			this.secUserrole = new SecUserrole();
		}else{
			/* Jika Dalam Posisi Edit */
			this.secUser = secUser;
			
			secUsers = userService.getAllUsers();
	
			avaibleSecUserroles = securityService.getAllRoles();
			listModelListAvaibleSecUserroles = new ListModelList<SecRole>(avaibleSecUserroles);
			listBoxRoleAvaible.setModel(listModelListAvaibleSecUserroles);

			selectedSecUserroles = secRoles;
			listModelListSelectedSecUserroles = new ListModelList<SecRole>(secRoles);
			listBoxRoleSelected.setModel(listModelListSelectedSecUserroles);
			
			notInAvailableRole();
		}
	}

	public void notInAvailableRole() {
		List<SecRole> listTemp = new ArrayList<SecRole>();
		for (SecRole secRoleAvailable : listModelListAvaibleSecUserroles) {
			for (SecRole secRoleSelected : listModelListSelectedSecUserroles) {
				if (secRoleAvailable.getRolId().equals(secRoleSelected.getRolId())) {
					listTemp.add(secRoleAvailable);
				}
			}
		}
		
		listModelListAvaibleSecUserroles.removeAll(listTemp);
	}

	@Command
	public void doClose() {
		System.out.println("doClose");
		BindUtils.postGlobalCommand(null, null, "refreshAfterClose", null);
	}
	
	@Command
	public void doSelect(){
		SecUser secUser = (SecUser) comboboxUser.getSelectedItem().getValue();
		comboboxUser.setAttribute("comboboxUser", secUser);
	}
	
	@Command
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void doSave(){
		
		if(secUser.getUsrId() == null){
			System.out.println("doSave");
			
			SecUser secUser = (SecUser) comboboxUser.getAttribute("comboboxUser");
			secUserrole.setUsrId(secUser);
			
			ListModelList<SecRole> listModelListSecRoles = (ListModelList) listBoxRoleSelected.getModel();
			for(SecRole secRole : listModelListSecRoles){
				secUserrole.setRolId(secRole);
				
				securityService.save(secUserrole);
			}
		}else{
			System.out.println("doUpdate");
			
			/*Delete semua detail*/
			ListModelList<SecRole> listModelListDeleteSecRoles = (ListModelList) listBoxRoleAvaible.getModel();
			for(SecRole secRole : listModelListDeleteSecRoles){
				
				SecUserrole secUserrole = securityService.getUseRoleByUserAndRole(secUser, secRole);
				
				if (secUserrole != null) {
					securityService.delete(secUserrole);	
				}
			}
			
			/*Insert lagi di detail*/
			SecUserrole secUserrole = new SecUserrole();
			secUserrole.setUsrId(secUser);
			
			ListModelList<SecRole> listModelListSaveSecRoles = (ListModelList) listBoxRoleSelected.getModel();
			for(SecRole secRole : listModelListSaveSecRoles){
				secUserrole.setRolId(secRole);
				SecUserrole secUserroleSelected = securityService.getUseRoleByUserAndRole(secUser, secRole);
				
				if(secUserroleSelected == null){
					securityService.save(secUserrole);
				}
				
			}
		}
	}

	@Command
	@NotifyChange({ "listModelListAvaibleSecUserroles","listModelListSelectedSecUserroles" })
	public void doChooseAll() {
		listModelListSelectedSecUserroles.addAll(listModelListAvaibleSecUserroles);
		listModelListAvaibleSecUserroles.clear();
	}

	@Command
	@NotifyChange({ "listModelListAvaibleSecUserroles","listModelListSelectedSecUserroles" })
	public void doChoose() {
		Set<SecRole> set = listModelListAvaibleSecUserroles.getSelection();
		listModelListSelectedSecUserroles.addAll(set);
		listModelListAvaibleSecUserroles.removeAll(set);
	}

	@Command
	@NotifyChange({ "listModelListAvaibleSecUserroles","listModelListSelectedSecUserroles" })
	public void doRemove() {
		Set<SecRole> set = listModelListSelectedSecUserroles.getSelection();
		listModelListAvaibleSecUserroles.addAll(set);
		listModelListSelectedSecUserroles.removeAll(set);
	}

	@Command
	@NotifyChange({ "listModelListAvaibleSecUserroles","listModelListSelectedSecUserroles" })
	public void doRemoveAll() {
		listModelListAvaibleSecUserroles.addAll(listModelListSelectedSecUserroles);
		listModelListSelectedSecUserroles.clear();
	}

	public SecUser getSecUser() {
		return secUser;
	}

	public void setSecUser(SecUser secUser) {
		this.secUser = secUser;
	}

	public List<SecUser> getSecUsers() {
		return secUsers;
	}

	public void setSecUsers(List<SecUser> secUsers) {
		this.secUsers = secUsers;
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

	public SecUserrole getSecUserrole() {
		return secUserrole;
	}

	public void setSecUserrole(SecUserrole secUserrole) {
		this.secUserrole = secUserrole;
	}

	public List<SecUserrole> getSecUserroles() {
		return secUserroles;
	}

	public void setSecUserroles(List<SecUserrole> secUserroles) {
		this.secUserroles = secUserroles;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public SecurityService getSecurityService() {
		return securityService;
	}

	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}

	public List<SecRole> getAvaibleSecUserroles() {
		return avaibleSecUserroles;
	}

	public void setAvaibleSecUserroles(List<SecRole> avaibleSecUserroles) {
		this.avaibleSecUserroles = avaibleSecUserroles;
	}

	public List<SecRole> getSelectedSecUserroles() {
		return selectedSecUserroles;
	}

	public void setSelectedSecUserroles(List<SecRole> selectedSecUserroles) {
		this.selectedSecUserroles = selectedSecUserroles;
	}

	public ListModelList<SecRole> getListModelListAvaibleSecUserroles() {
		return listModelListAvaibleSecUserroles;
	}

	public void setListModelListAvaibleSecUserroles(ListModelList<SecRole> listModelListAvaibleSecUserroles) {
		this.listModelListAvaibleSecUserroles = listModelListAvaibleSecUserroles;
	}

	public ListModelList<SecRole> getListModelListSelectedSecUserroles() {
		return listModelListSelectedSecUserroles;
	}

	public void setListModelListSelectedSecUserroles(ListModelList<SecRole> listModelListSelectedSecUserroles) {
		this.listModelListSelectedSecUserroles = listModelListSelectedSecUserroles;
	}
}