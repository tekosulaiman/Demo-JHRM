package org.app.portofolio.webui.security.role;

import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

import org.app.portofolio.webui.security.role.model.SecRoleListModelItemRenderer;
import org.module.sysadmin.model.SecRole;
import org.module.sysadmin.service.SecurityService;
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
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;

/**
*
* @author tekosulaiman@yahoo.com
*/
public class SecRoleListVM {

	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Wire component
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	@Wire("#textboxFilter")
	private Textbox textboxFilter;
	
	@Wire("#listBoxRole")
	private Listbox listBoxRole;
	
	@Wire("#pagingRole")
	private Paging pagingRole;
	
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Service yang dibutuhkan sesuai bisnis proses
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	private SecRole secRole;
	private List<SecRole>secRoles;
	private ListitemRenderer<SecRole> listitemRenderer;
	
	@WireVariable
	private SecurityService securityService;
	
	public void doPrepareList(){
		listBoxRole.setCheckmark(true);
		listBoxRole.setMultiple(true);
		listBoxRole.setRows(15);
		listBoxRole.setStyle("border-style: none;");
		listBoxRole.setMold("paging");
		
		listBoxRole.setPaginal(pagingRole);
	}
	
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Inisialize Methode MVVM yang pertama kali dijalankan
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	@AfterCompose
	public void setupComponents(@ContextParam(ContextType.VIEW) Component component,
		@ExecutionArgParam("object") Object object,
		@ExecutionArgParam("secRole") SecRole secRole) {
		
		Selectors.wireComponents(component, this, false);
		
		secRoles = securityService.getAllRoles();
		listitemRenderer = new SecRoleListModelItemRenderer();

		listBoxRole.setModel(new ListModelList<SecRole>(secRoles));
		listBoxRole.setItemRenderer(listitemRenderer);
		
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
			
			listBoxRole.setModel(new ListModelList<SecRole>(secRoles));
			
			doPrepareList();
		} else {
			try {
				for(SecRole secRole: cacheSecRoles) {
					if(secRole.getRolShortdescription().toLowerCase().indexOf(getName.toLowerCase()) >= 0) {
						secRoles.add(secRole);
					}
					
					listBoxRole.setModel(new ListModelList<SecRole>(secRoles));
					
					doPrepareList();
				}
			}catch (Exception e) {
				
			}
		}
	}
	
	@Command
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void doNew(){
		ListModelList listModelList = (ListModelList) listBoxRole.getModel();
		listModelList.add(0,  new SecRole());
	}

	
	@Command
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void doDelete(){
		final ListModelList<SecRole> listModelListSecRoles = (ListModelList) listBoxRole.getModel();
		
		if(listBoxRole.getSelectedIndex() == -1){
			Messagebox.show("There is no selected record?", "Confirm", Messagebox.OK, Messagebox.ERROR);
		}else{
			Messagebox.show("Do you really want to remove item?", "Confirm", Messagebox.OK | Messagebox.CANCEL, Messagebox.EXCLAMATION, new EventListener() {
			    public void onEvent(Event event) throws Exception {    	
			 		if (((Integer) event.getData()).intValue() == Messagebox.OK) {
			 			for(SecRole secRole : listModelListSecRoles){
			 				if(listModelListSecRoles.isSelected(secRole)){
			 					securityService.delete(secRole);
			 				}
			 			}
			 			
			 			BindUtils.postGlobalCommand(null, null, "refreshAfterSaveOrUpdate", null);
			 		}else{
			 			return;
			 		}
			 	}
			});
		}
	}
	
	@Command
	public void doRefresh(){
		refreshAfterSaveOrUpdate();
	}

	@Command
	public void doPrint() throws JRException{
		/*Test Viewer*/
		HashMap<String, Object> requestMap = new HashMap<String, Object>();
        requestMap.put("rolShortdescription", textboxFilter.getText().trim());
		
		//JRBeanCollectionDataSource jRBeanCollectionDataSource = new JRBeanCollectionDataSource(securityService.getAllRoles());
        JRBeanCollectionDataSource jRBeanCollectionDataSource = new JRBeanCollectionDataSource(securityService.getRequestMapBySecRole(requestMap));
        String source = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/WEB-INF/reports");
        source = source + "/SecRoleReport.jasper";
		
        JasperPrint jasperPrint = JasperFillManager.fillReport(source, requestMap, jRBeanCollectionDataSource);
        JasperViewer.viewReport(jasperPrint, false);
	}
	
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Methode from SecGroupListModelItemRenderer.java
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	@GlobalCommand
	@NotifyChange("secRoles")
	public void refreshAfterSaveOrUpdate(){
		secRoles = securityService.getAllRoles();
		
		System.out.println("Binding");
	}
	
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Getter Setter
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
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

	public SecurityService getSecurityService() {
		return securityService;
	}

	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}

	public ListitemRenderer<SecRole> getListitemRenderer() {
		return listitemRenderer;
	}

	public void setListitemRenderer(ListitemRenderer<SecRole> listitemRenderer) {
		this.listitemRenderer = listitemRenderer;
	}
}