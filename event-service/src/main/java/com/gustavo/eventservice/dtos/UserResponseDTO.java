package com.gustavo.eventservice.dtos;

import java.io.Serializable;
import java.util.UUID;

import com.gustavo.eventservice.entities.enums.UserStatus;

public class UserResponseDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private UUID userId;
	private String name;
	private String cpf;
	private String imageUrl;
	private String email;
	private UserStatus userStatus;
	
	public UserResponseDTO() {

	}

	public UserResponseDTO(UUID userId, String name, String cpf, String imageUrl, String email, UserStatus userStatus) {
		this.userId = userId;
		this.name = name;
		this.cpf = cpf;
		this.imageUrl = imageUrl;
		this.email = email;
		this.userStatus = userStatus;
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

}
