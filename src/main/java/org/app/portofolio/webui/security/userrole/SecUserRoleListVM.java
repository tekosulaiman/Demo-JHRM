package org.app.portofolio.webui.security.userrole;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.module.sysadmin.model.SecRole;
import org.module.sysadmin.model.SecUser;
import org.module.sysadmin.model.SecUserrole;
import org.module.sysadmin.service.SecurityService;
import org.module.sysadmin.service.UserService;
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
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
*
* @author tekosulaiman@yahoo.com
*/
public class SecUserRoleListVM {
	
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Wire component
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	@Wire("#windowUserRoleList")
	private Window windowUserRoleList;

	@Wire("#windowSecUserRole")
	private Window windowSecUserRole;
	
	@Wire("#textboxFilter")
	private Textbox textboxFilter;
	
	@Wire("#listBoxUserUserRole")
	private Listbox listBoxUserUserRole;
	
	@Wire("#listBoxRoleUserRole")
	private Listbox listBoxRoleUserRole;

	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Service yang dibutuhkan sesuai bisnis proses
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	@WireVariable
	private UserService userService;
	
	@WireVariable
	private SecurityService securityService;
	
	private SecUser secUser;
	private List<SecUser> secUsers;
	
	private SecRole secRole; 
	private List<SecRole> secRoles;
	
	private SecUserrole secUserrole;
	private List<SecUserrole> secUserroles;
	
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Function Custom sesuai kebutuhan
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	public void doPrepareList(){
		listBoxUserUserRole.setCheckmark(true);
		listBoxUserUserRole.setMultiple(true);
		listBoxUserUserRole.setRows(5);
		listBoxUserUserRole.setStyle("border-style: none;");
		listBoxUserUserRole.setMold("paging");
		listBoxUserUserRole.setAutopaging(true);
		
		listBoxRoleUserRole.setRows(5);
		listBoxRoleUserRole.setStyle("border-style: none;");
		listBoxRoleUserRole.setMold("paging");
		listBoxRoleUserRole.setAutopaging(true);
	}
	
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Inisialize Methode MVVM yang pertama kali dijalankan
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	@AfterCompose
	public void setupComponents(@ContextParam(ContextType.VIEW) Component component,
		@ExecutionArgParam("object") Object object,
		@ExecutionArgParam("secUserrole") SecUserrole secUserrole) {
		
		Selectors.wireComponents(component, this, false);

		secUsers = userService.getAllUsers();
		doPrepareList();
	}
	
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Function CRUD Event
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	@Command
	@NotifyChange("secUsers")
	public void doFilter(){
		secUsers.clear();
        
        List<SecUser> cacheSecUsers = userService.getAllUsers();
		String getName = textboxFilter.getValue();
		
		if(getName == null || "".equals(getName)) {
			secUsers.addAll(cacheSecUsers);
			
			listBoxUserUserRole.setModel(new ListModelList<SecUser>(secUsers));
			
			doPrepareList();
		} else {
			try {
				for(SecUser secUser: cacheSecUsers) {
					if(secUser.getUsrLoginname().toLowerCase().indexOf(getName.toLowerCase()) >= 0) {
						secUsers.add(secUser);
					}
					
					listBoxUserUserRole.setModel(new ListModelList<SecUser>(secUsers));
					
					doPrepareList();
				}
			}catch (Exception e) {
				
			}
		}
	}
	
	@Command
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@NotifyChange("secRoles")
	public void doSelect(){		
		ListModelList<SecUser> listModelListSecUsers = (ListModelList) listBoxUserUserRole.getModel();
		Set<SecUser> setSecUsers = listModelListSecUsers.getSelection();

		if(secUser == null){
			secRoles.clear();
		}else{
			SecUser secUser = setSecUsers.iterator().next();
			secRoles = userService.getRoleByUsers(secUser);
		}
	}
	
	@Command
	public void doNew(){
		/*HashMap<K, V> K = Key, V = Value*/
		HashMap<String, Object> arg = new HashMap<String, Object>();
		arg.put("object", "windowSecUserRole");
		Executions.createComponents("/WEB-INF/pages/sec_user_role/secUserRoleDialog.zul", null, arg);
	}
	
	@Command
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void doDelete(){
		final ListModelList<SecUser> listModelListUsers = (ListModelList) listBoxUserUserRole.getModel();
		
		if(listBoxUserUserRole.getSelectedIndex() == -1){
			Messagebox.show("There is no selected record?", "Confirm", Messagebox.OK, Messagebox.ERROR);
		}else{
			Messagebox.show("Do you really want to remove item?", "Confirm", Messagebox.OK | Messagebox.CANCEL, Messagebox.EXCLAMATION, new EventListener() {
			    public void onEvent(Event event) throws Exception {    	
			 		if (((Integer) event.getData()).intValue() == Messagebox.OK) {
			 			for(SecUser secUser : listModelListUsers){
			 				if(listModelListUsers.isSelected(secUser)){
			 					HashMap<String, Object> map = new HashMap<String, Object>();
			 					map.put("usrId", secUser);
			 					List<SecUserrole> secUserroles = securityService.getRequestMapBySecUserrole(map);
			 					
			 					for(SecUserrole secUserrole : secUserroles){
			 						securityService.delete(secUserrole);
			 					}
			 				}
			 			}
			 			
			 			BindUtils.postGlobalCommand(null, null, "refreshAfterClose", null);
			 		}else{
			 			return;
			 		}
			 	}
			});
		}
	}
	
	@Command
	public void doRefresh(){
		BindUtils.postGlobalCommand(null, null, "refreshAfterClose", null);
	}
	
	@Command
	public void doPrint(){
		
	}
	
	@Command
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void doDetail(){
		/*HashMap<K, V> K = Key, V = Value*/
		HashMap<String, Object> arg = new HashMap<String, Object>();
		arg.put("secUser", secUser);
		ListModelList<SecRole> listModelListSecRoles = (ListModelList) listBoxRoleUserRole.getModel(); 
		arg.put("secRoles", listModelListSecRoles);
		
		Executions.createComponents("/WEB-INF/pages/sec_user_role/secUserRoleDialog.zul", null, arg);
	}
	
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Methode from Dialog
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	@GlobalCommand
	@NotifyChange({"listBoxUserUserRole","secRoles"})
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void refreshAfterClose(){
		System.out.println("di Refresh dari Window!");
		listBoxUserUserRole.clearSelection();
		
		ListModelList<SecUser> listModelListSecUsers = (ListModelList) listBoxUserUserRole.getModel();
		listModelListSecUsers.clearSelection();
		
		if(secRoles!=null){
			secRoles.clear();
		}
	}

	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Getter Setter
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
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
}