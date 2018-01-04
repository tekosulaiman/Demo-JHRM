package org.app.portofolio.webui.hr.transaction.employee.model;

import org.module.hr.model.MstSubUnit;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

public class SubUnitListItemRenderer implements ListitemRenderer<MstSubUnit>{

	@Override
	public void render(Listitem item, MstSubUnit data, int index) throws Exception {
		Listcell listcell = new Listcell();
        listcell.setLabel(data.getNameSubUnit());
        item.appendChild(listcell);
	}

}
