package org.app.portofolio.webui.security.rolegroup;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.zkoss.bind.annotation.AfterCompose;
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
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import org.module.sysadmin.model.SecGroup;
import org.module.sysadmin.model.SecRole;
import org.module.sysadmin.model.SecRolegroup;
import org.module.sysadmin.service.SecurityService;

/**
*
* @author tekosulaiman@yahoo.com
*/
public class SecRoleGroupListVM {
	
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Wire component
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	@Wire("#windowRoleGroupList")
	private Window windowRoleGroupList;

	@Wire("#textboxFilter")
	private Textbox textboxFilter;
	
	@Wire("#listBoxRoleRoleGroup")
	private Listbox listBoxRoleRoleGroup;
	
	@Wire("#listBoxGroupRoleGroup")
	private Listbox listBoxGroupRoleGroup;

	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Service yang dibutuhkan sesuai bisnis proses
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	@WireVariable
	private SecurityService securityService;
	
	private SecRole secRole;
	private List<SecRole> secRoles;
	
	private SecGroup secGroup;
	private List<SecGroup> secGroups;

	private SecRolegroup secRolegroup;
	private List<SecRolegroup> secRolegroups;
	
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Function Custom sesuai kebutuhan
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	public void doPrepareList(){
		listBoxRoleRoleGroup.setCheckmark(true);
		listBoxRoleRoleGroup.setMultiple(true);
		listBoxRoleRoleGroup.setRows(5);
		listBoxRoleRoleGroup.setStyle("border-style: none;");
		listBoxRoleRoleGroup.setMold("paging");
		listBoxRoleRoleGroup.setAutopaging(true);
		
		listBoxGroupRoleGroup.setRows(5);
		listBoxGroupRoleGroup.setStyle("border-style: none;");
		listBoxGroupRoleGroup.setMold("paging");
		listBoxGroupRoleGroup.setAutopaging(true);
	}
	
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Inisialize Methode MVVM yang pertama kali dijalankan
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	@AfterCompose
	public void setupComponents(@ContextParam(ContextType.VIEW) Component component,
		@ExecutionArgParam("object") Object object,
		@ExecutionArgParam("secRolegroup") SecRolegroup secRolegroup) {
		
		Selectors.wireComponents(component, this, false);

		secRoles = securityService.getAllRoles();
		doPrepareList();
	}
	
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Function CRUD Event
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	@Command
	@NotifyChange("secRoles")
	public void doFilter(){
		secRoles.clear();
        
        List<SecRole> cacheSecRoles = securityService.getAllRoles();
		String getName = textboxFilter.getValue();
		
		if(getName == null || "".equals(getName)) {
			secRoles.addAll(cacheSecRoles);
			
			listBoxRoleRoleGroup.setModel(new ListModelList<SecRole>(secRoles));
			
			doPrepareList();
		} else {
			try {
				for(SecRole secRole: cacheSecRoles) {
					if(secRole.getRolShortdescription().toLowerCase().indexOf(getName.toLowerCase()) >= 0) {
						secRoles.add(secRole);
					}
					
					listBoxRoleRoleGroup.setModel(new ListModelList<SecRole>(secRoles));
					
					doPrepareList();
				}
			}catch (Exception e) {
				
			}
		}
	}
	
	@Command
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@NotifyChange("secGroups")
	public void doSelect(){		
		ListModelList<SecRole> listModelListSecRoles = (ListModelList) listBoxRoleRoleGroup.getModel();
		Set<SecRole> setSecRoles = listModelListSecRoles.getSelection();

		if(secRole == null){
			secGroups.clear();
		}else{
			SecRole secRole = setSecRoles.iterator().next();
			secGroups = securityService.getGroupByRoles(secRole);
		}
	}
	
	@Command
	public void doNew(){
		/*HashMap<K, V> K = Key, V = Value*/
		HashMap<String, Object> arg = new HashMap<String, Object>();
		arg.put("object", "windowSecGroupRight");
		Executions.createComponents("/WEB-INF/pages/sec_role_group/secRoleGroupDialog.zul", null, arg);
	}
	
	@Command
	public void doDelete(){
		
	}
	
	@Command
	public void doRefresh(){
		
	}
	
	@Command
	public void doPrint(){
		
	}
	
	@Command
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void doDetail(){
		/*HashMap<K, V> K = Key, V = Value*/
		HashMap<String, Object> arg = new HashMap<String, Object>();
		arg.put("secRole", secRole);
		ListModelList<SecGroup> listModelListSecGroups = (ListModelList) listBoxGroupRoleGroup.getModel(); 
		arg.put("secGroups", listModelListSecGroups);
		
		Executions.createComponents("/WEB-INF/pages/sec_role_group/secRoleGroupDialog.zul", null, arg);
	}
	
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Methode from Dialog
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	@GlobalCommand
	@NotifyChange({"listBoxRoleRoleGroup","secGroups"})
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void refreshAfterClose(){
		System.out.println("di Refresh dari Window!");
		listBoxRoleRoleGroup.clearSelection();
		
		ListModelList<SecRole> listModelListSecRoles = (ListModelList) listBoxRoleRoleGroup.getModel();
		listModelListSecRoles.clearSelection();
		
		if(secGroups!=null){
			secGroups.clear();
		}
	}
	
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Getter Setter
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	public SecurityService getSecurityService() {
		return securityService;
	}

	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
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

	public SecRolegroup getSecRolegroup() {
		return secRolegroup;
	}

	public void setSecRolegroup(SecRolegroup secRolegroup) {
		this.secRolegroup = secRolegroup;
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

	public List<SecRolegroup> getSecRolegroups() {
		return secRolegroups;
	}

	public void setSecRolegroups(List<SecRolegroup> secRolegroups) {
		this.secRolegroups = secRolegroups;
	}
}