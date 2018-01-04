package org.app.portofolio.common.menu.domain;

/**
*
* @author tekosulaiman@yahoo.com
*/
public interface IMenuDomain {
	String getRightName();
	String getId();
	String getLabel();
	Boolean isWithOnClickAction();
	String getZulNavigation();
	String getIconName();
}