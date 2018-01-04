package org.app.portofolio.webui.hr.transaction.employee.model;

import org.module.hr.model.MstJobCategory;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

public class JobCategoryListItemRenderer implements ListitemRenderer<MstJobCategory>{

	@Override
	public void render(Listitem item, MstJobCategory data, int index) throws Exception {
		Listcell listcell = new Listcell();
        listcell.setLabel(data.getJobCategoryName());
        item.appendChild(listcell);
	}
	
}
