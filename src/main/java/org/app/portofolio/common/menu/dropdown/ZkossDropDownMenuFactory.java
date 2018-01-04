package org.app.portofolio.common.menu.dropdown;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Menupopup;

import org.app.portofolio.common.menu.domain.IMenuDomain;
import org.app.portofolio.common.menu.util.ILabelElement;
import org.app.portofolio.common.menu.util.MenuFactoryDto;
import org.app.portofolio.common.menu.util.ZkossMenuFactory;

/**
*
* @author tekosulaiman@yahoo.com
*/
public class ZkossDropDownMenuFactory extends ZkossMenuFactory{

	public static void addDropDownMenu(Component component) {
		new ZkossDropDownMenuFactory(component);
	}

	private ZkossDropDownMenuFactory(Component component) {
		super(component);
	}

	@Override
	protected MenuFactoryDto createMenuComponent(Component parent, boolean open) {
		final DefaultDropDownMenu menu = new DefaultDropDownMenu();
		parent.appendChild(menu);

		final Menupopup menupopup = new Menupopup();
		menu.appendChild(menupopup);

		return new MenuFactoryDto(menupopup, menu);
	}

	@Override
	protected ILabelElement createItemComponent(Component parent) {
		final DefaultDropDownMenuItem item = new DefaultDropDownMenuItem();
		parent.appendChild(item);
		return item;
	}

	@Override
	protected void setAttributes(IMenuDomain treecellValue, ILabelElement defaultTreecell) {
		super.setAttributes(treecellValue, defaultTreecell);
		defaultTreecell.setImage(treecellValue.getIconName());
	}
}