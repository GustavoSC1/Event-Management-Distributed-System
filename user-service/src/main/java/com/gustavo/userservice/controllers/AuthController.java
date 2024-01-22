package com.gustavo.userservice.controllers;

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

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RefreshTokenService refreshTokenService;
	
	@PostMapping("/register")
	// A anotação @Validated oferece suporte a "grupos de validação"
	public ResponseEntity<UserResponseDTO> insert(@RequestBody @Validated(UserRequestDTO.UserView.UserPost.class)  
									@JsonView(UserRequestDTO.UserView.UserPost.class) UserRequestDTO userRequestDto) {
		UserResponseDTO userResponseDto = userService.insert(userRequestDto);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDto);
	}
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponseDTO> login(Authentication authentication) {
		LoginResponseDTO token = userService.login(authentication);
		
		return ResponseEntity.ok().body(token);
	}
	
	
	@PostMapping("/refreshtoken")
	public ResponseEntity<TokenRefreshResponseDTO> refreshtoken(@RequestBody @Validated TokenRefreshRequestDTO request) {
		TokenRefreshResponseDTO token = refreshTokenService.refreshToken(request);
		 
		return ResponseEntity.ok().body(token);
	}
	
	@PostMapping("/signout")
	public ResponseEntity<String> logoutUser() {
		refreshTokenService.logoutUser();
		String message = "Log out successful!";
		
		return ResponseEntity.ok().body(message);
	}

}
