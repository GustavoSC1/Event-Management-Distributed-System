package com.gustavo.userservice.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gustavo.userservice.entities.Role;
import com.gustavo.userservice.entities.enums.RoleType;
import com.gustavo.userservice.repositories.RoleRepository;
import com.gustavo.userservice.services.RoleService;
import com.gustavo.userservice.services.exceptions.ObjectNotFoundException;

@Service
public class RoleServiceImpl implements RoleService {
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Override
	public Role findByRoleName(RoleType roleName) {
		return roleRepository.findByRoleName(roleName)
				.orElseThrow(() -> new ObjectNotFoundException("Error: Role not found!"));
	}

}
