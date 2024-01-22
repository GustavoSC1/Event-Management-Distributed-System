package com.gustavo.userservice.entities;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="TB_REFRESHTOKEN")
public class RefreshToken implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID refreshTokenId;
	
	@Column(nullable = false)
	private String token;
	
	@Column(nullable = false)
	private Instant expiryDate;
	
	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

	public RefreshToken() {
		
	}

	public RefreshToken(UUID refreshTokenId, String token, Instant expiryDate, User user) {
		this.refreshTokenId = refreshTokenId;
		this.token = token;
		this.expiryDate = expiryDate;
		this.user = user;
	}

	public UUID getRefreshTokenId() {
		return refreshTokenId;
	}

	public void setRefreshTokenId(UUID refreshTokenId) {
		this.refreshTokenId = refreshTokenId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Instant getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Instant expiryDate) {
		this.expiryDate = expiryDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public int hashCode() {
		return Objects.hash(expiryDate, refreshTokenId, token, user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RefreshToken other = (RefreshToken) obj;
		return Objects.equals(expiryDate, other.expiryDate) && Objects.equals(refreshTokenId, other.refreshTokenId)
				&& Objects.equals(token, other.token) && Objects.equals(user, other.user);
	}

}
