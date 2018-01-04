package org.app.portofolio.webui.hr.transaction.employee.model;

import org.module.hr.model.dto.RelationshipType;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;

public class RelationshipListItemRenderer implements ComboitemRenderer<RelationshipType>{
	
	@Override
	public void render(Comboitem item, RelationshipType relationshipType, int index) throws Exception {

		item.setLabel(relationshipType.getStpTypname());
		
		item.setAttribute("data", relationshipType);
	}
}
