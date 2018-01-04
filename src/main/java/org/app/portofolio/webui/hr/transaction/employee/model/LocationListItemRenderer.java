package org.app.portofolio.webui.hr.transaction.employee.model;

import org.module.hr.model.MstLocation;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

public class LocationListItemRenderer implements ListitemRenderer<MstLocation>{

	@Override
	public void render(Listitem item, MstLocation data, int index) throws Exception {
		Listcell listcell = new Listcell();
        listcell.setLabel(data.getName());
        item.appendChild(listcell);
	}

}
