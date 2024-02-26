package com.gustavo.userservice.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.gustavo.userservice.dtos.LoginResponseDTO;
import com.gustavo.userservice.dtos.TokenRefreshRequestDTO;
import com.gustavo.userservice.dtos.TokenRefreshResponseDTO;
import com.gustavo.userservice.dtos.UserRequestDTO;
import com.gustavo.userservice.dtos.UserResponseDTO;
import com.gustavo.userservice.services.RefreshTokenService;
import com.gustavo.userservice.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth endpoint")
public class AuthController {
	
	Logger log = LogManager.getLogger(AuthController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RefreshTokenService refreshTokenService;
	
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
	
	@Operation(summary = "Login user", security = {@SecurityRequirement(name = "basicAuth")})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "User successfully saved"),
			@ApiResponse(responseCode = "400", description = "This request can be processed"),
			@ApiResponse(responseCode = "422", description = "Data validation error")
	})
	@PostMapping("/login")	
	public ResponseEntity<LoginResponseDTO> login(Authentication authentication) {
		log.debug("POST authController login authentication received {}", authentication.toString());
		LoginResponseDTO token = userService.login(authentication);
		
		return ResponseEntity.ok().body(token);
	}
	
	@Operation(summary = "Generate a new token")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Token generated successfully"),
			@ApiResponse(responseCode = "403", description = "Invalid refresh token"),
			@ApiResponse(responseCode = "422", description = "Data validation error")
	})
	@PostMapping("/refreshtoken")
	public ResponseEntity<TokenRefreshResponseDTO> refreshtoken(@RequestBody @Validated TokenRefreshRequestDTO request) {
		log.debug("POST authController refreshtoken request received {}", request.toString());
		TokenRefreshResponseDTO token = refreshTokenService.refreshToken(request);
		 
		return ResponseEntity.ok().body(token);
	}
	
	@Operation(summary = "Log out of account")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Log out completed successfully"),
			@ApiResponse(responseCode = "400", description = "This request can be processed")
	})
	@PostMapping("/signout")
	public ResponseEntity<String> logoutUser() {		
		refreshTokenService.logoutUser();
		String message = "Log out successful!";
		
		return ResponseEntity.ok().body(message);
	}

}
