package org.app.portofolio.common.menu.dropdown;

import java.io.Serializable;

import org.springframework.util.StringUtils;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Menu;
import org.zkoss.zul.Messagebox;

import org.app.portofolio.common.menu.util.ILabelElement;

/**
*
* @author tekosulaiman@yahoo.com
*/
@SuppressWarnings("rawtypes")
public class DefaultDropDownMenu extends Menu implements Serializable, ILabelElement, EventListener{

	private static final long serialVersionUID = 1L;
	private String zulNavigation;

	@Override
	public void onEvent(Event event) throws Exception {
		try {
			Executions.createComponents(getZulNavigation(), null, null);

		} catch (Exception e) {
			System.out.println("Error Menu ->>>"+e.toString());
			Messagebox.show(e.toString());
		}
	}

	private String getZulNavigation() {
		return this.zulNavigation;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void setZulNavigation(String zulNavigation) {
		this.zulNavigation = zulNavigation;
		if (!StringUtils.isEmpty(zulNavigation)) {
			addEventListener("onClick", this);
		}
	}
}