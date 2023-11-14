package com.gustavo.userservice.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gustavo.userservice.entities.User;

public interface UserRepository extends JpaRepository<User, UUID> {
	
	boolean existsByUsername(String username);
	
	boolean existsByEmail(String email);
	
	// O módulo Spring Data aplicará a estratégia FetchType.EAGER nos nós de 
	// atributos especificados, mesmo que nossa entidade declare uma estratégia de carregamento LAZY. 
	// E para outros, será aplicada a estratégia FetchType.LAZY.
	// https://www.baeldung.com/spring-data-jpa-named-entity-graphs
	@EntityGraph(
			attributePaths = "roles", type = EntityGraph.EntityGraphType.FETCH
	)
	Optional<User> findById(UUID userId);

}
