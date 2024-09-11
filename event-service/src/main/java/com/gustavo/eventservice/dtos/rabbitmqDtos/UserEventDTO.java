package com.gustavo.eventservice.dtos.rabbitmqDtos;

import java.util.UUID;

public class UserEventDTO {
	
	private UUID userId;
	private String firstName;
	private String lastName;
	private String phone;	
	private String cpf;
	private String imageUrl;
	private String username;
	private String email;
	private String actionType;
	
	public UserEventDTO() {

	}

	public UserEventDTO(UUID userId, String firstName, String lastName, String phone, String cpf, String imageUrl, String username,
			String email, String actionType) {
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.cpf = cpf;
		this.imageUrl = imageUrl;
		this.username = username;
		this.email = email;
		this.actionType = actionType;
	}

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	@Override
	public String toString() {
		return "UserEventDTO [userId=" + userId + ", firstName=" + firstName + ", lastName=" + lastName + ", phone="
				+ phone + ", cpf=" + cpf + ", imageUrl=" + imageUrl + ", username=" + username + ", email=" + email
				+ ", actionType=" + actionType + "]";
	}

}
