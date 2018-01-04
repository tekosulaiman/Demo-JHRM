package org.app.portofolio.webui.security.right.model;

import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;

import org.module.sysadmin.model.dto.SecType;

/**
*
* @author tekosulaiman@yahoo.com
*/
public class SecTypeListModelItemRenderer implements ComboitemRenderer<SecType>{

	@Override
	public void render(Comboitem item, SecType secType, int index) throws Exception {

		item.setLabel(secType.getStpTypname());
		
		item.setAttribute("data", secType);
	}
}