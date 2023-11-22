package com.gustavo.userservice.services;

import com.gustavo.userservice.entities.Role;
import com.gustavo.userservice.entities.enums.RoleType;

public interface RoleService {
	
	Role findByRoleName(RoleType name);

}
