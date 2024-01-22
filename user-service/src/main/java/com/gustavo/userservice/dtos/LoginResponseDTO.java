package com.gustavo.userservice.dtos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LoginResponseDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String token;
	private String type = "Bearer";
	private String refreshToken;
	private UUID userId;
	private String username;
	private List<String> roles = new ArrayList<>();
	
	public LoginResponseDTO() {

	}

	public LoginResponseDTO(String token, String refreshToken, UUID userId, String username,
			List<String> roles) {
		this.token = token;
		this.refreshToken = refreshToken;
		this.userId = userId;
		this.username = username;
		this.roles = roles;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

}
