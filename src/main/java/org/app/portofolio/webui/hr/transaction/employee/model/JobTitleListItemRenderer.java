package org.app.portofolio.webui.hr.transaction.employee.model;

import org.module.hr.model.MstJobtitle;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

public class JobTitleListItemRenderer implements ListitemRenderer<MstJobtitle>{

	@Override
	public void render(Listitem item, MstJobtitle data, int index) throws Exception {
		Listcell listcell = new Listcell();
        listcell.setLabel(data.getJobName());
        item.appendChild(listcell);
	}
	
}
