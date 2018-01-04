package org.app.portofolio.webui.hr.transaction.employee.model;

import org.module.hr.model.MstNationality;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

public class DummyNationalityItemRender implements ListitemRenderer<MstNationality>{

	@Override
	public void render(Listitem item, MstNationality data, int index) throws Exception {
		Listcell listcell = new Listcell();
        listcell.setLabel(data.getNameNationality());
        item.appendChild(listcell);
	}

	
}
