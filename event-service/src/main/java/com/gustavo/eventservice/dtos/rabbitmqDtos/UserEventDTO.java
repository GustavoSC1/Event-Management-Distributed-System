package com.gustavo.eventservice.dtos.rabbitmqDtos;

import java.util.UUID;

public class UserEventDTO {
	
	private UUID userId;
	private String name;
	private String phone;	
	private String cpf;
	private String imageUrl;
	private String username;
	private String email;
	private String userStatus;
	private String actionType;
	
	public UserEventDTO() {

	}

	public UserEventDTO(UUID userId, String name, String phone, String cpf, String imageUrl, String username,
			String email, String userStatus, String actionType) {
		this.userId = userId;
		this.name = name;
		this.phone = phone;
		this.cpf = cpf;
		this.imageUrl = imageUrl;
		this.username = username;
		this.email = email;
		this.userStatus = userStatus;
		this.actionType = actionType;
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

	public String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

}
