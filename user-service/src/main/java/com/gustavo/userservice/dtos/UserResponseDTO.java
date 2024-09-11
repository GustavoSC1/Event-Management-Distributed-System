package com.gustavo.userservice.dtos;

import java.io.Serializable;
import java.util.UUID;

import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseDTO extends RepresentationModel<UserResponseDTO> implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private UUID userId;
	private String firstName;
	private String lastName;
	private String phone;
	private String cpf;
	private String imageUrl;
	private String username;
	private String email;
		
	public UserResponseDTO() {
		
	}
	
	public UserResponseDTO(UUID userId, String firstName, String lastName, String phone, String cpf, String imageUrl, String username,
			String email) {
		super();
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.cpf = cpf;
		this.imageUrl = imageUrl;
		this.username = username;
		this.email = email;
	}

	public UserResponseDTO(UserRepresentation userRepresentation) {
		super();
		this.userId = UUID.fromString(userRepresentation.getId());
		this.firstName = userRepresentation.getFirstName();
		this.lastName = userRepresentation.getLastName();
		this.phone = userRepresentation.getAttributes().get("phone").get(0);
		this.cpf = userRepresentation.getAttributes().get("cpf").get(0);
		this.imageUrl = userRepresentation.getAttributes().containsKey("imageUrl") ? 
				userRepresentation.getAttributes().get("imageUrl").get(0) : null;
		this.username = userRepresentation.getUsername();
		this.email = userRepresentation.getEmail();
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

}
