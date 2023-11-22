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
	
	public Role findByRoleName(RoleType name) {
		return roleRepository.findByRoleName(RoleType.ROLE_USER)
				.orElseThrow(() -> new ObjectNotFoundException("Error: Role is not found!"));
	}

}
