package com.gustavo.eventservice.utils;

import java.util.Collection;
import java.util.UUID;

import com.gustavo.eventservice.entities.enums.UserStatus;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserAuthenticated implements UserDetails {	
	private static final long serialVersionUID = 1L;
	
	private UUID id;
	private UserStatus userStatus;
	private Collection<? extends GrantedAuthority> authorities;
		
	public UserAuthenticated(UUID id, UserStatus userStatus, Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.userStatus = userStatus;
		this.authorities = authorities;		
	}

	public UUID getId() {
		return id;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {		
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {			
		if(userStatus.equals(UserStatus.ACTIVE)) {
			return true;
		} else { 
			return false;
		}		
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
	public boolean hasRole(String role) {
		return getAuthorities().contains(new SimpleGrantedAuthority(role));
	}

}
