package com.gustavo.userservice.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.gustavo.userservice.dtos.UserRequestDTO;
import com.gustavo.userservice.dtos.UserResponseDTO;
import com.gustavo.userservice.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping()
	public ResponseEntity<UserResponseDTO> insert(@RequestBody @Validated(UserRequestDTO.UserView.UserPost.class)  
									@JsonView(UserRequestDTO.UserView.UserPost.class) UserRequestDTO userRequestDto) {
		UserResponseDTO userResponseDto = userService.insert(userRequestDto);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDto);
	}
	
	@GetMapping("/{userId}")
	public ResponseEntity<UserResponseDTO> getOneUser(@PathVariable UUID userId) {
		UserResponseDTO userResponseDto = userService.getOneUser(userId);
		
		return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
	}
	
	@PutMapping("/{userId}")
	public ResponseEntity<UserResponseDTO> update(@PathVariable UUID userId, 
			@RequestBody @Validated(UserRequestDTO.UserView.UserPut.class) 
			@JsonView(UserRequestDTO.UserView.UserPut.class) UserRequestDTO userRequestDto) {
		UserResponseDTO userResponseDto = userService.update(userId, userRequestDto);
		
		return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
	}
	
	@DeleteMapping("/{userId}")
	public ResponseEntity<String> delete(@PathVariable UUID userId) {
		userService.delete(userId);
		
		return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully.");
	}
	
	@PutMapping("/{userId}/password")
	public ResponseEntity<String> updatePassword(@PathVariable UUID userId, 
			@RequestBody @Validated(UserRequestDTO.UserView.PasswordPut.class) 
			@JsonView(UserRequestDTO.UserView.PasswordPut.class) UserRequestDTO userRequestDto) {
		userService.updatePassword(userId, userRequestDto);
		
		return ResponseEntity.status(HttpStatus.OK).body("Password updated successfully.");
	}
	
	@PutMapping("/{userId}/image")
	public ResponseEntity<UserResponseDTO> updateImage(@PathVariable UUID userId, 
			@RequestBody @Validated(UserRequestDTO.UserView.ImagePut.class) 
			@JsonView(UserRequestDTO.UserView.ImagePut.class) UserRequestDTO userRequestDto) {
		UserResponseDTO userResponseDto = userService.updateImage(userId, userRequestDto);
		
		return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
	}
	
}
