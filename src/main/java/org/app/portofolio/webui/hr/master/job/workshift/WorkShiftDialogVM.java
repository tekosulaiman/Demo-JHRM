package org.app.portofolio.webui.hr.master.job.workshift;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;

public class WorkShiftDialogVM {
	
	@AfterCompose
	public void setupComponents(@ContextParam(ContextType.VIEW) Component component, 
		@ExecutionArgParam("object") Object object) {
		
		Selectors.wireComponents(component, this, false);

	}
	
	@Command
	public void doNew(){
		Executions.createComponents("/WEB-INF/pages/module_hr/master/job/workshift/workShiftDialog.zul", null, null);
	}
}