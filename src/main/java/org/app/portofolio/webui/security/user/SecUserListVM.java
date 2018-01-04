package org.app.portofolio.webui.security.user;

import java.util.HashMap;
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
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.event.PagingEvent;
import org.app.portofolio.UserWorkspace;
import org.module.sysadmin.model.SecUser;
import org.module.sysadmin.service.UserService;

/**
*
* @author tekosulaiman@yahoo.com
*/
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SecUserListVM{
	
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Wire component
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	@Wire("#windowUserList")
	private Window windowUserList;
	
	@Wire("#textboxFilter")
	private Textbox textboxFilter;

	@Wire("#listBoxUser")
	private Listbox listBoxUser;
	
	@Wire("#pagingUser")
	private Paging pagingUser;
	
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Service yang dibutuhkan sesuai bisnis proses
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	private SecUser secUser;
	private List<SecUser> secUsers;
	@WireVariable
	private UserService userService;
	
	private HashMap<String, Object> hashMapSecUser;
	
	private int startPageNumber = 0;
	private int pageSize = 10;

	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Function Custom sesuai kebutuhan
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	private void doCheckRights() {

		final UserWorkspace workspace = new UserWorkspace();

		textboxFilter.setVisible(workspace.isAllowed("xxx"));
	}
	
	public void doPrepareList(){
		listBoxUser.setCheckmark(true);
		listBoxUser.setMultiple(true);
		listBoxUser.setStyle("border-style: none;");
		listBoxUser.setMold("paging");
		
		int count = userService.getCountAllSecUser();
		
		pagingUser.setTotalSize(count);
		pagingUser.setDetailed(true);
		pagingUser.setPageSize(pageSize);
	}
	
	private void refreshPageList(int refreshActivePage) {
		if (refreshActivePage == 0) {
			pagingUser.setActivePage(0);
		}
		
		refreshActivePage += 1;
		
		hashMapSecUser = new HashMap<String, Object>();
		hashMapSecUser.put("firstResult", refreshActivePage);
		hashMapSecUser.put("maxResults", pagingUser.getPageSize());
		
		secUsers = userService.getAllByRequestMapUsers(hashMapSecUser);
	}
	
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Inisialize Methode MVVM yang pertama kali dijalankan
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	@AfterCompose
	public void setupComponents(@ContextParam(ContextType.VIEW) Component component,
		@ExecutionArgParam("object") Object object,
		@ExecutionArgParam("secUser") SecUser secUser) {
		
		Selectors.wireComponents(component, this, false);

		doCheckRights();
		doPrepareList();
		refreshPageList(startPageNumber);
		
		//secUsers = userService.getAllUsers();
	}
	
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Function CRUD Event
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	@Command
	@NotifyChange("secUsers")
	public void onPaging(@ContextParam(ContextType.TRIGGER_EVENT) PagingEvent pagingEvent){
		startPageNumber = pagingEvent.getActivePage() * pageSize;
		refreshPageList(startPageNumber);
	}
	
	@Command
	@NotifyChange("secUsers")
	public void doFilter(){
		secUsers.clear();
        
        List<SecUser> cacheSecUsers = userService.getAllUsers();
		String getName = textboxFilter.getValue();
		
		if(getName == null || "".equals(getName)) {
			secUsers.addAll(cacheSecUsers);
			
			listBoxUser.setModel(new ListModelList<SecUser>(secUsers));
			
			doPrepareList();
		} else {
			try {
				for(SecUser secUser: cacheSecUsers) {
					if(secUser.getUsrLoginname().toLowerCase().indexOf(getName.toLowerCase()) >= 0) {
						secUsers.add(secUser);
					}
					
					listBoxUser.setModel(new ListModelList<SecUser>(secUsers));
					
					doPrepareList();
				}
			}catch (Exception e) {
				
			}
		}
	}
	
	@Command
	public void doNew(){
		Executions.createComponents("/WEB-INF/pages/sec_user/secUserDialog.zul", null, null);
	}
	
	@Command
	public void doDelete(){
		final ListModelList<SecUser> listModelListSecUsers = (ListModelList) listBoxUser.getModel();
		
		if(listBoxUser.getSelectedIndex() == -1){
			Messagebox.show("There is no selected record?", "Confirm", Messagebox.OK, Messagebox.ERROR);
		}else{
			Messagebox.show("Do you really want to remove item?", "Confirm", Messagebox.OK | Messagebox.CANCEL, Messagebox.EXCLAMATION, new EventListener() {
			    public void onEvent(Event event) throws Exception {    	
			 		if (((Integer) event.getData()).intValue() == Messagebox.OK) {
			 			for(SecUser secUser : listModelListSecUsers){
			 				if(listModelListSecUsers.isSelected(secUser)){
			 					userService.delete(secUser);
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
		/*JRBeanCollectionDataSource jRBeanCollectionDataSource = new JRBeanCollectionDataSource(userService.getAllUsers());

        Window win = (Window) Executions.createComponents("/WEB-INF/pages/sec_user/secUserReport.zul", null, null);

		Jasperreport report = (Jasperreport) win.getFellow("report");
	    report.setSrc("/WEB-INF/reports/SecUserReport.jasper");
		report.setDatasource(jRBeanCollectionDataSource);*/
		
		/*Test Viewer*/
		JRBeanCollectionDataSource jRBeanCollectionDataSource = new JRBeanCollectionDataSource(userService.getAllUsers());
        String source = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/WEB-INF/reports");
        source = source + "/SecUserReport.jasper";
		
        JasperPrint jasperPrint = JasperFillManager.fillReport(source, null, jRBeanCollectionDataSource);
        JasperViewer.viewReport(jasperPrint, false);
	}
	
	@Command
	public void doDetail(){
		/*HashMap<K, V> K = Key, V = Value*/
		HashMap<String, Object> arg = new HashMap<String, Object>();
		arg.put("secUser", secUser);
		Executions.createComponents("/WEB-INF/pages/sec_user/secUserDialog.zul", null, arg);
	}
	
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Methode from Dialog
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	@GlobalCommand
	@NotifyChange("listBoxUser")
	public void refreshAfterClose(){
		System.out.println("di Refresh dari Window!");
		listBoxUser.clearSelection();
		
		ListModelList<SecUser> listModelListSecUsers = (ListModelList) listBoxUser.getModel();
		listModelListSecUsers.clearSelection();
	}
	
	@GlobalCommand
	@NotifyChange("secUsers")
	public void refreshAfterSaveOrUpdate(){
		secUsers = userService.getAllUsers();
	}
	
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Getter Setter
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
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

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}