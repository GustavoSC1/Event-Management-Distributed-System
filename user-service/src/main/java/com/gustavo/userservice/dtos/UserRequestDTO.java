package com.gustavo.userservice.dtos;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.gustavo.userservice.services.validation.UsernameConstraint;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

// É usada para indicar quando a propriedade anotada pode ser serializada. 
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRequestDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	// É possível definir várias views do mesmo model/DTOs com diferentes combinações de campos.
	// Define as views como interfaces.
	public interface UserView {
		public static interface UserPost {}
		public static interface UserPut {}
		public static interface ImagePut {}
		public static interface PasswordPut {}
	}
	
	@NotEmpty(message="The name field is required", groups = {UserView.UserPost.class, UserView.UserPut.class})
	@Size(min=8, max=150, message="The length must be between 8 and 150 characters", groups = {UserView.UserPost.class, UserView.UserPut.class})
	// Usado para mapear campos para uma ou mais views.
	@JsonView({UserView.UserPost.class, UserView.UserPut.class})
	private String name;
	
	@NotEmpty(message="The phone field is required", groups = {UserView.UserPost.class, UserView.UserPut.class})
	@JsonView({UserView.UserPost.class, UserView.UserPut.class})
	private String phone;
	
	@NotEmpty(message="The cpf field is required", groups = {UserView.UserPost.class, UserView.UserPut.class})
	@JsonView({UserView.UserPost.class, UserView.UserPut.class})
	private String cpf;
	
	@NotEmpty(message="The username field is required", groups = UserView.UserPost.class)
	@Size(min=4, max=50, message="The length must be between 4 and 50 characters", groups = UserView.UserPost.class)
	@UsernameConstraint(groups = UserView.UserPost.class)
	@JsonView(UserView.UserPost.class)
	private String username;
	
	@NotEmpty(message="The imageUrl field is required", groups = UserView.ImagePut.class)
	@JsonView(UserView.ImagePut.class)
	private String imageUrl;
	
	@NotEmpty(message="The email field is required", groups = UserView.UserPost.class)
	@Email(message="Invalid email", groups = UserView.UserPost.class)
	@JsonView(UserView.UserPost.class)
	private String email;
	
	@NotEmpty(message="The password field is required", groups = {UserView.UserPost.class, UserView.PasswordPut.class})
	@Size(min=8, max=20, message="The length must be between 8 and 20 characters", groups = {UserView.UserPost.class, UserView.PasswordPut.class})
	@JsonView({UserView.UserPost.class, UserView.PasswordPut.class})
	private String password;
	
	@NotEmpty(message="The old password field is required", groups = UserView.PasswordPut.class)
	@Size(min=8, max=20, message="The length must be between 8 and 20 characters", groups = UserView.PasswordPut.class)
	@JsonView(UserView.PasswordPut.class)
	private String oldPassword;

	public UserRequestDTO() {
		
	}

	public UserRequestDTO(String name, String phone, String cpf, String username, String imageUrl, String email, String password) {
		super();
		this.name = name;
		this.phone = phone;
		this.cpf = cpf;
		this.username = username;
		this.imageUrl = imageUrl;
		this.email = email;
		this.password = password;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

}
