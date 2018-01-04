package org.app.portofolio.common.menu.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

/**
*
* @author tekosulaiman@yahoo.com
*/
@XmlRootElement(name = "XMLRootMenu")
public class RootMenuDomain {
	private List<IMenuDomain> items;

	@XmlElements( { @XmlElement(name = "menu", type = MenuDomain.class), @XmlElement(name = "menuItem", type = MenuItemDomain.class) })
	public List<IMenuDomain> getItems() {
		return this.items;
	}

	public void setItems(List<IMenuDomain> items) {
		this.items = items;
	}
}