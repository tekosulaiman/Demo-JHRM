package org.app.portofolio.policy.model;

import org.module.sysadmin.model.SecUser;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
*
* @author tekosulaiman@yahoo.com
*/
@SuppressWarnings("serial")
public class UserImpl extends User {
	
	final private long userId;

	private SecUser secUser;

	public UserImpl(SecUser user, Collection<GrantedAuthority> grantedAuthorities) throws IllegalArgumentException {

		super(user.getUsrLoginname(), user.getUsrPassword(), user.getUsrEnabled(), user.getUsrAccountnonexpired(), user.getUsrCredentialsnonexpired(), user.getUsrAccountnonlocked(), grantedAuthorities);
		this.userId = user.getUsrId();
		this.secUser = user;
	}

	public SecUser getSecUser() {
		return secUser;
	}

	public void setSecUser(SecUser secUser) {
		this.secUser = secUser;
	}

	public long getUserId() {
		return userId;
	}
}