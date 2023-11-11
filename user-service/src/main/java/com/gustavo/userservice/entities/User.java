package com.gustavo.userservice.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import com.gustavo.userservice.entities.enums.UserStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "TB_USER")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID userId;
	
	@Column(nullable = false, length = 150)
	private String name;
	
	@Column(length = 20)
	private String phone;
	
	@Column(length = 20)
	private String cpf;
	
	@Column
	private String imageUrl;

	@Column(nullable = false, unique = true, length = 50)
	private String username;
	
	@Column(nullable = false, unique = true, length = 50)
	private String email;
	
	@Column(nullable = false, length = 255)
	private String password;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private UserStatus userStatus;
	
	@Column(nullable = false)
	private LocalDateTime creationDate;
	
	@Column(nullable = false)
	private LocalDateTime lastUpdateDate;
	
	@ManyToMany()
	@JoinTable(
			name = "TB_USER_ROLE",
			joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
	)
	private Set<Role> roles = new HashSet<>();

	public User() {

	}

	public User(UUID userId, String name, String phone, String cpf, String imageUrl, String username, String email,
			String password, UserStatus userStatus, LocalDateTime creationDate, LocalDateTime lastUpdateDate) {
		super();
		this.userId = userId;
		this.name = name;
		this.phone = phone;
		this.cpf = cpf;
		this.imageUrl = imageUrl;
		this.username = username;
		this.email = email;
		this.password = password;
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

	public void setFullName(String name) {
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	@Override
	public int hashCode() {
		return Objects.hash(cpf, creationDate, email, name, imageUrl, lastUpdateDate, password, phone, roles,
				userId, userStatus, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(cpf, other.cpf) && Objects.equals(creationDate, other.creationDate)
				&& Objects.equals(email, other.email) && Objects.equals(name, other.name)
				&& Objects.equals(imageUrl, other.imageUrl) && Objects.equals(lastUpdateDate, other.lastUpdateDate)
				&& Objects.equals(password, other.password) && Objects.equals(phone, other.phone)
				&& Objects.equals(roles, other.roles) && Objects.equals(userId, other.userId)
				&& userStatus == other.userStatus && Objects.equals(username, other.username);
	}	

}
