package org.app.portofolio.webui.security.groupright;

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
import org.module.sysadmin.model.SecGroupright;
import org.module.sysadmin.model.SecRight;
import org.module.sysadmin.service.SecurityService;

/**
*
* @author tekosulaiman@yahoo.com
*/
public class SecGroupRightListVM {
	
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Wire component
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	@Wire("#windowGroupRightList")
	private Window windowGroupRightList;

	@Wire("#textboxFilter")
	private Textbox textboxFilter;
	
	@Wire("#listBoxGroupGroupRight")
	private Listbox listBoxGroupGroupRight;
	
	@Wire("#listBoxRightGroupRight")
	private Listbox listBoxRightGroupRight;

	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Service yang dibutuhkan sesuai bisnis proses
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	@WireVariable
	private SecurityService securityService;
	
	private SecGroup secGroup;
	private List<SecGroup> secGroups;
	
	private SecRight secRight;
	private List<SecRight> secRights;
	
	private SecGroupright secGroupright;
	private List<SecGroupright> secGrouprights;
	
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Function Custom sesuai kebutuhan
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	public void doPrepareList(){
		listBoxGroupGroupRight.setCheckmark(true);
		listBoxGroupGroupRight.setMultiple(true);
		listBoxGroupGroupRight.setRows(5);
		listBoxGroupGroupRight.setStyle("border-style: none;");
		listBoxGroupGroupRight.setMold("paging");
		listBoxGroupGroupRight.setAutopaging(true);
		
		listBoxRightGroupRight.setRows(5);
		listBoxRightGroupRight.setStyle("border-style: none;");
		listBoxRightGroupRight.setMold("paging");
		listBoxRightGroupRight.setAutopaging(true);
	}
	
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Inisialize Methode MVVM yang pertama kali dijalankan
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	@AfterCompose
	public void setupComponents(@ContextParam(ContextType.VIEW) Component component,
		@ExecutionArgParam("object") Object object,
		@ExecutionArgParam("secGroupright") SecGroupright secGroupright) {
		
		Selectors.wireComponents(component, this, false);

		secGroups = securityService.getAllGroups();
		doPrepareList();
	}
	
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Function CRUD Event
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	@Command
	@NotifyChange("secGroups")
	public void doFilter(){
		secGroups.clear();
        
        List<SecGroup> cacheSecGroups = securityService.getAllGroups();
		String getName = textboxFilter.getValue();
		
		if(getName == null || "".equals(getName)) {
			secGroups.addAll(cacheSecGroups);
			
			listBoxGroupGroupRight.setModel(new ListModelList<SecGroup>(secGroups));
			
			doPrepareList();
		} else {
			try {
				for(SecGroup secGroup: cacheSecGroups) {
					if(secGroup.getGrpShortdescription().toLowerCase().indexOf(getName.toLowerCase()) >= 0) {
						secGroups.add(secGroup);
					}
					
					listBoxGroupGroupRight.setModel(new ListModelList<SecGroup>(secGroups));
					
					doPrepareList();
				}
			}catch (Exception e) {
				
			}
		}
	}
	
	@Command
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@NotifyChange("secRights")
	public void doSelect(){		
		ListModelList<SecGroup> listModelListSecGroups = (ListModelList) listBoxGroupGroupRight.getModel();
		Set<SecGroup> setGroups = listModelListSecGroups.getSelection();

		if(secGroup == null){
			secRights.clear();
		}else{
			SecGroup secGroup = setGroups.iterator().next();
			secRights = securityService.getRightByGroups(secGroup);
		}
	}
	
	@Command
	public void doNew(){
		/*HashMap<K, V> K = Key, V = Value*/
		HashMap<String, Object> arg = new HashMap<String, Object>();
		arg.put("object", "windowSecGroupRight");
		Executions.createComponents("/WEB-INF/pages/sec_group_right/secGroupRightDialog.zul", null, arg);
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
		arg.put("secGroup", secGroup);
		ListModelList<SecRight> listModelListSecRights = (ListModelList) listBoxRightGroupRight.getModel(); 
		arg.put("secRights", listModelListSecRights);
		
		Executions.createComponents("/WEB-INF/pages/sec_group_right/secGroupRightDialog.zul", null, arg);
	}
	
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Methode from Dialog
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	@GlobalCommand
	@NotifyChange({"listBoxGroupGroupRight","secRights"})
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void refreshAfterClose(){
		System.out.println("di Refresh dari Window!");
		listBoxGroupGroupRight.clearSelection();
		
		ListModelList<SecGroup> listModelListSecGroups = (ListModelList) listBoxGroupGroupRight.getModel();
		listModelListSecGroups.clearSelection();
		
		if(secRights!=null){
			secRights.clear();
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

	public SecGroupright getSecGroupright() {
		return secGroupright;
	}

	public void setSecGroupright(SecGroupright secGroupright) {
		this.secGroupright = secGroupright;
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

	public List<SecGroupright> getSecGrouprights() {
		return secGrouprights;
	}

	public void setSecGrouprights(List<SecGroupright> secGrouprights) {
		this.secGrouprights = secGrouprights;
	}
}