package org.app.portofolio.common.menu.util;

import org.zkoss.zk.ui.Component;

/**
*
* @author tekosulaiman@yahoo.com
*/
public class MenuFactoryDto {
	
	public MenuFactoryDto(Component parent, ILabelElement node) {
		super();
		
		this.parent = parent;
		this.node = node;
	}

	public MenuFactoryDto(ILabelElement node) {
		this(node, node);
	}


	public Component getParent() {
		return this.parent;
	}


	public void setParent(Component parent) {
		this.parent = parent;
	}

	public ILabelElement getNode() {
		return this.node;
	}


	public void setNode(ILabelElement node) {
		this.node = node;
	}

	private Component parent;
	private ILabelElement node;
}