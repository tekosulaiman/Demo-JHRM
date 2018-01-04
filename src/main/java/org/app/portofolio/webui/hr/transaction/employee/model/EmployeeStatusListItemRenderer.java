package org.app.portofolio.webui.hr.transaction.employee.model;

import org.module.hr.model.MstEmployementStatus;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

public class EmployeeStatusListItemRenderer implements ListitemRenderer<MstEmployementStatus>{

	@Override
	public void render(Listitem item, MstEmployementStatus data, int index) throws Exception {
		Listcell listcell = new Listcell();
        listcell.setLabel(data.getEmployementStatusName());
        item.appendChild(listcell);
	}

}
