package com.gustavo.userservice.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
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
@RequestMapping("/auth")
@SecurityRequirement(name = "Keycloak")
@Tag(name = "Auth endpoint")
public class AuthController {
	
	Logger log = LogManager.getLogger(AuthController.class);
	
	@Autowired
	private UserService userService;

	@Operation(summary = "Save a user")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "User successfully saved"),
			@ApiResponse(responseCode = "400", description = "This request can be processed"),
			@ApiResponse(responseCode = "422", description = "Data validation error")
	})
	@PostMapping("/register")
	// A anotação @Validated oferece suporte a "grupos de validação"
	public ResponseEntity<UserResponseDTO> insert(@RequestBody @Validated(UserRequestDTO.UserView.UserPost.class)  
									@JsonView(UserRequestDTO.UserView.UserPost.class) UserRequestDTO userRequestDto) {
		log.debug("POST authController insert userRequestDto received {}", userRequestDto.toString());
		
		UserResponseDTO userResponseDto = userService.insert(userRequestDto);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDto);
	}
	
	@Operation(summary = "Updates a user password")
	@PutMapping("/forgot-password")
	public ResponseEntity<String> forgotPassword(@RequestParam String email) {
		log.debug("PATCH authController forgotPassword userEmail: {} received", email);
		userService.forgotPassword(email);
		
		return ResponseEntity.status(HttpStatus.OK).body("Password reset email sent successfully.");
	}

}
