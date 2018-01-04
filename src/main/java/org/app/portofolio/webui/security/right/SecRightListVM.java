package org.app.portofolio.webui.security.right;

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

import org.module.sysadmin.model.SecRight;
import org.module.sysadmin.service.SecurityService;
import org.app.portofolio.webui.security.right.model.SecRightListModelItemRenderer;

/**
*
* @author tekosulaiman@yahoo.com
*/
public class SecRightListVM{
	
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Wire component
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	@Wire("#textboxFilter")
	private Textbox textboxFilter;
	
	@Wire("#listBoxSecRight")
	private Listbox listBoxSecRight;
	
	@Wire("#pagingRight")
	private Paging pagingRight;
	
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Service yang dibutuhkan sesuai bisnis proses
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	private SecRight secRight;
	private List<SecRight> secRights;
	private ListitemRenderer<SecRight> listitemRenderer;
	
	@WireVariable
	private SecurityService securityService;
	
	public void doPrepareList(){
		listBoxSecRight.setCheckmark(true);
		listBoxSecRight.setMultiple(true);
		listBoxSecRight.setRows(15);
		listBoxSecRight.setStyle("border-style: none;");
		listBoxSecRight.setMold("paging");
		
		listBoxSecRight.setPaginal(pagingRight);
	}

	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Inisialize Methode MVVM yang pertama kali dijalankan
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	@AfterCompose
	public void setupComponents(@ContextParam(ContextType.VIEW) Component component,
		@ExecutionArgParam("object") Object object,
		@ExecutionArgParam("secRight") SecRight secRight) {
		
		Selectors.wireComponents(component, this, false);
		
		secRights = securityService.getAllRights();
		listitemRenderer = new SecRightListModelItemRenderer();

		listBoxSecRight.setModel(new ListModelList<SecRight>(secRights));
		listBoxSecRight.setItemRenderer(listitemRenderer);
		
		doPrepareList();
	}
	
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Function CRUD Event
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	@Command
	@NotifyChange("secRights")
	public void doFilter(){
		secRights.clear();
        
        List<SecRight> cacheSecRights = securityService.getAllRights();
		String getName = textboxFilter.getValue();
		
		if(getName == null || "".equals(getName)) {
			secRights.addAll(cacheSecRights);
			
			listBoxSecRight.setModel(new ListModelList<SecRight>(secRights));
			
			doPrepareList();
		} else {
			try {
				for(SecRight secRight: cacheSecRights) {
					if(secRight.getRigName().toLowerCase().indexOf(getName.toLowerCase()) >= 0) {
						secRights.add(secRight);
					}
					
					listBoxSecRight.setModel(new ListModelList<SecRight>(secRights));
					
					doPrepareList();
				}
			}catch (Exception e) {
				
			}
		}
	}
	
	@Command
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void doNew(){
		ListModelList listModelList = (ListModelList) listBoxSecRight.getModel();
		listModelList.add(0,  new SecRight());
	}
	
	@Command
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void doDelete(){
		final ListModelList<SecRight> listModelListSecRights = (ListModelList) listBoxSecRight.getModel();
		
		if(listBoxSecRight.getSelectedIndex() == -1){
			Messagebox.show("There is no selected record?", "Confirm", Messagebox.OK, Messagebox.ERROR);
		}else{
			Messagebox.show("Do you really want to remove item?", "Confirm", Messagebox.OK | Messagebox.CANCEL, Messagebox.EXCLAMATION, new EventListener() {
			    public void onEvent(Event event) throws Exception {    	
			 		if (((Integer) event.getData()).intValue() == Messagebox.OK) {
			 			for(SecRight secRight : listModelListSecRights){
			 				if(listModelListSecRights.isSelected(secRight)){
			 					securityService.delete(secRight);
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
		JRBeanCollectionDataSource jRBeanCollectionDataSource = new JRBeanCollectionDataSource(securityService.getAllRights());
        String source = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/WEB-INF/reports");
        source = source + "/SecRightReport.jasper";
		
        JasperPrint jasperPrint = JasperFillManager.fillReport(source, null, jRBeanCollectionDataSource);
        JasperViewer.viewReport(jasperPrint, false);
	}

	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Methode from SecGroupListModelItemRenderer.java
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	@GlobalCommand
	@NotifyChange("secRights")
	private void refreshAfterSaveOrUpdate(){
		secRights = securityService.getAllRights();
		
		System.out.println("Binding");
	}
	
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Getter Setter
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
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

	public ListitemRenderer<SecRight> getListitemRenderer() {
		return listitemRenderer;
	}

	public void setListitemRenderer(ListitemRenderer<SecRight> listitemRenderer) {
		this.listitemRenderer = listitemRenderer;
	}

	public SecurityService getSecurityService() {
		return securityService;
	}

	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}
}