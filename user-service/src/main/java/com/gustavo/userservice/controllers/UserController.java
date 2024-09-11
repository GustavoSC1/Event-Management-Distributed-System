package com.gustavo.userservice.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.gustavo.userservice.dtos.UserRequestDTO;
import com.gustavo.userservice.dtos.UserResponseDTO;
import com.gustavo.userservice.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/users")
@SecurityRequirement(name = "Keycloak")
@Tag(name = "User endpoint")
public class UserController {
	
	Logger log = LogManager.getLogger(UserController.class);
	
	@Autowired
	private UserService userService;
	
	@Operation(summary = "Obtains a user details by id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "User obtained successfully"),
			@ApiResponse(responseCode = "404", description = "Could not find the requested data")
	})
	@GetMapping("/{userId}")
	public ResponseEntity<UserResponseDTO> getOneUser(@PathVariable UUID userId) {
		log.debug("GET userController getOneUser userId: {} received", userId);
		UserResponseDTO userResponseDto = userService.getOneUser(userId);
				
		return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
	}
	
	@Operation(summary = "Find all Users")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Users found successfully"),
			@ApiResponse(responseCode = "403", description = "You are not allowed to make this request")
	})	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@GetMapping
	public ResponseEntity<List<UserResponseDTO>> getAllUsers(
			@RequestParam(value="username", defaultValue="") String username,
			@RequestParam(value="firstName", defaultValue="") String firstName,
			@RequestParam(value="lastName", defaultValue="") String lastName,
			@RequestParam(value="email", defaultValue="") String email,
			@RequestParam(value="page", defaultValue="0") String page,
			@RequestParam(value="pageSize", defaultValue="10") String pageSize) {
		
		log.debug("GET userController getAllUsers username: {} firstName: {} lastName: {} email: {} page: {} pageSize: {} received", username, firstName, lastName, email, page, pageSize);
		
		List<UserResponseDTO> urserResponseDtoPage = userService.findAll(username, firstName, lastName, email, Integer.valueOf(page), Integer.valueOf(pageSize));
		
		if(!urserResponseDtoPage.isEmpty()) {
			
			for(UserResponseDTO userResponseDto: urserResponseDtoPage) {
				userResponseDto.add(linkTo(methodOn(UserController.class).getOneUser(userResponseDto.getUserId())).withSelfRel());
			}
			
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(urserResponseDtoPage);
	}
	
	@Operation(summary = "Updates a user")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully edited user"),
			@ApiResponse(responseCode = "403", description = "You are not allowed to make this request"),
			@ApiResponse(responseCode = "404", description = "Could not find the requested data"),
			@ApiResponse(responseCode = "422", description = "Data validation error")
	})
	@PutMapping("/{userId}")
	public ResponseEntity<UserResponseDTO> update(@PathVariable UUID userId, 
			@RequestBody @Validated(UserRequestDTO.UserView.UserPut.class) 
			@JsonView(UserRequestDTO.UserView.UserPut.class) UserRequestDTO userRequestDto) {
		log.debug("PUT userController update userId: {} userRequestDto received {}", userId, userRequestDto.toString());
		UserResponseDTO userResponseDto = userService.update(userId, userRequestDto);
		
		return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
	}
	
	@Operation(summary = "Deletes a user by id")
	@ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User succesfully deleted"),
            @ApiResponse(responseCode = "403", description = "You are not allowed to make this request"),
            @ApiResponse(responseCode = "404", description = "Could not find the requested data")
    })
	@DeleteMapping("/{userId}")
	public ResponseEntity<String> delete(@PathVariable UUID userId) {
		log.debug("DELETE userController delete userId: {} received", userId);
		userService.delete(userId);
		
		return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully.");
	}
	
	@Operation(summary = "Updates a user email")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully edited user email"),
			@ApiResponse(responseCode = "400", description = "This request can be processed"),
			@ApiResponse(responseCode = "403", description = "You are not allowed to make this request"),
			@ApiResponse(responseCode = "404", description = "Could not find the requested data"),
			@ApiResponse(responseCode = "422", description = "Data validation error")
	})
	@PatchMapping("/{userId}/email")
	public ResponseEntity<String> updateEmail(@PathVariable UUID userId, 
			@RequestBody @Validated(UserRequestDTO.UserView.EmailPut.class) 
			@JsonView(UserRequestDTO.UserView.EmailPut.class) UserRequestDTO userRequestDto) {
		log.debug("PATCH userController updateEmail userId: {} userRequestDto received {}", userId, userRequestDto.toString());
		userService.updateEmail(userId, userRequestDto);
		
		return ResponseEntity.status(HttpStatus.OK).body("Email updated successfully.");
	}
	
	@Operation(summary = "Updates a user password")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully edited user password"),
			@ApiResponse(responseCode = "400", description = "This request can be processed"),
			@ApiResponse(responseCode = "403", description = "You are not allowed to make this request"),
			@ApiResponse(responseCode = "404", description = "Could not find the requested data"),
			@ApiResponse(responseCode = "422", description = "Data validation error")
	})
	@PatchMapping("/{userId}/password")
	public ResponseEntity<String> updatePassword(@PathVariable UUID userId) {
		log.debug("PATCH userController updatePassword userId: {} userRequestDto received {}", userId);
		userService.updatePassword(userId);
		
		return ResponseEntity.status(HttpStatus.OK).body("Password updated successfully.");
	}
	
	@Operation(summary = "Updates a user image")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully edited user image"),
			@ApiResponse(responseCode = "403", description = "You are not allowed to make this request"),
			@ApiResponse(responseCode = "404", description = "Could not find the requested data"),
			@ApiResponse(responseCode = "422", description = "Data validation error")
	})
	@PatchMapping("/{userId}/image")
	public ResponseEntity<UserResponseDTO> updateImage(@PathVariable UUID userId, 
			@RequestBody @Validated(UserRequestDTO.UserView.ImagePut.class) 
			@JsonView(UserRequestDTO.UserView.ImagePut.class) UserRequestDTO userRequestDto) {
		log.debug("PATCH userController updateImage userId: {} userRequestDto received {}", userId, userRequestDto.toString());
		UserResponseDTO userResponseDto = userService.updateImage(userId, userRequestDto);
		
		return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
	}
	
}
