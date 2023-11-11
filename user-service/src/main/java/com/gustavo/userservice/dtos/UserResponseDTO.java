package com.gustavo.userservice.dtos;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.gustavo.userservice.entities.Role;
import com.gustavo.userservice.entities.enums.UserStatus;

public class UserResponseDTO {
	
	private UUID userId;
	private String name;
	private String phone;
	private String cpf;
	private String imageUrl;
	private String username;
	private String email;
	private UserStatus userStatus;
	private LocalDateTime creationDate;
	private LocalDateTime lastUpdateDate;
	private Set<Role> roles = new HashSet<>();
		
	public UserResponseDTO() {
		
	}
	
	public UserResponseDTO(UUID userId, String name, String phone, String cpf, String imageUrl, String username,
			String email, UserStatus userStatus, LocalDateTime creationDate, LocalDateTime lastUpdateDate) {
		super();
		this.userId = userId;
		this.name = name;
		this.phone = phone;
		this.cpf = cpf;
		this.imageUrl = imageUrl;
		this.username = username;
		this.email = email;
		this.userStatus = userStatus;
		this.creationDate = creationDate;
		this.lastUpdateDate = lastUpdateDate;
	}

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UserStatus getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(UserStatus userStatus) {
		this.userStatus = userStatus;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public LocalDateTime getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

}
