package org.app.portofolio.webui.hr.transaction.employee.model;

import org.module.hr.model.dto.MaritalStatusType;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;

public class MaritalStatusListItemRenderer implements ComboitemRenderer<MaritalStatusType>{
	@Override
	public void render(Comboitem item, MaritalStatusType maritalStatusType, int index) throws Exception {

		item.setLabel(maritalStatusType.getStpTypname());

		item.setAttribute("data", maritalStatusType);
	}
}
