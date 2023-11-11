package com.gustavo.userservice.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gustavo.userservice.entities.Role;
import com.gustavo.userservice.entities.enums.RoleType;

public interface RoleRepository extends JpaRepository<Role, UUID> {
	
	Optional<Role> findByRoleName(RoleType name);

}
