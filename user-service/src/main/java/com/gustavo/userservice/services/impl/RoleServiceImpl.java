package com.gustavo.userservice.services.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gustavo.userservice.entities.Role;
import com.gustavo.userservice.entities.enums.RoleType;
import com.gustavo.userservice.repositories.RoleRepository;
import com.gustavo.userservice.services.RoleService;
import com.gustavo.userservice.services.exceptions.ObjectNotFoundException;

@Service
public class RoleServiceImpl implements RoleService {
	
	Logger log = LogManager.getLogger(RoleServiceImpl.class);
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Override
	public Role findByRoleName(RoleType roleName) {
		
		Role role =  roleRepository.findByRoleName(roleName)
				.orElseThrow(() -> {
					log.error("Role not found! roleName: {}", roleName);
					return new ObjectNotFoundException("Error: Role not found!");
				});
		
		log.debug("GET roleServiceImpl findByRoleName roleName: {} found", roleName);
		
		return role;
	}

}
