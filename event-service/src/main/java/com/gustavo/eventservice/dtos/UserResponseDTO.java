package com.gustavo.eventservice.dtos;

import java.io.Serializable;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private UUID userId;
	private String firstName;
	private String lastName;
	private String cpf;
	private String imageUrl;
	private String email;
	
	public UserResponseDTO() {

	}

	public UserResponseDTO(UUID userId, String firstName, String lastName, String cpf, String imageUrl, String email) {
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.cpf = cpf;
		this.imageUrl = imageUrl;
		this.email = email;
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

}
