package org.app.portofolio.common.menu.domain;

import java.lang.ref.SoftReference;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
*
* @author tekosulaiman@yahoo.com
*/
public class MetaMenuFactory {
	static private SoftReference<RootMenuDomain> referenceRootMenuDomain = new SoftReference<RootMenuDomain>(null);
	static String menuXMLRootPath = "/";

	static public RootMenuDomain getRootMenuDomain() {
		RootMenuDomain rootMenuDomain = referenceRootMenuDomain.get();
		if (rootMenuDomain == null) {
			try {
				Unmarshaller unmarshaller = JAXBContext.newInstance(RootMenuDomain.class).createUnmarshaller();
				rootMenuDomain = (RootMenuDomain) unmarshaller.unmarshal(MetaMenuFactory.class.getResource(menuXMLRootPath + "main-menu.xml"));
				referenceRootMenuDomain = new SoftReference<RootMenuDomain>(rootMenuDomain);


			} catch (JAXBException e) {
				throw new RuntimeException(e);
			}
		}
		return rootMenuDomain;
	}
}