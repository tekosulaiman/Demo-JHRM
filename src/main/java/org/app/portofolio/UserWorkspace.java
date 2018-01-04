package org.app.portofolio;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Window;

/**
*
* @author tekosulaiman@yahoo.com
*/
public class UserWorkspace implements Serializable, DisposableBean{

	private static final long serialVersionUID = 1L;

	static private Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	public static UserWorkspace getInstance() {
		return (UserWorkspace) SpringUtil.getBean("userWorkspace", UserWorkspace.class);
	}

	private String userLanguage;
	private String browserType;
	private boolean treeMenu = true;
	private final int menuOffset = 102;

	private Set<String> grantedAuthoritySet = null;

	@SuppressWarnings("deprecation")
	public UserWorkspace() {

		Window.setDefaultActionOnShow("");
	}

	public void doLogout() {
		destroy();

		/* ++++++ Kills the Http session ++++++ */
		// HttpSession s = (HttpSession)
		// Sessions.getCurrent().getNativeSession();
		// s.invalidate();
		/* ++++++ Kills the zk session +++++ */
		// Sessions.getCurrent().invalidate();

		Executions.sendRedirect("/j_spring_security_logout");

	}

	@SuppressWarnings("unchecked")
	private Set<String> getGrantedAuthoritySet() {

		if (this.grantedAuthoritySet == null) {

			final Collection<GrantedAuthority> list = (Collection<GrantedAuthority>) getAuthentication().getAuthorities();
			this.grantedAuthoritySet = new HashSet<String>(list.size());

			for (final GrantedAuthority grantedAuthority : list) {
				this.grantedAuthoritySet.add(grantedAuthority.getAuthority());
			}
		}
		return this.grantedAuthoritySet;
	}

	public boolean isAllowed(String rightName) {
		return getGrantedAuthoritySet().contains(rightName);
	}

	public Properties getUserLanguageProperty() {
		return null;
	}

	public void destroy() {
		this.grantedAuthoritySet = null;
		
		SecurityContextHolder.clearContext();
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++ //
	// ++++++++++++++++ Setter/Getter ++++++++++++++++++ //
	// +++++++++++++++++++++++++++++++++++++++++++++++++ //
	public void setUserLanguage(String userLanguage) {
		this.userLanguage = userLanguage;
	}

	public String getUserLanguage() {
		return this.userLanguage;
	}

	public void setBrowserType(String browserType) {
		this.browserType = browserType;
	}

	public String getBrowserType() {
		return this.browserType;
	}

	public int getMenuOffset() {

		int result = 0;

		if (isTreeMenu())
			result = 0;
		else
			result = menuOffset;

		return result;
	}

	public void setTreeMenu(boolean treeMenu) {
		this.treeMenu = treeMenu;
	}

	public boolean isTreeMenu() {
		return treeMenu;
	}
}