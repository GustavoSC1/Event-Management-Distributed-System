package com.gustavo.userservice.utils;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.gustavo.userservice.entities.Role;
import com.gustavo.userservice.entities.enums.RoleType;
import com.gustavo.userservice.entities.enums.UserStatus;

public class UserAuthenticated implements UserDetails {	
	private static final long serialVersionUID = 1L;
	
	private UUID id;
	private String username;
	private String password;
	private UserStatus userStatus;
	private Collection<? extends GrantedAuthority> authorities;
		
	public UserAuthenticated(UUID id, String username, String password, UserStatus userStatus, Set<Role> roles) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.userStatus = userStatus;
		this.authorities = roles.stream().map(role -> new SimpleGrantedAuthority(role.getRoleName().toString())).collect(Collectors.toList());		
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
		return password;
	}

	@Override
	public String getUsername() {
		return username;
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
	
	public boolean hasRole(RoleType role) {
		return getAuthorities().contains(new SimpleGrantedAuthority(role.toString()));
	}

}
