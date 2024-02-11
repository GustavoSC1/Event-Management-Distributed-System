package com.gustavo.userservice.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/users")
@Tag(name = "User endpoint")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Operation(summary = "Obtains a user details by id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "User obtained successfully"),
			@ApiResponse(responseCode = "404", description = "Could not find the requested data")
	})
	@GetMapping("/{userId}")
	public ResponseEntity<UserResponseDTO> getOneUser(@PathVariable UUID userId) {
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
	public ResponseEntity<Page<UserResponseDTO>> getAllUsers(
			@RequestParam(value="name", defaultValue="") String name,
			@RequestParam(value="email", defaultValue="") String email,
			@PageableDefault(page = 0, size = 10, sort = "userId", direction = Sort.Direction.ASC) Pageable pageable) {
		
		Page<UserResponseDTO> urserResponseDtoPage = userService.findAll(name, email, pageable);
		
		if(!urserResponseDtoPage.isEmpty()) {
			
			for(UserResponseDTO userResponseDto: urserResponseDtoPage.toList()) {
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
		userService.delete(userId);
		
		return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully.");
	}
	
	@Operation(summary = "Updates a user password")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully edited user password"),
			@ApiResponse(responseCode = "400", description = "This request can be processed"),
			@ApiResponse(responseCode = "403", description = "You are not allowed to make this request"),
			@ApiResponse(responseCode = "404", description = "Could not find the requested data"),
			@ApiResponse(responseCode = "422", description = "Data validation error")
	})
	@PutMapping("/{userId}/password")
	public ResponseEntity<String> updatePassword(@PathVariable UUID userId, 
			@RequestBody @Validated(UserRequestDTO.UserView.PasswordPut.class) 
			@JsonView(UserRequestDTO.UserView.PasswordPut.class) UserRequestDTO userRequestDto) {
		userService.updatePassword(userId, userRequestDto);
		
		return ResponseEntity.status(HttpStatus.OK).body("Password updated successfully.");
	}
	
	@Operation(summary = "Updates a user image")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully edited user image"),
			@ApiResponse(responseCode = "403", description = "You are not allowed to make this request"),
			@ApiResponse(responseCode = "404", description = "Could not find the requested data"),
			@ApiResponse(responseCode = "422", description = "Data validation error")
	})
	@PutMapping("/{userId}/image")
	public ResponseEntity<UserResponseDTO> updateImage(@PathVariable UUID userId, 
			@RequestBody @Validated(UserRequestDTO.UserView.ImagePut.class) 
			@JsonView(UserRequestDTO.UserView.ImagePut.class) UserRequestDTO userRequestDto) {
		UserResponseDTO userResponseDto = userService.updateImage(userId, userRequestDto);
		
		return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
	}
	
}
