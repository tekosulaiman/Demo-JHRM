package org.app.portofolio.common.menu.tree;

import java.io.Serializable;

import org.springframework.util.StringUtils;
import org.zkoss.zk.ui.ComponentNotFoundException;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;
import org.zkoss.zul.Treecell;

import org.app.portofolio.common.menu.util.ILabelElement;

/**
*
* @author tekosulaiman@yahoo.com
*/
@SuppressWarnings("rawtypes")
public class DefaultTreecell extends Treecell implements EventListener, Serializable, ILabelElement{

	private static final long serialVersionUID = 1L;
	private String zulNavigation;

	@Override
	public void onEvent(Event event) throws Exception {

		try {
			/* get an instance of the borderlayout defined in the zul-file */
			final Borderlayout borderlayout = (Borderlayout) Path.getComponent("/mainMenuWindow/borderlayoutMain");
			/* get an instance of the searched CENTER layout area */
			final Center center = borderlayout.getCenter();

			final Tabs tabs = (Tabs) center.getFellow("center").getFellow("tabs");

			// Check if the tab is already open, if not than create them
			Tab checkTab = null;
			try {
				checkTab = (Tab) tabs.getFellow("tab_" + this.getId());
				checkTab.setSelected(true);
			} catch (final ComponentNotFoundException ex) {
				// Ignore if can not get tab.
			}

			if (checkTab == null) {

				final Tab tab = new Tab();
				tab.setId("tab_" + this.getId());
				tab.setLabel(this.getLabel().trim());
				tab.setClosable(true);

				tab.setParent(tabs);

				final Tabpanels tabpanels = (Tabpanels) center.getFellow("center").getFellow("tabpanels");
				final Tabpanel tabpanel = new Tabpanel();
				tabpanel.setHeight("100%");
				tabpanel.setStyle("padding: 0px;");
				tabpanel.setParent(tabpanels);

				Executions.createComponents(getZulNavigation(), tabpanel, null);
				tab.setSelected(true);
			}
		} catch (final Exception e) {
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