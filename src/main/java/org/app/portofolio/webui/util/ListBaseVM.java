package org.app.portofolio.webui.util;

import org.zkoss.zul.Listbox;

/**
*
* @author tekosulaiman@yahoo.com
*/
public abstract class ListBaseVM extends Listbox {
	
	private static final long serialVersionUID = 1L;

	public void doOnCreateCommonListbox(Listbox listbox){		
		listbox.setCheckmark(true);
		listbox.setRows(10);
		listbox.setMultiple(true);
		listbox.setMold("paging");
		listbox.setPagingPosition("bottom");
		listbox.setVflex(true);
		listbox.setPageSize(10);
		/*listbox.setHflex("1");
		listbox.setAutopaging(true);
		listbox.setHeight("500px");
		listbox.setFixedLayout(true);*/
	}
}