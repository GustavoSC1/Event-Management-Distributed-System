package com.gustavo.userservice.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.gustavo.userservice.dtos.UserRequestDTO;
import com.gustavo.userservice.dtos.UserResponseDTO;
import com.gustavo.userservice.dtos.rabbitmqDtos.UserEventDTO;
import com.gustavo.userservice.entities.enums.ActionType;
import com.gustavo.userservice.producers.UserProducer;
import com.gustavo.userservice.services.RoleService;
import com.gustavo.userservice.services.UserService;
import com.gustavo.userservice.services.exceptions.BusinessException;
import com.gustavo.userservice.services.exceptions.ObjectNotFoundException;

import jakarta.ws.rs.core.Response;

@Service
public class UserServiceImpl implements UserService {
	
	Logger log = LogManager.getLogger(UserServiceImpl.class);
	
	@Autowired
    private Keycloak keycloak;
	
	@Autowired
    private RoleService roleService;
			
	@Autowired
	private UserProducer userProducer;

	@Value("${keycloak.realm}")
    private String realm;
		
	@Override
	public UserResponseDTO insert(UserRequestDTO userRequestDto) {
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(userRequestDto.getUsername());
        user.setEmail(userRequestDto.getEmail());
        user.setFirstName(userRequestDto.getFirstName());
        user.setLastName(userRequestDto.getLastName());
        user.setEmailVerified(false);   

        user.setAttributes(Map.of("phone", List.of(userRequestDto.getPhone()), 
        		                  "cpf", List.of(userRequestDto.getCpf())));

		CredentialRepresentation credential = new CredentialRepresentation();        
        credential.setValue(userRequestDto.getPassword());
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);

        List<CredentialRepresentation> list = new ArrayList<>();
        list.add(credential);
        user.setCredentials(list);

        UsersResource usersResource = getUsersResource();
        Response response = usersResource.create(user);

        if (response.getStatus() == 201) {        	
        	UUID userId = UUID.fromString(response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1"));

        	UserRepresentation userRepresentation = usersResource.get(userId.toString()).toRepresentation();

        	emailVerification(userRepresentation.getId());
        	log.debug("Email was send to userId: {}", userId);
            log.info("Email was send to userId: : {}", userId);   

            UserEventDTO userEventDto = new UserEventDTO(userRepresentation);
    		userEventDto.setActionType(ActionType.CREATE.toString());
    		userProducer.produceUserEvent(userEventDto);
        	UserResponseDTO userResponseDto = new UserResponseDTO(userRepresentation);
    		log.debug("POST userServiceImpl insert userId: {} saved", userResponseDto.getUserId());
            log.info("User saved successfully userId: {}", userResponseDto.getUserId());
    		
            return userResponseDto;
        } else if(response.getStatus() == 409) {
        	log.error("User already exists: {}", response.getStatusInfo().toString());
        	throw new BusinessException("User already exists: " + response.getStatusInfo().toString());        	  
        } else {
        	log.error("Failed to create user: {}", response.getStatusInfo().toString());
        	throw new BusinessException("Failed to create user: " + response.getStatusInfo().toString());      
        }
		
	}
	
	private UsersResource getUsersResource() {
		RealmResource realmResource = keycloak.realm(realm);
        return realmResource.users();
    }
	
	@Override
	public UserResponseDTO getOneUser(UUID userId) {
		try {
			UserRepresentation userRepresentation = getUsersResource().get(userId.toString()).toRepresentation();
					
			UserResponseDTO userResponseDto = new UserResponseDTO(userRepresentation);
			
			log.debug("GET userServiceImpl getOneUser userId: {} found", userResponseDto.getUserId());
	        log.info("User found successfully userId: {}", userResponseDto.getUserId());
			
			return userResponseDto;		
		} catch(Exception e) {
			log.error("Failed to retrieve user with userId: {}", userId, e);
			throw new BusinessException("Failed to retrieve user with userId: " + userId, e);
		}
	}
	
	@Override
	public List<UserResponseDTO> findAll(String username, String firstName, String lastName, String email, int page, int pageSize) {
		try {
			List<UserRepresentation> userRepresentationList = getUsersResource().search(username, firstName, lastName, email, page * pageSize, pageSize);
			
			List<UserResponseDTO> urserResponseDto = userRepresentationList.stream().map(userRepresentation -> {
				UserResponseDTO userResponseDto = new UserResponseDTO(userRepresentation);
				return userResponseDto;}).collect(Collectors.toList());
					
			log.debug("GET userServiceImpl findAll username: {} firstName: {} lastName: {} email: {} page: {} pageSize: {} found", username, firstName, lastName, email, page, pageSize);
	        log.info("Users found successfully username: {} firstName: {} lastName: {} email: {} page: {} pageSize: {}", username, firstName, lastName, email, page, pageSize);
	        
			return urserResponseDto;
		} catch(Exception e) {
			log.error("Failed to find users with criteria username: {}, firstName: {}, lastName: {}, email: {}, page: {}, pageSize: {}", 
	                  username, firstName, lastName, email, page, pageSize, e);
			throw new BusinessException("Error retrieving users with the provided criteria", e);
		}
	}
	
	@Override
	public UserResponseDTO update(UUID userId, UserRequestDTO userRequestDto) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if(!authentication.getName().equals(userId.toString())) {
			log.warn("Access denied! userId: {}", userId);
			throw new AccessDeniedException("Error: Access denied!");
		}
		
