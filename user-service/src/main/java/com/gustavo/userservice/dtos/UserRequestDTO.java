package com.gustavo.userservice.dtos;

import com.gustavo.userservice.validation.UsernameConstraint;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class UserRequestDTO {
	
	@NotEmpty(message="The name field is required")
	@Size(min=8, max=150, message="The length must be between 8 and 150 characters")
	private String fullName;
	
	@NotEmpty(message="The phone field is required")
	private String phone;
	
	@NotEmpty(message="The cpf field is required")
	private String cpf;
	
	@NotEmpty(message="The username field is required")
	@Size(min=4, max=50, message="The length must be between 4 and 50 characters")
	@UsernameConstraint
	private String username;
	
	@NotEmpty(message="The email field is required")
	@Email(message="Invalid email")
	private String email;
	
	@NotEmpty(message="The password field is required")
	@Size(min=8, max=20, message="The length must be between 8 and 20 characters")
	private String password;

	public UserRequestDTO() {
		
	}

	public UserRequestDTO(String fullName, String phone, String cpf, String username, String email, String password) {
		super();
		this.fullName = fullName;
		this.phone = phone;
		this.cpf = cpf;
		this.username = username;
		this.email = email;
		this.password = password;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
