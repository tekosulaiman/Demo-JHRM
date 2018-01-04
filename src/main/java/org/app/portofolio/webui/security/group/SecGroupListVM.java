package org.app.portofolio.webui.security.group;

import java.util.List;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

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

import org.module.sysadmin.model.SecGroup;
import org.module.sysadmin.service.SecurityService;
import org.app.portofolio.webui.security.group.model.SecGroupListModelItemRenderer;

/**
*
* @author tekosulaiman@yahoo.com
*/
public class SecGroupListVM {

	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Wire component
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	@Wire("#textboxFilter")
	private Textbox textboxFilter;
	
	@Wire("#listBoxGroup")
	private Listbox listBoxGroup;
	
	@Wire("#pagingGroup")
	private Paging pagingGroup;
	
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Service yang dibutuhkan sesuai bisnis proses
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	private SecGroup secGroup;
	private List<SecGroup> secGroups;
	private ListitemRenderer<SecGroup> listitemRenderer;
	
	@WireVariable
	private SecurityService securityService;
	
	public void doPrepareList(){
		listBoxGroup.setCheckmark(true);
		listBoxGroup.setMultiple(true);
		listBoxGroup.setRows(15);
		listBoxGroup.setStyle("border-style: none;");
		listBoxGroup.setMold("paging");
		
		listBoxGroup.setPaginal(pagingGroup);
	}
	
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Inisialize Methode MVVM yang pertama kali dijalankan
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	@AfterCompose
	public void setupComponents(@ContextParam(ContextType.VIEW) Component component,
		@ExecutionArgParam("object") Object object,
		@ExecutionArgParam("secGroup") SecGroup secGroup) {
		
		Selectors.wireComponents(component, this, false);
		
		secGroups = securityService.getAllGroups();
		listitemRenderer = new SecGroupListModelItemRenderer();

		listBoxGroup.setModel(new ListModelList<SecGroup>(secGroups));
		listBoxGroup.setItemRenderer(listitemRenderer);
		
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
			
			listBoxGroup.setModel(new ListModelList<SecGroup>(secGroups));
			
			doPrepareList();
		} else {
			try {
				for(SecGroup secGroup: cacheSecGroups) {
					if(secGroup.getGrpShortdescription().toLowerCase().indexOf(getName.toLowerCase()) >= 0) {
						secGroups.add(secGroup);
					}
					
					listBoxGroup.setModel(new ListModelList<SecGroup>(secGroups));
					
					doPrepareList();
				}
			}catch (Exception e) {
				
			}
		}
	}
	
	@Command
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void doNew(){
		ListModelList listModelList = (ListModelList) listBoxGroup.getModel();
		listModelList.add(0,  new SecGroup());
	}

	@Command
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void doDelete(){
		final ListModelList<SecGroup> listModelListSecGroups = (ListModelList) listBoxGroup.getModel();
		
		if(listBoxGroup.getSelectedIndex() == -1){
			Messagebox.show("There is no selected record?", "Confirm", Messagebox.OK, Messagebox.ERROR);
		}else{
			Messagebox.show("Do you really want to remove item?", "Confirm", Messagebox.OK | Messagebox.CANCEL, Messagebox.EXCLAMATION, new EventListener() {
			    public void onEvent(Event event) throws Exception {    	
			 		if (((Integer) event.getData()).intValue() == Messagebox.OK) {
			 			for(SecGroup secGroup : listModelListSecGroups){
			 				if(listModelListSecGroups.isSelected(secGroup)){
			 					securityService.delete(secGroup);
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
		JRBeanCollectionDataSource jRBeanCollectionDataSource = new JRBeanCollectionDataSource(securityService.getAllGroups());
        String source = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/WEB-INF/reports");
        source = source + "/SecGroupReport.jasper";
		
        JasperPrint jasperPrint = JasperFillManager.fillReport(source, null, jRBeanCollectionDataSource);
        JasperViewer.viewReport(jasperPrint, false);
	}

	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Methode from SecGroupListModelItemRenderer.java
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	@GlobalCommand
	@NotifyChange("secGroups")
	public void refreshAfterSaveOrUpdate(){
		secGroups = securityService.getAllGroups();
		
		System.out.println("Binding");
	}
	
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Getter Setter
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
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

	public ListitemRenderer<SecGroup> getListitemRenderer() {
		return listitemRenderer;
	}

	public void setListitemRenderer(ListitemRenderer<SecGroup> listitemRenderer) {
		this.listitemRenderer = listitemRenderer;
	}

	public SecurityService getSecurityService() {
		return securityService;
	}

	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}
}