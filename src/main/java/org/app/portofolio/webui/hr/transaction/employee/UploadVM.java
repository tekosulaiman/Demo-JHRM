package org.app.portofolio.webui.hr.transaction.employee;

import org.module.hr.model.TrsEmployee;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Fileupload;

public class UploadVM {
	
	@Wire("#upload")
	private Fileupload upFileupload;

	@AfterCompose
	public void setupComponents(@ContextParam(ContextType.VIEW) Component component,
			@ExecutionArgParam("object") Object object) {
			Selectors.wireComponents(component, this, false);
			
			Media media = upFileupload.get(null, null, true);
//			this.trsEmployee = new TrsEmployee();
		}

	public void setKucing() {
		Fileupload a = new Fileupload();
		Media media = a.get();
	}
	
}
