package org.app.portofolio.webui;

import org.app.portofolio.common.menu.dropdown.ZkossDropDownMenuFactory;
import org.app.portofolio.common.menu.tree.ZkossTreeMenuFactory;
import org.module.hr.service.AdministrationService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.ComponentNotFoundException;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Label;
import org.zkoss.zul.Menubar;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Window;

/**
*
* @author tekosulaiman@yahoo.com
*/
public class HomeVM{
	
	@Wire("#labelUserName")
	private Label labelUserName;
	
	@Wire("#labelZkVersion")
	private Label labelZkVersion;
	
	@Wire("#userName")
	private Label userName;
	
	@Wire("#mainMenuWindow")
	private Window mainMenuWindow;
	
	@Wire("#treeMenu")
	private Tree treeMenu;
	
	@Wire("#menubarMenu")
	private Menubar menubarMenu;
	
	@Wire("#tabs")
	private Tabs tabs;
	
	@Wire("#tabpanels")
	private Tabpanels tabpanels;
	
	@Wire("#chartTest")
	private org.zkoss.zul.Chart chartTest;
	
	@WireVariable
	private AdministrationService administrationService;
	
	@AfterCompose
	public void initComponents(@ContextParam(ContextType.VIEW) Component component,
		@ExecutionArgParam("object") Object object,
		@ExecutionArgParam("event") Event event) throws Exception {
		
		Selectors.wireComponents(component, this, false);
		
		//createMenuTree();
		createMenuBar();
		
		/*as standard, call the dashboard page*/
		try {
			//showPage("/WEB-INF/pages/dashboard.zul", "menu.home.dashboard", Labels.getLabel("menu.home.dashboard"));
			showPage("", "", "");	
		}catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		/*Cast Object to Component*/
		object =  tabs;
		
		doGetZkVersion();
		addSessionAttribute();
	}

	/**
	 * 
	 */
	private void addSessionAttribute() {
		Sessions.getCurrent().setAttribute("setting", administrationService.getAllSettingsAsHash());
	}

	@Command
	public void doLogout(){
		Executions.sendRedirect("/j_spring_security_logout");
	}
	
	public void createMenuTree(){
		final Treechildren treechildren = new Treechildren();
		treeMenu.appendChild(treechildren);

		ZkossTreeMenuFactory.addMainMenu(treechildren);
	}
	
	public void createMenuBar(){
		ZkossDropDownMenuFactory.addDropDownMenu(menubarMenu);
	}
	
	private void showPage(String zulFilePathName, String tabID, String tabLabel) throws InterruptedException {

		try {
			// params
			final int workWithTabs = 1;

			if (workWithTabs == 1) {

				Tab checkTab = null;
				try {
					// checkTab = (Tab) tabs.getFellow(tabName);
					checkTab = (Tab) tabs.getFellow("tab_" + tabID.trim());
					checkTab.setSelected(true);
				} catch (final ComponentNotFoundException ex) {
					// Ignore if can not get tab.
				}

				if (checkTab == null) {

					final Tab tab = new Tab();
					tab.setId("tab_" + tabID.trim());

					if (tabLabel != null) {
						tab.setLabel(tabLabel.trim());
					} /*else {
						tab.setLabel(tabLabel.trim());
					}*/
					
					tab.setClosable(true);
					tab.setParent(tabs);

					final Tabpanel tabpanel = new Tabpanel();
					tabpanel.setHeight("100%");
					tabpanel.setStyle("padding: 0px;");
					tabpanel.setParent(tabpanels);

					Executions.createComponents(zulFilePathName, tabpanel, null);
					tab.setSelected(true);
				}
			} 
		} catch (final Exception e) {
			/*Messagebox.show(e.toString());*/
		}
	}
	
	private String doGetZkVersion() {
		final String version = Executions.getCurrent().getDesktop().getWebApp().getVersion();
		final String build = Executions.getCurrent().getDesktop().getWebApp().getBuild();
		
		/*Untuk Mendapatkan UserName*/
		final String userName = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
		labelUserName.setValue(userName);
		
		String ConcatVersion = "ZK " + version + " EE" + " / build : " + build;
		labelZkVersion.setValue(ConcatVersion);
		
		return "ZK " + version + " EE" + " / build : " + build;
	}
	
	
}