		try {			
			UserResource usersResource = getUsersResource().get(userId.toString());		
			UserRepresentation userRepresentation = usersResource.toRepresentation();
			
			userRepresentation.setEnabled(true);
			userRepresentation.setUsername(userRequestDto.getUsername());
			userRepresentation.setFirstName(userRequestDto.getFirstName());
			userRepresentation.setLastName(userRequestDto.getLastName());
	        
	        Map<String, List<String>> attributes = userRepresentation.getAttributes();
	        if (attributes == null) {
	            attributes = new HashMap<>();
	        }
	        
	        attributes.put("phone", List.of(userRequestDto.getPhone()));
	        attributes.put("cpf", List.of(userRequestDto.getCpf()));
	        
	        userRepresentation.setAttributes(attributes);	
	        	        
	        usersResource.update(userRepresentation);
	        	        
	        UserResponseDTO userResponseDto = new UserResponseDTO(userRepresentation);
	        
	        log.debug("PUT userServiceImpl update userId: {} updated", userResponseDto.getUserId());
	        log.info("User updated successfully userId: {}", userResponseDto.getUserId());
	                       
	        return userResponseDto;   
		} catch (Exception e) {
			log.error("Failed to update user with userId: {}", userId, e);
			throw new BusinessException("Failed to update user with userId: " + userId, e);
        }
	}
	
	@Override
	public void delete(UUID userId) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if(!authentication.getName().equals(userId.toString()) && !roleService.hasRole("ROLE_ADMIN")) {
			log.warn("Access denied! userId: {}", userId);
			throw new AccessDeniedException("Error: Access denied!");
		}
		
		try {	
			getUsersResource().delete(userId.toString());
					
			log.debug("DELETE userServiceImpl delete userId: {} deleted", userId);
	        log.info("User successfully deleted userId: {}", userId);
		} catch (Exception e) {
			log.error("Failed to delete user with userId: {}", userId, e);
			throw new BusinessException("Failed to delete user with userId: " + userId, e);
        }
	}
	
	@Override
	public void updateEmail(UUID userId, UserRequestDTO userRequestDto) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if(!authentication.getName().equals(userId.toString())) {
			log.warn("Access denied! userId: {}", userId);
			throw new AccessDeniedException("Error: Access denied!");
		}
		
		try {
			UsersResource usersResource = getUsersResource();
			UserRepresentation userRepresentation = new UserRepresentation();
			userRepresentation.setEmail(userRequestDto.getEmail());
			userRepresentation.setEmailVerified(false);   
	        usersResource.get(userId.toString()).update(userRepresentation);	     
	        emailVerification(userId.toString());
	    	       	        	        
		} catch (Exception e) {
			log.error("Failed to update user email with userId: {}", userId, e);
			throw new BusinessException("Failed to update user email with userId: " + userId, e);
        }
	}
	
	@Override
	public void updatePassword(UUID userId) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if(!authentication.getName().equals(userId.toString())) {
			log.warn("Access denied! userId: {}", userId);
			throw new AccessDeniedException("Error: Access denied!");
		}
		try {
			UserResource userResource = getUsersResource().get(userId.toString());	
	        userResource.executeActionsEmail(List.of("UPDATE_PASSWORD"));
		               			
			log.debug("PATCH userServiceImpl updatePassword userId: {} updated", userId);
		    log.info("User password updated successfully userId: {}", userId);
		} catch (Exception e) {
			log.error("Failed to update user password with userId: {}", userId, e);
			throw new BusinessException("Failed to update user password with userId: " + e.getMessage());
        }
	}
	
	@Override
	public void forgotPassword(String userEmail) {
		
		UsersResource usersResource = getUsersResource();	
		
		List<UserRepresentation> users = usersResource.searchByEmail(userEmail, true);
		
		if (users.isEmpty()) {
            throw new ObjectNotFoundException("Error: User not found! Email: " + userEmail);
        }
		try {
			UUID userId = UUID.fromString(users.get(0).getId());
	
	        usersResource.get(userId.toString()).executeActionsEmail(List.of("UPDATE_PASSWORD"));
	        
	        log.debug("PATCH userServiceImpl forgotPassword userEmail: {} sent", userEmail);
		    log.info("User password reset email sent successfully userEmail: {}", userEmail);
		} catch (Exception e) {
			log.error("Failed to update user password with userEmail: {}", userEmail, e);
			throw new BusinessException("Failed to update user password with userEmail: " + userEmail, e);
        }
	}
	
	@Override
	public UserResponseDTO updateImage(UUID userId, UserRequestDTO userRequestDto) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if(!authentication.getName().equals(userId.toString())) {
			log.warn("Access denied! userId: {}", userId);
			throw new AccessDeniedException("Error: Access denied!");
		}
		
		UserResource usersResource = getUsersResource().get(userId.toString());		
		UserRepresentation userRepresentation = usersResource.toRepresentation();
		
		// buscar os atributos existentes
        Map<String, List<String>> attributes = userRepresentation.getAttributes();
        if (attributes == null) {
            attributes = new HashMap<>();
        }
       
        // modifica apenas aquele que deseja atualizar
        attributes.put("imageUrl", List.of(userRequestDto.getImageUrl()));
        
        // aplica as alterações de volta ao usuário
        userRepresentation.setAttributes(attributes);		
		
        try {
			usersResource.update(userRepresentation);
	
	        UserResponseDTO userResponseDto = new UserResponseDTO(userRepresentation);
	
			log.debug("PATCH userServiceImpl updateImage userId: {} updated", userResponseDto.getUserId());
	        log.info("User image updated successfully userId: {}", userResponseDto.getUserId());
			
			return userResponseDto;
        } catch (Exception e) {
        	log.error("Failed to update user image with userId: {}", userId, e);
			throw new BusinessException("Failed to update user image with userId: " + userId, e);
        }
		
	}
	
	public void emailVerification(String userId) {
		UsersResource usersResource = getUsersResource();
		usersResource.get(userId).sendVerifyEmail();
	}	
	
}
