package org.app.portofolio.policy.model;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.module.sysadmin.model.SecRight;
import org.module.sysadmin.model.SecUser;
import org.module.sysadmin.service.UserService;

/**
*
* @author tekosulaiman@yahoo.com
*/
public class PolicyManager implements UserDetailsService{

	private transient UserService userService;

	@Override
	public UserDetails loadUserByUsername(String userId) {

		SecUser user = null;
		Collection<GrantedAuthority> grantedAuthorities = null;

		try {
			user = getUserByLoginname(userId);

			if (user == null) {
				throw new UsernameNotFoundException("Invalid User");
			}

			grantedAuthorities = getGrantedAuthority(user);

		} catch (final NumberFormatException e) {
			throw new DataRetrievalFailureException("Cannot loadUserByUsername userId:" + userId + " Exception:" + e.getMessage(), e);
		}
		
		final UserDetails userDetails = new UserImpl(user, grantedAuthorities);

		return userDetails;
	}

	public SecUser getUserByLoginname(final String userName) {
		return getUserService().getUserByLoginNames(userName);
	}

	private Collection<GrantedAuthority> getGrantedAuthority(SecUser user) {

		final Collection<SecRight> rights = getUserService().getRightByUsers(user);
		final ArrayList<GrantedAuthority> rightsGrantedAuthorities = new ArrayList<GrantedAuthority>(rights.size());

		for (final SecRight right : rights) {
			rightsGrantedAuthorities.add(new SimpleGrantedAuthority(right.getRigName()));
		}

		return rightsGrantedAuthorities;
	}

	public UserService getUserService() {
		return this.userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}